define(['jquery', 'datatables'], function () {

  function addSingleRowSelection(tableId, table, onSelectCallback, onDeselect) {
    
    $('#' + [tableId] + ' tbody').on('click', 'tr', function () {
      if ($(this).hasClass('selected')) {
        $(this).removeClass('selected');
        onDeselect && onDeselect();
      }
      else {
        table.$('tr.selected').removeClass('selected');
        $(this).addClass('selected');
        onSelectCallback && onSelectCallback($(this));
      }
    });
  }

  function getApiUrl(resource, resourceId) {
    let apiUrl = window.location.pathname;
    return `${apiUrl}/${resource}${resourceId ? '/' + resourceId : "" }`;
  }

  return {
    addSingleRowSelection : addSingleRowSelection,
    getApiUrl : getApiUrl
  }
});