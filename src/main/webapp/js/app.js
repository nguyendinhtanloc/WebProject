// Global variables
let currentUser = null;
let currentChat = null;
let websocket = null;
let typingTimer = null;
let isTyping = false;

// Global variables
console.log('App.js loading... v20251013-5');
console.log('JavaScript file successfully loaded and parsed');

// Initialize app
document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM LOADED - Starting checkAuthStatus()');
    checkAuthStatus();
});

// Authentication functions
async function checkAuthStatus() {
    try {
        console.log('Checking auth status...');
        
        const params = new URLSearchParams();
        params.append('action', 'status');
        
        console.log('Sending request with action=status');
        console.log('Request body:', params.toString());
        
        const response = await fetch('/new_chat/auth', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: params.toString()
        });
        
        console.log('Response status:', response.status);
        
        if (response.ok) {
            const data = await response.json();
            console.log('Response data:', data);
            
            if (data.success) {
                currentUser = data.user;
                showMainApp();
                loadChats();
            } else {
                showAuthModal();
            }
        } else if (response.status === 401) {
            // User not authenticated - this is normal for first visit
            console.log('User not authenticated, showing auth modal');
            showAuthModal();
        } else {
            // Other error
            const errorText = await response.text();
            console.error('Auth check failed:', response.status, errorText);
            showToast('Lỗi kiểm tra đăng nhập: ' + errorText, 'error');
            showAuthModal();
        }
    } catch (error) {
        console.error('Auth check error:', error);
        showToast('Lỗi kết nối server', 'error');
        showAuthModal();
    }
}

function showAuthModal() {
    document.getElementById('authModal').style.display = 'block';
}

function hideAuthModal() {
    document.getElementById('authModal').style.display = 'none';
}

function switchTab(tabName) {
    // Remove active class from all tabs and forms
    document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));
    document.querySelectorAll('.auth-form').forEach(form => form.classList.remove('active'));
    
    // Add active class to selected tab and form
    document.querySelector(`[onclick="switchTab('${tabName}')"]`).classList.add('active');
    document.getElementById(`${tabName}Form`).classList.add('active');
}

async function handleLogin(event) {
    event.preventDefault();
    
    const username = document.getElementById('loginUsername').value;
    const password = document.getElementById('loginPassword').value;
    
    showLoading();
    
    try {
        const params = new URLSearchParams();
        params.append('action', 'login');
        params.append('username', username);
        params.append('password', password);
        
        const response = await fetch('/new_chat/auth', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: params.toString()
        });
        
        const data = await response.json();
        
        if (data.success) {
            currentUser = data.user;
            hideAuthModal();
            showMainApp();
            showToast('Đăng nhập thành công!', 'success');
            loadChats();
        } else {
            showToast(data.message, 'error');
        }
    } catch (error) {
        console.error('Login error:', error);
        showToast('Lỗi đăng nhập. Vui lòng thử lại.', 'error');
    } finally {
        hideLoading();
    }
}

async function handleRegister(event) {
    event.preventDefault();
    
    const username = document.getElementById('regUsername').value;
    const email = document.getElementById('regEmail').value;
    const fullName = document.getElementById('regFullName').value;
    const userType = document.getElementById('regUserType').value;
    const password = document.getElementById('regPassword').value;
    
    showLoading();
    
    try {
        const params = new URLSearchParams();
        params.append('action', 'register');
        params.append('username', username);
        params.append('email', email);
        params.append('fullName', fullName);
        params.append('userType', userType);
        params.append('password', password);
        
        const response = await fetch('/new_chat/auth', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: params.toString()
        });
        
        const data = await response.json();
        
        if (data.success) {
            showToast('Đăng ký thành công! Vui lòng đăng nhập.', 'success');
            switchTab('login');
            // Pre-fill login form
            document.getElementById('loginUsername').value = username;
        } else {
            showToast(data.message, 'error');
        }
    } catch (error) {
        console.error('Register error:', error);
        showToast('Lỗi đăng ký. Vui lòng thử lại.', 'error');
    } finally {
        hideLoading();
    }
}

