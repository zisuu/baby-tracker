import store from "../store.js";
import service from "../service.js";
import util from "../util.js";
import router from "../router.js";

let selectedRoleName;
let selectedModuleId;
let selectedCourseId;

let viewCourses;
let updateRatingsCue = new Set();
let config = {};

export default {
    templatePath: 'home.html',
    requiresAuth: true,
    css: 'sidebars.css',
    init: function() {

        // const roles = store.getRoles();
        const myUserInfos = store.getMyUserInfos();
        selectedBabyName = myUserInfos.babies[0].name;
        selectedModuleId = roles[0].modules[0].id;

        loadConfiguration()

        renderNavigation();
        renderModules();
        renderCourses();
        renderRatings();
    }
};

function loadConfiguration() {
    config = {};
    //global config
    config.currentSemester = util.determineSemester()
    config.showCourses = false;
    config.canEditRatings = true;
    config.showAverageSuccessRate = false;
    config.onlyWithMissingRatings = false;
    config.showFilterMissingRatings = false;
    config.markMissingRatings = false;
    // //for each role
    // switch (selectedRoleName) {
    //     case 'HeadOf':
    //         config.showCourses = false;
    //         config.canEditRatings = true;
    //         config.showAverageSuccessRate = false;
    //         config.onlyWithMissingRatings = false;
    //         config.showFilterMissingRatings = false;
    //         config.markMissingRatings = false;
    //         break;
    //     case 'Professor':
    //         config.showCourses = false;
    //         config.canEditRatings = true;
    //         config.showAverageSuccessRate = false;
    //         config.onlyWithMissingRatings = false;
    //         config.showFilterMissingRatings = false;
    //         config.markMissingRatings = false;
    //         break;
    //     case 'Assistant':
    //         config.showCourses = true;
    //         config.canEditRatings = false;
    //         config.showAverageSuccessRate = true;
    //         config.onlyWithMissingRatings = true;
    //         config.showFilterMissingRatings = true;
    //         config.markMissingRatings = true;
    //         break;
    //     case 'Student':
    //         config.showCourses = false;
    //         config.canEditRatings = false;
    //         config.showAverageSuccessRate = true;
    //         config.onlyWithMissingRatings = false;
    //         config.showFilterMissingRatings = false;
    //         config.markMissingRatings = false;
    //         break;
    //     default:
    //         config.showCourses = false;
    //         config.canEditRatings = false;
    //         config.showAverageSuccessRate = false;
    //         config.onlyWithMissingRatings = false;
    //         config.showFilterMissingRatings = false;
    //         config.markMissingRatings = false;
    // }
}

function toggleCourseModuleStatus(view, id) {
    const currentActiveA = view.querySelector('a.active');
    if (currentActiveA) {
        currentActiveA.className = 'list-group-item list-group-item-action py-3 lh-sm';
        currentActiveA.removeAttribute('aria-current');
        const small = currentActiveA.querySelector('small');
        small.className = 'text-muted';
    }

    const newActiveA = view.querySelector(`[data-value="${id}"]`)
    newActiveA.className = 'list-group-item list-group-item-action active py-3 lh-sm';
    newActiveA.setAttribute('aria-current','true')
    const small = newActiveA.querySelector('small');
    small.className = '';

}

