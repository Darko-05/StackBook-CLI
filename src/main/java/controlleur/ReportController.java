package controlleur;

import lombok.NoArgsConstructor;
import model.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

import static main.AppConfig.*;
import static main.AppView.reportView;
import static util.DataUtil.membresInactifs;
import static util.DataUtil.nombreTotalLivres;
import static util.MenuUtil.handleUserChoice;

@NoArgsConstructor
public class ReportController {

    private final Periode periode = new Periode(LocalDate.now().minusMonths(1), LocalDate.now());

    // Methode principal

    public void handleReportSystem() {

        // Afficher le menu principal

        reportView.showReportMainMenu(periode);

        // Gestion Choix

        handleUserChoice(
            Map.of(
                "G", this::showGlobalStatistics,
                "M", this::showMonthlyStatistics,
                "P", this::showBooksPopularity,
                "A", this::showMembersActivity,
                "I", this::showRiskyMembers,
                "0", MainController::launchControllers
            )
        );

    }

    // Afficher les statistiques global

    private void showGlobalStatistics() {

        // Appel du service

        StatistiquesGlobal globalStats = serviceRapport.genererStatistiquesGlobal(nombreTotalLivres());

        // Affichage du resultat

        reportView.showGlobalStatistics(globalStats);

        // Gestion choix

        handleUserChoice(Map.of("R", this::handleReportSystem));

    }

    // Afficher les statistiques mensuelles

    private void showMonthlyStatistics() {

        // Appel du service

        StatistiquesMensuelles monthlyStats = serviceRapport.genererStatistiquesMensuelles(periode.dateDebut());

        // Affichage du resultat

        YearMonth actualMonth = YearMonth.now();

        reportView.showMonthlyStatistics(actualMonth, monthlyStats);

        // Gestion choix

        handleUserChoice(Map.of("R", this::handleReportSystem));

    }

    // Afficher la popularité des livres

    private void showBooksPopularity() {

        // Appel du service

        RapportPopularite booksPopularity = serviceRapport.genererRapportPopularite();

        // Affichage du resultat

        reportView.showBooksPopularity(booksPopularity);

        // Gestion choix

        handleUserChoice(Map.of("R", this::handleReportSystem));

    }

    // Afficher l'activité des membres

    private void showMembersActivity() {

        // Appel du service

        ActivitesMembres membersActivity = serviceRapport.genererRapportActivitesMembres(periode.dateDebut());

        // Affichage du resultat

        YearMonth actualMonth = YearMonth.now();

        reportView.showMembersActivities(actualMonth, membersActivity);

        // Gestion choix

        handleUserChoice(Map.of("R", this::handleReportSystem));

    }

    // Afficher les membres inactifs

    private void showRiskyMembers() {

        // Affichage du resultat

        reportView.showRiskyMembers(membresInactifs());

        // Gestion choix

        handleUserChoice(Map.of("R", this::handleReportSystem));

    }

}