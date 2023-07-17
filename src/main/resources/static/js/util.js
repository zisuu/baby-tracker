
export default {
	showAuthContent: function(visible) {
		document.querySelectorAll('[data-auth=true]')
			.forEach(e => e.style.display = visible ? 'block' : 'none');
	},
	updateViewField: function(key, value) {
		document.querySelectorAll(`[data-field="${key}"]`)
			.forEach(e => e.innerHTML = value);
	}
}