async function logout() {
    showLoading();
    
    try {
        const params = new URLSearchParams();
        params.append('action', 'logout');
        
        const response = await fetch('/new_chat/auth', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: params.toString()
        });
        
        const data = await response.json();
        
        if (data.success) {
            currentUser = null;
            currentChat = null;
            closeWebSocket();
            document.getElementById('app').style.display = 'none';
            showAuthModal();
            showToast('Đăng xuất thành công!', 'success');
        }
    } catch (error) {
        console.error('Logout error:', error);
        showToast('Lỗi đăng xuất.', 'error');
    } finally {
        hideLoading();
    }
}

// Main app functions
function showMainApp() {
    document.getElementById('authModal').style.display = 'none';
    document.getElementById('app').style.display = 'flex';
    
    // Update user info in header
    document.getElementById('userFullName').textContent = currentUser.fullName;
    document.getElementById('userType').textContent = currentUser.userType;
    document.getElementById('userType').className = `user-type ${currentUser.userType.toLowerCase()}`;
    
    // Show/hide elements based on user type
    if (currentUser.userType === 'CUSTOMER') {
        document.getElementById('createChatBtn').style.display = 'inline-flex';
        document.getElementById('sidebarTitle').textContent = 'Cuộc trò chuyện của tôi';
    } else {
        document.getElementById('createChatBtn').style.display = 'none';
        document.getElementById('sidebarTitle').textContent = 'Cuộc trò chuyện';
        loadOpenChats();
    }
}

// Chat functions
async function loadChats() {
    try {
        const response = await fetch('/new_chat/api/chat/list');
        const data = await response.json();
        
        if (data.success) {
            renderChatList(data.chats);
        } else {
            showToast('Lỗi tải danh sách chat', 'error');
        }
    } catch (error) {
        console.error('Load chats error:', error);
        showToast('Lỗi tải danh sách chat', 'error');
    }
}

async function loadOpenChats() {
    if (currentUser.userType !== 'EMPLOYEE') return;
    
    try {
        const response = await fetch('/new_chat/api/chat/open');
        const data = await response.json();
        
        if (data.success) {
            document.getElementById('openChatsSection').style.display = 'block';
            renderOpenChatList(data.chats);
        }
    } catch (error) {
        console.error('Load open chats error:', error);
    }
}

function renderChatList(chats) {
    const chatList = document.getElementById('chatList');
    chatList.innerHTML = '';
    
    if (chats.length === 0) {
        chatList.innerHTML = '<p style="text-align: center; color: #666; padding: 20px;">Chưa có cuộc trò chuyện nào</p>';
        return;
    }
    
    chats.forEach(chat => {
        const chatItem = createChatItem(chat);
        chatList.appendChild(chatItem);
    });
}

function renderOpenChatList(chats) {
    const openChatList = document.getElementById('openChatList');
    openChatList.innerHTML = '';
    
    if (chats.length === 0) {
        openChatList.innerHTML = '<p style="text-align: center; color: #666; padding: 20px;">Không có chat chờ xử lý</p>';
        return;
    }
    
    chats.forEach(chat => {
        const chatItem = createChatItem(chat, true);
        openChatList.appendChild(chatItem);
    });
}

function createChatItem(chat, isOpenChat = false) {
    const chatItem = document.createElement('div');
    chatItem.className = 'chat-item';
    
    const statusClass = chat.status.toLowerCase().replace('_', '_');
    const statusText = {
        'open': 'Mở',
        'in_progress': 'Đang xử lý',
        'closed': 'Đã đóng'
    }[chat.status.toLowerCase()] || chat.status;
    
    const participants = currentUser.userType === 'CUSTOMER' 
        ? (chat.employee ? chat.employee.fullName : 'Chưa có nhân viên')
        : chat.customer.fullName;
    
    let assignButton = '';
    if (isOpenChat && chat.status === 'OPEN' && currentUser.userType === 'EMPLOYEE') {
        assignButton = `<button class="btn btn-sm btn-primary assign-btn" onclick="event.stopPropagation(); assignChat(${chat.id})">Nhận chat</button>`;
    }
    
    chatItem.innerHTML = `
        <div class="chat-item-header">
            <span class="chat-subject">${chat.subject}</span>
            <span class="chat-status ${statusClass}">${statusText}</span>
        </div>
        <div class="chat-participants">${participants}</div>
        <div class="chat-time">${formatDateTime(chat.updatedAt || chat.createdAt)}</div>
        ${assignButton}
    `;
    
    // Only add click event if it's not an open chat with assign button
    if (!(isOpenChat && chat.status === 'OPEN')) {
        chatItem.onclick = () => selectChat(chat);
    }
    
    if (isOpenChat && chat.status === 'OPEN') {
        chatItem.style.background = '#fff3cd';
        chatItem.style.border = '2px solid #ffc107';
    }
    
    return chatItem;
}

