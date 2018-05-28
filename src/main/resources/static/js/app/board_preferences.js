require(['jquery', 'datatables', 'app/csrf_helper', 'app/table_helper',
      'app/modal_helper', 'app/message_source'],
    function (jquery, datatables, csrfHelper, tableHelper, modalHelper,
        messageSource) {
      $(document).ready(function () {
        let tableEl = $('#preferencesTable');
        let table = tableEl.DataTable({
          "ajax": {
            "url": "/board-manager/board/" + board.urlHash + "/users",
            "dataSrc": ""
          },
          "selectable": true,
          "order": [[0, "asc"]],
          "selectors" : {},
          "columns": [
            {"data": "username"},
            {
              "data": "firstName",
              "render": function (i, type, row, meta) {
                return row.firstName + " " + row.lastName;
              }
            },
            {
              "data": "permission",
              "render": function (permission, type, row, meta) {
                return `<input type="hidden" value="${permission}"/>${messageSource.get(
                    permission)}`;
              }
            }
          ]
        });

        modalHelper.addToggleModalButtons('toggleModal');
        tableHelper.addSingleRowSelection(tableEl.attr('id'), table,
            (row) => {
              let permission = $('> td input', row).val();
              switch (permission) {
                case 'BOARD_OWNER' : {
                  $('#delete').attr('disabled', 'disabled');
                  $('#edit').attr('disabled', 'disabled');
                }
                  break;
                default: {
                  $('#delete').removeAttr('disabled');
                  $('#edit').removeAttr('disabled', 'disabled');
                }
              }
            }, () => {
              $('#delete').attr('disabled', 'disabled');
              $('#edit').attr('disabled', 'disabled');
            });

        $('#delete').click(function () {
          let boardRow = table.row('.selected');
          let user = table.row('.selected').data();
          $.ajax({
            url: `/board-manager/board/${board.urlHash}/user/${user.username}` ,
            method: "DELETE"
          }).done(data => {
            boardRow.remove().draw(false);
          });
        });

        $('#updateBoardDetailsButton').click(function () {
          let updateBoardDto = JSON.stringify({
            title: $('#title').val(),
            description: $('#description').val()
          });
          $.ajax({
            url: `/board-manager/board/${board.urlHash}` ,
            method: "PUT",
            contentType: "application/json",
            data: updateBoardDto
          }).done(data => {
            $('#title').val(data.title);
            $('#description').val(data.description)
          });
        });
        

        let closeModalAction = function () {
          let modal = $('#addModal');
          if (!modal.hasClass('is-active')) {
            modal.addClass('is-active')
          } else {
            modal.removeClass('is-active')
          }
        };

        $('#addModalCancel').click(closeModalAction);
        $('#addModalClose').click(closeModalAction);

        $('#addModalOk').click(function () {
          let setBoardRoleForUserDto = {
            role: $('#permission').val(),
            username: $('#username').val()
          };
          $.ajax({
            url: "/board-manager/board/" + board.urlHash + "/users",
            method: "POST",
            data: setBoardRoleForUserDto
          }).done(data => {
            table.ajax.reload();
            closeModalAction();
          }).fail(data => {

          });
        });


        $('#edit').click(function () {
          let user = table.row('.selected').data();

          $('#username').val(user.username);
          if (!$('#addModal').hasClass('is-active')) {
            $('#addModal').addClass('is-active')
          }
        });

      });
    });