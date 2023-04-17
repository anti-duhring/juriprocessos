package com.limatech.juriprocessos.repository.process;

import com.limatech.juriprocessos.models.process.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
}
