define(['stomp-websocket', 'sockjs-client', 'app/csrf_helper'],
    function (stomp_undefined, SockJS, csrf) {
      //stomp websocket js liblary doesnt work with requirejs
      // let websocketUrl = $('websocketUrl').value || window.location.href;
      let Stomp = window.Stomp;
      let stompClient;

      function getHeaders() {
        let headerName = csrf.header;
        let token = csrf.token;
        let headers = {};
        headers[headerName] = token;
        return headers;
      }

      function connect(options, subscribeCallback, aditionalInitCallback) {
        let ws = new SockJS(options.connectionString);
        stompClient = new Stomp.over(ws);

        function stompErrorHandler() {
          setTimeout(connect(options.connectionString, subscribeCallback, aditionalInitCallback), 1000);
        }

        stompClient.connect(getHeaders(), function () {
          aditionalInitCallback && aditionalInitCallback();
          stompClient.subscribe(options.readChannel, subscribeCallback);
          stompClient.onclose = stompErrorHandler;
        });
      }

      function send(target, options, object) {
        stompClient.send(target, options, JSON.stringify(object));
      }

      return {
        connect: connect,
        send: send
      }
    }
);