package ch.finecloud.babytracker.controller;

import ch.finecloud.babytracker.model.UserAccountDTO;
import ch.finecloud.babytracker.services.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        userAccountDTO.setPassword(passwordEncoder.encode(userAccountDTO.getPassword()));
        UserAccountDTO savedUserAccountDTO = userAccountService.saveNewUser(userAccountDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/users/" + savedUserAccountDTO.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping(BASE_URL)
//    @PreAuthorize("hasRole('ROLE_ADMIN') or #email == authentication.principal.email")
    public Page<UserAccountDTO> listUsers(@RequestParam(required = false) String email,
                                          @RequestParam(required = false) Integer pageNumber,
                                          @RequestParam(required = false) Integer pageSize) {
        log.debug("listUsers was called, in Controller");
        return userAccountService.listUsers(email, pageNumber, pageSize);
    }

    @GetMapping(BASE_URL_ID)
    @PreAuthorize("#userId == authentication.principal.id")
    public UserAccountDTO getUserById(@PathVariable("userId") UUID userId) {
        log.debug("getUserById was called with id: " + userId + ", in Controller");
        return userAccountService.getUserById(userId).orElseThrow(NotFoundException::new);
    }

}
