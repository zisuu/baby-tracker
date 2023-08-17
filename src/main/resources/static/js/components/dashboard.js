import util from '../util.js';
import store from '../store.js'
import router from '../router.js';
import service from '../service.js';

export default {
    title: 'Dashboard',
    templatePath: 'dashboard.html',
    requiresAuth: true,
    // css: 'dashboard.css',
    init: function (view){
        // this must be dynamic
        const firstBaby = store.getUserAccountInfos().babies[0];
        const username = store.getUser().email
        util.updateViewField('username', `Logout ${username}`, view);
        util.updateViewField('babyname', `Timeline of ${firstBaby.name}`, view);

        const tbody = view.querySelector('tbody'); // Select the tbody element

        store.getEvents().forEach(babyEvent => {
            const eventRow = document.createElement('tr'); // Create a new <tr> element

            const eventTypeCell = document.createElement('td');
            eventTypeCell.textContent = babyEvent.eventType;
            eventRow.appendChild(eventTypeCell);

            const startDateCell = document.createElement('td');
            const parsedStartDate = new Date(babyEvent.startDate);
            const options = {
                year: 'numeric',
                month: 'numeric',
                day: 'numeric',
                hour: 'numeric',
                minute: 'numeric',
                second: 'numeric'
            };
            startDateCell.textContent = parsedStartDate.toLocaleDateString('UTC', options);
            eventRow.appendChild(startDateCell);

            const endDateCell = document.createElement('td');
            const parsedEndDate = new Date(babyEvent.endDate);
            endDateCell.textContent = parsedEndDate.toLocaleDateString('UTC', options);
            eventRow.appendChild(endDateCell);

            const notesCell = document.createElement('td');
            notesCell.textContent = babyEvent.notes;
            eventRow.appendChild(notesCell);

            const deleteCell = document.createElement('td');
            const deleteLink = document.createElement('a');
            // deleteLink.innerHTML = `<a id="delete" class="btn bg-warning">DELETE</a>`;
            deleteLink.className = 'btn bg-warning';
            deleteLink.id = 'delete';
            deleteLink.textContent = 'DELETE';
            deleteCell.appendChild(deleteLink);
            eventRow.appendChild(deleteCell);
            deleteCell.querySelector('#delete').onclick = event => deleteEvent(babyEvent.id);

            const updateCell = document.createElement('td');
            const updateLink = document.createElement('a');
            updateLink.className = 'btn bg-success';
            updateLink.href = `update-event?id=${babyEvent.id}`;
            updateLink.textContent = 'UPDATE';
            updateCell.appendChild(updateLink);
            eventRow.appendChild(updateCell);

            tbody.appendChild(eventRow);
        });
    }
}
function deleteEvent(eventId) {
    service.deleteEvent(eventId)
        .then(() => {
            store.deleteEvent(eventId);
            router.navigate('/dashboard');
        })
        .catch(error => document.querySelector('footer').innerHTML = 'Unexpected error occurred');
}