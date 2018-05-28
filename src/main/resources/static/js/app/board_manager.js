require(['jquery', 'datatables', 'app/csrf_helper', 'app/table_helper',
      'app/modal_helper', 'app/message_source'],
    function (jquery, datatables, csrfHelper, tableHelper, modalHelper,
        messageSource) {
      $(document).ready(function () {
        let tableEl = $('#boardTable');
        let table = tableEl.DataTable({
          "ajax": {
            "url": "board-manager/boards",
            "dataSrc": ""
          },
          "selectable": true,
          "order": [[0, "asc"]],
          "columns": [
            {"data": "title"},
            {"data": "description"},
            {
              "data": "urlHash",
              "render": function (urlHash, type, row, meta) {
                return `<a href="${location.protocol}//${location.host
                + "/board/"
                + urlHash}">${urlHash}</a>`
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
                  $('#delete').removeAttr('disabled');
                  $('#preferences').removeAttr('disabled');
                }
                  break;
                case 'BOARD_ADMIN' : {
                  $('#delete').attr('disabled', 'disabled');
                  $('#preferences').removeAttr('disabled');
                }
                  break;
                default: {
                  $('#delete').attr('disabled', 'disabled');
                  $('#preferences').attr('disabled', 'disabled');
                }
              }
            }, () => {
              $('#delete').attr('disabled', 'disabled');
              $('#preferences').attr('disabled', 'disabled');
            });

        $('#preferences').click(() => {
          let boardRow = table.row('.selected');
          let board = table.row('.selected').data();
          location.href = tableHelper.getApiUrl('preferences',
              board.urlHash);
        });

        let closeModalAction = function () {
          let modal = $('#createModal');
          if (!modal.hasClass('is-active')) {
            modal.addClass('is-active')
          } else {
            modal.removeClass('is-active')
          }
        };

        $('#createModalCancel').click(closeModalAction);
        $('#createModalClose').click(closeModalAction);

        $('#createModalOk').click(function () {
          let createBoardDto = {
            title: $('#title').val(),
            description: $('#description').val()
          };
          $.ajax({
            url: tableHelper.getApiUrl('boards'),
            method: "POST",
            data: createBoardDto
          }).done(data => {
            table.ajax.reload();
            closeModalAction();
          }).fail(data => {

          });
        });

        $('#delete').click(function () {
          let boardRow = table.row('.selected');
          let board = table.row('.selected').data();
          $.ajax({
            url: tableHelper.getApiUrl('board', board.urlHash),
            method: "DELETE"
          }).done(data => {
            boardRow.remove().draw(false);
          });
        });

      });
    });