package controlleur;

import exception.MaisonEditionEnCoursUtilisationException;
import exception.MaisonEditionNonTrouverException;
import exception.MaisonEditonDejaExistantException;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import model.MaisonEdition;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static main.AppConfig.*;
import static main.AppView.homeEditorView;
import static main.AppView.menuView;
import static util.DataUtil.homeEditorBooks;
import static util.DataUtil.nombreTotalMaisonEdition;
import static util.MenuUtil.handleUserChoice;
import static util.ParsingUtil.parseLocalDate;
import static util.VerificatorUtil.*;

@NoArgsConstructor
public class HomeEditorController {

    // Méthode principal

    public void handleHomeEditorManagement() {

        // Affichage du menu principal

        homeEditorView.showHomeEditorMenu(nombreTotalMaisonEdition());

        // Gestion des choix

        handleUserChoice(
            Map.of(
                "A", this::handleAddingHomeEditor,
                "S", this::handleHomeEditorDeletion,
                "R", this::handleHomeEditorSearch,
                "L", this::handleShowAllHomeEditors,
                "0", MainController::launchControllers
            )
        );

    }

    // Menu 1 - Ajouter une maison d'édition

    private void handleAddingHomeEditor() {

        // Recuperation des infos necessaires

        String nomMaisonEdition = homeEditorView.getHomeEditorName().trim();
        String codePostal = homeEditorView.getHomeEditorPostalCode().trim();
        String numeroTelephone = homeEditorView.getHomeEditorPhoneNumber().trim();
        String ville = homeEditorView.getHomeEditorCity().trim();
        String dateCreation = homeEditorView.getHomeEditorDateOfCreation().trim();

        // Valider les données

        try {

            validateData(nomMaisonEdition, codePostal, numeroTelephone, ville);

            // Affichage et Gestion

            homeEditorView.showConfirmAddHomeEditor(nomMaisonEdition, codePostal, numeroTelephone, ville, dateCreation);

            handleUserChoice(
                Map.of(
                    "V", () -> handleParsindAndCreation(nomMaisonEdition, codePostal, numeroTelephone, ville, dateCreation),
                    "A", this::handleHomeEditorManagement
                )
            );

        } catch (IllegalArgumentException e) {

            menuView.showAFailure("ECHEC - AJOUT MAISON D'EDITION", e.getMessage());

        }

    }

    // Parsing et Gestion

    private void handleParsindAndCreation(String nomMaisonEdition, String codePostal, String numeroTelephone, String ville, String date) {

        LocalDate dateCreation;
        MaisonEdition nouvelMaisonEdition;

        try {

            dateCreation = parseLocalDate(date);

            // Creation de la maison d'édition

            nouvelMaisonEdition = serviceMaisonEdition.ajouterUneMaisonEdition(nomMaisonEdition, codePostal, numeroTelephone, ville, dateCreation);

            // Affichage du résultat

            homeEditorView.showAddindHomeEditorSucces(nouvelMaisonEdition);

            // Gestion Choix

            handleUserChoice(
                Map.of(
                    "A", this::handleAddingHomeEditor,
                    "R", this::handleHomeEditorManagement
                )
            );

        } catch (IllegalArgumentException | MaisonEditonDejaExistantException e) {

            menuView.showAFailure("ECHEC - CREATION MAISON D'EDITION", e.getMessage());

            // Gestion Choix

            handleUserChoice(
                Map.of(
                    "R", this::handleAddingHomeEditor,
                    "A", this::handleHomeEditorManagement
                )
            );

        }

    }

    // Valider les données

    private void validateData(String nomMaisonEdition, String codePostal, String numeroTelephone, String ville) {

        if (!nomEstValide.test(nomMaisonEdition)) throw new IllegalArgumentException("Le nom de la maison d'édition est trop court.");
        if (!codePostalEstValide.test(codePostal)) throw new IllegalArgumentException("Code postal invalide");
        if (!numeroEstValide.test(numeroTelephone)) throw new IllegalArgumentException("Numero de télephone invalide.");
        if (!nomEstValide.test(ville)) throw new IllegalArgumentException("Nom de la ville incorrect");

    }

    // Menu 2 - Supprimer une maison d'édition

    private void handleHomeEditorDeletion() {

        // Recuperer le nom de la Maison d'édition

        String homeEditorName = homeEditorView.getHomeEditorToDeleteName().trim();

        // Retrouver la maison d'édition

        try {

            MaisonEdition homeEditorToDelete = serviceMaisonEdition.findHomeEditor(homeEditorName);

            // Affichage du resultat

            homeEditorView.showDeleteProcessHomeEditor(homeEditorToDelete, homeEditorBooks(homeEditorToDelete).size());

            // Gerer Choix

            handleUserChoice(
                Map.of(
                    "S", () -> handleDeletionConfirmation(homeEditorToDelete),
                    "A", this::handleHomeEditorManagement
                )
            );


        } catch (MaisonEditionNonTrouverException e) {

            homeEditorView.showHomeEditorNotFind(homeEditorName);

            handleUserChoice(
                Map.of(
                    "R", this::handleHomeEditorDeletion,
                    "A", this::handleHomeEditorManagement
                )
            );

        }

    }

    private void handleDeletionConfirmation(@NonNull MaisonEdition homeEditor) {

        // Appel du service pour supression

        try {

            boolean result = serviceMaisonEdition.supprimerMaisonEdition(homeEditor.nom());

            if (result) {

                homeEditorView.showSucessfulDeletionResult(nombreTotalMaisonEdition());
                handleSupressionEnding();

            } else throw new IllegalStateException("Something is going bad");

        } /* Ne devrait pas arriver */ catch (MaisonEditionNonTrouverException _) {} catch (MaisonEditionEnCoursUtilisationException e) {

            homeEditorView.showSuppresionFailed(homeEditor, homeEditorBooks(homeEditor).size());
            handleSupressionEnding();

        }

    }

    private void handleSupressionEnding() {

        handleUserChoice(
            Map.of(
                "R", this::handleHomeEditorManagement,
                "A", this::handleHomeEditorDeletion
            )
        );

    }

    // Menu 3 - Rechercher une maison d'édition

    private void handleHomeEditorSearch() {

        // Recuperer le nom de la maison d'édition

        String homeEditorName = homeEditorView.getAuthorNamesForResearch().trim();

        // Appel du service

        List<MaisonEdition> researchResult = serviceMaisonEdition.chercherParNom(homeEditorName);

        // Affichage des résultats

        homeEditorView.showHomeEditorResearch(researchResult);

        // Gestion choix

        handleUserChoice(
            Map.of(
                "N", this::handleHomeEditorSearch,
                "R", this::handleHomeEditorManagement
            )
        );

    }

    // Menu 4 - Lister toute les maisons d'édition

    private void handleShowAllHomeEditors() {

        // Recuperer les maisons d'édition

        List<MaisonEdition> allHomeEditors = serviceMaisonEdition.toutLesMaisonEdition();

        // Affichage des résultats

        homeEditorView.showAllHomeEditors(allHomeEditors);

        // Gestion choix

        handleUserChoice(Map.of("R", this::handleHomeEditorManagement));

    }

}