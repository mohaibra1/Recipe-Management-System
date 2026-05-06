package recipes.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import recipes.model.AppUser;
import recipes.repository.AppUserRepository;

@Service
public class AppUserDetailsServiceImpl implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public AppUserDetailsServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findAppUserByUsername(username.toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new AppUserAdapter(user);
    }
}
