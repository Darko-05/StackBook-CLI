package view;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import model.ObjetImportable;
import model.TextModifier;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static java.lang.IO.print;
import static java.lang.IO.println;
import static main.AppView.menuView;

@NoArgsConstructor
public class ImportExportView {

    // Scanner

    private final Scanner sc = new Scanner(System.in);

    // Menu Principal

    public void showMainMenu(@NonNull LocalDateTime dateDernierImport, @NonNull LocalDateTime dateDernierExport) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ORANGE, "IMPORT / EXPORT DE DONNÉES"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Choix possible

        menuView.showLine("[E] EXPORTER des données");
        menuView.showLine("[I] IMPORTER des données");
        menuView.showLine();
        menuView.showLine(TextModifier.colorText(TextModifier.ROUGE, "[0] RETOUR AU MENU PRINCIPAL"));
        menuView.showLine();

        // Pied de page

        menuView.showMiddleBorder();
        menuView.showLine("Dernier Export : " + dateDernierExport.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        menuView.showLine("Dernier Import : " + dateDernierImport.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        menuView.showBottomBorder();

    }

    // Menu 1 - Export

    public void showExportMenu(long nombreTotalLivres, long nombreTotalMembres, long nombreTotalEmprunts, long nombreTotalReservations, long nombreTotalAuteurs, long nombreTotalMaisonsEdition) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "EXPORTER DES DONNÉES"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Menu

        menuView.showLine("Sélectionnez les données à exporter :");
        menuView.showLine();
        showExportMenuChoice(nombreTotalLivres, nombreTotalMembres, nombreTotalEmprunts, nombreTotalReservations, nombreTotalAuteurs, nombreTotalMaisonsEdition);

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine("Format par défaut : CSV  |  Destination : /data/" );
        menuView.showBottomBorder();

    }

    void showExportMenuChoice(long nombreTotalLivres, long nombreTotalMembres, long nombreTotalEmprunts, long nombreTotalRservations, long nombreTotalAuteurs, long nombreTotalMaisonsEdition) {

        // Le mois courant

        YearMonth actualMonth = YearMonth.now();

        // Choix possible

        menuView.showLine("[1] Livres (" + TextModifier.colorText(TextModifier.VERT, String.valueOf(nombreTotalLivres)) + " enregistrements)");
        menuView.showLine("[2] Membres (" + TextModifier.colorText(TextModifier.VERT, String.valueOf(nombreTotalMembres)) + " enregistrements)");
        menuView.showLine("[3] Emprunts (" + TextModifier.colorText(TextModifier.VERT, String.valueOf(nombreTotalEmprunts)) + " en cours)");
        menuView.showLine("[4] Réservations (" + TextModifier.colorText(TextModifier.VERT, String.valueOf(nombreTotalRservations)) + " en attente)");
        menuView.showLine("[5] Auteurs (" + TextModifier.colorText(TextModifier.VERT, String.valueOf(nombreTotalAuteurs)) + " enregistrements");
        menuView.showLine("[6] Maisons d'édition (" + TextModifier.colorText(TextModifier.VERT, String.valueOf(nombreTotalMaisonsEdition)) + " enregistrements)");

        // Statistiques

        menuView.showLine("[7] Statistiques globales");
        menuView.showLine("[8] Statistiques mensuelles (" + actualMonth.getMonth() + " "  + actualMonth.getYear() + ")");

        menuView.showLine();
        menuView.showLine(TextModifier.colorText(TextModifier.VERTCLAIR,"[9] Toute les données"));

        // Retour

        menuView.showLine();
        menuView.showLine(TextModifier.colorText(TextModifier.ROUGE,"[0] Retour"));

    }

    public void showExportSucces(String chemin) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERTCLAIR, "EXPORT TERMINÉ AVEC SUCCÈS"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Contenu

        menuView.showLine("L'export a été réalisé avec succès !");
        menuView.showLine();
        menuView.showLine("Fichier généré : " + chemin);

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[N]") + " Nouvel export | " + TextModifier.colorText(TextModifier.ROUGE,"[R]") + " Retour au menu");
        menuView.showBottomBorder();

    }

    // Menu 2 - Import

    public void showImportMenu() {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "IMPORTER DES DONNÉES"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Choix possible

        menuView.showLine("Sélectionnez le type de données à importer :");
        menuView.showLine();

        menuView.showLine("[1] Maisons d'édition");
        menuView.showLine("[2] Auteurs");
        menuView.showLine("[3] Livres");
        menuView.showLine("[4] Membres");

        menuView.showLine();
        menuView.showLine(TextModifier.colorText(TextModifier.ROUGE,"[0] Retour"));

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine("Formats acceptés : \"CSV\" | Delimiteur : \";\"");
        menuView.showCenteredLine("NB : Plus le fichier est lourd plus le traitement va durer");
        menuView.showBottomBorder();

    }

    public void showImportSucces(@NonNull String fichier, long nombreTotalDeLignes, long nouveauTotal, @NonNull ObjetImportable objet) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERTCLAIR, "IMPORT TERMINÉ AVEC SUCCÈS"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Contenu

        menuView.showLine("Rapport d'importation :");
        menuView.showLine();

        menuView.showLine("Fichier : " + fichier);
        menuView.showLine("Total de lignes : " + TextModifier.colorText(TextModifier.ROSE, String.valueOf(nombreTotalDeLignes)));
        menuView.showLine();

        menuView.showLine("Nouveau total : " + TextModifier.modifyText(TextModifier.SOULIGNE, TextModifier.colorText(TextModifier.ROUGE, String.valueOf(nouveauTotal))) + " " + objet.getNom());

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[N]") + " Nouvel import | " + TextModifier.colorText(TextModifier.ROUGE,"[R]") + " Retour au menu");
        menuView.showBottomBorder();

    }

    public void showImportFail(String e) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "ERREUR D'IMPORTATION"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Contenu

        menuView.showLine("Raison :");
        menuView.showLine("• " + e);
        menuView.showLine();
        menuView.showLine(TextModifier.colorText(TextModifier.SOULIGNE, "S'assurer que") + " : ");
        menuView.showLine();
        menuView.showLine("• Le fichier n'est pas corrompu et que le format csv est respectée.");
        menuView.showLine();
        menuView.showLine( "Modifier le fichier et réessayer");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[N]") + " Réessayer | " + TextModifier.colorText(TextModifier.ROUGE,"[R]") + " Retour au menu");
        menuView.showBottomBorder();

    }

    // Méthodes de recuperation

    public String getFilePath() {

        println("Entrez le chemin absolu du fichier a importer");

        print("> ");

        return sc.nextLine();

    }

}
