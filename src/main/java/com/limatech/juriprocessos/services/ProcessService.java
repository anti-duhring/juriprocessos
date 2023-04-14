package com.limatech.juriprocessos.services;

import com.limatech.juriprocessos.dtos.process.CreateProcessDTO;
import com.limatech.juriprocessos.exceptions.process.ProcessNotFoundException;
import com.limatech.juriprocessos.models.process.Process;
import com.limatech.juriprocessos.repository.process.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProcessService {

    private final ProcessRepository processRepository;

    @Autowired
    public ProcessService(ProcessRepository processRepository) {
        this.processRepository = processRepository;
    }

    public Process createProcess(CreateProcessDTO processDTO) {
        Process process = processDTO.toEntity();

        return processRepository.save(process);
    }

    public void deleteProcess(UUID id) {
        Process process = processRepository.findById(id).orElseThrow(() -> new ProcessNotFoundException("Process not found"));

        processRepository.deleteById(id);
    }

    public void updateProcess(CreateProcessDTO processDTO) {

    }
}
