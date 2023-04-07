package com.limatech.juriprocessos.services.user;

import com.limatech.juriprocessos.dtos.users.CreateUserDTO;
import com.limatech.juriprocessos.exceptions.users.InvalidPropertyException;
import com.limatech.juriprocessos.exceptions.users.UserAlreadyExistsException;
import com.limatech.juriprocessos.models.users.User;
import com.limatech.juriprocessos.repository.users.UserRepository;
import com.limatech.juriprocessos.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldThrowUserAlreadyExistsExceptionWhenEmailAlreadyExists() {
        // given
        CreateUserDTO userDTO = new CreateUserDTO("john.de","John Dee", "john@example.com", "password");
        User user = new User("john.de", "John", "john@example.com", "password");
        Optional<User> optionalUser = Optional.of(user);

        // when
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(optionalUser);

        // then
        assertThrows(UserAlreadyExistsException.class, () -> userService.validateIfUserAlreadyExists(userDTO));
    }

    @Test
    void shouldThrowUserAlreadyExistsExceptionWhenUsernameAlreadyExists() {
        // given
        CreateUserDTO userDTO = new CreateUserDTO("john.dee","John Dee", "john@example.com", "password");
        User user = new User("john.de", "John", "john@example.com", "password");
        Optional<User> optionalUser = Optional.of(user);

        // when
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(optionalUser);

        // then
        assertThrows(UserAlreadyExistsException.class, () -> userService.validateIfUserAlreadyExists(userDTO));
    }

    @Test
    void shouldThrowInvalidPropertyExceptionWhenEmailIsInvalid() {

        assertThrows(InvalidPropertyException.class, () -> {
            CreateUserDTO userDTO = new CreateUserDTO("john.dee","John Dee", "johndee", "password");
        });
    }

    @Test
    void shouldReturn204AndDeleteUser() {
        CreateUserDTO userDTO = new CreateUserDTO("john.dee","John Dee", "john@example.com", "password");
        User user = userService.createUser(userDTO);

        userService.deleteUser(user.getId());


    }
}
