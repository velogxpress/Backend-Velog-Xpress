package com.velogexpress.entity;

public enum Status {
    EXPEDITE("Expédiée "),
    PENDING("En attente"),
    AVAILABLE("Disponible"),
    DELIVERED("Livrée"),
    READ("A/R"),
    UNREAD("U/R"),
    ACTIVE("Actif(ve)"),
    DISABLE("Inactif(ve)"),
    BLOCK("Bloqué"),
    CASH("Payé"),
    CREDIT("Due"),
    LOGOUT("N/A"),
    CURRENTORDER("N/L"),
    FINISHORDER("D/L"),
    NOVALIDATE("N/V"),
    VALIDATE("V/S");

    private String label;

    Status(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
