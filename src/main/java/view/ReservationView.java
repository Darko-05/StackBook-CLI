package view;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.IO.print;
import static java.lang.IO.println;
import static main.AppView.menuView;
import static util.DisplayUtil.*;

@NoArgsConstructor
public class ReservationView {

    // Scanner

    private final Scanner sc = new Scanner(System.in);

    // Menu - Principal

    public void showReserveMainMenu(long reservationsEnCours, long fileAttenteActives) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE,"GESTION DES RÉSERVATIONS"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Menu

        menuView.showLine("[N] NOUVELLE RÉSERVATION");
        menuView.showLine("[C] CONFIRMER UNE RÉSERVATION");
        menuView.showLine("[A] ANNULER UNE RÉSERVATION");
        menuView.showLine("[S] SUPPRIMER TOUTE LES RÉSERVATIONS D'UN LIVRE");
        menuView.showLine("[L] LISTER TOUTES LES RÉSERVATIONS");
        menuView.showLine("[P] CONSULTER LES RÉSERVATIONS D'UN MEMBRE");
        menuView.showLine("[F] VOIR LA FILE D'ATTENTE D'UN LIVRE");
        menuView.showLine();

        // Retour

        menuView.showLine(TextModifier.colorText(TextModifier.ROUGE, "[0] RETOUR AU MENU PRINCIPAL"));

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine("Réservations en cours : " + TextModifier.colorText(TextModifier.MAGENTA, String.valueOf(reservationsEnCours)) + " | Files d'attente actives : " + TextModifier.colorText(TextModifier.TURQUOISE, String.valueOf(fileAttenteActives)));
        menuView.showBottomBorder();

    }

    // Menu 1 - Ajouter une nouvelle reservation

    public void showReserveAddingStep(String idLivre, String idMembre, @NonNull LocalDate dateExpiration, @NonNull Livre livreAReserver, @NonNull Membre membreVoulantReserver, long nombreReservationMembre) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERTCLAIR,"NOUVELLE RÉSERVATION"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Section info

        showReserveAddingResult(idLivre, idMembre, dateExpiration, livreAReserver, membreVoulantReserver, nombreReservationMembre);

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[V]") + " Valider la réservation | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et retourner");
        menuView.showBottomBorder();

    }

    void showReserveAddingResult(String idLivre, String idMembre, @NonNull LocalDate dateExpiration, @NonNull Livre livreAReserver, @NonNull Membre membreVoulantReserver, long nombreReservationMembre) {

        menuView.showLine("ID du livre : " + idLivre);
        menuView.showLine();
        menuView.showLine("ID du membre : " + idMembre);
        menuView.showLine();
        menuView.showLine("Date d'expiration (JJ/MM/AAAA) : " + dateExpiration.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        menuView.showLine();

        // Info Livre

        menuView.showLine("Livre : \"" + standariserNom(livreAReserver.getTitre()) + "\" - " + standariserNomAuteur(livreAReserver.getAuteur()));

        // Info Membre

        menuView.showLine("Membre : " + standariserNomMembre.apply(membreVoulantReserver) + " - " + TextModifier.colorText(TextModifier.VERT, String.valueOf(nombreReservationMembre)) + " réservations actives");

    }

    public void showReservationConfirmation(@NonNull Reservation reservationAjouter, int positionDansLaFile) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ORANGE,"RÉSERVATION CONFIRMÉE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("La réservation a été créée avec succès !");
        menuView.showLine();
        menuView.showLine("Détails :");
        menuView.showLine("• ID de réservation : " + reservationAjouter.getIdReservation());
        menuView.showLine("• Livre : " + standariserTitre(reservationAjouter.getLivre()) + " (ID : " + reservationAjouter.getLivre().getId() + ")");
        menuView.showLine("• Membre : " + standariserNomMembre.apply((reservationAjouter.getMembre())) + " (ID : " + reservationAjouter.getMembre().getId() + ")");
        menuView.showLine("• Date d'expiration : " + reservationAjouter.getDateExpiration().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        menuView.showLine("• Position dans la file d'attente : " + positionDansLaFile);
        menuView.showLine();
        menuView.showLine("Rappel : La réservation expire automatiquement à la date indiquée.");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.JAUNE, "[N]") + " Nouvelle réservation | " + TextModifier.colorText(TextModifier.ROUGE, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    // Gestion des cas d'erreurs

    public void showAddingReserveErrorLivreNonTrouveException(String idEnter) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE,"RÉSERVATION IMPOSSIBLE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Impossible de créer la réservation.");
        menuView.showLine();
        menuView.showLine("Raison : Livre non trouvé");
        menuView.showLine("ID du livre recherché : " + idEnter);
        menuView.showLine();
        menuView.showLine("Suggestions :");
        menuView.showLine("• Vérifier l'ID du livre");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERTCLAIR, "[C]") + " Corriger l'ID | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler");
        menuView.showBottomBorder();

    }

    public void showAddingReserveErrorLivreNonDisponibleException(Livre livreRechercher) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE,"LIVRE NON DISPONIBLE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Le livre demandé n'est pas disponible pour réservation.");
        menuView.showLine();
        menuView.showLine("Livre : " + standariserTitre(livreRechercher) + " - " + standariserNomAuteur(livreRechercher.getAuteur()) + " (ID : " + livreRechercher.getId() + ")");
        menuView.showLine("Statut : " + (livreRechercher.estDisponible() ? "Actuellement emprunté" : "Si vous voyez ceci, c'est qu'il a eu une erreur"));

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et retourner");
        menuView.showBottomBorder();

    }

    public void showAddingReserveErrorMembreNonTrouveException(String idEnter) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE,"RÉSERVATION IMPOSSIBLE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Impossible de créer la réservation.");
        menuView.showLine();
        menuView.showLine("Raison : Membre non trouvé");
        menuView.showLine("ID du membre : " + idEnter);
        menuView.showLine();
        menuView.showLine("Suggestions :");
        menuView.showLine("• Vérifier l'ID du membre");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERTCLAIR, "[C]") + " Corriger l'ID | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler");
        menuView.showBottomBorder();

    }

    public void showAddingReserveErrorMembreSuspendu(Membre membreSuspendu) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE,"MEMBRE SUSPENDU"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Ce membre est actuellement suspendu.");
        menuView.showLine();
        menuView.showLine("Membre : " + standariserNomMembre.apply(membreSuspendu) + " (ID : " + membreSuspendu.getId() + ")");
        menuView.showLine("Raison de la suspension : 3 emprunts en retard");
        menuView.showLine();
        menuView.showLine("Aucune réservation ou emprunt n'est autorisé pendant la suspension.");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et retourner");
        menuView.showBottomBorder();

    }

    public void showAddingReserveErrorReservationDejaExistant() {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE,"RÉSERVATION EXISTANTE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine();
        menuView.showLine("Impossible de réserver le même livre plusieurs fois.");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler");
        menuView.showBottomBorder();

    }

    public void showAddingReserveErrorLimiteReservationAtteint(Membre membreAyantAtteintLimite) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE,"LIMITE DE RÉSERVATIONS ATTEINTE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Ce membre a atteint son quota maximum de réservations.");
        menuView.showLine();
        menuView.showLine("Membre : " + standariserNomMembre.apply(membreAyantAtteintLimite) + " (ID : " + membreAyantAtteintLimite.getId() + ")");
        menuView.showLine("Limite autorisée : 3 réservations simultanées");
        menuView.showLine("Réservations actuelles : 3");
        menuView.showLine();
        menuView.showLine("Pour réserver un nouveau livre :");
        menuView.showLine("1. Annuler une réservation existante");
        menuView.showLine("2. Attendre qu'une réservation expire");
        menuView.showLine("3. Convertir une réservation en emprunt");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler une réservation | " + TextModifier.colorText(TextModifier.VERT, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    // Menu 2 - Confirmer une reservation

    public void showConfirmReservation(String idEnter, @NonNull Reservation reservationAConfirme) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.BLEU,"CONFIRMER UNE RÉSERVATION"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("ID de la réservation à confirmer : " + TextModifier.colorText(TextModifier.TURQUOISE, idEnter));
        menuView.showLine();
        menuView.showLine("Réservation trouvée :");
        menuView.showLine("• ID : " + reservationAConfirme.getIdReservation());
        menuView.showLine("• Membre : " + standariserNomMembre.apply(reservationAConfirme.getMembre()) + " (ID : " + reservationAConfirme.getMembre().getId() + ")");
        menuView.showLine("• Livre : " + standariserTitre(reservationAConfirme.getLivre()) + " - " + standariserNomAuteur(reservationAConfirme.getLivre().getAuteur()));
        menuView.showLine("• Date de réservation : " + reservationAConfirme.getDateReservation().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        menuView.showLine("• Expire le : " + reservationAConfirme.getDateExpiration().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERTCLAIR, "[C]") + " Confirmer et prêter le livre | " + TextModifier.colorText(TextModifier.ROUGE, "[R]") + " Annuler et retourner");
        menuView.showBottomBorder();

    }

    public void showConfirmationSucces(@NonNull Emprunt reservationConfirmer) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERTCLAIR,"RÉSERVATION CONVERTIE EN EMPRUNT"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("La réservation a été convertie en emprunt avec succès !");
        menuView.showLine();
        menuView.showLine("Détails de l'emprunt :");
        menuView.showLine("• ID d'emprunt : " + reservationConfirmer.getId());
        menuView.showLine("• Livre : " + standariserTitre(reservationConfirmer.getLivre()) + " (ID : " + reservationConfirmer.getLivre().getId() + ")");
        menuView.showLine("• Membre : " + standariserNomMembre.apply(reservationConfirmer.getMembre()) + " (ID : " + reservationConfirmer.getMembre().getId() + ")");
        menuView.showLine("• Date d'emprunt : " + reservationConfirmer.getDateEmprunt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        menuView.showLine("• Date de retour prévue : " + reservationConfirmer.getDateRetourPrevu().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        menuView.showLine();
        menuView.showLine("L'état de la réservation a été mis a \"Validé\"");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    public void showConfirmationError(Membre membreLimite) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE,"QUOTA D'EMPRUNT ATTEINT"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Impossible de confirmer cette réservation.");
        menuView.showLine();
        menuView.showLine("Raison : Le membre a atteint son quota maximum d'emprunts.");
        menuView.showLine("• Membre : " + standariserNomMembre.apply(membreLimite) + " (ID : " + membreLimite.getId() + ")");
        menuView.showLine("Emprunts en cours : 5/5");
        menuView.showLine();
        menuView.showLine("Solutions possibles :");
        menuView.showLine("• Retourner un livre emprunté");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[A]") + " Annuler et retourner");
        menuView.showBottomBorder();

    }

    // Menu 3 - Annuler une reservation

    public void showReserveCanceling(String idEnter, @NonNull Reservation reservationTrouve) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE,"ANNULER UNE RÉSERVATION"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("ID de la réservation à annuler : " + TextModifier.colorText(TextModifier.TURQUOISE, idEnter));
        menuView.showLine();
        menuView.showLine("Réservation trouvée :");
        menuView.showLine("• Membre : " + standariserNomMembre.apply(reservationTrouve.getMembre()) + " (ID : " + reservationTrouve.getMembre().getId() + ")");
        menuView.showLine("• Livre : " + standariserTitre(reservationTrouve.getLivre()) + " (ID : " + reservationTrouve.getLivre().getId() + ")");
        menuView.showLine("• Expire le : " + reservationTrouve.getDateExpiration().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        menuView.showLine();
        menuView.showLine("Cette action est irréversible.");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.CYAN, "[O]") + " Oui, annuler cette réservation | " + TextModifier.colorText(TextModifier.ROUGE, "[A] Annuler et retourner"));
        menuView.showBottomBorder();

    }

    public void showReserveCancelingSucces() {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.MAGENTA,"RÉSERVATION ANNULÉE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("La réservation a été annulée avec succès.");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    public void showReserveCancelingFail(String idEnter) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE,"ÉCHEC DE L'ANNULATION DE RÉSERVATION"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("L'annulation de la réservation a échoué.");
        menuView.showLine();
        menuView.showLine("ID de réservation : " + idEnter);
        menuView.showLine("Raison : Aucune réservation trouvée avec cet ID.");
        menuView.showLine();
        menuView.showLine("Suggestions :");
        menuView.showLine("• Vérifiez l'ID de réservation");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.CYAN, "[N]") + " Nouvelle tentative | " + TextModifier.colorText(TextModifier.ROSE, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    // Menu 4 - Supprimer toute les reservations d'un livre

    public void showDeleteAllReservationForABook(@NonNull Livre livre, long nombreReservationsActives) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE,"SUPPRIMER TOUTES LES RÉSERVATIONS D'UN LIVRE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("ID du livre : " + livre.getId());
        menuView.showLine();
        menuView.showLine("Livre : " + standariserTitre(livre) + " - " + standariserNomAuteur(livre.getAuteur()) + " (ID : " + livre.getId() + ")");
        menuView.showLine("Réservations actives : " + nombreReservationsActives);
        menuView.showLine();
        menuView.showLine("TOUTES les réservations en cours pour ce livre seront supprimer.");
        menuView.showLine("Cette action est irréversible.");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ORANGE, "[O]") + " Oui, tout supprimer | " + TextModifier.colorText(TextModifier.VERT, "[N]") + " Non, Retour");
        menuView.showBottomBorder();

    }

    public void showDeleteAllReserveSucces() {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.CYAN,"SUPPRESION RÉUSSIE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine();
        menuView.showLine("Toute les réservation pour ce livre a été supprimer");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    public void showDeleteAllReservationFail(String idEnter) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE,"ÉCHEC DE LA SUPPRESSION DES RÉSERVATIONS"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Impossible de supprimer les réservations pour ce livre.");
        menuView.showLine();
        menuView.showLine("ID du livre recherché : " + idEnter);
        menuView.showLine("Raison : Aucun livre trouvé avec cet ID.");
        menuView.showLine();
        menuView.showLine("Vérifiez :");
        menuView.showLine("• L'ID saisi");
        menuView.showLine("• Si le livre existe toujours dans le catalogue");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[R]") + " Réessayer avec un autre ID | " + TextModifier.colorText(TextModifier.JAUNE, "[A]") + " Annuler");
        menuView.showBottomBorder();

    }

    // Menu 5 - Lister toutes les réservations

    public void showAllReserveList(@NonNull List<Reservation> listeReservations) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROSE,"LISTE DES RÉSERVATIONS ACTIVES") + " (" + listeReservations.size() + ")");
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine(" ────────────────────────────────────────────────────────");
        menuView.showLine();

        // Si la liste de réservations est vide

        if (listeReservations.isEmpty()) {

            menuView.showLine("Il n'y aucune reservation pour le moment");

        }

        // Dans le cas contraire

        else {

            showAllReserveListResult(listeReservations);

        }

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.MAGENTA, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    void showAllReserveListResult(@NonNull List<Reservation> listeReservations) {

        AtomicInteger i = new AtomicInteger(0);

        listeReservations.forEach(reservation -> {

            // Id de la réservation

            menuView.showLine(i.incrementAndGet() + ". Réservation #" + reservation.getIdReservation());

            // Infos membre

            menuView.showLine("• Membre : " + standariserNomMembre.apply(reservation.getMembre()) + " (ID : " + reservation.getMembre().getId() + ")");

            // Infos livre

            menuView.showLine("• Livre : " + standariserTitre(reservation.getLivre()));

            // Date d'expiration

            menuView.showLine("• Expire le : " + reservation.getDateExpiration().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            // Position dans la file d'attente

            menuView.showLine("• Position : " + listeReservations.indexOf(reservation));

            menuView.showLine(" ────────────────────────────────────────────────────────");

            menuView.showLine();

        });

    }

    // Menu 6 - Consulter les reservations d'un membre

    public void showMemberReservations(Membre membre, List<Reservation> reservationsMembre) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.CYAN,"RÉSERVATIONS DU MEMBRE ") + " - " + TextModifier.colorText(TextModifier.ORANGE, standariserNomMembre.apply(membre)));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Réservations en cours :");
        showAllReserveList(reservationsMembre);

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[R]") + " Retour");
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.BLEU, "[A]") + " Annuler une réservation | " + TextModifier.colorText(TextModifier.TURQUOISE, "[C]") + " Convertir en emprunt");
        menuView.showBottomBorder();

    }

    public void showMemberReservationFail(String idEnter) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE,"ÉCHEC DE LA CONSULTATION DES RÉSERVATIONS"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Impossible de consulter les réservations du membre.");
        menuView.showLine();
        menuView.showLine("ID du membre recherché : " + idEnter);
        menuView.showLine("Raison : Aucun membre trouvé avec cet ID.");
        menuView.showLine();
        menuView.showLine("Verifiez :");
        menuView.showLine("• L'ID saisi");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[R]") + " Réessayer avec un autre ID | " + TextModifier.colorText(TextModifier.ROSE, "[A]") + " Annuler et retourner");
        menuView.showBottomBorder();

    }

    // Menu 7 - Voir la liste d'attente d'un livre

        public void showBookQueue(@NonNull Livre livre, @NonNull List<Reservation> listeReservations) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.MAGENTA,"FILE D'ATTENTE") + " - \"" + standariserTitre(livre) + "\" (" + listeReservations.size() + " personnes)");
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Ordre d'attente :");
        menuView.showLine();

        // Si la liste de réservations est vide

        if (listeReservations.isEmpty()) {

            menuView.showLine("Il n'y aucune reservation pour ce livre actuellement");

        }

        // Dans le cas contraire

        else {

            showBookQueueResult(listeReservations);

        }

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    void showBookQueueResult(@NonNull List<Reservation> listeReservations) {

        AtomicInteger i = new AtomicInteger(0);

        listeReservations.forEach(reservation -> {

            // Membre

            menuView.showLine(i.incrementAndGet() + ". " + standariserNomMembre.apply(reservation.getMembre()) + " (ID : " + reservation.getMembre().getId() + ")");

            // Reservation

            menuView.showLine("Réservation : " + reservation.getIdReservation() + " | Expire le : " + reservation.getDateExpiration().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            menuView.showLine();

        });

    }

    // Recuperation

    public String recupererIdReservation() {

        println("Entrez l'Id de la reservation à confirmer :");

        print("> ");

        return sc.nextLine();

    }

    public String recupererIdLivre() {

        println("Entrez l'Id du livre :");

        print("> ");

        return sc.nextLine();

    }

    public String recupererIdMembre() {

        println("Entrez l'Id du membre :");

        print("> ");

        return sc.nextLine();

    }

}