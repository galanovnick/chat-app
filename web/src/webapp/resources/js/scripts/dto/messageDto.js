var MessageDto = function(text, roomId) {
	return {
		"text": text,
		"chatId": roomId
	}
};

if (typeof define !== 'function') {
    var define = require('amdefine')(module);
}

define(function() {
	return MessageDto;
});