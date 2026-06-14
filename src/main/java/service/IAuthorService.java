package service;

import exception.AuteurDejaExistantException;
import exception.AuteurEnCoursUtilisationException;import exception.AuteurNonTrouveException;
import model.Auteur;import java.util.List;
import java.util.Map;

public interface IAuthorService {

    // Méthodes a implementer

    Auteur ajouterAuteur(String nom, String prenom, String nationalite, String biographie) throws AuteurDejaExistantException;

    Auteur supprimerAuteur(String nom, String prenom) throws AuteurEnCoursUtilisationException, AuteurNonTrouveException;

    Auteur findAuthor(String nom, String prenom) throws AuteurNonTrouveException;

    Map<Auteur, Long> chercherAuteurParNom(String unNom);

    List<Auteur> toutLesAuteurs();

}
