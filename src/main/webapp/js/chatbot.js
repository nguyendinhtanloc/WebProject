// Chatbot JavaScript
class Chatbot {
    constructor() {
        this.isOpen = false;
        this.isTyping = false;
        this.init();
    }

    init() {
        this.createChatbotHTML();
        this.bindEvents();
        this.addWelcomeMessage();
    }

    createChatbotHTML() {
        const chatbotHTML = `
            <div class="chatbot-container">
                <button class="chatbot-toggle" id="chatbot-toggle">
                    <i class="fas fa-comments"></i>
                </button>
                
                <div class="chatbot-window" id="chatbot-window">
                    <div class="chatbot-header">
                        <h3 class="chatbot-title">
                            <i class="fas fa-robot"></i>
                            Trợ lý BusBooking
                        </h3>
                        <button class="chatbot-close" id="chatbot-close">
                            <i class="fas fa-times"></i>
                        </button>
                    </div>
                    
                    <div class="chatbot-messages" id="chatbot-messages">
                        <!-- Messages will be added here -->
                    </div>
                    
                    <div class="chatbot-input">
                        <form class="chatbot-form" id="chatbot-form">
                            <input 
                                type="text" 
                                class="chatbot-input-field" 
                                id="chatbot-input" 
                                placeholder="Nhập câu hỏi của bạn..."
                                autocomplete="off"
                            >
                            <button type="submit" class="chatbot-send" id="chatbot-send">
                                <i class="fas fa-paper-plane"></i>
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        `;
        
        document.body.insertAdjacentHTML('beforeend', chatbotHTML);
    }

    bindEvents() {
        const toggle = document.getElementById('chatbot-toggle');
        const close = document.getElementById('chatbot-close');
        const form = document.getElementById('chatbot-form');
        const input = document.getElementById('chatbot-input');

        toggle.addEventListener('click', () => this.toggleChat());
        close.addEventListener('click', () => this.closeChat());
        form.addEventListener('submit', (e) => this.handleSubmit(e));
        
        // Close chatbot when clicking outside
        document.addEventListener('click', (e) => {
            const chatbotContainer = document.querySelector('.chatbot-container');
            if (!chatbotContainer.contains(e.target) && this.isOpen) {
                this.closeChat();
            }
        });
    }

    toggleChat() {
        if (this.isOpen) {
            this.closeChat();
        } else {
            this.openChat();
        }
    }

    openChat() {
        const window = document.getElementById('chatbot-window');
        const toggle = document.getElementById('chatbot-toggle');
        
        window.classList.add('show');
        toggle.innerHTML = '<i class="fas fa-times"></i>';
        this.isOpen = true;
        
        // Focus input
        setTimeout(() => {
            document.getElementById('chatbot-input').focus();
        }, 300);
    }

    closeChat() {
        const window = document.getElementById('chatbot-window');
        const toggle = document.getElementById('chatbot-toggle');
        
        window.classList.remove('show');
        toggle.innerHTML = '<i class="fas fa-comments"></i>';
        this.isOpen = false;
    }

    addWelcomeMessage() {
        const welcomeHTML = `
            <div class="welcome-message">
                <i class="fas fa-robot"></i>
                <p>Xin chào! Tôi là trợ lý AI của BusBooking.<br>
                Hãy hỏi tôi về dịch vụ đặt vé xe buýt!</p>
            </div>
        `;
        
        const messagesContainer = document.getElementById('chatbot-messages');
        messagesContainer.innerHTML = welcomeHTML;
    }

    async handleSubmit(e) {
        e.preventDefault();
        
        if (this.isTyping) return;
        
        const input = document.getElementById('chatbot-input');
        const message = input.value.trim();
        
        if (!message) return;
        
        // Add user message
        this.addMessage(message, 'user');
        input.value = '';
        
        // Show typing indicator
        this.showTypingIndicator();
        
        try {
            // Send to backend
            const response = await this.sendToAPI(message);
            this.hideTypingIndicator();
            
            if (response.success) {
                this.addMessage(response.message, 'bot');
            } else {
                this.addMessage(response.error || 'Có lỗi xảy ra, vui lòng thử lại.', 'bot');
            }
        } catch (error) {
            console.error('Chatbot error:', error);
            this.hideTypingIndicator();
            this.addMessage('Xin lỗi, tôi không thể xử lý câu hỏi ngay bây giờ. Vui lòng thử lại sau.', 'bot');
        }
    }

    async sendToAPI(message) {
        const response = await fetch('/Web-project/api/chatbot', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `message=${encodeURIComponent(message)}`
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        return await response.json();
    }

    addMessage(text, sender) {
        const messagesContainer = document.getElementById('chatbot-messages');
        
        // Remove welcome message if exists
        const welcomeMessage = messagesContainer.querySelector('.welcome-message');
        if (welcomeMessage) {
            welcomeMessage.remove();
        }
        
        const messageHTML = `
            <div class="message message-${sender}">
                <div class="message-bubble">
                    ${this.formatMessage(text)}
                </div>
            </div>
        `;
        
        messagesContainer.insertAdjacentHTML('beforeend', messageHTML);
        this.scrollToBottom();
    }

    formatMessage(text) {
        // Basic text formatting
        return text
            .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
            .replace(/\*(.*?)\*/g, '<em>$1</em>')
            .replace(/\n/g, '<br>');
    }

    showTypingIndicator() {
        const messagesContainer = document.getElementById('chatbot-messages');
        const typingHTML = `
            <div class="message message-bot">
                <div class="typing-indicator" id="typing-indicator">
                    <div class="typing-dots">
                        <div class="typing-dot"></div>
                        <div class="typing-dot"></div>
                        <div class="typing-dot"></div>
                    </div>
                </div>
            </div>
        `;
        
        messagesContainer.insertAdjacentHTML('beforeend', typingHTML);
        document.getElementById('typing-indicator').style.display = 'block';
        this.isTyping = true;
        this.scrollToBottom();
    }

    hideTypingIndicator() {
        const typingIndicator = document.getElementById('typing-indicator');
        if (typingIndicator) {
            typingIndicator.closest('.message').remove();
        }
        this.isTyping = false;
    }

    scrollToBottom() {
        const messagesContainer = document.getElementById('chatbot-messages');
        setTimeout(() => {
            messagesContainer.scrollTop = messagesContainer.scrollHeight;
        }, 100);
    }
}

// Initialize chatbot when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    new Chatbot();
});

// Handle page visibility change to maintain chat state
document.addEventListener('visibilitychange', function() {
    if (document.hidden) {
        // Page is hidden, could save chat state here
    } else {
        // Page is visible again
    }
});