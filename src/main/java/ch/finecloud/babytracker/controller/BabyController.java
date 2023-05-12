package ch.finecloud.babytracker.controller;

import ch.finecloud.babytracker.model.BabyDTO;
import ch.finecloud.babytracker.services.BabyService;
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
public class BabyController {
    private final BabyService babyService;
    public static final String BASE_URL = "/api/v1/babys";
    public static final String BASE_URL_ID = BASE_URL + "/{babyId}";

    @PatchMapping(BASE_URL_ID)
    public ResponseEntity updateCustomerPatchById(@PathVariable("babyId") UUID babyId, @RequestBody BabyDTO babyDTO) {
        babyService.patchBabyById(babyId, babyDTO);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(BASE_URL_ID)
    public ResponseEntity deleteById(@PathVariable("babyId") UUID babyId) {
        if (!babyService.deleteById(babyId)) {
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(BASE_URL_ID)
    public ResponseEntity updateById(@PathVariable("babyId") UUID babyId, @RequestBody BabyDTO babyDTO) {
        if (babyService.updateBabyById(babyId, babyDTO).isEmpty()) {
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(BASE_URL)
    public ResponseEntity handlePost(@RequestBody BabyDTO babyDTO) {
        BabyDTO savedBabyDTO = babyService.saveNewBaby(babyDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/babys/" + savedBabyDTO.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping(BASE_URL)
    public List<BabyDTO> listBabys() {
        log.debug("listCustomers was called, in Controller");
        return babyService.listBabys();
    }

    @GetMapping(BASE_URL_ID)
    public BabyDTO getBabyById(@PathVariable("babyId") UUID babyId) {
        log.debug("getCustomerById was called with id: " + babyId + ", in Controller");
        return babyService.getBabyById(babyId).orElseThrow(NotFoundException::new);
    }

}
