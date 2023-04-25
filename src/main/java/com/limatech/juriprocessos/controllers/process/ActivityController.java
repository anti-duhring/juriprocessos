package com.limatech.juriprocessos.controllers.process;

import com.limatech.juriprocessos.dtos.process.CreateActivityDTO;
import com.limatech.juriprocessos.models.process.Activity;
import com.limatech.juriprocessos.services.ActivityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.url_base}/activity")
public class ActivityController {

    @Autowired
    ActivityService activityService;

    @PostMapping
    public ResponseEntity<?> createActivity(
            @RequestBody @Valid CreateActivityDTO activityDTO
    ) {
        Activity activity = activityService.createActivity(activityDTO);
        return ResponseEntity.ok(activity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateActivity(
            @PathVariable Long id,
            @RequestBody @Valid CreateActivityDTO activityDTO
    ) {
        Activity activity = activityService.updateActivity(id, activityDTO);
        return ResponseEntity.ok(activity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteActivity(
            @PathVariable Long id
    ) {
        activityService.deleteActivity(id);
        return ResponseEntity.noContent().build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getActivity(
            @PathVariable Long id
    ) {
        Activity activity = activityService.getActivity(id);
        return ResponseEntity.ok(activity);
    }
}
