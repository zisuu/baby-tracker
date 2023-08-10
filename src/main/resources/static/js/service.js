import store from './store.js';

const BASE_URL = '/api/v1/';

export default {
    postUser: function (user) {
        const url = BASE_URL + 'auth/register';
        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(user)
        };
        return fetch(url, options)
            .then(response => response.ok ? Promise.resolve(response) : Promise.reject(response));
    },
    postToken: function (user) {
        const url = BASE_URL + 'auth/authenticate';
        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(user)
        };
        return fetch(url, options)
            .then(response => response.ok ? response.text() : Promise.reject(response));
    },
    getUserAccountInfos: function (user) {
        const url = BASE_URL + 'users?email=' + user.email;
        const options = {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + store.getUserToken(),
                'Accept': 'application/json'
            }
        };
        return fetch(url, options)
            .then(response => response.ok ? response.json() : Promise.reject((response)))
    },
    getBaby: function (babyId) {
        const url = BASE_URL + 'babies/' + babyId;
        const options = {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + store.getUserToken(),
                'Accept': 'application/json'
            }
        };
        return fetch(url, options)
            .then(response => response.ok ? response.json() : Promise.reject((response)))
    },
    getBabies: function (user) {
        const url = BASE_URL + 'babies?username=' + user.email;
        const options = {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + store.getUserToken(),
                'Accept': 'application/json'
            }
        };
        return fetch(url, options)
            .then(response => response.ok ? response.json() : Promise.reject((response)))
    },
    postBaby: function (baby) {
        const url = BASE_URL + 'babies';
        const options = {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + store.getUserToken(),
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(baby)
        }
        return fetch(url, options)
            .then(response => response.ok ? Promise.resolve(response) : Promise.reject(response));
    },
    postEvent: function (event) {
        const url = BASE_URL + 'events';
        const options = {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + store.getUserToken(),
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(event)
        }
        return fetch(url, options)
            .then(response => response.ok ? Promise.resolve(response) : Promise.reject(response));
    },
    putTodo: function (user, todo) {
        const url = BASE_URL + 'todos/' + todo.id;
        const options = {
            method: 'PUT',
            headers: {
                'Authorization': getBasicAuthHeader(user),
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(todo)
        };
        return fetch(url, options)
            .then(response => response.ok ? response.json() : Promise.reject(response));
    },
    putBabyToUser(babyId, userAccountId) {
        const url = BASE_URL + 'babies/' + babyId + '/user/' + userAccountId;
        const options = {
            method: 'PUT',
            headers: {
                'Authorization': 'Bearer ' + store.getUserToken()
            },
        }
        return fetch(url, options)
            .then(response => response.ok ? Promise.resolve(response) : Promise.reject(response));
    },
    getEvent(eventUuid) {
        const url = BASE_URL + 'events/' + eventUuid;
        const options = {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + store.getUserToken(),
                'Accept': 'application/json'
            }
        };
        return fetch(url, options)
            .then(response => response.ok ? response.json() : Promise.reject((response)))

    },
    getEvents(babyId) {
        const url = BASE_URL + 'events?babyId=' + babyId;
        const options = {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + store.getUserToken(),
                'Accept': 'application/json'
            }
        };
        return fetch(url, options)
            .then(response => response.ok ? response.json() : Promise.reject((response)))

    },
    putEventToBaby(eventId, babyId) {
        const url = BASE_URL + 'events/' + eventId + '/baby/' + babyId;
        const options = {
            method: 'PUT',
            headers: {
                'Authorization': 'Bearer ' + store.getUserToken()
            },
        }
        return fetch(url, options)
            .then(response => response.ok ? Promise.resolve(response) : Promise.reject(response));
    }
}
