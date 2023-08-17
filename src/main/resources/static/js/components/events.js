// import service from "../service.js";
// import store from '../store.js'
//
// function generateCalendarEvents(eventsData) {
//     const calendarEvents = eventsData.map(event => ({
//         start: event.startDate,
//         end: event.endDate,
//         resourceId: 1,
//         title: event.eventType,
//         notes: event.notes,
//     }));
//     return calendarEvents;
// }
//
//
// export default {
//     title: 'Events',
//     templatePath: 'events.html',
//     requiresAuth: true,
//     init: function (view, babyId) {
//         service.getEvents(babyId)
//             .then(data => data.content)
//             .then(eventsData => {
//                 const calendarEvents = generateCalendarEvents(eventsData);
//                 store.setEvents(calendarEvents);
//
//                 let ec = new EventCalendar(document.getElementById('ec'), {
//                     view: 'timeGridWeek',
//                     height: '800px',
//                     headerToolbar: {
//                         start: 'prev,next today',
//                         center: 'title',
//                         end: 'dayGridMonth,timeGridWeek,timeGridDay,listWeek resourceTimeGridWeek'
//                     },
//                     buttonText: function (texts) {
//                         texts.resourceTimeGridWeek = 'resources';
//                         return texts;
//                     },
//                     resources: [
//                         {id: 1, title: store.getBabies()[0].name},
//                     ],
//                     scrollTime: '09:00:00',
//                     // events: createEvents(),
//                     events: calendarEvents,
//                     views: {
//                         timeGridWeek: {pointer: true},
//                         resourceTimeGridWeek: {pointer: true}
//                     },
//                     dayMaxEvents: true,
//                     nowIndicator: true,
//                     selectable: true
//                 })
//             })
//     }
// }