package com.chat.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "chats")
public class Chat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, referencedColumnName = "user_id")
    private User customer;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "user_id")
    private User employee;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ChatStatus status;
    
    @Column(name = "subject")
    private String subject;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "closed_at")
    private LocalDateTime closedAt;
    
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("createdAt ASC")
    private List<Message> messages;
    
    // Constructors
    public Chat() {
        this.createdAt = LocalDateTime.now();
        this.status = ChatStatus.OPEN;
    }
    
    public Chat(User customer, String subject) {
        this();
        this.customer = customer;
        this.subject = subject;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getCustomer() {
        return customer;
    }
    
    public void setCustomer(User customer) {
        this.customer = customer;
    }
    
    public User getEmployee() {
        return employee;
    }
    
    public void setEmployee(User employee) {
        this.employee = employee;
    }
    
    public ChatStatus getStatus() {
        return status;
    }
    
    public void setStatus(ChatStatus status) {
        this.status = status;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public LocalDateTime getClosedAt() {
        return closedAt;
    }
    
    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }
    
    public List<Message> getMessages() {
        return messages;
    }
    
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", customer=" + (customer != null ? customer.getUsername() : null) +
                ", employee=" + (employee != null ? employee.getUsername() : null) +
                ", status=" + status +
                ", subject='" + subject + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

