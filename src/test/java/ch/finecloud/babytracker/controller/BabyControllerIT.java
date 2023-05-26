package ch.finecloud.babytracker.controller;

import ch.finecloud.babytracker.entities.Baby;
import ch.finecloud.babytracker.mappers.BabyMapper;
import ch.finecloud.babytracker.model.BabyDTO;
import ch.finecloud.babytracker.repositories.BabyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class BabyControllerIT {

    @Autowired
    BabyController babyController;

    @Autowired
    BabyRepository babyRepository;

    @Autowired
    BabyMapper babyMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    void testListBabyByName() throws Exception {
        mockMvc.perform(get(BabyController.BASE_URL)
                        .queryParam("name", "Miriam")
                        .with(httpBasic(BabyControllerTest.USERNAME, BabyControllerTest.PASSWORD))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", CoreMatchers.is(1)));
    }

    @Test
    void testPatchBabyBadName() throws Exception {
        Baby testBaby = babyRepository.findAll().get(0);
        Map<String, Object> babyMap = new HashMap<>();
        babyMap.put("name", "NewName1231231231231223312322342342234234234234342341123");
        MvcResult mvcResult = mockMvc.perform(patch(BabyController.BASE_URL_ID, testBaby.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(babyMap))
                .with(httpBasic(BabyControllerTest.USERNAME, BabyControllerTest.PASSWORD)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", CoreMatchers.is(1)))
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testDeleteByIdNotFound() {
        assertThrows(NotFoundException.class, () -> babyController.deleteById(UUID.randomUUID()));
    }

    @Rollback
    @Transactional
    @Test
    void deleteByIdFound() {
        Baby testBaby = babyRepository.findAll().get(0);
        ResponseEntity responseEntity = babyController.deleteById(testBaby.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(babyRepository.findById(testBaby.getId()).isEmpty());
    }

    @Test
    void testUpdateNotFound() {
        assertThrows(NotFoundException.class, () -> babyController.updateById(UUID.randomUUID(), BabyDTO.builder().build()));
    }

    @Rollback
    @Transactional
    @Test
    void updateExistingBaby() {
        Baby testBaby = babyRepository.findAll().get(0);
        BabyDTO babyDTO = babyMapper.babyToBabyDto(testBaby);
        babyDTO.setId(null);
        babyDTO.setVersion(null);
        babyDTO.setName("Updated Baby");
        ResponseEntity responseEntity = babyController.updateById(testBaby.getId(), babyDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        Baby updatedBaby = babyRepository.findById(testBaby.getId()).get();
        assertThat(updatedBaby.getName()).isEqualTo("Updated Baby");
    }

//    @Rollback
//    @Transactional
//    @Test
//    void saveNewBabyTest() {
//        BabyDTO babyDTO = BabyDTO.builder()
//                .name("New Baby")
//                .build();
//        ResponseEntity responseEntity = babyController.handlePost(babyDTO);
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
//        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
//        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
//        UUID savedUUID = UUID.fromString(locationUUID[4]);
//        Baby baby = babyRepository.findById(savedUUID).get();
//        assertThat(baby).isNotNull();
//    }

    @Test
    void testBabyByIdNotFound() {
        assertThrows(NotFoundException.class, () -> babyController.getBabyById(UUID.randomUUID()));
    }

    @Test
    void testGetById() {
        Baby testBaby = babyRepository.findAll().get(0);
        BabyDTO babyDTO = babyController.getBabyById(testBaby.getId());
        assertThat(babyDTO).isNotNull();
    }

    @Test
    void testListBabies() {
        Page<BabyDTO> babyDTOList = babyController.listBabies(null, 1, 25);
        assertThat(babyDTOList.getContent().size()).isEqualTo(3);
    }

//    @Rollback
//    @Transactional
//    @Test
//    void testEmptyList() {
//        babyRepository.deleteAll();
//        Page<BabyDTO> babyDTOList = babyController.listBabies(null, 1, 25);
//        assertThat(babyDTOList.getContent().size()).isEqualTo(0);
//    }
}

