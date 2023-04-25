package com.limatech.juriprocessos.controllers.process;

import com.limatech.juriprocessos.dtos.process.CreateGroupDTO;
import com.limatech.juriprocessos.dtos.process.ManageProcessToGroupDTO;
import com.limatech.juriprocessos.models.process.Group;
import com.limatech.juriprocessos.services.GroupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("${api.url_base}/group")
public class GroupController {

    @Autowired
    GroupService groupService;

    @PostMapping
    public ResponseEntity<Group> createGroup(
            @RequestBody @Valid CreateGroupDTO groupDTO
    ) {
        Group group = groupService.createGroup(groupDTO);
        return ResponseEntity.ok(group);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(
            @PathVariable("id") UUID id
    ) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroup(
            @PathVariable("id") UUID id
    ) {
        Group group = groupService.getGroup(id);
        return ResponseEntity.ok(group);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Group> updateGroup(
            @PathVariable("id") UUID id,
            @RequestBody @Valid CreateGroupDTO groupDTO
    ) {
        Group group = groupService.updateGroup(id, groupDTO);
        return ResponseEntity.ok(group);
    }

    @PostMapping("/{id}/add-process")
    public ResponseEntity<Group> addProcessToGroup(
            @PathVariable("id") UUID id,
            @RequestBody @Valid ManageProcessToGroupDTO manageProcessToGroupDTO
    ) {
        Group group = groupService.addProcess(id, manageProcessToGroupDTO);
        return ResponseEntity.ok(group);
    }

    @PostMapping("/{id}/remove-process")
    public ResponseEntity<Group> removeProcessToGroup(
            @PathVariable("id") UUID id,
            @RequestBody @Valid ManageProcessToGroupDTO manageProcessToGroupDTO
    ) {
        Group group = groupService.removeProcess(id, manageProcessToGroupDTO);
        return ResponseEntity.ok(group);
    }
}
