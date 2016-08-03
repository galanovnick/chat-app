if (typeof define !== 'function') {
    events = require('events');
}

var ChatService = function(_eventBus) {

    var _createChat = function(newChatData) {
        $.ajax({
            type: "post",
            data: {
                token: newChatData.token,
                chatName: newChatData.chatDto.title
            },
            url: "/api/chat/new",
            dataType: "json",
            jsonp: false,
            success: function(response) {
                _eventBus.post(response.chats, events.roomSuccessfullyCreatedEvent);
            },
            error: function(response) {
                response = JSON.parse(response.responseText);
                _eventBus.post(response.message, events.roomCreationFailedEvent);
            }
        });
    };

    var _onUserJoined = function(userRequestData) {
        $.ajax({
            type: "post",
            data: {
                chatId: userRequestData.chatId,
                token: userRequestData.token
            },
            url: "/api/chat/join",
            dataType: "json",
            jsonp: false,
            success: function(response) {
                _eventBus.post({
                    chatId: response.chatId,
                    chatName: response.chatName
                }, events.userSuccessfullyJoinedEvent);
            },
            error: function(response) {
                response = JSON.parse(response.responseText);
                _eventBus.post(response.message, events.failedRoomJoinEvent);
            }
        });
    };

    var _onUserLeft = function(userRoomInfo) {
        $.ajax({
            type: "delete",
            url: "/api/chat/leave?" +
                "token=" + userRoomInfo.token +
                "&chatId=" + userRoomInfo.chatId,
            dataType: "json",
            jsonp: false,
            success: function() {
                _eventBus.post(userRoomInfo.chatId, events.userSuccessfullyLeftEvent);
            }
        });
    };

    var _getAllChats = function(token) {
        $.ajax({
            type: "get",
            url: "/api/chat?token=" + token,
            dataType: "json",
            jsonp: false,
            success: function(response) {
                _eventBus.post(response.chats, events.chatListProvidedEvent);
            }
        });
    };

    var _onMessageAdded = function(sendMessageData) {
        $.ajax({
            type: "post",
            data: {
                token: sendMessageData.token,
                chatId: sendMessageData.message.chatId,
                message: sendMessageData.message.text
            },
            url: "/api/chat/add-message",
            dataType: "json",
            jsonp: false,
            success: function(response) {
                _eventBus.post(
                    {
                        roomId: response.roomId,
                        messages: response.messages
                    }, events.messageSuccessfullyAddedEvent);
            },
            error: function(response) {

            }
        });
    };

    var _getAllMessages = function(messagesData) {
        $.ajax({
            type: "get",
            url: "/api/chat/messages?" +
                "token=" + messagesData.token +
                "&chatId=" + messagesData.chatId,
            dataType: "json",
            jsonp: false,
            success: function(response) {
                _eventBus.post({
                    messages: response.messages,
                    chatId: messagesData.chatId
                }, events.messageListProvidedEvent);
            }
        });
    };

    return {
        "onRoomAdded": _createChat,
        "onChatListRequested": _getAllChats,
        "onUserJoined": _onUserJoined,
        "onMessageAdded": _onMessageAdded,
        "onMessageListRequested": _getAllMessages,
        "onUserLeft": _onUserLeft
    }
};

if (typeof define !== 'function') {
    var define = require('amdefine')(module);
}

define(function() {
    return ChatService;
});