async function selectChat(chat) {
    currentChat = chat;
    
    // Update UI
    document.querySelectorAll('.chat-item').forEach(item => item.classList.remove('active'));
    event.currentTarget.classList.add('active');
    
    // Show chat container
    document.getElementById('noChatSelected').style.display = 'none';
    document.getElementById('chatContainer').style.display = 'flex';
    
    // Update chat header
    document.getElementById('chatTitle').textContent = chat.subject;
    document.getElementById('chatStatus').textContent = chat.status;
    document.getElementById('chatStatus').className = `chat-status ${chat.status.toLowerCase().replace('_', '_')}`;
    
    // Load messages
    await loadMessages(chat.id);
    
    // Connect WebSocket
    connectWebSocket(chat.id);
}

async function loadMessages(chatId) {
    try {
        const response = await fetch(`/new_chat/api/message/chat/${chatId}`);
        const data = await response.json();
        
        if (data.success) {
            renderMessages(data.messages);
            // Mark messages as read
            markMessagesAsRead(chatId);
        } else {
            showToast('Lỗi tải tin nhắn', 'error');
        }
    } catch (error) {
        console.error('Load messages error:', error);
        showToast('Lỗi tải tin nhắn', 'error');
    }
}

function renderMessages(messages) {
    const messagesArea = document.getElementById('messagesArea');
    messagesArea.innerHTML = '';
    
    messages.forEach(message => {
        const messageElement = createMessageElement(message);
        messagesArea.appendChild(messageElement);
    });
    
    // Scroll to bottom
    messagesArea.scrollTop = messagesArea.scrollHeight;
}

function createMessageElement(message) {
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${message.sender.id === currentUser.id ? 'sent' : 'received'}`;
    
    const avatar = message.sender.fullName.charAt(0).toUpperCase();
    const time = formatDateTime(message.createdAt);
    
    messageDiv.innerHTML = `
        <div class="message-avatar">${avatar}</div>
        <div class="message-content">
            <div class="message-header">
                <span class="message-sender">${message.sender.fullName}</span>
                <span class="message-time">${time}</span>
            </div>
            <div class="message-text">${message.content}</div>
        </div>
    `;
    
    return messageDiv;
}

// WebSocket functions
function connectWebSocket(chatId) {
    closeWebSocket();
    
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    const wsUrl = `${protocol}//${window.location.host}/new_chat/ws/chat/${chatId}/${currentUser.id}`;
    
    websocket = new WebSocket(wsUrl);
    
    websocket.onopen = function() {
        console.log('WebSocket connected');
    };
    
    websocket.onmessage = function(event) {
        const data = JSON.parse(event.data);
        handleWebSocketMessage(data);
    };
    
    websocket.onclose = function() {
        console.log('WebSocket disconnected');
    };
    
    websocket.onerror = function(error) {
        console.error('WebSocket error:', error);
    };
}

function closeWebSocket() {
    if (websocket) {
        websocket.close();
        websocket = null;
    }
}

function handleWebSocketMessage(data) {
    switch (data.type) {
        case 'connection':
            console.log('WebSocket connection confirmed:', data.message);
            break;
        case 'message':
            if (data.data) {
                addNewMessage(data.data);
            }
            break;
        case 'typing':
            handleTypingIndicator(data);
            break;
        case 'userStatus':
            handleUserStatus(data);
            break;
        case 'error':
            showToast(data.message, 'error');
            break;
        default:
            console.log('Unknown WebSocket message type:', data.type);
            break;
    }
}

