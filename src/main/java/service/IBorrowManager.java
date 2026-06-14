package service;

import exception.*;
import model.Emprunt;

import java.util.List;

public interface IBorrowManager {

    // Méthodes a implementer

    Emprunt emprunterLivre(String idLivre, String idMembre) throws MembreNonTrouveException, MembreSuspenduException, LivreNonTrouveException, LivreNonDisponibleException, QuotaEmpruntAtteintException, LivreEnCoursEmpruntException;

    Emprunt retournerLivre(String idMembre, String idLivre) throws LivreNonTrouveException, MembreNonTrouveException;

    Emprunt renouvellerEmprunt(String idMembre, String idLivre) throws LivreNonTrouveException, LimiteRenouvellementAtteintException, EmpruntDejaEnRetardException, MembreNonTrouveException, MembreSuspenduException, QuotaEmpruntAtteintException;

    List<Emprunt> toutLesEmprunts();

    List<Emprunt> obtenirEmpruntEnRetard();

    double calculerPenalite(String idMembre) throws MembreNonTrouveException;

    void verifierLimiteEmprunt(String idMembre) throws MembreNonTrouveException, QuotaEmpruntAtteintException;

}
