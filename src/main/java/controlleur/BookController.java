package controlleur;

import exception.*;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import model.*;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Map;

import static main.AppConfig.*;
import static main.AppView.bookView;
import static main.AppView.menuView;
import static util.MenuUtil.handleUserChoice;
import static util.DataUtil.getBook;
import static util.ParsingUtil.*;
import static util.VerificatorUtil.*;

@NoArgsConstructor
public class BookController {

    // Méthode principal

    public void handleBookManagement() {

        // Affichage du menu

        bookView.showBooksMainMenu();

        // Gestion du choix utilisateur

        handleUserChoice(
            Map.of(
                "A", this::handleAddingBooks,
                "R", this::handleBooKSearching,
                "L", this::handleDisplayBooks,
                "M", this::handleBookModification,
                "S", this::handleBookDeletion,
                "V", this::handleShowAvailability,
                "Q", MainController::launchControllers
            )
        );

    }

    // Menu 1 - Ajouter un nouveau livre

    private void handleAddingBooks() {

        /* Recuperation des données */

        menuView.getDataHeader();

        // Recuperation des informations necessaires

        String Isbn = bookView.getISBN().trim();
        String titre = bookView.getTitle().trim();
        String nomAuteur = bookView.getAuthorLastName().trim();
        String prenomAuteur = bookView.getAuthorFirstName().trim();
        String nomMaisonEdition = bookView.getHomeEditorName().trim();
        String anneePublication = bookView.getPublishingYear().trim();
        String categorieLivre = bookView.getBookCategory().trim();
        String nombreExemplaires = bookView.getCopyNumbers().trim();
        LocalDate dateAjout = LocalDate.now();

        menuView.getDataFooter();

        /* Recuperation des données */

        // Affichage avant validation

        bookView.showBooksRecording(Isbn, titre, nomAuteur, prenomAuteur, nomMaisonEdition, anneePublication, categorieLivre, nombreExemplaires, dateAjout);

        // Gestion choix

        handleUserChoice(
            Map.of("S",
            () -> handleAddingBooksResult(Isbn, titre, nomAuteur, prenomAuteur, nomMaisonEdition, anneePublication, categorieLivre, nombreExemplaires))
        );

    }

    private void handleAddingBooksResult(String Isbn, String titre, String nomAuteur, String prenomAuteur, String nomMaisonEdition, String anneePublication, String categorieLivre, String nombreExemplaires) {

        Year annee;
        CategorieLivre categorie;
        long nombreExemplairesInitial;
        Auteur auteur;
        MaisonEdition maisonEdition;

        try {

            // Validation - Lève des exceptions

            validateBookData(Isbn, titre, nomAuteur, prenomAuteur, nomMaisonEdition, anneePublication);

            // Parsing

            annee = tryParsingPlushingYear(anneePublication);

            categorie = tryParsingBookCategory(categorieLivre);

            nombreExemplairesInitial = tryParsingCopyNumber(nombreExemplaires);

            auteur = serviceAuteur.findAuthor(nomAuteur, prenomAuteur);

            maisonEdition = serviceMaisonEdition.findHomeEditor(nomMaisonEdition);

        }

        // Cas d'erreur

        catch (IllegalArgumentException e) {

            bookView.showBookRecordingFailure(e.getMessage());

            handleFailureEnd();

            return;

        } catch (AuteurNonTrouveException e) {

            bookView.showBookRecordingFailure("Aucun auteur n'a été retrouver avec le nom \"" + nomAuteur + " " + prenomAuteur + "\"");

            handleFailureEnd();

            return;

        } catch (MaisonEditionNonTrouverException e) {

            bookView.showBookRecordingFailure("Aucune maison d'édition n'a été retrouver avec le nom \"" + nomMaisonEdition + "\"");

            handleFailureEnd();

            return;

        }

        // La vérification a réussie

        try {

            Livre nouveauLivre = serviceLivre.ajouterLivre(Isbn, titre, auteur, maisonEdition, annee, categorie, nombreExemplairesInitial);

            bookView.showBookRecordingSucces(nouveauLivre);

        } catch (LivreDejaExistantException e) {

            bookView.showBookRecordingFailure("Un livre existe déja avec le meme ISBN");

            handleFailureEnd();

            return;

        }

        catch (IllegalStateException e) {

            bookView.showAvailabilityFailure("Le meme livre existe déja dans le registre");

            handleFailureEnd();

            return;

        }

        // Gestion du choix

        handleSuccesEnd();

    }

