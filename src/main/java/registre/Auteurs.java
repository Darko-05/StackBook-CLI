package registre;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import model.Auteur;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Auteurs {

    // Instance unique

    private static final Auteurs INSTANCE = new Auteurs();

    // Attribut registre

    private final List<Auteur> auteurs = new ArrayList<>();

    // Getters

    public static Auteurs getInstance() {

        return INSTANCE;

    }

    public List<Auteur> getAuteurs() {

        return new AuthorRecord(auteurs).auteurs();

    }

    // Méthodes utilitaire

    public void ajouterAuteur(@NonNull Auteur a) {

        auteurs.add(a);

    }

    public void supprimerAuteur(Auteur a) {

        auteurs.remove(a);

    }

}
