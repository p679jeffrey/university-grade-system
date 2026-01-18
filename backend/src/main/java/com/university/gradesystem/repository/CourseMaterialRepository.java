
package com.university.gradesystem.repository;

import com.university.gradesystem.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface CourseMaterialRepository extends JpaRepository<CourseMaterial, Integer> {
    List<CourseMaterial> findByCourseIdOrderByUploadedAtDesc(Integer courseId);
}