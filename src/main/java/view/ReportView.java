package view;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import model.*;

import java.time.DayOfWeek;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static main.AppView.menuView;
import static util.DisplayUtil.standariserNomMembre;
import static util.DisplayUtil.standariserTitre;

@NoArgsConstructor
public class ReportView {

    // Menu - Statistiques et Rapports

    public void showReportMainMenu(@NonNull Periode periodeAnalyse) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.TURQUOISE, "RAPPORTS ET STATISTIQUES"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Menu

        menuView.showLine("[G] STATISTIQUES GLOBALES");
        menuView.showLine("[M] STATISTIQUES MENSUELLES");
        menuView.showLine("[P] POPULARITÉ DES LIVRES");
        menuView.showLine("[A] ACTIVITÉS DES MEMBRES");
        menuView.showLine("[I] MEMBRES INACTIFS");
        menuView.showLine();
        menuView.showLine(TextModifier.colorText(TextModifier.ROUGE, "[0] RETOUR AU MENU PRINCIPAL"));

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showLine("Période analysée : " + periodeAnalyse.dateDebut().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " - " + periodeAnalyse.dateFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        menuView.showBottomBorder();

    }

    // Menu 1 - Statistiques Global

    public void showGlobalStatistics(@NonNull StatistiquesGlobal statistiquesGlobal) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERTCLAIR, "STATISTIQUES GLOBALES DE LA BIBLIOTHÈQUE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Stats global

        showGlobalStats(statistiquesGlobal);
        menuView.showLine();

        // Evolution des 3 derniers mois

        showLast3MonthsEvolution(statistiquesGlobal.evolutionMensuellesDes3DerniersMois());
        menuView.showLine();

        // Top 5 livres

        showTop5BestBooks(statistiquesGlobal.top5LivresDeToutePeriode());

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    void showGlobalStats(@NonNull StatistiquesGlobal statistiquesGlobal) {

        menuView.showLine(TextModifier.modifyText(TextModifier.ITALIQUE, TextModifier.colorText(TextModifier.TURQUOISE, "INDICATEURS CLÉS :")));
        menuView.showLine("• Stock total de livres : " + statistiquesGlobal.stockTotalDeLivres());
        menuView.showLine("• Emprunts cumulés : " + statistiquesGlobal.empruntsCumules());
        menuView.showLine("• Taux de rotation global : " + statistiquesGlobal.tauxDeRotationGlobal());
        menuView.showLine("• Membres actifs : " + statistiquesGlobal.membreActifs());
        menuView.showLine("• Livres disponibles : " + statistiquesGlobal.livresDisponibles());
        menuView.showLine("• Retours totaux : " + statistiquesGlobal.nombreTotalDeRetour());
        menuView.showLine("• Réservations actives : " + statistiquesGlobal.reservationsActives());
        menuView.showLine("• Taux de réservations : " + statistiquesGlobal.tauxDeReservations());
        menuView.showLine("• Pénalités totales : " + statistiquesGlobal.penalitesTotal());

    }

    void showLast3MonthsEvolution(@NonNull List<EvolutionMensuelle> evolutionMensuelles) {

        menuView.showLine(TextModifier.colorText(TextModifier.MAGENTA, "ÉVOLUTION DES 3 DERNIERS MOIS :"));
        evolutionMensuelles.forEach(evolutionMensuelle ->

                menuView.showLine(YearMonth.of(evolutionMensuelle.dateAFormater().getYear(), evolutionMensuelle.dateAFormater().getMonth()) + " : " + evolutionMensuelle.nombreEmprunts() + " emprunts | " + evolutionMensuelle.nombreReservations() + " réservations")

        );

    }

    void showTop5BestBooks(@NonNull Map<Livre, Long> top5Livres) {

        AtomicInteger i = new AtomicInteger(0);

        menuView.showLine(TextModifier.colorText(TextModifier.CYAN, "TOP 5 DES LIVRES DE TOUTE PÉRIODE : "));
        top5Livres.forEach((livre, nbrEmprunts) ->

                menuView.showLine(i.incrementAndGet() + ". \"" + standariserTitre(livre) + "\" - " + nbrEmprunts + " emprunts")

        );

    }

    // Menu 2 - Statistiques mensuelles

