package registre;

import model.Auteur;

import java.util.List;

public record AuthorRecord(List<Auteur> auteurs) {

    public AuthorRecord {

        auteurs = List.copyOf(auteurs);

    }

}
