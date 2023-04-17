package com.limatech.juriprocessos.services;

import com.limatech.juriprocessos.dtos.process.CreateProcessDTO;
import com.limatech.juriprocessos.dtos.users.CreateUserDTO;
import com.limatech.juriprocessos.exceptions.process.ProcessNotFoundException;
import com.limatech.juriprocessos.models.process.Process;
import com.limatech.juriprocessos.models.users.User;
import com.limatech.juriprocessos.repository.process.ProcessRepository;
import com.limatech.juriprocessos.repository.users.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

public class ProcessServiceTest {

    ProcessService processService;

    ProcessRepository processRepository = Mockito.mock(ProcessRepository.class);
    UserRepository userRepository = Mockito.mock(UserRepository.class);

    @BeforeEach
    void setup() {
        this.processService = new ProcessService(this.processRepository, this.userRepository);
    }

    @Test
    void shouldCallSaveMethodWhenCreateANewProcess() {
        // given
        CreateUserDTO userDTO = new CreateUserDTO("john.dee","John Dee", "john@example.com", "password");
        User user = userDTO.toEntity();
        user.setId(UUID.randomUUID());

        CreateProcessDTO processDTO = new CreateProcessDTO(user.getId(), "000", "PE", "TJPE", "1", "vara");
        Process process = processDTO.toEntity();

        // when
        Mockito.when(this.processRepository.save(Mockito.any(Process.class))).thenReturn(process);
        Mockito.when(this.userRepository.findById(user.getId())).thenReturn(Optional.of(user));

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
        User user = userDTO.toEntity();
        user.setId(UUID.randomUUID());

        CreateProcessDTO processDTO = new CreateProcessDTO(userDTO.toEntity().getId(), "000", "PE", "TJPE", "1", "vara");
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

    @Test
    void shouldCallSaveMethodWhenUpdateAProcess() {
        // given
        CreateUserDTO userDTO = new CreateUserDTO("john.dee","John Dee", "john@example.com", "password");
        User user = userDTO.toEntity();
        user.setId(UUID.randomUUID());

        CreateProcessDTO processDTO = new CreateProcessDTO(userDTO.toEntity().getId(), "000", "PE", "TJPE", "1", "vara1");
        CreateProcessDTO processDTOUpdated = new CreateProcessDTO(userDTO.toEntity().getId(), "001", "RS", "TJSP", "2",
                "vara2");
        Process process = processDTO.toEntity();
        Process processUpdated = processDTOUpdated.toEntity();
        UUID id = UUID.randomUUID();

        process.setId(id);
        processUpdated.setId(id);

        // when
        Mockito.when(this.processRepository.findById(process.getId())).thenReturn(Optional.of(process));
        Mockito.when(this.processRepository.save(Mockito.any(Process.class))).thenReturn(processUpdated);
        Mockito.when(this.userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // then
        Process processUpdatedReturned = this.processService.updateProcess(id, processDTOUpdated);
        Mockito.verify(this.processRepository).save(Mockito.any(Process.class));

        Assertions.assertEquals(processUpdatedReturned.getCourt(),processUpdated.getCourt());
        Assertions.assertEquals(processUpdatedReturned.getUf(),processUpdated.getUf());
        Assertions.assertEquals(processUpdatedReturned.getIdentifier(),processUpdated.getIdentifier());
        Assertions.assertEquals(processUpdatedReturned.getDegree(), processUpdated.getDegree());
        Assertions.assertEquals(processUpdatedReturned.getVara(), processUpdated.getVara());
    }


    @Test
    void shouldThrowProcessNotFoundExceptionWhenTryToUpdateAnInexistentProcess() {
        UUID id = UUID.randomUUID();

        // when
        Mockito.when(this.processRepository.findById(id)).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(ProcessNotFoundException.class, () -> processService.updateProcess(id, new CreateProcessDTO()));
    }

    @Test
    void shouldCallFindByIdMethodWhenTryToFindAProcess() {
        // given
        CreateUserDTO userDTO = new CreateUserDTO("john.dee","John Dee", "john@example.com", "password");
        User user = userDTO.toEntity();
        user.setId(UUID.randomUUID());

        CreateProcessDTO processDTO = new CreateProcessDTO(userDTO.toEntity().getId(), "000", "PE", "TJPE", "1", "Vara");
        Process process = processDTO.toEntity();
        UUID id = UUID.randomUUID();
        process.setId(id);

        // when
        Mockito.when(this.processRepository.findById(id)).thenReturn(Optional.of(process));

        // then
        Process processReturned = this.processService.getProcess(id);
        Mockito.verify(this.processRepository).findById(id);

        Assertions.assertEquals(processReturned.getCourt(),process.getCourt());
        Assertions.assertEquals(processReturned.getUf(),process.getUf());
        Assertions.assertEquals(processReturned.getIdentifier(),process.getIdentifier());
        Assertions.assertEquals(processReturned.getDegree(), process.getDegree());
    }
}
