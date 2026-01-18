-- ========================================
-- 2. 測試資料 (Sample Data)
-- ========================================

USE university_grades;

-- 插入老師資料
INSERT INTO teachers (teacher_id, name, password) VALUES 
('T001', '李老師', 'pass123'),
('T002', '王老師', 'pass123'),
('T003', '陳老師', 'pass123')
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- 插入學生資料
INSERT INTO students (student_id, name, password) VALUES 
('S001', '張小明', 'pass123'),
('S002', '陳小華', 'pass123'),
('S003', '林小美', 'pass123'),
('S004', '黃小強', 'pass123'),
('S005', '劉小芳', 'pass123')
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- 插入課程資料
INSERT INTO courses (course_id, course_name, teacher_id) VALUES 
(1, '資料庫系統', 'T001'),
(2, '網頁程式設計', 'T001'),
(3, '資料結構', 'T002'),
(4, '作業系統', 'T002'),
(5, '演算法', 'T003')
ON DUPLICATE KEY UPDATE course_name=VALUES(course_name);

-- 插入選課資料
INSERT INTO enrollments (student_id, course_id) VALUES 
('S001', 1), ('S001', 2),
('S002', 1), ('S002', 3),
('S003', 2), ('S003', 3),
('S004', 1), ('S004', 2), ('S004', 3),
('S005', 1), ('S005', 4)
ON DUPLICATE KEY UPDATE student_id=student_id;

-- 插入成績資料
INSERT INTO grades (student_id, course_id, score) VALUES 
('S001', 1, 85),
('S001', 2, 90),
('S002', 1, 78),
('S003', 2, 92)
ON DUPLICATE KEY UPDATE score=VALUES(score);

-- 插入課程公告
INSERT INTO course_announcements (course_id, title, content) VALUES 
(1, '期中考試通知', '期中考試將於下週三舉行...'),
(1, '補課通知', '本週五因故停課...'),
(2, '專題報告說明', '專題報告需於期末繳交...')
ON DUPLICATE KEY UPDATE title=VALUES(title);

-- 插入課程教材
INSERT INTO course_materials (course_id, title, description, file_name, file_path, file_size) VALUES 
(1, '第一章：資料庫概論', 'SQL基礎語法介紹', 'chapter1.pdf', '/uploads/materials/chapter1.pdf', 2048576)
ON DUPLICATE KEY UPDATE title=VALUES(title);

-- 插入課程作業
INSERT INTO course_assignments (course_id, title, description, due_date) VALUES 
(1, '作業一：SQL查詢練習', '完成課本第3章習題', '2026-02-01 23:59:59')
ON DUPLICATE KEY UPDATE title=VALUES(title);