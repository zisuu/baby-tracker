export default {
	showAuthContent: function(visible) {
		document.querySelectorAll('[data-auth=true]')
			.forEach(e => e.style.display = visible ? 'block' : 'none');
	},
	updateViewField: function(key, value) {
		document.querySelectorAll(`[data-field="${key}"]`)
			.forEach(e => e.innerHTML = value);
	},
	determineSemester: function() {
		/*
		Approach: Initialize the current date to a variable using a new Date() which by default returns the current date. Initialize the starting date of the current year ( i.e. Jan 1) to startDate. Calculate the difference between the two dates in days by subtracting startDate from currentDate.
		This returns the difference between dates in milliseconds.
		now dividing the result by total milliseconds in a day gives a difference between dates in days.
		Add the number of days to the current weekday using getDay() and divide it by 7. We will get the current week’s number.

		herbst isch immer = KW43 bis KW14
		und Früehlig = KW17 bis KW40
		 */

		const currentDate = new Date();
		const startDate = new Date(currentDate.getFullYear(), 0, 1);
		const days = Math.floor((currentDate - startDate) / (24 * 60 * 60 * 1000));
		const weekNumber = Math.ceil(days / 7);

		const year = currentDate.getFullYear().toString().slice(-2);

		return 17 <= weekNumber <= 40 ? 'HS' + year : 'FS' + year;
	}
}

