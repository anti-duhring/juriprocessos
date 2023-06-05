package com.limatech.juriprocessos.services.process;

import com.limatech.juriprocessos.dtos.process.CreateProcessDTO;
import com.limatech.juriprocessos.exceptions.process.ProcessNotFoundException;
import com.limatech.juriprocessos.exceptions.users.ForbiddenActionException;
import com.limatech.juriprocessos.exceptions.users.UserNotFoundException;
import com.limatech.juriprocessos.models.process.entity.Process;
import com.limatech.juriprocessos.models.users.entity.User;
import com.limatech.juriprocessos.repository.process.ProcessRepository;
import com.limatech.juriprocessos.repository.users.UserRepository;
import com.limatech.juriprocessos.services.interfaces.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProcessService implements UserValidation {

    private final ProcessRepository processRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProcessService(ProcessRepository processRepository, UserRepository userRepository) {
        this.processRepository = processRepository;
        this.userRepository = userRepository;
    }

    public Process createProcess(CreateProcessDTO processDTO) {
        validateUserPermission(processDTO);

        User user = userRepository.findById(processDTO.getUserId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        processDTO.setUser(user);
        Process process = processDTO.toEntity();

        return processRepository.save(process);
    }

    public void deleteProcess(UUID id) {
        validateUserPermission(id);

        Process process = processRepository.findById(id).orElseThrow(() -> new ProcessNotFoundException("Process not found"));

        processRepository.deleteById(id);
    }

    public Process updateProcess(UUID id, CreateProcessDTO processDTO) {
        validateUserPermission(id);

        Process process = processRepository.findById(id).orElseThrow(() -> new ProcessNotFoundException("Process not found"));

        if(processDTO.getIdentifier() != null) {
            process.setIdentifier(processDTO.getIdentifier());
        }
        if(processDTO.getUf() != null) {
            process.setUf(processDTO.getUf());
        }
        if(processDTO.getCourt() != null) {
            process.setCourt(processDTO.getCourt());
        }
        if(processDTO.getDegree() != null) {
            process.setDegree(processDTO.getDegree());
        }
        if(processDTO.getVara() != null) {
            process.setVara(processDTO.getVara());
        }

        return processRepository.save(process);
    }

    public Process getProcess(UUID id) {
        validateUserPermission(id);

        return processRepository.findById(id).orElseThrow(() -> new ProcessNotFoundException("Process not found"));
    }

    @Override
    public void validateUserPermission(UUID id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID currentUserId = currentUser.getId();

        User currentUserWithProcess = userRepository.findById(currentUserId).orElseThrow(() -> new UserNotFoundException("Current user not " +
                        "found"));

        List<Process> processes = currentUserWithProcess.getProcesses();
        List<UUID> processesID = processes.stream().map(Process::getId).toList();

        System.out.println(processesID.size());
        if(!processesID.contains(id) && !isUserAdmin()) {
            throw new ForbiddenActionException();
        }
    }

    public void validateUserPermission(CreateProcessDTO processDTO) {
        UUID userFromProcess = processDTO.getUserId();

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID currentUserId = currentUser.getId();


        if(!userFromProcess.toString().equals(currentUserId.toString()) && !isUserAdmin()) {
            throw new ForbiddenActionException();
        }

    }
}
