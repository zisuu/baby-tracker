import router from './router.js';
import login from './components/login.js';
import register from './components/register.js';
import logout from './components/logout.js';
import dashboard from './components/dashboard.js';
import events from './components/events.js';
import addBaby from './components/add-baby.js';
import addEvent from './components/add-event.js';

router.register('/', login);
router.register('/login', login);
router.register('/register', register);
router.register('/logout', logout);
router.register('/dashboard', dashboard);
router.register('/events', events);
router.register('/add-baby', addBaby);
router.register('/add-event', addEvent);

router.navigate('/');
