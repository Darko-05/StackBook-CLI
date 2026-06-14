package registre;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import model.Emprunt;

import java.util.LinkedHashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Emprunts {

    // Instance unique

    private final static Emprunts INSTANCE = new Emprunts();

    // Attribut registre

    private final Map<String, Emprunt> emprunts = new LinkedHashMap<>();

    // Getters

    public static Emprunts getInstance() {

        return INSTANCE;

    }

    public Map<String, Emprunt> getEmprunts() {

        return new BorrowRecord(emprunts).emprunts();

    }

    // Ajouter un emprunt

    public void ajouteremprunt(@NonNull Emprunt e) {

        emprunts.put(e.getId(), e);

    }

    // Trouver avec id

    public Emprunt findById(String idEmprunt) {

        return emprunts.values().stream()
        .filter(valeur -> valeur.getId().equals(idEmprunt))
        .findAny()
        .orElseThrow(IllegalArgumentException::new);

    }

}
