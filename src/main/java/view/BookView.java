package view;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import model.CategorieLivre;
import model.EtatLivre;
import model.Livre;
import model.TextModifier;

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
public class BookView {

    // Scanner

    private final Scanner sc = new Scanner(System.in);

    // Menu Principal

    public void showBooksMainMenu() {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.BLEU, "GESTION DES LIVRES DE LA BIBLIOTHÈQUE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Menu

        menuView.showLine("[A] AJOUTER UN NOUVEAU LIVRE");
        menuView.showLine("[R] RECHERCHER UN LIVRE");
        menuView.showLine("[L] LISTER LES LIVRES");
        menuView.showLine("[M] MODIFIER LES INFORMATIONS D'UN LIVRE");
        menuView.showLine("[S] SUPPRIMER UN LIVRE");
        menuView.showLine("[V] VERIFIER LA DISPONIBILITE D'UN LIVRE");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "[Q] RETOUR AU MENU PRINCIPAL"));
        menuView.showBottomBorder();

    }

    // Menu 1 - Ajouter un nouveau livre

    public void showBooksRecording(String ISBN, String titre, String authorName, String authorFirstName, String nomMaisonEdition, String anneePublication, String categorie, String nombreExemplaires, @NonNull LocalDate dateAjout) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "ENREGISTREMENT D'UN NOUVEAU LIVRE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Informations complètes du livre :");
        menuView.showLine();
        menuView.showLine("• ISBN (Identifiant unique) : " + ISBN);
        menuView.showLine("• Titre : " + standariserNom(titre));
        menuView.showLine("• Auteur (Nom & Prénom) : " + standariserNom(authorName) + " " + standariserNom(authorFirstName));
        menuView.showLine("• Maison d'Édition : " + nomMaisonEdition);
        menuView.showLine("• Année de Publication : " + anneePublication);
        menuView.showLine("• Catégorie : " + categorie);
        menuView.showLine("• Nombre d'Exemplaires : " + nombreExemplaires);
        menuView.showLine("• Date d'Ajout (AAAA-MM-JJ) : " + dateAjout.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROSE, "[S]") + " Sauvegarder le Livre | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retour");
        menuView.showBottomBorder();

    }

    public String getISBN() {

        println("Entrez l'ISBN du livre (Ex: 978-0-306-40615-7) :");

        print("> ");

        return sc.nextLine();

    }

    public String getTitle() {

        println("Entrez le titre du livre :");

        print("> ");

        return sc.nextLine();

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

    public String getHomeEditorName() {

        println("Entrez le nom de la maison d'édition :");

        print("> ");

        return sc.nextLine();

    }

    public String getPublishingYear() {

        println("Entrez l'année de publication :");

        print("> ");

        return sc.nextLine();

    }

    public String getBookCategory() {

        println("Indiquez la catégorie du livre :");
        println();

        // Afficher les differentes catégories pour un choix

        AtomicInteger i = new AtomicInteger(0);

        for (CategorieLivre categorie : CategorieLivre.values()) {

            println(i.incrementAndGet() + ". " + categorie);

        }

        println();
        print("> ");

        return sc.nextLine();

    }

    public String getCopyNumbers() {

        println("Entrez le nombres d'exemplaires :");

        print("> ");

        return sc.nextLine();

    }

    public void showBookRecordingSucces(@NonNull Livre nouveauLivre) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERTCLAIR, "LIVRE AJOUTÉ"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Le livre a été enregistré avec succès par le système :");
        menuView.showLine();
        menuView.showLine(TextModifier.colorText(TextModifier.TURQUOISE, "RÉCAPITULATIF DE L'ENREGISTREMENT :"));
        menuView.showLine();
        menuView.showLine("• ISBN (Identifiant unique) : " + nouveauLivre.getIsbn());
        menuView.showLine("• Titre : " + standariserTitre(nouveauLivre));
        menuView.showLine("• Auteur Lié : " + standariserNomAuteur(nouveauLivre.getAuteur()));
        menuView.showLine("• Maison d'Édition Liée : " + standariserNom(nouveauLivre.getMaisonEdition().nom()));
        menuView.showLine("• Catégorie Liée : " + nouveauLivre.getCategorie());
        menuView.showLine("• Exemplaires totaux : " + nouveauLivre.getNombreExemplaire());
        menuView.showLine("• Date d'Ajout (AAAA-MM-JJ) : " + nouveauLivre.getDateAjout().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.CYAN, "[N]") + " Ajouter un autre Livre | " + TextModifier.colorText(TextModifier.VERT, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    public void showBookRecordingFailure (String raison) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "ÉCHEC - AJOUT DE LIVRE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine(TextModifier.colorText(TextModifier.ROUGE,"Raison :"));
        menuView.showLine("• " + raison);

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROSE, "[R]") + " Réessayer | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retour");
        menuView.showBottomBorder();

    }

    // Menu 2 - Rechercher un livre

    public void showBookResearchMainMenu() {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.JAUNE, "RECHERCHER UN LIVRE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Comment souhaitez-vous effectuer la recherche ?");
        menuView.showLine();
        menuView.showLine("[1] Par Identifiant (ID)");
        menuView.showLine("[2] Par Titre");
        menuView.showLine("[3] Par Auteur (Nom)");
        menuView.showLine("[4] Par Catégorie");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROSE, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    public void showBookResearchResult(@NonNull String critere, String valeur, @NonNull List<Livre> livres) {

        boolean estFormatable = (critere.equalsIgnoreCase("Titre") || critere.equalsIgnoreCase("Auteur"));

        // En tete

        menuView.showBigTopBorder();
        menuView.showBigCenteredLine(TextModifier.colorText(TextModifier.ORANGE, "RÉSULTAT DE RECHERCHE") + " - Par " + critere + " : \"" +

        /* Formater seulement si il s'agit du titre ou du nom de l'auteur */

        (estFormatable ? standariserNom(valeur) : valeur) + "\"");

        menuView.showBigMiddleBorder();
        menuView.showBigLine();

        // Détails

        menuView.showBigLine(livres.size() + " livres trouvés");

        if (!livres.isEmpty()) {

            showBooks(livres);

        }

        // Pied de page

        menuView.showBigLine();
        menuView.showBigMiddleBorder();
        menuView.showBigCenteredLine(TextModifier.colorText(TextModifier.VERT, "[N]") + " Nouvelle Recherche | " + TextModifier.colorText(TextModifier.ROUGE, "[Q]") + " Quitter");
        menuView.showBigBottomBorder();

    }

    void showBooks(@NonNull List<Livre> livres) {

        menuView.showBigLine();
        menuView.showBigLine(String.format("%-10s | %-30s | %-15s | %s",
                "ID", "TITRE & AUTEUR", "CATÉGORIE", "DISPO"));
        menuView.showBigLine("-----------|--------------------------------|-----------------|------");

        livres.forEach(livre -> {

            String id = tronquer(livre.getId(), 10);

            String titreAuteur = tronquer(
            standariserTitre(livre) + " - " + standariserNomAuteur(livre.getAuteur()),
            30
            );

            String categorie = tronquer(livre.getCategorie().toString(), 15);

            String disponible = String.valueOf(livre.getNombreExemplaireDisponible());

            menuView.showBigLine(String.format("%-10s | %-30s | %-15s | %s",
            id, titreAuteur, categorie, disponible));

        });

        menuView.showBigLine();
    }

    private String tronquer(@NonNull String texte, int longueur) {

        return texte.length() > longueur
        ? texte.substring(0, longueur - 3) + "..."
        : texte;

    }

    public void showBookResearchFailure(@NonNull String critereRecherche, String value, String raison) {

        boolean estFormatable = (critereRecherche.equalsIgnoreCase("Titre") || critereRecherche.equalsIgnoreCase("Auteur"));

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "ÉCHEC - LIVRE NON TROUVÉ"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Critère de recherche : " + critereRecherche + " (" + (estFormatable ? standariserNom(value) : value) + ")");
        menuView.showLine();
        menuView.showLine("Raison :");
        menuView.showLine(" • " + raison);

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.CYAN, "[R]") + " Réessayer la recherche | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Quitter");
        menuView.showBottomBorder();

    }

    // Menu 3 - Afficher les livres

    public void showBooksMenu() {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROSE, "AFFICHAGE DES LIVRES"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Menu

        menuView.showLine("Que souhaitez-vous afficher ?");
        menuView.showLine();
        menuView.showLine("[1] Afficher tout les livres");
        menuView.showLine("[2] Afficher uniquement les livres disponibles");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "[R] Retour"));
        menuView.showBottomBorder();

    }

    public void showAllBooks(@NonNull List<Livre> livres) {

        // En tete

        menuView.showBigTopBorder();
        menuView.showBigCenteredLine(TextModifier.colorText(TextModifier.MAGENTA, "LISTE DES LIVRES"));
        menuView.showBigMiddleBorder();
        menuView.showBigLine();

        // Menu

        menuView.showBigLine("TOTAL LIVRES : " + livres.size());
        menuView.showBigLine();

        if (!livres.isEmpty()) showBooks(livres);

        // Pied de page

        menuView.showBigLine();
        menuView.showBigMiddleBorder();
        menuView.showBigCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "[R] Retour"));
        menuView.showBigBottomBorder();

    }

    public void showAvailableBooks(@NonNull List<Livre> livres) {

        // En tete

        menuView.showBigTopBorder();
        menuView.showBigCenteredLine(TextModifier.colorText(TextModifier.MAGENTA, "LISTE DES LIVRES DISPONIBLES"));
        menuView.showBigMiddleBorder();
        menuView.showBigLine();

        // Menu

        menuView.showBigLine("TOTAL LIVRES DISPONIBLES : " + livres.size());
        menuView.showBigLine();

        if (!livres.isEmpty()) showBooks(livres);

        // Pied de page

        menuView.showBigLine();
        menuView.showBigMiddleBorder();
        menuView.showBigCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "[R] Retour"));
        menuView.showBigBottomBorder();

    }

    // Menu 4 - Modifier les informations d'un livre

    public void showBookModification(String idEnter) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "MODIFICATION DES INFORMATIONS D'UN LIVRE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Identifiant du livre a modifier :");
        menuView.showLine();
        menuView.showLine("• ID du Livre : " + idEnter);

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERTCLAIR, "[V]") + " Valider | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retour");
        menuView.showBottomBorder();

    }

    public String getBookId() {

        println("Entrez l'id du livre :");

        print("> ");

        return sc.nextLine();

    }

    public void showModificationToStepFailure(String idEnter) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "ÉCHEC - RECUPERATION DES DONNEES DU LIVRE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Aucun livre n'a été retrouver avec l'id : " + idEnter);

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ORANGE, "[R]") + " Réessayer | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retour");
        menuView.showBottomBorder();

    }

    public void showBookModificationStep(@NonNull Livre livreAModifier) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERTCLAIR, "MODIFICATION DES INFORMATIONS D'UN LIVRE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("ID du Livre à modifier : " + livreAModifier.getId());
        menuView.showLine();
        menuView.showLine("Quel champ souhaitez-vous modifier pour ce livre ?");
        menuView.showLine();
        menuView.showLine("[1] Titre (Actuel : " + standariserTitre(livreAModifier) + ")");
        menuView.showLine("[2] Etat du livre (Actuel : " + livreAModifier.getEtatDuLivre() + ")");
        menuView.showLine("[3] Catégorie (Actuel : " + livreAModifier.getCategorie() + ")");
        menuView.showLine("[4] Nombre d'Exemplaires (Actuel : " + livreAModifier.getNombreExemplaire() + ")");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.TURQUOISE, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    public String getBookState() {

        println("Choisissez un état :");

        AtomicInteger i = new AtomicInteger(0);

        for (EtatLivre e : EtatLivre.values()) {

            println(i.incrementAndGet() + ". " + e);

        }

        println();
        print("> ");

        return sc.nextLine();

    }

    public void showBookModificationSucces(String champ, String idEnter, String ancienneValeur, String nouvelleValeur) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERTCLAIR, "DONNÉE LIVRE MODIFIÉE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Le " + champ + " du livre " + idEnter + " a été mis à jour.");
        menuView.showLine();
        menuView.showLine("Ancienne valeur : " + ancienneValeur);
        menuView.showLine("Nouvelle valeur : " + nouvelleValeur);

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    public void showBookModificationFailure(String raison) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "ÉCHEC - MODIFICATION LIVRE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("La modification n'a pas pu etre effectuer pour la raison suivante :");
        menuView.showLine();
        menuView.showLine("Raison :");
        menuView.showLine(" • " + raison);

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ORANGE, "[R]") + " Réessayer | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retour");
        menuView.showBottomBorder();

    }

    // Menu 5 - Supprimer un livre

    public void showBookDeletion(String idEnter) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROSE, "SUPPRESSION D'UN LIVRE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("ID du Livre à supprimer : " + idEnter);
        menuView.showLine();
        menuView.showLine("ATTENTION : Cette action est irréversible.");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "[C]") + " Confirmer la Suppression | " + TextModifier.colorText(TextModifier.VERT, "[A]") + " Annuler et Retour");
        menuView.showBottomBorder();

    }

    public void showBookDeletionSucces(@NonNull Livre livre) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "LIVRE SUPPRIMÉ"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Le livre " + livre.getId() + " (" + standariserTitre(livre) + ") a été supprimé avec succès.");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[N]") + " Nouvelle Suppression | " + TextModifier.colorText(TextModifier.VERTCLAIR, "[A]") + " Annuler et Retour");
        menuView.showBottomBorder();

    }

    public void showBookDeletionFailure(String bookId, String exception, String details) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "ÉCHEC - SUPPRESSION REFUSÉE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Le livre " + bookId + " ne peut pas être supprimé.");
        menuView.showLine();
        menuView.showLine("Raison : " + exception);
        menuView.showLine("• " + details);

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ORANGE, "[R]") + " Réessayer | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retour");
        menuView.showBottomBorder();

    }

    // Menu 6 - Verifier la disponibilité du livre

    public void showBookAvailability() {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERTCLAIR, "VÉRIFICATION DE DISPONIBILITÉ"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Voulez-vous vérifier la disponibilité :");
        menuView.showLine();
        menuView.showLine("[1] Par Identifiant du Livre");
        menuView.showLine("[2] Par Titre du Livre");

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    public void showAvailabilitySucces(@NonNull Livre livre) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERTCLAIR, "DISPONIBILITÉ CONFIRMÉE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("Livre : " + standariserTitre(livre) + " (" + livre.getId() + ")");
        menuView.showLine();
        menuView.showLine("Disponible : " + (livre.estDisponible() ? "Oui" : "Non"));
        menuView.showLine();
        menuView.showLine("• Total Exemplaires : " + livre.getNombreExemplaire());
        menuView.showLine("• Exemplaires Empruntés : " + (livre.getNombreExemplaire() - livre.getNombreExemplaireDisponible()));
        menuView.showLine("• Exemplaires Restants : " + livre.getNombreExemplaireDisponible());

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.VERT, "[N]") + " Nouvelle Vérification | " + TextModifier.colorText(TextModifier.ROSE, "[R]") + " Retour");
        menuView.showBottomBorder();

    }

    public void showAvailabilityFailure(String raison) {

        // En tete

        menuView.showTopBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ROUGE, "ÉCHEC - LIVRE NON DISPONIBLE"));
        menuView.showMiddleBorder();
        menuView.showLine();

        // Détails

        menuView.showLine("La vérification n'a pas pu etre effectuer pour la raison suivante :");
        menuView.showLine();
        menuView.showLine("Raison :");
        menuView.showLine(" • " + raison);

        // Pied de page

        menuView.showLine();
        menuView.showMiddleBorder();
        menuView.showCenteredLine(TextModifier.colorText(TextModifier.ORANGE, "[R]") + " Réessayer | " + TextModifier.colorText(TextModifier.ROUGE, "[A]") + " Annuler et Retour");
        menuView.showBottomBorder();

    }

}
