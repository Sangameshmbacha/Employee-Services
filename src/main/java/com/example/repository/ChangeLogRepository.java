package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.entity.ChangeLog;

public interface ChangeLogRepository extends JpaRepository<ChangeLog, Long> {
}