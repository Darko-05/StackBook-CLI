package model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class Reservation {

    // Attributs d'une reservation

    @EqualsAndHashCode.Include
    private String idReservation;
    private Livre livre;
    private Membre membre;
    private LocalDate dateReservation;
    private LocalDate dateExpiration;
    private EtatReservation etatReservation;

    // Méthode utilitaire

    public void changerEtatReservation(EtatReservation etat) {

        this.setEtatReservation(etat);

    }

}
