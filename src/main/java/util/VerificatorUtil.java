package util;

import lombok.experimental.UtilityClass;

import java.util.function.Predicate;

@UtilityClass
public final class VerificatorUtil {

    // Méthodes utilitaire

    public static Predicate<String> validerISBN = Isbn -> {

        String IsbnSansTiret = Isbn.replaceAll("-", "");

        return IsbnSansTiret.length() == 13 && IsbnSansTiret.matches("\\d+");

    };

    public static Predicate<String> nomEstValide = nom -> nom != null && !nom.isBlank() && nom.trim().length() >= 3 && nom.matches("^[a-zA-ZÀ-ÿ\\s-]+$");

    public static Predicate<String> emailValide = email -> email != null && email.matches("[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");

    public static Predicate<String> codePostalEstValide = code -> !(code == null) && !code.isBlank() && code.length() >= 5 && code.matches(".*[0-9].*");

    public static Predicate<String> numeroEstValide = numero -> !(numero == null) && !numero.isBlank() && (numero.length() >= 8 && numero.length() <= 10) && numero.matches(".*[0-9].*");

    public static Predicate<String> biographieEstValide = bio -> !(bio == null) && !bio.isBlank() && bio.length() >= 5;

    public static Predicate<String> titreEstValide = titre -> !(titre == null) && !titre.isBlank() && titre.length() >= 3 && titre.matches(".*[a-zA-Z0-9].*");

}
