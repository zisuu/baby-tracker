import router from './router.js';
import login from './components/login.js';
import home from './components/home.js';

router.register('/', login);
router.register('/home', home);

router.navigate('/');