    public void showMonthlyStatistics(@NonNull YearMonth moisActuel, @NonNull StatistiquesMensuelles statistiquesMensuelle) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ORANGE, "STATISTIQUES MENSUELLES") + " - " + moisActuel.getMonth() + " " + moisActuel.getYear());
        menuView.showMiddleBorder();
        menuView.showLine();

        // Activités du mois

        showMonthActivity(statistiquesMensuelle);
        menuView.showLine();

        // Tendances hebdomadaires

        showWeeklyTrends(statistiquesMensuelle.tendanceHebdomadaire());
        menuView.showLine();

        // Top 5 catégories

        showTopCategories(statistiquesMensuelle.top5Categories());
        menuView.showLine();

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    void showMonthActivity(@NonNull StatistiquesMensuelles statistiquesMensuelle) {

        menuView.showLine(TextModifier.colorText(TextModifier.JAUNE, "ACTIVITÉ DU MOIS :"));
        menuView.showLine("• Emprunts : " + statistiquesMensuelle.nombreEmpruntsDuMois());
        menuView.showLine("• Nouveaux membres : " + statistiquesMensuelle.nouveauxMembres());
        menuView.showLine("• Retours : " + statistiquesMensuelle.retours());
        menuView.showLine("• Réservations : " + statistiquesMensuelle.reservations());
        menuView.showLine("• Taux de rotation : " + statistiquesMensuelle.tauxRotation());
        menuView.showLine("• Pénalités : " + statistiquesMensuelle.penalitesDuMois());

    }

    void showWeeklyTrends(@NonNull Map<DayOfWeek, Long> tendanceHebdomadaire) {

        menuView.showLine(TextModifier.colorText(TextModifier.ROUGE, "TENDANCE HEBDOMADAIRE :"));
        tendanceHebdomadaire.forEach((jourSemaine, nombreEmprunts) ->

                menuView.showLine(jourSemaine + " : " + nombreEmprunts)

        );

    }

    void showTopCategories(@NonNull Map<CategorieLivre, Long> top5Categories) {

        AtomicInteger i = new AtomicInteger(0);

        menuView.showLine(TextModifier.colorText(TextModifier.ROSE, "TOP 5 CATÉGORIES :"));
        top5Categories.forEach((categorie, nombreEmprunts) ->

                menuView.showLine(i.incrementAndGet() + ". " + categorie + " : " + nombreEmprunts + " emprunts")

        );

    }

    // Menu 3 - Popularité des livres

    public void showBooksPopularity(@NonNull RapportPopularite rapportPopularite) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROSE, "RAPPORT DE POPULARITÉ DES LIVRES"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Top 3 des livres empruntés

        showTop10BoworredBooks(rapportPopularite.topLivresPlusEmprunte());
        menuView.showLine();

        // Analyse par catégorie

        showAnalyseByCategories(rapportPopularite.analyseParCategorie());
        menuView.showLine();

        // Livres sous utilisés

        showBooksUnderUtilized(rapportPopularite.livresSousUtilises());
        menuView.showLine();

        // Tendances emergentes

        showNewTrends(rapportPopularite.tendancesEmergentes());

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    void showTop10BoworredBooks(@NonNull TopLivresPlusEmpruntes top10) {

        // Variables

        AtomicInteger livreI = new AtomicInteger();
        AtomicInteger i = new AtomicInteger(0);

        menuView.showLine(TextModifier.colorText(TextModifier.BLEU, "TOP 10 DES LIVRES LES PLUS EMPRUNTÉS :"));
        menuView.showLine();
        top10.nombreEmpruntsParLivre().forEach((livre, nombreEmprunts) -> {

            menuView.showLine(i.incrementAndGet() + ". \"" + standariserTitre(livre) + "\"" + " - " + nombreEmprunts + " emprunts | Taux : " + top10.taux().get(livreI.get()));

            livreI.getAndIncrement();

        });

    }

    void showAnalyseByCategories(@NonNull Map<CategorieLivre, Double> analyseParCategorie) {

        menuView.showLine(TextModifier.colorText(TextModifier.VERTCLAIR, "ANALYSE PAR CATÉGORIE :"));
        analyseParCategorie.forEach((categorie, tauxEmprunts) ->

            menuView.showLine("• " + categorie + " : " + tauxEmprunts + "%")

        );

    }

    void showBooksUnderUtilized(@NonNull Map<Livre, Long> livresSousUtilises) {

        menuView.showLine(TextModifier.colorText(TextModifier.SOULIGNE, TextModifier.colorText(TextModifier.ROUGE, "LIVRES SOUS-UTILISÉS :")));
        livresSousUtilises.forEach((key, value) ->

                menuView.showLine("• \"" + standariserTitre(key) + "\" - " + value + " emprunts")

        );

    }

    void showNewTrends(@NonNull Map<CategorieLivre, Double> tendancesEmergentes) {

        menuView.showLine(TextModifier.colorText(TextModifier.MAGENTA, "TENDANCES EMERGENTES :"));
        tendancesEmergentes.forEach((categorie, taux) ->

            menuView.showLine("• " + categorie + " - Taux d'emergence : " + taux + "%")

        );

    }

    // Menu 4 - Activités des membres

    public void showMembersActivities(@NonNull YearMonth moisActuel, @NonNull ActivitesMembres activitesMembres) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.MAGENTA, "ACTIVITÉS DES MEMBRES") + " - " + moisActuel.getMonth() + " " + moisActuel.getYear());
        menuView.showMiddleBorder();
        menuView.showLine();

        // SnapShot du mois

        showMonthlySnapShots(activitesMembres);
        menuView.showLine();

        // Top 5 membres les plus actifs

        showTopMembers(activitesMembres.top5MembresLesPlusActifs());
        menuView.showLine();

        // Variations

        showVariations(activitesMembres.variationEmprunts(), activitesMembres.variationRetards());
        menuView.showLine();

        // Jours de pic d'activités

        showPeakActivitiesDays(activitesMembres.jourPicActivites());

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    void showMonthlySnapShots(@NonNull ActivitesMembres activitesMembres) {

        Periode periode = activitesMembres.periode();

        menuView.showLine(TextModifier.colorText(TextModifier.ORANGE, "Période : ") + periode.dateDebut().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " - " + periode.dateFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        menuView.showLine("• Membres actifs : " + activitesMembres.totalMembresActifs());
        menuView.showLine("• Nouveaux membres : " + activitesMembres.nouveauMembres());
        menuView.showLine("• Emprunts effectués : " + activitesMembres.empruntsEffectues());
        menuView.showLine("• Moyenne d'emprunts par membre : " + activitesMembres.moyenne());
        menuView.showLine("• Retours : " + activitesMembres.retours());
        menuView.showLine("• Renouvellements : " + activitesMembres.renouvellements());
        menuView.showLine("• Réservations actives : " + activitesMembres.reservationsActives());
        menuView.showLine("• Réservations annulées : " + activitesMembres.reservationAnnule());
        menuView.showLine("• Pénalités générées : " + activitesMembres.penaliteGenerees());

    }


    void showTopMembers(@NonNull Map<Membre, Long> top5Membres) {

        AtomicInteger i = new AtomicInteger(0);

        menuView.showLine(TextModifier.colorText(TextModifier.BLEU, "TOP 5 MEMBRES ACTIFS :"));
        top5Membres.forEach((membre, nombreEmprunts) ->

                menuView.showLine(i.incrementAndGet() + ". " + standariserNomMembre.apply(membre) + " : " + nombreEmprunts + " emprunts")

        );

    }

    void showVariations(double variationEmprunts, double variationRetards) {

        menuView.showLine(TextModifier.colorText(TextModifier.JAUNE, "VARIATIONS :"));
        menuView.showLine("• Emprunts : " + variationEmprunts + "%");
        menuView.showLine("• Retards : " + variationRetards + "%");

    }

    void showPeakActivitiesDays(@NonNull List<DayOfWeek> jourPicActivites) {

        menuView.showLine(TextModifier.colorText(TextModifier.CYAN, "JOURS DE PIC D'ACTIVITÉ :"));
        menuView.showLine("I- " + jourPicActivites.getFirst() + " II- " + jourPicActivites.get(1) + " III- " + jourPicActivites.getLast());

    }

    // Menu 5 - Membres inactifs

    public void showRiskyMembers(@NonNull List<Membre> membresInactif) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "MEMBRES INACTIFS"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine(TextModifier.colorText(TextModifier.ROUGE, "MEMBRES INACTIFS :"));
        membresInactif.forEach(membre ->

            menuView.showLine("• " + standariserNomMembre.apply(membre) + " - Date d'inscription : " + membre.getDateInscription().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))

        );

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.CYAN, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

}
