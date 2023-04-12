package com.limatech.juriprocessos.services.user;

import com.limatech.juriprocessos.dtos.users.CreateUserDTO;
import com.limatech.juriprocessos.exceptions.users.InvalidPropertyException;
import com.limatech.juriprocessos.exceptions.users.UserAlreadyExistsException;
import com.limatech.juriprocessos.exceptions.users.UserNotFoundException;
import com.limatech.juriprocessos.models.users.User;
import com.limatech.juriprocessos.repository.users.UserRepository;
import com.limatech.juriprocessos.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockitoSession;
import static org.mockito.Mockito.when;

class UserServiceTest {

    UserService userService;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setup() {

        UserRepository userRepository = Mockito.mock(UserRepository.class);
        this.userService = new UserService(userRepository);
    }

    @Test
    void shouldThrowUserAlreadyExistsExceptionWhenEmailAlreadyExists() {
        // given
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(userRepository);

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
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(userRepository);

        CreateUserDTO userDTO = new CreateUserDTO("john.dee","John Dee", "john@example.com", "password");
        User user = new User("john.de", "John", "john@example.com", "password");
        Optional<User> optionalUser = Optional.of(user);

        // when
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(optionalUser);
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
    void shouldThrowUserNotFoundExceptionWhenTryToDeleteInexistentUser() {
        // given
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(userRepository);

        UUID id = UUID.randomUUID();

        // then
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(id));
    }

    @Test
    void shouldCallDeleteUserById() {
        // given
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(userRepository);

        CreateUserDTO userDTO = new CreateUserDTO("john.dee","John Dee", "john@example.com", "password");
        User user = userDTO.toUserEntity();
        UUID id = UUID.randomUUID();

        user.setId(id);

        // when
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // then
        userService.deleteUser(user.getId());
        Mockito.verify(userRepository).deleteById(user.getId());

    }

    @Test
    void shouldCallSaveMethodWhenUpdateUser() {
        // given
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(userRepository);

        CreateUserDTO userDTO = new CreateUserDTO("john.dee","John Dee", "john@example.com", "password");
        CreateUserDTO userUpdatedDTO = new CreateUserDTO("foo.bar","Foo Bar", "foo@example.com", "password");

        User user = userDTO.toUserEntity();
        User userUpdated = userUpdatedDTO.toUserEntity();
        UUID id = UUID.randomUUID();
        user.setId(id);
        userUpdated.setId(id);

        // when
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // then
        userService.updateUser(user.getId(), userUpdatedDTO);
        Mockito.verify(userRepository).save(Mockito.any(User.class));

    }

    @Test
    void shouldThrownUserNotFoundExcepetionWhenTryToUpdateAnInexistentUser() {
        // given
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(userRepository);

        CreateUserDTO userUpdatedDTO = new CreateUserDTO("foo.bar","Foo Bar", "foo@example.com", "password");

        User userUpdated = userUpdatedDTO.toUserEntity();
        UUID id = UUID.randomUUID();
        userUpdated.setId(id);

        // when
        when(userRepository.findById(userUpdated.getId())).thenReturn(Optional.empty());

        // then
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(userUpdated.getId(), userUpdatedDTO));

    }

}
