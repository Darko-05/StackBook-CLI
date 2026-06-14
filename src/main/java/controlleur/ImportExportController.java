package controlleur;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import model.ObjetImportable;
import util.DataUtil;
import util.MenuUtil;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.function.Supplier;

import static main.AppConfig.*;
import static main.AppView.importExportView;
import static main.AppView.menuView;
import static util.DataUtil.*;
import static util.MenuUtil.handleUserChoice;

@NoArgsConstructor
public class ImportExportController {
    
    // Methode principal
    
    public void handleImportExportManagement() {

        // Afficher le menu principal

        importExportView.showMainMenu(serviceImportExport.getDateDernierExport(), serviceImportExport.getDateDernierImport());

        // Gestion des Choix

        handleUserChoice(
            Map.of(
                "E", this::handleDataExportation,
                "I", this::handleDataImportation,
                "0", MainController::launchControllers
            )
        );

    }

    // Export de données

    private void handleDataExportation() {

         // Afficher le menu d'export

        importExportView.showExportMenu(nombreTotalLivres(), nombreTotalMembres(), nombreTotalEmprunts(), nombreTotalReservations(), nombreAuteurs(), nombreTotalMaisonEdition());

        // Gestion Choix

        handleUserChoice(
            Map.of(
                "1", this::handleExportBooks,
                "2", this::handleExportMembers,
                "3", this::handleExportBorrowing,
                "4", this::handleExportReservation,
                "5", this::handleExportAuthors,
                "6", this::handleExportHomeEditor,
                "7", this::handleExportGlobalStatistics,
                "8", this::handleExportMonthlyStatistics,
                "9", this::exportAllData,
                "0", this::handleImportExportManagement
            )
        );

    }

    // Imports de données

    private void handleDataImportation() {

        // Afficher le menu d'import

        importExportView.showImportMenu();

        // Gestion Choix

        handleUserChoice(
            Map.of(
                "1", this::handleHomeEditorImportation,
                "2", this::handleAuthorImportation,
                "3", this::handleBooksImportation,
                "4", this::handleMembersImportation,
                "0", this::handleImportExportManagement
            )
        );

    }

    // Exporter les livres

    private void handleExportBooks() {

        handleGenericExport(

            serviceImportExport::exporterLivres,
            "data/livres/livres.csv",
            this::handleExportBooks

        );
    }

    // Exporter les membres

    private void handleExportMembers() {

        handleGenericExport(

            serviceImportExport::exporterMembres,
            "data/membres/membres.csv",
            this::handleExportMembers

        );
    }

    // Exporter les emprunts

    private void handleExportBorrowing() {

        handleGenericExport(

            serviceImportExport::exporterEmprunts,
            "data/emprunts/emprunts.csv",
            this::handleExportBorrowing

        );

    }

    // Exporter les réservations

    private void handleExportReservation() {

        handleGenericExport(

            serviceImportExport::exporterReservations,
            "data/reservations/reservations.csv",
            this::handleExportReservation

        );

    }

    // Exporter les auteurs

    private void handleExportAuthors() {

        handleGenericExport(

            serviceImportExport::exporterAuteurs,
            "data/auteurs/auteurs.csv",
            this::handleExportAuthors

        );

    }

    // Exporter les maisons d'édition

    private void handleExportHomeEditor() {

        handleGenericExport(

            serviceImportExport::exporterMaisonEdition,
            "data/maisonEdition/maisonsEdition.csv",
            this::handleExportHomeEditor

        );

    }

    // Exporter les statistiques global

    private void handleExportGlobalStatistics() {

        handleGenericExport(

            serviceImportExport::exporterStatistiquesGlobal,
            "data/statistiques/statistiques_Global.csv",
            this::handleExportGlobalStatistics

        );

    }

    // Exporter les statistiques mensuelles

    private void handleExportMonthlyStatistics() {

        handleGenericExport(

            serviceImportExport::exporterStatistiquesMensuelles,
            "data/statistiques/statistiques_Mensuelles.csv",
            this::handleExportMonthlyStatistics

        );

    }

