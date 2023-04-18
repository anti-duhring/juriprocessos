package com.limatech.juriprocessos.services;

import com.limatech.juriprocessos.dtos.process.CreateGroupDTO;
import com.limatech.juriprocessos.models.process.Group;
import com.limatech.juriprocessos.repository.process.GroupRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
    void shouldThrowInvalidPropertyExceptionWhenCreatingGroupWithInvalidName() {

    }
}
