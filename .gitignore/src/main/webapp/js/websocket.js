var myWebSocket = (function() {
    var mySocket = {};
    mySocket.tryTime = 0;
    mySocket.webSocketUrl = null;
    mySocket.batchKey = null;
    mySocket.webSocket = null;
    mySocket.initSocket = function() {
        if (!window.WebSocket) {
            // alert("Your browsers don't support websocket.");
            return false;
        }
        mySocket.webSocket = new WebSocket(mySocket.webSocketUrl + mySocket.batchKey);
        mySocket.webSocket.onmessage = function(msg) {
            console.log(msg);
        };
        mySocket.webSocket.onerror = function(event) {
            console.log(event);
        };
        mySocket.webSocket.onopen = function(event) {
            console.log(event);
        };
        mySocket.webSocket.onclose = function() {
            if (mySocket.tryTime < 10) {
                setTimeout(function() {
                    webSocket = null;
                    mySocket.tryTime++;
                    mySocket.initSocket();
                }, 500);
            } else {
                mySocket.tryTime = 0;
            }
        };
    };
    mySocket.closeSocket = function() {
        if (mySocket.webSocket) {
            mySocket.webSocket.close();
        }
    };
    return mySocket;
})();