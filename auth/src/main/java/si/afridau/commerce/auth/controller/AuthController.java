package si.afridau.commerce.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import si.afridau.commerce.auth.model.Role;
import si.afridau.commerce.auth.model.User;
import si.afridau.commerce.auth.model.UserRole;
import si.afridau.commerce.auth.repository.GroupRepo;
import si.afridau.commerce.auth.repository.UserGroupRepo;
import si.afridau.commerce.auth.repository.UserRepo;
import si.afridau.commerce.auth.request.LoginRequest;
import si.afridau.commerce.auth.request.RegisterRequest;
import si.afridau.commerce.auth.service.JwtService;

@CrossOrigin
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserGroupRepo userGroupRepo;
    private final GroupRepo groupRepo;

    @Value("${constants.deafult-register-group}")
    private String defaultGroupName;
    @PostMapping("/login")
    public void login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        //TODO move logic to service
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationRequest);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        String token = jwtService.generateToken(userRepo.findByEmail(loginRequest.getUsername()).orElseThrow(() -> new IllegalCallerException("Missing user in db")));
        response.setHeader("Authorization", "Bearer " + token);
    }

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest registerRequest) {
        if (defaultGroupName == null || defaultGroupName.isEmpty()) {
            throw new IllegalArgumentException("Server error");
        }

        Role defaultRole = groupRepo.findByName(defaultGroupName).orElseThrow(() -> new IllegalArgumentException("Default group not found"));

        if (!registerRequest.getPassword().equals(registerRequest.getPasswordRepeated())) {
            throw new IllegalArgumentException("Password mismatch");
        }
        userRepo.findByEmail(registerRequest.getEmail())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("User with email already exists");
                });

        if (registerRequest.getEmail() == null ||registerRequest.getEmail().trim().equals("")) {
            throw new IllegalArgumentException("Email required");
        }

        User user = new User();
        //TODO check data not empty, firstname and lastname
        user.setFirstname(registerRequest.getFirstname());
        user.setLastname(registerRequest.getLastname());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userRepo.save(user);

        UserRole ug = new UserRole();
        ug.setRoleId(defaultRole.getId());
        ug.setUserId(user.getId());
        userGroupRepo.save(ug);
    }
}
