import router from './router.js';
import login from './components/login.js';
import register from './components/register.js';
import logout from './components/logout.js';
import home from './components/home.js';
import events from './components/events.js';
import addBaby from './components/add-baby.js';
import addEvent from './components/add-event.js';

router.register('/', login);
router.register('/login', login);
router.register('/register', register);
router.register('/logout', logout);
router.register('/home', home);
router.register('/events', events);
router.register('/add-baby', addBaby);
router.register('/add-event', addEvent);

router.navigate('/');
