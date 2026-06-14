package model;

import lombok.Getter;

@Getter
public enum StatutMembre {

    // Etat possible

    ACTIF("L’utilisateur peut emprunter des livres normalement."),
    SUSPENDU("L’utilisateur est temporairement bloqué et ne peut pas emprunter."),
    INACTIF("L’utilisateur a un compte désactivé."),
    RETARDATAIRE("L’utilisateur a des emprunts non rendus et en retard.");

    // Attribut description

    private final String description;

    // Constructeur

    StatutMembre(final String description) {

        this.description = description;

    }

}
