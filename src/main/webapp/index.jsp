<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>websocket 实时进度条上传sftp</title>
<script type="text/javascript" src="./js/jquery.js"></script>
<script type="text/javascript" src="./js/websocket.js"></script>
<script type="text/javascript" src="./js/js-md5.js"></script>
<script type="text/javascript">
	var commissionWebsocketUrl = "ws://localhost:8080/websocket-upload/websocket/";
	fileWebSocket.webSocketUrl = commissionWebsocketUrl;
	var fileKey = "";
	function sendMsg() {
		$('#div3').html("正在计算，请稍等。。。")
		//获取文件
		var file = document.getElementById('file').files[0];
		//var file = $('#file')[0]
		if (file) {
			calculate(file, function(fileMd5) {
				fileKey = fileMd5;
				fileWebSocket.fileMD5 = fileMd5;
				fileWebSocket.initSocket();
				fileWebSocket.webSocket.onmessage = function(msg) {
					setPercent(msg);
				};
				uploadFile(fileMd5,file)
			})
		} else {
			console.log("请选择文件")
			return;
		}
	}
	function uploadFile(fileKey,file){
		var formFile = new FormData();
        formFile.append("fileKey", fileKey);  
        formFile.append("file", file); //加入文件对象
        console.log(new Date())
		$.ajax({
			type : "POST",
			url : './upload1.action',
			data : formFile,
			/**
             *必须false才会自动加上正确的Content-Type
             */
             contentType: false,
             /**
             * 必须false才会避开jQuery对 formdata 的默认处理
             * XMLHttpRequest会对 formdata 进行正确的处理
             */
            processData: false,
			beforeSend : function(xhr) {

			},
			complete : function() {
				fileWebSocket.closeSocket();
			},
			success : function(data) {
				if(data.resultFlag){
					if(data.resultFlag=='100001'){
						$('#div2').width(300)
						$('#div3').html('100%')
					}
				}
				alert(data.msg)
			}, 
			error: function(){
				alert("上传失败！")
			}
		});
	}
	function suspendUpload(){
		fileWebSocket.webSocket.send('suspend');
	}
	function setPercent(msg){
		$('#div2').width(3 * Number(msg.data.replace(/%/, "")))
		$('#div3').html(msg.data)
	}
</script>
<style>
#div1 {width: 300px; height: 30px; border: 1px solid #000; position: relative;}
#div2 {width: 0; height: 30px; background: #CCC;}
#div3 {width: 300px; height: 30px; line-height: 30px; text-align: center; position: absolute; left: 0; top: 0;}
</style>
</head>
<body>
	<input type="file" id="file" />
	<input type="button" id="btn" value="上传" onclick="sendMsg()"/>
	<input type="button" id="btnSuspend" value="暂停" onclick="suspendUpload()"/>
	 <!-- 进度条 -->
    <div id="div1">
        <div id="div2"></div>
        <div id="div3">0%</div>
    </div>
</body>
</html>