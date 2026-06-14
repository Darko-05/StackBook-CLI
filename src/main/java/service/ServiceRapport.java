package service;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import main.AppConfig;
import model.*;
import registre.Emprunts;
import registre.Livres;
import registre.Membres;
import registre.Reservations;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static util.ParsingUtil.toLinkedHashMap;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceRapport implements IReportService {

    // Instance unique

    private static ServiceRapport INSTANCE;

    // Attributs registre

    @NonNull private final Livres registreLivres;
    @NonNull private final Membres registreMembres;
    @NonNull private final Emprunts registreEmprunts;
    @NonNull private final Reservations registreReservations;

    // Service d'emprunt

    @NonNull private final ServiceEmprunt serviceEmprunt;

    // Getter

    public static ServiceRapport getInstance() {

        if (INSTANCE == null) INSTANCE = new ServiceRapport(
        AppConfig.registreLivres,
        AppConfig.registreMembres,
        AppConfig.registreEmprunts,
        AppConfig.registreReservations,
        AppConfig.serviceEmprunt
        );

        return INSTANCE;

    }

    // Redefinitions

    @Override
    public StatistiquesGlobal genererStatistiquesGlobal(long stockInitial) {

        // Stock total de livres

        long stockTotalDeLivres = registreLivres.getLivres().size();

        // Emprunts cumules

        long empruntsCumules = registreEmprunts.getEmprunts().size();

        /* Nombre de livres actuel */

        long nombreActuel = registreLivres.getLivres().values().stream()
        .mapToLong(Livre::getNombreExemplaire)
        .sum();

        /* Stock moyen */

        double stockMoyen = (double) (stockInitial + nombreActuel) / 2;

        // Taux de rotation global

        double tauxDeRotationGlobal = (double) empruntsCumules / stockMoyen;

        // Membres actif

        long membreActifs = membresActif();

        // Livres disponible

        long nombreDeLivresDisponible = nombreDeLivresDisponible();

        // Nombre total de retours

        long nombreTotalDeRetours = nombreTotalDeRetours();

        // Reservations actives

        long reservationActives = reservationsActives();

        // Taux de reservation

        double tauxDeReservation = /* Nombre de livres réserver */ (double) registreReservations.getReservations().size() / stockTotalDeLivres;

        // Penalite total

        long penaliteTotal = penaliteTotal();

        // Evolutions mensuelles

        List<EvolutionMensuelle> evolutionMensuellesDes3DerniersMois = evolutionMensuelles();

        // Top 5 des livres

        Map<Livre, Long> top5Livres = top5Livres();

        // Retourne le record des stats global

        return new StatistiquesGlobal(stockTotalDeLivres, empruntsCumules, tauxDeRotationGlobal, membreActifs, nombreDeLivresDisponible, nombreTotalDeRetours, reservationActives, tauxDeReservation, penaliteTotal, evolutionMensuellesDes3DerniersMois, top5Livres);

    }

    @Override
    public StatistiquesMensuelles genererStatistiquesMensuelles(LocalDate dateLancement) {

        // Emprunts du mois

        long nombreEmpruntsDuMois = empruntsDuMois();

        // Nouveaux membres

        long nouveauxMembres = nouveauxMembres();

        // Retours du mois

        long retoursDuMois = retoursDuMois();

        // Reservations du mois

        long reservationsDuMois = reservationsDuMois();

        // Taux de rotation

        double tauxDeRotation = tauxDeRotation(dateLancement);

        // Penalites du mois

        long penalitesDuMois = penalitesDuMois();

        // Tendances hebdomadaire

        Map<DayOfWeek, Long> tendancesHebdomadaire = tendancesHebdomadaire();

        // Top 5 categories (nombre d'emprunts par categorie)

        Map<CategorieLivre, Long> top5Categories = top5Categories();

        // Top 5 membres Actifs

        /* 10 sont renvoyer */ Map<Membre, Long> top5MembresActifs = top5MembresActifs();

        return new StatistiquesMensuelles(nombreEmpruntsDuMois, nouveauxMembres, retoursDuMois, reservationsDuMois, tauxDeRotation, penalitesDuMois, tendancesHebdomadaire, top5Categories, top5MembresActifs);

    }

    @Override
    public RapportPopularite genererRapportPopularite() {

        TopLivresPlusEmpruntes top10LivresPlusEmpruntes = top10LivresPlusEmpruntes();

        Map<CategorieLivre, Double> analyseParCategorie = analyseParCategorie();

        Map<Livre, Long> livresSousUtilise = livresSousUtilise();

        Map<CategorieLivre, Double> tendancesEmergentes = tendancesEmergentes();

        return new RapportPopularite(top10LivresPlusEmpruntes, analyseParCategorie, livresSousUtilise, tendancesEmergentes);

    }

    @Override
    public ActivitesMembres genererRapportActivitesMembres(LocalDate dateLancement) {

        Periode periode = periode(dateLancement);

        long totalMembresActifs= totalMembresActifs();

        long nouveauxMembres = nouveauxMembres();

        long empruntsEffectues = empruntsDuMois();

        // Nombre d'emprunts du mois / Le nombre de jours depuis le debut du mois

        double moyenne = (double) empruntsEffectues / LocalDate.now().getDayOfMonth();

        long retours = retoursDuMois();

        long renouvellements = renouvellementsDuMois();

        long reservationsActives = reservationsActivesDuMois();

        long reservationsAnnules = reservationsAnnuleDuMois();

        long penalitesGenerer = penalitesDuMois();

        Map<Membre, Long> top5MembresActifsPenalites = top5MembresActifsPenalites();

        double variationEmpunts = variationEmprunts();

        double variationRetards = variationRetards();

        List<DayOfWeek> joursPicsActivites = joursPicsActivites();

        List<Membre> membresInactifs = membresInactifs();

        return new ActivitesMembres(periode, totalMembresActifs, nouveauxMembres, empruntsEffectues, moyenne, retours, renouvellements, reservationsActives, reservationsAnnules, penalitesGenerer, top5MembresActifsPenalites, variationEmpunts, variationRetards, joursPicsActivites, membresInactifs);

    }

    // Méthodes utilitaire - Activités membres

    Periode periode(LocalDate dateLancement) {

        // Retourne la periode du debut du programme a audjourd'hui

        return new Periode(dateLancement, LocalDate.now());

    }

    long totalMembresActifs() {

        // Retourne le total des membres qu'ils soit actif ou retardataire

        return registreMembres.getMembres().values().stream()
        .filter(membre -> membre.getStatut() == StatutMembre.ACTIF || membre.getStatut() == StatutMembre.RETARDATAIRE)
        .count();

    }

    long renouvellementsDuMois() {

        return registreEmprunts.getEmprunts().values().stream()
        .filter(emprunt -> YearMonth.from(emprunt.getDateEmprunt()).equals(YearMonth.now()))
        .filter(emprunt -> emprunt.getNombreRenouvellement() > 0)
        .count();

    }

    long reservationsActivesDuMois() {

        return registreReservations.getReservations().stream()
        .filter(reservation -> YearMonth.from(reservation.getDateReservation()).equals(YearMonth.now()))
        .filter(reservation -> reservation.getEtatReservation() == EtatReservation.EN_COURS)
        .count();

    }

    long reservationsAnnuleDuMois() {

        return registreReservations.getReservations().stream()
        .filter(reservation -> YearMonth.from(reservation.getDateReservation()).equals(YearMonth.now()))
        .filter(reservation -> reservation.getEtatReservation() == EtatReservation.ANNULE)
        .count();

    }

    Map<Membre, Long> top5MembresActifsPenalites() {

        return top5MembresActifs().entrySet().stream()
        .collect(Collectors.toMap(

            // Le membre

            Map.Entry::getKey,

            // Le total de pénalités du membre sur le mois

            cle_valeur -> 500 * cle_valeur.getKey().getEmpruntsActif()
            .stream().filter(emprunt -> YearMonth.from(emprunt.getDateEmprunt()).equals(YearMonth.now()))
            .filter(emprunt -> emprunt.getDateRetourPrevu().isBefore(LocalDate.now()) && emprunt.getDateRetourEffective() == null)
            .count(),

            // En cas de valeur identique

            (a, _) -> a,

            // LinkedHashMap pour l'ordre

            LinkedHashMap::new

        ));

    }

    double variationEmprunts() {

        // Emprunts du mois passée

        long empruntsMoisPasse = registreEmprunts.getEmprunts().values().stream()
        .filter(emprunt -> YearMonth.from(emprunt.getDateEmprunt()).equals(YearMonth.now().minusMonths(1)))
        .count();

        // Retourne la variation du taux d'emprunts

        if (empruntsMoisPasse > 0) return ((double) (empruntsDuMois() - empruntsMoisPasse) / empruntsMoisPasse) * 100;

        // Nouvelles activités = 0

        return 0;

    }

    double variationRetards() {

        // Retards du mois passée

        long retardsDuMoisPasse = registreEmprunts.getEmprunts().values().stream()
        .filter(emprunt -> YearMonth.from(emprunt.getDateEmprunt()).equals(YearMonth.now().minusMonths(1)))
        .filter(emprunt -> emprunt.getEtatEmprunt() == EtatEmprunt.EN_RETARD)
        .count();

        // Retards de ce mois

        long retardsDuMois = registreEmprunts.getEmprunts().values().stream()
        .filter(emprunt -> YearMonth.from(emprunt.getDateEmprunt()).equals(YearMonth.now()))
        .filter(emprunt -> emprunt.getEtatEmprunt() == EtatEmprunt.EN_RETARD)
        .count();

        // Retourne la variation du taux de retards

        if (retardsDuMoisPasse > 0) return ((double) (retardsDuMois - retardsDuMoisPasse) / retardsDuMoisPasse) * 100;

        // Nouvelles activités = 0

        return 0;

    }

    List<DayOfWeek> joursPicsActivites() {

        return registreEmprunts.getEmprunts().values().stream()
        .filter(emprunt -> YearMonth.from(emprunt.getDateEmprunt()).equals(YearMonth.now()))
        .collect(Collectors.groupingBy(

            // Le jour de la semaine

            emprunt -> emprunt.getDateEmprunt().getDayOfWeek(),

            // Le nombre d'emprunts

            Collectors.counting()

        ))
        .entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
        .limit(3)
        .map(Map.Entry::getKey)
        .toList();

    }

    List<Membre> membresInactifs() {

        // Renvoie les membres dans l'ordre de date d'inscription qui n'ont jamais fait d'emprunts

        return registreMembres.getMembres().values().stream()
        .filter(membre -> membre.getHistoriqueEmprunts().isEmpty())
        .sorted(Comparator.comparing(Membre::getDateInscription))
        .toList();

    }

    // Méthodes utilitaire - Rapport popularité

     TopLivresPlusEmpruntes top10LivresPlusEmpruntes() {

        // Nombre d'emprunts par livres

        Map<Livre, Long> nombreEmpruntsParLivre = registreEmprunts.getEmprunts().values().stream()
        .collect(Collectors.groupingBy(

            // Le livre

            Emprunt::getLivre,

            // Le nombre d'emprunts

            Collectors.counting()

        ))
        .entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
        .limit(10)
        .collect(toLinkedHashMap());

        // Nombre maximal d'emprunts possible

         long nombreEmpruntsMaximal = registreLivres.getLivres().values().stream()
         .mapToLong(Livre::getNombreExemplaire)
         .sum();

         // Les taux par livres

         List<Double> taux = nombreEmpruntsParLivre.values().stream()
         .map(nombreEmprunt -> (double) nombreEmprunt / nombreEmpruntsMaximal)
         .toList();

         // Retourne le top

         return new TopLivresPlusEmpruntes(nombreEmpruntsParLivre, taux);

    }

    Map<CategorieLivre, Double> analyseParCategorie() {

        // Grouper nombre d'emprunts par categorie

        return registreEmprunts.getEmprunts().values().stream()
        .collect(Collectors.groupingBy(

            // La categorie

            emprunt -> emprunt.getLivre().getCategorie(),

            // Le nombre d'emprunts

            Collectors.counting()

        ))
        .entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
        .collect(Collectors.toMap(

            // La categorie

            Map.Entry::getKey,

            // Nombre d'emprunts de la categorie / Nombre total d'emprunts

            valeur -> (double) valeur.getValue() / registreEmprunts.getEmprunts().size(),

            // En cas de doublons

            (a, _) -> a,

            // LinkedHasMap pour l'ordre

            LinkedHashMap::new

        ));

    }

    Map<Livre, Long> livresSousUtilise() {

        return registreEmprunts.getEmprunts().values().stream()
        .collect(Collectors.groupingBy(

            // Le livre

            Emprunt::getLivre,

            // Le nombre d'emprunts

            Collectors.counting()

        ))
        .entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue())
        .collect(toLinkedHashMap());

    }

    Map<CategorieLivre, Double> tendancesEmergentes() {

        return registreEmprunts.getEmprunts().values().stream()
        .filter(emprunt -> YearMonth.from(emprunt.getDateEmprunt()).equals(YearMonth.now()))
        .collect(Collectors.groupingBy(

            // Categorie

            emprunt -> emprunt.getLivre().getCategorie(),

            // Nombre d'emprunts

            Collectors.counting()

        ))
        .entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
        .collect(Collectors.toMap(

            // Categorie

            Map.Entry::getKey,

            // Le taux d'emergence = (nombre d'emprunts de ce mois / celui du mois dernier) - 1 * 100

            cle_valeur -> (((double)

            cle_valeur.getValue() / registreEmprunts.getEmprunts().values().stream()
            .filter(emprunt -> YearMonth.from(emprunt.getDateEmprunt()).equals(YearMonth.now().minusMonths(1)))
            .count()) - 1) * 100,

            // En case de deux valeur identique

            (a, _) -> a,

            // LinkedHashMap pour l'ordre

            LinkedHashMap::new

        ));

    }

    // Méthodes utilitaire - Statistiques mensuelles

    long empruntsDuMois() {

        // Recuperer le mois actuel

        YearMonth currentMonth = YearMonth.now();

        // Parcourir le registre d'emprunt et compter le nombre d'emprunts du mois

        return registreEmprunts.getEmprunts().values().stream()
        .filter(emprunt -> YearMonth.from(emprunt.getDateEmprunt()).equals(currentMonth))
        .count();

    }

    long nouveauxMembres() {

        // Recuperer le mois actuel

        YearMonth currentMonth = YearMonth.now();

        // Parcourir le registre des membres et compter le nombre de nouveaux membres du mois

        return registreMembres.getMembres().values().stream()
        .filter(membre -> YearMonth.from(membre.getDateInscription()).equals(currentMonth))
        .count();

    }

    long retoursDuMois() {

        // Recuperer le mois actuel

        YearMonth currentMonth = YearMonth.now();

        // Parcours le registre d'emprunts pour compter les retours du mois

        return registreEmprunts.getEmprunts().values().stream()
        .filter(emprunt -> YearMonth.from(emprunt.getDateRetourEffective()).equals(currentMonth))
        .count();

    }

    long reservationsDuMois() {

        // Recuperer le mois actuel

        YearMonth currentMonth = YearMonth.now();

        // Parcourir le registre de reservations pour compter les reservations du mois

        return registreReservations.getReservations().stream()
        .filter(reservation -> YearMonth.from(reservation.getDateReservation()).equals(currentMonth))
        .count();

    }

    double tauxDeRotation(LocalDate dateLancementSysteme) {

        // Recuperer le mois actuel

        YearMonth currentMonth = YearMonth.now();

        // Date de debut du mois

        LocalDate dateDebutMois = currentMonth.atDay(1);

        // Date de fin du mois

        LocalDate dateFinMois = currentMonth.atEndOfMonth();

        // Stock moyen de livres = (stock au debut du mois + stock a la fin du mois) / 2

        double stockMoyenDeLivres =  (double)

        (registreLivres.getLivres().values().stream()
        .filter(livre -> (livre.getDateAjout().isEqual(dateLancementSysteme) || livre.getDateAjout().isAfter(dateLancementSysteme)) && (livre.getDateAjout().isEqual(dateDebutMois) || livre.getDateAjout().isBefore(dateDebutMois)))
        .count()

        +

        registreLivres.getLivres().values().stream()
        .filter(livre -> (livre.getDateAjout().isEqual(dateLancementSysteme) || livre.getDateAjout().isAfter(dateLancementSysteme)) && (livre.getDateAjout().isEqual(dateFinMois) || livre.getDateAjout().isBefore(dateFinMois)))
        .count())

        /

        2;

        return empruntsDuMois() / stockMoyenDeLivres;

    }

    long penalitesDuMois() {

        // Recuperer le mois actuel

        YearMonth currentMonth = YearMonth.now();

        // Recuperer les emprunts du mois et dont la date de retour prevu est avant le moment ou en calcule les penalites

        return 500 * registreEmprunts.getEmprunts().values().stream()
        .filter(emprunt -> YearMonth.from(emprunt.getDateEmprunt()).equals(currentMonth))
        .filter(emprunt -> emprunt.getDateRetourPrevu().isBefore(LocalDate.now()))
        .filter(emprunt -> emprunt.getDateRetourEffective() == null)
        .count();

    }

    Map<DayOfWeek, Long> tendancesHebdomadaire() {

        // Recuperer le mois actuel

        YearMonth currentMonth = YearMonth.now();

        // Groupe le nombre d'emprunts par jour de la semaine

        Map<DayOfWeek, Long> resultat = registreEmprunts.getEmprunts().values().stream()
        .filter(emprunt -> YearMonth.from(emprunt.getDateEmprunt()).equals(currentMonth))
        .collect(Collectors.groupingBy(

            // Jours de la semaine

            emprunt -> emprunt.getDateEmprunt().getDayOfWeek(),

            // Nombre d'emprunts

            Collectors.counting()

        ));

        // Si pour un jour on a 0 emprunt

        for (DayOfWeek day : DayOfWeek.values()) {

            // Ajoute le jour d'emprunt et son nombre d'emprunt a 0

            resultat.putIfAbsent(day, 0L);

        }

        // Retourne le resultat

        return resultat;

    }

    Map<CategorieLivre, Long> top5Categories() {

        // Recuperer le mois actuel

        YearMonth currentMonth = YearMonth.now();

        // Groupe le nombre d'emprunts par categorie de livres

        return registreEmprunts.getEmprunts().values().stream()
        .filter(emprunt -> YearMonth.from(emprunt.getDateEmprunt()).equals(currentMonth))
        .collect(Collectors.groupingBy(

            // Categorie du livre

            emprunt -> emprunt.getLivre().getCategorie(),

            // Nombre d'emprunts

            Collectors.counting()

        ))
        .entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
        .limit(5)
        .collect(toLinkedHashMap());

    }

    Map<Membre, Long> top5MembresActifs() {

        // Recuperer le mois actuel

        YearMonth currentMonth = YearMonth.now();

        // Grouper les membres par le nombre d'emprunts qu'ils auraient fait durant le mois

        return registreEmprunts.getEmprunts().values().stream()
        .filter(emprunt -> YearMonth.from(emprunt.getDateEmprunt()).equals(currentMonth))
        .collect(Collectors.groupingBy(

            // Le membre

            Emprunt::getMembre,

            // Le nombre d'emprunts

            Collectors.counting()

        ))
        .entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
        .limit(10) // 5 a recuperer
        .collect(toLinkedHashMap());

    }

    // Méthodes utilitaire - Statistiques Global

    long membresActif() {

        Predicate<Membre> estActif = membre -> membre.getStatut() == StatutMembre.ACTIF;
        Predicate<Membre> estRetardataire = membre -> membre.getStatut() == StatutMembre.RETARDATAIRE;

        Predicate<Membre> membreActif = estActif.or(estRetardataire);

         return registreMembres.getMembres().values().stream()
        .filter(membreActif)
        .count();

    }

    long nombreDeLivresDisponible() {

        return registreLivres.getLivres().values().stream()
        .filter(Livre::estDisponible)
        .count();

    }

    long nombreTotalDeRetours() {

        return registreEmprunts.getEmprunts().values().stream()
        .filter(emprunt -> emprunt.getEtatEmprunt() == EtatEmprunt.RETOURNE)
        .count();

    }

    long reservationsActives() {

        return registreReservations.getReservations().stream()
        .filter(reservation -> reservation.getEtatReservation() == EtatReservation.EN_COURS)
        .count();

    }

    long penaliteTotal() {

        // Penalites total

        return registreMembres.getMembres().values().stream()
        .mapToLong(membre -> {

            /* Inutile de laisser en double car : nbrJours * 500 */

            return (long) serviceEmprunt.calculerPenalite(membre.getId());

        })
        .sum();

    }

    List<EvolutionMensuelle> evolutionMensuelles() {

        // Recuperer les 3 derniers mois

        List<YearMonth> last3Months = List.of(YearMonth.now(), YearMonth.now().minusMonths(1), YearMonth.now().minusMonths(2));

        // Parcours chaque 3 derniers mois - année

        return last3Months.stream()
        .map(month ->

        {

            // Recupère le nombre d'emprunts du mois

            long nombreEmprunts = registreEmprunts.getEmprunts().values().stream()
            .filter(emprunt -> YearMonth.from(emprunt.getDateEmprunt()).equals(month))
            .count();

            // Recupère le nombre de reservations du mois

            long nombreReservations = registreReservations.getReservations().stream()
            .filter(reservation -> YearMonth.from(reservation.getDateReservation()).equals(month))
            .count();

            // Retourne chaque evolution mensuelle

            return new EvolutionMensuelle(month.atEndOfMonth(), nombreEmprunts, nombreReservations);

        })
        .toList();

    }

    Map<Livre, Long> top5Livres() {

        // Top 5 des livres toute periode confondu

        return registreLivres.getLivres().values().stream()
        .collect(Collectors.toMap(

            // Livre

            livre -> livre,

            // Nombre d'emprunts

            livre -> registreEmprunts.getEmprunts().values().stream()
                    .filter(emprunt -> emprunt.getLivre().equals(livre))
                    .count()

        ))
        .entrySet().stream()
        .sorted(Map.Entry.<Livre, Long>comparingByValue().reversed())
        .limit(5)
        .collect(toLinkedHashMap());

    }

}