import service from '../service.js';
import router from '../router.js';
import store from '../store.js';
import util from '../util.js';

export default {
	templatePath: 'login.html',
	requiresAuth: false,
	css: 'sign-in.css',
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
			return service.getMyUserInfos(user.name)
		})
		.then(myUserInfos => {
			if (myUserInfos.length === 0) {
				return service.getMyUserInfos(user.name);
			}
			return myUserInfos;
		})
		.then(myUserInfos => {
			store.setMyUserInfos(myUserInfos);
			router.navigate('/home');
		})
		.catch(error => {
			let msg = error.status === 401
				? "Wrong username or password. Please try again."
				: "Retrieving baby-tracker data failed!";
			util.updateViewField('error', msg);
			util.updateViewField('info', '');
		});
	util.updateViewField('error', '');
	util.updateViewField('info', 'Login in please wait.');
}


function getFormData(form) {
	return {
		name: form.username.value,
		password: form.password.value
	};
}

