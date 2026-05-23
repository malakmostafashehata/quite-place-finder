package org.example.quiet_place.service;

import org.example.quiet_place.model.User;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.time.LocalDateTime;

@Component
@SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserSession  implements Serializable {

    private static final long serialVersionUID = 1L;

    private User currentUser;
    private Long userId;
    private LocalDateTime sessionCreatedAt;
    private LocalDateTime lastActivityAt;

    public void login(User user) {
        this.currentUser = user;
        this.userId = user.getId();
        this.sessionCreatedAt = LocalDateTime.now();
        this.lastActivityAt = LocalDateTime.now();
    }

    public void logout() {
        this.currentUser = null;
        this.userId = null;
        this.sessionCreatedAt = null;
        this.lastActivityAt = null;
    }

    public void refreshActivity() {
        this.lastActivityAt = LocalDateTime.now();
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDateTime getSessionCreatedAt() {
        return sessionCreatedAt;
    }

    public LocalDateTime getLastActivityAt() {
        return lastActivityAt;
    }
}