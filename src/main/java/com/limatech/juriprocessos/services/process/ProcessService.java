package com.limatech.juriprocessos.services.process;

import com.limatech.juriprocessos.dtos.process.CreateProcessDTO;
import com.limatech.juriprocessos.exceptions.process.ProcessNotFoundException;
import com.limatech.juriprocessos.exceptions.users.UserNotFoundException;
import com.limatech.juriprocessos.models.process.entity.Process;
import com.limatech.juriprocessos.models.users.entity.User;
import com.limatech.juriprocessos.repository.process.ProcessRepository;
import com.limatech.juriprocessos.repository.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProcessService {

    private final ProcessRepository processRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProcessService(ProcessRepository processRepository, UserRepository userRepository) {
        this.processRepository = processRepository;
        this.userRepository = userRepository;
    }

    public Process createProcess(CreateProcessDTO processDTO) {
        User user = userRepository.findById(processDTO.getUserId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        processDTO.setUser(user);
        Process process = processDTO.toEntity();

        return processRepository.save(process);
    }

    public void deleteProcess(UUID id) {
        Process process = processRepository.findById(id).orElseThrow(() -> new ProcessNotFoundException("Process not found"));

        processRepository.deleteById(id);
    }

    public Process updateProcess(UUID id, CreateProcessDTO processDTO) {
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
        return processRepository.findById(id).orElseThrow(() -> new ProcessNotFoundException("Process not found"));
    }
}
