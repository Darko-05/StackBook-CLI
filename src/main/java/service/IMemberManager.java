package service;

import exception.MembreNonTrouveException;
import exception.MembreSuspenduException;
import exception.QuotaEmpruntAtteintException;
import model.Emprunt;
import model.Livre;
import model.Membre;
import model.StatutMembre;

import java.util.List;

public interface IMemberManager {

    // Méthodes a implementer

    Membre inscrireMembre(String nom, String prenom, String email, String numeroTelephone);

    Membre trouverMembreParId(String id) throws MembreNonTrouveException;

    List<Membre> trouverMembreParNom(String nom);

    List<Membre> toutLesMembres();

    Membre modifierNom(String idMembre, String nouveauNom) throws MembreNonTrouveException;

    Membre modifierPrenom(String idMembre, String nouveauPrenom) throws MembreNonTrouveException;

    Membre modifierEmail(String idMembre, String nouveauEmail) throws MembreNonTrouveException;

    Membre modiferNumeroTelephone(String idMembre, String nouveauNumero) throws MembreNonTrouveException;

    Membre changerStatutMembre(String idMembre, StatutMembre nouveauStatut) throws MembreNonTrouveException, MembreSuspenduException;

    void verifierEligibiliteEmprunt(String idMembre) throws MembreNonTrouveException, MembreSuspenduException, QuotaEmpruntAtteintException;

    List<Emprunt> consulterHistoriqueMembre(String idMembre) throws MembreNonTrouveException;

    List<Livre> suggererLivreMembre(String idMembre) throws MembreNonTrouveException;

}