const API_BASE = 'http://localhost:3000';
let currentTeacher = null;
let currentCourse = null;

// 初始化
window.onload = function() {
    // 檢查登入狀態
    const teacher = localStorage.getItem('currentTeacher');
    if (!teacher) {
        window.location.href = 'login.html';
        return;
    }
    
    currentTeacher = JSON.parse(teacher);
    document.getElementById('teacherName').textContent = currentTeacher.name + ' 老師';
    
    loadCourses();
};

// ========== 課程相關 ==========
function loadCourses() {
    fetch(`${API_BASE}/teacher/${currentTeacher.teacher_id}/courses`)
        .then(r => r.json())
        .then(courses => {
            const html = courses.map(c => `
                <div class="course-item" onclick="selectCourse(${c.courseId}, '${c.courseName}')">
                    <h4>${c.courseName}</h4>
                    <p>課程編號: ${c.courseId}</p>
                </div>
            `).join('');
            document.getElementById('courseList').innerHTML = html || '<div class="empty-state">尚無課程</div>';
        });
}

function selectCourse(courseId, courseName) {
    currentCourse = { courseId, courseName };
    document.getElementById('courseTitle').textContent = courseName;
    
    // 更新選中狀態
    document.querySelectorAll('.course-item').forEach(item => item.classList.remove('active'));
    event.target.closest('.course-item').classList.add('active');
    
    // 載入各項資料
    loadStudents();
    loadAnnouncements();
    loadMaterials();
    loadAssignments();
    loadGrades();
}

function switchTab(tabName) {
    // 切換標籤
    document.querySelectorAll('.tab').forEach(tab => tab.classList.remove('active'));
    event.target.classList.add('active');
    
    // 切換內容
    document.querySelectorAll('.tab-content').forEach(content => content.classList.remove('active'));
    document.getElementById('tab-' + tabName).classList.add('active');
}

function logout() {
    localStorage.removeItem('currentTeacher');
    window.location.href = 'login.html';
}

// ========== 學生名單 ==========
function loadStudents() {
    if (!currentCourse) return;
    
    fetch(`${API_BASE}/courses/${currentCourse.courseId}/students`)
        .then(r => r.json())
        .then(students => {
            const html = students.map(s => `
                <tr>
                    <td>${s.student_id}</td>
                    <td>${s.name}</td>
                    <td><button class="btn btn-danger btn-small">移除</button></td>
                </tr>
            `).join('');
            document.getElementById('studentList').innerHTML = html || '<tr><td colspan="3" class="empty-state">尚無學生</td></tr>';
        });
}

function showAddStudentModal() {
    // 載入所有學生
    fetch(`${API_BASE}/students`)
        .then(r => r.json())
        .then(students => {
            const options = students.map(s => 
                `<option value="${s.studentId}">${s.studentId} - ${s.name}</option>`
            ).join('');
            document.getElementById('studentSelect').innerHTML = options;
            document.getElementById('addStudentModal').classList.add('active');
        });
}

function addStudentToCourse() {
    const studentId = document.getElementById('studentSelect').value;
    
    fetch(`${API_BASE}/api/courses/${currentCourse.courseId}/students`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ studentId })
    })
    .then(r => r.json())
    .then(() => {
        alert('學生加入成功!');
        closeModal('addStudentModal');
        loadStudents();
    })
    .catch(err => alert('加入失敗: ' + err));
}

// ========== 課程公告 ==========
function loadAnnouncements() {
    if (!currentCourse) return;
    
    fetch(`${API_BASE}/api/courses/${currentCourse.courseId}/announcements`)
        .then(r => r.json())
        .then(announcements => {
            const html = announcements.map(a => `
                <div class="item">
                    <div class="item-header">
                        <div class="item-title">${a.title}</div>
                        <div class="item-actions">
                            <button class="btn btn-small" onclick="editAnnouncement(${a.id})">編輯</button>
                            <button class="btn btn-danger btn-small" onclick="deleteAnnouncement(${a.id})">刪除</button>
                        </div>
                    </div>
                    <p>${a.content}</p>
                    <div class="item-meta">發布於 ${formatDateTime(a.createdAt)}</div>
                </div>
            `).join('');
            document.getElementById('announcementList').innerHTML = html || '<div class="empty-state">尚無公告</div>';
        });
}

function showAnnouncementModal() {
    document.getElementById('announcementTitle').value = '';
    document.getElementById('announcementContent').value = '';
    document.getElementById('announcementModal').classList.add('active');
}

function saveAnnouncement() {
    const title = document.getElementById('announcementTitle').value;
    const content = document.getElementById('announcementContent').value;
    
    fetch(`${API_BASE}/api/courses/${currentCourse.courseId}/announcements`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title, content })
    })
    .then(r => r.json())
    .then(() => {
        alert('公告發布成功!');
        closeModal('announcementModal');
        loadAnnouncements();
    });
}

function deleteAnnouncement(id) {
    if (!confirm('確定要刪除此公告?')) return;
    
    fetch(`${API_BASE}/api/courses/announcements/${id}`, { method: 'DELETE' })
        .then(() => {
            alert('刪除成功!');
            loadAnnouncements();
        });
}

