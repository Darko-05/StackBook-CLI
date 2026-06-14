package service;

import exception.LivreDejaExistantException;
import exception.LivreEnCoursEmpruntException;
import exception.LivreNonTrouveException;
import exception.ReservationEnCoursException;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import main.AppConfig;
import model.*;
import registre.Emprunts;
import registre.Livres;
import registre.Reservations;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

import static util.GeneratorUtil.genererIdLivre;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceLivre implements IBookManager{

    // Instance unique

    private static ServiceLivre INSTANCE;

    // Utilise registre de livres et registre d'emprunts

    @NonNull private final Livres registreLivre;
    @NonNull private final Emprunts registreEmprunt;
    @NonNull private final Reservations registreReservation;

    // Getter

    public static ServiceLivre getInstance() {

        if (INSTANCE == null) INSTANCE = new ServiceLivre(
        AppConfig.registreLivres,
        AppConfig.registreEmprunts,
        AppConfig.registreReservations
        );

        return INSTANCE;

    }

    // Redefinition des méthodes

    @Override
    public Livre ajouterLivre(String isbn, String titre, Auteur auteur, MaisonEdition maisonEdition, Year anneePublication, CategorieLivre categorie, long nombreExemplaire) throws LivreDejaExistantException {

        // Créer le livre

        Livre l = new Livre(null, isbn, titre, auteur, LocalDate.now(), maisonEdition, anneePublication, categorie, EtatLivre.DISPONIBLE, nombreExemplaire, nombreExemplaire, 0);

        // Assigner un id au livre

        l.setId(genererIdLivre.apply(l));

        // Si le livre existait déja dans le registre

        if (registreLivre.contientLivre(l)) throw new IllegalStateException();

        // Si l'isbn du livre était déja lié a un livre dans le registre

        if (registreLivre.getLivres().values().stream().anyMatch(livre -> livre.getIsbn().equals(l.getIsbn()))) throw new LivreDejaExistantException();

        // Ajouter le livre

        registreLivre.ajouterLivre(l);

        // Retourne le nouveau livre

        return l;

    }

    @Override
    public Livre rechercherParId(String id) throws LivreNonTrouveException {

        // Appel la méthode findById

        return registreLivre.findById(id);

    }

    @Override
    public List<Livre> rechercherParTitre(String titre) {

        // Renvoie tout les livres ayant le meme titre

        return registreLivre.getLivres().values().stream()
        .filter(livre -> livre.getTitre().equalsIgnoreCase(titre))
        .toList();

    }

    @Override
    public List<Livre> rechercherParAuteur(String nomAuteur) {

        // Renvoie tout les livres ayant le meme nom d'auteur

        return registreLivre.getLivres().values().stream()
        .filter(livre -> livre.getAuteur().nom().equalsIgnoreCase(nomAuteur) || livre.getAuteur().prenom().equalsIgnoreCase(nomAuteur))
        .toList();

    }

    @Override
    public List<Livre> rechercherParCategorie(CategorieLivre categorie) {

        // Retourne les livres dont la categorie est celui indiquer

        return registreLivre.getLivres().values().stream()
        .filter(livre -> livre.getCategorie() == categorie)
        .toList();

    }

    @Override
    public List<Livre> livresDisponible() {

        // Retourne les livres disponibles

        return registreLivre.getLivres().values().stream()
        .filter(Livre::estDisponible)
        .toList();

    }

    @Override
    public List<Livre> toutLesLivres() {

        // Retourne la liste de tout les livres

        return registreLivre.getLivres().values().stream().toList();

    }

    @Override
    @SneakyThrows
    public Livre modifierTitre(String id, String titre) {

        // Retrouver le livre

        Livre modifierTitre = rechercherParId(id);

        // Changer son nom

        modifierTitre.setTitre(titre);

        // Retourne l'objet modifier

        return modifierTitre;

    }

    @Override
    @SneakyThrows
    public Livre changerStatut(String id, EtatLivre statut) {

        // Retrouver le livre

        Livre changerStatut = rechercherParId(id);

        // Modifier le statut

        changerStatut.setEtatDuLivre(statut);

        // Retourne l'objet modifier

        return changerStatut;

    }

    @Override
    @SneakyThrows
    public Livre changerCategorie(String id, CategorieLivre categorie) {

        // Retrouver le livre

        Livre changerCategorie = rechercherParId(id);

        // Modifier la categorie

        changerCategorie.setCategorie(categorie);

        // Retourne l'objet modifier

        return changerCategorie;

    }

    @Override
    @SneakyThrows
    public Livre modifierNombreExemplaire(String id, long nombreExemplaire) {

        // Retrouver le livre

        Livre modifierNombreExemplaire = rechercherParId(id);

        // Modifier le nombre d'exemplaires

        modifierNombreExemplaire.setNombreExemplaire((int) nombreExemplaire);

        // Retourne l'objet modifier

        return modifierNombreExemplaire;

    }

    @Override
    public void supprimerLivre(String id) throws LivreNonTrouveException, LivreEnCoursEmpruntException, ReservationEnCoursException {

        Livre supprimerLivre = rechercherParId(id);

        // Si le livre est en emprunt

        if (registreEmprunt.getEmprunts().values().stream().filter(emprunt -> emprunt.getLivre().equals(supprimerLivre)).anyMatch(emprunt -> emprunt.getEtatEmprunt() == EtatEmprunt.EN_COURS))
         throw new LivreEnCoursEmpruntException();

        // Verifier si le livre est reserver

        if (registreReservation.getReservations().stream().filter(reservation -> reservation.getLivre().getId().equals(id)).anyMatch(reservation -> reservation.getEtatReservation() == EtatReservation.EN_COURS)) throw new ReservationEnCoursException();

        // Supprimer le livre

        registreLivre.retirerLivre(supprimerLivre);

    }

    @Override
    public Livre verifierDisponibilite(String id) throws LivreNonTrouveException {

        return rechercherParId(id);

    }

    @Override
    public List<Livre> verifierDisponibiliteTitre(String titre) {

        // Les livres ayant le titre

        return rechercherParTitre(titre);

    }

}