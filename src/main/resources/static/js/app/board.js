define(['fabric', 'jquery'], function (fabric, jquery) {

  let tempIdSet = new Set();
  let alreadyExecuted = new Set();
  // todo seek array
  let seekArray = [];

  let boardCanvas = (function () {
    let canvas;

    //TODO readonly property of board
    if(readOnly) {
      canvas = new fabric.StaticCanvas('canvas', {
        isDrawingMode: false,
        selection: false
      });
    } else {
      canvas = new fabric.Canvas('canvas', {
        isDrawingMode: false,
        selection: false
      });
    }

    //Canvas resize
    window.addEventListener('resize', resizeCanvas, false);

    function resizeCanvas() {
      canvas.setHeight(window.innerHeight);
      canvas.setWidth(window.innerWidth);
      canvas.calcOffset();
      canvas.renderAll();
    }

    resizeCanvas();

    //Zoom, TODO limit movemenet to max canvas area
    canvas.on('mouse:wheel', function (opt) {
      let delta = opt.e.deltaY;
      let zoom = boardCanvas.getZoom();
      zoom = zoom - (delta / 400) * zoom;
      if (zoom > 20) {
        zoom = 20;
      }
      if (zoom < 0.1) {
        zoom = 0.1;
      }
      canvas.zoomToPoint({x: opt.e.offsetX, y: opt.e.offsetY}, zoom);
      opt.e.preventDefault();
      opt.e.stopPropagation();
      canvas.calcOffset();
    });
    return canvas;
  })();

  /**
   * Fabric specific settings
   */
  (function () {
    fabric.Object.prototype.transparentCorners = false;

    //To be able to serialize as even the images svg

    fabric.Image.prototype.getSvgSrc = function () {
      return this.toDataURLforSVG();
    };

    fabric.Image.prototype.toDataURLforSVG = function (options) {
      let el = fabric.util.createCanvasElement();
      el.width = this._element.naturalWidth;
      el.height = this._element.naturalHeight;
      el.getContext("2d").drawImage(this._element, 0, 0);
      let data = el.toDataURL(options);
      return data;
    };
  })();

  function serialize(type) {
    switch (type) {
      case 'png' : {
        return boardCanvas.toDataURL('png');
      }
      case 'svg' : {
        let svg = boardCanvas.toSVG();
        return svg;
      }
      case 'svg-base64' : {
        let svg = boardCanvas.toSVG();
        let svgbase64 = window.btoa(svg);
        return svgbase64;
      }
      default :
        return boardCanvas.toDatalessJSON();
    }
  }

  (function () {
    $('#pencil-mode').click(function () {
      if ($('#pencil-mode').hasClass('is-outlined')) {
        boardCanvas.isDrawingMode = true;
        boardCanvas.isDragging = false;
        $('.options').removeClass('is-invisible');
        $('#pencil-mode').removeClass('is-outlined');
        $('#move-mode').addClass('is-outlined');
        $('#text-mode').addClass('is-outlined');
      }
      else {
        $(".options").addClass('is-invisible');
        $('#pencil-mode').addClass('is-outlined');
        boardCanvas.isDrawingMode = false;
      }
    });

    $('#move-mode').click(() => {
      if ($('#move-mode').hasClass('is-outlined')) {
        $('#text-mode').addClass('is-outlined');
        $('#pencil-mode').addClass('is-outlined');
        $(".options").addClass('is-invisible');
        $('#move-mode').removeClass('is-outlined');
        boardCanvas.isDrawingMode = false;
      }
      else {
        $('#move-mode').addClass('is-outlined');
        boardCanvas.isDragging = false;
      }
    });

    $('#text-mode').click(() => {
      if ($('#text-mode').hasClass('is-outlined')) {
        $('#text-mode').removeClass('is-outlined');
        $('#move-mode').addClass('is-outlined');
        $('#pencil-mode').addClass('is-outlined');
        $(".options").addClass('is-invisible');
        boardCanvas.isDragging = false;
        boardCanvas.isDrawingMode = false;
      }
      else {
        $('#text-mode').addClass('is-outlined')
      }
    });

    boardCanvas.on('mouse:down', function (opt) {
      let evt = opt.e;
      if (!$('#move-mode').hasClass('is-outlined')) {
        boardCanvas.isDragging = true;
        boardCanvas.lastPosX = evt.clientX;
        boardCanvas.lastPosY = evt.clientY;
      } else if ($('.toolbox').has('#text-mode') && !$('#text-mode').hasClass('is-outlined')) {
        //TODO to function and mode instead
        let ImCanvas = fabric.util.invertTransform(
            boardCanvas.viewportTransform);
        let p = fabric.util.transformPoint(
            new fabric.Point(evt.clientX, evt.clientY), ImCanvas);
        let itext = new fabric.IText('Szerkeszthető szöveges objektum', {
          left: p.x,
          top: p.y,
          fill: '#367cd8',
          strokeWidth: 2,
          stroke: "#367cd8"
        });
        boardCanvas.add(itext);
      }
    });

    boardCanvas.on('mouse:move', function (opt) {
      if (boardCanvas.isDragging) {
        let e = opt.e;
        let viewportTransform = this.viewportTransform;
        viewportTransform[4] += e.clientX - this.lastPosX;
        viewportTransform[5] += e.clientY - this.lastPosY;
        boardCanvas.lastPosX = e.clientX;
        boardCanvas.lastPosY = e.clientY;
        boardCanvas.setViewportTransform(viewportTransform);
      }
    });

    boardCanvas.on('mouse:up', function (opt) {
      boardCanvas.isDragging = false;
      boardCanvas.forEachObject((o) => {
        o.setCoords();
      });
      boardCanvas.calcOffset();
      boardCanvas.calcViewportBoundaries();
      boardCanvas.renderOnAddRemove && this.requestRenderAll();
    });

    $('#drawing-color').change(function () {
      boardCanvas.freeDrawingBrush.color = this.value;
    });

    $('#drawing-line-width').change(function () {
      boardCanvas.freeDrawingBrush.width = parseInt(this.value, 10) || 1;
      this.previousSibling.innerHTML = this.value;
    });

    if (boardCanvas.freeDrawingBrush) {
      boardCanvas.freeDrawingBrush.color = $('#drawing-color').val();
      boardCanvas.freeDrawingBrush.width = parseInt($('#drawing-line-width').value,
          10) || 1;
    }

    $('#saveToPng').click(() => {
      let pngBase64 = serialize('png');
      let hiddenPngTag = $('#pngDownload');
      let fileName = `${boardTitle}_${new Date().toLocaleString()}.png`;
      hiddenPngTag.attr('href', pngBase64);
      hiddenPngTag.attr('download', fileName);
      hiddenPngTag[0].click();
    });

    $('#saveToSvg').click(() => {
      let svg = serialize('svg');
      let fileName = `${boardTitle}_${new Date().toLocaleString()}.svg`;
      let hiddenSvgTag = $('#svgDownload');
      hiddenSvgTag.attr('href', 'data:image/svg+xml;utf8,' + svg);
      hiddenSvgTag.attr('download', fileName);
      hiddenSvgTag[0].click();
    });

    $('.canvas-outer').keydown((event) => {
      //46 is for delete
      if (event.keyCode == 46) {
        event.preventDefault();
        boardCanvas.remove(boardCanvas.getActiveObject());
      }
    })
  })();
  let reactToChangesInCanvas = true;

  function onChange(createCallback) {
    let events = ['object:modified', 'object:added', 'object:removed'];
    let pathEvents = ['path:created', 'path:removed'];
    let fastEvents = ['object:moving', 'object:scaling', 'object:rotating',
      'object:skewing'];
    let allEvents = events.concat(fastEvents);
    for (let i = 0; i < events.length; i++) {
      addCallback(events[i])
    }

    function addCallback(eventName) {
      let callback = createCallback(eventName);

      boardCanvas.on(eventName, callbackWrapper);

      function callbackWrapper() {
        if (reactToChangesInCanvas) {
          callback.apply(callback, arguments)
        }
      }
    }
  }

  //TODO local utility, instead use apply or call
  function indexOfWithAttr(array, attr, value) {
    for (let i = 0; i < array.length; i += 1) {
      if (array[i][attr] === value) {
        return i;
      }
    }
    return -1;
  }

  function generateId(map) {
    let id = genId();
    while (map.has(id)) {
      id = genId()
    }

    function genId() {
      return username + 'ß' + Math.floor((Math.random() * 10000)
          + 1).toString();
    }

    return id;
  }

  function onCreate(callback) {
    let event = 'object:added';
    boardCanvas.on(event, (options) => {
      if (!options.target.id) {
        let tempId = generateId(tempIdSet);
        options.target.tempId = tempId;
        tempIdSet.add(tempId);
        callback(options.target.toObject(['tempId']))
      }
    })
  }

  function processCreate(object) {
    if (tempIdSet.has(object.tempId)) {
      let i = indexOfWithAttr(boardCanvas.getObjects(), 'tempId',
          object.tempId);
      if (i >= 0) {
        boardCanvas.getObjects()[i].id = object.id;
        tempIdSet.delete(object.tempId);
      }
    } else {
      loadObjectIntoCanvas(object);
    }
  }

  function onDelete(callback) {
    let event = 'object:removed';

    boardCanvas.on(event, (options) => {
      if (!alreadyExecuted.has(options.target.tempId) && options.target.id) {
        callback(options.target.toObject(['id']))
      }
      alreadyExecuted.delete(options.target.tempId);
    });
  }

  function finalizeDelete(id) {
    let i = indexOfWithAttr(boardCanvas.getObjects(), 'id',
        id);
    if (i >= 0) {
      let tempId = generateId(alreadyExecuted);
      boardCanvas.getObjects()[i].tempId = tempId;
      alreadyExecuted.add(tempId);
      boardCanvas.remove(boardCanvas.getObjects()[i]);
      boardCanvas.renderAll();
    }
  }

  function onUpdate(callback) {
    let events = ['object:modified'];

    applyCallback(events[0]);

    function applyCallback(event) {
      boardCanvas.on(event, (options) => {
        if (options.target.id) {
          callback(options.target.toObject(['id']))
        }
        // alreadyExecuted.delete(options.target.tempId);
      })
    }
  }

  function finalizeUpdate(object) {
    let i = indexOfWithAttr(boardCanvas.getObjects(), 'id',
        object.id);
    if (i >= 0) {
      // object.tempId = generateId(alreadyExecuted);
      // alreadyExecuted.add(object.tempId);

      fabric.util.enlivenObjects([object], (oArray) => {
        // boardCanvas.getObjects()[i] = oArray[0];
        // boardCanvas.getObjects()[i].set();
        fabric.util.object.extend(boardCanvas.getObjects()[i], oArray[0]);
        boardCanvas.renderAll();
      }, 'fabric', () => {
      });
    }
  }

  function onSelect(callback) {
    let event = 'selection:created';

    boardCanvas.on(event, (options) => {
      // $('.canvas-outer').focus();
      // callback(options.target.toObject(['id']));
    });

  }

  function finalizeSelect() {

  }

  function onDeselect() {
    let event = "selection:cleared";

    boardCanvas.on(event, (options) => {
      // $('.canvas-outer').focus();
      // callback(options.target.toObject(['id']));
    });
  }

  function finalizeDeselect() {

  }

  function finalizeFail() {

  }

  function loadIntoCanvas(boardJson) {
    reactToChangesInCanvas = false;
    boardCanvas.loadFromJSON(
        boardJson,
        boardCanvas.renderAll.bind(boardCanvas)
    );
  }

  function loadObjectIntoCanvas(jsonObject) {
    fabric.util.enlivenObjects([jsonObject], (objects) => {
      reactToChangesInCanvas = false;
      let origRenderOnAddRemove = boardCanvas.renderOnAddRemove;
      boardCanvas.renderOnAddRemove = false;
      objects.forEach(o => boardCanvas.add(o));
      boardCanvas.renderOnAddRemove = origRenderOnAddRemove;
      boardCanvas.renderAll();
    }, 'fabric', () => {
    });

  }

  /**
   * init canvas object from map, and add all array indexes to our boarid index map
   * @param jsonObjects
   */
  function initializeCanvasFromMap(jsonObjects) {
    fabric.util.enlivenObjects(jsonObjects, (objects) => {
      reactToChangesInCanvas = false;
      let origRenderOnAddRemove = boardCanvas.renderOnAddRemove;
      boardCanvas.renderOnAddRemove = false;
      objects.forEach(o => {
        let index = boardCanvas.getObjects().length;
        boardCanvas.insertAt(o, index, true);
        seekArray[index] = o.id;
      });

      boardCanvas.renderOnAddRemove = origRenderOnAddRemove;
      boardCanvas.renderAll();
    }, 'fabric', () => {
    });
  }

  boardCanvas.on('after:render', () => reactToChangesInCanvas = true);

  return {
    onCreate: onCreate,
    onDelete: onDelete,
    onUpdate: onUpdate,
    onSelect: onSelect,
    onDeselect: onDeselect,
    serialize: serialize,
    processCreate: processCreate,
    finalizeDelete: finalizeDelete,
    finalizeUpdate: finalizeUpdate,
    finalizeSelect: finalizeSelect,
    finalizeDeselect: finalizeDeselect,
    finalizeFail: finalizeFail,
    loadIntoCanvas: loadIntoCanvas,
    initializeCanvasFromMap: initializeCanvasFromMap
  };
});