    private void handleSuccesEnd() {

        handleUserChoice(
            Map.of(
                "N", this::handleAddingBooks,
                "R", this::handleBookManagement
            )
        );

    }

    private void handleFailureEnd() {

        handleUserChoice(
            Map.of(
                "R", this::handleAddingBooks,
                "A", this::handleBookManagement
            )
        );

    }

    private void validateBookData(String Isbn, String titre, String nomAuteur, String prenomAuteur, String nomMaisonEdition, String anneePublication) {

        // Validation

        if (!validerISBN.test(Isbn)) throw new IllegalArgumentException("ISBN invalide");
        if (!titreEstValide.test(titre)) throw new IllegalArgumentException("Titre invalide");
        if (!nomEstValide.test(nomAuteur)) throw new IllegalArgumentException("Nom de l'auteur invalide");
        if (!nomEstValide.test(prenomAuteur)) throw new IllegalArgumentException("Prenom de l'auteur invalide");
        if (!nomEstValide.test(nomMaisonEdition)) throw new IllegalArgumentException("Nom de la maison d'édition invalide");
        if (!anneePublication.matches("\\d{4}")) throw new IllegalArgumentException("Année de publication invalide");

        // Aucune exception levé (...)

    }

    // Menu 2 - Rechercher un livre

    private void handleBooKSearching() {

        // Affichage des critères de recherche possible

        bookView.showBookResearchMainMenu();

        // Gestion des choix

        handleUserChoice(
            Map.of(
                "1", this::handleSearchById,
                "2", this::handleSearchByTitle,
                "3", this::handleSearchByAuthorName,
                "4", this::handleSearchByBookCategorie,
                "R", this::handleBookManagement
            )
        );

    }

    /* Chercher par Id */

    private void handleSearchById() {

        // Recuperer l'id

        String idLivre = bookView.getBookId().trim();

        // Appel du service pour la recherche

        try {

            Livre livreRechercher = serviceLivre.rechercherParId(idLivre);

            bookView.showBookResearchResult("Id", idLivre, List.of(livreRechercher));

            handleSearchSuccesEnd();

        } catch (LivreNonTrouveException e) {

            bookView.showBookResearchFailure("Id", idLivre, "Aucun livre n'a été retrouver avec cet Id");

            handleSearchFailureEnd();

        }

    }

    private void handleSearchSuccesEnd() {

        handleUserChoice(
            Map.of(
                "N", this::handleBooKSearching,
                "Q", this::handleBookManagement
            )
        );

    }

    private void handleSearchFailureEnd() {

        handleUserChoice(
            Map.of(
                "R", this::handleSearchById,
                "A", this::handleBookManagement
            )
        );

    }

    /* Chercher par Id */

    /* Chercher par titre */

    private void handleSearchByTitle() {

        // Recuperation du titre

        String titreLivre = bookView.getTitle().trim();

        // Appel du service

        List<Livre> livresTrouves = serviceLivre.rechercherParTitre(titreLivre);

        // Affichage des résultats (Peut etre O si aucun livre avec ce titre)

        bookView.showBookResearchResult("Titre", titreLivre, livresTrouves);

        // Gestion du choix

        handleSearchByOtherMethodsEnd();

    }

    /* Chercher par titre */

    /* Chercher par nom de l'auteur */

    private void handleSearchByAuthorName() {

        // Recuperer le nom de l'auteur

        String authorName = bookView.getAuthorLastName().trim();

        // Appel du service

        List<Livre> livresTrouves = serviceLivre.rechercherParAuteur(authorName);

        // Affichage des résultats

        bookView.showBookResearchResult("Auteur", authorName, livresTrouves);

        // Gestion du choix

        handleSearchByOtherMethodsEnd();

    }

    /* Chercher par nom de l'auteur */

    /* Chercher categorie */

    private void handleSearchByBookCategorie() {

        // Recuperer le choix de la categorie

        String entry = "";
        CategorieLivre categorieLivre;

        try {

            entry = bookView.getBookCategory().trim();

            categorieLivre = tryParsingBookCategory(entry);

        } catch (IllegalArgumentException e) {

            bookView.showBookResearchFailure("Categorie", entry, "Choix de categorie invalide.");

            handleSearchByCategorieFailureEnd();

            return;

        }

        // Appel du service

        List<Livre> livresTrouves = serviceLivre.rechercherParCategorie(categorieLivre);

        // Affichage du resultat

        assert categorieLivre != null;
        bookView.showBookResearchResult("Categorie", categorieLivre.toString(), livresTrouves);

        // Gerer le choix

        handleSearchByOtherMethodsEnd();

    }

