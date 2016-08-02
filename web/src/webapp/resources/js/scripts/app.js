require.config({
	baseUrl: "resources/js/scripts",
	paths: {
		jquery: "lib/jquery.min"
	},
	urlArgs: "bust=" + (new Date()).getTime()
});

define(function(require) {
	require("events");
	require("lib/eventBus");
	require("dto/userDto");
	require("dto/messageDto");
	require("dto/chatDto");
	require("service/remoteUserService");
	require("service/remoteChatService");
	require("uiComponents/registration");
	require("uiComponents/login");
	require("uiComponents/menu");
	require("uiComponents/chatRoom");
	require("chat");
	
	var chat = new ChatApp("chat-container").init();
});		