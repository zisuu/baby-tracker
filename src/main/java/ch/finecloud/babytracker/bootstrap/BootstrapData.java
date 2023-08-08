package ch.finecloud.babytracker.bootstrap;


import ch.finecloud.babytracker.entities.Baby;
import ch.finecloud.babytracker.entities.Event;
import ch.finecloud.babytracker.entities.Role;
import ch.finecloud.babytracker.entities.UserAccount;
import ch.finecloud.babytracker.mappers.BabyMapper;
import ch.finecloud.babytracker.model.EventType;
import ch.finecloud.babytracker.model.UserAccountCSVRecord;
import ch.finecloud.babytracker.repositories.BabyRepository;
import ch.finecloud.babytracker.repositories.EventRepository;
import ch.finecloud.babytracker.repositories.UserAccountRepository;
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
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private BabyMapper babyMapper;

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
                String email = userAccountCSVRecord.getEmail();
                String password = userAccountCSVRecord.getPassword();

                userAccountRepository.save(UserAccount.builder()
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .role(Role.USER)
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
                    .email("userAccount1")
                    .password(passwordEncoder.encode("password1"))
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .role(Role.USER)
                    .build();

            UserAccount userAccount2 = UserAccount.builder()
                    .id(UUID.randomUUID())
                    .version(2)
                    .email("userAccount2")
                    .password(passwordEncoder.encode("password2"))
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .role(Role.USER)
                    .build();

            UserAccount userAccount3 = UserAccount.builder()
                    .id(UUID.randomUUID())
                    .version(3)
                    .email("userAccount3")
                    .password(passwordEncoder.encode("password3"))
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .role(Role.USER)
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

            Baby baby4 = Baby.builder()
                    .id(UUID.randomUUID())
                    .version(4)
                    .name("Billy")
                    .userAccount(userAccountRepository.findUserAccountByEmail("userAccount2").get())
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            babyRepository.save(baby1);
            babyRepository.save(baby2);
            babyRepository.save(baby3);
            babyRepository.save(baby4);
        }
    }


    private void loadEventData() {
        if (eventRepository.count() == 0) {
            Event event1 = Event.builder()
                    .id(UUID.randomUUID())
                    .version(1)
                    .eventType(EventType.SLEEPING)
                    .startDate(LocalDateTime.now().minusHours(1))
                    .endDate(LocalDateTime.now())
                    .baby(babyRepository.findBabyByName("Billy"))
                    .build();

            Event event12 = Event.builder()
                    .id(UUID.randomUUID())
                    .version(1)
                    .eventType(EventType.BEDTIME)
                    .startDate(LocalDateTime.now().minusHours(4))
                    .endDate(LocalDateTime.now())
                    .baby(babyRepository.findBabyByName("Billy"))
                    .build();

            Event event13 = Event.builder()
                    .id(UUID.randomUUID())
                    .version(1)
                    .eventType(EventType.WAKEUP)
                    .startDate(LocalDateTime.now().minusHours(5))
                    .endDate(LocalDateTime.now())
                    .baby(babyRepository.findBabyByName("Billy"))
                    .build();

            Event event2 = Event.builder()
                    .id(UUID.randomUUID())
                    .version(1)
                    .eventType(EventType.DIAPER)
                    .startDate(LocalDateTime.now().minusHours(2).minusMinutes(1))
                    .endDate(LocalDateTime.now().minusHours(2))
                    .baby(babyRepository.findAll().get(1))
                    .build();

            Event event3 = Event.builder()
                    .id(UUID.randomUUID())
                    .version(1)
                    .eventType(EventType.FEEDING)
                    .startDate(LocalDateTime.now().minusHours(3))
                    .endDate(LocalDateTime.now().minusHours(3).minusMinutes(20))
                    .baby(babyRepository.findAll().get(2))
                    .build();

            eventRepository.save(event1);
            eventRepository.save(event12);
            eventRepository.save(event13);
            eventRepository.save(event2);
            eventRepository.save(event3);
        }
    }
}
