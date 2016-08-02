var ChatDto = function(title) {
	return {
		"title": title
	}
};

if (typeof define !== 'function') {
    var define = require('amdefine')(module);
}

define(function() {
	return ChatDto;
});