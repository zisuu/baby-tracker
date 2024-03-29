import service from '../service.js';
import router from '../router.js';
import store from '../store.js';

export default {
    title: 'Update Event',
    templatePath: 'update-event.html',
    requiresAuth: true,
    css: 'add-event.css',
    init: function (view, id) {
        view.querySelector('[data-action=cancel]').addEventListener('click', e => {
            e.preventDefault();
            router.navigate('/dashboard');
        });

        const eventTypes = store.getEventTypes();
        const eventTypeElement = document.getElementsByName('eventType')[0];
        eventTypes.forEach(eventType => {
            const optionElement = document.createElement('option');
            optionElement.textContent = eventType;
            eventTypeElement.appendChild(optionElement);
        });

        let event = {};
        Object.assign(event, store.getEvent(id));
        if (event) {
            const form = view.querySelector('form');
            form.eventType.value = event.eventType;
            form.startDate.value = event.startDate;
            form.endDate.value = event.endDate;
            form.notes.value = event.notes;
            view.querySelector('[data-action=update]').onclick = jsEvent => updateEvent(jsEvent, view, event);
        } else {
            document.querySelector('[data-field=error]').innerHTML = `Event ${id} not found`;
        }

        $('input[name="startDate"]').datetimepicker({
            format: 'Y-m-d H:i',
            step: 5
        });
        $('input[name="endDate"]').datetimepicker({
            format: 'Y-m-d H:i',
            step: 5
        });

    }
}

function updateEvent(jsEvent, view, event) {
    jsEvent.preventDefault();
    let form = view.querySelector('form');
    if (!form.reportValidity()) return;
    event = {...event, ...getFormData(form)};
    service.putEvent(event)
        .then(() => {
            return service.getEvent(event.id)
        })
        .then(updatedEvent => {
            store.updateEvent(updatedEvent);
            router.navigate('/dashboard');
        })
        .catch(error => {
            const alertDiv = document.createElement('div');
            alertDiv.className = 'alert alert-danger';
            alertDiv.role = 'alert';
            alertDiv.textContent = "Updating event failed! " + error.text();

            const field = view.querySelector('[data-field=error]');
            field.innerHTML = '';
            field.appendChild(alertDiv);
        });
}

function getFormData(form) {
    const timezoneOffset = new Date().getTimezoneOffset() * 60000; // in milliseconds
    const formData = {
        eventType: form.eventType.value,
        notes: form.notes.value
    };

    if (form.startDate.value && form.endDate.value) {
        const startDate = new Date(form.startDate.value);
        const endDate = new Date(form.endDate.value);
        const utcStartDate = new Date(startDate.getTime() - timezoneOffset);
        const utcEndDate = new Date(endDate.getTime() - timezoneOffset);
        formData.startDate = utcStartDate.toISOString().slice(0, 16);
        formData.endDate = utcEndDate.toISOString().slice(0, 16);
    } else if (form.startDate.value) {
        const startDate = new Date(form.startDate.value);
        const utcStartDate = new Date(startDate.getTime() - timezoneOffset);
        formData.startDate = utcStartDate.toISOString().slice(0, 16);
    }

    return formData;
}

