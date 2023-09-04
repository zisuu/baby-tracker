package ch.finecloud.babytracker;

import ch.finecloud.babytracker.repositories.BabyRepository;
import ch.finecloud.babytracker.services.BabyServiceJPA;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BabyTrackerApplicationTests {

	@Autowired
	private BabyRepository babyRepository;

	@Test
	void contextLoads() {
		assertTrue(babyRepository.findAll().size() > 0);
	}

}