function renderNavigation() {
    const viewNav = document.querySelector('div.navigation');
    viewCourses = document.querySelector('div.courses');

    createNavLis();

    //
    //create settings
    //

    // show course switch
    const divShowCourses = document.createElement('div');
    divShowCourses.className = 'form-check form-switch';
    divShowCourses.addEventListener('click', function() {
        if (inputShowCourses.checked === true) {
            viewCourses.classList.add('d-flex');
            viewCourses.classList.remove('d-none');
            viewCourses.previousElementSibling.classList.add('d-flex');
            viewCourses.previousElementSibling.classList.remove('d-none');
        } else {
            viewCourses.classList.add('d-none');
            viewCourses.classList.remove('d-flex');
            viewCourses.previousElementSibling.classList.add('d-none');
            viewCourses.previousElementSibling.classList.remove('d-flex');
        }
    })
    const inputShowCourses = document.createElement('input');
    inputShowCourses.className = 'form-check-input';
    inputShowCourses.type = 'checkbox';
    inputShowCourses.setAttribute('role', 'switch');
    inputShowCourses.id = 'showCoursesChecked';
    inputShowCourses.checked = config.showCourses;
    inputShowCourses.style.cursor = 'pointer';

    const labelShowCourses = document.createElement('label');
    labelShowCourses.className = 'form-check-label';
    labelShowCourses.setAttribute('for', 'showCoursesChecked');
    labelShowCourses.innerHTML = 'Show courses';
    labelShowCourses.style.cursor = 'pointer';

    divShowCourses.append(inputShowCourses, labelShowCourses)

    if (!config.showCourses) {
        viewCourses.classList.add('d-none');
        viewCourses.previousElementSibling.classList.add('d-none');
    }


    // filter by semester switch
    const divFilterBySemester = document.createElement('div');
    divFilterBySemester.className = 'form-check form-switch';
    divFilterBySemester.addEventListener('click', e => {
        e.preventDefault()
        // service.getMyRoles(inputFilterBySemester.checked === true ? config.currentSemester : undefined)
        service.getMyUserInfos()
            .then(myUserInfos => {
                if (myUserInfos.length > 0) {
                    store.setRoles(myRoles);
                    createNavLis(myRoles);
                    renderModules();
                    renderCourses();
                    renderRatings();
                    inputFilterBySemester.checked = !inputFilterBySemester.checked
                } else {
                    alert('There are roles for current semester');
                    inputFilterBySemester.checked = false;
                }
           })
            .catch(error => {
                // yes mr. locher this is buggy here, but we didn't catch the case a user don't have any roles while developing
                // found this bug at the day of presentation could not fix it :(
                if (error.status === 401) logout();
                alert('Could not get ratings for current semester');
                inputFilterBySemester.checked = false;
            })
    })
    const inputFilterBySemester = document.createElement('input');
    inputFilterBySemester.className = 'form-check-input';
    inputFilterBySemester.type = 'checkbox';
    inputFilterBySemester.setAttribute('role', 'switch');
    inputFilterBySemester.id = 'showFilterBySemester';
    inputFilterBySemester.checked = true;
    inputFilterBySemester.style.cursor = 'pointer';

    const labelFilterBySemester = document.createElement('label');
    labelFilterBySemester.className = 'form-check-label';
    labelFilterBySemester.setAttribute('for', 'showFilterBySemester');
    labelFilterBySemester.innerHTML = 'Current Semester';
    labelFilterBySemester.style.cursor = 'pointer';

    divFilterBySemester.append(inputFilterBySemester, labelFilterBySemester)
    viewNav.querySelector('div.settings').append(divShowCourses, divFilterBySemester);

    // filter only with missing Ratings
    if (config.showFilterMissingRatings) {
        const divOnlyWithMissingRatings = document.createElement('div');
        divOnlyWithMissingRatings.className = 'form-check form-switch';
        divOnlyWithMissingRatings.addEventListener('click', function () {
            config.onlyWithMissingRatings = inputOnlyWithMissingRatings.checked
            renderModules();
            renderCourses();
            renderRatings();
        })
        const inputOnlyWithMissingRatings = document.createElement('input');
        inputOnlyWithMissingRatings.className = 'form-check-input';
        inputOnlyWithMissingRatings.type = 'checkbox';
        inputOnlyWithMissingRatings.setAttribute('role', 'switch');
        inputOnlyWithMissingRatings.id = 'showOnlyWithMissingRatings';
        inputOnlyWithMissingRatings.checked = config.onlyWithMissingRatings;
        inputOnlyWithMissingRatings.style.cursor = 'pointer';

        const labelOnlyWithMissingRatings = document.createElement('label');
        labelOnlyWithMissingRatings.className = 'form-check-label';
        labelOnlyWithMissingRatings.setAttribute('for', 'showOnlyWithMissingRatings');
        labelOnlyWithMissingRatings.innerHTML = 'Only missing ratings';
        labelOnlyWithMissingRatings.style.cursor = 'pointer';

        divOnlyWithMissingRatings.append(inputOnlyWithMissingRatings, labelOnlyWithMissingRatings)
        viewNav.querySelector('div.settings').append(divOnlyWithMissingRatings);
    }



    // create logout
    const aLogout = document.createElement('a');
    aLogout.className = 'text-bg-dark text-decoration-none';
    aLogout.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-door-closed" viewBox="0 0 16 16">\n' +
        '<path d="M3 2a1 1 0 0 1 1-1h8a1 1 0 0 1 1 1v13h1.5a.5.5 0 0 1 0 1h-13a.5.5 0 0 1 0-1H3V2zm1 13h8V2H4v13z"/>\n' +
        '<path d="M9 9a1 1 0 1 0 2 0 1 1 0 0 0-2 0z"/>\n' +
        '</svg>' +
        `<strong> Logout ${store.getUser().name}</strong>`
    aLogout.href = 'http://localhost:8080/';
    aLogout.addEventListener('click', e => {
        e.preventDefault();
        if (updateRatingsCue.size !== 0 ? confirm('There are unsaved ratings, do you really want to leave?') : true) {
            logout();
        }
    })
    viewNav.querySelector('div.logout').append(aLogout);

    function createNavLis() {

        const ul = viewNav.querySelector('ul.nav')
        while (ul.lastElementChild) {
            ul.removeChild(ul.lastElementChild);
        }
        store.getRoles().forEach(role => ul.append(createNavLi(role)))

        function createNavLi(role) {
            const li = document.createElement('li');

            //create link
            const a = document.createElement('a');
            a.href = '#/home';
            a.className = 'd-flex align-items-center nav-link text-white';

            a.addEventListener('click', e => {
                e.preventDefault();
                if (updateRatingsCue.size !== 0 ? confirm('There are unsaved ratings, do you really want to leave?') : true) {
                    selectedRoleName = role.role;
                    selectedModuleId = role.modules[0].id;
                    selectedCourseId = undefined;
                    loadConfiguration(selectedRoleName)
                    toggleRoleStatus(li);
                    store.clearRatings();
                    renderModules();
                    renderCourses();
                    renderRatings();
                }
            })

            //add svg
            a.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor" class="bi bi-dot" viewBox="0 0 16 16">\n' +
                '  <path d="M8 9.5a1.5 1.5 0 1 0 0-3 1.5 1.5 0 0 0 0 3z"/>\n' +
                '</svg>';
            a.innerHTML += `<span>${role.role}</span>`;
            li.append(a);

            //set active
            if (selectedRoleName === role.role) {
                //set active
                li.className = 'nav-item';
                a.classList.remove('text-white')
                a.classList.add('active');
                a.setAttribute('aria-current', 'page');
            }
            return li
        }
    }


    function toggleRoleStatus(newActiveLi) {
        //inactive -> delete active
        const currentActiveLi = document.querySelector('ul.nav .nav-item');
        if (currentActiveLi) {
            currentActiveLi.className = '';
            const a = currentActiveLi.querySelector('a');
            a.classList.remove('active')
            a.classList.add('text-white');
            a.removeAttribute('aria-current');
        }

        //set active
        newActiveLi.className = 'nav-item';
        const a = newActiveLi.querySelector('a');
        a.classList.remove('text-white')
        a.classList.add('active');
        a.setAttribute('aria-current', 'page');

    }

}


