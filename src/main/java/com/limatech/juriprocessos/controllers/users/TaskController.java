package com.limatech.juriprocessos.controllers.users;

import com.limatech.juriprocessos.dtos.users.CreateTaskDTO;
import com.limatech.juriprocessos.dtos.users.ManageTaskDTO;
import com.limatech.juriprocessos.models.users.entity.Task;
import com.limatech.juriprocessos.services.user.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("${api.url_base}/task")
public class TaskController {

    @Autowired
    TaskService taskService;

    @PostMapping()
    public ResponseEntity<?> createTask(
            @RequestBody @Valid CreateTaskDTO taskDTO
    ) {
        Task task = taskService.createTask(taskDTO);

        return ResponseEntity.created(null).body(task);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updateTask(
            @RequestBody @Valid ManageTaskDTO taskDTO,
            @PathVariable("id") UUID id
            ) {
        Task task = taskService.updateTask(id, taskDTO);

        return ResponseEntity.ok(task);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteTask(
            @PathVariable("id") UUID id
    ) {
        taskService.deleteTask(id);

        return ResponseEntity.noContent().build();
    }


    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getTask(
            @PathVariable("id") UUID id
    ) {
        Task task = taskService.getTask(id);

        return ResponseEntity.ok(task);
    }
}
