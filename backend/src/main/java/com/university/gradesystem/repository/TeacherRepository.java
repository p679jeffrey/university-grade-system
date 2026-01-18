package com.university.gradesystem.repository;

import com.university.gradesystem.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, String> {
    Optional<Teacher> findByTeacherIdAndPassword(String teacherId, String password);
}