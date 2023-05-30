package com.limatech.juriprocessos.controllers.users;

import com.limatech.juriprocessos.dtos.users.CreateTaskDTO;
import com.limatech.juriprocessos.models.users.entity.Task;
import com.limatech.juriprocessos.services.user.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
