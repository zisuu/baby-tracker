//package ch.finecloud.babytracker.bootstrap;
//
//import ch.finecloud.babytracker.repositories.BabyRepository;
//import ch.finecloud.babytracker.repositories.EventRepository;
//import ch.finecloud.babytracker.repositories.UserAccountRepository;
//import ch.finecloud.babytracker.services.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//
//@DataJpaTest
//@Import({UserCsvServiceImpl.class})
//class BootstrapDataTest {
//
//    @Autowired
//    BabyRepository babyRepository;
//
//    @Autowired
//    EventRepository eventRepository;
//
//    @Autowired
//    UserAccountRepository userAccountRepository;
//
////    @Autowired
////    EventCsvService eventCsvService;
////
////    @Autowired
////    BabyCsvService babyCsvService;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    @Autowired
//    UserCsvService userCsvService;
//
//    BootstrapData bootstrapData;
//
//    @BeforeEach
//    void setUp() {
////        bootstrapData = new BootstrapData(userAccountRepository, babyRepository, eventRepository, userCsvService, eventCsvService, babyCsvService);
//        bootstrapData = new BootstrapData(userAccountRepository, babyRepository, eventRepository, userCsvService, passwordEncoder);
//    }
//
//    @Test
//    void Testrun() throws Exception {
//        bootstrapData.run(null);
//        assertThat(userAccountRepository.count()).isEqualTo(103);
//        assertThat(babyRepository.count()).isEqualTo(4);
//        assertThat(eventRepository.count()).isEqualTo(5);
//    }
//
//
//}