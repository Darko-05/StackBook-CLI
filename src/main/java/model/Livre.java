package model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Year;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class Livre {

    // Attributs d'un livre

    @EqualsAndHashCode.Include
    private String id;
    private String isbn;
    private String titre;
    private Auteur auteur;
    private LocalDate dateAjout;
    private MaisonEdition maisonEdition;
    private Year anneePublication;
    private CategorieLivre categorie;
    private EtatLivre etatDuLivre;
    private long nombreExemplaire;
    private long nombreExemplaireDisponible;
    private long compteurEmpruntTotaux;

    // Méthodes utilitaire

    public void incrementerNombreExemplaireDisponible() {

        nombreExemplaireDisponible++;

    }

    public void decrementerNombreExemplaireDisponible() {

        nombreExemplaireDisponible--;

    }

    public void incrementerCompteurEmpruntsTotaux() {

        compteurEmpruntTotaux++;

    }

    public boolean estDisponible() {

        return nombreExemplaireDisponible > 0;

    }

}