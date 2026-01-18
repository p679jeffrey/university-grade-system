# 🚀 完整安裝指南

## 📋 前置需求

### 必須安裝的軟體

1. **Java Development Kit (JDK) 17 或以上**
   - 下載: https://adoptium.net/
   - 驗證安裝: `java -version`

2. **Apache Maven 3.6+**
   - 下載: https://maven.apache.org/download.cgi
   - 驗證安裝: `mvn -version`

3. **MySQL 8.0+**
   - 下載: https://dev.mysql.com/downloads/mysql/
   - 驗證安裝: `mysql --version`

4. **Git**
   - 下載: https://git-scm.com/downloads
   - 驗證安裝: `git --version`

---

## 📥 步驟 1: Clone 專案

```bash
git clone https://github.com/你的帳號/university-grade-system.git
cd university-grade-system
```

---

## 🗄️ 步驟 2: 建立資料庫

### 2.1 進入 MySQL

```bash
mysql -u root -p
```

### 2.2 執行資料庫腳本

```sql
-- 建立資料庫和資料表
source database/schema.sql

-- 插入測試資料
source database/sample-data.sql

-- 驗證建立成功
USE university_grades;
SHOW TABLES;
-- 應該看到 9 個資料表
```

或使用命令列:

```bash
mysql -u root -p < database/schema.sql
mysql -u root -p < database/sample-data.sql
```

---

## ⚙️ 步驟 3: 設定後端

### 3.1 複製配置檔案

```bash
cd backend
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

### 3.2 編輯配置檔案

開啟 `backend/src/main/resources/application.properties`

修改以下設定:

```properties
# 修改資料庫密碼
spring.datasource.password=你的MySQL密碼

# 如果 MySQL 不在 localhost,修改 URL
spring.datasource.url=jdbc:mysql://localhost:3306/university_grades?useSSL=false&serverTimezone=Asia/Taipei
```

### 3.3 建立上傳目錄

```bash
# 在 backend 目錄下執行
mkdir -p uploads/materials
mkdir -p uploads/submissions
```

---

## 🏃 步驟 4: 啟動後端

### 方法 A: 使用 Maven

```bash
cd backend
mvn clean spring-boot:run
```

### 方法 B: 打包成 JAR 執行

```bash
cd backend
mvn clean package
java -jar target/grade-system-1.0.0.jar
```

### 驗證啟動成功

看到以下訊息表示成功:

```
=================================
🎓 成績管理系統啟動成功!
📡 伺服器運行於: http://localhost:3000
=================================
```

測試 API:
```bash
curl http://localhost:3000/
```

---

## 🌐 步驟 5: 開啟前端

### 老師端

1. 開啟瀏覽器
2. 開啟檔案: `frontend/teacher/login.html`
3. 使用測試帳號登入:
   - 帳號: `T001`
   - 密碼: `pass123`

### 學生端

1. 開啟瀏覽器
2. 開啟檔案: `frontend/student/login.html`
3. 使用測試帳號登入:
   - 帳號: `S001`
   - 密碼: `pass123`

---

## ✅ 步驟 6: 功能測試

### 測試清單

- [ ] 老師登入
- [ ] 查看課程列表
- [ ] 選擇課程
- [ ] 新增公告
- [ ] 上傳教材
- [ ] 新增作業
- [ ] 輸入成績
- [ ] 學生登入
- [ ] 查看成績

---

## 🐛 常見問題排除

### 問題 1: 資料庫連線失敗

**錯誤訊息**: `Access denied for user 'root'@'localhost'`

**解決方法**:
1. 確認 MySQL 服務已啟動
2. 檢查 `application.properties` 中的密碼是否正確
3. 確認資料庫 `university_grades` 已建立

### 問題 2: Port 3000 已被占用

**錯誤訊息**: `Port 3000 is already in use`

**解決方法**:
- 修改 `application.properties` 中的 `server.port=3000` 為其他 port
- 或關閉占用 3000 port 的程式

### 問題 3: 檔案上傳失敗

**錯誤訊息**: `找不到檔案: uploads/materials/xxx`

**解決方法**:
```bash
cd backend
mkdir -p uploads/materials uploads/submissions
```

### 問題 4: CORS 錯誤

**錯誤訊息**: `Access to fetch at 'http://localhost:3000' ... has been blocked by CORS policy`

**解決方法**:
- 確認後端的 `@CrossOrigin(origins = "*")` 註解存在
- 重新啟動後端

### 問題 5: Maven 編譯失敗

**錯誤訊息**: `Failed to execute goal ... compilation failure`

**解決方法**:
```bash
mvn clean install -U
```

---

## 🔧 開發模式

### 啟用熱重載

使用 Spring Boot DevTools:

```xml
<!-- 在 pom.xml 中加入 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

### 查看 API 文檔

Swagger UI (可選):
```
http://localhost:3000/swagger-ui.html
```

---

## 📊 效能優化

### 資料庫索引

```sql
-- 為常用查詢欄位建立索引
CREATE INDEX idx_student_id ON enrollments(student_id);
CREATE INDEX idx_course_id ON enrollments(course_id);
CREATE INDEX idx_assignment_id ON assignment_submissions(assignment_id);
```

### 連線池設定

在 `application.properties` 加入:

```properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
```

---

## 🚀 部署到生產環境

### 1. 修改配置

```properties
# 關閉 SQL 顯示
spring.jpa.show-sql=false

# 設定正式的資料庫連線
spring.datasource.url=jdbc:mysql://production-host:3306/university_grades

# 修改 Log 等級
logging.level.root=WARN
```

### 2. 打包

```bash
mvn clean package -DskipTests
```

### 3. 使用 Docker (可選)

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/grade-system-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

---

## 📞 需要幫助?

- 📧 Email: your.email@example.com
- 💬 GitHub Issues: https://github.com/你的帳號/university-grade-system/issues
- 📖 Wiki: https://github.com/你的帳號/university-grade-system/wiki