package com.limatech.juriprocessos.services;

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
import com.limatech.juriprocessos.services.user.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class TaskServiceTest {

    TaskService taskService;

    TaskRepository taskRepository = Mockito.mock(TaskRepository.class);

    UserRepository userRepository = Mockito.mock(UserRepository.class);

    ProcessRepository processRepository = Mockito.mock(ProcessRepository.class);

    @BeforeEach
    void setup() {
        taskService = new TaskService(taskRepository, userRepository, processRepository);
    }

    @Test
    void shouldCreateTask() {
        // given
        User user = new User();
        Process process = new Process();

        user.setId(UUID.randomUUID());
        process.setId(UUID.randomUUID());

        LocalDateTime deadline = LocalDateTime.now().plusMonths(1);
        CreateTaskDTO taskDTO = new CreateTaskDTO(
                user.getId(),
                process.getId(),
                "Task name",
                "Description",
                deadline
        );
        Task task = taskDTO.toEntity();

        // when
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(processRepository.findById(process.getId())).thenReturn(Optional.of(process));

        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);

        Task taskReturned = taskService.createTask(taskDTO);

        // then
        Assertions.assertEquals(taskDTO.getName(), taskReturned.getName());
        Assertions.assertEquals(taskDTO.getDescription(), taskReturned.getDescription());
        Assertions.assertEquals(taskDTO.getDeadlineAt(), taskReturned.getDeadlineAt());
        Assertions.assertEquals(user, taskDTO.getUser());
        Assertions.assertEquals(process, taskDTO.getProcess());
    }

    @Test
    void shouldThrowUserNotFoundWhenTryToCreateATaskToInexistentUser() {
        // given
        LocalDateTime deadline = LocalDateTime.now().plusMonths(1);
        CreateTaskDTO taskDTO = new CreateTaskDTO(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Task name",
                "Description",
                deadline
        );

        // when
        Mockito.when(userRepository.findById(taskDTO.getUserId())).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(UserNotFoundException.class, () -> taskService.createTask(taskDTO));
    }

    @Test
    void shouldThrowProcessNotFoundWhenTryToCreateATaskToInexistentProcess() {
        // given
        User user = new User();
        user.setId(UUID.randomUUID());

        LocalDateTime deadline = LocalDateTime.now().plusMonths(1);
        CreateTaskDTO taskDTO = new CreateTaskDTO(
                user.getId(),
                UUID.randomUUID(),
                "Task name",
                "Description",
                deadline
        );

        // when
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(processRepository.findById(taskDTO.getProcessId())).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(ProcessNotFoundException.class, () -> taskService.createTask(taskDTO));
    }

    @Test
    void shouldCallDeleteByIdWhenDeleteTask() {
        // given
        UUID id = UUID.randomUUID();
        Task task = new Task();

        // when
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        taskService.deleteTask(id);

        // then
        Mockito.verify(taskRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void shouldThrowTaskNotFoundWhenTryToDeleteATaskThatDoesNotExist() {
        // given
        UUID id = UUID.randomUUID();

        // when
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(id));
    }
}
