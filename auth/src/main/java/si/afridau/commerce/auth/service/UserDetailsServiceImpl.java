package si.afridau.commerce.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import si.afridau.commerce.auth.repository.UserRepo;
import si.afridau.commerce.auth.model.User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> applicationUser = userRepo.findByEmail(username);
        if (applicationUser.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        return applicationUser.get();
    }
}
