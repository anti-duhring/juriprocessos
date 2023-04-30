package com.limatech.juriprocessos.services.process;

import com.limatech.juriprocessos.dtos.process.AddAndRemoveUserToGroupDTO;
import com.limatech.juriprocessos.dtos.process.CreateGroupDTO;
import com.limatech.juriprocessos.dtos.process.ManageProcessToGroupDTO;
import com.limatech.juriprocessos.exceptions.process.GroupNotFoundException;
import com.limatech.juriprocessos.exceptions.process.ProcessNotFoundException;
import com.limatech.juriprocessos.exceptions.users.UserNotFoundException;
import com.limatech.juriprocessos.models.process.entity.Group;
import com.limatech.juriprocessos.models.process.entity.Process;
import com.limatech.juriprocessos.models.users.entity.User;
import com.limatech.juriprocessos.repository.process.GroupRepository;
import com.limatech.juriprocessos.repository.process.ProcessRepository;
import com.limatech.juriprocessos.repository.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final ProcessRepository processRepository;
    private final UserRepository userRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository, ProcessRepository processRepository,
                        UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.processRepository = processRepository;
        this.userRepository = userRepository;
    }

    public Group createGroup(CreateGroupDTO groupDTO) {
        User user = userRepository.findById(groupDTO.getUserId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        groupDTO.setUser(user);
        Group group = groupDTO.toEntity();
        return groupRepository.save(group);
    }

    public void deleteGroup(UUID id) {
        Group group  = groupRepository.findById(id).orElseThrow(() -> new GroupNotFoundException("Group not found"));
        groupRepository.deleteById(id);
    }

    public Group updateGroup(UUID id, CreateGroupDTO groupDTO) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new GroupNotFoundException("Group not found"));

        if(groupDTO.getName() != null)
            group.setName(groupDTO.getName());

        if(groupDTO.getDescription() != null)
            group.setDescription(groupDTO.getDescription());


        return groupRepository.save(group);
    }

    public Group getGroup(UUID id) {
        return groupRepository.findById(id).orElseThrow(() -> new GroupNotFoundException("Group not found"));
    }

    public Group addProcess(UUID id, ManageProcessToGroupDTO manageProcessToGroupDTO) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new GroupNotFoundException("Group not found"));
        Process process = processRepository.findById(manageProcessToGroupDTO.getProcessId()).orElseThrow(() -> new ProcessNotFoundException(
                "Process not found"));

        group.addProcess(process);

        return groupRepository.save(group);
    }

    public Group removeProcess(UUID id, ManageProcessToGroupDTO manageProcessToGroupDTO) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new GroupNotFoundException("Group not found"));
        Process process =
                processRepository.findById(manageProcessToGroupDTO.getProcessId()).orElseThrow(() -> new ProcessNotFoundException("Process not found"));

        group.removeProcess(process);
        return groupRepository.save(group);
    }

    public Group addUsers(AddAndRemoveUserToGroupDTO groupDTO) {
        Group group = groupRepository.findById(groupDTO.getGroupId()).orElseThrow(() -> new GroupNotFoundException("Group " +
                "not found"));
        List<UUID> userIdsToAddInCanRead = groupDTO.getCanRead();
        List<UUID> userIdsToAddInCanWrite = groupDTO.getCanWrite();

        List<User> usersToAddInCanRead = userRepository.findAllById(userIdsToAddInCanRead);
        List<User> usersToAddInCanWrite = userRepository.findAllById(userIdsToAddInCanWrite);

        usersToAddInCanRead.forEach(group::addCanRead);
        usersToAddInCanWrite.forEach(group::addCanWrite);

        return groupRepository.save(group);
    }

    public Group removeUsers(AddAndRemoveUserToGroupDTO groupDTO) {
        Group group = groupRepository.findById(groupDTO.getGroupId()).orElseThrow(() -> new GroupNotFoundException("Group " +
                "not found"));
        List<UUID> userIdsToRemoveInCanRead = groupDTO.getCanRead();
        List<UUID> userIdsToRemoveInCanWrite = groupDTO.getCanWrite();

        List<User> usersToRemoveInCanRead = userRepository.findAllById(userIdsToRemoveInCanRead);
        List<User> usersToRemoveInCanWrite = userRepository.findAllById(userIdsToRemoveInCanWrite);

        usersToRemoveInCanRead.forEach(group::removeCanRead);
        usersToRemoveInCanWrite.forEach(group::removeCanWrite);

        return groupRepository.save(group);
    }
}
