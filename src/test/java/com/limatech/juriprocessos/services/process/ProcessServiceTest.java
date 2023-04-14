package com.limatech.juriprocessos.services.process;

import com.limatech.juriprocessos.dtos.process.CreateProcessDTO;
import com.limatech.juriprocessos.dtos.users.CreateUserDTO;
import com.limatech.juriprocessos.exceptions.process.ProcessNotFoundException;
import com.limatech.juriprocessos.models.process.Process;
import com.limatech.juriprocessos.repository.process.ProcessRepository;
import com.limatech.juriprocessos.services.ProcessService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

public class ProcessServiceTest {

    ProcessService processService;

    ProcessRepository processRepository = Mockito.mock(ProcessRepository.class);

    @BeforeEach
    void setup() {
        this.processService = new ProcessService(this.processRepository);
    }

    @Test
    void shouldCallSaveMethodWhenCreateANewProcess() {
        // given
        CreateUserDTO userDTO = new CreateUserDTO("john.dee","John Dee", "john@example.com", "password");
        CreateProcessDTO processDTO = new CreateProcessDTO(userDTO.toUserEntity(), "000", "PE", "TJPE", "1");
        Process process = processDTO.toEntity();

        // when
        Mockito.when(this.processRepository.save(Mockito.any(Process.class))).thenReturn(process);

        // then
        Process processCreated = this.processService.createProcess(processDTO);
        Mockito.verify(this.processRepository).save(Mockito.any(Process.class));

        Assertions.assertEquals(processCreated.getCourt(),process.getCourt());
        Assertions.assertEquals(processCreated.getUf(),process.getUf());
        Assertions.assertEquals(processCreated.getIdentifier(),process.getIdentifier());
        Assertions.assertEquals(processCreated.getDegree(), process.getDegree());
    }

    @Test
    void shouldCallDeleteWhenTryToDeleteAProcess()  {
        // given
        CreateUserDTO userDTO = new CreateUserDTO("john.dee","John Dee", "john@example.com", "password");
        CreateProcessDTO processDTO = new CreateProcessDTO(userDTO.toUserEntity(), "000", "PE", "TJPE", "1");
        Process process = processDTO.toEntity();
        UUID id = UUID.randomUUID();
        process.setId(id);

        // when
        Mockito.when(this.processRepository.findById(process.getId())).thenReturn(Optional.of(process));

        // then
        processService.deleteProcess(process.getId());
        Mockito.verify(this.processRepository).deleteById(process.getId());
    }
    
    @Test
    void shouldThrowProcessNotFoundExceptionWhenTryToDeleteAnInexistentProcess() {
        UUID id = UUID.randomUUID();

        // when
        Mockito.when(this.processRepository.findById(id)).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(ProcessNotFoundException.class, () -> processService.deleteProcess(id));
    }
}
