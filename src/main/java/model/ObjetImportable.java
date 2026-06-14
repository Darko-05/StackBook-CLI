package model;

import lombok.Getter;

public enum ObjetImportable {

    // Etat possibme

    MAISON_EDITION("maisons d'édition"),
    AUTEUR("auteurs"),
    LIVRE("livres"),
    MEMBRE("membres");

    // Attributs

    @Getter private final String nom;

    // Constructeur

    ObjetImportable(String nom) {

        this.nom = nom;

    }

}