    private void handleSearchByCategorieFailureEnd() {

        handleUserChoice(
            Map.of(
                "R", this::handleSearchByBookCategorie,
                "A", this::handleBookManagement
            )
        );

    }

    /* Chercher categorie */

    // Gerer le choix après chercher par Titre ou Auteur ou Par categorie

    private void handleSearchByOtherMethodsEnd() {

        handleUserChoice(
            Map.of(
                "N", this::handleBooKSearching,
                "Q", this::handleBookManagement
            )
        );

    }

    // Menu 3 - Afficher la liste des livres ou les livres "disponibles"

    private void handleDisplayBooks() {
        
        // Afficher le menu de choix
        
        bookView.showBooksMenu();

        // Gestion choix

        handleUserChoice(
            Map.of(
                "1", () -> handleShowAllBooks("1"),
                "2", () -> handleShowAllBooks("2")
            )
        );
        
    }
    
    // Gère tout les livres / livres disponibles
    
    private void handleShowAllBooks(@NonNull String choix) {
        
        List<Livre> books = switch (choix) {

            case "1" -> serviceLivre.toutLesLivres();

            case "2" -> serviceLivre.livresDisponible();

            default -> throw new IllegalStateException("Choix invalide pour recuperation des livres (Ne devrait jamais arriver)...");

        };
        
        // Appel de la méthode d'affichage
        
        bookView.showAllBooks(books);
        
        // Gestion du choix unique
        
        handleUserChoice(Map.of("R", this::handleBookManagement));

    }
    
    // Menu 4 - Modifier les informations d'un livre

    private void handleBookModification() {

        // Recuperer l'id du livre

        String bookId = bookView.getBookId().trim();

        // Validation de l'id

        bookView.showBookModification(bookId);

        // Gestion choix

        handleUserChoice(Map.of("V", () -> tryGetBookAndModify(bookId)));

    }

    private void tryGetBookAndModify(String idLivre) {

        // verifier l'existance du livre

        Livre bookToModify;

        try {

            bookToModify = registreLivres.findById(idLivre);

        } catch (LivreNonTrouveException e) {

            bookView.showModificationToStepFailure(idLivre);

            handleTryingGetBookAndModifyFailure();

            return;

        }

        // Livre retrouver -> Recuperer le champ a modifier

        bookView.showBookModificationStep(bookToModify);

        // Gestion Choix

        handleUserChoice(
            Map.of(
                "1", () -> handleTitleModification(idLivre),
                "2", () -> handleBookStateModification(idLivre),
                "3", () -> handleBookCategoryModification(idLivre),
                "4", () -> handleNumberOfCopiesModification(idLivre)
            )
        );

    }

    private void handleTryingGetBookAndModifyFailure() {

        handleUserChoice(
            Map.of(
                "R", this::handleBookModification,
                "A", this::handleBookManagement
            )
        );

    }

    /* Modification du titre */

    private void handleTitleModification(String idLivre) {

        Livre bookToModify = getBook(idLivre);

        // Recuperer le nouveau titre du livre

        String nouveauTitre = bookView.getTitle().trim();

        // Si la modification réussie

        if (titreEstValide.test(nouveauTitre)) {

            titleModificationSucces(idLivre, nouveauTitre, bookToModify);

        } else {

            titleModificationFailure(idLivre);

        }

    }

    private void titleModificationSucces(String idLivre, String nouveauTitre, @NonNull Livre bookToModify) {

        Livre livreModifier = serviceLivre.modifierTitre(idLivre, nouveauTitre);

        bookView.showBookModificationSucces("titre", idLivre, bookToModify.getTitre(), livreModifier.getTitre());

        // Gestion du retour

        handleModificationSuccesEnd(idLivre);

    }

    private void titleModificationFailure(String idLivre) {

        bookView.showBookModificationFailure("Titre invalide");

        handleUserChoice(
            Map.of(
                    "R", () -> handleTitleModification(idLivre),
                    "A", this::handleBookManagement
            )
        );

    }

