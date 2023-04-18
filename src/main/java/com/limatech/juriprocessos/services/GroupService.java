package com.limatech.juriprocessos.services;

import com.limatech.juriprocessos.dtos.process.CreateGroupDTO;
import com.limatech.juriprocessos.models.process.Group;
import com.limatech.juriprocessos.repository.process.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Group createGroup(CreateGroupDTO groupDTO) {
        Group group = groupDTO.toEntity();
        return groupRepository.save(group);
    }

}
