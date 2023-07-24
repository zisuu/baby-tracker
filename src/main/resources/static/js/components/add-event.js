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
            let babyId = "";
            service.postEvent(event)
                .then(response => {
                    eventId = response.headers.get('Location').split('/').pop();
                    return service.getEvent(eventId);
                })
                .then(event => {
                    store.addEvent(event);
                    router.navigate('/home');
                    // todo: this must be dynamic
                    babyId = store.getBabies()[0].id;
                    return service.putEventToBaby(eventId, babyId);
                })
                .then(() => router.navigate("#/events/"+babyId))
                .catch(error => view.querySelector('[data-field=error]').innerHTML = "Adding event failed! msg: " + error);

        });
    }
}


function getFormData(form) {
    const timezoneOffset = new Date().getTimezoneOffset() * 60000; // in milliseconds
    const startDate = new Date(form.startTime.value);
    const endDate = new Date(form.endTime.value);

    const utcStartDate = new Date(startDate.getTime() - timezoneOffset);
    const utcEndDate = new Date(endDate.getTime() - timezoneOffset);
    return {
        eventType: form.eventType.value,
        notes: form.notes.value,
        startDate: utcStartDate.toISOString().slice(0, 16),
        endDate: utcEndDate.toISOString().slice(0, 16)
    };
}
