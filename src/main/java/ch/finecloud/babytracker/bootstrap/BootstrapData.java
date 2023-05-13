package ch.finecloud.babytracker.bootstrap;


import ch.finecloud.babytracker.entities.Baby;
import ch.finecloud.babytracker.entities.Event;
import ch.finecloud.babytracker.model.EventType;
import ch.finecloud.babytracker.repositories.BabyRepository;
import ch.finecloud.babytracker.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final BabyRepository babyRepository;
    private final EventRepository eventRepository;

    @Override
    public void run(String... args) throws Exception {
        loadBabyData();
        loadEventData();
    }

    private void loadBabyData() {
        if (babyRepository.count() == 0) {
            Baby baby1 = Baby.builder()
                    .id(UUID.randomUUID())
                    .version(1)
                    .name("Hans Fischer")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            Baby baby2 = Baby.builder()
                    .id(UUID.randomUUID())
                    .version(2)
                    .name("Peter Muster")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            Baby baby3 = Baby.builder()
                    .id(UUID.randomUUID())
                    .version(3)
                    .name("Max Müller")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            babyRepository.save(baby1);
            babyRepository.save(baby2);
            babyRepository.save(baby3);
        }
    }

    private void loadEventData() {
        if (eventRepository.count() == 0) {
            Event event1 = Event.builder()
                    .id(UUID.randomUUID())
                    .version(1)
                    .eventType(EventType.SLEEPING)
                    .build();

            Event event2 = Event.builder()
                    .id(UUID.randomUUID())
                    .version(1)
                    .eventType(EventType.DIAPER)
                    .build();

            Event event3 = Event.builder()
                    .id(UUID.randomUUID())
                    .version(1)
                    .eventType(EventType.FEEDING)
                    .build();

            eventRepository.save(event1);
            eventRepository.save(event2);
            eventRepository.save(event3);
        }
    }
}
