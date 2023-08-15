import service from "../service.js";
import router from '../router.js';
import store from '../store.js';

export default {
    title: 'Delete Event',
    templatePath: 'dashboard.html',
    requiresAuth: true,
    init: function (eventId) {
        service.deleteEvent(eventId)
            .then(response => {
                eventId = response.headers.get('Location').split('/').pop();
                return service.getEvent(eventId);
            })
            .then(event => {
                store.addEvent(event);
                // todo: this must be dynamic
                babyId = store.getBabies()[0].id;
                return service.putEventToBaby(eventId, babyId);
            })
            .then(() => router.navigate('/dashboard'))
            .catch(error => view.querySelector('[data-field=error]').innerHTML = "Adding event failed! msg: " + error);
        router.navigate('/dashboard');

    }
}
