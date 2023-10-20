package com.unipi.msc.raiseupapi.Config;

import com.unipi.msc.raiseupapi.Model.User.User;
import com.unipi.msc.raiseupapi.Model.User.UserRepository;
import com.unipi.msc.raiseupapi.Shared.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final UserRepository userRepository;
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> {
            try {
                User user = userRepository.findByUsername(username).orElse(null);
                if (user == null){
                    user = userRepository.findByEmail(username).orElse(null);
                }
                if (user == null){
                    throw new UsernameNotFoundException(ErrorMessages.USER_NOT_FOUND);
                }
                if (user.getUsername() == null){
                    user.setUsername(user.getEmail());
                }
                return user;
            }catch (Exception ignore){
                return null;
            }
        };
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}