package registre;

import org.apache.commons.collections4.queue.UnmodifiableQueue;
import model.Reservation;

import java.util.LinkedList;
import java.util.Queue;

public record ReservationRecord(Queue<Reservation> reservations) {

    public ReservationRecord {

        reservations = UnmodifiableQueue.unmodifiableQueue(new LinkedList<>(reservations));

    }

}
