package com.seniors.domain.resume.repository;

import com.seniors.domain.resume.entity.Resume;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface ResumeRepository extends JpaRepository<Resume, Long>, ResumeRepositoryCustom {

}
