import service from "../service.js";
import router from '../router.js';
import store from '../store.js';

export default {
    title: 'Add Baby',
    templatePath: 'add-baby.html',
    requiresAuth: true,
    init: function (view) {
        view.querySelector('[data-action=cancel]').addEventListener('click', e => {
            e.preventDefault();
            router.navigate('/home');
        });

        view.querySelector('[data-action=add]').addEventListener('click', e => {
            e.preventDefault();

            const form = view.querySelector('form');
            if (!form.reportValidity()) return;
            const baby = getFormData(form);
            let babyId = "";
            service.postBaby(baby)
                .then(response => {
                    babyId = response.headers.get('Location').split('/').pop();
                    return service.getBaby(babyId);
                })
                .then(baby => {
                    store.addBaby(baby);
                    router.navigate('/home');
                    return service.putBabyToUser(babyId, store.getUserAccountInfos().id);
                })
                .catch(error => view.querySelector('[data-field=error]').innerHTML = "Adding baby failed! msg: " + error);
            router.navigate('/home');
        });
    }
}


function getFormData(form) {
    return {
        name: form.name.value
    };
}
