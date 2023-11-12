package org.auth.services;

import org.auth.UserRepository;
import org.auth.util.UserValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public abstract class AbstractUserService {
    
    protected final UserValidator validator;
    protected final PasswordEncoder passwordEncoder;
    protected final UserRepository userRepository;

}
