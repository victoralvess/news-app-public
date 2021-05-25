package com.github.victoralvess.newz.domain.entities;

public class User {
    private String subject;

    public User(String subject) {
        this.subject = subject;
    }

    public boolean hasSubject() {
        return subject != null;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
