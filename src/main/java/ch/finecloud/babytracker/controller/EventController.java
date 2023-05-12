package ch.finecloud.babytracker.controller;

import ch.finecloud.babytracker.model.EventDTO;
import ch.finecloud.babytracker.services.EventService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
public class EventController {
    private final EventService eventService;
    public static final String BASE_URL = "/api/v1/events";
    public static final String BASE_URL_ID = BASE_URL + "/{eventId}";

    @PatchMapping(BASE_URL_ID)
    public ResponseEntity updateEventPatchById(@PathVariable("eventId") UUID eventId, @RequestBody EventDTO eventDTO) {
        eventService.patchEventById(eventId, eventDTO);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(BASE_URL_ID)
    public ResponseEntity deleteById(@PathVariable("eventId") UUID eventId) {
        eventService.deleteById(eventId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(BASE_URL_ID)
    public ResponseEntity updateById(@PathVariable("eventId") UUID eventId, @RequestBody EventDTO eventDTO) {
        eventService.updateEventById(eventId, eventDTO);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(BASE_URL)
    public ResponseEntity handlePost(@RequestBody EventDTO eventDTO) {
        EventDTO savedEventDTO = eventService.saveNewEvent(eventDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/events/" + savedEventDTO.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping(BASE_URL)
    public List<EventDTO> listEvents() {
        log.debug("listEvents was called, in Controller");
        return eventService.listEvents();
    }

    @GetMapping(BASE_URL_ID)
    public EventDTO getEventById(@PathVariable("eventId")  UUID eventId) {
        log.debug("getEventById was called with id: " + eventId + ", in Controller");
        return eventService.getEventById(eventId).orElseThrow(NotFoundException::new);
    }

}
