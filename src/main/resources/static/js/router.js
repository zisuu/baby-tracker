import store from './store.js';

const TEMPLATE_ROOT = 'templates/';

const routes = Object.create(null);

export default {
	register: function(path, component) {
		routes[path] = component;
	},
	navigate: function(path, param) {
		path += param ? '/' + param : '';
		if (location.hash !== '#' + path) location.hash = path;
		else render();
	}
}

window.addEventListener('hashchange', render);

function render() {
	const view = document.createElement('div');
	document.querySelector('main').replaceChildren(view);

	const [path, param] = location.hash.split('/').splice(1);
	const component = routes['/' + path];
	if (!component) {
		return view.innerHTML = `<h2>404 Not Found</h2><p>Sorry, page not found!</p>`;
	}
	if (component.requiresAuth && !store.getUser()) {
		return view.innerHTML = `<h2>401 Unauthorized</h2><p>Please login!</p>`;
	}
	getTemplate(component)
		.then(template => {
			view.innerHTML = template;
			component.init(view, param);
			document.title = 'Todo App' + (component.title ? ' - ' + component.title : '');
		})
		.catch(errorTemplate => {
			view.innerHTML = errorTemplate;
		});
}

function getTemplate(component) {
	return fetch(TEMPLATE_ROOT + component.templatePath)
		.then(response => response.ok ? response.text() : Promise.reject(response))
		.catch(e => Promise.reject(`<p class="danger">Loading template failed!</p>`));
}

