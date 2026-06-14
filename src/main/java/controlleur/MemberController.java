package controlleur;

import exception.MembreNonTrouveException;
import exception.MembreSuspenduException;
import exception.QuotaEmpruntAtteintException;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import model.Emprunt;
import model.Livre;
import model.Membre;
import model.StatutMembre;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static main.AppConfig.*;
import static main.AppView.memberView;
import static main.AppView.menuView;
import static util.MenuUtil.handleUserChoice;
import static util.VerificatorUtil.*;

@NoArgsConstructor
public class MemberController {

    // Methode principal

    public void handleMemberManagement() {

        // Afficher le menu principal

        memberView.showMainMemberMenu();

        // Gestion Choix

        handleUserChoice(
            Map.of(
            "I", this::handleMemberInscription,
            "R", this::handleMemberResearch,
            "A", this::handleShowAllMembers,
            "M", this::handleMemberDataModification,
            "S", this::handleMemberStatutChange,
            "V", this::handleVerifyEligibility,
            "C", this::handleShowMemberHistory,
            "L", this::showBooksSuggestionForAMember,
            "0", MainController::launchControllers
            )
        );

    }

    // Menu 1 - Inscrire un nouveau membre

    private void handleMemberInscription() {

        // Recuperer les informations necessaires

        String nom = memberView.getMemberLastName();
        String prenom = memberView.getMemberFirstName();
        String email = memberView.getMemberEmail();
        String numeroTelephone = memberView.getMemberPhoneNumber();
        LocalDate dateInscription = LocalDate.now();

        // Gerer la confirmation

        memberView.showAddNewMember(nom, prenom, email, numeroTelephone, dateInscription);

        handleUserChoice(
            Map.of(
                "S", () -> handleInscriptionConfirmation(nom, prenom, email, numeroTelephone),
                "A", this::handleMemberManagement
            )
        );

    }

    private void handleInscriptionConfirmation(String nom, String prenom, String email, String numeroTelephone) {

        // Valider les données

        try {

            validateData(nom, prenom, email, numeroTelephone);

            // Appel du service

            Membre nouveauMembre = serviceMembre.inscrireMembre(nom, prenom, email, numeroTelephone);

            // Afficher la réussite et gerer le choix

            memberView.showAddMemberSucces(nouveauMembre);

            handleUserChoice(Map.of("R", this::handleMemberManagement));

        } catch (IllegalArgumentException e) {

            memberView.showAddMemberFailure(e.getMessage());

            // Gerer le choix

            handleUserChoice(
                Map.of(
                    "R", this::handleMemberInscription,
                    "A", this::handleMemberManagement
                )
            );

        }

    }

    private void validateData(String nom, String prenom, String email, String numeroTelephone) {

        if (!nomEstValide.test(nom)) throw new IllegalArgumentException("Le nom est trop court ou contient des caractères non autorisés.");
        if (!nomEstValide.test(prenom)) throw new IllegalArgumentException("Le prenom est trop court ou contient des caractères non autorisés.");
        if (!emailValide.test(email)) throw new IllegalArgumentException("Email invalide");
        if (!numeroEstValide.test(numeroTelephone)) throw new IllegalArgumentException("Numero de télephone invalide");

    }

    // Menu 2 - Rechercher un membre (Par nom ou par id)

    private void handleMemberResearch() {

        // Afficher le menu

        memberView.showSearchMemberMenu();

        // Gerer le choix

        handleUserChoice(
            Map.of(
                "1", this::handleSearchById,
                "2", this::handleSearchByName,
                "R", this::handleMemberManagement
            )
        );

    }

    private void handleSearchById() {

        // Recuperer l'id du membre

        String idMembre = memberView.getMemberId();

        // Appel du service

        try {

            Membre memberToFind = serviceMembre.trouverMembreParId(idMembre);

            // Afficher le resultat

            memberView.showSearchByIdResult(idMembre, memberToFind);

            // Gestion du choix

            handleUserChoice(
                Map.of(
                    "N", this::handleMemberResearch,
                    "R", this::handleMemberManagement
                )
            );

        } catch (MembreNonTrouveException e) {

            memberView.showSearchByIdFailure(idMembre);

            // Gestion du choix

            handleUserChoice(
                Map.of(
                    "R", this::handleSearchById,
                    "A", this::handleMemberManagement
                )
            );

        }

    }

    private void handleSearchByName() {

        // Recuperer le nom du membre

        String nomMembre = memberView.getMemberLastName();

        // Recuperer les membres ayant le nom mentionner

        List<Membre> lesMembres = serviceMembre.trouverMembreParNom(nomMembre);

        // Affichage du resultat et gestion du choic

        memberView.showSearchByNameResult(nomMembre, lesMembres);

        handleUserChoice(
            Map.of(
                "N", this::handleMemberResearch,
                "R", this::handleMemberManagement
            )
        );

    }

    // Menu 3 - Afficher tout les membres

    private void handleShowAllMembers() {

        // Recuperer tout les membres

        List<Membre> allMembers = serviceMembre.toutLesMembres();

        // Afficher le resultat et gerer le choix

        memberView.showAllMembers(allMembers);

        handleUserChoice(Map.of("R", this::handleMemberManagement));

    }

