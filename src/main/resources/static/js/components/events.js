import service from "../service.js";
import store from '../store.js'

export default {
	title: 'Events',
	templatePath: 'events.html',
	requiresAuth: true,
	init: function(view) {
		store.getEvents().forEach(event => {
			const li = document.createElement('li');
			li.innerHTML = `
				<b>${event.eventType}</b>
				<b>${event.notes}</b>
			`;
			view.querySelector('ul').append(li);
		})
	}
}
