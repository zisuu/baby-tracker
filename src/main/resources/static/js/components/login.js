import service from '../service.js';
import router from '../router.js';
import store from '../store.js';
import util from '../util.js';

export default {
	templatePath: 'login.html',
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
			user.token = token;
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
	util.showAuthContent(true);
	util.updateViewField('user.name', user.name);
	router.navigate('/home');
}


function getFormData(form) {
	return {
		name: form.name.value,
		password: form.password.value
	};
}
