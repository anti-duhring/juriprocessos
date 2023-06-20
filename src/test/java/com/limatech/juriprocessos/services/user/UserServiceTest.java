package com.limatech.juriprocessos.services.user;

import com.limatech.juriprocessos.dtos.users.RegisterUserRequestDTO;
import com.limatech.juriprocessos.exceptions.users.InvalidPropertyException;
import com.limatech.juriprocessos.exceptions.users.UserAlreadyExistsException;
import com.limatech.juriprocessos.exceptions.users.UserNotFoundException;
import com.limatech.juriprocessos.models.users.entity.User;
import com.limatech.juriprocessos.repository.users.UserRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    UserService userService;

    PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);

    UserRepository userRepository = Mockito.mock(UserRepository.class);

    AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);

    User contextUser = Instancio.create(User.class);

    @BeforeEach
    void setup() {
        this.userService = new UserService(userRepository, passwordEncoder, authenticationManager);

        // Auth context setup
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal()).thenReturn(contextUser);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void shouldThrowUserAlreadyExistsExceptionWhenEmailAlreadyExists() {

        RegisterUserRequestDTO userDTO = new RegisterUserRequestDTO("john.de","John Dee", "john@example.com", "password");
        User user = new User("john.de", "John", "john@example.com", "password");
        Optional<User> optionalUser = Optional.of(user);

        // when
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(optionalUser);

        // then
        assertThrows(UserAlreadyExistsException.class, () -> userService.validateIfUserAlreadyExists(userDTO));
    }

    @Test
    void shouldThrowUserAlreadyExistsExceptionWhenUsernameAlreadyExists() {
        RegisterUserRequestDTO userDTO = new RegisterUserRequestDTO("john.dee","John Dee", "john@example.com", "password");
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
            RegisterUserRequestDTO userDTO = new RegisterUserRequestDTO("john.dee","John Dee", "johndee", "password");
        });
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenTryToDeleteInexistentUser() {
        // given
        UUID id = UUID.randomUUID();
        contextUser.setId(id);

        // then
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(id));
    }

    @Test
    void shouldCallDeleteUserById() {
        // given
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(userRepository, passwordEncoder, authenticationManager);

        RegisterUserRequestDTO userDTO = new RegisterUserRequestDTO("john.dee","John Dee", "john@example.com", "password");
        User user = userDTO.toEntity();
        UUID id = UUID.randomUUID();

        user.setId(id);
        contextUser.setId(id);

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
        UserService userService = new UserService(userRepository, passwordEncoder, authenticationManager);

        RegisterUserRequestDTO userDTO = new RegisterUserRequestDTO("john.dee","John Dee", "john@example.com", "password");
        RegisterUserRequestDTO userUpdatedDTO = new RegisterUserRequestDTO("foo.bar","Foo Bar", "foo@example.com", "password");

        User user = userDTO.toEntity();
        User userUpdated = userUpdatedDTO.toEntity();
        UUID id = UUID.randomUUID();
        user.setId(id);
        userUpdated.setId(id);
        contextUser.setId(id);

        // when
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // then
        User userReturned = userService.updateUser(user.getId(), userUpdatedDTO);
        Mockito.verify(userRepository).save(Mockito.any(User.class));

    }

    @Test
    void shouldThrownUserNotFoundExcepetionWhenTryToUpdateAnInexistentUser() {
        // given
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(userRepository, passwordEncoder, authenticationManager);

        RegisterUserRequestDTO userUpdatedDTO = new RegisterUserRequestDTO("foo.bar","Foo Bar", "foo@example.com", "password");

        User userUpdated = userUpdatedDTO.toEntity();
        UUID id = UUID.randomUUID();
        userUpdated.setId(id);
        contextUser.setId(id);

        // when
        when(userRepository.findById(userUpdated.getId())).thenReturn(Optional.empty());

        // then
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(userUpdated.getId(), userUpdatedDTO));

    }

    @Test
    void shouldCallSaveUserWhenCreateANewUser() {
        // given
        RegisterUserRequestDTO userDTO = Instancio.create(RegisterUserRequestDTO.class);
        User user = userDTO.toEntity();

        // when
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn(userDTO.getPassword());

        // then
        userService.createUser(userDTO);
        Mockito.verify(userRepository).save(Mockito.any(User.class));
    }
//
//    @Test
//    void shouldCallFindByIdWhenGettingAUser() {
//        // given
//        UserRepository userRepository = Mockito.mock(UserRepository.class);
//        UserService userService = new UserService(userRepository, passwordEncoder, authenticationManager);
//
//        RegisterUserRequestDTO userDTOOne = new RegisterUserRequestDTO("foo.bar","Foo Bar", "foo@example.com", "password");
//        RegisterUserRequestDTO userDTOTwo = new RegisterUserRequestDTO("john.dee","John Dee", "john@example.com", "password");
//
//        User userOne = userDTOOne.toEntity();
//        User userTwo = userDTOTwo.toEntity();
//
//        List<User> users = new ArrayList<User>();
//        users.add(userOne);
//        users.add(userTwo);
//
//        // when
//        when(userRepository.findAll()).thenReturn(users);
//
//        // then
//        List<User> getAll = userService.getAll();
//        assertEquals(users, getAll);
//    }
//
//
//    @Test
//    void shouldCallFindByIdWhenGettingAUserById() {
//        // given
//        UserRepository userRepository = Mockito.mock(UserRepository.class);
//        UserService userService = new UserService(userRepository, passwordEncoder, authenticationManager);
//
//        RegisterUserRequestDTO userDTO = new RegisterUserRequestDTO("foo.bar","Foo Bar", "foo@example.com", "password");
//
//        User user = userDTO.toEntity();
//
//        UUID id = UUID.randomUUID();
//        user.setId(id);
//
//        // when
//        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
//
//        // then
//        User userReturned = userService.getById(user.getId());
//        assertEquals(user, userReturned);
//    }
//
//    @Test
//    void shouldThrownUserNotFoundExcepetionWhenTryToGetAnInexistentUser() {
//        // given
//        UserRepository userRepository = Mockito.mock(UserRepository.class);
//        UserService userService = new UserService(userRepository, passwordEncoder, authenticationManager);
//
//        UUID id = UUID.randomUUID();
//
//        // when
//        when(userRepository.findById(id)).thenReturn(Optional.empty());
//
//        // then
//        assertThrows(UserNotFoundException.class, () -> userService.getById(id));
//    }

}
