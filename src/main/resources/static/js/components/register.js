import service from '../service.js';
import router from '../router.js';
import store from '../store.js';
import util from '../util.js';

export default {
	templatePath: 'register.html',
	requiresAuth: false,
	css: 'sign-in.css',
	init: function(view) {
		view.querySelector('[data-action=register]').addEventListener('click', e => {
			e.preventDefault();
			processRegister(view);
		});
	}
};
function processRegister(view) {
	const form = view.querySelector('form');
	if (!form.reportValidity()) return;
	const user = getFormData(form);
	service.postUser(user)
		.then(() => initAfterRegister())
		.catch(error => {
			let msg;
			switch(error.status) {
				case 400: msg = "The provided user data is invalid!"; break;
				case 409: msg = "Please choose another username!"; break;
				default: msg = "Registering user failed!";
			}
			util.updateViewField('error', msg);
		});
}

function initAfterRegister() {
	router.navigate('/login');
}


function getFormData(form) {
	return {
		name: form.username.value,
		password: form.password.value
	};
}