function renderModules() {
    const viewModules = document.querySelector('div.modules');

    // create module div
    const divModules = document.createElement('div');
    divModules.className = 'list-group list-group-flush border-bottom scrollarea';

    // create a for each module
    const modules = store.getModules(selectedRoleName, config.onlyWithMissingRatings);

    // no modules found
    if (modules.length === 0) {
        selectedModuleId = undefined;
        const spanNoModule = document.createElement('span');
        spanNoModule.innerHTML = 'No module found, change filter';
        divModules.append(spanNoModule)
        if (viewModules.firstElementChild.nextElementSibling) viewModules.lastElementChild.remove();
        viewModules.append(divModules);
        return;
    }

    // process modules
    selectedModuleId = modules[0].id;
    selectedCourseId = undefined;
    modules.forEach(module => divModules.append(createModule(module)));
    if (viewModules.firstElementChild.nextElementSibling) viewModules.lastElementChild.remove();
    viewModules.append(divModules);
    toggleCourseModuleStatus(viewModules, selectedModuleId);

    function createModule(module) {
        const a = document.createElement('a');
        a.className = 'list-group-item list-group-item-action py-3 lh-sm';
        a.href = `#/home`;
        a.setAttribute('data-value', module.id);
        a.addEventListener('click', e => {
            e.preventDefault();
            if (updateRatingsCue.size !== 0 ? confirm('There are unsaved ratings, do you really want to leave?') : true) {
                selectedModuleId = module.id;
                selectedCourseId = undefined;
                toggleCourseModuleStatus(viewModules, module.id);
                renderCourses();
                renderRatings();
            }
        });

        //create Heading
        const divHeading = document.createElement('div');
        divHeading.className = 'd-flex w-100 align-items-center justify-content-between mb-2';

        const strong = document.createElement('strong');
        strong.innerHTML = module.name;
        strong.className = 'fs-6';

        const small = document.createElement('small');
        small.innerHTML = module.shortName;
        small.className = 'text-muted';

        divHeading.append(strong, small);

        //create course infos
        const divInfos = document.createElement('div');
        divInfos.className = 'd-flex w-100 align-items-center justify-content-between col-10 mb-1 small';

        const spanStartEnd = document.createElement('span');
        spanStartEnd.innerHTML = `Start: ${module.startDate} End: ${module.endDate}`;

        divInfos.append(spanStartEnd);

        if (config.markMissingRatings && module.numberOfMissingRatings) {
            const spanMissingRatingsBadge = document.createElement('span');
            spanMissingRatingsBadge.innerHTML = `${module.numberOfMissingRatings} ratings missing`;
            spanMissingRatingsBadge.className = 'badge text-bg-danger';
            divInfos.append(spanMissingRatingsBadge);
        }

        a.append(divHeading, divInfos);

        return a

    }
}


