package controlleur;

import exception.*;
import lombok.NoArgsConstructor;
import model.Emprunt;
import model.Membre;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static main.AppConfig.*;
import static main.AppView.borrowView;
import static util.MenuUtil.handleUserChoice;

@NoArgsConstructor
public class BorrowController {

    // Methode princiapl

    public void handleBorrowManagement() {

        // Afficher le menu principal

        borrowView.showBooksBorrowMainMenu();

        // Gestion du choix

        handleUserChoice(
            Map.of(
            "E", this::handleBookBorrowing,
            "R", this::handleBorrowReturn,
            "RE", this::handleLoanRenew,
            "A", this::handleShowAllBorrowings,
            "C", this::handleShowAllLateBorrowings,
            "P", this::handleCalculateMembersPenality,
            "V", this::handleVerifyMemberLimit,
            "0", MainController::launchControllers
            )
        );

    }

    // Menu 1 - Emprunter un livre

    private void handleBookBorrowing() {

        // Recuperer l'id du membre et l'id du livre

        String idLivre = borrowView.getIdLivre();

        String idMembre = borrowView.getIdMembre();

        // Gerer la confirmation

        borrowView.showBorrowing(idLivre, idMembre);

        handleUserChoice(
            Map.of(
                "V", () -> handleLoanConfirmation(idLivre, idMembre),
                "A", this::handleBorrowManagement
            )
        );

    }

    private void handleLoanConfirmation(String idLivre, String idMembre) {

        // Tenter l'emprunt

        try {

            Emprunt nouvelEmprunt = serviceEmprunt.emprunterLivre(idLivre, idMembre);

            // En cas de succès

            borrowView.showBorrowingSucces(nouvelEmprunt);

            // Gerer le choix

            handleUserChoice(
                Map.of(
                    "N", this::handleBookBorrowing,
                    "R", this::handleBorrowManagement
                )
            );

        } catch (MembreNonTrouveException e) {

            borrowView.showBorrowingFailure("Aucun membre retrouvée avec l'id : \"" + idMembre + "\".");

            handleBorrowingFailureEnd();

        } catch (MembreSuspenduException e) {

            borrowView.showBorrowingFailure("Le membre est suspendu.");

            handleBorrowingFailureEnd();

        } catch (LivreNonTrouveException e) {

            borrowView.showBorrowingFailure("Aucun livre retrouvée avec l'id : \"" + idLivre + "\".");

            handleBorrowingFailureEnd();

        } catch (LivreNonDisponibleException e) {

            borrowView.showBorrowingFailure("Le livre a emprunter est indisponible.");

            handleBorrowingFailureEnd();

        } catch (QuotaEmpruntAtteintException e) {

            borrowView.showBorrowingFailure("Nombre maximal d'emprunts atteint par le membre.");

            handleBorrowingFailureEnd();

        } catch (LivreEnCoursEmpruntException e) {

            borrowView.showBorrowingFailure("Le livre a emprunter est déja en cours d'emprunt.");

            handleBorrowingFailureEnd();

        }

    }

    private void handleBorrowingFailureEnd() {

        handleUserChoice(
            Map.of(
                "R", this::handleBookBorrowing,
                "A", this::handleBorrowManagement
            )
        );

    }

    // Menu 2 - Enregistrer un retour

    private void handleBorrowReturn() {

        // Recuperer l'id du membre et l'id du livre

        String idLivre = borrowView.getIdLivre();

        String idMembre = borrowView.getIdMembre();

        // Gerer la confirmation

        borrowView.showLoanReturn(idLivre, idMembre);

        handleUserChoice(
            Map.of(
                "V", () -> handleLoanReturnConfirmation(idLivre, idMembre),
                "A", this::handleBorrowManagement
            )
        );

    }

    private void handleLoanReturnConfirmation(String idLivre, String idMembre) {

        // Appel du service

        try {

            Emprunt empruntRetourner = serviceEmprunt.retournerLivre(idLivre, idMembre);

            // Afficher le succès et gerer le choix

            borrowView.showLoanReturnSucces(empruntRetourner);

            handleUserChoice(
                Map.of(
                    "N", this::handleBorrowReturn,
                    "R", this::handleBorrowManagement
                )
            );

        } catch (LivreNonTrouveException e) {

            borrowView.showLoanReturnFailure("Aucun livre retrouver avec l'id : \"" + idLivre + "\"");

            handleLoanReturnFailure();

        } catch (MembreSuspenduException e) {

            borrowView.showLoanReturnFailure("Aucun membre retrouver avec l'id : \"" + idMembre + "\"");

            handleLoanReturnFailure();

        } catch (NoSuchElementException e) {

            borrowView.showLoanReturnFailure("Emprunt inexistant.");

            handleLoanReturnFailure();

        }

    }

    private void handleLoanReturnFailure() {

        handleUserChoice(
            Map.of(
                "R", this::handleBorrowReturn,
                "A", this::handleBorrowManagement
            )
        );

    }

    // Menu 3 - Renouveller un emprunt

    private void handleLoanRenew() {

        // Recuperer l'id du membre et l'id du livre

        String idLivre = borrowView.getIdLivre();

        String idMembre = borrowView.getIdMembre();

        // Gerer la confirmation

        borrowView.showRenewBorrowing(idLivre, idMembre);

        handleUserChoice(
            Map.of(
                "V", () -> handleLoanRenewConfirmation(idLivre, idMembre),
                "A", this::handleBorrowManagement
            )
        );

    }

