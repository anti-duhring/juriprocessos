package com.limatech.juriprocessos.repository.process;

import com.limatech.juriprocessos.models.process.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProcessRepository extends JpaRepository<Process, UUID> {
}
