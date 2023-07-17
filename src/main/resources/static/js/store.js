let data = {};

export default {
	setUser: function(user) {
		data.user = user;
	},
	getUser: function() {
		return data.user;
	},
	setBabies: function(babies) {
		data.babies = babies;
	},
	addBaby: function(baby) {
		data.babies.push(baby);
	},
	getBabies: function() {
		return data.babies;
	},
	setEvents: function(events) {
		data.events = events;
	},
	addEvent(event) {
		data.events.push(event);
	},
	getEvents: function() {
		return data.events;
	},
	getUserToken: function() {
		return data.user.token;
	},
	setUserAccountInfos: function(userAccountInfos) {
		data.userAccountInfos = userAccountInfos;
	},
	getUserAccountInfos: function() {
		return data.userAccountInfos;
	},
	clear: function() {
		data = {};
	}
}