function renderCourses() {

    // create module div
    const divCourses = document.createElement('div');
    divCourses.className = 'list-group list-group-flush border-bottom scrollarea';

    // create a for each module
    const courses = store.getCourses(selectedRoleName, selectedModuleId, config.onlyWithMissingRatings);

    if (courses.length === 0) {
        const spanNoCourse = document.createElement('span');
        spanNoCourse.innerHTML = 'No course found, change filter';
        divCourses.append(spanNoCourse);
    } else {
        courses.forEach(course => divCourses.appendChild(createCourse(course)))
    }

    if (viewCourses.firstElementChild.nextElementSibling) viewCourses.lastElementChild.remove();
    viewCourses.append(divCourses);

    function createCourse(course) {

        const a = document.createElement('a');
        a.className = 'list-group-item list-group-item-action p-3 lh-sm';
        a.setAttribute('data-value', course.id);
        a.href = '#';
        a.addEventListener('click', e => {
            e.preventDefault();
            if (updateRatingsCue.size !== 0 ? confirm('There are unsaved ratings, do you really want to leave?') : true) {
                selectedCourseId = course.id;
                toggleCourseModuleStatus(viewCourses, course.id);
                renderRatings();
            }
        })

        //create header div
        const divHeading = document.createElement('div');
        divHeading.className = 'd-flex w-100 align-items-center justify-content-between mb-2';

        const strong = document.createElement('strong');
        strong.innerHTML = course.name;
        strong.className = 'fs-6';

        const small = document.createElement('small');
        small.innerHTML = course.shortName;
        small.className = 'text-muted';

        divHeading.append(strong, small);

        //create course infos
        const divInfos = document.createElement('div');
        divInfos.className = 'd-flex w-100 align-items-center justify-content-between col-10 mb-1 small';

        const spanProfName = document.createElement('span');
        spanProfName.innerHTML = `Professor: ${course.professorFistName} ${course.professorLastName}`;

        divInfos.append(spanProfName);

        const spanWeightRatingsBadge = document.createElement('span');
        spanWeightRatingsBadge.innerHTML = 'Weight: ' + course.weight;
        spanWeightRatingsBadge.className = 'badge text-bg-secondary';
        divInfos.append(spanWeightRatingsBadge);

        if (course.numberOfMissingRatings) {
            const spanMissingRatingsBadge = document.createElement('span');
            spanMissingRatingsBadge.innerHTML = `${course.numberOfMissingRatings} ratings missing`;
            spanMissingRatingsBadge.className = 'badge text-bg-danger';
            divInfos.append(spanMissingRatingsBadge);
        }

        a.append(divHeading, divInfos);

        return a

    }
}


