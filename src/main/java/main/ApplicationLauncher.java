package main;

import lombok.NoArgsConstructor;
import model.EtatReservation;
import model.StatutMembre;

import java.time.LocalDate;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.IO.println;
import static main.AppConfig.registreMembres;
import static main.AppConfig.registreReservations;

@NoArgsConstructor
public class ApplicationLauncher {

    // Variable systeme

    public static final LocalDate DATE_LANCEMENT = LocalDate.now();

    // SchedulerExercutorService

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    // Launch method

    public void run() {

        Runtime.getRuntime().addShutdownHook(new Thread(this::arreter));

        demarrer();

        MainController.getInstance().launchControllers();

    }

    // Methodes planifier

    private void demarrer() {

        scheduler.scheduleAtFixedRate(
        this::executerTaches,
        0,
        24,
        TimeUnit.HOURS
        );

    }

    private void executerTaches() {

        actualiserReservations();
        actualiserMembres();

    }

    public void arreter() {

        println("Arrêt du scheduler...");

        scheduler.shutdown();

        try {

            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {

                scheduler.shutdownNow();

            }

        } catch (InterruptedException e) {

            scheduler.shutdownNow();

            Thread.currentThread().interrupt();

        }

    }

    private void actualiserReservations() {

        registreReservations.getReservations().stream()
        .filter(reservation -> reservation.getDateExpiration().isBefore(LocalDate.now()))
        .forEach(reservation -> reservation.setEtatReservation(EtatReservation.EXPIRE));

    }

    private void actualiserMembres() {

        registreMembres.getMembres().values().forEach(membre -> {

            long nbRetards = membre.nombreEmpruntsEnRetards();

            if (nbRetards >= 3) {

                membre.setStatut(StatutMembre.SUSPENDU);

            } else if (nbRetards > 0) {

                membre.setStatut(StatutMembre.RETARDATAIRE);

            } else {

                membre.setStatut(StatutMembre.ACTIF);

            }

        });

    }

}