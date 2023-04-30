package com.limatech.juriprocessos.repository.process;

import com.limatech.juriprocessos.models.process.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, UUID> {
}
