package com.university.gradesystem.controller;

import com.university.gradesystem.dto.*;
import com.university.gradesystem.entity.*;
import com.university.gradesystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
public class GradeController {

    @Autowired private TeacherRepository teacherRepo;
    @Autowired private StudentRepository studentRepo;
    @Autowired private CourseRepository courseRepo;
    @Autowired private EnrollmentRepository enrollmentRepo;
    @Autowired private GradeRepository gradeRepo;

    // ========== 老師登入 ==========
    @PostMapping("/teacher/login")
    public ResponseEntity<?> teacherLogin(@RequestBody LoginRequest request) {
        Optional<Teacher> teacher = teacherRepo.findByTeacherIdAndPassword(
            request.getTeacher_id(), request.getPassword());
        
        if (teacher.isPresent()) {
            return ResponseEntity.ok(new LoginResponse(
                teacher.get().getTeacherId(), 
                teacher.get().getName()
            ));
        }
        
        // 修正:登入失敗的回應
        LoginResponse errorResponse = new LoginResponse();
        errorResponse.setError("帳號或密碼錯誤");
        return ResponseEntity.status(401).body(errorResponse);
    }

    // ========== 學生登入 ==========
    @PostMapping("/student/login")
    public ResponseEntity<?> studentLogin(@RequestBody LoginRequest request) {
        Optional<Student> student = studentRepo.findByStudentIdAndPassword(
            request.getStudent_id(), request.getPassword());
        
        if (student.isPresent()) {
            return ResponseEntity.ok(new LoginResponse(
                student.get().getStudentId(), 
                student.get().getName()
            ));
        }
        
        // 修正:登入失敗的回應
        LoginResponse errorResponse = new LoginResponse();
        errorResponse.setError("帳號或密碼錯誤");
        return ResponseEntity.status(401).body(errorResponse);
    }

    // ========== 查詢老師的課程列表 ==========
    @GetMapping("/teacher/{teacherId}/courses")
    public List<Course> getTeacherCourses(@PathVariable String teacherId) {
        return courseRepo.findByTeacherId(teacherId);
    }

    // ========== 新增課程 ==========
    @PostMapping("/courses")
    public ResponseEntity<?> addCourse(@RequestBody CourseRequest request) {
        Course course = new Course();
        course.setCourseName(request.getCourse_name());
        course.setTeacherId(request.getTeacher_id());
        
        Course saved = courseRepo.save(course);
        
        MessageResponse response = new MessageResponse("課程新增成功");
        response.setData(saved.getCourseId());
        return ResponseEntity.ok(response);
    }

    // ========== 查詢課程的學生名單 (含成績) ==========
    @GetMapping("/courses/{courseId}/students")
    public List<StudentWithGrade> getCourseStudents(@PathVariable Integer courseId) {
        List<Enrollment> enrollments = enrollmentRepo.findByCourseId(courseId);
        List<StudentWithGrade> result = new ArrayList<>();
        
        for (Enrollment enrollment : enrollments) {
            Optional<Student> student = studentRepo.findById(enrollment.getStudentId());
            if (student.isPresent()) {
                Optional<Grade> grade = gradeRepo.findByStudentIdAndCourseId(
                    enrollment.getStudentId(), courseId);
                
                StudentWithGrade swg = new StudentWithGrade();
                swg.setStudent_id(student.get().getStudentId());
                swg.setName(student.get().getName());
                swg.setScore(grade.isPresent() ? grade.get().getScore() : null);
                result.add(swg);
            }
        }
        
        return result;
    }

    // ========== 輸入/更新成績 ==========
    @PostMapping("/grades")
    public ResponseEntity<?> saveGrade(@RequestBody GradeRequest request) {
        Optional<Grade> existing = gradeRepo.findByStudentIdAndCourseId(
            request.getStudent_id(), request.getCourse_id());
        
        Grade grade;
        if (existing.isPresent()) {
            grade = existing.get();
            grade.setScore(request.getScore());
        } else {
            grade = new Grade();
            grade.setStudentId(request.getStudent_id());
            grade.setCourseId(request.getCourse_id());
            grade.setScore(request.getScore());
        }
        
        gradeRepo.save(grade);
        return ResponseEntity.ok(new MessageResponse("成績更新成功"));
    }

    // ========== 學生查詢自己的成績 ==========
    @GetMapping("/student/{studentId}/grades")
    public List<StudentGradeResponse> getStudentGrades(@PathVariable String studentId) {
        List<Enrollment> enrollments = enrollmentRepo.findByStudentId(studentId);
        List<StudentGradeResponse> result = new ArrayList<>();
        
        for (Enrollment enrollment : enrollments) {
            Optional<Course> course = courseRepo.findById(enrollment.getCourseId());
            if (course.isPresent()) {
                Optional<Teacher> teacher = teacherRepo.findById(course.get().getTeacherId());
                Optional<Grade> grade = gradeRepo.findByStudentIdAndCourseId(
                    studentId, enrollment.getCourseId());
                
                StudentGradeResponse sgr = new StudentGradeResponse();
                sgr.setCourse_name(course.get().getCourseName());
                sgr.setTeacher_name(teacher.isPresent() ? teacher.get().getName() : "");
                sgr.setScore(grade.isPresent() ? grade.get().getScore() : null);
                result.add(sgr);
            }
        }
        
        return result;
    }

    // ========== 查詢所有學生 ==========
    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    // ========== 將學生加入課程 ==========
    @PostMapping("/enrollments")
    public ResponseEntity<?> addEnrollment(@RequestBody Enrollment enrollment) {
        enrollmentRepo.save(enrollment);
        return ResponseEntity.ok(new MessageResponse("學生加入成功"));
    }

    // ========== 測試用首頁 ==========
    @GetMapping("/")
    public String home() {
        return "🎓 成績管理系統 API 運行中<br>" +
               "請使用前端介面存取系統<br>" +
               "API 文件:<br>" +
               "- POST /teacher/login<br>" +
               "- POST /student/login<br>" +
               "- GET /teacher/{teacherId}/courses<br>" +
               "- POST /courses<br>" +
               "- GET /courses/{courseId}/students<br>" +
               "- POST /grades<br>" +
               "- GET /student/{studentId}/grades";
    }
}