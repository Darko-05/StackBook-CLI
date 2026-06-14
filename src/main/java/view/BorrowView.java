package view;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import model.Emprunt;
import model.Membre;
import model.TextModifier;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import static java.lang.IO.print;
import static java.lang.IO.println;
import static main.AppView.menuView;
import static util.DisplayUtil.standariserNomMembre;
import static util.DisplayUtil.standariserTitre;

@NoArgsConstructor
public class BorrowView {

    // Scanner

    private final Scanner sc = new Scanner(System.in);

    // Menu Principal

    public void showBooksBorrowMainMenu() {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ORANGE, "GESTION DES EMPRUNTS DE LA BIBLIOTHÈQUE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Menu

        menuView.showLine("[E] EMPRUNTER UN LIVRE (Nouvel Emprunt)");
        menuView.showLine("[R] RETOURNER UN LIVRE");
        menuView.showLine("[RE] RENOUVELLER UN EMPRUNT");
        menuView.showLine("[A] AFFICHER L'HISTORIQUE D'EMPRUNTS");
        menuView.showLine("[C] CONSULTER LES EMPRUNTS EN RETARDS");
        menuView.showLine("[P] CALCULER LA PENALITE D'UN MEMBRE");
        menuView.showLine("[V] VERIFIER LA LIMITE D'EMPRUNT D'UN MEMBRE");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "[0] RETOUR AU MENU PRINCIPAL"));
        menuView.showBottomBorder();

    }

    // Menu 1 - Emprunter un livre

    public void showBorrowing(String idLivre, String idMembre) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.JAUNE, "ENREGISTREMENT D'UN NOUVEL EMPRUNT"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Identifiants pour l'emprunts :");
        menuView.showLine();
        menuView.showLine("• ID du Livre : " + idLivre);
        menuView.showLine("• ID du Membre : " + idMembre);

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERTCLAIR, "[V]") + " Valider d'emprunt | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retour");
        menuView.showBottomBorder();

    }

    public void showBorrowingSucces(@NonNull Emprunt nouvelEmprunt) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "EMPRUNT ENREGISTRÉ"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("L'emprunt a été enregistré avec succès.");
        menuView.showLine();
        menuView.showLine("• Membre : " + nouvelEmprunt.getMembre().getId() + " (" + standariserNomMembre.apply(nouvelEmprunt.getMembre()) + ")");
        menuView.showLine(" • Livre : " + standariserTitre(nouvelEmprunt.getLivre()) + " (" + nouvelEmprunt.getLivre().getIsbn() + ")");
        menuView.showLine("• Date d'Emprunt : " + nouvelEmprunt.getDateEmprunt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        menuView.showLine("• Date de Retour Prévue : " + nouvelEmprunt.getDateRetourPrevu().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROSE, "[N]") + " Nouvel Emprunt | " + TextModifier.colorText(TextModifier.ROUGE, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    public void showBorrowingFailure(String raison) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "ÉCHEC - ENREGISTREMENT EMPRUNT"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("L'emprunt n'a pas pu être validé pour la raison suivante :");
        menuView.showLine();
        menuView.showLine("Raison :");
        menuView.showLine(" • " + raison);

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ORANGE, "[R]") + " Réessayer | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retour");
        menuView.showBottomBorder();

    }

    // Menu 2 - Enregistrer un retour

    public void showLoanReturn(String idLivre, String idMembre) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.JAUNE, "ENREGISTREMENT D'UN RETOUR"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Le livre et le membre pour le retour :");
        menuView.showLine();
        menuView.showLine("• ID du Livre : " + idLivre);
        menuView.showLine("• ID du Membre : " + idMembre);

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERTCLAIR, "[V]") + " Valider | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retour");
        menuView.showBottomBorder();

    }

    public void showLoanReturnSucces(@NonNull Emprunt empruntRetourne) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERTCLAIR, "RETOUR ENREGISTRÉ"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Le livre a été retourné avec succès.");
        menuView.showLine();
        menuView.showLine("• Membre : " + empruntRetourne.getMembre().getId() + " (" + standariserNomMembre.apply(empruntRetourne.getMembre()) + ")");
        menuView.showLine("• Livre : " + standariserTitre(empruntRetourne.getLivre()) + " (" + empruntRetourne.getLivre().getIsbn() + ")");
        menuView.showLine("• Date de Retour Effective : " + empruntRetourne.getDateRetourEffective().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        menuView.showLine("• Pénalités : " + empruntRetourne.getPenalite() + "€");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROSE, "[N]") + " Nouveau Retour | " + TextModifier.colorText(TextModifier.ROUGE, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    public void showLoanReturnFailure(String raison) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "ÉCHEC - ENREGISTREMENT RETOUR"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Le retour n'a pas pu être traité pour la raison suivante :");
        menuView.showLine();
        menuView.showLine("Raison :");
        menuView.showLine(" • " + raison);

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ORANGE, "[R]") + " Réessayer | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retour");
        menuView.showBottomBorder();

    }

    // Menu 3 - Renouveller un emprunt

    public void showRenewBorrowing(String idLivre, String idMembre) {

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.TURQUOISE, "RENOUVELLEMENT D'UN EMPRUNT"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Informations nécessaires :");
        menuView.showLine();
        menuView.showLine("• ID du Livre : " + idLivre);
        menuView.showLine("• ID du Membre : " + idMembre);

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERTCLAIR, "[V]") + " Valider le Renouvellement | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retour");
        menuView.showBottomBorder();

    }

    public void showRenewSucces(@NonNull Emprunt empruntRenouveller) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.CYAN, "RENOUVELLEMENT ACCEPTÉ"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("L'emprunt a été renouvelé avec succès.");
        menuView.showLine();
        menuView.showLine("• Livre : " + standariserTitre(empruntRenouveller.getLivre()) + " (" + empruntRenouveller.getLivre().getIsbn() + ")");
        menuView.showLine("• Membre : " + empruntRenouveller.getMembre().getId() + " (" + standariserNomMembre.apply(empruntRenouveller.getMembre()) + ")");
        menuView.showLine("• Ancienne Date de Retour : " + empruntRenouveller.getDateRetourPrevu().minusDays(21).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        menuView.showLine("• Nouvelle Date de Retour : " + empruntRenouveller.getDateRetourPrevu().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        menuView.showLine("• Nombre de Renouvellements : " + empruntRenouveller.getNombreRenouvellement() + "/2");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.MAGENTA, "[N]") + " Nouveau Renouvellement | " + TextModifier.colorText(TextModifier.ROUGE, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    public void showRenewFailure(String raison) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "ÉCHEC - RENOUVELLEMENT REFUSÉ"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Le renouvellement n'a pas pu être effectué pour la raison suivante :");
        menuView.showLine();
        menuView.showLine("Raison :");
        menuView.showLine(" • " + raison);

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ORANGE, "[R]") + " Réessayer | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retour");
        menuView.showBottomBorder();

    }

    // Menu 4 - Afficher tout les emprunts

    public void showAllBorrowings(@NonNull List<Emprunt> toutLesEmprunts) {

        menuView.showBigTopBorder();
        menuView.showBigCenteredLine(TextModifier.colorText(TextModifier.ROSE, "LISTE DE TOUT LES EMPRUNTS"));
        menuView.showBigMiddleBorder();
        menuView.showBigLine();

        // Détails

        showAllBorrowing("total d'emprunts", toutLesEmprunts);

        // Pied de page

        menuView.showBigLine();
        menuView.showBigMiddleBorder();
        menuView.showBigCenteredLine(TextModifier.colorText(TextModifier.BLEU, "[R]") + " Retour");
        menuView.showBigBottomBorder();

    }

    void showAllBorrowing(String totalOuEnretard, @NonNull List<Emprunt> emprunts) {

        menuView.showBigLine("Nombre " + totalOuEnretard + " : " + emprunts.size());

        // Si il n'y a encore aucun emprunts

        if (emprunts.isEmpty()) {

            menuView.showBigLine("Il n'y a eu encore aucun emprunts dans le système");

        }

        // Sinon ...

        else {

            menuView.showBigLine();
            menuView.showBigLine("ID MEMBRE | LIVRE (Titre)        | DATE RETOUR PRÉVUE | ETAT");
            menuView.showBigLine("----------|----------------------|--------------------|-------------");
            emprunts.forEach(emprunt ->

                    menuView.showBigLine(emprunt.getMembre().getId() + "    | " + standariserTitre(emprunt.getLivre()) + "             | " + emprunt.getDateRetourPrevu().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "         | " + emprunt.getEtatEmprunt())

            );

        }

    }

    // Menu 5 - Afficher tout les emprunts en retards

    public void showAllLateBorrowing(@NonNull List<Emprunt> empruntsEnRetard) {

        menuView.showBigTopBorder();
        menuView.showBigCenteredLine(TextModifier.colorText(TextModifier.ROSE, "LISTE DES EMPRUNTS EN RETARD"));
        menuView.showBigMiddleBorder();
        menuView.showBigLine();

        // Détails

        showAllBorrowing("d'emprunts en retards", empruntsEnRetard);

        // Pied de page

        menuView.showBigLine();
        menuView.showBigMiddleBorder();
        menuView.showBigCenteredLine(TextModifier.colorText(TextModifier.CYAN, "[C]") + " Calculer Pénalité pour un Membre | " + TextModifier.colorText(TextModifier.BLEU, "[R]") + " Retour");
        menuView.showBigBottomBorder();

    }

    // Menu 6 - Calculer la pénalité d'un membre

    public void showCalculateMemberPenality(String idEnter) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.TURQUOISE, "CALCUL DE PÉNALITÉ"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("ID du Membre : " + idEnter);

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.CYAN, "[C]") + " Calculer | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retour");
        menuView.showBottomBorder();

    }

    public void showPenalityResult(@NonNull Membre membre, double penaliteDuMembre) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERTCLAIR, "RÉSULTAT PÉNALITÉ"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine(TextModifier.colorText(TextModifier.CYAN, "Membre") + " : " + membre.getId() + " (" + standariserNomMembre.apply(membre) + ")");
        menuView.showLine();
        menuView.showLine("• Nombre d'emprunts en retard :  " + membre.nombreEmpruntsEnRetards());
        menuView.showLine("• Total Jours de Retard cumulés : " + membre.nombreJoursEnRetards());
        menuView.showLine();

        if (membre.nombreEmpruntsEnRetards() == 0) {

            menuView.showLine("Ce membre n'a aucune pénalité a payer");

        }

        else {

            menuView.showLine(TextModifier.modifyText(TextModifier.SOULIGNE, TextModifier.colorText(TextModifier.VERTCLAIR, "• MONTANT TOTAL DE LA PÉNALITÉ")) + " : " + penaliteDuMembre + "€");
            menuView.showLine();
            menuView.showLine("500 F / Jour");

        }

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ORANGE, "[N]") + " Nouveau Calcul | " + TextModifier.colorText(TextModifier.ROUGE, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    public void showPenalityResultFailure(String raison) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "ÉCHEC - RÉSULTAT PÉNALITÉ"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("La pénalité du membre n'a pas pu etre calculer :");
        menuView.showLine();
        menuView.showLine("Raison :");
        menuView.showLine(" • " + raison);

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ORANGE, "[R]") + " Réessayer | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retour");
        menuView.showBottomBorder();

    }

    // Menu 7 - Verifier la limite d'emprunts d'un membre

    public void showVerifyBorrowingMemberLimit(String idEnter) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.JAUNE, "VÉRIFICATION LIMITE D'EMPRUNT"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("ID du Membre : " + idEnter);

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ORANGE, "[C]") + " Calculer | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retour");
        menuView.showBottomBorder();

    }

    public void showVerifyLimitSucces(@NonNull Membre membre) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERTCLAIR, "LIMITE D'EMPRUNT NON ATTEINTE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        int nombreEmpruntsActif = membre.getEmpruntsActif().size();

        menuView.showLine("Membre : " + membre.getId() + " (" + standariserNomMembre.apply(membre) + ")");
        menuView.showLine("Statut : " + membre.getStatut());
        menuView.showLine();
        menuView.showLine("Détails :");
        menuView.showLine("• Nombre d'emprunts actuels : " + nombreEmpruntsActif);
        menuView.showLine("• Limite maximale : 5");

        if (nombreEmpruntsActif >= 5) {

            menuView.showLine("• Le membre a atteint sa limite d'emprunts");

        }

        else {

            menuView.showLine("• Le membre peut emprunter " + (5 - nombreEmpruntsActif) + "livres supplémentaires");

        }

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.BLEU, "[N]") + " Nouvelle verification | " + TextModifier.colorText(TextModifier.ROUGE, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    public void showVerifyLimitFailure(String raison) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "ÉCHEC - VERIFICATION DE LA LIMITE D'EMPRUNTS"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("La vérification na pas pu etre effectuer :");
        menuView.showLine();
        menuView.showLine("Raison :");
        menuView.showLine(" • " + raison);

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ORANGE, "[R]") + " Réessayer | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retour");
        menuView.showBottomBorder();

    }

    // Méthodes de recuperation

    public String getIdLivre() {

        println("Entrez l'id du livre :");

        print("> ");

        return sc.nextLine();

    }

    public String getIdMembre() {

        println("Entrez l'id du membre :");

        print("> ");

        return sc.nextLine();

    }

}