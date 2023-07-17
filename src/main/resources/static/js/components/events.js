import service from "../service.js";
import store from '../store.js'

export default {
    title: 'Events',
    templatePath: 'events.html',
    requiresAuth: true,
    init: function (view, babyId) {
        service.getEvents(babyId)
            .then(data => data.content)
            .then(events => {
                store.setEvents(events);
            })
            .catch(error => {
                let msg = error.status === 401
                    ? "Wrong username or password. Please try again."
                    : "Retrieving myUserAccountInfos failed!";
                console.log(msg)
            });


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