import util from '../util.js';
import store from '../store.js'
import router from '../router.js';
import service from '../service.js';

export default {
    title: 'Dashboard',
    templatePath: 'dashboard.html',
    requiresAuth: true,
    css: null,
    init: function (view) {
        const username = store.getUser().email
        util.updateViewField('username', `Logout ${username}`, view);

        if (store.getBabies().length !== 0) {
            // this must be dynamic
            const firstBaby = store.getBabies()[0];
            util.updateViewField('babyname', `Timeline of ${firstBaby.name}`, view);
            let ageString = calculateAge(firstBaby.birthday)
            util.updateViewField('age', `Age: ${ageString}`, view);
            const tbody = view.querySelector('tbody'); // Select the tbody element

            store.getEvents().forEach(babyEvent => {
                const eventRow = document.createElement('tr'); // Create a new <tr> element

                const eventTypeCell = document.createElement('td');

                eventTypeCell.innerHTML = `<i class="${getIconClassForEventType(babyEvent.eventType)}"></i>
                    <span>${babyEvent.eventType}</span>`
                eventRow.appendChild(eventTypeCell);

                const startDateCell = document.createElement('td');
                if (babyEvent.startDate) {
                    startDateCell.textContent = formatDate(babyEvent.startDate)
                } else {
                    startDateCell.textContent = 'N/A';
                }
                eventRow.appendChild(startDateCell);

                const endDateCell = document.createElement('td');
                if (babyEvent.endDate) {
                    endDateCell.textContent = formatDate(babyEvent.endDate)
                } else {
                    endDateCell.textContent = 'N/A';
                }
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
                updateLink.href = `#/update-event/${babyEvent.id}`;
                updateLink.textContent = 'UPDATE';
                updateCell.appendChild(updateLink);
                eventRow.appendChild(updateCell);

                tbody.appendChild(eventRow);
            });
        } else {
            removeEventTableAndButton(view);
            util.updateViewField('babyname', 'Add your Baby in the menu above, to track your babies events here.', view);
        }
    }
};

function deleteEvent(eventId) {
    service.deleteEvent(eventId)
        .then(() => {
            store.deleteEvent(eventId);
            router.navigate('/dashboard');
        })
        .catch(error => document.querySelector('footer').innerHTML = 'Unexpected error occurred');
}

function removeEventTableAndButton(view) {
    const eventTable = view.querySelector('table');
    eventTable.remove();
    const addBabyButton = view.querySelector('#addBabyButton');
    addBabyButton.remove();
}

function calculateAge(inputDate) {
    const birthDate = new Date(inputDate);
    const currentDate = new Date();

    let years = currentDate.getFullYear() - birthDate.getFullYear();
    const months = currentDate.getMonth() - birthDate.getMonth();
    const days = currentDate.getDate() - birthDate.getDate();

    if (months < 0 || (months === 0 && days < 0)) {
        years--;
    }

    const ageString = [];
    if (years > 0) {
        ageString.push(`${years} year${years > 1 ? 's' : ''}`);
    }
    if (months > 0) {
        ageString.push(`${months} month${months > 1 ? 's' : ''}`);
    }
    if (days > 0) {
        ageString.push(`${days} day${days > 1 ? 's' : ''}`);
    }

    return ageString.join(', ');
}

function getIconClassForEventType(eventType) {
    switch (eventType) {
        case 'FEEDING':
            return 'fas fa-person-breastfeeding fa-2xl';
        case 'SLEEPING':
            return 'fas fa-moon fa-2xl';
        case 'DIAPER':
            return 'fas fa-toilet-paper fa-2xl';
        case 'BEDTIME':
            return 'fas fa-bed fa-2xl';
        case 'WAKEUP':
            return 'fas fa-sun fa-2xl';
        case 'BATH':
            return 'fas fa-bath fa-2xl';
        case 'CRYING':
            return 'fas fa-face-sad-cry fa-2xl';
        default:
            return 'fas fa-question fa-2xl';
    }
}

function formatDate(dateString) {
    if (!dateString) {
        return null;
    }
    const parsedDate = new Date(dateString);
    const options = {
        year: 'numeric',
        month: 'numeric',
        day: 'numeric',
        hour: 'numeric',
        minute: 'numeric',
        second: 'numeric'
    };
    return parsedDate.toLocaleDateString('UTC', options);
}