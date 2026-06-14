package view;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import model.MaisonEdition;
import model.TextModifier;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import static java.lang.IO.print;
import static java.lang.IO.println;
import static main.AppView.menuView;
import static util.DisplayUtil.standariserNom;

@Slf4j
@NoArgsConstructor
public class HomeEditorView {

    // Scanner

    private final static Scanner sc = new Scanner(System.in);

    // Menu - Maison d'edition

    public void showHomeEditorMenu(long nombreTotalMaisonEdition) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROSE, "GESTION DES MAISONS D'ÉDITION"));
        menuView.showMiddleBorder();

        // Choix menu

        menuView.showLine();
        showHomeEditorMenuChoice();
        menuView.showLine();

        // Pied de page

        menuView.showMiddleBorder();
        menuView.showCenteredLine("Total maisons d'édition : " + TextModifier.colorText(TextModifier.VERT, String.valueOf(nombreTotalMaisonEdition)));
        menuView.showBottomBorder();

    }

    void showHomeEditorMenuChoice() {

        menuView.showLine("[A] AJOUTER UNE MAISON D'ÉDITION");
        menuView.showLine("[S] SUPPRIMER UNE MAISON D'ÉDITION");
        menuView.showLine("[R] RECHERCHER UNE MAISON D'ÉDITION");
        menuView.showLine("[L] LISTER TOUTES LES MAISONS D'ÉDITION");

        menuView.showLine();
        menuView.showLine(TextModifier.colorText(TextModifier.ROUGE, "[0] RETOUR AU MENU PRINCIPAL"));

    }

    // Menu 1 - Ajouter Maison d'édition

    public void showConfirmAddHomeEditor(String nom, String codePostal, String numeroTelephone, String ville, String dateCreation) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.CYAN, "AJOUT D'UNE MAISON D'ÉDITION"));
        menuView.showMiddleBorder();

        // Resultat de remplissage

        menuView.showLine();
        showAddHomeEditorResult(nom, codePostal, numeroTelephone, ville, dateCreation);
        menuView.showLine();

        // Pied de page

        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[V]") + " Valider l'ajout | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et retourner au menu");
        menuView.showBottomBorder();

    }

    public void showAddindHomeEditorSucces(@NonNull MaisonEdition maisonEdition) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERTCLAIR, "SUCCES - MAISON D'ÉDITION AJOUTEE"));
        menuView.showMiddleBorder();

        // Resultat de remplissage

        menuView.showLine();
        showAddHomeEditorResult(maisonEdition.nom(), maisonEdition.codePostal(), maisonEdition.numeroDeTelephone(), maisonEdition.ville(), maisonEdition.dateDeCreation().toString());
        menuView.showLine();

        // Pied de page

        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.TURQUOISE, "[A]") + " Autre Ajout | " + TextModifier.colorText(TextModifier.VERT, "[R]") + " Retour au menu");
        menuView.showBottomBorder();

    }

    void showAddHomeEditorResult(String nom, String codePostal, String numeroTelephone, String ville, String dateCreation) {

        menuView.showLine("Nom : " + standariserNom(nom));
        menuView.showLine();
        menuView.showLine("Code postal : " + codePostal);
        menuView.showLine();
        menuView.showLine("Numéro de téléphone : " + numeroTelephone);
        menuView.showLine();
        menuView.showLine("Ville : " + standariserNom(ville));
        menuView.showLine();
        menuView.showLine("Date de création : " + dateCreation);

    }

    // Recuperation pour ajout

    public String getHomeEditorName() {

        println("Entrez le nom de la maison d'édition : ");
        print("> ");

        return sc.nextLine();

    }

    public String getHomeEditorPostalCode() {

        println("Entrez le code postal (5 chiffres minimum) : ");
        print("> ");

        return sc.nextLine();

    }

    public String getHomeEditorPhoneNumber() {

        println("Entrez le numero de téléphone (8 <= N >= 10 chiffres) : ");
        print("> ");

        return sc.nextLine();

    }

    public String getHomeEditorCity() {

        println("Entrez la ville : ");
        print("> ");

        return sc.nextLine();

    }

    public String getHomeEditorDateOfCreation() {

        println("Entrez la date de création (Format : yyyy/MM/dd) : ");
        print("> ");

        return sc.nextLine();

    }

    // Menu 2 - Supprimer une maison d'édition

    public void showDeleteProcessHomeEditor(@NonNull MaisonEdition maisonEditionTrouve, long nombreLivresAssocie) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.CYAN, "SUPPRESSION D'UNE MAISON D'ÉDITION"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Info Suppresion

        showHomeEditorToDeleteResult(maisonEditionTrouve, nombreLivresAssocie);

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "[S]") + " Confirmer la suppression | " + TextModifier.colorText(TextModifier.ORANGE, "[A]") + " Annuler et retourner");
        menuView.showBottomBorder();

    }

    public String getHomeEditorToDeleteName() {

        println();
        println("Nom de la maison d'édition a supprimer : ");

        print("> ");

        return sc.nextLine();

    }

    public void showHomeEditorNotFind(String enteredName) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.CYAN, "SUPPRESSION D'UNE MAISON D'ÉDITION"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Nom recherché : \"" + standariserNom(enteredName) + "\"");
        menuView.showLine();
        menuView.showLine(TextModifier.colorText(TextModifier.ROUGE, "Aucune maison d'édition trouvée avec ce nom."));

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.BLEU, "[R]") + " Réessayer | " + TextModifier.colorText(TextModifier.ORANGE, "[A]") + " Annuler et retourner");
        menuView.showBottomBorder();

    }

    void showHomeEditorToDeleteResult(@NonNull MaisonEdition maisonEditionTrouve, long nombreLivresAssocie) {

        // Maison d'édition trouvée

        menuView.showLine("Maison d'édition trouvée :");
        menuView.showLine();
        menuView.showLine("Nom : " + standariserNom(maisonEditionTrouve.nom()));
        menuView.showLine("Ville : " + standariserNom(maisonEditionTrouve.ville()));
        menuView.showLine("Téléphone : " + maisonEditionTrouve.numeroDeTelephone());
        menuView.showLine("Date de création : " + maisonEditionTrouve.dateDeCreation().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        menuView.showLine();

        // Si aucun livre n'est associée a la maison d'édition

        if (nombreLivresAssocie == 0) return;

        // Si il a des livres associés

        menuView.showLine("Cette maison est associée à " + TextModifier.colorText(TextModifier.ROUGE, String.valueOf(nombreLivresAssocie)) + " livres.");

        menuView.showLine("La suppression échouera si des livres y sont encore rattachés.");

    }

    // Suppresion reussie

    public void showSucessfulDeletionResult(long totalRestant) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERTCLAIR, "SUPPRESSION EFFECTUÉE"));
        menuView.showMiddleBorder();

        // Info utiles

        menuView.showLine();
        menuView.showLine("La maison d'édition a été supprimée avec succès.");
        menuView.showLine();
        menuView.showLine("────────────────────────────────────────────────────────");
        menuView.showLine("Total restant : " + TextModifier.colorText(TextModifier.VERT, String.valueOf(totalRestant)) + " maisons d'édition");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[R]") + " Retour au Menu Principal | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Autre Suppression");
        menuView.showBottomBorder();

    }

    // Echec de la suppresion

    public void showSuppresionFailed(@NonNull MaisonEdition homeEditorToDelete, long nombreLivresAssocies) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "SUPPRESSION IMPOSSIBLE"));
        menuView.showMiddleBorder();

        // Info utiles

        menuView.showLine();
        menuView.showLine("Impossible de supprimer cette maison d'édition.");
        menuView.showLine();
        menuView.showLine("Nom : " + standariserNom(homeEditorToDelete.nom()));
        menuView.showLine("Raison : " + TextModifier.colorText(TextModifier.VERTCLAIR, String.valueOf(nombreLivresAssocies)) + " livres sont encore associés à cette maison.");
        menuView.showLine();
        menuView.showLine("• Vous devez d'abord :");
        menuView.showLine("1. Supprimer ou réaffecter les livres concernés");
        menuView.showLine("2. Puis retenter la suppression");
        menuView.showLine();

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[R]") + " Retour au Menu Principal | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Autre Suppression");
        menuView.showBottomBorder();

    }

    // Menu 3 - Rechercher une maison d'édition

    public void showHomeEditorResearch(@NonNull List<MaisonEdition> resultat) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.TURQUOISE, "RECHERCHE D'UNE MAISON D'ÉDITION"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Resultat recherche

        showResearchResult(resultat);

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROSE, "[N]") + " Nouvelle Recherche | " + TextModifier.colorText(TextModifier.ROUGE, "[R]") + " Retour au Menu");
        menuView.showBottomBorder();

    }

    void showResearchResult(@NonNull List<MaisonEdition> resultat) {

        menuView.showLine("Résultats (" + TextModifier.colorText(TextModifier.VERT, String.valueOf(resultat.size())) + " maison(s) d'édition touvées ) :");
        menuView.showLine();

        resultat.forEach(maisonEdition ->

                {
                    menuView.showLine("• " + standariserNom(maisonEdition.nom()));
                    menuView.showLine(standariserNom(maisonEdition.ville()) + ", | Tél : " + maisonEdition.numeroDeTelephone() + " | Créer le : " + maisonEdition.dateDeCreation().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
                    menuView.showLine();
                }

        );

    }

    // Menu 4 - Lister toute les maisons d'édition

    public void showAllHomeEditors(@NonNull List<MaisonEdition> allHomeEditor) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.JAUNE, "LISTE COMPLÈTE DES MAISONS D'ÉDITION") + " (" + TextModifier.colorText(TextModifier.VERTCLAIR, String.valueOf(allHomeEditor.size())) + ")");
        menuView.showMiddleBorder();
        menuView.showLine();

        // Liste des maisons d'éditions

        allHomeEditor.forEach(maisonEdition ->

            {
                menuView.showLine("• " + standariserNom(maisonEdition.nom()));
                menuView.showLine(standariserNom(maisonEdition.ville()) + ", | Tél : " + maisonEdition.numeroDeTelephone() + " | Créer le : " + maisonEdition.dateDeCreation().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
                menuView.showLine();
            }

        );

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.MAGENTA, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    // Recuperation pour recherche

    public String getAuthorNamesForResearch() {

        println();
        println("Entrez le nom de la maison d'édition :");

        print("> ");

        return sc.nextLine();

    }

}
