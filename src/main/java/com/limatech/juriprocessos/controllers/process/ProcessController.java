package com.limatech.juriprocessos.controllers.process;

import com.limatech.juriprocessos.dtos.process.CreateProcessDTO;
import com.limatech.juriprocessos.models.process.Process;
import com.limatech.juriprocessos.services.ProcessService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("${api.url_base}/process")
public class ProcessController {

    @Autowired
    ProcessService processService;

    @PostMapping
    public ResponseEntity createProcess(
            @RequestBody @Valid CreateProcessDTO processDTO
    ) {
        Process process = processService.createProcess(processDTO);
        return ResponseEntity.ok(process);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateProcess(
            @PathVariable UUID id,
            @RequestBody @Valid CreateProcessDTO processDTO
    ) {
        Process process = processService.updateProcess(id, processDTO);
        return ResponseEntity.ok(process);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProcess(
            @PathVariable UUID id
    ) {
        processService.deleteProcess(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity getProcess(
            @PathVariable UUID id
    ) {
        Process process = processService.getProcess(id);
        return ResponseEntity.ok(process);
    }
}
