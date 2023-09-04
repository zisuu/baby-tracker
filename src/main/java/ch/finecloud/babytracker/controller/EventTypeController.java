package ch.finecloud.babytracker.controller;

import ch.finecloud.babytracker.model.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class EventTypeController {
    public static final String BASE_URL = "/api/v1/eventtypes";

    @GetMapping(BASE_URL)
    public EventType[] listEventTypes() {
        log.debug("listEventTypes was called, in Controller");
        return EventType.values();
    }

}
