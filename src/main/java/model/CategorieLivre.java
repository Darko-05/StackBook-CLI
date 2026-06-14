package model;

import lombok.Getter;

@Getter
public enum CategorieLivre {

    // Etat possible

    ROMAN("Œuvre de fiction narrative."),
    POESIE("Texte littéraire en vers, axé sur le rythme et les images."),
    THEATRE("Texte destiné à être joué sur scène."),
    ESSAI("Texte d’analyse ou de réflexion personnelle."),
    DOCUMENTAIRE("Œuvre informative basée sur des faits réels."),
    SCIENCE_FICTION("Fiction explorant des technologies ou mondes imaginaires."),
    FANTASY("Fiction avec des éléments magiques ou surnaturels."),
    POLAR("Roman policier ou thriller."),
    BIOGRAPHIE("Récit de la vie d’une personne réelle."),
    JEUNESSE("Livre destiné aux enfants ou adolescents."),
    UNIVERSITAIRE("Livre académique ou scolaire pour études supérieures."),
    BD("Bande dessinée, récit illustré."),
    MANUEL("Livre pratique ou guide d’apprentissage.");

    // Attribut description

    private final String description;

    // Constructeur

    CategorieLivre(final String description) {

        this.description = description;

    }

}