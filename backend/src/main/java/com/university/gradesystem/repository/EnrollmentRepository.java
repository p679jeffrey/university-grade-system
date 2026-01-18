package com.university.gradesystem.repository;

import com.university.gradesystem.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    List<Enrollment> findByCourseId(Integer courseId);
    List<Enrollment> findByStudentId(String studentId);
}