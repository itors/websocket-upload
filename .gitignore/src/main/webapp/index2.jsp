<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<script type="text/javascript" src="./js/jquery.js"></script>
<script type="text/javascript" src="./js/websocket.js"></script>
<script type="text/javascript">
var commissionWebsocketUrl = "ws://localhost:8080/demo/websocket/";
var fileKey = "BBBBBBBB";
myWebSocket.webSocketUrl = commissionWebsocketUrl;
myWebSocket.batchKey = fileKey;
myWebSocket.initSocket();

function sendMsg(){
	var msg = $('#msg').val();
     myWebSocket.webSocket.onmessage = function(msg) {
     	$('#rceMsg').html(msg.data)
     };
	$.ajax({
	    type : "POST",
	    url : './test.action',
	    data : {'fileId':fileKey,'msg':msg},
	    dataType : "json",
	    beforeSend: function(xhr) {
	       
	    },
	    complete: function(){
	        
	    },
	    success : function(data) {
	    	//myWebSocket.closeSocket();
	    }
	});
}

</script>
</head>
<body>
<input type="text" id = 'msg' >
<input type="button" value="发送" onclick="sendMsg()" />
<div id = "rceMsg"></div>
</body>
</html>