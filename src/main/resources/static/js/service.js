import store from "./store.js";

const BASE_URL = '/api/v1/';

export default {
	postToken: function(user) {
		const url = BASE_URL + 'token';
		const options = {
			method: 'POST',
			headers: {
				'Authorization': getBasicAuthHeader(user),
				'Accept': 'text/plain'
			},
		};
		return fetch(url, options)
			.then(response => response.ok ? response.text() : Promise.reject(response));
	},
	postUser: function(userAccount) {
		const url = BASE_URL + 'users';
		const options = {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(userAccount)
		};
		return fetch(url, options)
			.then(response => response.ok ? Promise.resolve(response) : Promise.reject(response));
	},
	getMyUserInfos: function(username) {
		let url = BASE_URL + 'users?username=' + username;
		const options = {
			method: 'GET',
			headers: {
				'Authorization': 'Bearer ' + store.getUserToken(),
				'Accept': 'application/json'
			}
		};
		return fetch(url, options)
			.then(response => response.ok ? response.json() : Promise.reject((response))
			.then(data => data.content))
	},
	// getModuleRatings: function(moduleId, role) {
	// 	const url = BASE_URL + `modules/${moduleId}/ratings?view=${role.toLowerCase()}`;
	// 	const options = {
	// 		method: 'GET',
	// 		headers: {
	// 			'Authorization': 'Bearer ' + store.getUserToken(),
	// 			'Accept': 'application/json'
	// 		}
	// 	};
	// 	return fetch(url, options)
	// 		.then(response => response.ok ? response.json() : Promise.reject((response)))
	// },
	// putCourseRatings: function(courseId, ratings) {
	// 	const url = BASE_URL + `courses/${courseId}/ratings`;
	// 	const options = {
	// 		method: 'PUT',
	// 		headers: {
	// 			'Authorization': 'Bearer ' + store.getUserToken(),
	// 			'Content-Type': 'application/json',
	// 			'Accept': 'application/json'
	// 		},
	// 		body: JSON.stringify(ratings)
	// 	};
	// 	return fetch(url, options)
	// 		.then(response => response.ok ? response.json() : Promise.reject((response)))
	// }
}

function getBasicAuthHeader(user) {
	return 'Basic ' + btoa(user.name + ':' + user.password);
}