    // Exporter toute les données

    private void exportAllData() {

        handleGenericExport(

            serviceImportExport::toutExporter,
            "data/allData",
            this::exportAllData

        );

    }

    // Importer des maisons d'éditions

    private void handleHomeEditorImportation() {

        // Recuperer le chemin du fichier

        String filePath = importExportView.getFilePath();

        // Gerer l'import

        handleGenericImport(

            serviceImportExport::importerMaisonEdition,
            ObjetImportable.MAISON_EDITION,
            filePath,
            DataUtil::nombreTotalMaisonEdition,
            this::handleHomeEditorImportation

        );

    }

    // Importer des auteurs

    private void handleAuthorImportation() {

        // Recuperer le chemin du fichier

        String filePath = importExportView.getFilePath();

        // Gerer l'import

        handleGenericImport(

            serviceImportExport::importerAuteurs,
            ObjetImportable.AUTEUR,
            filePath,
            DataUtil::nombreAuteurs,
            this::handleAuthorImportation

        );

    }

    // Importer des livres

    private void handleBooksImportation() {

        // Recuperer le chemin du fichier

        String filePath = importExportView.getFilePath();

        // Gerer l'import

        handleGenericImport(

            serviceImportExport::importerLivres,
            ObjetImportable.LIVRE,
            filePath,
            DataUtil::nombreTotalLivres,
            this::handleBooksImportation

        );

    }

    // Importer des membres

    private void handleMembersImportation() {

        // Recuperer le chemin du fichier

        String filePath = importExportView.getFilePath();

        // Gerer l'import

        handleGenericImport(

            serviceImportExport::importerMembres,
            ObjetImportable.MEMBRE,
            filePath,
            DataUtil::nombreTotalMembres,
            this::handleMembersImportation

        );

    }

    // Methode generique d'export

    private void handleGenericExport(@NonNull MenuUtil.ExportAction exportAction, String filePath, Runnable retryAction) {

        try {

            // Lancer l'export

            exportAction.execute();

            // Afficher succes

            importExportView.showExportSucces(filePath);

            // Gerer la fin de l'export

            handleExportSuccesEnd();

        } catch (IOException e) {

            menuView.showAFailure("ECHEC D'EXPORTATION", "Erreur d'ecriture dans le fichier");

            // Gestion choix

            handleUserChoice(
                Map.of(
                    "R", retryAction,
                    "A", this::handleImportExportManagement
                )
            );

        }

    }

    // Methode generique d'import

    private void handleGenericImport(@NonNull MenuUtil.ImportAction importAction, ObjetImportable obj, String filePath, Supplier<Long> nouveauTotal, Runnable retryAction) {

        // Compter le nombre de lignes du ficher

        long nombreLignes = 0;

        try (FileReader reader = new FileReader(filePath)) {

            nombreLignes = countLines(reader);

        } catch (IOException _) {}

        try (FileReader reader = new FileReader(filePath)) {

            // Lancer l'import

            importAction.execute(reader);

            // Afficher le succes

            importExportView.showImportSucces(filePath, nombreLignes, nouveauTotal.get(), obj);

            handleImportSucessEnd();

        } catch (IOException e) {

            importExportView.showImportFail("Erreur de lecture du fichier.");

            handleImportFailureEnd(retryAction);

        } catch (IllegalArgumentException e) {

            importExportView.showImportFail(e.getMessage());

            handleImportFailureEnd(retryAction);

        }

    }

    private void handleImportSucessEnd() {

        handleUserChoice(
            Map.of(
                "N", this::handleDataImportation,
                "R", this::handleImportExportManagement
            )
        );

    }

    private void handleImportFailureEnd(Runnable retryAction) {

        handleUserChoice(
            Map.of(
                "N", retryAction,
                "R", this::handleImportExportManagement
            )
        );

    }

    // Gerer la fin des exports

    private void handleExportSuccesEnd() {

        handleUserChoice(
            Map.of(
                "N", this::handleDataExportation,
                "R", this::handleImportExportManagement
            )
        );

    }
    
}