function addNewMessage(message) {
    const messagesArea = document.getElementById('messagesArea');
    const messageElement = createMessageElement(message);
    messagesArea.appendChild(messageElement);
    
    // Scroll to bottom
    messagesArea.scrollTop = messagesArea.scrollHeight;
    
    // Mark as read if it's not from current user
    if (message.sender.id !== currentUser.id) {
        markMessagesAsRead(currentChat.id);
    }
}

function handleTypingIndicator(data) {
    const typingIndicator = document.getElementById('typingIndicator');
    const typingText = document.getElementById('typingText');
    
    if (data.isTyping && data.userId !== currentUser.id) {
        typingText.textContent = 'Đang nhập...';
        typingIndicator.style.display = 'block';
    } else {
        typingIndicator.style.display = 'none';
    }
}

function handleUserStatus(data) {
    // Handle user online/offline status
    console.log('User status:', data);
}

// Message functions
async function sendMessage() {
    const messageInput = document.getElementById('messageInput');
    const content = messageInput.value.trim();
    
    if (!content || !currentChat || !websocket || websocket.readyState !== WebSocket.OPEN) {
        if (!websocket || websocket.readyState !== WebSocket.OPEN) {
            showToast('Không có kết nối WebSocket. Đang thử kết nối lại...', 'warning');
            connectWebSocket(currentChat.id);
        }
        return;
    }
    
    try {
        // Send message via WebSocket
        const messageData = {
            type: 'message',
            content: content
        };
        
        websocket.send(JSON.stringify(messageData));
        messageInput.value = '';
        
    } catch (error) {
        console.error('Send message error:', error);
        showToast('Lỗi gửi tin nhắn', 'error');
        
        // Fallback to HTTP if WebSocket fails
        sendMessageHTTP(content);
    }
}

async function sendMessageHTTP(content) {
    showLoading();
    
    try {
        const params = new URLSearchParams();
        params.append('chatId', currentChat.id);
        params.append('content', content);
        params.append('messageType', 'TEXT');
        
        const response = await fetch('/new_chat/api/message/send', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: params.toString()
        });
        
        const data = await response.json();
        
        if (data.success) {
            document.getElementById('messageInput').value = '';
            // If WebSocket is not working, add message manually
            if (!websocket || websocket.readyState !== WebSocket.OPEN) {
                addNewMessage(data.messageData);
            }
        } else {
            showToast(data.message, 'error');
        }
    } catch (error) {
        console.error('Send message HTTP error:', error);
        showToast('Lỗi gửi tin nhắn', 'error');
    } finally {
        hideLoading();
    }
}

function handleMessageKeyPress(event) {
    if (event.key === 'Enter') {
        sendMessage();
    } else {
        handleTyping();
    }
}

function handleTyping() {
    if (!websocket || !currentChat) return;
    
    // Send typing indicator
    const typingData = {
        type: 'typing',
        isTyping: true
    };
    
    websocket.send(JSON.stringify(typingData));
    
    // Clear previous timer
    if (typingTimer) {
        clearTimeout(typingTimer);
    }
    
    // Set timer to stop typing indicator
    typingTimer = setTimeout(() => {
        const stopTypingData = {
            type: 'typing',
            isTyping: false
        };
        websocket.send(JSON.stringify(stopTypingData));
    }, 1000);
}

async function markMessagesAsRead(chatId) {
    try {
        const params = new URLSearchParams();
        params.append('chatId', chatId);
        
        await fetch('/new_chat/api/message/mark-read', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: params.toString()
        });
    } catch (error) {
        console.error('Mark messages as read error:', error);
    }
}

// Chat management functions
function showCreateChatModal() {
    document.getElementById('createChatModal').style.display = 'block';
}

function hideCreateChatModal() {
    const modal = document.getElementById('createChatModal');
    if (modal) {
        modal.style.display = 'none';
    }
    
    const subjectElement = document.getElementById('chatSubject');
    if (subjectElement) {
        subjectElement.value = '';
    }
}

