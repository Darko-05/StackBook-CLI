package controlleur;

import exception.*;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import model.Emprunt;
import model.Livre;
import model.Membre;
import model.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static main.AppConfig.*;
import static main.AppView.menuView;
import static main.AppView.reservationView;
import static util.DataUtil.*;
import static util.MenuUtil.handleUserChoice;

@NoArgsConstructor
public class ReservationController {

    // Methode principal

    @SneakyThrows
    public void handleReservationManagement() {

        // Afficher le menu principal

        reservationView.showReserveMainMenu(nombreTotalReservations(), nombreFilesAttente());

        // Gestion Choix

        handleUserChoice(
            Map.of(
            "N", this::handleBooksReservations,
            "C", this::handleReservationConfirmation,
            "A", this::handleReservationCancellation,
            "S", this::handleDeleteAllReservationsForABook,
            "L", this::handleShowAllReservation,
            "P", this::handleShowAllOneMemberReservations,
            "F", this::handleShowBookQueue,
            "0", MainController::launchControllers
            )
        );

    }

    // Menu 1 - Reserver un livre

    @SneakyThrows
    private void handleBooksReservations() {

        // Recuperer les informations necessaires

        String idLivre = reservationView.recupererIdLivre();
        String idMembre = reservationView.recupererIdMembre();

        // Les reservations durent 10 Jours

        LocalDate dateExpiration = LocalDate.now().plusDays(10);

        // Gestion de la suite

        handleAddingReservation(idLivre, idMembre, dateExpiration);

    }

    private void handleAddingReservation(String idLivre, String idMembre, LocalDate dateExpiration) throws LivreNonTrouveException, MembreNonTrouveException {

        // Tenter la création

        try {

            Reservation nouveauReservation = serviceReservation.reserverLivre(idLivre, idMembre, dateExpiration);

            // Gerer la confirmation ou non

            handleConfirmationChoice(nouveauReservation);

        } catch (LivreNonTrouveException e) {

            reservationView.showAddingReserveErrorLivreNonTrouveException(idLivre);

            handleAddingBadId();

        } catch (MembreNonTrouveException e) {

            reservationView.showAddingReserveErrorMembreNonTrouveException(idMembre);

            handleAddingBadId();

        } catch (LivreNonDisponibleException e) {

            reservationView.showAddingReserveErrorLivreNonDisponibleException(registreLivres.findById(idLivre));

            handleAddingNotAvailable();

        } catch (MembreSuspenduException e) {

            reservationView.showAddingReserveErrorMembreSuspendu(registreMembres.findById(idMembre));

            handleAddingNotAvailable();

        } catch (ReservationEnCoursException e) {

            reservationView.showAddingReserveErrorReservationDejaExistant();

            handleAddingNotAvailable();

        } catch (LimiteReservationAtteintException e) {

            reservationView.showAddingReserveErrorLimiteReservationAtteint(registreMembres.findById(idMembre));

            handleAddingLimit();

        }

    }

    private void handleConfirmationChoice(@NonNull Reservation reservation) {

        Membre membre = reservation.getMembre();

        reservationView.showReserveAddingStep(reservation.getLivre().getId(), reservation.getMembre().getId(), reservation.getDateExpiration(), reservation.getLivre(), membre, nombreReservationsMembre(membre));

        handleUserChoice(
            Map.of(
                "V", () -> {

                    reservationView.showReservationConfirmation(reservation, positionDansLaFileAttente(reservation));

                    // Gerer le choix

                    handleSuccesEnd();

                },

                "A", () -> {

                    // Annuler la réservation

                    serviceReservation.annulerReservation(reservation.getIdReservation());

                    // Retourner au menu

                    Runnable handleReservationManagement = this::handleReservationManagement;

                    handleReservationManagement.run();

                }

            )
        );

    }

    private void handleSuccesEnd() {

        handleUserChoice(
            Map.of(
                "N", this::handleBooksReservations,
                "R", this::handleReservationManagement
            )
        );

    }

    private void handleAddingBadId() {

        handleUserChoice(
            Map.of(
            "C", this::handleBooksReservations,
            "A", this::handleReservationManagement
            )
        );

    }

    private void handleAddingLimit() {

        handleUserChoice(
            Map.of(
                "A", this::handleReservationCancellation,
                "R", this::handleReservationManagement
            )
        );

    }

    private void handleAddingNotAvailable() {

        handleUserChoice(Map.of("A", this::handleReservationManagement));

    }

    // Menu 2 - Confirmer une reservation
    private void handleReservationConfirmation() {

        // Recuperer l'id de la reservation

        String idReservation = reservationView.recupererIdReservation();

        // Essayer la confirmation

        try {

            Reservation reservation = registreReservations.findById(idReservation);

            reservationView.showConfirmReservation(idReservation, reservation);

            // Gerer la confirmation

            handleUserChoice(
                Map.of(
                    "C", () -> handleConfirmation(reservation.getIdReservation()),
                    "R", this::handleReservationManagement
                )
            );

        } catch (NoSuchElementException e) {

            menuView.showAFailure("ECHEC - CONFIRMATION DE LA RESERVATION", "Aucune reservation retrouver avec cet id.");

            handleConfirmationFailureEnd();

        }

    }

    private void handleConfirmation(String idReservation) {

        Reservation reservation = registreReservations.findById(idReservation);

        try {

            // Emprunt et affichage

            Emprunt emprunt = serviceReservation.confirmerReservation(idReservation);

            reservationView.showConfirmationSucces(emprunt);

        } catch (QuotaEmpruntAtteintException e) {

            reservationView.showConfirmationError(reservation.getMembre());

            handleUserChoice(Map.of("A", this::handleReservationManagement));

        } catch (MembreSuspenduException e) {

            menuView.showAFailure("ECHEC - CONFIRMATION DE LA RESERVATION", "Le membre est supendu.");

            handleConfirmationFailureEnd();

        }

        // Reservation réussi

        handleUserChoice(Map.of("R", this::handleReservationManagement));

    }

