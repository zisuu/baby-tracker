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
	deleteEvent: function(eventId) {
		const i = data.events.findIndex(event => event.id === eventId);
		if (i >= 0) data.events.splice(i, 1)
	},
	addEvent(event) {
		data.events.push(event);
	},
	getEvents: function() {
		return data.events;
	},
	getEvent: function(id) {
		return data.events.find(event => event.id == id);
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
	},
    setEventTypes(eventTypes) {
        data.eventTypes = eventTypes;
    },
	getEventTypes() {
		return data.eventTypes;
	},
	updateEvent(event) {
		Object.assign(this.getEvent(event.id), event);
	}
}
