package com.limatech.juriprocessos.services;

import com.limatech.juriprocessos.dtos.users.CreateUserDTO;
import com.limatech.juriprocessos.exceptions.users.UserAlreadyExistsException;
import com.limatech.juriprocessos.exceptions.users.UserNotFoundException;
import com.limatech.juriprocessos.models.users.User;
import com.limatech.juriprocessos.repository.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {


    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(CreateUserDTO userDTO) {
        validateIfUserAlreadyExists(userDTO);

        User newUser = userDTO.toUserEntity();

        return userRepository.save(newUser);
    }

    public User updateUser(UUID id, CreateUserDTO userDTO) {
        validateIfUserAlreadyExists(userDTO);

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User " + id + " not " +
                "found"));
        if(userDTO.getUsername() != null) {
            user.setName(userDTO.getName());
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

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User " + id + "not " +
                "found"));

        userRepository.deleteById(id);
    }

    public List<User> getAll() {

        return userRepository.findAll();
    }

    public void validateIfUserAlreadyExists(CreateUserDTO userDTO) {
        Optional<User> userWithThisEmail = userRepository.findByEmail(userDTO.getEmail());
        Optional<User> userWithThisUsername = userRepository.findByUsername(userDTO.getUsername());

        if(userWithThisEmail.isPresent()) {
            throw new UserAlreadyExistsException("Email already exists");
        }
        if(userWithThisUsername.isPresent()) {
            throw new UserAlreadyExistsException("Username already exists");
        }
    }

}
