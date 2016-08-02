//TODO: bold, italic, href
//TODO: ctrl+enter default button action
var ChatApp = function(_rootId) {
	//hello!
	$.Mustache.load('resources/htmlTemplates/registration.html');
	$.Mustache.load('resources/htmlTemplates/login.html');
	$.Mustache.load('resources/htmlTemplates/user-menu.html');
	$.Mustache.load('resources/htmlTemplates/chat-list.html');
	$.Mustache.load('resources/htmlTemplates/chat-room.html');
	$.Mustache.load('resources/htmlTemplates/add-message.html');
	$.Mustache.load('resources/htmlTemplates/message-list.html');
	$.Mustache.add('message-list-div', '<div class="container text-container messages"></div>');

	var _eventBus = new EventBus();
	var _userService = new UserService(_eventBus);

	var _components = {};

	var _init = function() {

		_components.registrationComponent = new RegistrationComponent("reg-" + _rootId, _rootId, _eventBus);
		_components.loginComponent = new UserLoginComponent("login-" + _rootId, _rootId, _eventBus);

		_eventBus.subscribe(events.userAddedEvent, _userService.onUserAdded);
		_eventBus.subscribe(events.userAuthenticatedEvent, _userService.onUserAuthenticated);
		_eventBus.subscribe(events.successfulRegistrationEvent, _components.registrationComponent.onRegistrationSuccess);
		_eventBus.subscribe(events.registrationFailedEvent, _components.registrationComponent.onRegistrationFailed);
		_eventBus.subscribe(events.authenticationFailedEvent, _components.loginComponent.onUserAuthenticationFailed);
		_eventBus.subscribe(events.successfulAuthenticationEvent, _components.loginComponent.onUserSuccessfullyAuthenticated);
		_eventBus.subscribe(events.successfulAuthenticationEvent, _components.registrationComponent.onUserSuccessfullyAuthenticated);
		_eventBus.subscribe(events.successfulAuthenticationEvent, _createUserMenu);

		if (!$.Mustache.has("login-template") || !$.Mustache.has("registration-template")) {
			setTimeout(function() {
				Object.keys(_components).forEach(function(key) {
					_components[key].init();
				});
			}, 100);
		} else {
			Object.keys(_components).forEach(function(key) {
				_components[key].init();
			});
		}

		$('body').on('keyup', '.txt-input', function(event) {
			if (event.ctrlKey && event.keyCode === 13) {
				$(this).parent().children('.txt-input-btn').click();
			}
		});
	};

	var _createUserMenu = function() {
		_components.userBox = new UserMenuComponent("userBox", _rootId, _eventBus);
		_components.userBox.init();
	};

	return {"init" : _init};
};

if (typeof define !== 'function') {
    var define = require('amdefine')(module);
}

define(function() {
	return ChatApp;
});