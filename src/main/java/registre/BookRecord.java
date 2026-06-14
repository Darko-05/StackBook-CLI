package registre;

import model.Livre;

import java.util.Map;

record BookRecord(Map<String, Livre> livres) {

    BookRecord {

        livres = Map.copyOf(livres);

    }

}
