package com.limatech.juriprocessos.services.user;

import com.limatech.juriprocessos.dtos.users.CreateTaskDTO;
import com.limatech.juriprocessos.dtos.users.ManageTaskDTO;
import com.limatech.juriprocessos.exceptions.process.ProcessNotFoundException;
import com.limatech.juriprocessos.exceptions.users.ForbiddenActionException;
import com.limatech.juriprocessos.exceptions.users.TaskNotFoundException;
import com.limatech.juriprocessos.exceptions.users.UserNotFoundException;
import com.limatech.juriprocessos.models.process.entity.Process;
import com.limatech.juriprocessos.models.users.entity.Task;
import com.limatech.juriprocessos.models.users.entity.User;
import com.limatech.juriprocessos.repository.process.ProcessRepository;
import com.limatech.juriprocessos.repository.users.TaskRepository;
import com.limatech.juriprocessos.repository.users.UserRepository;
import com.limatech.juriprocessos.services.interfaces.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskService implements UserValidation {

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
        validateUserPermission(taskDTO);

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
        validateUserPermission(taskId);

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId.toString()));
        taskRepository.deleteById(taskId);
    }

    public Task updateTask(UUID taskId, ManageTaskDTO taskDTO) {
        validateUserPermission(taskId);

        Task taskToBeUpdated = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId.toString()));

        if(!taskDTO.getName().isBlank()) {
            taskToBeUpdated.setName(taskDTO.getName());
        }

        if(!taskDTO.getDescription().isBlank()) {
            taskToBeUpdated.setDescription(taskDTO.getDescription());
        }

        if(taskDTO.getDeadlineAt() != null) {
            taskToBeUpdated.setDeadlineAt(taskDTO.getDeadlineAt());
        }

        if(taskDTO.getProcessId() != null) {
            Process process = processRepository.findById(taskDTO.getProcessId()).orElseThrow(() -> new ProcessNotFoundException(
                    "Process not found"));
            taskToBeUpdated.setProcess(process);
        }

        if(taskDTO.getUserId() != null) {
            User user = userRepository.findById(taskDTO.getUserId()).orElseThrow(() -> new UserNotFoundException("User " +
                    "not found"));
            taskToBeUpdated.setUser(user);
        }

        if(taskDTO.getStartAt() != null) {
            taskToBeUpdated.setStartAt(taskDTO.getStartAt());
        }

        if(taskDTO.getFinishAt() != null) {
            taskToBeUpdated.setFinishAt(taskDTO.getFinishAt());
        }

        return taskRepository.save(taskToBeUpdated);
    }

    public Task getTask(UUID taskId) {
        validateUserPermission(taskId);
        return taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId.toString()));
    }

    @Override
    public void validateUserPermission(UUID taskId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID currentUserId = currentUser.getId();

        User currentUserWithTasks =
                userRepository.findById(currentUserId).orElseThrow(() -> new UserNotFoundException("Current user not " +
                        "found"));

        List<Task> tasks = currentUserWithTasks.getTasks();
        List<UUID> ids = tasks.stream().map(Task::getId).toList();

        if(!ids.contains(taskId) && !isUserAdmin()) {
            throw new ForbiddenActionException();
        }
    }

    public void validateUserPermission(CreateTaskDTO taskDTO) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID currentUserId = currentUser.getId();
        User userFromDb  = userRepository.findById(taskDTO.getUserId()).orElseThrow(() -> new UserNotFoundException(
                "User not found"));

        Process process =
                processRepository.findById(taskDTO.getProcessId()).orElseThrow(() -> new ProcessNotFoundException(
                        "Process not found"));

        if(!taskDTO.getUserId().toString().equals(currentUserId.toString()) && !isUserAdmin() && !userCanWriteOnProcess(userFromDb, process)) {
            throw new ForbiddenActionException();
        }
    }
}
