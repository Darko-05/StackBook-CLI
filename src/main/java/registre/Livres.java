package registre;

import exception.LivreNonTrouveException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import model.*;

import java.util.LinkedHashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Livres {

    // Instance unique

    private static final Livres INSTANCE = new Livres();

    // Stock initial

    public final static long STOCK_INITIAL = 0;

    // Attribut registre

    private final Map<String, Livre> livres = new LinkedHashMap<>();

    // Getters

    public static Livres getInstance() {

        return INSTANCE;

    }

    public Map<String, Livre> getLivres() {

        return new BookRecord(livres).livres();

    }

    // Méthode metier

    public void ajouterLivre(@NonNull Livre l) {

        livres.put(l.getId(), l);

    }

    public void retirerLivre(@NonNull Livre l) {

        livres.remove(l.getId());

    }

    public Livre findById(String id) throws LivreNonTrouveException {

        // Retourne le livre ou lance "LivreNonTrouveException"

        return livres.values().stream()
        .filter(valeur -> valeur.getId().equals(id))
        .findAny()
        .orElseThrow(LivreNonTrouveException::new);

    }

    public boolean contientLivre(Livre l) {

        // Verifie si l'id du livre est parmis les clés des livres du système

        return livres.keySet().stream()
        .anyMatch(cle -> cle.equals(l.getId()));

    }

}
