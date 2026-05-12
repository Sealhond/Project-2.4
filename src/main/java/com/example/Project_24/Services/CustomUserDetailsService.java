package com.example.Project_24.Services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    //user repository hier

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        //User user = userRepository.findByEmail ......

        //lange naam want heb zelf ook model User, kan evt importen met alias
//        return new org.springframework.security.core.userdetails.User(
//                user.getEmail(),
//                user.getPassword(),
//
                // list.of want spring expects een collection:
                // List.of(user.getRole())
        //        );

        return null;
    }
}
