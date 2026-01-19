# 🎓 大學成績管理系統

完整的學生資訊管理系統,包含課程管理、公告、教材、作業等功能。

## 📂 專案結構

```
university-grade-system/
├── backend/                          # 後端 (Spring Boot)
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/university/gradesystem/
│   │       │       ├── GradeSystemApplication.java
│   │       │       ├── controller/
│   │       │       │   ├── GradeController.java
│   │       │       │   └── CourseManagementController.java
│   │       │       ├── entity/
│   │       │       │   ├── Teacher.java
│   │       │       │   ├── Student.java
│   │       │       │   ├── Course.java
│   │       │       │   ├── Enrollment.java
│   │       │       │   ├── Grade.java
│   │       │       │   ├── CourseAnnouncement.java
│   │       │       │   ├── CourseMaterial.java
│   │       │       │   ├── CourseAssignment.java
│   │       │       │   └── AssignmentSubmission.java
│   │       │       ├── repository/
│   │       │       │   ├── TeacherRepository.java
│   │       │       │   ├── StudentRepository.java
│   │       │       │   ├── CourseRepository.java
│   │       │       │   ├── EnrollmentRepository.java
│   │       │       │   ├── GradeRepository.java
│   │       │       │   ├── CourseAnnouncementRepository.java
│   │       │       │   ├── CourseMaterialRepository.java
│   │       │       │   ├── CourseAssignmentRepository.java
│   │       │       │   └── AssignmentSubmissionRepository.java
│   │       │       ├── service/
│   │       │       │   └── FileStorageService.java
│   │       │       └── dto/
│   │       │           ├── LoginRequest.java
│   │       │           ├── LoginResponse.java
│   │       │           ├── CourseRequest.java
│   │       │           ├── StudentWithGrade.java
│   │       │           ├── GradeRequest.java
│   │       │           ├── StudentGradeResponse.java
│   │       │           └── MessageResponse.java
│   │       └── resources/
│   │           └── application.properties.example
│   ├── pom.xml
│   ├── .gitignore
│   └── README.md
│
├── frontend/                         # 前端 (HTML/CSS/JS)
│   ├── teacher/
│   │   ├── login.html
│   │   └── dashboard.html
│   │   └── teacher-app.js
│   ├── student/
│   │   └── portal.html
│   
│
├── database/                         # 資料庫
│   ├── schema.sql                   # 資料表結構
│   ├── sample-data.sql              # 測試資料
│   
│
│
├── .gitignore                       # Git 忽略檔案
├── README.md                        # 專案說明
└── LICENSE                          # 授權條款
```

## 🚀 快速開始

### 環境需求
- Java 17+
- Maven 3.6+
- MySQL 8.0+
- 現代瀏覽器 (Chrome, Firefox, Edge)

### 安裝步驟

1. **Clone 專案**
```bash
git clone https://github.com/你的帳號/university-grade-system.git
cd university-grade-system
```

2. **建立資料庫**
```bash
mysql -u root -p < database/schema.sql
mysql -u root -p < database/sample-data.sql
```

3. **設定後端**
```bash
cd backend
cp src/main/resources/application.properties.example src/main/resources/application.properties
# 編輯 application.properties 設定資料庫密碼
```

4. **啟動後端**
```bash
mvn clean spring-boot:run
```

5. **開啟前端**
- 老師端: 瀏覽器開啟 `frontend/teacher/login.html`
- 學生端: 瀏覽器開啟 `frontend/student/login.html`

### 測試帳號

**老師**
- 帳號: T001
- 密碼: pass123

**學生**
- 帳號: S001
- 密碼: pass123

## 📚 功能說明

### 老師端
- ✅ 課程管理
- ✅ 學生名單管理
- ✅ 課程公告 (新增/編輯/刪除)
- ✅ 教材上傳與管理
- ✅ 作業管理
- ✅ 成績輸入與管理

### 學生端
- ✅ 查看課程成績
- ✅ 瀏覽課程公告
- ✅ 下載課程教材
- ✅ 提交作業

## 🛠️ 技術架構

### 後端
- **框架**: Spring Boot 3.2.0
- **資料庫**: MySQL 8.0
- **ORM**: Spring Data JPA / Hibernate
- **檔案上傳**: MultipartFile

### 前端
- **基礎**: HTML5 + CSS3 + JavaScript (ES6)
- **UI**: 原生 CSS (響應式設計)
- **API 呼叫**: Fetch API

### 資料庫
- **類型**: MySQL
- **表數量**: 9 個主要資料表
- **關聯**: 外鍵約束

## 📖 API 文檔

詳細 API 文檔請參考: [docs/api-documentation.md](docs/api-documentation.md)

### 主要端點

#### 認證
- `POST /teacher/login` - 老師登入
- `POST /student/login` - 學生登入

#### 課程
- `GET /teacher/{teacherId}/courses` - 取得老師課程
- `POST /courses` - 新增課程

#### 公告
- `GET /api/courses/{courseId}/announcements` - 取得公告
- `POST /api/courses/{courseId}/announcements` - 新增公告
- `DELETE /api/courses/announcements/{id}` - 刪除公告

#### 教材
- `GET /api/courses/{courseId}/materials` - 取得教材
- `POST /api/courses/{courseId}/materials` - 上傳教材
- `GET /api/courses/materials/{id}/download` - 下載教材
- `DELETE /api/courses/materials/{id}` - 刪除教材

#### 作業
- `GET /api/courses/{courseId}/assignments` - 取得作業
- `POST /api/courses/{courseId}/assignments` - 新增作業
- `POST /api/courses/assignments/{id}/submit` - 學生提交作業

#### 成績
- `GET /courses/{courseId}/students` - 取得課程學生與成績
- `POST /grades` - 輸入/更新成績
- `GET /student/{studentId}/grades` - 學生查詢成績

## 🔒 安全性

- ✅ CORS 跨域設定
- ✅ SQL 注入防護 (使用參數化查詢)
- ⚠️ 生產環境建議加入:
  - JWT 認證
  - 密碼加密 (BCrypt)
  - HTTPS
  - 檔案上傳驗證

## 📝 開發紀錄

### Version 1.0.0 (2026-01-19)
- ✅ 基本認證功能
- ✅ 課程管理
- ✅ 成績管理
- ✅ 公告系統
- ✅ 教材上傳
- ✅ 作業管理

