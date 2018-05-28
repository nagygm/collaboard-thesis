require(['app/board', 'app/socket', 'app/csrf_helper', 'jquery'],
    function (board, socket, csrf) {

      let serverUrl = window.location.origin;
      let commandMap = new Map();

      let request = new XMLHttpRequest();
      request.open('GET', urlHash + '/init', true);
      request.setRequestHeader(csrf.header, csrf.token);

      request.onload = function () {
        if (request.status >= 200 && request.status < 400) {
          // Success!
          let resp = request.responseText;
          let objects = JSON.parse(resp).objects;
          for (let i = 0; i < objects.length; i++) {
            objects[i].type = removeFabricPrefix(objects[i].type);
          }
          board.initializeCanvasFromMap(objects);
          // board.loadIntoCanvas(JSON.parse(resp));
        } else {
          // We reached our target server, but it returned an error
        }
      };

      request.onerror = function () {
        // There was a connection error of some sort
      };

      let socketOptions = {
        connectionString: serverUrl + '/whiteboard',
        readChannel: '/topic/board/' + urlHash,
        writeChannel: '/collaboard/' + urlHash
      };

      socket.connect(socketOptions, (message) => {
        let command = JSON.parse(message.body);
        switch (command.type) {
          case 'fabric_create':
            board.processCreate(
                translateBoardObjectToFabric(command.boardObject));
            break;
          case 'fabric_delete' :
            board.finalizeDelete(command.objectId);
            break;
          case 'fabric_select' :
            board.finalizeSelect(command.objectId);
            break;
          case 'fabric_deselect' :
            board.finalizeDeselect(command.objectId);
            break;
          case 'fabric_update' :
            board.finalizeUpdate(
                translateBoardObjectToFabric(command.boardObject));
            break;
          case 'fail' :
            console.log(command);
            break;
          default:
            console.log(message.body);
        }
        commandMap.delete(command.id);
      }, () => {
        request.send();
      });

      function translateBoardObjectToFabric(boardObject) {
        boardObject.type = removeFabricPrefix(boardObject.type);
        return boardObject;
      }

      function removeFabricPrefix(type) {
        return type.split("_", 2)[1];
      }

      function addFabricPrefix(type) {
        return 'fabric_' + type;
      }

      if (!readOnly) {
        board.onCreate(function (object) {
          let command = new CreateCommand(object);
          commandMap.set(command.id, command);
          socket.send(socketOptions.writeChannel, {priority: 9},
              command);
        });

        board.onUpdate(function (object) {
          let command = new UpdateCommand(object);
          commandMap.set(command.id, command);
          socket.send(socketOptions.writeChannel, {priority: 9},
              command);
        });

        board.onDelete(function (object) {
          let command = new DeleteCommand(object);
          commandMap.set(command.id, command);
          socket.send(socketOptions.writeChannel, {priority: 9},
              command);
        });

        board.onSelect(function (object) {
          let command = new SelectCommand(object);
          commandMap.set(command.id, command);
          socket.send(socketOptions.writeChannel, {priority: 9},
              command);
        });

        board.onDeselect(function (object) {
          let command = new DeselectCommand(object);
          commandMap.set(command.id, command);
          socket.send(socketOptions.writeChannel, {priority: 9},
              command);
        });
      }

      function CreateCommand(object) {
        Command.call(this, 'fabric_create');
        this.boardObject = object;
        this.boardObject.type = addFabricPrefix(this.boardObject.type);
        return this;
      }

      function UpdateCommand(object) {
        Command.call(this, 'fabric_update');
        this.boardObject = object;
        this.boardObject.type = addFabricPrefix(this.boardObject.type);
        return this;
      }

      function DeleteCommand(object) {
        Command.call(this, 'fabric_delete');
        this.objectId = object.id;
        return this;
      }

      function SelectCommand(object) {
        Command.call(this, 'fabric_select');
        this.objectId = object.id;
        return this;
      }

      function DeselectCommand(object) {
        Command.call(this, 'fabric_deselect');
        this.objectId = object.id;
        return this;
      }

      //TODO proper command pattern here
      function Command(type) {
        // this.jsonRender = function (key ,value) {
        //   if(key.startsWith('_')) {
        //     return undefined;
        //   } else {
        //     return value;
        //   }
        // };
        this.type = type;
        this.id = generateId();
        return this;
      }

      function generateId() {
        //TODO more unique
        let id = genId();
        while (commandMap.has(id)) {
          id = genId()
        }

        function genId() {
          return username + '#' + Math.floor((Math.random() * 10000)
              + 1).toString();
        }

        return id;
      }

    });
