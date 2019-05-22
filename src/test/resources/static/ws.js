$(function() {
	var wsUrl = "ws://localhost:9000/ws";
	var socket;

	var isConnect = false;
	$('#connBtn').click(function() {
		if (isConnect) {
			isConnetct = false;
			$('#connBtn').text('Connect');
			socket.close();
		} else {
			isConnect = true;
			$('#connBtn').text('Disconnect');
			socket = new WebSocket(wsUrl);
			socket.onopen = function(evt) {
				console.log('open');
			};
			socket.onclose = function(evt) {
				isConnect = false;
				console.log('close');
			};
			socket.onmessage = function(evt) {
				$("#content").append($("<pre></pre>").text(evt.data));
				console.log(evt.data);
			};
		}
	});
			
	$('#sendBtn').click(function() {
		var msg = $('#msg').val();
		socket.send(msg);
	});
});