    // Menu 4 - Modifier les informations d'un membre

    private void handleMemberDataModification() {

        // Recuperer l'id du membre

        String idMembre = memberView.getMemberId();

        // Trouver le membre

        try {

            Membre membre = serviceMembre.trouverMembreParId(idMembre);

            handleModificationChoice(membre, idMembre);

        } catch (MembreNonTrouveException e) {

            memberView.showMemberModificationFail(idMembre);

            handleUserChoice(Map.of("A", this::handleMemberManagement));

        }

    }

    private void handleModificationChoice(Membre membre, String idMembre) {

        memberView.showMemberModificationMenu(membre);

        // Gestion du choix

        handleUserChoice(
            Map.of(
                "1", () -> handleLastNameModification(membre, idMembre),
                "2", () -> handleFirstNameModification(membre, idMembre),
                "3", () -> handleEmailModification(membre, idMembre),
                "4", () -> handlePhoneNumberModification(membre, idMembre),
                "R", this::handleMemberManagement
            )
        );

    }

    private void handleLastNameModification(Membre membre, String idMembre) {

        // Recuperer un nouveau nom pour le membre

        String newLastName = memberView.getMemberNewLastName();

        // Gerer la confirmation

        memberView.showLastNameModification(membre, newLastName);

        handleUserChoice(
            Map.of(
                "V", () -> {

                    // Verification

                    if (!nomEstValide.test(newLastName)) menuView.showAFailure("ECHEC - MODIFICATION DU NOM DU MEMBRE", "Le nom est trop court ou contient des caractères non autorisés.");

                    // Appel du service

                    Membre memberModified = serviceMembre.modifierNom(idMembre, newLastName);

                    // Afficher le succes et gerer le choix

                    memberView.showModificationSucess("Nom", idMembre, memberModified.getNom());

                    handleUserChoice(Map.of("R", this::handleMemberManagement));

                },
                "A", this::handleMemberManagement
            )
        );

    }

    private void handleFirstNameModification(Membre membre, String idMembre) {

        // Recuperer un nouveau nom pour le membre

        String newFirstName = memberView.getMemberNewFirstName();

        // Gerer la confirmation

        memberView.showFirstNameModification(membre, newFirstName);

        handleUserChoice(
            Map.of(
                "V", () -> {

                    // Verification

                    if (!nomEstValide.test(newFirstName)) menuView.showAFailure("ECHEC - MODIFICATION DU PRENOM DU MEMBRE", "Le prenom est trop court ou contient des caractères non autorisés.");

                    // Appel du service

                    Membre memberModified = serviceMembre.modifierPrenom(idMembre, newFirstName);

                    // Afficher le succes et gerer le choix

                    memberView.showModificationSucess("Prenom", idMembre, memberModified.getPrenom());

                    handleUserChoice(Map.of("R", this::handleMemberManagement));

                },
                "A", this::handleMemberManagement
            )
        );

    }

    private void handleEmailModification(Membre membre, String idMembre) {

        // Recuperer le nouveau email du membre

        String newEmail = memberView.getMemberNewEmail();

        // Gerer la confirmation

        memberView.showEmailModification(membre, newEmail);

        handleUserChoice(
            Map.of(
                "V", () -> {

                    // Verification

                    if (!emailValide.test(newEmail)) menuView.showAFailure("ECHEC - MODIFICATION DE L'EMAIL DU MEMBRE", "Email invalide.");

                    // Appel du service

                    Membre memberModified = serviceMembre.modifierEmail(idMembre, newEmail);

                    // Afficher le succes et gerer le choix

                    memberView.showModificationSucess("Email", idMembre, memberModified.getEmail());

                    handleUserChoice(Map.of("R", this::handleMemberManagement));

                },
                "A", this::handleMemberManagement
            )
        );

    }

    private void handlePhoneNumberModification(Membre membre, String idMembre) {

        // Recuperer le nouveau numero de telephone

        String newPhoneNumber = memberView.getMemberNewPhoneNumber();

        // Gerer la confirmation

        memberView.showPhoneNumberModification(membre, newPhoneNumber);

        handleUserChoice(
            Map.of(
                "V", () -> {

                    // Verification

                    if (!numeroEstValide.test(newPhoneNumber)) menuView.showAFailure("ECHEC - MODIFICATION DU NUMERO DE TELEPHONE DU MEMBRE", "Numero de télephone invalide.");

                    // Appel du service

                    Membre memberModified = serviceMembre.modiferNumeroTelephone(idMembre, newPhoneNumber);

                    // Afficher le succes et gerer le choix

                    memberView.showModificationSucess("Numero de télephone", idMembre, memberModified.getNumeroDeTelephone());

                    handleUserChoice(Map.of("R", this::handleMemberManagement));

                },
                "A", this::handleMemberManagement
            )
        );

    }

    // Menu 5 - Suspendre un membre ou changer son statut

