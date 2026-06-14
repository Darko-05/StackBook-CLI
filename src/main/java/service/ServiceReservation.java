package service;

import exception.*;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import main.AppConfig;
import model.*;
import registre.Livres;
import registre.Membres;
import registre.Reservations;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static main.AppConfig.registreReservations;
import static util.GeneratorUtil.genererIdReservation;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceReservation implements IReservationManager {

    // Instance unique

    private static ServiceReservation INSTANCE;

    // Attribut service

    @NonNull private final ServiceEmprunt serviceEmprunt;
    @NonNull private final ServiceMembre serviceMembre;

    // Attributs registre

    @NonNull private final Reservations registreReservation;
    @NonNull private final Livres registreLivres;
    @NonNull private final Membres registreMembres;

    // Getter

    public static ServiceReservation getInstance() {

        if (INSTANCE == null) INSTANCE = new ServiceReservation(
        AppConfig.serviceEmprunt,
        AppConfig.serviceMembre,
        AppConfig.registreReservations,
        AppConfig.registreLivres,
        AppConfig.registreMembres
        );

        return INSTANCE;

    }

    // Redefinition

    @Override
    public Reservation reserverLivre(String idLivre, String idMembre, LocalDate dateExpriration) throws LivreNonTrouveException, ReservationEnCoursException, LimiteReservationAtteintException, MembreSuspenduException, LivreNonDisponibleException, MembreNonTrouveException {

        // Trouver le livre

        Livre livreAReserver = registreLivres.findById(idLivre);

        // Trouver le membre

        Membre membreVoulantReserver = registreMembres.findById(idMembre);

        // Verifier que le livre est disponible

        if (!livreAReserver.estDisponible()) throw new LivreNonDisponibleException();

        // S'assurer que le membre n'est pas suspendu

        if (membreVoulantReserver.estSuspendu()) throw new MembreSuspenduException();

        // Verifier que le membre n'a pas déja une reservation actif sur le meme livre

        if (registreReservation.getReservations().stream()
        .anyMatch(reservation -> reservation.getLivre().equals(livreAReserver) && reservation.getMembre().equals(membreVoulantReserver)) /* Impossible de reserver deux fois le meme livre */
        ) throw new ReservationEnCoursException();

        // Assurer une limite de 3 reservation par membre

        if (registreReservation.getReservations().stream().filter(reservation -> reservation.getMembre().equals(membreVoulantReserver)).count() >= 3) throw new LimiteReservationAtteintException();

        // Créer la reservation

        Reservation nouvelleReservation = new Reservation(genererIdReservation.apply(List.of(livreAReserver, membreVoulantReserver)), livreAReserver, membreVoulantReserver, LocalDate.now(), /* 10 jours pour venir prendre le livre */ LocalDate.now().plusDays(10), EtatReservation.EN_COURS);

        // Ajouter la reservation au registre

        registreReservation.ajouterReservation(nouvelleReservation);

        // Retourner la reservation

        return nouvelleReservation;

    }

    @Override
    public Emprunt confirmerReservation(String idReservation) {

        // Trouver la reservation

        /* Si non trouvée -> lève : NoSuchElementException */

        Reservation reservationAValider = registreReservation.findById(idReservation);

        // Valider la reservation

        reservationAValider.changerEtatReservation(EtatReservation.VALIDE);

        // Appeler le service d'emprunt

        return serviceEmprunt.emprunterLivre(reservationAValider.getLivre().getId(), reservationAValider.getMembre().getId());

    }

    @Override
    public void annulerReservation(String idReservation) {

        // Trouver la reservation

        /* Si non trouvée -> lève : NoSuchElementException */

        Reservation reservationAValider = registreReservation.findById(idReservation);

        // Annuler la reservation

        reservationAValider.changerEtatReservation(EtatReservation.ANNULE);

        // Retourner la reservation

    }

    @Override
    public List<Reservation> toutLesReservations() {

        return registreReservation.getReservations().stream().toList();

    }

    @Override
    public PriorityQueue<Reservation> consulterReservationsMembre(String idMembre) {

        // Retrouver le membre

        Membre reservationsDuMembre = serviceMembre.trouverMembreParId(idMembre);

        // Retourner la liste de ses reservations en ordre

        return registreReservation.getReservations().stream()
        .filter(reservation -> reservation.getMembre().equals(reservationsDuMembre))
        .collect(Collectors.toCollection(PriorityQueue::new));

    }

    @Override
    @SneakyThrows
    public PriorityQueue<Reservation> obtenirListeAttente(String idLivre) {

        // Trouver le livre

        Livre livreDontOnVeutConsulterLaListeAttente = registreLivres.findById(idLivre);

        // Retourne la liste ordonné des reservations pour le livre

        return registreReservation.getReservations().stream().filter(reservation -> reservation.getLivre().equals(livreDontOnVeutConsulterLaListeAttente))
        .collect(Collectors.toCollection(PriorityQueue::new));

    }

    @Override
    @SneakyThrows
    public void supprimerToutesReservationsPourLivre(String idLivre) {

        // Trouver le livre

        Livre livreDontOnVeutSupprimerLesReservations = registreLivres.findById(idLivre);

        // Trouver la liste de reservations concernant le livre

        List<Reservation> listeASupprimer = registreReservation.getReservations().stream()
        .filter(reservation -> reservation.getLivre().equals(livreDontOnVeutSupprimerLesReservations))
        .toList();

        // Méthode courte pour supprimer tout directement

        listeASupprimer.forEach(registreReservation::supprimerReservation);

    }

    @Override
    @SneakyThrows
    public long compterReservationPourLivre(String idLivre) {

        // Trouver le livre

        Livre livreDontOnVeutCompterLesReservations = registreLivres.findById(idLivre);

        return registreReservations.getReservations().stream()
        .filter(reservation -> reservation.getEtatReservation() == EtatReservation.EN_COURS)
        .filter(reservation -> reservation.getLivre().equals(livreDontOnVeutCompterLesReservations))
        .count();

    }

}