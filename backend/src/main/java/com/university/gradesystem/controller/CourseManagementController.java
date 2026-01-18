package com.university.gradesystem.controller;

import com.university.gradesystem.entity.*;
import com.university.gradesystem.repository.*;
import com.university.gradesystem.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CourseManagementController {

    @Autowired private CourseRepository courseRepo;
    @Autowired private CourseAnnouncementRepository announcementRepo;
    @Autowired private CourseMaterialRepository materialRepo;
    @Autowired private CourseAssignmentRepository assignmentRepo;
    @Autowired private AssignmentSubmissionRepository submissionRepo;
    @Autowired private EnrollmentRepository enrollmentRepo;
    @Autowired private StudentRepository studentRepo;
    @Autowired private FileStorageService fileStorageService;

    // ========== 課程公告 CRUD ==========
    
    @GetMapping("/{courseId}/announcements")
    public List<CourseAnnouncement> getAnnouncements(@PathVariable Integer courseId) {
        return announcementRepo.findByCourseIdOrderByCreatedAtDesc(courseId);
    }

    @PostMapping("/{courseId}/announcements")
    public ResponseEntity<?> createAnnouncement(@PathVariable Integer courseId, @RequestBody CourseAnnouncement announcement) {
        announcement.setCourseId(courseId);
        announcement.setCreatedAt(LocalDateTime.now());
        announcement.setUpdatedAt(LocalDateTime.now());
        CourseAnnouncement saved = announcementRepo.save(announcement);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/announcements/{id}")
    public ResponseEntity<?> updateAnnouncement(@PathVariable Integer id, @RequestBody CourseAnnouncement announcement) {
        Optional<CourseAnnouncement> existing = announcementRepo.findById(id);
        if (existing.isPresent()) {
            CourseAnnouncement updated = existing.get();
            updated.setTitle(announcement.getTitle());
            updated.setContent(announcement.getContent());
            updated.setUpdatedAt(LocalDateTime.now());
            announcementRepo.save(updated);
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/announcements/{id}")
    public ResponseEntity<?> deleteAnnouncement(@PathVariable Integer id) {
        announcementRepo.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "刪除成功"));
    }

    // ========== 課程教材 CRUD ==========
    
    @GetMapping("/{courseId}/materials")
    public List<CourseMaterial> getMaterials(@PathVariable Integer courseId) {
        return materialRepo.findByCourseIdOrderByUploadedAtDesc(courseId);
    }

    @PostMapping("/{courseId}/materials")
    public ResponseEntity<?> uploadMaterial(
            @PathVariable Integer courseId,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file) {
        
        try {
            String filePath = fileStorageService.storeMaterial(file);
            
            CourseMaterial material = new CourseMaterial();
            material.setCourseId(courseId);
            material.setTitle(title);
            material.setDescription(description);
            material.setFileName(file.getOriginalFilename());
            material.setFilePath(filePath);
            material.setFileSize(file.getSize());
            material.setUploadedAt(LocalDateTime.now());
            
            CourseMaterial saved = materialRepo.save(material);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "上傳失敗: " + e.getMessage()));
        }
    }

    @GetMapping("/materials/{id}/download")
    public ResponseEntity<Resource> downloadMaterial(@PathVariable Integer id) {
        try {
            Optional<CourseMaterial> material = materialRepo.findById(id);
            if (material.isPresent()) {
                Resource resource = fileStorageService.loadFile(material.get().getFilePath());
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, 
                                "attachment; filename=\"" + material.get().getFileName() + "\"")
                        .body(resource);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/materials/{id}")
    public ResponseEntity<?> deleteMaterial(@PathVariable Integer id) {
        try {
            Optional<CourseMaterial> material = materialRepo.findById(id);
            if (material.isPresent()) {
                fileStorageService.deleteFile(material.get().getFilePath());
                materialRepo.deleteById(id);
                return ResponseEntity.ok(Map.of("message", "刪除成功"));
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "刪除失敗: " + e.getMessage()));
        }
    }

    // ========== 課程作業 CRUD ==========
    
    @GetMapping("/{courseId}/assignments")
    public List<Map<String, Object>> getAssignments(@PathVariable Integer courseId) {
        List<CourseAssignment> assignments = assignmentRepo.findByCourseIdOrderByDueDateAsc(courseId);
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (CourseAssignment assignment : assignments) {
            Map<String, Object> data = new HashMap<>();
            data.put("id", assignment.getId());
            data.put("courseId", assignment.getCourseId());
            data.put("title", assignment.getTitle());
            data.put("description", assignment.getDescription());
            data.put("dueDate", assignment.getDueDate());
            data.put("createdAt", assignment.getCreatedAt());
            
            List<AssignmentSubmission> submissions = submissionRepo.findByAssignmentId(assignment.getId());
            data.put("submissionCount", submissions.size());
            
            result.add(data);
        }
        
        return result;
    }

    @PostMapping("/{courseId}/assignments")
    public ResponseEntity<?> createAssignment(@PathVariable Integer courseId, @RequestBody CourseAssignment assignment) {
        assignment.setCourseId(courseId);
        assignment.setCreatedAt(LocalDateTime.now());
        CourseAssignment saved = assignmentRepo.save(assignment);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/assignments/{id}")
    public ResponseEntity<?> updateAssignment(@PathVariable Integer id, @RequestBody CourseAssignment assignment) {
        Optional<CourseAssignment> existing = assignmentRepo.findById(id);
        if (existing.isPresent()) {
            CourseAssignment updated = existing.get();
            updated.setTitle(assignment.getTitle());
            updated.setDescription(assignment.getDescription());
            updated.setDueDate(assignment.getDueDate());
            assignmentRepo.save(updated);
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/assignments/{id}")
    public ResponseEntity<?> deleteAssignment(@PathVariable Integer id) {
        assignmentRepo.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "刪除成功"));
    }

    // ========== 作業提交 ==========
    
    @GetMapping("/assignments/{assignmentId}/submissions")
    public List<Map<String, Object>> getSubmissions(@PathVariable Integer assignmentId) {
        List<AssignmentSubmission> submissions = submissionRepo.findByAssignmentId(assignmentId);
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (AssignmentSubmission sub : submissions) {
            Map<String, Object> data = new HashMap<>();
            data.put("id", sub.getId());
            data.put("assignmentId", sub.getAssignmentId());
            data.put("studentId", sub.getStudentId());
            data.put("fileName", sub.getFileName());
            data.put("submittedAt", sub.getSubmittedAt());
            data.put("score", sub.getScore());
            data.put("feedback", sub.getFeedback());
            
            Optional<Student> student = studentRepo.findById(sub.getStudentId());
            data.put("studentName", student.isPresent() ? student.get().getName() : "");
            
            result.add(data);
        }
        
        return result;
    }

    @PostMapping("/assignments/{assignmentId}/submit")
    public ResponseEntity<?> submitAssignment(
            @PathVariable Integer assignmentId,
            @RequestParam("studentId") String studentId,
            @RequestParam("file") MultipartFile file) {
        
        try {
            String filePath = fileStorageService.storeSubmission(file);
            
            Optional<AssignmentSubmission> existing = 
                submissionRepo.findByAssignmentIdAndStudentId(assignmentId, studentId);
            
            AssignmentSubmission submission;
            if (existing.isPresent()) {
                submission = existing.get();
                fileStorageService.deleteFile(submission.getFilePath());
            } else {
                submission = new AssignmentSubmission();
                submission.setAssignmentId(assignmentId);
                submission.setStudentId(studentId);
            }
            
            submission.setFileName(file.getOriginalFilename());
            submission.setFilePath(filePath);
            submission.setSubmittedAt(LocalDateTime.now());
            
            AssignmentSubmission saved = submissionRepo.save(submission);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "提交失敗: " + e.getMessage()));
        }
    }

    @GetMapping("/submissions/{id}/download")
    public ResponseEntity<Resource> downloadSubmission(@PathVariable Integer id) {
        try {
            Optional<AssignmentSubmission> submission = submissionRepo.findById(id);
            if (submission.isPresent()) {
                Resource resource = fileStorageService.loadFile(submission.get().getFilePath());
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, 
                                "attachment; filename=\"" + submission.get().getFileName() + "\"")
                        .body(resource);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/submissions/{id}/grade")
    public ResponseEntity<?> gradeSubmission(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> gradeData) {
        
        Optional<AssignmentSubmission> submission = submissionRepo.findById(id);
        if (submission.isPresent()) {
            AssignmentSubmission updated = submission.get();
            updated.setScore((Integer) gradeData.get("score"));
            updated.setFeedback((String) gradeData.get("feedback"));
            submissionRepo.save(updated);
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    // ========== 新增學生到課程 ==========
    
    @PostMapping("/{courseId}/students")
    public ResponseEntity<?> addStudentToCourse(
            @PathVariable Integer courseId,
            @RequestBody Map<String, String> data) {
        
        String studentId = data.get("studentId");
        
        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(studentId);
        enrollment.setCourseId(courseId);
        
        try {
            enrollmentRepo.save(enrollment);
            return ResponseEntity.ok(Map.of("message", "學生加入成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "學生可能已在課程中"));
        }
    }
}