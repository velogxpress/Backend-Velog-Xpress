package com.velogexpress.entity;

public enum Type {
    DIRECT("Directe"),
    INDIRECT("Indirecte"),
    INTERMEDIATE("Intermédiaire");

    private String label;

    Type(String label) {
        this.label = label;
    }
}
