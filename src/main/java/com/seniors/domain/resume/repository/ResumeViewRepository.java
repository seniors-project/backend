package com.seniors.domain.resume.repository;

import com.seniors.domain.resume.entity.Resume;
import com.seniors.domain.resume.entity.ResumeView;
import com.seniors.domain.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResumeViewRepository extends JpaRepository<ResumeView, Long> {

    Optional<ResumeView> findByUsersAndResume(Users user, Resume resume);
}
