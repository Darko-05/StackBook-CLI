package service;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import main.AppConfig;
import model.*;
import static main.ApplicationLauncher.DATE_LANCEMENT;
import static registre.Livres.STOCK_INITIAL;

import registre.*;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static util.DisplayUtil.standariserTitre;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceImportExport implements ImportExportService {

    // Instance unique

    private static ServiceImportExport INSTANCE;

    // Attributs service

    @NonNull private final ServiceLivre serviceLivre;
    @NonNull private final ServiceMembre serviceMembre;
    @NonNull private final ServiceEmprunt serviceEmprunt;
    @NonNull private final ServiceReservation serviceReservation;
    @NonNull private final ServiceRapport serviceRapport;
    @NonNull private final ServiceAuteur serviceAuteur;
    @NonNull private final ServiceMaisonEdition serviceMaisonEdition;

    // Attributs registres

    @NonNull private final Livres registreLivres;
    @NonNull private final Membres registreMembres;
    @NonNull private final Auteurs registreAuteurs;
    @NonNull private final MaisonEditions registreMaisonEdition;

    // Date dernier import ou export

    @Getter @Setter private LocalDateTime dateDernierImport = LocalDateTime.of(0, 1, 1, 0, 0, 0);
    @Getter @Setter private LocalDateTime dateDernierExport = LocalDateTime.of(0, 1, 1, 0, 0, 0);

    // Getter

    public static ServiceImportExport getInstance() {

        if (INSTANCE == null) INSTANCE = new ServiceImportExport(
        AppConfig.serviceLivre,
        AppConfig.serviceMembre,
        AppConfig.serviceEmprunt,
        AppConfig.serviceReservation,
        AppConfig.serviceRapport,
        AppConfig.serviceAuteur,
        AppConfig.serviceMaisonEdition,
        AppConfig.registreLivres,
        AppConfig.registreMembres,
        AppConfig.registreAuteurs,
        AppConfig.registreMaisonEdition
        );

        return INSTANCE;

    }

    // Méthodes d'export

    @Override
    public void exporterLivres() {

        try(BufferedWriter bw = new BufferedWriter(new FileWriter("data/livres/livres.csv"))) {

            // Recuperer tout les livres du systeme

            List<Livre> lesLivres = serviceLivre.toutLesLivres();

            // En tete du fichier

            bw.write("Id;Isbn;Titre;Nom de l'auteur;Prenom de l'auteur;Date d'ajout;Maison d'editon;Année de publication;Categorie du livre;Etat du livre;Nombre d'exemplaires;Nombre d'exemplaires disponibles;Nombre d'emprunts totaux\n");

            // Construction de la chaine de chaque livre

            StringBuilder chaineLivres = new StringBuilder();

            // Construction

            lesLivres.forEach(livre -> chaineLivres

                .append(livre.getId())
                .append(";")
                .append(livre.getIsbn())
                .append(";")
                .append(standariserTitre(livre))
                .append(";")
                .append(formaterChaine.apply(livre.getAuteur().nom()))
                .append(";")
                .append(formaterChaine.apply(livre.getAuteur().prenom()))
                .append(";")
                .append(formaterDate.apply(livre.getDateAjout()))
                .append(";")
                .append(livre.getMaisonEdition().nom())
                .append(";")
                .append(livre.getAnneePublication())
                .append(";")
                .append(livre.getCategorie())
                .append(";")
                .append(livre.getEtatDuLivre())
                .append(";")
                .append(livre.getNombreExemplaire())
                .append(";")
                .append(livre.getNombreExemplaireDisponible())
                .append(";")
                .append(livre.getCompteurEmpruntTotaux())
                .append("\n")

            );

            bw.write(chaineLivres.toString());

            // Liberer la memoire

            bw.flush();

        }

        catch(IOException e) {

            log.error("Erreur : ", e);

            return;

        }

        // Actualiser la date du dernier export

        setDateDernierExport(LocalDateTime.now());

    }

    @Override
    public void exporterMembres() {

        try(BufferedWriter bw = new BufferedWriter(new FileWriter("data/membres/membres.csv"))) {

            // Recuperer les membres du systeme

            List<Membre> lesMembres = serviceMembre.toutLesMembres();

            // En tete de fichier

            bw.write("Id;Numero du membre;Nom;Prenom;Email;Numero de telephone;Date d'inscription;Statut du membre;Nombre d'emprunts en cours\n");

            // Construction de la chaine des membres

            StringBuilder chaineMembres = new StringBuilder();

            // Construction

            lesMembres.forEach(membre -> chaineMembres

                .append(membre.getId())
                .append(";")
                .append(membre.getNumeroMembre())
                .append(";")
                .append(formaterChaine.apply(membre.getNom()))
                .append(";")
                .append(formaterChaine.apply(membre.getPrenom()))
                .append(";")
                .append(membre.getEmail())
                .append(";")
                .append(membre.getNumeroDeTelephone())
                .append(";")
                .append(formaterDate.apply(membre.getDateInscription()))
                .append(";")
                .append(membre.getStatut())
                .append(";")
                .append(membre.getNombreEmpruntsEnCours())
                .append("\n")

            );

            bw.write(chaineMembres.toString());

            // Vider le buffer

            bw.flush();

        }

        catch (IOException e) {

            log.error("Erreur : ", e);

            return;

        }

        // Actualiser la date du dernier export

        setDateDernierExport(LocalDateTime.now());

    }

    @Override
    public void exporterEmprunts() {

        try(BufferedWriter bw = new BufferedWriter(new FileWriter("data/emprunts/emprunts.csv"))) {

            // Recuperer les emprunts

            List<Emprunt> lesEmprunts = serviceEmprunt.toutLesEmprunts();

            // En tete du fichier

            bw.write("Id de l'emprunt;Id du livre;Titre du livre;Id du membre;Nom du membre;Prenom du membre;Date de l'emprunt;Date de retour prévu;Date de retour effective;Statut de l'emprunt;Penalités;Nombre de renouvellements\n");

            // Construction de la chaine d'emprunts

            StringBuilder chaineEmprunts = new StringBuilder();

            // Construction

            lesEmprunts.forEach(emprunt ->

                 chaineEmprunts
                .append(emprunt.getId())
                .append(";")
                .append(emprunt.getLivre().getId())
                .append(";")
                .append(standariserTitre(emprunt.getLivre()))
                .append(";")
                .append(emprunt.getMembre().getId())
                .append(";")
                .append(formaterChaine.apply(emprunt.getMembre().getNom()))
                .append(";")
                .append(formaterChaine.apply(emprunt.getMembre().getPrenom()))
                .append(";")
                .append(formaterDate.apply(emprunt.getDateEmprunt()))
                .append(";")
                .append(formaterDate.apply(emprunt.getDateRetourPrevu()))
                .append(";")
                .append(formaterDate.apply(emprunt.getDateRetourEffective()))
                .append(";")
                .append(emprunt.getEtatEmprunt())
                .append(";")

                 /* Méthode qui lève l'exeption */

                 .append(serviceEmprunt.calculerPenalite(emprunt.getMembre().getId()))

                 /* Méthode qui lève l'exeption */

                .append(";")
                .append(emprunt.getNombreRenouvellement())
                .append("\n")

            );

            bw.write(chaineEmprunts.toString());

            // Vider le buffer

            bw.flush();

        }

        catch (IOException e) {

            log.error("Erreur : ", e);

            return;

        }

        // Actualiser la date du dernier export

        setDateDernierExport(LocalDateTime.now());

    }

    @Override
    public void exporterReservations() {

        try(BufferedWriter bw = new BufferedWriter(new FileWriter("data/reservations/reservations.csv"))) {

            // Recuperer tout les reservations

            List<Reservation> lesReservations = serviceReservation.toutLesReservations();

            // En tete du fichier

            bw.write("Id;Id du Livre;Titre du Livre;Id du Membre;Nom du Membre;Prenom du Membre;Date de Reservation;Date d'expriration;Etat de la Reservation\n");

            // Construction de la chaine de reservations

            StringBuilder chaineReservation = new StringBuilder();

            // Construction

            lesReservations.forEach(reservation -> chaineReservation

                .append(reservation.getIdReservation())
                .append(";")
                .append(reservation.getLivre().getId())
                .append(";")
                .append(standariserTitre(reservation.getLivre()))
                .append(";")
                .append(reservation.getMembre().getId())
                .append(";")
                .append(formaterChaine.apply(reservation.getMembre().getNom()))
                .append(";")
                .append(formaterChaine.apply(reservation.getMembre().getPrenom()))
                .append(";")
                .append(formaterDate.apply(reservation.getDateReservation()))
                .append(";")
                .append(formaterDate.apply(reservation.getDateExpiration()))
                .append(";")
                .append(reservation.getEtatReservation())

            );

            bw.write(chaineReservation.toString());

            // Vider le buffer

            bw.flush();

        }

        catch (IOException e) {

            log.error("Erreur : ", e);

            return;

        }

        // Actualiser la date du dernier export

        setDateDernierExport(LocalDateTime.now());

    }

    @Override
    public void exporterAuteurs() {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/auteurs/auteurs.csv"))) {

            // Recuperer tout les auteurs

            List<Auteur> lesAuteurs = serviceAuteur.toutLesAuteurs();

            // En tete du fichier

            bw.write("Nom;Prenom;Nationalite;Biographie\n");

            // Construction de la chaine d'auteurs

            StringBuilder chaineAuteurs = new StringBuilder();

            // Construction

            lesAuteurs.forEach(auteur -> chaineAuteurs

                    .append(formaterChaine.apply(auteur.nom()))
                    .append(";")
                    .append(formaterChaine.apply(auteur.prenom()))
                    .append(";")
                    .append(auteur.nationalite())
                    .append(";")
                    .append(auteur.biographie())
                    .append("\n")

            );

            // Ecrire la chaine

            bw.write(chaineAuteurs.toString());

            // Vider le buffer

            bw.flush();

        }

        catch (IOException e) {

            log.error("Erreur : ", e);

            return;

        }

        // Actualiser la date du dernier export

        setDateDernierExport(LocalDateTime.now());

    }

    @Override
    public void exporterMaisonEdition() {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/maisonEdition/maisonsEdition.csv"))) {

            // Recuperer tout les auteurs

            List<MaisonEdition> lesMaisonEdition = serviceMaisonEdition.toutLesMaisonEdition();

            // En tete du fichier

            bw.write("Nom;Code Postal;Numero de Telephone;Ville;Date de Creation\n");

            // Construction de la chaine d'auteurs

            StringBuilder chaineMaisonEdition = new StringBuilder();

            // Construction

            lesMaisonEdition.forEach(maisonEdition -> chaineMaisonEdition

                .append(formaterChaine.apply(maisonEdition.nom()))
                .append(";")
                .append(maisonEdition.codePostal())
                .append(";")
                .append(maisonEdition.numeroDeTelephone())
                .append(";")
                .append(maisonEdition.ville())
                .append(";")
                .append(formaterDate.apply(maisonEdition.dateDeCreation()))
                .append("\n")

            );

            // Ecrire la chaine

            bw.write(chaineMaisonEdition.toString());

            // Vider le buffer

            bw.flush();

        }

        catch (IOException e) {

            log.error("Erreur : ", e);

            return;

        }

        // Actualiser la date du dernier export

        setDateDernierExport(LocalDateTime.now());

    }

    @Override
    public void exporterStatistiquesGlobal() {

        try(BufferedWriter bw = new BufferedWriter(new FileWriter("data/statistiques/statistiques_Global.csv"))) {

            // Première section

            bw.write("Section;Stock total de livres;Livres disponibles;Emprunts cumulés;Reservations actives;Taux de rotation global;Taux de reservation;Membres actifs;Pénalités totales\n");

            bw.write(activitesGlobal());

            // Deuxième section

            bw.newLine();

            bw.write("Section;Mois;Emprunts;Reservations\n");

            bw.write(evolutionMensuelle());

            // Troisième section

            bw.newLine();

            bw.write("Section;Top 5 des livres;Nombres d'emprunts\n");

            bw.write(top5Livres());

            // Vider le buffer

            bw.flush();

        }

        catch (IOException e) {

            log.error("Erreur : ", e);

            return;

        }

        // Actualiser la date du dernier export

        setDateDernierExport(LocalDateTime.now());

    }

    @Override
    public void exporterStatistiquesMensuelles() {

        try(BufferedWriter bw = new BufferedWriter(new FileWriter("data/statistiques/statistiques_Mensuelles.csv"))) {

            // Première section

            bw.write("Section;Activités mensuelle;Nombre d'emprunts;Retours;Taux de rotation;Nouveaux membres;Reservations;Pénalités\n");

            bw.write(activitesMensuel());

            // Deuxième section

            bw.newLine();

            bw.write("Section;Tendances hebdomadaire,Nombre d'emprunts\n");

            bw.write(tendancesHebdomadaire());

            // Troisième section

            bw.newLine();

            bw.write("Section;Top 5 catégories;Nombre d'emprunts\n");

            bw.write(top5Categories());

            // Quatrième section

            bw.newLine();

            bw.write("Section;Top 5 membres;Nombre d'emprunts\n");

            bw.write(top5Membres());

        }

        catch (IOException e) {

            log.error("Erreur : ",e);

            return;

        }

        // Actualiser la date du dernier export

        setDateDernierExport(LocalDateTime.now());

    }

    @Override
    public void toutExporter() {

        // Appel des fonctions d'exports

        exporterAuteurs();
        exporterEmprunts();
        exporterLivres();
        exporterMembres();
        exporterReservations();
        exporterMaisonEdition();
        exporterStatistiquesGlobal();
        exporterStatistiquesMensuelles();

    }

    // Méthodes d'imports

    @Override
    public void importerMaisonEdition(FileReader chemin) {

        try(BufferedReader br = new BufferedReader(chemin)) {

            String line;

            // Consommer la première ligne

            br.readLine();

            // Parcour le fichier

            while ((line = br.readLine()) != null) {

                // Si la ligne est non vide

                if (!line.trim().isEmpty()) {

                    // Construire la maison d'edition importé

                    MaisonEdition maisonEditionImporter = construireMaisonEdition(line);

                    // Ajoute la maison d'edition au registre

                    registreMaisonEdition.ajouterMaisonEdition(maisonEditionImporter);

                }

            }

        }

        catch (FileNotFoundException e) {

            log.error("Erreur : fichier de maisons d'edition non retrouvés");

            return;

        }

        catch (IOException e) {

            log.error("Erreur : ", e);

            return;

        }

        // Actualiser la date du dernier import

        setDateDernierImport(LocalDateTime.now());

    }

    @Override
    public void importerAuteurs(FileReader chemin) {

        try(BufferedReader br = new BufferedReader(chemin)) {

            String line;

            // Consommer la première ligne

            br.readLine();

            // Parcourir le fichier

            while ((line = br.readLine()) != null) {

                // Si la ligne n'est pas vide

                if (!line.trim().isEmpty()) {

                    // Construire l'auteur a importer

                    Auteur auteurAImporter = construireAuteur(line);

                    // Ajouter l'auteur importé au registre

                    registreAuteurs.ajouterAuteur(auteurAImporter);

                }

            }

        }

        catch (FileNotFoundException e) {

            log.error("Erreur : fichier d'auteurs non retrouvés");

            return;

        }

        catch (IOException e) {

            log.error("Erreur : ", e);

            return;

        }

        // Actualiser la date du dernier import

        setDateDernierImport(LocalDateTime.now());

    }

    @Override
    public void importerLivres(FileReader chemin) {

        // Si le registre d'auteurs ou de maisons d'editions est vide

        if (registreAuteurs.getAuteurs().isEmpty() || registreMaisonEdition.getMaisonEditions().isEmpty()) {

            throw new IllegalArgumentException("Pour importer des livres, veillez importer les auteurs et les maisons d'editions d'abord");

        }

        // Gestion de l'import

        try (BufferedReader br = new BufferedReader(chemin)) {

            // Sauter la première ligne

            br.readLine();

            // Consommer la première ligne

            br.readLine();

            String line;

            // Parcours du fichier

            while ((line = br.readLine()) != null) {

                // Si la ligne est non vide

                if (!line.trim().isEmpty()) {

                    // Formation du livre

                    Livre nouveauLivreImporter = construireLivre(line);

                    // Ajouter le livre importé au registre

                    registreLivres.ajouterLivre(nouveauLivreImporter);

                }

            }


        }

        catch (FileNotFoundException e) {

            log.error("Erreur : fichier de livres non retrouvés");

            return;

        }

        catch (IOException e) {

            log.error("Erreur : ", e);

            return;

        }

        // Actualiser la date du dernier import

        setDateDernierImport(LocalDateTime.now());

    }

    @Override
    public void importerMembres(FileReader chemin) {

        try (BufferedReader br = new BufferedReader(chemin)) {

            // Sauter la première ligne

            br.readLine();

            // Consommer la première ligne

            br.readLine();

            String line;

            // Parcours des lignes

            while ((line = br.readLine()) != null) {

                // Si la ligne est non vide

                if (!line.trim().isEmpty()) {

                    // Construire les membres

                    Membre nouveauMembreImporter = construireMembre(line);

                    // Ajouter le membre importé au registre

                    registreMembres.ajouterMembre(nouveauMembreImporter);

                }

            }

        }

        catch (FileNotFoundException e) {

            log.error("Erreur : fichier de membres non retrouvés");

            return;

        }

        catch (IOException e) {

            log.error("Erreur : ", e);

            return;

        }

        // Actualiser la date du dernier import

        setDateDernierImport(LocalDateTime.now());

    }

    // Méthodes utilitaire - Importer Maison d'édition

    MaisonEdition construireMaisonEdition(@NonNull String line) {

        // Diviser la ligne

        String[] attributsMaisonEdition = line.split(";");

        // Recuperer les attributs

        String nom = attributsMaisonEdition[0];
        String codePostal = attributsMaisonEdition[1];
        String numeroDeTelephone = attributsMaisonEdition[2];
        String ville = attributsMaisonEdition[3];
        String date_creation = attributsMaisonEdition[4];

        // Parser la date

        LocalDate dateCreation = LocalDate.parse(date_creation);

        // Retourne la maison d'édition

        return new MaisonEdition(nom, codePostal, numeroDeTelephone, ville, dateCreation);

    }

    // Méthodes utilitaire - Importer Auteurs

    Auteur construireAuteur(@NonNull String line) {

        // Diviser la ligne

        String[] attributsAuteur = line.split(";");

        // Recuperer les attributs

        String nom = attributsAuteur[0];
        String prenom = attributsAuteur[1];
        String nationalite = attributsAuteur[2];
        String biographie = attributsAuteur[3];

        // Retourne l'auteur

        return new Auteur(nom, prenom, nationalite, biographie);

    }

    // Méthodes utilitaire - Importer Livres

    Livre construireLivre(@NonNull String line) {

        // Diviser la ligne

        String[] attributsLivre = line.split(";");

        // Recuperer les attributs

        String id = attributsLivre[0];
        String isbn = attributsLivre[1];
        String titre = attributsLivre[2];
        String nom_auteur = attributsLivre[3];
        String prenom_auteur = attributsLivre[4];
        String date = attributsLivre[5];
        String nom_maison_edition = attributsLivre[6];
        String annee_publication = attributsLivre[7];
        String categorie_du_livre = attributsLivre[8];
        String etat_du_livre = attributsLivre[9];
        String nombre_exemplaires = attributsLivre[10];
        String nombre_exemplaires_disponibles = attributsLivre[11];
        String compteur_emprunts_totaux = attributsLivre[12];

        // Trouver l'auteur

        Auteur auteurDuLivre = trouverAuteur(nom_auteur, prenom_auteur);

        // Trouver la maison d'édition

        MaisonEdition maisonEditionDuLivre = trouverMaisonEdition(nom_maison_edition);

        // Parser les dates

        LocalDate dateAjoutLivre = LocalDate.parse(date);

        Year anneePublicationDuLivre = Year.parse(annee_publication);

        // Parser les enums

        CategorieLivre categorieDuLivre = CategorieLivre.valueOf(categorie_du_livre);

        EtatLivre etatDuLivre = EtatLivre.valueOf(etat_du_livre);

        // Parser les chiffres

        long nombreExemplairesInitial = Long.parseLong(nombre_exemplaires);

        long nombreExemplairesDisponibles = Long.parseLong(nombre_exemplaires_disponibles);

        long nombreEmpruntsTotal = Long.parseLong(compteur_emprunts_totaux);

        // Retourne le livre

        return new Livre(id, isbn, titre, auteurDuLivre, dateAjoutLivre, maisonEditionDuLivre, anneePublicationDuLivre, categorieDuLivre, etatDuLivre, nombreExemplairesInitial, nombreExemplairesDisponibles, nombreEmpruntsTotal);

    }

    Auteur trouverAuteur(String nom, String prenom) {

        return serviceAuteur.toutLesAuteurs().stream()
        .filter(auteur -> auteur.nom().equalsIgnoreCase(nom) && auteur.prenom().equalsIgnoreCase(prenom))
        .findAny()

        /* Si l'auteur n'est pas retoruver */

        .orElse(null);

    }

    MaisonEdition trouverMaisonEdition(String nom) {

        return serviceMaisonEdition.toutLesMaisonEdition().stream()
        .filter(maisonEdition -> maisonEdition.nom().equalsIgnoreCase(nom))
        .findAny()

        /* Si la maison d'édition n'est pas retrouvée */

        .orElse(null);

    }

    // Méthode utilitaire - Importer Membres

    Membre construireMembre(@NonNull String line) {

        // Attributs du membre

        String[] attributsMembre = line.split(";");

        // Recuperer les attributs

        String id = attributsMembre[0];
        String numero_membre = attributsMembre[1];
        String nom_membre = attributsMembre[2];
        String prenom_membre = attributsMembre[3];
        String email = attributsMembre[4];
        String numero_telephone_membre = attributsMembre[5];
        String date_inscription_membre = attributsMembre[6];
        String statut_membre = attributsMembre[7];
        String nombre_emprunts_en_cours = attributsMembre[8];

        // Parsing

        LocalDate dateInscriptionMembre = LocalDate.parse(date_inscription_membre);
        StatutMembre statutDuMembre = StatutMembre.valueOf(statut_membre);
        long numeroMembre = Long.parseLong(numero_membre);
        long nombreEmpruntsEnCours = Long.parseLong(nombre_emprunts_en_cours);

        // Retourne le membre

        return new Membre(id, numeroMembre, nom_membre, prenom_membre, email, numero_telephone_membre, dateInscriptionMembre, statutDuMembre, nombreEmpruntsEnCours,
        null, null);

    }

    // Méthodes utilitaire - Statistiques Mensuel

    String activitesMensuel() {

        // Statistiques mensuel

        StatistiquesMensuelles statistiquesMensuelles = serviceRapport.genererStatistiquesMensuelles(DATE_LANCEMENT);

        // Retour de la chaine formatée

        return "Activités mensuelle" +

        ";" +
        statistiquesMensuelles.nombreEmpruntsDuMois() +
        ";" +
        statistiquesMensuelles.retours() +
        ";" +
        statistiquesMensuelles.tauxRotation() +
        ";" +
        statistiquesMensuelles.nouveauxMembres() +
        ";" +
        statistiquesMensuelles.reservations() +
        ";"
        + statistiquesMensuelles.penalitesDuMois();

    }

    String tendancesHebdomadaire() {

        // Statistiques mensuel

        StatistiquesMensuelles statistiquesMensuelles = serviceRapport.genererStatistiquesMensuelles(DATE_LANCEMENT);

        // Retour de la chaine formatée

        AtomicInteger i = new AtomicInteger(0);

        return statistiquesMensuelles.tendanceHebdomadaire().entrySet().stream()
        .map(cle_valeur -> i.incrementAndGet() + ";" + cle_valeur.getKey() + ";" + cle_valeur.getValue())
        .collect(Collectors.joining("\n"));

    }

    String top5Categories() {

        // Statistiques mensuel

        StatistiquesMensuelles statistiquesMensuelles = serviceRapport.genererStatistiquesMensuelles(DATE_LANCEMENT);

        // Retour de la chaine formatée

        AtomicInteger i = new AtomicInteger(0);

        return statistiquesMensuelles.top5Categories().entrySet().stream()
        .map(cle_valeur -> i.incrementAndGet() + ";" + cle_valeur.getKey() + ";" + cle_valeur.getValue())
        .collect(Collectors.joining("\n"));

    }

    String top5Membres() {

        // Statistiques mensuel

        StatistiquesMensuelles statistiquesMensuelles = serviceRapport.genererStatistiquesMensuelles(DATE_LANCEMENT);

        // Retour de la chaine formatée

        AtomicInteger i = new AtomicInteger(0);

        return statistiquesMensuelles.top5MembresActifs().entrySet().stream()
        .map(cle_valeur -> i.incrementAndGet() + ";" + formaterChaine.apply(cle_valeur.getKey().getNom()) + " " + formaterChaine.apply(cle_valeur.getKey().getPrenom()) + " (" + cle_valeur.getKey().getId() + ")" + ";" + cle_valeur.getValue())
        .collect(Collectors.joining("\n"));

    }

    // Méthodes utilitaire - Statistiques Global

    String activitesGlobal() {

        // Statistiques global

        StatistiquesGlobal statistiquesGlobal = serviceRapport.genererStatistiquesGlobal(STOCK_INITIAL);

        // Retourne la chaine formatée

        return "Activités global" +

                statistiquesGlobal.stockTotalDeLivres() +
                ";" +
                statistiquesGlobal.livresDisponibles() +
                ";" +
                statistiquesGlobal.empruntsCumules() +
                ";" +
                statistiquesGlobal.reservationsActives() +
                ";" +
                statistiquesGlobal.tauxDeRotationGlobal() +
                ";" +
                statistiquesGlobal.tauxDeReservations() +
                ";" +
                statistiquesGlobal.membreActifs() +
                ";" +
                statistiquesGlobal.penalitesTotal() +
                "\n";


    }

    String evolutionMensuelle() {

        // Statistiques global

        StatistiquesGlobal statistiquesGlobal = serviceRapport.genererStatistiquesGlobal(STOCK_INITIAL);

        // Evolution mensuelle

        EvolutionMensuelle premierMois = statistiquesGlobal.evolutionMensuellesDes3DerniersMois().getFirst();

        EvolutionMensuelle deuxiemeMois = statistiquesGlobal.evolutionMensuellesDes3DerniersMois().get(1);

        EvolutionMensuelle troisiemeMois = statistiquesGlobal.evolutionMensuellesDes3DerniersMois().getLast();

        // Retourne la chaine formatée

        return  "Evolution Mensuelle" +

        formateLaDate.apply(premierMois.dateAFormater()) +
        ";" +
        premierMois.nombreEmprunts() +
        ";" +
        premierMois.nombreReservations() +
        "\n" +

        "Evolution Mensuelle" +

        formateLaDate.apply(deuxiemeMois.dateAFormater()) +
        ";" +
        deuxiemeMois.nombreEmprunts() +
        ";" +
        deuxiemeMois.nombreReservations() +
        "\n" +

        "Evolution Mensuelle" +

        formateLaDate.apply(troisiemeMois.dateAFormater()) +
        ";" +
        troisiemeMois.nombreEmprunts() +
        ";" +
        troisiemeMois.nombreReservations() +
        "\n";

    }

    String top5Livres() {

        // Statistiques global

        StatistiquesGlobal statistiquesGlobal = serviceRapport.genererStatistiquesGlobal(STOCK_INITIAL);

        // Retourne la chaine formatée

        AtomicInteger i = new AtomicInteger(0);

        return statistiquesGlobal.top5LivresDeToutePeriode().entrySet().stream()
        .map(cle_valeur -> i.incrementAndGet() + ";" + standariserTitre(cle_valeur.getKey()) + ";" + cle_valeur.getValue())
        .collect(Collectors.joining("\n"));

    }

    Function<String, String> formaterChaine = chaine -> Character.toUpperCase(chaine.charAt(0)) + chaine.substring(1);

    Function<LocalDate, String> formaterDate = date -> date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

    Function<LocalDate, String> formateLaDate = laDate -> {

        YearMonth date = YearMonth.from(laDate);

        return date.format(DateTimeFormatter.ofPattern("MM yyyy"));

    };

}