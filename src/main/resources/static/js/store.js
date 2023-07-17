let data = {};

export default {
	setUser: function(user) {
		data.user = user;
	},
	getUser: function() {
		return data.user;
	},
	getUserToken: function() {
		return data.user.token;
	},
	setMyUserInfos: function(userInfos) {
		data.userInfos = userInfos;
	},
	getMyUserInfos: function() {
		return data.userInfos;
	},
	getModules: function(roleName, onyWithMissingRatings) {
		if (onyWithMissingRatings) return data.roles.find(role => role.role === roleName)
			.modules.filter(module => module.numberOfMissingRatings > 0);
		return data.roles.find(role => role.role === roleName).modules;
	},
	getCourses: function(roleName, moduleId, onyWithMissingRatings) {
		if (onyWithMissingRatings) return data.roles.find(role => role.role === roleName)
			.courses.filter(course => course.moduleId === moduleId && course.numberOfMissingRatings > 0);

		return data.roles.find(role => role.role === roleName)
			.courses.filter(course => course.moduleId === moduleId);
	},
	addRatings: function(moduleId, moduleRatings) {
		if (!data.hasOwnProperty('modules')) data.modules = [];
		const module = {
			'id': moduleId,
			'moduleRatings': moduleRatings
		}
		data.modules.push(module);
	},
	getRatings: function(moduleId, courseId) {
		if (!data.hasOwnProperty('modules')) return undefined;
		const module = data.modules.find(module => module.id === moduleId);

		// return modules if courseId is not set
		if(!courseId) return module ? module.moduleRatings : undefined;

		// filter out ratings if courseId is set, use copy to not remove from original module
		const cloneModule = JSON.parse(JSON.stringify(module));
		cloneModule.moduleRatings.forEach(moduleRating => {
			moduleRating.courseRatings = [moduleRating.courseRatings.find(courseRating => courseRating.courseId === courseId)];
		})
		return cloneModule.moduleRatings ? cloneModule.moduleRatings : undefined;
	},
	updateRating: function(moduleId, rating) {
		const moduleIndex = data.modules.findIndex(module => module.id === moduleId);
		const moduleRatingIndex = data.modules[moduleIndex].moduleRatings.findIndex(storedRating => storedRating.personId === rating.personId);
		const courseRatingIndex = data.modules[moduleIndex].moduleRatings[moduleRatingIndex].courseRatings.findIndex(storedRating => storedRating.courseId === rating.courseId)

		data.modules[moduleIndex].moduleRatings[moduleRatingIndex].courseRatings[courseRatingIndex].version = rating.version;
		data.modules[moduleIndex].moduleRatings[moduleRatingIndex].courseRatings[courseRatingIndex].successRate = rating.successRate;
	},
	clearRatings: function() {
		if (data.hasOwnProperty('modules')) delete data.modules;
	},
	clear: function() {
		data = {};
	}
}
