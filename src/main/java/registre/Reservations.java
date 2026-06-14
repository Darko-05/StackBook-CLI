package registre;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import model.Reservation;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Reservations {

    // Instance unique

    private static final Reservations INSTANCE = new Reservations();

    // Attribut registre

    private final Queue<Reservation> reservations = new PriorityQueue<>(Comparator.comparingInt(reservation -> reservation.getEtatReservation().getPriorite()));

    // Getters

    public static Reservations getInstance() {

        return INSTANCE;

    }

    public Queue<Reservation> getReservations() {

        return new ReservationRecord(reservations).reservations();

    }

    // Méthodes metier

    public void ajouterReservation(@NonNull Reservation r) {

        reservations.add(r);

    }

    public void supprimerReservation(@NonNull Reservation r) {

        reservations.remove(r);

    }

    public Reservation findById(String idReservation) {

        return reservations.stream()
        .filter(reservation -> reservation.getIdReservation().equals(idReservation))
        .findAny()
        .orElseThrow(NoSuchElementException::new);

    }

}
