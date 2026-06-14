package view;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import model.TextModifier;

import java.util.Scanner;

import static java.lang.IO.print;
import static java.lang.IO.println;

@Slf4j
@NoArgsConstructor
public class MainMenuView {

    // Scanner

    private final Scanner sc = new Scanner(System.in);

    // Menu de recuperation de données

    public void getDataHeader() {

        try {

            println();
            println("... Chargement du formulaire de remplissage ...");
            println();

            Thread.sleep(1000);

        }

        catch (InterruptedException e) {

            log.error("Erreur : ", e);

        }

    }

    public void getDataFooter() {

        try {

            println();
            println("... Recuperation et remplissage des données ...");
            println();

            Thread.sleep(1000);

        }

        catch (InterruptedException e) {

            log.error("Erreur : ", e);

        }

    }

    // Afficher Menu Principal

    public void showMainMenu(long nombreTotalLivres, long nombreTotalMembres) {

        // En tete

        showTopBorder();

        // Titre de l'Application

        showCenteredLine(TextModifier.putWhiteBackground(TextModifier.modifyText(TextModifier.SOULIGNE, TextModifier.colorText(TextModifier.NOIR, "StackBook CLI v1.0"))));

        // Ligne du milieu

        showMiddleBorder();

        // Menu Principal

        showLine();
        showLine(TextModifier.colorText(TextModifier.BLEU, "MENU PRINCIPAL"));

        // Differents choix du menu

        showMenuChoice();

        // Pied de page

        showFooter(nombreTotalLivres, nombreTotalMembres);

        showBottomBorder();

    }

    // Choix menu

    public void showMenuChoice() {

        showLine();
        showLine("[" + 1 + "] GESTION DES LIVRES");
        showLine("[" + 2 + "] GESTION DES MEMBRES");
        showLine("[" + 3 + "] GESTION DES EMPRUNTS & RETOURS");
        showLine("[" + 4 + "] SYSTEME DE RESERVATIONS");
        showLine("[" + 5 + "] GESTION DES AUTEURS");
        showLine("[" + 6 + "] GESTION DES MAISON D'EDITION");
        showLine("[" + 7 + "] RAPPORT & STATISTIQUES");
        showLine("[" + 8 + "] IMPORT / EXPORT");

        showLine();
        showLine(TextModifier.colorText(TextModifier.ROUGE, "[" + 0 + "] QUITTER L'APPLICATION"));
        showLine();

    }

    // Pied de page

    public void showFooter(long nombreTotalLivres, long nombreTotalMembres) {

        showMiddleBorder();

        showCenteredLine("Total : " + TextModifier.modifyText(TextModifier.SOULIGNE, TextModifier.colorText(TextModifier.VERT, nombreTotalLivres + " livres")) + " | " + TextModifier.modifyText(TextModifier.SOULIGNE, TextModifier.colorText(TextModifier.MAGENTA, nombreTotalMembres + " membres")));

    }

    // Recuperer choix du menu

    public String getChoice() {

        println("Votre Choix :");

        print("> ");

        return sc.nextLine();

    }

    // Menu Erreur Generique

    public void showAFailure (@NonNull String menu, String raison) {

        // En tete

        showTopBorder();
        showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, menu.toUpperCase()));
        showMiddleBorder();
        showLine();

        // Détails

        showLine(TextModifier.colorText(TextModifier.ROUGE, "Raison :"));
        showLine("• " + raison);

        // Pied de page

        showLine();
        showMiddleBorder();
        showCenteredLine(TextModifier.colorText(TextModifier.ROSE, "[R]") + " Réessayer | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retour");
        showBottomBorder();

    }

    // Méthode utilitaire

    public void showTopBorder() {

        println("╔" + "=".repeat(70) + "╗");

    }

    public void showMiddleBorder() {

        println("╠" + "═".repeat(70) + "╣");

    }

    public void showBottomBorder() {

        println("╚" + "═".repeat(70) + "╝");

    }

    public void showLine() {

        // Le contenu de la chaine est vide

        String content = "";

        // Calcul des dimensions

        int totalWidth = 70;
        int paddingLeft = 2;
        int paddingRight = totalWidth - paddingLeft;

        // Ligne normal non centré

        String line = "║" + " ".repeat(paddingLeft) + content + " ".repeat(paddingRight) + "║";

        // Affiche la ligne

        println(line);

    }

    public void showLine(@NonNull String content) {

        // Enlève les code ANSI

        String cleanContent = content.replaceAll("\u001B\\[[;\\d]*m", "");

        // Calcul des dimensions

        int totalWidth = 70;
        int paddingLeft = 2;
        int paddingRight = totalWidth - cleanContent.length() - paddingLeft;

        // Enlève le padding a droite si le texte est trop long

        if (paddingRight < 0) paddingRight = 0;

        // Ligne normal non centré

        String line = "║" + " ".repeat(paddingLeft) + content + " ".repeat(paddingRight) + "║";

        // Affiche la ligne

        println(line);

    }

    public void showCenteredLine(@NonNull String content) {

        // Enlève les code ANSI

        String cleanContent = content.replaceAll("\u001B\\[[;\\d]*m", "");

        // Calcule les dimensions

        int totalWidth = 70;
        int paddingLeft = (totalWidth - cleanContent.length()) / 2;
        int paddingRight = totalWidth - cleanContent.length() - paddingLeft;

        // Forle la ligne

        String line = "║" + " ".repeat(paddingLeft) + content + " ".repeat(paddingRight) + "║";

        // Affiche

        println(line);

    }

    // Méthodes pour les grands tableau

    public void showBigTopBorder() {

        println("╔" + "=".repeat(90) + "╗");

    }

    public void showBigMiddleBorder() {

        println("╠" + "═".repeat(90) + "╣");

    }

    public void showBigBottomBorder() {

        println("╚" + "═".repeat(90) + "╝");

    }

    public void showBigLine() {

        // Le contenu de la chaine est vide

        String content = "";

        // Calcul des dimensions

        int totalWidth = 90;
        int paddingLeft = 2;
        int paddingRight = totalWidth - paddingLeft;

        // Ligne normal non centré

        String line = "║" + " ".repeat(paddingLeft) + content + " ".repeat(paddingRight) + "║";

        // Affiche la ligne

        println(line);

    }

    public void showBigLine(@NonNull String content) {

        // Enlève les code ANSI

        String cleanContent = content.replaceAll("\u001B\\[[;\\d]*m", "");

        // Calcul des dimensions

        int totalWidth = 90;
        int paddingLeft = 2;
        int paddingRight = totalWidth - cleanContent.length() - paddingLeft;

        // Enlève le padding a droite si le texte est trop long

        if (paddingRight < 0) paddingRight = 0;

        // Ligne normal non centré

        String line = "║" + " ".repeat(paddingLeft) + content + " ".repeat(paddingRight) + "║";

        // Affiche la ligne

        println(line);

    }

    public void showBigCenteredLine(@NonNull String content) {

        // Enlève les code ANSI

        String cleanContent = content.replaceAll("\u001B\\[[;\\d]*m", "");

        // Calcule les dimensions

        int totalWidth = 90;
        int paddingLeft = (totalWidth - cleanContent.length()) / 2;
        int paddingRight = totalWidth - cleanContent.length() - paddingLeft;

        // Forle la ligne

        String line = "║" + " ".repeat(paddingLeft) + content + " ".repeat(paddingRight) + "║";

        // Affiche

        println(line);

    }

}