function renderRatings() {
    const viewRatings = document.querySelector('div.ratings');
    let btnSaveAll;

    if(!selectedModuleId && !selectedCourseId) {
        if (viewRatings.firstElementChild.nextElementSibling) viewRatings.lastElementChild.remove();
        const divRating = document.createElement('div');
        divRating.className = 'list-group list-group-flush border-bottom scrollarea';
        divRating.innerHTML = 'Select module or course to load ratings.';
        viewRatings.append(divRating);
        //clear header
        createRatingHeader();
        return;
    }
    const ratings = store.getRatings(selectedModuleId, selectedCourseId);
    if (ratings) {
        if (viewRatings.firstElementChild.nextElementSibling) viewRatings.lastElementChild.remove();
        const divRating = document.createElement('div');
        divRating.className = 'list-group list-group-flush border-bottom scrollarea';

        createRatingHeader(ratings[0]);
        ratings.forEach(rating => {
            divRating.append(createRating(rating))
        })
        viewRatings.append(divRating);
    } else {
        //get ratings
        viewRatings.lastElementChild.remove();
        const divMessage = document.createElement('div');
        divMessage.className = 'ratingMessage';
        divMessage.innerHTML = '<p>Loading ratings.....</p>';
        viewRatings.append(divMessage);

        service.getModuleRatings(selectedModuleId, selectedRoleName)
            .then(moduleRatings => {
                divMessage.remove();
                store.addRatings(selectedModuleId, moduleRatings);
                renderRatings()
            })
            .catch(error => {
                if (error.status === 401) logout();
                divMessage.innerHTML = "<p class='text-danger'>Could not load ratings</p>"
            })
    }

    function createRatingHeader(rating) {
        // to clear headers call function without parameter
        const header = viewRatings.firstElementChild;
        if (header.firstElementChild.nextElementSibling) header.firstElementChild.nextElementSibling.remove();
        if (rating) {
            const divCourseHeaders = document.createElement('div');
            divCourseHeaders.className = 'd-flex gap-2 align-items-center'
            rating.courseRatings.forEach(courseRating => {
                const divCourseHeader = document.createElement('div');
                divCourseHeader.className = 'text-center fw-semibold';
                divCourseHeader.style.width = '65px';
                if (config.canEditRatings) divCourseHeader.style.marginRight = '48px';
                divCourseHeader.innerHTML = courseRating.courseShortName;
                divCourseHeaders.append(divCourseHeader);
            })

            //btnSaveAll
            btnSaveAll = document.createElement('button');
            btnSaveAll.className = 'btn btn-success';
            btnSaveAll.innerHTML = 'Save All';
            btnSaveAll.addEventListener('click', function() {
                updateRatingsCue.forEach(rating => {
                    const inputRating = viewRatings.lastElementChild.querySelector(`[personId="${rating.personId}"] [courseId="${rating.courseId}"] input`);
                    if (!inputRating.reportValidity()) return;
                    util.updateViewField('msgSavingAll', 'Saving all ratings')
                    util.updateViewField('msgAllError', '')
                    util.updateViewField(`msgError-${rating.personId}`, '')
                    const saveRating = {
                        'personId': rating.personId,
                        'courseId': rating.courseId,
                        'successRate': inputRating.value,
                        'version': rating.version
                    }

                    service.putCourseRatings(rating.courseId, [saveRating])
                        .then(updatedRatings => {
                            updatedRatings.forEach(updatedRating => {
                                store.updateRating(selectedModuleId, updatedRating)
                                renderUpdatedRating(updatedRating, true);
                            })
                            updateRatingsCue.delete(rating);
                            util.updateViewField('msgSavingAll', '')
                        })
                        .catch(error => {
                            if (error.status === 401) logout();
                            renderUpdatedRating(saveRating, false);
                            btnSaveAll.style.display = 'none';
                            util.updateViewField('msgAllError', 'Error unsaved Ratings')
                            util.updateViewField('msgSavingAll', '')
                        })

                    viewRatings.querySelector(`[personId="${rating.personId}"] .btn-success`).style.display = 'none';
                })

                btnSaveAll.style.display = 'none';

            })
            btnSaveAll.style.display = 'none';
            divCourseHeaders.append(btnSaveAll);

            // saving all message
            const spanSavingAllRatingMsg = document.createElement('span');
            spanSavingAllRatingMsg.setAttribute('data-field','msgSavingAll')
            spanSavingAllRatingMsg.className = 'text-primary p-1'
            divCourseHeaders.append(spanSavingAllRatingMsg);

            // all error message
            const spanSaveAllRatingErrorMsg = document.createElement('span');
            spanSaveAllRatingErrorMsg.setAttribute('data-field','msgAllError')
            spanSaveAllRatingErrorMsg.className = 'text-danger p-1'
            divCourseHeaders.append(spanSaveAllRatingErrorMsg);

            header.append(divCourseHeaders);

        }

    }

    function createRating(rating) {
        updateRatingsCue = new Set();

        const divModuleStudentRating = document.createElement('div');
        divModuleStudentRating.className = 'd-flex gap-2 align-items-center list-group-item py-2 lh-sm';
        divModuleStudentRating.setAttribute('personId',rating.personId)

        const elements = [];
        //divStudentName
        const divStudentName = document.createElement('div');
        divStudentName.className = 'col-2 text-end pe-3';

        const strong = document.createElement('strong');
        strong.innerHTML = `${rating.firstName} ${rating.lastName}`;
        divStudentName.append(strong);
        elements.push(divStudentName);

        //divCourseRating
        let divInputRatings = [];
        rating.courseRatings.forEach(courseRating => {

            const divGroup = document.createElement('div');
            divGroup.className = 'input-group w-auto flex-nowrap';
            divGroup.setAttribute('courseId',courseRating.courseId)

            const inputRating = document.createElement('input');
            inputRating.name = 'successRate'
            inputRating.value = courseRating.successRate;
            inputRating.className = 'form-control text-center text-bg-light rounded-1 flex-grow-0 px-1';
            config.markMissingRatings && courseRating.successRate === null ? inputRating.classList.add('border', 'border-danger', 'border-2') : inputRating.classList.add('border-top-0', 'border-bottom-0')
            inputRating.readOnly = !config.canEditRatings;
            inputRating.style.width = '65px';
            inputRating.type = 'number';
            inputRating.max = '100';
            inputRating.min = '0';
            const header = viewRatings.firstElementChild.lastElementChild.children[rating.courseRatings.indexOf(courseRating)];

            divGroup.append(inputRating)

            // enable edit functions
            if (config.canEditRatings) {
                // show buttons if input have changed
                inputRating.addEventListener("input", function () {
                    btnSave.style.display = 'block';
                    btnSaveAll.style.display = 'block';
                    util.updateViewField(`msgSaving-${rating.personId}`, '')
                    util.updateViewField(`msgError-${rating.personId}`, '')
                    util.updateViewField('msgAllError', '')
                    inputRating.classList.remove('text-bg-light', 'text-bg-danger', 'text-bg-success')
                    inputRating.classList.add('text-bg-info');
                    updateRatingsCue.add(courseRating)
                });


                // Buttons
                // create delete button
                const btnDelete = document.createElement('button');
                btnDelete.className = 'btn btn-danger px-0';
                btnDelete.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-trash3" viewBox="0 0 16 16">\n' +
                    '  <path d="M6.5 1h3a.5.5 0 0 1 .5.5v1H6v-1a.5.5 0 0 1 .5-.5ZM11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3A1.5 1.5 0 0 0 5 1.5v1H2.506a.58.58 0 0 0-.01 0H1.5a.5.5 0 0 0 0 1h.538l.853 10.66A2 2 0 0 0 4.885 16h6.23a2 2 0 0 0 1.994-1.84l.853-10.66h.538a.5.5 0 0 0 0-1h-.995a.59.59 0 0 0-.01 0H11Zm1.958 1-.846 10.58a1 1 0 0 1-.997.92h-6.23a1 1 0 0 1-.997-.92L3.042 3.5h9.916Zm-7.487 1a.5.5 0 0 1 .528.47l.5 8.5a.5.5 0 0 1-.998.06L5 5.03a.5.5 0 0 1 .47-.53Zm5.058 0a.5.5 0 0 1 .47.53l-.5 8.5a.5.5 0 1 1-.998-.06l.5-8.5a.5.5 0 0 1 .528-.47ZM8 4.5a.5.5 0 0 1 .5.5v8.5a.5.5 0 0 1-1 0V5a.5.5 0 0 1 .5-.5Z"/>\n' +
                    '</svg>';
                btnDelete.style.visibility = 'hidden';
                btnDelete.style.width = '25px';
                btnDelete.addEventListener('click', function () {
                    inputRating.value = null;
                    inputRating.classList.add('text-bg-info');
                    inputRating.classList.remove('text-bg-light', 'text-bg-danger', 'text-bg-success')
                    util.updateViewField(`msgSaving-${rating.personId}`, '')
                    util.updateViewField('msgAllError', '')
                    btnSave.style.display = 'block';
                    updateRatingsCue.add(courseRating)
                })
                divGroup.append(btnDelete);

                //Create cancel button
                const btnCancel = document.createElement('button');
                btnCancel.className = 'btn btn-primary px-0';
                btnCancel.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-x-lg" viewBox="0 0 16 16">' +
                    '<path d="M2.146 2.854a.5.5 0 1 1 .708-.708L8 7.293l5.146-5.147a.5.5 0 0 1 .708.708L8.707 8l5.147 5.146a.5.5 0 0 1-.708.708L8 8.707l-5.146 5.147a.5.5 0 0 1-.708-.708L7.293 8 2.146 2.854Z"/>' +
                    '</svg>';
                btnCancel.style.visibility = 'hidden';
                btnCancel.style.width = '25px';
                btnCancel.addEventListener('click', function () {
                    inputRating.value = courseRating.successRate;
                    inputRating.classList.add('text-bg-light')
                    inputRating.classList.remove('text-bg-info', 'text-bg-danger', 'text-bg-success')
                    updateRatingsCue.delete(courseRating)
                    util.updateViewField(`msgSaving-${rating.personId}`, '')
                    util.updateViewField(`msgError-${rating.personId}`, '')
                    util.updateViewField('msgAllError', '')

                    // if no other ratings for the user are in cue
                    if(!Array.from(updateRatingsCue).find(rating => rating.personId === courseRating.personId)) {
                        btnSave.style.display = 'none';
                    }

                    // hide saveAll button if no rating is in update cue
                    if(updateRatingsCue.size === 0) btnSaveAll.style.display = 'none';

                })
                divGroup.append(btnCancel);

                divGroup.addEventListener('mouseover', function() {
                    inputRating.classList.add('border-dark')
                    btnDelete.style.visibility = 'visible';
                    btnCancel.style.visibility = 'visible';
                    header.classList.add('rounded-1', 'bg-dark', 'text-white', 'bg-opacity-75');
                })

                divGroup.addEventListener('mouseout', function() {
                    inputRating.classList.remove('border-dark')
                    btnDelete.style.visibility = 'hidden';
                    btnCancel.style.visibility = 'hidden';
                    header.classList.remove('rounded-1', 'bg-dark', 'text-white', 'bg-opacity-75');
                })
            }
            divInputRatings.push(divGroup);
        })
        divInputRatings.forEach(div => elements.push(div));

        // Average Success Rate
        // don't show if course is selected
        if (config.showAverageSuccessRate && !selectedCourseId) {
            //divAverageSuccessRate
            const divAverageSuccessRate = document.createElement('div');
            divAverageSuccessRate.className = 'd-flex justify-content-center align-items-center rounded-1';
            divAverageSuccessRate.style.width = '50px';
            divAverageSuccessRate.style.height = '36px';

            const averageSuccessRate = document.createElement('strong');
            averageSuccessRate.innerHTML = rating.averageSuccessRate;
            rating.averageSuccessRate < 51 ? divAverageSuccessRate.className += ' text-bg-danger' : divAverageSuccessRate.className += ' text-bg-success';
            divAverageSuccessRate.append(averageSuccessRate);
            elements.push(divAverageSuccessRate);
        }

        //btnSave
        const btnSave = document.createElement('button');
        btnSave.className = 'btn btn-success';
        btnSave.innerHTML = 'Save';
        btnSave.addEventListener('click', function() {

            util.updateViewField(`msgSaving-${rating.personId}`, 'Saving....')

            updateRatingsCue.forEach(uRating => {
                if (uRating.personId === rating.personId) {
                    const inputRating = viewRatings.lastElementChild.querySelector(`[personId="${uRating.personId}"] [courseId="${uRating.courseId}"] input`);
                    if (!inputRating.reportValidity()) return;


                    const saveRating = {
                        'personId': uRating.personId,
                        'courseId': uRating.courseId,
                        'successRate': inputRating.value,
                        'version': uRating.version
                    }

                    service.putCourseRatings(uRating.courseId, [saveRating])
                        .then(updatedRatings => {
                            updatedRatings.forEach(updatedRating => {
                                store.updateRating(selectedModuleId, updatedRating)
                                renderUpdatedRating(updatedRating, true);
                            })
                            updateRatingsCue.delete(uRating);
                            util.updateViewField(`msgSaving-${rating.personId}`, '')
                            if (updateRatingsCue.size === 0 ) btnSaveAll.style.display = 'none';
                        })
                        .catch(error => {
                            if (error.status === 401) logout();
                            renderUpdatedRating(saveRating, false);
                            btnSave.style.display = 'none';
                            btnSaveAll.style.display = 'none';
                            util.updateViewField(`msgSaving-${rating.personId}`, '')
                        })
                }
            })

            btnSave.style.display = 'none';

        })
        btnSave.style.display = 'none';
        elements.push(btnSave);

        // saving message
        const spanSavingRatingMsg = document.createElement('span');
        spanSavingRatingMsg.setAttribute('data-field',`msgSaving-${rating.personId}`)
        spanSavingRatingMsg.className = 'text-primary p-1'
        elements.push(spanSavingRatingMsg);

        // error message
        const spanSaveRatingErrorMsg = document.createElement('span');
        spanSaveRatingErrorMsg.setAttribute('data-field',`msgError-${rating.personId}`)
        spanSaveRatingErrorMsg.className = 'text-danger p-1'
        elements.push(spanSaveRatingErrorMsg);

        elements.forEach(div => divModuleStudentRating.append(div));

        return divModuleStudentRating

    }

    function renderUpdatedRating(rating, success) {
        //get ratings from user
        const inputRating = viewRatings.lastElementChild.querySelector(`[personId="${rating.personId}"] [courseId="${rating.courseId}"] input`)
        if (success) {
            inputRating.classList.remove('text-bg-light', 'text-bg-danger', 'text-bg-info')
            inputRating.classList.add('text-bg-success')
            inputRating.value = rating.successRate;
        } else {
            inputRating.classList.remove('text-bg-light', 'text-bg-info', 'text-bg-success')
            inputRating.classList.add('text-bg-danger')
            util.updateViewField(`msgError-${rating.personId}`, 'Error unsaved ratings')
        }
    }
}

function logout() {
    store.clear();
    router.navigate('/');
}
