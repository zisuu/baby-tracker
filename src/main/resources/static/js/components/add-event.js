import service from "../service.js";
import router from '../router.js';
import store from '../store.js';

export default {
    title: 'Add Event',
    templatePath: 'add-event.html',
    requiresAuth: true,
    css: 'add-event.css',
    init: function (view) {
        view.querySelector('[data-action=cancel]').addEventListener('click', e => {
            e.preventDefault();
            router.navigate('/dashboard');
        });

        $('input[name="startTime"]').datetimepicker({
            format: 'Y-m-d H:i',
            step: 5
        });
        $('input[name="endTime"]').datetimepicker({
            format: 'Y-m-d H:i',
            step: 5
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
                    // todo: this must be dynamic
                    babyId = store.getBabies()[0].id;
                    return service.putEventToBaby(eventId, babyId)
                        .then(() => {
                            store.addEvent(event);
                        });
                })
                .then(() => router.navigate('/dashboard'))
                .catch(async error => view.querySelector('[data-field=error]').innerHTML = "Adding event failed! msg: " + await error.text());
        });

        const eventTypes = store.getEventTypes();
        const eventTypeElement = document.getElementsByName('eventType')[0];
        eventTypes.forEach(eventType => {
            const optionElement = document.createElement('option');
            optionElement.textContent = eventType;
            eventTypeElement.appendChild(optionElement);
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
