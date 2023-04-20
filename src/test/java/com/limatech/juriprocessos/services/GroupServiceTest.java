package com.limatech.juriprocessos.services;

import com.limatech.juriprocessos.dtos.process.CreateGroupDTO;
import com.limatech.juriprocessos.exceptions.process.GroupNotFoundException;
import com.limatech.juriprocessos.models.process.Group;
import com.limatech.juriprocessos.repository.process.GroupRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

public class GroupServiceTest {

    GroupService groupService;

    GroupRepository groupRepository = Mockito.mock(GroupRepository.class);

    @BeforeEach
    void setUp() {
        groupService = new GroupService(groupRepository);
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
}
