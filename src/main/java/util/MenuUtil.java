package util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import static java.lang.IO.println;
import static main.AppView.menuView;

@UtilityClass
public final class MenuUtil {

    // Interface fonctionnel pour les methodes generics

    @FunctionalInterface
    public interface ExportAction {

        void execute() throws IOException;

    }

    @FunctionalInterface
    public interface ImportAction {

        void execute(FileReader reader) throws IOException;

    }

    // Méthodes utilitaires

    public static void handleUserChoice(@NonNull Map<String, Runnable> actions) {

        // Recupère les clés de chaque map en un tableau

        String[] keys = actions.keySet().toArray(new String[0]);

        // Recupère un choix correct

        String choix = getValidChoice(keys);

        // Appel la méthode associée

        actions.get(choix).run();

    }

    public static @NonNull String getValidChoice(String @NonNull ... validChoices) {

        while (true) {

            String choix = menuView.getChoice();

            for (String valid : validChoices) {

                if (choix.equalsIgnoreCase(valid)) {

                    return choix.toUpperCase();

                }

            }

            println("Choix invalide");

        }

    }

}