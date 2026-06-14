package service;

import exception.*;
import model.Emprunt;
import model.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.PriorityQueue;

public interface IReservationManager {

    Reservation reserverLivre(String idLivre, String idMembre, LocalDate dateExpriration) throws LivreNonTrouveException, LivreNonDisponibleException, MembreNonTrouveException, MembreSuspenduException, ReservationEnCoursException, LimiteReservationAtteintException;

    Emprunt confirmerReservation(String idReservation);

    void annulerReservation(String idReservation);

    List<Reservation> toutLesReservations();

    PriorityQueue<Reservation> consulterReservationsMembre(String idMembre) throws MembreNonTrouveException;

    PriorityQueue<Reservation> obtenirListeAttente(String idLivre) throws LivreNonTrouveException;

    void supprimerToutesReservationsPourLivre(String idLivre) throws LivreNonTrouveException;

    long compterReservationPourLivre(String idLivre) throws LivreNonTrouveException;

}
