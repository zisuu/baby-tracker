import util from '../util.js';
import store from '../store.js'

export default {
    title: 'Dashboard',
    templatePath: 'dashboard.html',
    requiresAuth: true,
    // css: 'dashboard.css',
    init: function (view) {
        // this must be dynamic
        const firstBaby = store.getUserAccountInfos().babies[0];
        const username = store.getUser().email
        util.updateViewField('username', `Logout ${username}`, view);
        util.updateViewField('babyname', `Timeline of ${firstBaby.name}`, view);

        const tbody = view.querySelector('tbody'); // Select the tbody element

        store.getEvents().forEach(event => {
            const eventRow = document.createElement('tr'); // Create a new <tr> element

            const eventTypeCell = document.createElement('td');
            eventTypeCell.textContent = event.eventType;
            eventRow.appendChild(eventTypeCell);

            const startDateCell = document.createElement('td');
            const parsedStartDate = new Date(event.startDate);
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
            const parsedEndDate = new Date(event.endDate);
            endDateCell.textContent = parsedEndDate.toLocaleDateString('UTC', options);
            eventRow.appendChild(endDateCell);

            const notesCell = document.createElement('td');
            notesCell.textContent = event.notes;
            eventRow.appendChild(notesCell);

            const deleteCell = document.createElement('td');
            const deleteLink = document.createElement('a');
            deleteLink.className = 'btn bg-warning';
            deleteLink.href = `delete-event?id=${event.id}`;
            deleteLink.textContent = 'DELETE';
            deleteCell.appendChild(deleteLink);
            eventRow.appendChild(deleteCell);

            const updateCell = document.createElement('td');
            const updateLink = document.createElement('a');
            updateLink.className = 'btn bg-success';
            updateLink.href = `update-event?id=${event.id}`;
            updateLink.textContent = 'UPDATE';
            updateCell.appendChild(updateLink);
            eventRow.appendChild(updateCell);

            tbody.appendChild(eventRow); // Append the event row to the tbody
        });
    }
}
