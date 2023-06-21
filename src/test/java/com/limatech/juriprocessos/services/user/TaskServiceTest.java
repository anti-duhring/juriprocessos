package com.limatech.juriprocessos.services.user;

import com.limatech.juriprocessos.dtos.users.CreateTaskDTO;
import com.limatech.juriprocessos.dtos.users.ManageTaskDTO;
import com.limatech.juriprocessos.exceptions.process.ProcessNotFoundException;
import com.limatech.juriprocessos.exceptions.users.TaskNotFoundException;
import com.limatech.juriprocessos.exceptions.users.UserNotFoundException;
import com.limatech.juriprocessos.models.process.entity.Process;
import com.limatech.juriprocessos.models.users.entity.Role;
import com.limatech.juriprocessos.models.users.entity.Task;
import com.limatech.juriprocessos.models.users.entity.User;
import com.limatech.juriprocessos.repository.process.ProcessRepository;
import com.limatech.juriprocessos.repository.users.TaskRepository;
import com.limatech.juriprocessos.repository.users.UserRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaskServiceTest {

    TaskRepository taskRepository = Mockito.mock(TaskRepository.class);

    UserRepository userRepository = Mockito.mock(UserRepository.class);

    ProcessRepository processRepository = Mockito.mock(ProcessRepository.class);

    User contextUser = Instancio.create(User.class);

    TaskService taskService = new TaskService(taskRepository, userRepository, processRepository);;

    @BeforeEach
    void restartContextUserData() {
        contextUser.setAuthorities(Role.USER);
        contextUser.setId(UUID.randomUUID());

        // Auth context setup
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal()).thenReturn(contextUser);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void shouldCreateTask() {
        // given
        User user = new User();
        Process process = new Process();

        UUID userId = UUID.randomUUID();
        user.setId(userId);
        contextUser.setId(userId);

        process.setId(UUID.randomUUID());

        CreateTaskDTO taskDTO = Instancio.create(CreateTaskDTO.class);
        LocalDateTime deadline = LocalDateTime.now().plusMonths(1);
        taskDTO.setDeadlineAt(deadline);
        taskDTO.setUserId(userId);
        taskDTO.setProcessId(process.getId());

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
        CreateTaskDTO taskDTO = Instancio.create(CreateTaskDTO.class);

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

        CreateTaskDTO taskDTO = Instancio.create(CreateTaskDTO.class);
        taskDTO.setUserId(user.getId());

        // when
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(processRepository.findById(taskDTO.getProcessId())).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(ProcessNotFoundException.class, () -> taskService.createTask(taskDTO));
    }

    @Test
    void shouldCallDeleteByIdWhenDeleteTask() {
        // given
        Task task = Instancio.create(Task.class);

        contextUser.setTasks(List.of(task));

        // when
        Mockito.when(userRepository.findById(contextUser.getId())).thenReturn(Optional.of(contextUser));
        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        taskService.deleteTask(task.getId());

        // then
        Mockito.verify(taskRepository, Mockito.times(1)).deleteById(task.getId());
    }

    @Test
    void shouldThrowTaskNotFoundWhenTryToDeleteATaskThatDoesNotExist() {
        // given
        UUID id = UUID.randomUUID();

        contextUser.setAuthorities(Role.ADMIN);

        // when
        Mockito.when(userRepository.findById(contextUser.getId())).thenReturn(Optional.of(contextUser));
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(id));
    }

    @Test
    void shouldUpdateTask() {
        // given
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID processId = UUID.randomUUID();

        ManageTaskDTO taskDTO = Instancio.create(ManageTaskDTO.class);
        taskDTO.setProcessId(processId);
        taskDTO.setUserId(userId);

        Task taskUpdated = taskDTO.toEntity();
        taskUpdated.setId(id);

        contextUser.setTasks(List.of(taskUpdated));

        // when
        Mockito.when(userRepository.findById(contextUser.getId())).thenReturn(Optional.of(contextUser));
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(new Task()));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        Mockito.when(processRepository.findById(processId)).thenReturn(Optional.of(new Process()));
        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(taskUpdated);

        Task taskUpdatedReturned = taskService.updateTask(id, taskDTO);

        // then
        Mockito.verify(taskRepository, Mockito.times(1)).save(Mockito.any(Task.class));
        Assertions.assertEquals(taskUpdatedReturned.getName(), taskUpdated.getName());
        Assertions.assertEquals(taskUpdatedReturned.getDescription(), taskUpdated.getDescription());
        Assertions.assertEquals(taskUpdatedReturned.getStartAt(), taskUpdated.getStartAt());
        Assertions.assertEquals(taskUpdatedReturned.getFinishAt(), taskUpdated.getFinishAt());
        Assertions.assertEquals(taskUpdatedReturned.getDeadlineAt(), taskUpdated.getDeadlineAt());
    }
//
//    @Test
//    void shouldThrowTaskNotFoundWhenTryToUpdateATaskThatDoesNotExist() {
//        // given
//        UUID id = UUID.randomUUID();
//
//        // when
//        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.empty());
//
//        // then
//        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(id, new ManageTaskDTO()));
//    }
//
//    @Test
//    void shouldThrowUserNotFoundWhenTryToUpdateATaskToInexistentUser() {
//        // given
//        UUID id = UUID.randomUUID();
//        UUID userId = UUID.randomUUID();
//        UUID processId = UUID.randomUUID();
//
//        ManageTaskDTO taskDTO = new ManageTaskDTO(
//                userId,
//                processId,
//                "Task name",
//                "Description",
//                null,
//                null,
//                null
//        );
//
//        // when
//        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(new Task()));
//        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());
//        Mockito.when(processRepository.findById(processId)).thenReturn(Optional.of(new Process()));
//        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(taskDTO.toEntity());
//
//        // then
//        Assertions.assertThrows(UserNotFoundException.class, () -> taskService.updateTask(id, taskDTO));
//
//    }
//    @Test
//    void shouldThrowProcessNotFoundWhenTryToUpdateATaskToInexistentProcess() {
//        // given
//        UUID id = UUID.randomUUID();
//        UUID userId = UUID.randomUUID();
//        UUID processId = UUID.randomUUID();
//
//        ManageTaskDTO taskDTO = new ManageTaskDTO(
//                userId,
//                processId,
//                "Task name",
//                "Description",
//                null,
//                null,
//                null
//        );
//
//        // when
//        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(new Task()));
//        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
//        Mockito.when(processRepository.findById(processId)).thenReturn(Optional.empty());
//        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(taskDTO.toEntity());
//
//        // then
//        Assertions.assertThrows(ProcessNotFoundException.class, () -> taskService.updateTask(id, taskDTO));
//
//    }
//
//    @Test
//    void shouldCallFindByIdWhenGetTask() {
//        // given
//        UUID id = UUID.randomUUID();
//
//        // when
//        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(new Task()));
//        taskService.getTask(id);
//
//        // then
//        Mockito.verify(taskRepository, Mockito.times(1)).findById(id);
//    }
//
//    @Test
//    void shouldThrowTaskNotFoundWhenTryToGetATaskThatDoesNotExist() {
//        // given
//        UUID id = UUID.randomUUID();
//
//        // when
//        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.empty());
//
//        // then
//        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.getTask(id));
//    }
}
