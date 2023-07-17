import service from "../service.js";
import router from '../router.js';
import store from '../store.js';

export default {
    title: 'Add Event',
    templatePath: 'add-event.html',
    requiresAuth: true,
    init: function (view) {
        view.querySelector('[data-action=cancel]').addEventListener('click', e => {
            e.preventDefault();
            router.navigate('/home');
        });

        view.querySelector('[data-action=add]').addEventListener('click', e => {
            e.preventDefault();

            const form = view.querySelector('form');
            if (!form.reportValidity()) return;
            const event = getFormData(form);
            let eventId = "";
            service.postEvent(event)
                .then(response => {
                    eventId = response.headers.get('Location').split('/').pop();
                    return service.getEvent(eventId);
                })
                .then(event => {
                    store.addEvent(event);
                    router.navigate('/home');
                    return service.putEventToBaby(eventId, store.getBabies()[0].id);
                })
                .catch(error => view.querySelector('[data-field=error]').innerHTML = "Adding event failed! msg: " + error);
            router.navigate('/home');
        });
    }
}


function getFormData(form) {
    return {
        name: form.name.value
    };
}
