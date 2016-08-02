if (typeof define !== 'function') {
    events = require('events');
}

var UserService = function(_userEventBus) {

    var _create = function(user) {
        $.ajax({
            type: "post",
            data: {
                username: user.username,
                password: user.password,
                passwordConfirm: user.passwordConfirm
            },
            url: "/api/register",
            dataType: "json",
            jsonp: false,
            success: function() {
                _userEventBus.post(user.username, events.successfulRegistrationEvent);
            },
            error: function(response) {
                response = JSON.parse(response.responseText);
                _userEventBus.post(response.message, events.registrationFailedEvent);
            }
        });
    };

    var _authenticate = function(user) {
        $.ajax({
            type: "post",
            data: {
                username: user.username,
                password: user.password
            },
            url: "/api/login",
            dataType: "json",
            jsonp: false,
            success: function(response) {
                _userEventBus.post({
                    token: response.token,
                    username: user.username
                }, events.successfulAuthenticationEvent);
            },
            error: function(response) {
                response = JSON.parse(response.responseText);
                _userEventBus.post(response.message, events.authenticationFailedEvent);
            }
        });
    };

    var _onUserAdded = function(user) {
        _create(user);
    };

    var _onUserAuthenticated = function(user) {
        return _authenticate(user);
    };

    return {
        "onUserAdded": _onUserAdded,
        "onUserAuthenticated": _onUserAuthenticated
    }
};

if (typeof define !== 'function') {
    var define = require('amdefine')(module);
}

define(function() {
    return UserService;
});