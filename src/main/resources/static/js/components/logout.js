import store from '../store.js';
import util from '../util.js';

export default {
	title: 'Logout',
	templatePath: 'logout.html',
	requiresAuth: true,
	css: 'signin.css',
	init: function(view) {
		store.clear();
		util.showAuthContent(false);
	}
};
