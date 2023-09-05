package ch.finecloud.babytracker.controller;

import ch.finecloud.babytracker.model.EventDTO;
import ch.finecloud.babytracker.services.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
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
        if (!eventService.deleteById(eventId)) {
            log.debug("EventController.deleteById was called with eventId: " + eventId);
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(BASE_URL_ID)
    public ResponseEntity updateById(@PathVariable("eventId") UUID eventId, @Validated @RequestBody EventDTO eventDTO) {
        if (eventService.updateEventById(eventId, eventDTO).isEmpty()) {
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(BASE_URL_ID + "/baby/{babyId}")
//    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity createAssociation(@PathVariable("eventId") UUID eventId, @PathVariable("babyId") UUID babyId) {
        eventService.createAssociation(eventId, babyId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(BASE_URL)
    public ResponseEntity handlePost(@Validated @RequestBody EventDTO eventDTO) {
        EventDTO savedEventDTO = eventService.saveNewEvent(eventDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/events/" + savedEventDTO.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping(BASE_URL)
//    public Page<EventDTO> listEvents(@RequestParam(required = false) EventType eventType,
    public Page<EventDTO> listEvents(@RequestParam(required = false) UUID babyId,
                                     @RequestParam(required = false) Integer pageNumber,
                                     @RequestParam(required = false) Integer pageSize) {
        log.debug("listEvents was called, in Controller");
        return eventService.listEvents(babyId, pageNumber, pageSize);
    }

    @GetMapping(BASE_URL_ID)
    public EventDTO getEventById(@PathVariable("eventId") String eventIdStr) {
    try {
        UUID eventId = UUID.fromString(eventIdStr);
        log.debug("getEventById was called with id: " + eventId + ", in Controller");
        return eventService.getEventById(eventId).orElseThrow(NotFoundException::new);
    } catch (IllegalArgumentException e) {
        // Handle the case where eventIdStr is not a valid UUID
        throw new IllegalArgumentException("Invalid eventId");
    }
}


}
