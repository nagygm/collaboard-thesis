define(['jquery'], function () {
  function addToggleModalButtons(prefix) {
    $('[id*=' + prefix + ']').click(function () {
      let modalId = $(this).attr('id').slice(prefix.length).toLowerCase()+ 'Modal';
      let modal = $('#'+modalId);
      if ( !modal.hasClass('is-active') ) {
        modal.addClass('is-active')
      }
    });
  }


  return {
    addToggleModalButtons : addToggleModalButtons
  }
});