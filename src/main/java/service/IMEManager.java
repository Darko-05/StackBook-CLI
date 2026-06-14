package service;

import exception.MaisonEditionEnCoursUtilisationException;
import exception.MaisonEditionNonTrouverException;
import exception.MaisonEditonDejaExistantException;
import model.MaisonEdition;

import java.time.LocalDate;
import java.util.List;

public interface IMEManager {

    // Méthodes a implementer

    MaisonEdition ajouterUneMaisonEdition(String nom, String codePostal, String numeroDeTelephone, String ville, LocalDate dateDeCreation) throws MaisonEditonDejaExistantException;

    boolean supprimerMaisonEdition(String nom) throws MaisonEditionNonTrouverException, MaisonEditionEnCoursUtilisationException;

    MaisonEdition findHomeEditor(String nomMaisonEdition) throws MaisonEditionNonTrouverException;

    List<MaisonEdition> chercherParNom(String unNom);

    List<MaisonEdition> toutLesMaisonEdition();

}