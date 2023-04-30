package com.limatech.juriprocessos.services.user;

import com.limatech.juriprocessos.dtos.process.CreateTaskDTO;
import com.limatech.juriprocessos.exceptions.process.ProcessNotFoundException;
import com.limatech.juriprocessos.exceptions.users.TaskNotFoundException;
import com.limatech.juriprocessos.exceptions.users.UserNotFoundException;
import com.limatech.juriprocessos.models.process.entity.Process;
import com.limatech.juriprocessos.models.users.entity.Task;
import com.limatech.juriprocessos.models.users.entity.User;
import com.limatech.juriprocessos.repository.process.ProcessRepository;
import com.limatech.juriprocessos.repository.users.TaskRepository;
import com.limatech.juriprocessos.repository.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    private final ProcessRepository processRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository, ProcessRepository processRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.processRepository = processRepository;
    }

    public Task createTask(CreateTaskDTO taskDTO) {
        User user = userRepository.findById(taskDTO.getUserId()).orElseThrow(() -> new UserNotFoundException("User " +
                "not found"));
        Process process =
                processRepository.findById(taskDTO.getProcessId()).orElseThrow(() -> new ProcessNotFoundException(
                        "Process not found"));

        taskDTO.setProcess(process);
        taskDTO.setUser(user);

        return taskRepository.save(taskDTO.toEntity());
    }

    public void deleteTask(UUID taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId.toString()));
        taskRepository.deleteById(taskId);
    }
}
