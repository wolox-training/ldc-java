package wolox.training.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import wolox.training.models.User;
import wolox.training.repositories.UserRepository;
import wolox.training.security.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    @SuppressWarnings("unused")
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findFirstByUsername(username);
        CustomUserDetails userDetails =
            new CustomUserDetails(userRepository.findFirstByUsername(username));
        if (userDetails != null && userDetails.getUser() != null) {
            return userDetails;
        }
        throw new UsernameNotFoundException(username);
    }
}