package model;

import lombok.NonNull;

public enum TextModifier {

    // Codes couleurs

    NOIR("\u001B[30m"),
    ROUGE("\u001B[31m"),
    VERT("\u001B[32m"),
    VERTCLAIR("\u001B[92m"),
    JAUNE("\u001B[33m"),
    BLEU("\u001B[34m"),
    MAGENTA("\u001B[35m"),
    CYAN("\u001B[36m"),
    BLANC("\u001B[37m"),
    FOND_BLANC("\u001B[47m"),
    ORANGE("\u001B[38;5;208m"),
    ROSE("\u001B[38;5;205m"),
    TURQUOISE("\u001B[38;2;64;224;208m"),

    // Codes textes

    ITALIQUE("\u001B[3m"),
    SOULIGNE("\u001B[4m"),

    // Code de fin

    FIN("\u001B[0m");

    // Attribut Code

    private final String modifierCode;

    // Constructeur

    TextModifier(final String code) {

        this.modifierCode = code;

    }

    // Methodes utilitaire

    public static @NonNull String colorText(@NonNull TextModifier color, String text) {

        return color.modifierCode + text + TextModifier.FIN.modifierCode;

    }

    public static @NonNull String putWhiteBackground(String text) {

        return TextModifier.FOND_BLANC.modifierCode + text + TextModifier.FIN.modifierCode;

    }

    public static @NonNull String modifyText(@NonNull TextModifier police, String text) {

        return police.modifierCode + text + TextModifier.FIN.modifierCode;

    }

}
