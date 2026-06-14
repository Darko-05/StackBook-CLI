package util;

import lombok.experimental.UtilityClass;
import model.Livre;
import model.Membre;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

@UtilityClass
public class GeneratorUtil {

    public static Function<Livre, String> genererIdLivre = livre -> {

        // Generer id

        Random random = new Random();

        return /* Premier caractère du titre*/ Character.toUpperCase(livre.getTitre().charAt(0))

        + /* Nombre aleatoire entre 0 et 9 */ random.nextInt(9)

        + /* Deux caractères suivant du titre */livre.getTitre().substring(1, 3).toUpperCase()

        + /* Année de publication */ livre.getAnneePublication();

    };

    public static Function<Membre, String> genererIdMembre = membre -> {

        // Generer id

        Random random = new Random();

        int position = membre.getEmail().indexOf("@");

        return /* Recupère la partie avant '@' */ membre.getEmail().substring(0, position)

        + /* Plus aleatoire 10^3 possibilités*/ random.nextInt(9) + random.nextInt(9) + random.nextInt(9)

        + /* Premier caractère du nom en majuscule */ membre.getNom().toLowerCase().charAt(0)

        + /* Dernier caractère du prenom en miniscule */ membre.getPrenom().toUpperCase().charAt(membre.getPrenom().length() - 1)

        + /* Un nombre entre 1 et 10 */ random.nextInt(10) + 1;

    };

    public static Function<List<Object>, String> genererIdEmprunt = livreOuMembre -> {

        String idEmprunt = "";

        // Recuperer les 3 premiers caractères de l'id du livre et du memebre

        if (livreOuMembre instanceof Livre livre) {

            idEmprunt += livre.getId().substring(0, 3);

        }

        if (livreOuMembre instanceof Membre membre) {

            idEmprunt += membre.getId().substring(0, 3);

        }

        // Ajoute un nombre aléatoire

        Random random = new Random();

        idEmprunt += random.nextInt(1000);

        return idEmprunt;

    };

    public static Function<List<Object>, String> genererIdReservation = livreEtMembre -> {

        String idReservation = "";

        // 3 premiers caractères de l'id du livre

        if (livreEtMembre instanceof Livre leLivre) {

            idReservation += leLivre.getId().substring(0, 3);

        }

        // Année de reservation (année du jour de la reservation)

        idReservation += LocalDate.now().getYear();

        // 3 premier caractères de l'id du membre

        if (livreEtMembre instanceof Membre leMembre) {

            idReservation += leMembre.getId().substring(0, 3);

        }

        // L'heure actuel

        idReservation += LocalDateTime.now().getHour();

        return idReservation;

    };

}