    private void handleConfirmationFailureEnd() {

        handleUserChoice(
            Map.of(
                "R", this::handleReservationConfirmation,
                "A", this::handleReservationManagement
            )
        );

    }

    // Menu 3 - Annuler une reservation

    private void handleReservationCancellation() {

        // Recuperer l'id de la reservation

        String idReservation = reservationView.recupererIdReservation();

        // Recuperer la reservation et tenter l'annulation

        try {

            Reservation reservationToCancel = registreReservations.findById(idReservation);

            // Gerer la confirmation

            handleCancelingConfirmation(idReservation, reservationToCancel);

        } catch (NoSuchElementException e) {

            reservationView.showReserveCancelingFail(idReservation);

            handleUserChoice(
                Map.of(
                    "N", this::handleReservationCancellation,
                    "R", this::handleReservationManagement
                )
            );

        }

    }

    private void handleCancelingConfirmation(String idReservation, @NonNull Reservation reservationToCancel) {

        reservationView.showReserveCanceling(idReservation, reservationToCancel);

        handleUserChoice(
            Map.of(

                "O", () -> {

                    // Appel du service

                    serviceReservation.annulerReservation(idReservation);

                    // Afficher le succès

                    reservationView.showReserveCancelingSucces();

                    // Retour

                    handleUserChoice(Map.of("R", this::handleReservationManagement));

                },

                "A",  this::handleReservationManagement
            )
        );

    }

    // Menu 4 - Supprimer toute les reservations d'un livre

    private void handleDeleteAllReservationsForABook() {

        // Recuperer l'id du livre

        String idLivre = reservationView.recupererIdLivre();

        // Gerer la suppression

        try {

            Livre livre = registreLivres.findById(idLivre);

            // Gerer la confirmation

            handleDeleteAllReservationsConfirmation(livre, idLivre);

        } catch (LivreNonTrouveException e) {

            reservationView.showDeleteAllReservationFail(idLivre);

            // Gerer le choix

            handleUserChoice(
                Map.of(
                    "R", this::handleDeleteAllReservationsForABook,
                    "A", this::handleReservationManagement
                )
            );

        }

    }

    private void handleDeleteAllReservationsConfirmation(@NonNull Livre livre, String idLivre) {

        // Gerer la confirmation

        reservationView.showDeleteAllReservationForABook(livre, serviceReservation.compterReservationPourLivre(idLivre));

        handleUserChoice(
            Map.of(
                "O", () -> {

                    // Supprimer les reservations

                    serviceReservation.supprimerToutesReservationsPourLivre(idLivre);

                    // Afficher le succès et gerer le retour

                    reservationView.showDeleteAllReserveSucces();

                    handleUserChoice(Map.of("R", this::handleReservationManagement));

                },
                "R", this::handleReservationManagement
            )
        );

    }

    // Menu 5 - Lister toutes les réservations

    private void handleShowAllReservation() {

        // Recuperer tout les reservations

        List<Reservation> allReservations = serviceReservation.toutLesReservations();

        // Afficher et gerer le choix

        reservationView.showAllReserveList(allReservations);

        handleUserChoice(Map.of("R", this::handleReservationManagement));

    }

    // Menu 6 - Consulter les reservations d'un membre

    private void handleShowAllOneMemberReservations() {

        // Recuperer l'id du membre

        String idMembre = reservationView.recupererIdMembre();

        // Gerer l'affichage

        try {

            Membre memberToConsult = registreMembres.findById(idMembre);

            // Gerer la suite

            handleShowAllMemberReservation(memberToConsult, idMembre);

        } catch (MembreNonTrouveException e) {

            reservationView.showMemberReservationFail(idMembre);

            // Gerer le choix

            handleUserChoice(
                Map.of(
                    "R", this::handleShowAllOneMemberReservations,
                    "A", this::handleReservationManagement
                )
            );

        }

    }

    @SneakyThrows
    private void handleShowAllMemberReservation(Membre memberToConsult, String idMembre) {

        List<Reservation> reservationsMembre = serviceReservation.consulterReservationsMembre(idMembre)
        .stream()
        .toList();

        // Afficher le succès et gerer le choix

        reservationView.showMemberReservations(memberToConsult, reservationsMembre);

        handleUserChoice(
            Map.of(
                "R", this::handleReservationManagement,
                "A", this::handleReservationCancellation,
                "C",this::handleReservationConfirmation
            )
        );

    }

    // Menu 7 - Voir la liste d'attente d'un livre

    private void handleShowBookQueue() {

        // Recuperer l'id du livre

        String idLivre = reservationView.recupererIdLivre();

        try {

            Livre livre = registreLivres.findById(idLivre);

            // Recuperer et afficher la liste d'attente et gerer le choix

            List<Reservation> bookReservations = serviceReservation.obtenirListeAttente(idLivre)
            .stream()
            .toList();

            reservationView.showBookQueue(livre, bookReservations);

            handleUserChoice(Map.of("R", this::handleReservationManagement));

        } catch (LivreNonTrouveException e) {

            menuView.showAFailure("ECHEC - CONSULTATION DES RESERVATIONS DU LIVRE", "Aucun livre avec cet Id");

            // Gerer le choix

            handleUserChoice(
                Map.of(
                    "R", this::handleShowBookQueue,
                    "A", this::handleReservationManagement
                )
            );

        }

    }

}