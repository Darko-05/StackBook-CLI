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
public class MemberView {

    // Scanner

    private final Scanner sc = new Scanner(System.in);

    // Menu Principal

    public void showMainMemberMenu() {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.BLEU, "GESTION DES MEMBRES DE LA BIBLIOTHÈQUE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Menu

        menuView.showLine("[I] INSCRIRE UN NOUVEAU MEMBRE");
        menuView.showLine("[R] RECHERCHER UN MEMBRE");
        menuView.showLine("[A] AFFICHER TOUT LES MEMBRES");
        menuView.showLine("[M] MODIFIER LES INFORMATIONS D'UN MEMBRE");
        menuView.showLine("[S] SUSPENDRE / CHANGER LE STATUT D'UN MEMBRE");
        menuView.showLine("[V] VERIFIER L'ELIGIBILITÉ A UN EMPRUNT D'UN MEMBRE");
        menuView.showLine("[C] CONSULTER L'HISTORIQUE D'EMPRUNTS D'UN MEMBRE");
        menuView.showLine("[L] SUGGESTION DE LIVRES A UN MEMBRE");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "[0] RETOUR AU MENU PRINCIPAL"));
        menuView.showBottomBorder();

    }

    // Menu 1 - Inscrire un nouveau membre

    public void showAddNewMember(String nom, String prenom, String email, String numeroTelephone, @NonNull LocalDate dateInscription) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.BLEU, "INSCRIPTION D'UN NOUVEAU MEMBRE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Section Infos

        menuView.showLine("Informations du nouveau membre :");
        menuView.showLine();
        menuView.showLine("• Nom : " + standariserNom(nom));
        menuView.showLine("• Prénom : " + standariserNom(prenom));
        menuView.showLine("• Email : " + email);
        menuView.showLine("• Numéro de Téléphone : " + numeroTelephone);
        menuView.showLine("• Date d'Inscription : " + dateInscription.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERTCLAIR, "[S]") + " Sauvegarder l'inscription | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retourner");
        menuView.showBottomBorder();

    }

    // Méthodes de recuperation

    public String getMemberId() {

        println("Entrez l'id du membre :");

        print("> ");

        return sc.nextLine();

    }

    public String getMemberLastName() {

        println("Entrez le nom du membre :");

        print("> ");

        return sc.nextLine();

    }

    public String getMemberFirstName() {

        println("Entrez le prenom du membre :");

        print("> ");

        return sc.nextLine();

    }

    public String getMemberEmail() {

        println("Entrez l'email du membre :");

        print("> ");

        return sc.nextLine();

    }

    public String getMemberPhoneNumber() {

        println("Entrez le numero de téléphone du membre :");

        print("> ");

        return sc.nextLine();

    }

    public void showAddMemberSucces(Membre membre) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "MEMBRE INSCRIT"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Le membre \"" + standariserNomMembre.apply(membre) + "\" a été inscrit avec succès.");
        menuView.showLine("ID Membre : " + membre.getId());
        menuView.showLine("Statut initial : " + membre.getStatut());
        menuView.showLine("Date d'Inscription : " + membre.getDateInscription().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERTCLAIR, "[R]") + " Retour au Menu");
        menuView.showBottomBorder();

    }

    public void showAddMemberFailure(String message) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "ÉCHEC - INSCRIPTION MEMBRE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Raison : " + message);

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[R]") + " Réessayer l'inscription | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retour");
        menuView.showBottomBorder();

    }

    // Menu 2 - Rechercher un membre (Par nom ou par id)

    public void showSearchMemberMenu() {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROSE, "RECHERCHER UN MEMBRE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Choix

        menuView.showLine("Comment souhaitez-vous effectuer la recherche ?");
        menuView.showLine();
        menuView.showLine("Quel champ souhaitez-vous modifier pour ce membre ?");
        menuView.showLine();
        menuView.showLine("[1] Par Identifiant (Id)");
        menuView.showLine("[2] Par Nom");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[R]") + " Retour au menu");
        menuView.showBottomBorder();

    }

    public void showSearchByIdResult(String idEnter, @NonNull Membre membre) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "RÉSULTAT DE RECHERCHE PAR ID"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Choix

        menuView.showLine("ID Saisi : " + idEnter);
        menuView.showLine();
        menuView.showLine("MEMBRE TROUVÉ :");
        menuView.showLine();
        menuView.showLine("• Nom & Prénom : " + standariserNomMembre.apply(membre));
        menuView.showLine("• Email : " + membre.getEmail());
        menuView.showLine("• Tél : " + membre.getNumeroDeTelephone());
        menuView.showLine("• Date Inscription : " + membre.getDateInscription().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        menuView.showLine("• Statut : " + membre.getStatut());
        menuView.showLine("• Nombre Emprunts Actifs : " + membre.getEmpruntsActif().size());

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[N]") + " Nouvelle Recherche | " + TextModifier.colorText(TextModifier.ROUGE, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    public void showSearchByNameResult(String nameEnter, @NonNull List<Membre> membres) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.JAUNE, "RÉSULTAT DE RECHERCHE PAR NOM"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Choix

        menuView.showLine("Nom recherché : " + nameEnter);
        menuView.showLine();
        menuView.showLine(membres.size() + " membres trouvés");

        if (!membres.isEmpty()) {

            menuView.showLine();
            menuView.showLine("ID | PRÉNOM & NOM | STATUT | EMAIL");

            membres.forEach(membre ->

                menuView.showLine(membre.getId() + " | " + standariserNomMembre.apply(membre) + " | " + membre.getStatut() + " | " + membre.getEmail())

            );

        }

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[N]") + " Nouvelle Recherche | " + TextModifier.colorText(TextModifier.ROUGE, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    public void showSearchByIdFailure(String idEnter) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "ÉCHEC - MEMBRE NON TROUVÉ"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Raison : Membre non trouvé.");
        menuView.showLine("• L'ID de Membre '" + idEnter + "' n'existe pas.");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[R]") + " Réessayer | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retour");
        menuView.showBottomBorder();

    }

    // Menu 3 - Afficher tout les membres

    public void showAllMembers(@NonNull List<Membre> membres) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.TURQUOISE, "LISTE DE TOUS LES MEMBRES"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Choix

        menuView.showLine("TOTAL MEMBRES : " + membres.size());

        if (!membres.isEmpty()) {

            menuView.showLine();
            menuView.showLine("ID | PRÉNOM & NOM | STATUT | EMAIL");

            membres.forEach(membre ->

                    menuView.showLine(membre.getId() + " | " + standariserNomMembre.apply(membre) + " | " + membre.getStatut() + " | " + membre.getEmail())

            );

        }

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[R]") + " Retour au menu");
        menuView.showBottomBorder();

    }

    // Menu 4 - Modifier les informations d'un membre

    public void showMemberModificationMenu(@NonNull Membre membre) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.JAUNE, "MODIFICATION DES INFORMATIONS D'UN MEMBRE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Choix

        menuView.showLine("ID du membre à modifier : " + membre.getId());
        menuView.showLine();
        menuView.showLine("Quel champ souhaitez-vous modifier pour ce membre ?");
        menuView.showLine();
        menuView.showLine("[1] Nom (Actuel : " + standariserNom(membre.getNom()) + ")");
        menuView.showLine("[2] Prénom (Actuel : " + standariserNom(membre.getPrenom()) + ")");
        menuView.showLine("[3] Email (Actuel : " + membre.getEmail() + ")");
        menuView.showLine("[4] Numéro de Téléphone (Actuel : " + membre.getNumeroDeTelephone() + ")");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[R]") + " Retour au menu");
        menuView.showBottomBorder();

    }

    public void showMemberModificationFail(String idEnter) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "ECHEC MODIFICATION"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("ID du membre à modifier : " + idEnter);
        menuView.showLine();
        menuView.showLine("Raison : Aucun membre trouvé avec cet ID");
        menuView.showLine();

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retourner");
        menuView.showBottomBorder();

    }

    // Nom

    public void showLastNameModification(@NonNull Membre membre, String nouveauNom) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ORANGE, "MODIFICATION DU NOM DU MEMBRE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("ID du membre : " + membre.getId());
        menuView.showLine("Nom actuel : " + standariserNom(membre.getNom()));
        menuView.showLine();
        menuView.showLine("Nouveau Nom : " + standariserNom(nouveauNom));

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[V]") + " Valider | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retourner");
        menuView.showBottomBorder();

    }

    // Prenom

    public void showFirstNameModification(@NonNull Membre membre, String nouveauPrenom) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ORANGE, "MODIFICATION DU PRENOM DU MEMBRE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("ID du membre : " + membre.getId());
        menuView.showLine("Prenom actuel : " + standariserNom(membre.getPrenom()));
        menuView.showLine();
        menuView.showLine("Nouveau Prenom : " + standariserNom(nouveauPrenom));

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[V]") + " Valider | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retourner");
        menuView.showBottomBorder();

    }

    // Email

    public void showEmailModification(@NonNull Membre membre, String nouveauEmail) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ORANGE, "MODIFICATION DU MAIL DU MEMBRE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("ID du membre : " + membre.getId());
        menuView.showLine("Email actuel : " + standariserNom(membre.getEmail()));
        menuView.showLine();
        menuView.showLine("Nouveau Email : " + standariserNom(nouveauEmail));

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[V]") + " Valider | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retourner");
        menuView.showBottomBorder();

    }

    // Numero de telephone

    public void showPhoneNumberModification(@NonNull Membre membre, String nouveauNumero) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ORANGE, "MODIFICATION DU NUMERO DE TELEPHONE DU MEMBRE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("ID du membre : " + membre.getId());
        menuView.showLine("Numero actuel : " + standariserNom(membre.getNumeroDeTelephone()));
        menuView.showLine();
        menuView.showLine("Nouveau Numero : " + nouveauNumero);

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[V]") + " Valider | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retourner");
        menuView.showBottomBorder();

    }

    public void showModificationSucess(String champ, String idMembre, String nouvelleValeur) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "DONNÉE MEMBRE MODIFIÉE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Le " + champ + " du membre " + idMembre + " a été mis a jour.");
        menuView.showLine("Nouvelle valeur: " + standariserNom(nouvelleValeur));

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.BLEU, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    // Méthodes de recuperation

    public String getMemberNewLastName() {

        println("Entrez le nouveau nom du membre :");

        print("> ");

        return sc.nextLine();

    }

    public String getMemberNewFirstName() {

        println("Entrez le nouveau prenom du membre :");

        print("> ");

        return sc.nextLine();

    }

    public String getMemberNewEmail() {

        println("Entrez le nouveau mail du membre :");

        print("> ");

        return sc.nextLine();

    }

    public String getMemberNewPhoneNumber() {

        println("Entrez le nouveau numero de téléphone du membre :");

        print("> ");

        return sc.nextLine();

    }

    // Menu 5 - Suspendre un membre ou changer son statut

    public void showChangeMemberStatus(@NonNull Membre membre) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.BLEU, "CHANGEMENT DU STATUT DU MEMBRE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Choix

        menuView.showLine("ID du membre concerné : " + membre.getId());
        menuView.showLine("Statut Actuel : " + membre.getStatut());
        menuView.showLine();
        menuView.showLine("Nouveau Statut :");
        menuView.showLine("[1] ACTIF");
        menuView.showLine("[2] SUSPENDU");
        menuView.showLine("[3] INACTIF");
        menuView.showLine("[4] RETARDATAIRE");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[R]") + " Annuler et Retour");
        menuView.showBottomBorder();

    }

    public void showChangeMemberStatusSucces(@NonNull Membre membre, StatutMembre ancienStatut) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "STATUT MEMBRE MODIFIÉ"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Le statut du membre " + membre.getId() + " (" + standariserNomMembre.apply(membre) + ") a été mis a jour.");
        menuView.showLine("Ancien Statut : " + ancienStatut);
        menuView.showLine("Nouveau Statut : " + membre.getStatut());

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.BLEU, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    public void showChangeMemberStatusMembreNonTrouve(String idEnter) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "ÉCHEC - CHANGEMENT DE STATUT"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Raison : Membre non trouvé.");
        menuView.showLine("• L'ID de Membre '" + idEnter + "' n'existe pas.");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[R]") + " Réessayer | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retour");
        menuView.showBottomBorder();

    }

    public void showChangeMemberStatusMembreDejaSuspendu() {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "ÉCHEC - CHANGEMENT DE STATUT"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Raison : Membre déjà suspendu.");
        menuView.showLine("• Impossible de changer le statut d'un membre déjà SUSPENDU.");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[R]") + " Réessayer | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retour");
        menuView.showBottomBorder();

    }

    // Menu 6 - Verifier l'éligibilité d'un membre

    public void showVerifyEligibility(String idEnter) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROSE, "VÉRIFICATION D'ÉLIGIBILITÉ À L'EMPRUNT"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("ID du Membre à vérifier : " + idEnter);

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.TURQUOISE, "[V]") + " Vérifier | " + TextModifier.colorText(TextModifier.VERT, "[A]") + " Annuler et Retour");
        menuView.showBottomBorder();

    }

    public void showMemberEligibilitySucces(@NonNull Membre membre) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.CYAN, "ÉLIGIBILITÉ CONFIRMÉE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Le membre " + membre.getId() + " (" + standariserNomMembre.apply(membre) + ") est éligible a emprunté.");
        menuView.showLine("• Livres empruntés : " + membre.getNombreEmpruntsEnCours() + "/5");
        menuView.showLine("• Statut : " + membre.getStatut());

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.MAGENTA, "[N]") + " Nouvelle vérification | " + TextModifier.colorText(TextModifier.VERT, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    public void showCheckMemberEligibilityFail(String idEnter) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "ÉCHEC - CHANGEMENT DE STATUT"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Raison : Membre non trouvé.");
        menuView.showLine("• L'ID de Membre '" + idEnter + "' n'existe pas.");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[R]") + " Réessayer | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retour");
        menuView.showBottomBorder();

    }

    public void showMemberEligibilityQuotatEmpruntAtteint(@NonNull Membre membre) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "ÉCHEC - MEMBRE NON ÉLIGIBLE À L'EMPRUNT"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Le membre " + membre.getId() + " (" + standariserNomMembre.apply(membre) + ") n'est pas éligible a emprunté.");
        menuView.showLine();
        menuView.showLine("Raison : Limite d'emprunts en cours atteint");
        menuView.showLine("• Le membre a atteint la limite de 5 livres empruntés.");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[N]") + " Nouvelle vérification | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retour");
        menuView.showBottomBorder();

    }

    public void showMemberEligibilityMemberSuspended(@NonNull Membre membre) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "ÉCHEC - MEMBRE NON ÉLIGIBLE À L'EMPRUNT"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Le membre " + membre.getId() + " (" + standariserNomMembre.apply(membre) + ") n'est pas éligible a emprunté.");
        menuView.showLine();
        menuView.showLine("Raison : Membre suspendu");
        menuView.showLine("• Le membre est actuellement suspendu.");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[N]") + " Nouvelle vérification | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retour");
        menuView.showBottomBorder();

    }

    // Menu 7 - Consulter l'historique d'emprunts d'un membre

    public void showMemberBorrowHistory(@NonNull Membre membre, @NonNull List<Emprunt> historique) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.TURQUOISE, "CONSULTATION DE L'HISTORIQUE D'EMPRUNT"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Résultat

        menuView.showLine("ID du Membre : " + membre.getId() + " (" + standariserNomMembre.apply(membre) + ")");
        menuView.showLine();

        // Si le membre n'a jamais effectuer d'emprunts

        if (membre.getHistoriqueEmprunts().isEmpty()) {

            menuView.showLine("Ce membre n'a encore effectuer aucun emprunt");

        }

        // Sinon on affiche la liste d'emprunts

        else {

            AtomicInteger i = new AtomicInteger(0);

            menuView.showLine("HISTORIQUE D'EMPRUNTS (" + membre.getNombreEmpruntsEnCours() + ")");
            menuView.showLine("------------------------------------------------------------------");
            historique.forEach(emprunt -> {

                menuView.showLine(i.incrementAndGet() + ". " + standariserTitre(emprunt.getLivre()) + " (Id : " + emprunt.getLivre().getId() + ")");
                menuView.showLine("• Emprunt : " + emprunt.getDateEmprunt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " | Retour : " + emprunt.getDateRetourPrevu().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            });

        }

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    public void showMemberHistoryFail(String idEnter) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "ÉCHEC - CONSULTATION HISTORIQUE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Raison : Membre non trouvé.");
        menuView.showLine("• L'ID de Membre '" + idEnter + "' n'existe pas.");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[R]") + " Réessayer | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retour");
        menuView.showBottomBorder();

    }

    // Menu 8 - Suggestion de livres pour un membreA

    public void showBooksSuggestion(@NonNull Membre membre, @NonNull List<Livre> livresSuggerer) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.BLEU, "SUGGESTION DE LIVRES POUR MEMBRE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Résultat

        menuView.showLine("ID du Membre : " + membre.getId() + " (" + standariserNomMembre.apply(membre) + ")");
        menuView.showLine();

        // Si le membre n'a jamais effectuer d'emprunts

        if (membre.getHistoriqueEmprunts().isEmpty()) {

            menuView.showLine("Ce membre n'a encore effectuer aucun emprunt");

        }

        // Sinon

        else {

            AtomicInteger i = new AtomicInteger(0);

            menuView.showLine("Basé sur l'historique de l'emprunt et les tendances.");
            menuView.showLine();
            menuView.showLine("SUGGESTIONS :");
            menuView.showLine("------------------------------------------------------------------");
            livresSuggerer.forEach(livre -> {

                menuView.showLine(i.incrementAndGet() + ". Titre : " + standariserTitre(livre));
                menuView.showLine("• Auteur : " + standariserNomAuteur(livre.getAuteur()) + " | Catégorie : " + livre.getCategorie());

            });

        }

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    public void showBooksSuggestionFail(String idEnter) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "ÉCHEC - SUGGESTION DE LIVRES"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Raison : Membre non trouvé.");
        menuView.showLine("• L'ID de Membre '" + idEnter + "' n'existe pas.");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[R]") + " Réessayer | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retour");
        menuView.showBottomBorder();

    }

}