import service from '../service.js';
import router from '../router.js';
import store from '../store.js';
import util from '../util.js';

export default {
	templatePath: 'login.html',
	requiresAuth: false,
	css: 'signin.css',
	init: function(view) {
		view.querySelector('[data-action=login]').addEventListener('click', e => {
			e.preventDefault();
			processLogin(view);
		});
	}
};

function processLogin(view) {
	const form = view.querySelector('form');
	if (!form.reportValidity()) return;
	const user = getFormData(form);
	service.postToken(user)
		.then(token => {
			user.token = JSON.parse(token).token;
			delete user.password;
			store.setUser(user);
			return service.getUserAccountInfos(user);
		})
		.then(myUserAccountInfos => {
			if (myUserAccountInfos.length === 0) {
				return service.getUserAccountInfos(user)
			}
			return myUserAccountInfos;
		})
		.then(data => data.content[0])
		.then(myUserAccountInfos => {
			initAfterLogin(user, myUserAccountInfos)
			return getEventTypes();
		})
		.then(() => {
			router.navigate('/dashboard');
		})
		.catch(error => {
			let msg = error.status === 401
				? "Wrong username or password. Please try again."
				: "Retrieving myUserAccountInfos failed!";
			util.updateViewField('error', msg);
		});
}

function initAfterLogin(user, myUserAccountInfos) {
	store.setUserAccountInfos(myUserAccountInfos);
	store.setBabies(myUserAccountInfos.babies);
	// this must be dynamic
	store.setEvents(myUserAccountInfos.babies[0].events);
	util.showAuthContent(true);
}

function getEventTypes() {
	service.getEventTypes()
		.then(eventTypes => {
			store.setEventTypes(eventTypes);
			return eventTypes;
		})
		.catch(error => {
			let msg = 'Could not load EventTypes!'
			util.updateViewField('error', msg);
		});
}

function getFormData(form) {
	return {
		email: form.email.value,
		password: form.password.value
	};
}
