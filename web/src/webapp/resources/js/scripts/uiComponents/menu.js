var UserMenuComponent = function(_componentRootId, _rootId, _eventBus) {
    var _chatService = new ChatService(_eventBus);

    var _init = function() {
        _eventBus.subscribe(events.messageAddedEvent, _chatService.onMessageAdded);
        _eventBus.subscribe(events.createRoomButtonClickedEvent, _chatService.onRoomAdded);
        _eventBus.subscribe(events.roomSuccessfullyCreatedEvent, _onRoomSuccessfullyCreated);
        _eventBus.subscribe(events.roomCreationFailedEvent, _onRoomCreationFailed);
        _eventBus.subscribe(events.joinRoomButtonClickedEvent, _chatService.onUserJoined);
        _eventBus.subscribe(events.userSuccessfullyJoinedEvent, _onUserSuccessfullyJoined);
        _eventBus.subscribe(events.failedRoomJoinEvent, _onRoomCreationFailed);
        _eventBus.subscribe(events.leaveRommButtonClickedEvent, _chatService.onUserLeft);
        _eventBus.subscribe(events.chatListProvidedEvent, _render);
        _eventBus.subscribe(events.chatListRequestedEvent, _chatService.onChatListRequested);
        _eventBus.subscribe(events.messageListRequestedEvent, _chatService.onMessageListRequested);

        _eventBus.post($("#u-token").val(), events.chatListRequestedEvent);
    };

    var _render = function(chats) {
        $('#' + _rootId + " .menu-content").mustache('user-menu-template',
            {
                id: _componentRootId,
                username: $("#u-name").val(),
                chats: chats
            }, {method: 'html'});

        if (chats.length < 1) {
            $("#" + _componentRootId + " .join-room-elem").hide();
        }
        $("#" + _componentRootId + " .new-room").click(function() {
            _eventBus.post({
                    chatDto: new ChatDto($("#" + _componentRootId + " .room-name").val()),
                    token: $("#u-token").val()
                },
                events.createRoomButtonClickedEvent);
        });

        $("#" + _componentRootId + " .join-room").click(function() {
            _eventBus.post(
                {
                    token: $("#u-token").val(),
                    chatId: $("#" + _componentRootId + " .room-names").val()
                },
                events.joinRoomButtonClickedEvent);
        });
    };

    var _onRoomCreationFailed = function(message) {
        $("#" + _componentRootId + " .error").html(message);
    };

    var _onRoomSuccessfullyCreated = function(_chats) {
        $("#" + _componentRootId + " .room-names").mustache('chat-list-template', {
            chats: _chats
        }, {method: 'html'});
        if (_chats.length < 1) {
            $("#" + _componentRootId + " .join-room-elem").hide();
        } else {
            $("#" + _componentRootId + " .join-room-elem").show();
        }

        $("#" + _componentRootId + " .error").html("");
        $("#" + _componentRootId + " .room-name").val("");
    };

    var _onUserSuccessfullyJoined = function(chatData) {
        var newRoom = new ChatRoomComponent(chatData.chatId, chatData.chatName,
            _rootId, _eventBus, _chatService);
        newRoom.init();

        $("#" + _componentRootId + " .error").html("");
        $("#" + _componentRootId + " .room-name").val("");
    };

    return {
        "init": _init
    }
};

if (typeof define !== 'function') {
    var define = require('amdefine')(module);
}

define(function() {
    return UserMenuComponent;
});