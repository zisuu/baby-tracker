package ch.finecloud.babytracker.controller;

import ch.finecloud.babytracker.model.UserAccountDTO;
import ch.finecloud.babytracker.services.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserAccountController {
    private final UserAccountService userAccountService;
    public static final String BASE_URL = "/api/v1/users";
    public static final String BASE_URL_ID = BASE_URL + "/{userId}";

    @PatchMapping(BASE_URL_ID)
    public ResponseEntity updateUserPatchById(@PathVariable("userId") UUID userId, @RequestBody UserAccountDTO userAccountDTO) {
        userAccountService.patchUserById(userId, userAccountDTO);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(BASE_URL_ID)
    public ResponseEntity deleteById(@PathVariable("userId") UUID userId) {
        if (!userAccountService.deleteById(userId)) {
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(BASE_URL_ID)
    public ResponseEntity updateById(@PathVariable("userId") UUID userId, @Validated @RequestBody UserAccountDTO userAccountDTO) {
        if (userAccountService.updateUserById(userId, userAccountDTO).isEmpty()) {
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(BASE_URL)
    public ResponseEntity handlePost(@Validated @RequestBody UserAccountDTO userAccountDTO) {
        UserAccountDTO savedUserAccountDTO = userAccountService.saveNewUser(userAccountDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/users/" + savedUserAccountDTO.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping(BASE_URL)
    public Page<UserAccountDTO> listUsers(@RequestParam(required = false) String username,
                                          @RequestParam(required = false) Integer pageNumber,
                                          @RequestParam(required = false) Integer pageSize) {
        log.debug("listUsers was called, in Controller");
        return userAccountService.listUsers(username, pageNumber, pageSize);
    }

    @GetMapping(BASE_URL_ID)
    public UserAccountDTO getUserById(@PathVariable("userId") UUID userId) {
        log.debug("getUserById was called with id: " + userId + ", in Controller");
        return userAccountService.getUserById(userId).orElseThrow(NotFoundException::new);
    }

}
