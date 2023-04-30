package com.limatech.juriprocessos.services.process;

import com.limatech.juriprocessos.dtos.process.CreateActivityDTO;
import com.limatech.juriprocessos.exceptions.process.ActivityNotFoundException;
import com.limatech.juriprocessos.exceptions.process.ProcessNotFoundException;
import com.limatech.juriprocessos.models.process.entity.Activity;
import com.limatech.juriprocessos.models.process.entity.Process;
import com.limatech.juriprocessos.repository.process.ActivityRepository;
import com.limatech.juriprocessos.repository.process.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final ProcessRepository processRepository;

    @Autowired
    public ActivityService(ActivityRepository activityRepository, ProcessRepository processRepository) {
        this.activityRepository = activityRepository;
        this.processRepository = processRepository;
    }

    public Activity createActivity(CreateActivityDTO activityDTO) {
        Process process = processRepository.findById(activityDTO.getProcessId()).orElseThrow(() -> new ProcessNotFoundException("Process not found"));
        activityDTO.setProcess(process);

        Activity activity = activityDTO.toEntity();

        return activityRepository.save(activity);
    }

    public void deleteActivity(UUID id) {
        Activity activity = activityRepository.findById(id).orElseThrow(() -> new ActivityNotFoundException("Activity not found"));
        activityRepository.deleteById(id);
    }

    public Activity updateActivity(UUID id, CreateActivityDTO activityDTO) {

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
}
