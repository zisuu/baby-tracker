package ch.finecloud.babytracker.controller;

import ch.finecloud.babytracker.model.Baby;
import ch.finecloud.babytracker.services.BabyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@RestController
public class BabyController {
    private final BabyService babyService;
    public static final String BASE_URL = "/api/v1/babys";
    public static final String BASE_URL_ID = BASE_URL + "/{babyId}";

    @PatchMapping(BASE_URL_ID)
    public ResponseEntity updateCustomerPatchById(@PathVariable("babyId") UUID babyId, @RequestBody Baby baby) {
        babyService.patchBabyById(babyId, baby);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(BASE_URL_ID)
    public ResponseEntity deleteById(@PathVariable("babyId") UUID babyId) {
        babyService.deleteById(babyId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(BASE_URL_ID)
    public ResponseEntity updateById(@PathVariable("babyId") UUID babyId, @RequestBody Baby baby) {
        babyService.updateBabyById(babyId, baby);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(BASE_URL)
    public ResponseEntity handlePost(@RequestBody Baby baby) {
        Baby savedBaby = babyService.saveNewBaby(baby);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/babys/" + savedBaby.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping(BASE_URL)
    public List<Baby> listCustomers() {
        log.debug("listCustomers was called, in Controller");
        return babyService.listBabys();
    }

    @GetMapping(BASE_URL_ID)
    public Baby getCustomerById(@PathVariable("babyId") UUID babyId) {
        log.debug("getCustomerById was called with id: " + babyId + ", in Controller");
        return babyService.getBabyById(babyId).orElseThrow(NotFoundException::new);
    }

}
