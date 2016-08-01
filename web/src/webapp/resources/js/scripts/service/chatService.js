if (typeof define !== 'function') {
	events = require('events');
}

var ChatService = function(_eventBus, _storage) {

	var generateRandomId = function() {
		var result = "";
    	var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	    for( var i=0; i < 10; i++ ) {
	        result += possible.charAt(Math.floor(Math.random() * possible.length));
	    }

		return result;
	}

	var _createRoom = function(chatRoom) {
		chatRoom.title = chatRoom.title.replace(/ +/g, ' ');
		if (chatRoom.title === '') {
			_eventBus.post("Chat name cannot be empty.", events.roomCreationFailedEvent);
		} else if(chatRoom.title.indexOf(' ') === 0) {
			_eventBus.post("Chat name cannot contain whitspace as first symbol.", events.roomCreationFailedEvent);
		} else if(chatRoom.title.search(/[^a-z0-9 ]/i) !== -1) {
			_eventBus.post("Chat name can contain only letters, digits and whitspaces.", events.roomCreationFailedEvent);
		} else if(isChatExists(chatRoom.title)) {
			_eventBus.post("Chat with such name already exists.", events.roomCreationFailedEvent);
		} else {

			_storage.addItem("chats", chatRoom);
			_eventBus.post(chatRoom.title, events.roomSuccessfullyCreatedEvent);
		}
	}

	var randomIdChatPrefix = generateRandomId();

	var _onUserJoined = function(userRequestData) {
		var chatRoomTitle = userRequestData.title;
		if (chatRoomTitle === '') {
			_eventBus.post("Chat name cannot be empty.", events.failedRoomJoinEvent);
		} else if(!isChatExists(chatRoomTitle)) {
			_eventBus.post("Chat with such name does not exist.", events.failedRoomJoinEvent);
		} else if(isUserJoined(userRequestData.username, chatRoomTitle)) {
			_eventBus.post("You cannot join chat room '" + chatRoomTitle + "' twice.", events.failedRoomJoinEvent);
		} else {
			_storage.addItem(randomIdChatPrefix + chatRoomTitle, userRequestData.username);
			_eventBus.post(chatRoomTitle, events.userSuccessfullyJoinedEvent);
		}
	}

	var _onUserLeft = function(userRoomInfo) {
		_storage.removeItem(randomIdChatPrefix + userRoomInfo.roomId, userRoomInfo.username);
		_eventBus.post(userRoomInfo.roomId, events.userSuccessfullyLeftEvent);
	}

	var isChatExists = function(chatRoomTitle) {
		var chats = _storage.getItems("chats");
		var isChatExists = false;
		chats.forEach(function(item) {
			if (item.title === chatRoomTitle) {
				isChatExists = true;
				return;
			}
		});
		return isChatExists;
	}

	var isUserJoined = function(username, chatRoomTitle) {
		var usersInChat = _storage.getItems(randomIdChatPrefix + chatRoomTitle);

		return usersInChat.indexOf(username) > -1;
	}

	var _getAllRooms = function() {
		return _storage.getItems("chats");
	}

	var randomIdMessagePrefix = generateRandomId();

	var _onMessageAdded = function(message) {

		if (typeof message.text === 'undefined' || message.text === null || message.text === "") {

			_eventBus.post({roomId: message.roomId, text: "Message text cannot be empty."}, events.messageAdditionFailedEvent);
		} else if(!isUserJoined(message.username, message.roomId)) {
			_eventBus.post({roomId: message.roomId, text: "User '" + message.username + "' not in this chat."}, events.messageAdditionFailedEvent);
		} else {
			_storage.addItem(randomIdMessagePrefix + message.roomId, message);

			_eventBus.post({roomId: message.roomId, messages: _storage.getItems(randomIdMessagePrefix + message.roomId)}, events.messageSuccessfullyAddedEvent);
		}
	}

	var _getAllMessages = function(roomId) {
		return _storage.getItems(randomIdMessagePrefix + roomId);
	}

	return {
		"onRoomAdded": _createRoom,
		"getAllRooms": _getAllRooms,
		"onUserJoined": _onUserJoined,
		"onMessageAdded": _onMessageAdded,
		"getAllMessages": _getAllMessages,
		"onUserLeft": _onUserLeft
	}
}

if (typeof define !== 'function') {
    var define = require('amdefine')(module);
}

define(function() {
	return ChatService;
});