    /* Modification du titre */

    /* Modification de l'état du livre */

    private void handleBookStateModification(String idLivre) {

        String etatChoisie = bookView.getBookState().trim();

        EtatLivre etatLivre = null;

        try {

            etatLivre = switch (etatChoisie) {

                case "1" -> EtatLivre.DISPONIBLE;

                case "2" -> EtatLivre.EN_RETARD;

                case "3" -> EtatLivre.HORS_SERVICE;

                // Si il a fait un choix invalide

                default -> throw new IllegalStateException();

            };

        } catch (IllegalStateException e) {

            bookView.showBookModificationFailure("Choix de l'Etat non valide");

            handleUserChoice(
                Map.of(
                    "R", () -> handleBookStateModification(idLivre),
                    "A", () -> tryGetBookAndModify(idLivre)
                )
            );

        }

        // Gère la modification en cas de succès

        modifyBookState(etatLivre, idLivre);

    }

    // Modifier l'état du livre

    private void modifyBookState(EtatLivre etatLivre, String idLivre) {

        // Livre dans l'état non modifié

        Livre notYetModifyBook = getBook(idLivre);

        // Modifie l'état

        Livre livreModifier = serviceLivre.changerStatut(idLivre, etatLivre);

        // Afficher le succès

        bookView.showBookModificationSucces("statut", idLivre, notYetModifyBook.getEtatDuLivre().toString(), livreModifier.getEtatDuLivre().toString());

        // Recupere le choix et redirige

        handleModificationSuccesEnd(idLivre);

    }

    /* Modification de l'état du livre */

    /* Modifier la categorie du livre */

    private void handleBookCategoryModification(String idLivre) {

        String categorie = bookView.getBookCategory().trim();

        // Parser pour recuperer la valeur

        CategorieLivre categorieLivre = null;

        try {

            categorieLivre = tryParsingBookCategory(categorie);

        } catch (IllegalArgumentException e) {

            bookView.showBookModificationFailure(e.getMessage());

            // Gestion Choix

            handleUserChoice(
                Map.of(
                    "R", () -> handleBookCategoryModification(idLivre),
                    "A", () -> tryGetBookAndModify(idLivre)
                )
            );

        }

        // En cas de recuperation réeussie

        modifyBookState(categorieLivre, idLivre);

    }

    private void modifyBookState(CategorieLivre categorieLivre, String idLivre) {

        // Recuperer l'ancien livre

        Livre bookNotYetModify = getBook(idLivre);

        // Modifier la categorie

        Livre livreModifier = serviceLivre.changerCategorie(idLivre, categorieLivre);

        // Afficher le succès

        bookView.showBookModificationSucces("categorie", idLivre, bookNotYetModify.getCategorie().toString(), livreModifier.getCategorie().toString());

        // Gestion du retour

        handleModificationSuccesEnd(idLivre);

    }

    /* Modifier la categorie du livre */

    /* Modifier le nombre d'exemplaires du livre */

    private void handleNumberOfCopiesModification(String idLivre) {

        // Recuperer le nombre de copies

        String nombreCopies = bookView.getCopyNumbers().trim();

        // Parser le nombre d'exemplaires

        long nombreExemplaires = 0;

        try {

            nombreExemplaires = tryParsingCopyNumber(nombreCopies);

        } catch (IllegalArgumentException e) {

            bookView.showBookModificationFailure(e.getMessage());

            // Gestion Choix

            handleUserChoice(
                Map.of(
                    "R", () -> handleNumberOfCopiesModification(idLivre),
                    "A", () -> tryGetBookAndModify(idLivre)
                )
            );

        }

        // Gère la modification

        modifyCopyNumber(nombreExemplaires, idLivre);

    }

    // Modifie le nombre d'exemplaires

    private void modifyCopyNumber(long nombreExemplaires, String idLivre) {

        // Ancien état

        Livre oldBook = getBook(idLivre);

        // Modification

        Livre livreModifier = serviceLivre.modifierNombreExemplaire(idLivre, nombreExemplaires);

        // Modification avec succès

        bookView.showBookModificationSucces("nombre d'exemplaires", idLivre, String.valueOf(oldBook.getNombreExemplaire()), String.valueOf(livreModifier.getNombreExemplaire()));

        // Gestion retour

        handleModificationSuccesEnd(idLivre);

    }

