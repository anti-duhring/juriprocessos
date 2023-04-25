package com.limatech.juriprocessos.services;

import com.limatech.juriprocessos.dtos.process.ManageProcessToGroupDTO;
import com.limatech.juriprocessos.dtos.process.CreateGroupDTO;
import com.limatech.juriprocessos.exceptions.process.GroupNotFoundException;
import com.limatech.juriprocessos.exceptions.process.ProcessNotFoundException;
import com.limatech.juriprocessos.models.process.Group;
import com.limatech.juriprocessos.models.process.Process;
import com.limatech.juriprocessos.repository.process.GroupRepository;
import com.limatech.juriprocessos.repository.process.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final ProcessRepository processRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository,  ProcessRepository processRepositoy) {
        this.groupRepository = groupRepository;
        this.processRepository = processRepositoy;
    }

    public Group createGroup(CreateGroupDTO groupDTO) {
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
}
