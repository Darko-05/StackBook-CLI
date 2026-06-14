package model;

import lombok.Getter;
import lombok.NonNull;

@Getter
public enum EtatLivre {

    // Etat Livre

    DISPONIBLE("Le livre est prêt à être emprunté."),
    EN_RETARD("Le livre n’a pas été rendu à temps par l’emprunteur."),
    HORS_SERVICE("Le livre n’est plus utilisable (abîmé, perdu ou retiré).");

    // Attribut description

    private final String description;

    // Constructeur

    EtatLivre(final String description) {

        this.description = description;

    }

    // Format de chaine

    @Override
    public @NonNull String toString() {

        return switch (this) {

            case DISPONIBLE -> "Disponible";

            case EN_RETARD -> "En retard";

            case HORS_SERVICE -> "Hors service";

        };

    }

}
