package com.limatech.juriprocessos.services;

import com.limatech.juriprocessos.dtos.process.AddAndRemoveUserToGroupDTO;
import com.limatech.juriprocessos.dtos.process.ManageProcessToGroupDTO;
import com.limatech.juriprocessos.dtos.process.CreateGroupDTO;
import com.limatech.juriprocessos.dtos.process.CreateProcessDTO;
import com.limatech.juriprocessos.exceptions.process.GroupNotFoundException;
import com.limatech.juriprocessos.exceptions.process.ProcessNotFoundException;
import com.limatech.juriprocessos.models.process.Group;
import com.limatech.juriprocessos.models.process.Process;
import com.limatech.juriprocessos.models.users.User;
import com.limatech.juriprocessos.repository.process.GroupRepository;
import com.limatech.juriprocessos.repository.process.ProcessRepository;
import com.limatech.juriprocessos.repository.users.UserRepository;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Array;
import java.util.*;

public class GroupServiceTest {

    GroupService groupService;

    GroupRepository groupRepository = Mockito.mock(GroupRepository.class);

    ProcessRepository processRepository = Mockito.mock(ProcessRepository.class);

    UserRepository  userRepository = Mockito.mock(UserRepository.class);

    @BeforeEach
    void setUp() {
        groupService = new GroupService(groupRepository, processRepository, userRepository);
    }

    @Test
    void shouldCallSaveWhenCreatingGroup() {
        // given
        UUID userId = UUID.randomUUID();
        CreateGroupDTO groupDTO = new CreateGroupDTO(userId, "Group test", "Group test description 1 2 3 4 5");
        Group group = groupDTO.toEntity();

        // when
        Mockito.when(groupRepository.save(Mockito.any(Group.class))).thenReturn(group);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
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
        UUID userId = UUID.randomUUID();
        CreateGroupDTO groupDTO = new CreateGroupDTO(userId, "Group test", "Group test description 1 2 3 4 5");
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
        UUID userId = UUID.randomUUID();
        CreateGroupDTO groupDTO = new CreateGroupDTO(userId, "Group test", "Group test description 1 2 3 4 5");
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
        UUID userId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        CreateGroupDTO groupDTO = new CreateGroupDTO(userId, "Group test", "Group test description 1 2 3 4 5");

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
        UUID userId = UUID.randomUUID();
        CreateGroupDTO groupDTO = new CreateGroupDTO(userId, "Group test", "Group test description 1 2 3 4 5");
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
        UUID userId = UUID.randomUUID();
        CreateGroupDTO groupDTO = new CreateGroupDTO(userId, "Group test", "Group test description 1 2 3 4 5");
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
        CreateGroupDTO groupDTO = new CreateGroupDTO(UUID.randomUUID(), "Group test", "Group test description 1 2 3 4" +
                " 5");
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

        CreateGroupDTO groupDTO = new CreateGroupDTO(UUID.randomUUID(), "Group test", "Group test description 1 2 3 4" +
                " 5");
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

        CreateGroupDTO groupDTO = new CreateGroupDTO(UUID.randomUUID(), "Group test", "Group test description 1 2 3 4" +
                " 5");
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

    @Test
    void shouldAddUsersGroupInCanReadAndCanWriteProperty() {
        // given
        UUID userId = UUID.randomUUID();
        CreateGroupDTO groupDTO = new CreateGroupDTO(userId, "Group test", "Group test description 1 2 3 4 5");
        Group group = groupDTO.toEntity();
        group.setId(UUID.randomUUID());

        List<UUID> canRead = new ArrayList<>(Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()));
        List<UUID> canWrite = new ArrayList<>(Arrays.asList(UUID.randomUUID(), UUID.randomUUID()));
        AddAndRemoveUserToGroupDTO addAndRemoveUserToGroupDTO = new AddAndRemoveUserToGroupDTO(group.getId(), canRead
                , canWrite);

        List<User> usersCanRead = new ArrayList<>(Arrays.asList(new User(), new User(), new User()));
        List<User> usersCanWrite = new ArrayList<>(Arrays.asList(new User(), new User()));

        // when
        Mockito.when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        Mockito.when(userRepository.findAllById(canRead)).thenReturn(usersCanRead);
        Mockito.when(userRepository.findAllById(canWrite)).thenReturn(usersCanWrite);
        Mockito.when(groupRepository.save(Mockito.any(Group.class))).thenReturn(group);

        Group groupReturned = groupService.addUsers(addAndRemoveUserToGroupDTO);

        // then
        Assertions.assertEquals(3, groupReturned.getCanRead().size());
        Assertions.assertEquals(2, groupReturned.getCanWrite().size());
    }

    @Test
    void shouldThrowGroupNotFoundExceptionWhenAddingUsersToInexistentGroup() {
        // given
        UUID groupId = UUID.randomUUID();

        List<UUID> canRead = new ArrayList<>();
        List<UUID> canWrite = new ArrayList<>();

        AddAndRemoveUserToGroupDTO addAndRemoveUserToGroupDTO = new AddAndRemoveUserToGroupDTO(groupId, canRead,  canWrite);

        // when
        Mockito.when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(GroupNotFoundException.class, () -> {
            groupService.addUsers(addAndRemoveUserToGroupDTO);
        });
    }

    @Test
    void shouldRemoveUsersGroupInCanReadAndCanWriteProperty() {
        // given
        Group group = new Group();
        group.setId(UUID.randomUUID());

        User user1 = new User();
        user1.setId(UUID.randomUUID());
        User user2 = new User();
        user2.setId(UUID.randomUUID());
        User user3 = new User();
        user3.setId(UUID.randomUUID());

        List<User> usersFromCanRead = new ArrayList<>(Arrays.asList(user1, user2));
        List<User> usersFromCanWrite = new ArrayList<>(Arrays.asList(user1, user2, user3));

        group.addCanRead(user1);
        group.addCanRead(user2);

        group.addCanWrite(user1);
        group.addCanWrite(user2);
        group.addCanWrite(user3);

        List<UUID> usersToRemoveFromCanRead = new ArrayList<>(Arrays.asList(user1.getId(), user2.getId()));
        List<UUID> usersToRemoveFromCanWrite = new ArrayList<>(Arrays.asList(user1.getId(), user2.getId(),
                user3.getId()));

        AddAndRemoveUserToGroupDTO addAndRemoveUserToGroupDTO = new AddAndRemoveUserToGroupDTO(group.getId(),
                usersToRemoveFromCanRead, usersToRemoveFromCanWrite);

        // when
        Mockito.when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        Mockito.when(groupRepository.save(Mockito.any(Group.class))).thenReturn(group);
        Mockito.when(userRepository.findAllById(usersToRemoveFromCanRead)).thenReturn(usersFromCanRead);
        Mockito.when(userRepository.findAllById(usersToRemoveFromCanWrite)).thenReturn(usersFromCanWrite);

        Group groupReturned = groupService.removeUsers(addAndRemoveUserToGroupDTO);

        // then
        Assertions.assertEquals(0, groupReturned.getCanRead().size());
        Assertions.assertEquals(0, groupReturned.getCanWrite().size());
    }
}
