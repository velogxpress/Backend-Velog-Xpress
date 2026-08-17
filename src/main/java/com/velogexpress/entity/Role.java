package com.velogexpress.entity;

public enum Role {
    CLIENT("Client"),
    AGENT("Agent"),
    ADMINISTRATOR("Admin");

    private String label;

    Role(String label) {
        this.label = label;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
}
