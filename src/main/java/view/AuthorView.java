package view;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import model.Auteur;
import model.Livre;
import model.TextModifier;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.lang.IO.print;
import static java.lang.IO.println;
import static util.DisplayUtil.*;
import static main.AppView.menuView;

@NoArgsConstructor
public class AuthorView {

    // Scanner

    private final static Scanner sc = new Scanner(System.in);

    // Menu Auteur

    public void showAuthorMenu(long nombreTotalAuteurs, Map<Auteur, Long> top2AuteursPlusProlifiques) {

        menuView.showTopBorder();

        // Titre menu

        menuView.showCenteredLine(TextModifier.modifyText(TextModifier.SOULIGNE, TextModifier.colorText(TextModifier.JAUNE, "GESTION DES AUTEURS")));

        menuView.showMiddleBorder();

        // Choix possible

        menuView.showLine();

        showAuthorChoiceMenu();

        // Pied de page

        menuView.showMiddleBorder();

        showAuthorFooter(nombreTotalAuteurs, top2AuteursPlusProlifiques);

        // Fin

        menuView.showBottomBorder();

    }

    // Choix Menu - Auteur

    public void showAuthorChoiceMenu() {

        menuView.showLine("[A]. AJOUTER AUTEUR");
        menuView.showLine("[B]. RECHERCHER UN AUTEUR");
        menuView.showLine("[M]. SUPPRIMER UN AUTEUR");
        menuView.showLine("[L]. LISTER TOUT LES AUTEUR");

        menuView.showLine();

        menuView.showLine(TextModifier.colorText(TextModifier.ROUGE, "[0] RETOUR AU MENU PRINCIPAL"));

    }

    // Pied de page

    void showAuthorFooter(long nombreTotalAuteurs, @NonNull Map<Auteur, Long> top2AuteursPlusProlifiques) {

        AtomicInteger i = new AtomicInteger(0);

        // Affichage

        menuView.showCenteredLine("Total Auteurs : " + TextModifier.colorText(TextModifier.VERT, String.valueOf(nombreTotalAuteurs)) + " | Auteurs les plus prolifiques");
        menuView.showLine(

                // Chaine auteur prolifique

                top2AuteursPlusProlifiques.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(cle_valeur ->

                    i.incrementAndGet() + ". " + standariserNomAuteur(cle_valeur.getKey()) + " (" + TextModifier.colorText(TextModifier.VERT, String.valueOf(cle_valeur.getValue())) + " livres)"

                )
                .collect(Collectors.joining(" | "))

        );

    }

    // Menu 1 - Ajouter un auteur

    public void showAddAuthor(String nom, String prenom, String nationalite, String description) {

        // En tete

        showAddAuthorHeader();

        // Formulaire de remplissage

        menuView.showLine();
        menuView.showLine("Nom : " + standariserNom(nom));
        menuView.showLine("Prenom : " + standariserNom(prenom));
        menuView.showLine();
        menuView.showLine("Nationalité : " + standariserNom(nationalite));
        menuView.showLine();
        menuView.showLine("Biographie :");
        menuView.showLine(descriptionWrapper(description));

        // Pied de page

        showAddAuthorFooter();

    }

