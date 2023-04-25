package com.limatech.juriprocessos.services;

import com.limatech.juriprocessos.dtos.process.ManageProcessToGroupDTO;
import com.limatech.juriprocessos.dtos.process.CreateGroupDTO;
import com.limatech.juriprocessos.dtos.process.CreateProcessDTO;
import com.limatech.juriprocessos.exceptions.process.GroupNotFoundException;
import com.limatech.juriprocessos.exceptions.process.ProcessNotFoundException;
import com.limatech.juriprocessos.models.process.Group;
import com.limatech.juriprocessos.models.process.Process;
import com.limatech.juriprocessos.repository.process.GroupRepository;
import com.limatech.juriprocessos.repository.process.ProcessRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

public class GroupServiceTest {

    GroupService groupService;

    GroupRepository groupRepository = Mockito.mock(GroupRepository.class);

    ProcessRepository processRepository = Mockito.mock(ProcessRepository.class);

    @BeforeEach
    void setUp() {
        groupService = new GroupService(groupRepository, processRepository);
    }

    @Test
    void shouldCallSaveWhenCreatingGroup() {
        // given
        CreateGroupDTO groupDTO = new CreateGroupDTO("Group test", "Group test description 1 2 3 4 5");
        Group group = groupDTO.toEntity();

        // when
        Mockito.when(groupRepository.save(Mockito.any(Group.class))).thenReturn(group);
        Group groupReturned = groupService.createGroup(groupDTO);

        // then
        Mockito.verify(groupRepository).save(Mockito.any(Group.class));
        Assertions.assertEquals(groupReturned.getId(), group.getId());
        Assertions.assertEquals(groupReturned.getName(), group.getName());
        Assertions.assertEquals(groupReturned.getDescription(), group.getDescription());
        Assertions.assertEquals(groupReturned.getProcesses().size(), 0);
    }

    @Test
    void shouldCallDeleteWhenDeletingGroup() {
        // given
        CreateGroupDTO groupDTO = new CreateGroupDTO("Group test", "Group test description 1 2 3 4 5");
        Group group = groupDTO.toEntity();
        group.setId(UUID.randomUUID());

        // when
        Mockito.when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        groupService.deleteGroup(group.getId());

        // then
        Mockito.verify(groupRepository).deleteById(group.getId());
    }

    @Test
    void shouldThrowGroupNotFoundExceptionWhenDeletingInexistentGroup() {
        // given
        UUID groupId = UUID.randomUUID();

        // when
        Mockito.when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(GroupNotFoundException.class, () -> {
            groupService.deleteGroup(groupId);
        });
    }

    @Test
    void shouldCallFindByIdAndUpdateWhenUpdatingGroup() {

        // given
        CreateGroupDTO groupDTO = new CreateGroupDTO("Group test", "Group test description 1 2 3 4 5");
        Group group = groupDTO.toEntity();
        group.setId(UUID.randomUUID());

        // when
        Mockito.when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        Mockito.when(groupRepository.save(Mockito.any(Group.class))).thenReturn(group);
        Group groupReturned = groupService.updateGroup(group.getId(), groupDTO);

        // then
        Mockito.verify(groupRepository).save(Mockito.any(Group.class));
        Assertions.assertEquals(groupReturned.getId(), group.getId());
        Assertions.assertEquals(groupReturned.getName(), group.getName());
        Assertions.assertEquals(groupReturned.getDescription(), group.getDescription());
        Assertions.assertEquals(groupReturned.getProcesses().size(), 0);
    }

    @Test
    void shouldThrowGroupNotFoundExceptionWhenUpdatingInexistentGroup() {
        // given
        UUID groupId = UUID.randomUUID();
        CreateGroupDTO groupDTO = new CreateGroupDTO("Group test", "Group test description 1 2 3 4 5");

        // when
        Mockito.when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(GroupNotFoundException.class, () -> {
            groupService.updateGroup(groupId, groupDTO);
        });
    }

    @Test
    void shouldCallFindByIdWhenFindingGroup() {
        // given
        CreateGroupDTO groupDTO = new CreateGroupDTO("Group test", "Group test description 1 2 3 4 5");
        Group group = groupDTO.toEntity();
        group.setId(UUID.randomUUID());

        // when
        Mockito.when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        Group groupReturned = groupService.getGroup(group.getId());

        // then
        Assertions.assertEquals(groupReturned.getId(), group.getId());
        Assertions.assertEquals(groupReturned.getName(), group.getName());
        Assertions.assertEquals(groupReturned.getDescription(), group.getDescription());
        Assertions.assertEquals(groupReturned.getProcesses().size(), 0);
    }

    @Test
    void shouldThrowGroupNotFoundExceptionWhenFindingInexistentGroup() {
        // given
        UUID groupId = UUID.randomUUID();

        // when
        Mockito.when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(GroupNotFoundException.class, () -> {
            groupService.getGroup(groupId);
        });
    }

