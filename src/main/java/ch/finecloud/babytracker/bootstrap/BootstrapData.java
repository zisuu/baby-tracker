package ch.finecloud.babytracker.bootstrap;


import ch.finecloud.babytracker.entities.Baby;
import ch.finecloud.babytracker.entities.Event;
import ch.finecloud.babytracker.entities.UserAccount;
import ch.finecloud.babytracker.model.BabyCSVRecord;
import ch.finecloud.babytracker.model.EventCSVRecord;
import ch.finecloud.babytracker.model.EventType;
import ch.finecloud.babytracker.model.UserAccountCSVRecord;
import ch.finecloud.babytracker.repositories.BabyRepository;
import ch.finecloud.babytracker.repositories.EventRepository;
import ch.finecloud.babytracker.repositories.UserAccountRepository;
import ch.finecloud.babytracker.services.BabyCsvService;
import ch.finecloud.babytracker.services.EventCsvService;
import ch.finecloud.babytracker.services.UserCsvService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final UserAccountRepository userAccountRepository;
    private final BabyRepository babyRepository;
    private final EventRepository eventRepository;
//    private final BabyCsvService babyCsvService;
    private final UserCsvService userCsvService;
//    private final EventCsvService eventCsvService;
@Autowired
private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        loadUserAccountData();
        loadCsvData();
        loadBabyData();
        loadEventData();
    }

    private void loadCsvData() throws FileNotFoundException {
        if (userAccountRepository.count() < 10) {
            File file = ResourceUtils.getFile("classpath:csvdata/users.csv");

            List<UserAccountCSVRecord> userAccountCSVRecords = userCsvService.convertCSV(file);

            userAccountCSVRecords.forEach(userAccountCSVRecord -> {
                String username = userAccountCSVRecord.getUsername();
                String password = userAccountCSVRecord.getPassword();
                String email = userAccountCSVRecord.getEmail();

                userAccountRepository.save(UserAccount.builder()
                        .username(username)
                        .password(passwordEncoder.encode(password))
                        .email(email)
                        .build());
            });
        }
//
//        if (eventRepository.count() < 10) {
//            File file = ResourceUtils.getFile("classpath:csvdata/events.csv");
//
//            List<EventCSVRecord> eventCSVRecords = eventCsvService.convertCSV(file);
//
//            eventCSVRecords.forEach(eventCSVRecord -> {
//                EventType eventType = eventCSVRecord.getEventType();
//
//                eventRepository.save(Event.builder()
//                        .eventType(eventType)
//                        .build());
//            });
//        }
//        if (babyRepository.count() < 10) {
//            File file = ResourceUtils.getFile("classpath:csvdata/babies.csv");
//
//            List<BabyCSVRecord> babyCSVRecords = babyCsvService.convertCSV(file);
//
//            babyCSVRecords.forEach(babyCSVRecord -> {
//                String name = babyCSVRecord.getName();
//
//                babyRepository.save(Baby.builder()
//                        .name(name)
//                        .build());
//            });
//        }
    }

    private void loadUserAccountData() {
        // TODO: add roles to those users and those in the CSV file
        if (userAccountRepository.count() == 0) {
            UserAccount userAccount1 = UserAccount.builder()
                    .id(UUID.randomUUID())
                    .version(1)
                    .username("userAccount1")
                    .password(passwordEncoder.encode("password1"))
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            UserAccount userAccount2 = UserAccount.builder()
                    .id(UUID.randomUUID())
                    .version(2)
                    .username("userAccount2")
                    .password(passwordEncoder.encode("password2"))
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            UserAccount userAccount3 = UserAccount.builder()
                    .id(UUID.randomUUID())
                    .version(3)
                    .username("userAccount3")
                    .password(passwordEncoder.encode("password3"))
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            userAccountRepository.save(userAccount1);
            userAccountRepository.save(userAccount2);
            userAccountRepository.save(userAccount3);
        }
    }

    private void loadBabyData() {
        if (babyRepository.count() == 0) {
            Baby baby1 = Baby.builder()
                    .id(UUID.randomUUID())
                    .version(1)
                    .name("Max")
                    .userAccount(userAccountRepository.findAll().get(0))
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            Baby baby2 = Baby.builder()
                    .id(UUID.randomUUID())
                    .version(2)
                    .name("Miriam")
                    .userAccount(userAccountRepository.findAll().get(1))
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            Baby baby3 = Baby.builder()
                    .id(UUID.randomUUID())
                    .version(3)
                    .name("Paul")
                    .userAccount(userAccountRepository.findAll().get(2))
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
                    .baby(babyRepository.findAll().get(0))
                    .build();

            Event event2 = Event.builder()
                    .id(UUID.randomUUID())
                    .version(1)
                    .eventType(EventType.DIAPER)
                    .baby(babyRepository.findAll().get(1))
                    .build();

            Event event3 = Event.builder()
                    .id(UUID.randomUUID())
                    .version(1)
                    .eventType(EventType.FEEDING)
                    .baby(babyRepository.findAll().get(2))
                    .build();

            eventRepository.save(event1);
            eventRepository.save(event2);
            eventRepository.save(event3);
        }
    }
}
