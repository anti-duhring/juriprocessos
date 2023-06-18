package com.limatech.juriprocessos.services.process;

import com.limatech.juriprocessos.dtos.process.CreateActivityDTO;
import com.limatech.juriprocessos.exceptions.process.ActivityNotFoundException;
import com.limatech.juriprocessos.exceptions.process.ProcessNotFoundException;
import com.limatech.juriprocessos.exceptions.users.ForbiddenActionException;
import com.limatech.juriprocessos.exceptions.users.UserNotFoundException;
import com.limatech.juriprocessos.models.process.entity.Activity;
import com.limatech.juriprocessos.models.process.entity.Group;
import com.limatech.juriprocessos.models.process.entity.Process;
import com.limatech.juriprocessos.models.users.entity.User;
import com.limatech.juriprocessos.repository.process.ActivityRepository;
import com.limatech.juriprocessos.repository.process.ProcessRepository;
import com.limatech.juriprocessos.repository.users.UserRepository;
import com.limatech.juriprocessos.services.interfaces.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityService implements UserValidation {

    private final ActivityRepository activityRepository;
    private final ProcessRepository processRepository;
    private final UserRepository userRepository;

    @Autowired
    public ActivityService(ActivityRepository activityRepository, ProcessRepository processRepository, UserRepository userRepository) {
        this.activityRepository = activityRepository;
        this.processRepository = processRepository;
        this.userRepository = userRepository;
    }

    public Activity createActivity(CreateActivityDTO activityDTO) {
        validateUserPermission(activityDTO);

        Process process = processRepository.findById(activityDTO.getProcessId()).orElseThrow(() -> new ProcessNotFoundException("Process not found"));
        activityDTO.setProcess(process);

        Activity activity = activityDTO.toEntity();

        return activityRepository.save(activity);
    }

    public void deleteActivity(UUID id) {
        validateUserPermission(id);

        Activity activity = activityRepository.findById(id).orElseThrow(() -> new ActivityNotFoundException("Activity not found"));
        activityRepository.deleteById(id);
    }

    public Activity updateActivity(UUID id, CreateActivityDTO activityDTO) {
        validateUserPermission(id);

        Activity activity = activityRepository.findById(id).orElseThrow(() -> new ActivityNotFoundException("Activity not found"));
        if(activityDTO.getName() != null) {
            activity.setName(activityDTO.getName());
        }
        if(activityDTO.getDescription() != null) {
            activity.setDescription(activityDTO.getDescription());
        }
        if(activityDTO.getDate() != null) {
            activity.setDate(activityDTO.getDate().toString());
        }
        if(activityDTO.getType() != null) {
            activity.setType(activityDTO.getType());
        }

        return activityRepository.save(activity);
    }

    public Activity getActivity(UUID id) {
        return activityRepository.findById(id).orElseThrow(() -> new ActivityNotFoundException("Activity not found"));
    }

    @Override
    public void validateUserPermission(UUID id) {
        User currentUserFromContext = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID currentUserId = currentUserFromContext.getId();
        User currentUserFromDB = userRepository.findById(currentUserId).orElseThrow(() -> new UserNotFoundException(
                "User not found"));

        Activity activity = activityRepository.findById(id).orElseThrow(() -> new ActivityNotFoundException("Activity not found"));
        Process process = activity.getProcess();


        if(
                !process.getUser().getId().toString().equals(currentUserId.toString()) &&
                !userCanWriteOnProcess(currentUserFromDB, process) &&
                !this.isUserAdmin()
        ) {
            throw new ForbiddenActionException();
        }
    }

    public void validateUserPermission(CreateActivityDTO activityDTO) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID currentUserId = currentUser.getId();
        User currentUserFromDB = userRepository.findById(currentUserId).orElseThrow(() -> new UserNotFoundException(
                "User not found"));

        Process process = processRepository.findById(activityDTO.getProcessId()).orElseThrow(() -> new ProcessNotFoundException("Process not found"));

        if(
                !process.getUser().getId().toString().equals(currentUserId.toString()) &&
                !userCanWriteOnProcess(currentUserFromDB, process) &&
                !this.isUserAdmin()
        ) {
            throw new ForbiddenActionException();
        }
    }
}
