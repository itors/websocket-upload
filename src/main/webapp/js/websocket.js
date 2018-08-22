var fileWebSocket = (function() {
    var fileWebSocket = {};
    fileWebSocket.tryTime = 0;
    fileWebSocket.webSocketUrl = null;
    fileWebSocket.fileMD5 = null;
    fileWebSocket.webSocket = null;
    fileWebSocket.initSocket = function() {
        if (!window.WebSocket) {
            // alert("Your browsers don't support websocket.");
            return false;
        }
        fileWebSocket.webSocket = new WebSocket(fileWebSocket.webSocketUrl + fileWebSocket.fileMD5);
        fileWebSocket.webSocket.onmessage = function(msg) {
            console.log(msg);
        };
        fileWebSocket.webSocket.onerror = function(event) {
            console.log(event);
        };
        fileWebSocket.webSocket.onopen = function(event) {
            console.log(event);
        };
        fileWebSocket.webSocket.onclose = function() {
        	// 此处是重试机制  暂时不需要
//            if (mySocket.tryTime < 10) {
//                setTimeout(function() {
//                    webSocket = null;
//                    mySocket.tryTime++;
//                    mySocket.initSocket();
//                }, 500);
//            } else {
//                mySocket.tryTime = 0;
//            }
        };
    };
    fileWebSocket.closeSocket = function() {
        if (fileWebSocket.webSocket) {
        	fileWebSocket.webSocket.close();
        }
    };
    return fileWebSocket;
})();