    /* Modifier le nombre d'exemplaires du livre */

    // Gestion de la fin de tout les succès identique

    private void handleModificationSuccesEnd(String idLivre) {

        handleUserChoice(Map.of("R", () -> tryGetBookAndModify(idLivre)));

    }

    // Menu 5 - Supprimer un livre

    private void handleBookDeletion() {

        // Recuperer l'id du livre

        String bookId = bookView.getBookId().trim();

        // Gerer la confirmation

        bookView.showBookDeletion(bookId);

        // Gestion Choix

        handleUserChoice(
            Map.of(
                "C", () -> tryToDeleteBook(bookId),
                "A", this::handleBookManagement
            )
        );

    }

    private void tryToDeleteBook(String bookId) {

        // Recuperer le livre

        Livre bookToDelete = getBook(bookId);

        // Gestion de l'appel

        try {

            serviceLivre.supprimerLivre(bookId);

            // Gère la reussite

            handleBookDeletionSucces(bookToDelete);

        } /* En cas d'echec - Gestion de l'erreur */ catch  (LivreNonTrouveException e) {

            bookView.showBookDeletionFailure(bookId, "Livre non trouvé", "Aucun livre trouvé avec l'id \"" + bookId + "\"");

            handleSearchFailureEnd();

        } catch (LivreEnCoursEmpruntException e) {

            bookView.showBookDeletionFailure(bookId, "Livre en cours d'emprunts", "Le livre est actuellement en emprunt, il ne peut pas etre supprimer");

            handleBookDeletionFailureEnd();

        } catch (ReservationEnCoursException e) {

            bookView.showBookDeletionFailure(bookId, "Livre en cours de reservation", "Le livre est dans la liste de reservations actif");

            handleBookDeletionFailureEnd();

        }

        // Si la suppresion réussie

    }

    private void handleBookDeletionSucces(@NonNull Livre livreSupprimer) {

        // Affiche le succès

        bookView.showBookDeletionSucces(livreSupprimer);

        // Gestion Choix

        handleUserChoice(
            Map.of(
                "N", this::handleBookDeletion,
                "A", this::handleBookManagement
            )
        );

    }

    private void handleBookDeletionFailureEnd() {

        handleUserChoice(
            Map.of(
                "R", this::handleBookDeletion,
                "A", this::handleBookManagement
            )
        );

    }

    // Menu 6 - Verifier la disponibilité d'un livre

    private void handleShowAvailability() {

        bookView.showBookAvailability();

        handleUserChoice(
            Map.of(
                "1", this::handleShowAvailabilityById,
                "2", this::handleShowAvailabilityByTitle,
                "R", this::handleBookManagement
            )
        );

    }

    // Methode 1 : Par ID

    private void handleShowAvailabilityById() {

        // Recuperer l'id

        String bookId = bookView.getBookId().trim();

        // Recupère le livre pour l'affichage

        try {

            Livre bookToVerify = serviceLivre.verifierDisponibilite(bookId);

            handleShowAvailabilitySuccesEnd(bookToVerify);

        } catch (LivreNonTrouveException e) {

            // Aucun livre retrouvé avec cet id ~ Methode generique

            bookView.showModificationToStepFailure(bookId);

            handleShowAvailabilityFailureEnd();

        }

    }

    // Methode 2 : Par titre

    private void handleShowAvailabilityByTitle() {

        // Recuperer le titre du livre

        String title = bookView.getTitle().trim();

        // Recuperer les livres portant ou contenant le titre

        List<Livre> booksWithTitle = serviceLivre.verifierDisponibiliteTitre(title);

        // Afficher les livres retrouvés ou aucun

        bookView.showAvailableBooks(booksWithTitle);

        // Gestion choix

        handleUserChoice(Map.of("R", this::handleBookManagement));

    }

    private void handleShowAvailabilitySuccesEnd(@NonNull Livre livreAVerifier) {

        bookView.showAvailabilitySucces(livreAVerifier);

        // Gestion choix

        handleUserChoice(
            Map.of(
                "N", this::handleShowAvailability,
                "R", this::handleBookManagement
            )
        );

    }

    private void handleShowAvailabilityFailureEnd() {

        // Gestion choix

        handleUserChoice(
            Map.of(
                "R", this::handleShowAvailabilityById,
                "A", this::handleShowAvailability
            )
        );

    }

}