package util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import model.Auteur;
import model.CategorieLivre;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@UtilityClass
public final class ParsingUtil {

    public static <K, V> @NonNull Collector<Map.Entry<K, V>, ?, LinkedHashMap<K, V>> toLinkedHashMap() {

        return Collectors.toMap(

            Map.Entry::getKey,

            Map.Entry::getValue,

            (a, _) -> a,

            LinkedHashMap::new

        );

    }

    public static LocalDate parseLocalDate(String date) {

        LocalDate localDate;

        try {

            localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        } catch (DateTimeParseException e) {

            throw new IllegalArgumentException("Format de date invalide : " + e.getMessage());

        }

        return localDate;

    }

    public static long tryParsingAuthorChoice(String enter, long numberOfAuthor) {

        long numeroAuteur;

        try {

            numeroAuteur = Long.parseLong(enter);

            if (numeroAuteur < 1 || numeroAuteur > numberOfAuthor) throw new IllegalArgumentException("Numero inexistant");

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException("Numero invalide");

        }

        return numeroAuteur;

    }

    public static long tryParsingCopyNumber(String enter) {

        long nombreCopies;

        try {

            nombreCopies = Long.parseLong(enter);

            if (nombreCopies <= 0) throw new IllegalArgumentException("Nombre de copies du livre invalide");

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException("Nombre d'exemplaires invalide");

        }

        return nombreCopies;

    }

    public static @NonNull Year tryParsingPlushingYear(String enter) {

        try {

            return Year.parse(enter);

        } catch (DateTimeParseException e) {

            throw new IllegalArgumentException("Format de l'année invalide");

        }

    }

    public static CategorieLivre tryParsingBookCategory(String enter) {

        try {

            int numeroCategorie = Integer.parseInt(enter);

            int nombreCategories = CategorieLivre.values().length;

            if (numeroCategorie <= 0 || numeroCategorie > nombreCategories) throw new IllegalArgumentException("Numero de catégorie invalide");

            return CategorieLivre.values()[numeroCategorie - 1];

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException("Choix de categorie invalide");

        }

    }

    public static @NonNull Map<Long, Auteur> toAuthorKeyMap(@NonNull Set<Auteur> auteurs) {

        Map<Long, Auteur> authorKeyMap = new HashMap<>();

        AtomicLong i = new AtomicLong(0);

        auteurs.forEach(auteur -> authorKeyMap.putIfAbsent(i.incrementAndGet(), auteur));

        return authorKeyMap;

    }

}