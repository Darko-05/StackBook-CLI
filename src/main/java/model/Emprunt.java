package model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class Emprunt {

    // Attributs d'un emprunt

    @EqualsAndHashCode.Include
    private String id;
    private Livre livre;
    private Membre membre;
    private LocalDate dateEmprunt;
    private LocalDate dateRetourPrevu;
    private LocalDate dateRetourEffective;
    private EtatEmprunt etatEmprunt;
    private double penalite;
    private int nombreRenouvellement;

    // Méthodes utilitaires

    public void incrementerNombreRenouvellement() {

        nombreRenouvellement++;

    }

}