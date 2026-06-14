package controlleur;

import exception.AuteurDejaExistantException;
import exception.AuteurEnCoursUtilisationException;
import exception.AuteurNonTrouveException;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import model.Auteur;
import model.Livre;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static main.AppConfig.*;
import static main.AppConfig.MainController;
import static main.AppView.authorView;
import static main.AppView.menuView;
import static util.DataUtil.*;
import static util.MenuUtil.handleUserChoice;
import static util.ParsingUtil.toAuthorKeyMap;
import static util.ParsingUtil.tryParsingAuthorChoice;
import static util.VerificatorUtil.biographieEstValide;
import static util.VerificatorUtil.nomEstValide;

@NoArgsConstructor
public class AuthorController {

    // Methode principal

    public void handleAuthorManagement() {

        // Afficher le menu principal

        authorView.showAuthorMenu(nombreAuteurs(), top2AuteursProlifiques());

        // Gestion des choix

        handleUserChoice(
            Map.of(
            "A", this::handleAddAuthor,
            "B", this::handleAuthorResearch,
            "M", this::handleAuthorDeletion,
            "L", this::handleShowAllAuthors,
            "0", MainController::launchControllers
            )
        );

    }

    // Menu 1 - Ajouter un Auteur

    private void handleAddAuthor() {

        // Header

        menuView.getDataHeader();

        // Recuperer les infos necessaires

        String nom = authorView.getAuthorLastName().trim();

        String prenom = authorView.getAuthorFirstName().trim();

        String nationalite = authorView.getAuthorNationality().trim();

        String biographie = authorView.getAuthorBiography().trim();

        // Footer

        menuView.getDataFooter();

        // Gestion confirmation

        authorView.showAddAuthor(nom, prenom, nationalite, biographie);

        // Gestion choix

        handleUserChoice(
            Map.of(
                "C", () -> handleParsingAndAuthorCreation(nom, prenom, nationalite, biographie),
                "A", this::handleAuthorManagement
            )
        );

    }

    private void handleParsingAndAuthorCreation(String nom, String prenom, String nationalite, String biographie) {

        // Valider les données

        try {

            validateAuthorData(nom, prenom, nationalite, biographie);

        } catch (IllegalArgumentException e) {

            menuView.showAFailure("ÉCHEC - AJOUT D'AUTEUR", e.getMessage());

            handleUserChoice(
                Map.of("R", this::handleAddAuthor, "A", this::handleAuthorManagement)
            );

        }

        // En cas de réussite

        createAuthorAndSuccesHandling(nom, prenom, nationalite, biographie);

    }

    private void createAuthorAndSuccesHandling(String nom, String prenom, String nationalite, String biographie) {

        Auteur nouvelAuteur = null;

        try {

            nouvelAuteur = serviceAuteur.ajouterAuteur(nom, prenom, nationalite, biographie);

        } catch (AuteurDejaExistantException e) {

            menuView.showAFailure("ÉCHEC - AJOUT D'AUTEUR", "Cet Auteur existe déja");

            handleUserChoice(
                Map.of("R", this::handleAddAuthor, "A", this::handleAuthorManagement)
            );

        }

        assert nouvelAuteur != null;

        authorView.showAuthorRecordingSucces(nouvelAuteur);

        handleUserChoice(
            Map.of(
                "1", this::handleAddAuthor,
                "2", this::handleAuthorManagement,
                "0", MainController::launchControllers
            )
        );

    }

    private void validateAuthorData(String nom, String prenom, String nationalite, String biographie) {

        if (!nomEstValide.test(nom)) throw new IllegalArgumentException("Nom non valide");
        if (!nomEstValide.test(prenom)) throw new IllegalArgumentException("Prenom non valide");
        if (!nomEstValide.test(nationalite)) throw new IllegalArgumentException("Nationalité non valide");
        if (!biographieEstValide.test(biographie)) throw new IllegalArgumentException("Biographie trop courte");

    }

    // Menu 2 - Rechercher un auteur

    private void handleAuthorResearch() {

        // Recuperer le nom ou le prenom de l'auteur

        String firstOrLastName = authorView.getAuthorNamesForResearch().trim();

        // Appel service pour recuperation

        Map<Auteur, Long> searchResult = serviceAuteur.chercherAuteurParNom(firstOrLastName);

        // Affichage du resultat

        authorView.searchAuthor(firstOrLastName, searchResult);

        // Gestion Choix

        handleUserChoice(
            Map.of(
                "N", this::handleAuthorResearch,
                "B", this::handleAuthorManagement,
                "V", () -> handleShowAuthorBooks(toAuthorKeyMap(searchResult.keySet())),
                "S", this::handleAuthorDeletion
            )
        );

    }

