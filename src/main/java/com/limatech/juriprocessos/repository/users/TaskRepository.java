package com.limatech.juriprocessos.repository.users;

import com.limatech.juriprocessos.models.users.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
}
