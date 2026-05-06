package recipes.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import recipes.model.AppUser;
import recipes.repository.AppUserRepository;

@RestController
public class AuthController {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/api/register")
// Added @Valid to trigger the constraints on the record
    public ResponseEntity<Void> registerUser(@Valid @RequestBody RegistrationRequest request) {
        String username = request.email; // Records use accessor methods without 'get'

        if(appUserRepository.findAppUserByUsername(username.toLowerCase()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        var user = new AppUser();
        user.setUsername(username.toLowerCase());
        user.setPassword(passwordEncoder.encode(request.password));
        user.setAuthority("ROLE_USER");

        appUserRepository.save(user);
        return ResponseEntity.ok().build();
    }

    record RegistrationRequest(@NotBlank @Pattern(regexp = ".+@.+\\..+") String email,
                               @NotBlank @Size(min = 8) String password) { }}