    @Test
    void shouldAddProcessToGroup() {
        // given
        CreateGroupDTO groupDTO = new CreateGroupDTO("Group test", "Group test description 1 2 3 4 5");
        Group group = groupDTO.toEntity();
        group.setId(UUID.randomUUID());

        CreateProcessDTO processDTO = new CreateProcessDTO(null, "000", "PE", "TJPE", "1", "vara");
        Process process =  processDTO.toEntity();
        process.setId(UUID.randomUUID());

        ManageProcessToGroupDTO manageProcessToGroupDTO = new ManageProcessToGroupDTO(process.getId());

        // when
        Mockito.when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        Mockito.when(processRepository.findById(process.getId())).thenReturn(Optional.of(process));
        Mockito.when(groupRepository.save(Mockito.any(Group.class))).thenReturn(group);

        Group groupReturned = groupService.addProcess(group.getId(), manageProcessToGroupDTO);

        // then
        Assertions.assertEquals(groupReturned.getProcesses().size(), 1);
    }

    @Test
    void shouldThrowGroupNotFoundExceptionWhenAddingProcessToInexistentGroup() {
        // given
        UUID groupId = UUID.randomUUID();
        ManageProcessToGroupDTO manageProcessToGroupDTO = new ManageProcessToGroupDTO(UUID.randomUUID());

        // when
        Mockito.when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(GroupNotFoundException.class, () -> {
            groupService.addProcess(groupId, manageProcessToGroupDTO);
        });
    }

    @Test
    void shouldThrowProcessNotFoundExceptionWhenAddingInexistentProcessToGroup() {
        // given
        CreateGroupDTO groupDTO = new CreateGroupDTO("Group test", "Group test description 1 2 3 4 5");
        Group group = groupDTO.toEntity();
        group.setId(UUID.randomUUID());

        ManageProcessToGroupDTO manageProcessToGroupDTO = new ManageProcessToGroupDTO(UUID.randomUUID());

        // when
        Mockito.when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        Mockito.when(processRepository.findById(manageProcessToGroupDTO.getProcessId())).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(ProcessNotFoundException.class, () -> {
            groupService.addProcess(group.getId(), manageProcessToGroupDTO);
        });
    }

    @Test
    void shouldRemoveProcessFromGroup() {
        // given
        CreateProcessDTO processDTO = new CreateProcessDTO(null, "000", "PE", "TJPE", "1", "vara");
        Process process =  processDTO.toEntity();
        process.setId(UUID.randomUUID());

        CreateGroupDTO groupDTO = new CreateGroupDTO("Group test", "Group test description 1 2 3 4 5");
        Group group = groupDTO.toEntity();
        group.setId(UUID.randomUUID());
        group.addProcess(process);

        ManageProcessToGroupDTO manageProcessToGroupDTO = new ManageProcessToGroupDTO(process.getId());

        // when
        Mockito.when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        Mockito.when(processRepository.findById(process.getId())).thenReturn(Optional.of(process));
        Mockito.when(groupRepository.save(Mockito.any(Group.class))).thenReturn(group);

        Group groupReturned = groupService.removeProcess(group.getId(), manageProcessToGroupDTO);

        // then
        Assertions.assertEquals(groupReturned.getProcesses().size(), 0);
    }

    @Test

    void shouldThrowGroupNotFoundExceptionWhenRemovingProcessFromInexistentGroup() {
        // given
        UUID groupId = UUID.randomUUID();
        ManageProcessToGroupDTO manageProcessToGroupDTO = new ManageProcessToGroupDTO(UUID.randomUUID());

        // when
        Mockito.when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(GroupNotFoundException.class, () -> {
            groupService.removeProcess(groupId, manageProcessToGroupDTO);
        });
    }


    @Test
    void shouldThrowProcessNotFoundExceptionWhenRemovingInexistentProcessFromGroup() {
        // given
        CreateProcessDTO processDTO = new CreateProcessDTO(null, "000", "PE", "TJPE", "1", "vara");
        Process process =  processDTO.toEntity();
        process.setId(UUID.randomUUID());

        CreateGroupDTO groupDTO = new CreateGroupDTO("Group test", "Group test description 1 2 3 4 5");
        Group group = groupDTO.toEntity();
        group.setId(UUID.randomUUID());

        ManageProcessToGroupDTO manageProcessToGroupDTO = new ManageProcessToGroupDTO(process.getId());

        // when
        Mockito.when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        Mockito.when(processRepository.findById(manageProcessToGroupDTO.getProcessId())).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(ProcessNotFoundException.class, () -> {
            groupService.removeProcess(group.getId(), manageProcessToGroupDTO);
        });
    }
}