    void showAddAuthorHeader() {

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.BLEU, "AJOUTER UN AUTEUR"));
        menuView.showMiddleBorder();

    }

    void showAddAuthorFooter() {

        menuView.showLine();
        menuView.showMiddleBorder();

        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[C]") + " CONFIRMER | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " ANNULER");

        menuView.showBottomBorder();

    }

    String descriptionWrapper(@NonNull String description) {

        return description.length() <= 50 ? description : description.substring(0, 50) + "...";

    }

    public String getAuthorLastName() {

        println("Entrez le nom de l'auteur :");

        print("> ");

        return sc.nextLine();

    }

    public String getAuthorFirstName() {

        println("Entrez le prenom de l'auteur :");

        print("> ");

        return sc.nextLine();

    }

    public String getAuthorNationality() {

        println("Entrez sa nationalité :");

        print("> ");

        return sc.nextLine();

    }

    public String getAuthorBiography() {

        println("Entrez une courte biographie :");

        print("> ");

        return sc.nextLine();

    }

    public void showAuthorRecordingSucces(@NonNull Auteur auteur) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.modifyText(TextModifier.ITALIQUE, TextModifier.colorText(TextModifier.CYAN, "AUTEUR AJOUTE AVEC SUCCES")));
        menuView.showMiddleBorder();

        // Info Auteur

        menuView.showLine();
        menuView.showLine("Nom : " + standariserNom(auteur.nom()) + " " + standariserNom(auteur.prenom()));
        menuView.showLine("Nationalité : " + standariserNom(auteur.nationalite()));
        menuView.showLine();

        // Suite de menu

        menuView.showLine("[1]. Ajouter un autre auteur");
        menuView.showLine("[2]. Retour au menu auteurs");

        menuView.showLine();
        menuView.showLine(TextModifier.modifyText(TextModifier.SOULIGNE, TextModifier.colorText(TextModifier.ROUGE,"[0]. Retour au menu principal")));

        // Pied de page

        menuView.showBottomBorder();

    }

    // Menu 2 - Rechercher un auteur

    public void searchAuthor(String nom_ou_prenom, Map<Auteur, Long> resultatsDeRecherche) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROSE, "RECHERCHER UN AUTEUR"));
        menuView.showMiddleBorder();

        // Affichage de resultat

        showSearchAuthorResult(nom_ou_prenom, resultatsDeRecherche);

        // Suite Menu

        menuView.showLine();
        menuView.showLine("ACTIONS : " + TextModifier.colorText(TextModifier.VERT, "[V]") + " VOIR LIVRES" + " | " + TextModifier.colorText(TextModifier.ROUGE, "[S]") + " SUPPRIMER");
        menuView.showLine();

        // Pied de page

        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[N]") + " NOUVELLE RECHERCHE | " + TextModifier.colorText(TextModifier.ROUGE, "[B]") + " RETOUR");
        menuView.showBottomBorder();

    }

    public String getAuthorNamesForResearch() {

        println();
        println("Entrez le nom ou le prenom de l'auteur :");

        print("> ");

        return sc.nextLine();

    }

    void showSearchAuthorResult(String nom_ou_prenom, @NonNull Map<Auteur, Long> resultatsDeRecherche) {

        // Affichage de resultat

        menuView.showLine();
        menuView.showLine("Nom | Prenom : " + nom_ou_prenom);

        menuView.showLine();
        menuView.showLine("Résultats (" + resultatsDeRecherche.size() + " trouvés) :");
        menuView.showLine();

        // Liste des auteurs

        AtomicInteger i = new AtomicInteger(0);

        resultatsDeRecherche.forEach((auteur, nombreLivres) ->

            menuView.showLine(i.incrementAndGet() + ". " + auteur.nom().toUpperCase() + " " + standariserNom(auteur.prenom()) + " - " + TextModifier.colorText(TextModifier.VERTCLAIR, String.valueOf(nombreLivres)) + " livres")

        );

    }

    // Menu 2 - 1

    public String chooseAuthor(@NonNull Map<Long, Auteur> auteurs) {

        println();

        auteurs.forEach((index, auteur) -> println(index + ". " + standariserNomAuteur(auteur)));

        println();
        println("Choisissez un auteur :");

        print("> ");

        return sc.nextLine();

    }

    public void showAuthorBooks(Auteur auteur, @NonNull List<Livre> livresAuteur) {

        // En tete

        menuView.showBigTopBorder();
        menuView.showBigCenteredLine("LIVRES DE : " + TextModifier.colorText(TextModifier.VERT, standariserNomAuteur(auteur)));
        menuView.showBigMiddleBorder();

        // Resultat de recherche

        AtomicInteger i = new AtomicInteger(0);

        livresAuteur.forEach(livre ->

            menuView.showBigLine(i.incrementAndGet() + ". " + standariserNom(livre.getTitre()) + " (" + livre.getAnneePublication() + ") - " + standariserEnum(livre.getCategorie()) + " " + livre.getNombreExemplaireDisponible() + "/" + livre.getNombreExemplaire() + " exemplaires disponibles")

        );

        // Pied de page

        menuView.showBigLine();
        menuView.showBigMiddleBorder();

        menuView.showBigCenteredLine(TextModifier.colorText(TextModifier.BLEU, "[M]") + " Menu Principal | " + TextModifier.colorText(TextModifier.ROUGE, "[B]") + " Retour");
        menuView.showBigBottomBorder();

    }

    // Menu 3 - Supprimer un auteur

    public void showDeleteAuthorConfirmation(String nomAuteur, String prenomAuteur, @NonNull Auteur auteur, long nombreLivresAuteur, long nombreEmpruntsEnCours, long nombreEmpruntsHistorique) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "CONFIRMATION DE LA SUPRESSION D'AUTEUR"));
        menuView.showMiddleBorder();

        // Informations remplie

        menuView.showLine("Nom de l'auteur : " + standariserNom(nomAuteur));
        menuView.showLine("Prenom de l'auteur : " + standariserNom(prenomAuteur));
        menuView.showLine();

        // Resultat de recherche

        // Auteur retrouvé

        menuView.showLine(TextModifier.colorText(TextModifier.VERTCLAIR, "AUTEUR RETROUVÉ") + " : " + TextModifier.colorText(TextModifier.VERTCLAIR, auteur.nom().toUpperCase() + " " + standariserNom(auteur.prenom())) + " (" + auteur.nationalite() + ")");

        // Si l'auteur n'a aucun livre dans le catalogue

        if (nombreLivresAuteur <= 0) {

            menuView.showLine("Cet auteur n'a aucun livre actuellement dans le catalogue");

        }

        // Si l'auteur a au moins un livre dans le catalogue

        else {

            menuView.showLine();

            menuView.showLine(TextModifier.modifyText(TextModifier.SOULIGNE, TextModifier.colorText(TextModifier.ITALIQUE, TextModifier.colorText(TextModifier.ROUGE, "ATTENTION :"))) + " Cet auteur a " + TextModifier.colorText(TextModifier.VERT, String.valueOf(nombreLivresAuteur)) + " livres dans le catalogue");

            menuView.showLine();
            menuView.showLine("• " + TextModifier.colorText(TextModifier.ROSE, String.valueOf(nombreEmpruntsEnCours)) + " livres actuellement empruntés");

            menuView.showLine("• " + TextModifier.colorText(TextModifier.ROSE, String.valueOf(nombreEmpruntsHistorique)) + " emprunts historiques");

        }

        confirmMessage();
        footer();

    }

    public void deleteAuthorSucces(Auteur auteur) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "SUPPRIMER UN AUTEUR"));
        menuView.showMiddleBorder();

        // Resultat de recherche

        menuView.showLine("L'auteur \"" + standariserNomAuteur(auteur) + "\" a été supprimer avec succes.");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE,"[N]") + " Nouvelle Supression | " + TextModifier.colorText(TextModifier.VERT, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    void confirmMessage() {

        // Confirmation

        menuView.showLine();
        menuView.showLine("[ X ] Je confirme la suppresion");

    }

    void footer() {

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine( TextModifier.colorText(TextModifier.TURQUOISE, "[A]") + " Annuler Et Retour");
        menuView.showBottomBorder();

    }

    // Menu 4 - Liste des auteurs

    public void showAuthorsList(long nombreAuteurs, @NonNull Map<Auteur, Long> lesAuteurs) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERTCLAIR, "LISTE DES AUTEURS") + " (" + nombreAuteurs + ")");
        menuView.showMiddleBorder();

        // Résultats de recherche

        menuView.showLine();

        lesAuteurs.forEach((auteur, nombreLivres) ->

            menuView.showLine(auteur.nom().toUpperCase() + " " + standariserNom(auteur.prenom()) + " (" + standariserNom(auteur.nationalite()) + ") - " + nombreLivres + " livres")

        );

        // Pied de page

        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "[B]") + " RETOUR");
        menuView.showBottomBorder();

    }

}