require(['jquery', 'datatables', 'app/csrf_helper', 'app/table_helper',
  'app/modal_helper'],
    function (jquery, datatables, csrfHelper, tableHelper, modalHelper) {

  $(document).ready(function () {
    let tableEl = $('#userTable');
    let table = tableEl.DataTable({
      "ajax": {
        "url": "user-manager/users",
        "dataSrc": ""
      },
      "selectable": true,
      "order": [[0, "asc"]],
      "columns": [
        {"data": "username"},
        {"data": "firstName"},
        {"data": "lastName"},
        {"data": "email"},
        {
          "data": "active",
          "render": function (isActive, type, row, meta) {
            let icon = isActive === true ? " is-danger fa-check-circle"
                : "fa-times-circle is-success";
            return `<span class="icon"><i class="fas ${icon}"></i></span>`;
          }
        }
      ]
    });

    modalHelper.addToggleModalButtons('toggleModal');
    tableHelper.addSingleRowSelection(tableEl.attr('id'), table);

    $('#delete').click(function () {
      let userRow = table.row('.selected');
      let user = table.row('.selected').data();
      $.ajax({
        url: tableHelper.getApiUrl('user', user.username),
        method: "DELETE",
      }).done(data => {
        userRow.remove().draw(false);
      })
    });

    $('#toggleActivation').click(function () {
      let userRow = table.row('.selected');
      let user = table.row('.selected').data();
      $.ajax({
        url: tableHelper.getApiUrl('user', user.username),
        method: "PUT",
      }).done(data => {
        table.ajax.reload();
      })
    });

  });
});