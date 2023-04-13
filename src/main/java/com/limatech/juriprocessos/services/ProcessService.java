package com.limatech.juriprocessos.services;

import com.limatech.juriprocessos.repository.process.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessService {

    private ProcessRepository processRepository;

    @Autowired
    public ProcessService(ProcessRepository processRepository) {
        this.processRepository = processRepository;
    }
}
