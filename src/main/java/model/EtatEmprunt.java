package model;

import lombok.Getter;

@Getter
public enum EtatEmprunt {

    // Etat possible

    EN_COURS("L’emprunt ou la réservation est actuellement actif."),
    RETOURNE("L’objet ou le livre a été rendu."),
    EN_RETARD("L’emprunt n’a pas été rendu à temps.");

    // Attribut description

    private final String description;

    // Constructeur

    EtatEmprunt(final String description) {

        this.description = description;

    }

}
