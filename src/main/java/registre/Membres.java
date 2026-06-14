package registre;

import exception.MembreNonTrouveException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import model.Membre;

import java.util.LinkedHashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Membres {

    // Instance unique

    private static final Membres INSTANCE = new Membres();

    // Constante Numero Membre

    private static long NUMERO_MEMBRE = 0;

    // Attribut registre

    private final Map<String, Membre> membres = new LinkedHashMap<>();

    // Getters

    public static Membres getInstance() {

        return INSTANCE;

    }

    public Map<String, Membre> getMembres() {

        return new MemberRecord(membres).membres();

    }

    // Ajouter un membre

    public void ajouterMembre(@NonNull Membre m) {

        // Enregistrer le numero du membre

        m.setNumeroMembre(NUMERO_MEMBRE++);

        // Recuperer le membre

        membres.put(m.getId(), m);

    }

    // Trouver par id

    public Membre findById(String idMembre) throws MembreNonTrouveException {

        // Retourne le membre avec l'id ou lance 'MembreNonTrouveException'

        return membres.values().stream()
        .filter(valeur -> valeur.getId().equals(idMembre))
        .findAny()
        .orElseThrow(MembreNonTrouveException::new);

    }

}