async function handleCreateChat(event) {
    event.preventDefault();
    
    const subjectElement = document.getElementById('chatSubject');
    if (!subjectElement) {
        console.error('Element chatSubject not found');
        showToast('Lỗi: Không tìm thấy form tạo chat', 'error');
        return;
    }
    
    const subject = subjectElement.value.trim();
    
    if (!subject) {
        showToast('Vui lòng nhập chủ đề cuộc trò chuyện', 'error');
        return;
    }
    
    showLoading();
    
    try {
        const params = new URLSearchParams();
        params.append('subject', subject);
        
        const response = await fetch('/new_chat/api/chat/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: params.toString()
        });
        
        const data = await response.json();
        
        if (data.success) {
            hideCreateChatModal();
            showToast('Tạo chat thành công!', 'success');
            loadChats();
            selectChat(data.chat);
        } else {
            showToast(data.message, 'error');
        }
    } catch (error) {
        console.error('Create chat error:', error);
        showToast('Lỗi tạo chat', 'error');
    } finally {
        hideLoading();
    }
}

async function closeChat() {
    if (!currentChat) return;
    
    if (!confirm('Bạn có chắc chắn muốn đóng cuộc trò chuyện này?')) {
        return;
    }
    
    showLoading();
    
    try {
        const params = new URLSearchParams();
        params.append('chatId', currentChat.id);
        
        const response = await fetch('/new_chat/api/chat/close', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: params.toString()
        });
        
        const data = await response.json();
        
        if (data.success) {
            showToast('Đóng chat thành công!', 'success');
            closeWebSocket();
            loadChats();
            showNoChatSelected();
        } else {
            showToast(data.message, 'error');
        }
    } catch (error) {
        console.error('Close chat error:', error);
        showToast('Lỗi đóng chat', 'error');
    } finally {
        hideLoading();
    }
}

async function assignChat(chatId) {
    if (currentUser.userType !== 'EMPLOYEE') return;
    
    showLoading();
    
    try {
        const params = new URLSearchParams();
        params.append('chatId', chatId);
        
        const response = await fetch('/new_chat/api/chat/assign', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: params.toString()
        });
        
        const data = await response.json();
        
        if (data.success) {
            showToast('Nhận chat thành công!', 'success');
            loadOpenChats();
            loadChats();
            selectChat(data.chat);
        } else {
            showToast(data.message, 'error');
        }
    } catch (error) {
        console.error('Assign chat error:', error);
        showToast('Lỗi nhận chat', 'error');
    } finally {
        hideLoading();
    }
}

function showNoChatSelected() {
    document.getElementById('chatContainer').style.display = 'none';
    document.getElementById('noChatSelected').style.display = 'flex';
    currentChat = null;
    closeWebSocket();
}

// Utility functions
function formatDateTime(dateString) {
    const date = new Date(dateString);
    const now = new Date();
    const diff = now - date;
    
    if (diff < 60000) { // Less than 1 minute
        return 'Vừa xong';
    } else if (diff < 3600000) { // Less than 1 hour
        return Math.floor(diff / 60000) + ' phút trước';
    } else if (diff < 86400000) { // Less than 1 day
        return Math.floor(diff / 3600000) + ' giờ trước';
    } else {
        return date.toLocaleDateString('vi-VN');
    }
}

function showLoading() {
    document.getElementById('loadingOverlay').style.display = 'flex';
}

function hideLoading() {
    document.getElementById('loadingOverlay').style.display = 'none';
}

function showToast(message, type = 'info') {
    const toastContainer = document.getElementById('toastContainer');
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    
    const icon = {
        success: '✓',
        error: '✕',
        warning: '⚠',
        info: 'ℹ'
    }[type] || 'ℹ';
    
    toast.innerHTML = `
        <span>${icon}</span>
        <span>${message}</span>
    `;
    
    toastContainer.appendChild(toast);
    
    // Auto remove after 3 seconds
    setTimeout(() => {
        toast.remove();
    }, 3000);
}



