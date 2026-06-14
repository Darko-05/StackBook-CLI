package model;

import lombok.Getter;

@Getter
public enum EtatReservation {

    // Etat possible

    EN_COURS(1, "La réservation a été faite mais n’est pas encore confirmée."),
    VALIDE(2, "La réservation est confirmée et active."),
    EXPIRE(3, "La réservation n’a pas été utilisée dans le délai prévu et est automatiquement terminée"),
    ANNULE(4, "La réservation a été annulée par l’utilisateur ou le système.");

    //Attribut description

    private final String description;
    private final int priorite;

    // Constructeur

    EtatReservation(final int priorite, final String description) {

        this.description = description;
        this.priorite = priorite;

    }

}
