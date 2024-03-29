package ch.finecloud.babytracker.controller;

import ch.finecloud.babytracker.model.BabyDTO;
import ch.finecloud.babytracker.services.BabyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BabyController {
    private final BabyService babyService;
    public static final String BASE_URL = "/api/v1/babies";
    public static final String BASE_URL_ID = BASE_URL + "/{babyId}";

    @PatchMapping(BASE_URL_ID)
    public ResponseEntity<Void> updateCustomerPatchById(@PathVariable("babyId") UUID babyId, @RequestBody BabyDTO babyDTO) {
        babyService.patchBabyById(babyId, babyDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(BASE_URL_ID)
    public ResponseEntity<Void> deleteById(@PathVariable("babyId") UUID babyId) {
        if (Boolean.FALSE.equals(babyService.deleteById(babyId))) {
            throw new NotFoundException();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(BASE_URL_ID)
    public ResponseEntity<Void> updateById(@PathVariable("babyId") UUID babyId, @Validated @RequestBody BabyDTO babyDTO) {
        if (babyService.updateBabyById(babyId, babyDTO).isEmpty()) {
            throw new NotFoundException();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(BASE_URL_ID + "/user/{userId}")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<Void> createAssociation(@PathVariable("babyId") UUID babyId, @PathVariable("userId") UUID userId) {
        babyService.createAssociation(babyId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(BASE_URL)
    public ResponseEntity<Void> handlePost(@Validated @RequestBody BabyDTO babyDTO) {
        BabyDTO savedBabyDTO = babyService.saveNewBaby(babyDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/babies/" + savedBabyDTO.getId().toString());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(BASE_URL)
    public Page<BabyDTO> listBabies(@RequestParam(required = false) String babyName,
                                    @RequestParam(required = false) Integer pageNumber,
                                    @RequestParam(required = false) Integer pageSize, Principal principal) {
        log.debug("listBabies was called, in Controller");
        return babyService.listBabiesByUserAccountEmail(principal.getName(), babyName, pageNumber, pageSize);
    }

    @GetMapping(BASE_URL_ID)
    public BabyDTO getBabyById(@PathVariable("babyId") UUID babyId) {
        log.debug("getBabyById was called with id: " + babyId + ", in Controller");
        return babyService.getBabyById(babyId).orElseThrow(NotFoundException::new);
    }

}
