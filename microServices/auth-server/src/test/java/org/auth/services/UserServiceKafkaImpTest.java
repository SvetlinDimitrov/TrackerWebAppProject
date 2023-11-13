package org.auth.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.auth.UserRepository;
import org.auth.model.dto.RegisterUserDto;
import org.auth.model.dto.UserView;
import org.auth.model.entity.User;
import org.auth.util.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceKafkaImpTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidator validator;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private UserServiceKafkaImp userServiceKafkaImp;

    private User user;
    private RegisterUserDto registerUserDto;
    private ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    
    @BeforeEach
    public void setUp() {
        user = new User();
        user.setEmail("email@abv.bg");
        user.setUsername("username");
        user.setPassword("password");
        new UserView(user);
        registerUserDto = new RegisterUserDto();
        registerUserDto.setEmail("email@abv.bg");
        registerUserDto.setUsername("username");
        registerUserDto.setPassword("password");
    }

    @Test
    public void register_ValidInput_SuccessfulRegister() {

        when(passwordEncoder.encode(user.getPassword())).thenReturn("password");
        when(validator.validateFullRegistration(any(User.class))).thenReturn(false);
        
        userServiceKafkaImp.register(registerUserDto);

        verify(userRepository).saveAndFlush(userCaptor.capture());
        
        User savedUser = userCaptor.getValue();

        assertEquals(savedUser.getEmail(), user.getEmail());
        assertEquals(savedUser.getUsername(), user.getUsername());

    }
}
