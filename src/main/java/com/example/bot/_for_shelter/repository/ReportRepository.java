package com.example.bot._for_shelter.repository;

import com.example.bot._for_shelter.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
