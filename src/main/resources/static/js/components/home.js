import service from "../service.js";
import store from '../store.js'

export default {
	title: 'Home',
	templatePath: 'home.html',
	requiresAuth: true,
	init: function(view) {
		store.getUserAccountInfos().babies.forEach(baby => {
			const li = document.createElement('li');
			li.innerHTML = `
				<b>${baby.name}</b>
				<span class="right"><a href="#/events/${baby.id}">Events</a> | <a id="delete">Delete</a></span>
			`;
			view.querySelector('ul').append(li);
		})
	}
}