    private void handleLoanRenewConfirmation(String idLivre, String idMembre) {

        // Tenter l'emprunt

        try {

            Emprunt nouvelEmprunt = serviceEmprunt.renouvellerEmprunt(idLivre, idMembre);

            // En cas de succès

            borrowView.showRenewSucces(nouvelEmprunt);

            // Gerer le choix

            handleUserChoice(
                Map.of(
                    "N", this::handleLoanRenew,
                    "R", this::handleBorrowManagement
                )
            );

        } catch (MembreNonTrouveException e) {

            borrowView.showRenewFailure("Aucun membre retrouvée avec l'id : \"" + idMembre + "\".");

            handleRenewFailureEnd();

        } catch (MembreSuspenduException e) {

            borrowView.showRenewFailure("Le membre est suspendu.");

            handleRenewFailureEnd();

        } catch (LivreNonTrouveException e) {

            borrowView.showRenewFailure("Aucun livre retrouvée avec l'id : \"" + idLivre + "\".");

            handleRenewFailureEnd();

        } catch (LimiteRenouvellementAtteintException e) {

            borrowView.showRenewFailure("L'emprunt a atteint sa limite de renouvellement.");

            handleRenewFailureEnd();

        } catch (EmpruntDejaEnRetardException e) {

            borrowView.showRenewFailure("Le membre possède déja un emprunt en retard.");

            handleRenewFailureEnd();

        } catch (QuotaEmpruntAtteintException e) {

            borrowView.showRenewFailure("Le membre a atteint sa limite d'emprunts.");

            handleRenewFailureEnd();

        }

    }

    private void handleRenewFailureEnd() {

        handleUserChoice(
            Map.of(
                "R", this::handleLoanRenew,
                "A", this::handleBorrowManagement
            )
        );

    }

    // Menu 4 - Afficher tout les emprunts

    private void handleShowAllBorrowings() {

        // Recuperer tout les emprunts

        List<Emprunt> lesEmprunts = serviceEmprunt.toutLesEmprunts();

        // Afficher les emprunts

        borrowView.showAllBorrowings(lesEmprunts);

        // Gerer le retour

        handleUserChoice(Map.of("R", this::handleBorrowManagement));

    }

    // Menu 5 - Afficher tout les emprunts en retards

    private void handleShowAllLateBorrowings() {

        List<Emprunt> lateBorrowing = serviceEmprunt.obtenirEmpruntEnRetard();

        // Afficher les emprunts

        borrowView.showAllLateBorrowing(lateBorrowing);

        // Gerer le retour

        handleUserChoice(
            Map.of(
                "C", this::handleCalculateMembersPenality,
                "R", this::handleBorrowManagement
            )
        );

    }

    // Menu 6 - Calculer la pénalité d'un membre

    private void handleCalculateMembersPenality() {

        // Recuperer l'id du membre

        String idMembre = borrowView.getIdMembre();

        // Affichage de la confirmation

        borrowView.showCalculateMemberPenality(idMembre);

        // Gerer le choix

        handleUserChoice(
            Map.of(
                "C", () -> handleCalculateMemberPenalityConfirmation(idMembre),
                "A", this::handleBorrowManagement
            )
        );

    }

    private void handleCalculateMemberPenalityConfirmation(String idMembre) {

        try {

            double penalites = serviceEmprunt.calculerPenalite(idMembre);

            // Afficher le resultat

            Membre theMember = registreMembres.findById(idMembre);

            borrowView.showPenalityResult(theMember, penalites);

            // Gerer le choix

            handleUserChoice(
                Map.of(
                    "N", this::handleCalculateMembersPenality,
                    "R", this::handleBorrowManagement
                )
            );

        } catch (MembreNonTrouveException e) {

            borrowView.showPenalityResultFailure("Aucun membre trouver avec l'id : \"" + idMembre + "\"");

            // Gerer le choix

            handleUserChoice(
                Map.of(
                    "R", this::handleCalculateMembersPenality,
                    "A", this::handleBorrowManagement
                )
            );

        }

    }

    // Menu 7 - Verifier la limite d'emprunts d'un membre

    private void handleVerifyMemberLimit() {

        // Recuperer l'id du membre

        String idMembre = borrowView.getIdMembre();

        // Afficher la confirmation

        borrowView.showVerifyBorrowingMemberLimit(idMembre);

        // Gerer le choix

        handleUserChoice(
            Map.of(
                "C", () -> verifyMemberLimitConfirmation(idMembre),
                "A", this::handleBorrowManagement
            )
        );

    }

    private void verifyMemberLimitConfirmation(String idMembre) {

        // Appel du service

        try {

            serviceEmprunt.verifierLimiteEmprunt(idMembre);

            // Recuperer le membre et afficher le succès

            Membre membre = registreMembres.findById(idMembre);

            borrowView.showVerifyLimitSucces(membre);

            // Gerer le choix

            handleUserChoice(
                Map.of(
                    "N", this::handleVerifyMemberLimit,
                    "R", this::handleBorrowManagement
                )
            );


        } catch (MembreNonTrouveException e) {

            borrowView.showVerifyLimitFailure("Aucun membre trouver avec l'id : \"" + idMembre + "\"");

            handleVerifyLimitFailureEnd();

        } catch (QuotaEmpruntAtteintException e) {

            borrowView.showVerifyLimitFailure("Le membre a atteint sa limite d'emprunts.");

            handleVerifyLimitFailureEnd();

        }

    }

    private void handleVerifyLimitFailureEnd() {

        handleUserChoice(
            Map.of(
                "R", this::handleVerifyMemberLimit,
                "A", this::handleBorrowManagement
            )
        );

    }

}