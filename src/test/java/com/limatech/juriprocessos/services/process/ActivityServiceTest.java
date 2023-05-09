package com.limatech.juriprocessos.services.process;

import com.limatech.juriprocessos.dtos.process.CreateActivityDTO;
import com.limatech.juriprocessos.dtos.process.CreateProcessDTO;
import com.limatech.juriprocessos.exceptions.process.ActivityNotFoundException;
import com.limatech.juriprocessos.exceptions.process.ProcessNotFoundException;
import com.limatech.juriprocessos.models.process.entity.Activity;
import com.limatech.juriprocessos.models.process.entity.Process;
import com.limatech.juriprocessos.repository.process.ActivityRepository;
import com.limatech.juriprocessos.repository.process.ProcessRepository;
import com.limatech.juriprocessos.services.process.ActivityService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class ActivityServiceTest {

    ActivityService activityService;

    ActivityRepository activityRepository = Mockito.mock(ActivityRepository.class);
    ProcessRepository processRepository = Mockito.mock(ProcessRepository.class);

    @BeforeEach
    void setUp() {
        activityService = new ActivityService(this.activityRepository, this.processRepository);
    }

    @Test
    void shouldCallSaveWhenCreateActivity() {
        // given
        UUID processId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LocalDateTime  now = LocalDateTime.now();

        CreateProcessDTO processDTO = new CreateProcessDTO(userId, "000", "PE", "TJPE", "1", "vara");
        CreateActivityDTO activityDTO = new CreateActivityDTO(processId, "Contestação", "Constestação do requerido",
                "Contestação", now);

        Process process = processDTO.toEntity();
        process.setId(processId);

        activityDTO.setProcess(process);

        // when
        Mockito.when(processRepository.findById(processId)).thenReturn(Optional.of(process));
        Mockito.when(activityRepository.save(Mockito.any(Activity.class))).thenReturn(activityDTO.toEntity());

        // then
        Activity activityCreated = activityService.createActivity(activityDTO);
        Mockito.verify(activityRepository, Mockito.times(1)).save(
                Mockito.any(Activity.class)
        );
        Mockito.verify(processRepository, Mockito.times(1)).findById(processId);

        Assertions.assertEquals(activityCreated.getProcess().getId(), processId);
        Assertions.assertEquals(activityCreated.getName(), activityDTO.getName());
        Assertions.assertEquals(activityCreated.getDescription(), activityDTO.getDescription());
        Assertions.assertEquals(activityCreated.getType(), activityDTO.getType());
        Assertions.assertEquals(activityCreated.getDate(), activityDTO.getDate().toString());
    }

    @Test
    void shouldThrowProcessNotFoundExceptionWhenCreateActivityInInexistentProcess() {
        // given
        UUID processId = UUID.randomUUID();
        LocalDateTime  now = LocalDateTime.now();

        CreateActivityDTO activityDTO = new CreateActivityDTO(processId, "Contestação", "Constestação do requerido",
                "Contestação", now);

        // when
        Mockito.when(processRepository.findById(processId)).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(ProcessNotFoundException.class, () -> {
            activityService.createActivity(activityDTO);
        });
    }

    @Test
    void shouldCallDeleteByIdWhenDeleteActivity() {
        // given
        UUID processId = UUID.randomUUID();
        LocalDateTime  now = LocalDateTime.now();

        CreateActivityDTO activityDTO = new CreateActivityDTO(processId, "Contestação", "Constestação do requerido",
                "Contestação", now);
        Activity activity = activityDTO.toEntity();
        activity.setId(UUID.randomUUID());

        // when
        Mockito.when(activityRepository.findById(activity.getId())).thenReturn(Optional.of(activity));
        activityService.deleteActivity(activity.getId());

        // then
        Mockito.verify(activityRepository, Mockito.times(1)).deleteById(activity.getId());
    }

    @Test
    void shouldThrowActivityNotFoundExceptionWhenDeleteInexistentActivity() {

        // given
        UUID id = UUID.randomUUID();

        // when
        Mockito.when(activityRepository.findById(id)).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(ActivityNotFoundException.class, () -> {
            activityService.deleteActivity(id);
        });
    }

    @Test
    void shouldCallSaveWhenUpdateActivity() {
        // given
        UUID processId = UUID.randomUUID();
        LocalDateTime  now = LocalDateTime.now();

         CreateActivityDTO activityDTO = new CreateActivityDTO(processId, "Contestação", "Constestação do requerido",
                "Contestação", now);
         CreateActivityDTO activityDTOUpdated = new CreateActivityDTO(processId, "Sentença", "Sentença dando " +
                 "provimento ao mérito", "Sentença", now);

         Activity activity = activityDTO.toEntity();
         Activity activityUpdated = activityDTOUpdated.toEntity();

         UUID id = UUID.randomUUID();
         activity.setId(id);
         activityUpdated.setId(id);

        // when
        Mockito.when(activityRepository.findById(activity.getId())).thenReturn(Optional.of(activity));
        Mockito.when(activityRepository.save(Mockito.any(Activity.class))).thenReturn(activityDTOUpdated.toEntity());
        Activity activityUpdatedReturned = activityService.updateActivity(activity.getId(), activityDTOUpdated);

        // then
        Mockito.verify(activityRepository, Mockito.times(1)).save(
                Mockito.any(Activity.class)
        );
        Mockito.verify(activityRepository, Mockito.times(1)).findById(activity.getId());

        Assertions.assertEquals(activityUpdatedReturned.getName(), activityDTOUpdated.getName());
        Assertions.assertEquals(activityUpdatedReturned.getDescription(), activityDTOUpdated.getDescription());
        Assertions.assertEquals(activityUpdatedReturned.getType(), activityDTOUpdated.getType());
        Assertions.assertEquals(activityUpdatedReturned.getDate(), activityDTOUpdated.getDate().toString());
    }

    @Test
    void shouldThrowActivityNotFoundExceptionWhenUpdateInexistentActivity() {
        // given
        UUID id = UUID.randomUUID();

        // when
        Mockito.when(activityRepository.findById(id)).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(ActivityNotFoundException.class, () -> {
            activityService.updateActivity(id, new CreateActivityDTO());
        });
    }

    @Test
    void shouldCallFindByIdWhenGetActivityById() {
        // given
        UUID processId = UUID.randomUUID();
        LocalDateTime  now = LocalDateTime.now();

        CreateActivityDTO activityDTO = new CreateActivityDTO(processId, "Contestação", "Constestação do requerido",
                "Contestação", now);

        UUID activityId = UUID.randomUUID();

        // when
        Mockito.when(activityRepository.findById(activityId)).thenReturn(Optional.of(activityDTO.toEntity()));
        Activity activityReturned = activityService.getActivity(activityId);

        // then
        Mockito.verify(activityRepository, Mockito.times(1)).findById(activityId);

        Assertions.assertEquals(activityReturned.getName(), activityDTO.getName());
        Assertions.assertEquals(activityReturned.getDescription(), activityDTO.getDescription());
        Assertions.assertEquals(activityReturned.getType(), activityDTO.getType());
        Assertions.assertEquals(activityReturned.getDate(), activityDTO.getDate().toString());
    }

    @Test
    void shouldThrowActivityNotFoundExceptionWhenGetInexistentActivity() {
        // given
        UUID id = UUID.randomUUID();

        // when
        Mockito.when(activityRepository.findById(id)).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(ActivityNotFoundException.class, () -> {
            activityService.getActivity(id);
        });
    }
}
