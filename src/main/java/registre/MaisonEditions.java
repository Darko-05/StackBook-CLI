package registre;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import model.MaisonEdition;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MaisonEditions {

    // Instance unique

    private static final MaisonEditions INSTANCE = new MaisonEditions();

    // Attribut registre

    private final List<MaisonEdition> maisonEditions = new ArrayList<>();

    // Getters

    public static MaisonEditions getInstance() {

        return INSTANCE;

    }

    public List<MaisonEdition> getMaisonEditions() {

        return new MaisonEditionRecord(maisonEditions).maisonEditions();

    }

    // Méthodes utilitaire

    public void ajouterMaisonEdition(@NonNull MaisonEdition m) {

        maisonEditions.add(m);

    }

    public void supprimerMaisonEdition(@NonNull MaisonEdition m) {

        maisonEditions.remove(m);

    }

    public boolean estContenu(@NonNull MaisonEdition m) {

        return maisonEditions.contains(m);

    }

}