// ========== 課程教材 ==========
function loadMaterials() {
    if (!currentCourse) return;
    
    fetch(`${API_BASE}/api/courses/${currentCourse.courseId}/materials`)
        .then(r => r.json())
        .then(materials => {
            const html = materials.map(m => `
                <div class="item">
                    <div class="item-header">
                        <div class="item-title">📄 ${m.title}</div>
                        <div class="item-actions">
                            <a href="${API_BASE}/api/courses/materials/${m.id}/download" class="btn btn-small btn-success">下載</a>
                            <button class="btn btn-danger btn-small" onclick="deleteMaterial(${m.id})">刪除</button>
                        </div>
                    </div>
                    <p>${m.description}</p>
                    <div class="item-meta">檔案: ${m.fileName} (${formatFileSize(m.fileSize)}) | 上傳於 ${formatDateTime(m.uploadedAt)}</div>
                </div>
            `).join('');
            document.getElementById('materialList').innerHTML = html || '<div class="empty-state">尚無教材</div>';
        });
}

function showMaterialModal() {
    document.getElementById('materialTitle').value = '';
    document.getElementById('materialDescription').value = '';
    document.getElementById('materialFile').value = '';
    document.getElementById('materialModal').classList.add('active');
}

function uploadMaterial() {
    const title = document.getElementById('materialTitle').value;
    const description = document.getElementById('materialDescription').value;
    const file = document.getElementById('materialFile').files[0];
    
    if (!file) {
        alert('請選擇檔案');
        return;
    }
    
    const formData = new FormData();
    formData.append('title', title);
    formData.append('description', description);
    formData.append('file', file);
    
    fetch(`${API_BASE}/api/courses/${currentCourse.courseId}/materials`, {
        method: 'POST',
        body: formData
    })
    .then(r => r.json())
    .then(() => {
        alert('教材上傳成功!');
        closeModal('materialModal');
        loadMaterials();
    });
}

function deleteMaterial(id) {
    if (!confirm('確定要刪除此教材?')) return;
    
    fetch(`${API_BASE}/api/courses/materials/${id}`, { method: 'DELETE' })
        .then(() => {
            alert('刪除成功!');
            loadMaterials();
        });
}

// ========== 課程作業 ==========
function loadAssignments() {
    if (!currentCourse) return;
    
    fetch(`${API_BASE}/api/courses/${currentCourse.courseId}/assignments`)
        .then(r => r.json())
        .then(assignments => {
            const html = assignments.map(a => `
                <div class="item">
                    <div class="item-header">
                        <div class="item-title">✏️ ${a.title}</div>
                        <div class="item-actions">
                            <button class="btn btn-small" onclick="viewSubmissions(${a.id})">查看繳交 (${a.submissionCount})</button>
                            <button class="btn btn-danger btn-small" onclick="deleteAssignment(${a.id})">刪除</button>
                        </div>
                    </div>
                    <p>${a.description}</p>
                    <div class="item-meta">截止日期: ${formatDateTime(a.dueDate)}</div>
                </div>
            `).join('');
            document.getElementById('assignmentList').innerHTML = html || '<div class="empty-state">尚無作業</div>';
        });
}

function showAssignmentModal() {
    document.getElementById('assignmentTitle').value = '';
    document.getElementById('assignmentDescription').value = '';
    document.getElementById('assignmentDueDate').value = '';
    document.getElementById('assignmentModal').classList.add('active');
}

function saveAssignment() {
    const title = document.getElementById('assignmentTitle').value;
    const description = document.getElementById('assignmentDescription').value;
    const dueDate = document.getElementById('assignmentDueDate').value;
    
    fetch(`${API_BASE}/api/courses/${currentCourse.courseId}/assignments`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title, description, dueDate })
    })
    .then(r => r.json())
    .then(() => {
        alert('作業建立成功!');
        closeModal('assignmentModal');
        loadAssignments();
    });
}

function deleteAssignment(id) {
    if (!confirm('確定要刪除此作業?')) return;
    
    fetch(`${API_BASE}/api/courses/assignments/${id}`, { method: 'DELETE' })
        .then(() => {
            alert('刪除成功!');
            loadAssignments();
        });
}

function viewSubmissions(assignmentId) {
    // TODO: 顯示學生提交列表
    alert('查看作業繳交功能 (待實作)');
}

// ========== 成績管理 ==========
function loadGrades() {
    if (!currentCourse) return;
    
    fetch(`${API_BASE}/courses/${currentCourse.courseId}/students`)
        .then(r => r.json())
        .then(students => {
            const html = students.map(s => `
                <tr>
                    <td>${s.student_id}</td>
                    <td>${s.name}</td>
                    <td>
                        <input type="number" class="score-input" id="score_${s.student_id}" 
                               value="${s.score || ''}" min="0" max="100" style="width:80px; padding:5px;">
                    </td>
                    <td>
                        <button class="btn btn-success btn-small" onclick="saveScore('${s.student_id}')">儲存</button>
                    </td>
                </tr>
            `).join('');
            document.getElementById('gradeList').innerHTML = html || '<tr><td colspan="4" class="empty-state">尚無學生</td></tr>';
        });
}

function saveScore(studentId) {
    const score = document.getElementById('score_' + studentId).value;
    
    fetch(`${API_BASE}/grades`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            student_id: studentId,
            course_id: currentCourse.courseId,
            score: parseInt(score)
        })
    })
    .then(r => r.json())
    .then(() => alert('成績儲存成功!'));
}

// ========== 工具函數 ==========
function closeModal(modalId) {
    document.getElementById(modalId).classList.remove('active');
}

function formatDateTime(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleString('zh-TW');
}

function formatFileSize(bytes) {
    if (!bytes) return '0 B';
    const k = 1024;
    const sizes = ['B', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i];
}