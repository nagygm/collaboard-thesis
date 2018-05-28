define(['jquery'], function() {
  let token = $("meta[name='_csrf']").attr("content");
  let header = $("meta[name='_csrf_header']").attr("content");

  $.ajaxPrefilter(function( options ) {
    if ( !options.beforeSend) {
      options.beforeSend = function (xhr) {
        xhr.setRequestHeader(header, token);
      }
    }
  });
  return {
    token : token,
    header : header
  };
});