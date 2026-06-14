package service;

import exception.LivreDejaExistantException;
import exception.LivreEnCoursEmpruntException;
import exception.LivreNonTrouveException;
import exception.ReservationEnCoursException;
import model.*;

import java.time.Year;
import java.util.List;

public interface IBookManager {

    // Méthodes a implementer

    Livre ajouterLivre(String ISBN, String titre, Auteur auteur, MaisonEdition maisonEdition, Year anneePublication, CategorieLivre categorie, long nombreExemplaire) throws LivreDejaExistantException;

    Livre rechercherParId(String id) throws LivreNonTrouveException;

    List<Livre> rechercherParTitre(String titre);

    List<Livre> rechercherParAuteur(String nomAuteur);

    List<Livre> rechercherParCategorie(CategorieLivre categorie);

    List<Livre> livresDisponible();

    List<Livre> toutLesLivres();

    Livre modifierTitre(String id, String titre) throws LivreNonTrouveException;

    Livre changerStatut(String id, EtatLivre statut) throws LivreNonTrouveException;

    Livre changerCategorie(String id, CategorieLivre categorie) throws LivreNonTrouveException;

    Livre modifierNombreExemplaire(String id, long nombreExemplaire) throws LivreNonTrouveException;

    void supprimerLivre(String id) throws LivreNonTrouveException, LivreEnCoursEmpruntException, ReservationEnCoursException;

    Livre verifierDisponibilite(String id) throws LivreNonTrouveException;

    List<Livre> verifierDisponibiliteTitre(String titre) throws LivreNonTrouveException;

}