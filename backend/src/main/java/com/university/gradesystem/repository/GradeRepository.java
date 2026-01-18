package com.university.gradesystem.repository;

import com.university.gradesystem.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Integer> {
    Optional<Grade> findByStudentIdAndCourseId(String studentId, Integer courseId);
    List<Grade> findByStudentId(String studentId);
    List<Grade> findByCourseId(Integer courseId);
}