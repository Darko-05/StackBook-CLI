package main;

import lombok.NoArgsConstructor;
import registre.*;
import service.*;

@NoArgsConstructor
public final class AppConfig {

    // Gestion des registres

    public static final Auteurs registreAuteurs = Auteurs.getInstance();
    public static final Emprunts registreEmprunts = Emprunts.getInstance();
    public static final Livres registreLivres = Livres.getInstance();
    public static final MaisonEditions registreMaisonEdition = MaisonEditions.getInstance();
    public static final Membres registreMembres = Membres.getInstance();
    public static final Reservations registreReservations = Reservations.getInstance();

    // Controller principal

    public static final MainController MainController = main.MainController.getInstance();

    // Application Launcher

    public static final ApplicationLauncher StackBook_CLI = new ApplicationLauncher();

    // Gestion des services

    public static final ServiceAuteur serviceAuteur = ServiceAuteur.getInstance();
    public static final ServiceEmprunt serviceEmprunt = ServiceEmprunt.getInstance();
    public static final ServiceLivre serviceLivre = ServiceLivre.getInstance();
    public static final ServiceMaisonEdition serviceMaisonEdition = ServiceMaisonEdition.getInstance();
    public static final ServiceMembre serviceMembre = ServiceMembre.getInstance();
    public static final ServiceRapport serviceRapport = ServiceRapport.getInstance();
    public static final ServiceReservation serviceReservation = ServiceReservation.getInstance();
    public static final ServiceImportExport serviceImportExport = ServiceImportExport.getInstance();

}
