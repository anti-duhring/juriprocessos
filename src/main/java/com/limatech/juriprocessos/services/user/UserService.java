package com.limatech.juriprocessos.services.user;

import com.limatech.juriprocessos.dtos.users.LoginUserRequestDTO;
import com.limatech.juriprocessos.dtos.users.RegisterUserRequestDTO;
import com.limatech.juriprocessos.exceptions.users.ForbiddenActionException;
import com.limatech.juriprocessos.exceptions.users.UserAlreadyExistsException;
import com.limatech.juriprocessos.exceptions.users.UserNotFoundException;
import com.limatech.juriprocessos.models.users.entity.Role;
import com.limatech.juriprocessos.models.users.entity.User;
import com.limatech.juriprocessos.repository.users.UserRepository;
import com.limatech.juriprocessos.services.interfaces.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserValidation {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public User createUser(RegisterUserRequestDTO userDTO) {
        validateIfUserAlreadyExists(userDTO);

        User newUser = userDTO.toEntity();
        newUser.setAuthorities(Role.USER);

        return userRepository.save(newUser);
    }

    public User authenticateUser(LoginUserRequestDTO userDTO) {
        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.password)
        );

        User user =
                userRepository.findByUsername(userDTO.getUsername()).orElseThrow(() -> new UserNotFoundException(
                        "User " + userDTO.getUsername() + " not found"));

        return user;
    }

    public User updateUser(UUID id, RegisterUserRequestDTO userDTO) {
        validateUserPermission(id);
        validateIfUserAlreadyExists(userDTO);

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User " + id + " not " +
                "found"));
        if(userDTO.getUsername() != null) {
            user.setUsername(userDTO.getUsername());
        }
        if(userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getName() != null) {
            user.setName(userDTO.getName());
        }

        return userRepository.save(user);
    }

    public void deleteUser(UUID id) {
        validateUserPermission(id);
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User " + id + "not " +
                "found"));

        userRepository.deleteById(id);
    }

    public List<User> getAll() {
        validateUserAdminPermission();

        return userRepository.findAll();
    }

    public User getById(UUID id) {
        validateUserPermission(id);

        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User " + id + "not " +
                "found"));
    }

    public void validateIfUserAlreadyExists(RegisterUserRequestDTO userDTO) {
        Optional<User> userWithThisEmail = userRepository.findByEmail(userDTO.getEmail());
        Optional<User> userWithThisUsername = userRepository.findByUsername(userDTO.getUsername());

        if(userWithThisEmail.isPresent()) {
            throw new UserAlreadyExistsException("Email already exists");
        }
        if(userWithThisUsername.isPresent()) {
            throw new UserAlreadyExistsException("Username already exists");
        }
    }

    public User getByUsername(String username) {

        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User " + username + " not found"));
    }

    @Override
    public void validateUserPermission(UUID userId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isUserAdmin =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(role -> role.toString().equals(Role.ADMIN.toString()));

        if(!userId.toString().equals(currentUser.getId().toString()) && !isUserAdmin) {
            throw new ForbiddenActionException();
        }
    }
}