    private void handleMemberStatutChange() {

        // Recuperer l'id du membre

        String idMembre = memberView.getMemberId();

        // Appel du service

        try {

            Membre member = serviceMembre.trouverMembreParId(idMembre);

            // Afficher le resultat

            memberView.showChangeMemberStatus(member);

            // Gestion du choix

            handleStatutChanging(member);

        } catch (MembreNonTrouveException e) {

            memberView.showChangeMemberStatusMembreNonTrouve(idMembre);

            // Gestion du choix

            handleUserChoice(
                Map.of(
                    "R", this::handleMemberStatutChange,
                    "A", this::handleMemberManagement
                )
            );

        }

    }

    private void handleStatutChanging(Membre member) {

        // Gestion des cases

        handleUserChoice(
            Map.of(
                "1", () -> changeState(member, StatutMembre.ACTIF),
                "2", () -> changeState(member, StatutMembre.SUSPENDU),
                "3", () -> changeState(member, StatutMembre.INACTIF),
                "4", () -> changeState(member, StatutMembre.RETARDATAIRE),
                "R", this::handleMemberManagement
            )
        );

    }

    private void changeState(@NonNull Membre member, StatutMembre statut) {

        // Changer le statut

        StatutMembre oldState = member.getStatut();

        try {

            Membre memberChanged = serviceMembre.changerStatutMembre(member.getId(), statut);

            // Afficher le resultat

            memberView.showChangeMemberStatusSucces(memberChanged, oldState);

            // Gestion du choix

            handleUserChoice(Map.of("R", this::handleMemberManagement));

        } catch (MembreSuspenduException e) {

            memberView.showChangeMemberStatusMembreDejaSuspendu();

            handleUserChoice(
                Map.of(
                    "R", this::handleMemberStatutChange,
                    "A", this::handleMemberManagement
                )
            );

        }

    }

    // Menu 6 - Verifier l'éligibilité d'un membre

    private void handleVerifyEligibility() {

        // Recuperer l'id du membre

        String idMembre = memberView.getMemberId();

        // Afficher la confirmation

        memberView.showVerifyEligibility(idMembre);

        // Gerer le choix

        handleUserChoice(
            Map.of(
                "V", () -> verifyMemberEligibility(idMembre),
                "A", this::handleMemberManagement
            )
        );

    }

    private void verifyMemberEligibility(String idMembre) {

        Membre member = registreMembres.findById(idMembre);

        try {

            serviceMembre.verifierEligibiliteEmprunt(idMembre);

            memberView.showMemberEligibilitySucces(member);

            handleVerifySuccesEnd();

        } catch (MembreNonTrouveException e) {

            memberView.showCheckMemberEligibilityFail(idMembre);

            handleVerifyMemberNotFound();

        } catch (MembreSuspenduException e) {

            memberView.showMemberEligibilityMemberSuspended(member);

            handleVerifyFailureEnd();

        } catch (QuotaEmpruntAtteintException e) {

            memberView.showMemberEligibilityQuotatEmpruntAtteint(member);

            handleVerifyFailureEnd();

        }

    }

    private void handleVerifySuccesEnd() {

        handleUserChoice(
            Map.of(
                "N", this::handleVerifyEligibility,
                "R", this::handleMemberManagement
            )
        );

    }

    private void handleVerifyMemberNotFound() {

        handleUserChoice(
            Map.of(
                "R", this::handleVerifyEligibility,
                "A", this::handleMemberManagement
            )
        );

    }

    private void handleVerifyFailureEnd() {

        handleUserChoice(
            Map.of(
                "N", this::handleVerifyEligibility,
                "A", this::handleMemberManagement
            )
        );

    }

    // Menu 7 - Consulter l'historique d'emprunts d'un membre

    private void handleShowMemberHistory() {

        // Recuperer l'id du membre

        String idMembre = memberView.getMemberId();

        // Recuperer les données

        try {

            Membre member = serviceMembre.trouverMembreParId(idMembre);

            // Appel du service

            List<Emprunt> memberHistory = serviceMembre.consulterHistoriqueMembre(idMembre);

            // Afficher le resultat

            memberView.showMemberBorrowHistory(member, memberHistory);

            // Gestion du choix

            handleUserChoice(Map.of("R", this::handleMemberManagement));

        } catch (MembreNonTrouveException e) {

            memberView.showMemberHistoryFail(idMembre);

            handleUserChoice(
                Map.of(
                    "R", this::handleShowMemberHistory,
                    "A", this::handleMemberManagement
                )
            );

        }

    }

    // Menu 8 - Suggestion de livres pour un membre

    private void showBooksSuggestionForAMember() {

        // Recuperer l'id du membre

        String idMembre = memberView.getMemberId();

        // Appel du service

        try {

            Membre member = serviceMembre.trouverMembreParId(idMembre);

            List<Livre> booksSuggested = serviceMembre.suggererLivreMembre(idMembre);

            memberView.showBooksSuggestion(member, booksSuggested);

            handleUserChoice(Map.of("R", this::handleMemberManagement));

        } catch (MembreNonTrouveException e) {

            memberView.showBooksSuggestionFail(idMembre);

            handleUserChoice(
                Map.of(
                    "R", this::showBooksSuggestionForAMember,
                    "A", this::handleMemberManagement
                )
            );

        }

    }

}
