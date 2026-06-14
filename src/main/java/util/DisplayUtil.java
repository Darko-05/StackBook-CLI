package util;

import lombok.experimental.UtilityClass;
import model.Auteur;
import model.Livre;
import lombok.NonNull;
import model.Membre;

import java.util.function.Function;

@UtilityClass
public final class DisplayUtil {

    // Méthode de standarisation

    public static @NonNull String standariserTitre(@NonNull Livre l) {

        return Character.toUpperCase(l.getTitre().charAt(0)) + l.getTitre().substring(1);

    }

    public static @NonNull String standariserNomAuteur(@NonNull Auteur a) {

        return Character.toUpperCase(a.nom().charAt(0)) + a.nom().substring(1) + " " +  Character.toUpperCase(a.prenom().charAt(0)) + a.prenom().substring(1);

    }

    public static @NonNull String standariserNom(@NonNull String nom) {

        return Character.toUpperCase(nom.charAt(0)) + nom.substring(1);

    }

    public static @NonNull String standariserEnum(@NonNull Object e) {

        return Character.toUpperCase(e.toString().charAt(0)) + e.toString().substring(1).toLowerCase();

    }

    public static Function<Membre, String> standariserNomMembre = membre -> Character.toUpperCase(membre.getPrenom().charAt(0)) + membre.getPrenom().substring(1) + " " + Character.toUpperCase(membre.getNom().charAt(0)) + membre.getNom().substring(1);

}
