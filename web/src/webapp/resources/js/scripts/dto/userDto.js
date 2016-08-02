var UserDto = function(username, password, passwordConfirm) {
	return {
		"username": username,
		"password": password,
		"passwordConfirm": passwordConfirm
	}
};

if (typeof define !== 'function') {
    var define = require('amdefine')(module);
}

define(function() {
	return UserDto;
});