    private void handleShowAuthorBooks(@NonNull Map<Long, Auteur> authorConcerned) {

        String numeroAuteur = authorView.chooseAuthor(authorConcerned);

        long authorChoice = 0;

        try {

            authorChoice = tryParsingAuthorChoice(numeroAuteur, authorConcerned.size());

        } catch (IllegalArgumentException e) {

            menuView.showAFailure("ÉCHEC - AFFICHAGE LIVRES D'AUTEURS", e.getMessage());

            // Gestion Choix

            handleUserChoice(
                Map.of(
                    "R", () -> handleShowAuthorBooks(authorConcerned),
                    "A", this::handleAuthorManagement
                )
            );

        }

        // Recuperation et affichage

        Auteur auteur = authorConcerned.get(authorChoice);
        List<Livre> authorBooks = authorBooks(auteur);
        authorView.showAuthorBooks(auteur, authorBooks);

        // Gestion choix

        handleUserChoice(
            Map.of(
                "M", MainController::launchControllers,
                "B", this::handleAuthorManagement
            )
        );

    }

    // Menu 3 - Supprimer un Auteur

    private void handleAuthorDeletion() {

        // Recuperation du nom et du prenom de l'auteur

        String nomAuteur = authorView.getAuthorLastName().trim();
        String prenomAuteur = authorView.getAuthorFirstName().trim();

        // Appel du service

        Auteur deleted = null;

        try {

            deleted = serviceAuteur.supprimerAuteur(nomAuteur, prenomAuteur);

        } catch (AuteurNonTrouveException e) {

            menuView.showAFailure("ECHEC - SUPRESSION AUTEUR", "Auteur non retrouvée");

            handleAuthorDeletionEnd();

        } catch (AuteurEnCoursUtilisationException e) {

            menuView.showAFailure("ECHEC - SUPRESSION AUTEUR", "Il existe des livres associées actuellement a cet Auteur");

            handleAuthorDeletionEnd();

        }

        // Gestion Résultat

        deletion(deleted, nomAuteur, prenomAuteur);

    }

    private void deletion(Auteur deleted, String enteredName, String enteredLastName) {

        // Recuperation des infos necessaire a la vue

        long nombreLivresAuteur = authorBooks(deleted).size();
        long nombreEmpruntsEnCours = nombreEmpruntsEnCoursAuteur(deleted);
        long nombreEmpruntsHistorique = nombreEmpruntsHistoriqueAuteur(deleted);

        // Affichage du resultat

        authorView.showDeleteAuthorConfirmation(enteredLastName, enteredName, deleted, nombreLivresAuteur, nombreEmpruntsEnCours, nombreEmpruntsHistorique);

        handleUserChoice(
            Map.of(
                "X", () -> {

                    authorView.deleteAuthorSucces(deleted);
                    handleAuthorDeletionSuccesEnd();

                },
                "A", this::handleAuthorManagement
            )
        );

    }

    private void handleAuthorDeletionEnd() {

        handleUserChoice(
            Map.of(
                "R", this::handleAuthorDeletion,
                "A", this::handleAuthorManagement
            )
        );

    }

    private void handleAuthorDeletionSuccesEnd() {

        handleUserChoice(
            Map.of(
                "N", this::handleAuthorDeletion,
                "R", this::handleAuthorManagement
            )
        );

    }

    // Menu 4 - Lister tout les auteurs

    private void handleShowAllAuthors() {

        // Recuperer tout les auteurs

        List<Auteur> allAuthors = serviceAuteur.toutLesAuteurs();

        // Construire la Map : Auteur ~ Nombre de Livres

        Map<Auteur, Long> allAuthorsAndBooks = new HashMap<>();

        allAuthors.forEach(auteur -> allAuthorsAndBooks.putIfAbsent(auteur, (long) authorBooks(auteur).size()));

        // Afficher les infos auteurs

        authorView.showAuthorsList(nombreAuteurs(), allAuthorsAndBooks);

        // Gestion Choix

        handleUserChoice(
            Map.of(
             "B", this::handleAuthorManagement
            )
        );

    }

}