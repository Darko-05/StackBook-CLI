package util;

import exception.LivreNonTrouveException;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import model.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static main.AppConfig.*;
import static main.AppConfig.registreLivres;
import static util.ParsingUtil.toLinkedHashMap;

@UtilityClass
public final class DataUtil {

    public static long nombreTotalLivres() {

        return registreLivres.getLivres().size();

    }

    public static long nombreTotalEmprunts() {

        return registreEmprunts.getEmprunts().size();

    }

    public static long nombreTotalReservations() {

        return registreReservations.getReservations().size();

    }

    public static long nombreFilesAttente() {

        return registreReservations.getReservations().stream()
        .filter(reservation -> reservation.getEtatReservation() == EtatReservation.EN_COURS)
        .map(Reservation::getLivre)
        .distinct()
        .count();

    }

    public static int positionDansLaFileAttente(Reservation r) {

        PriorityQueue<Reservation> copy = new PriorityQueue<>(registreReservations.getReservations());

        int position = 0;

        while (!copy.isEmpty()) {

            Reservation reservation = copy.poll();

            position++;

            if (reservation.equals(r)) {

                // Position trouvée

                break;
            }

        }

        return position;

    }

    public static long nombreReservationsMembre(Membre membre) {

        return registreReservations.getReservations().stream()
        .filter(reservation -> reservation.getMembre().equals(membre))
        .count();

    }

    public static long nombreTotalMaisonEdition() {

        return registreMaisonEdition.getMaisonEditions().size();

    }

    public static long nombreTotalMembres() {

        return registreMembres.getMembres().size();

    }

    public static long nombreAuteurs() {

        return registreAuteurs.getAuteurs().size();

    }

    public static long countLines(FileReader reader) {

        long nombreLignes = 0;

        try (BufferedReader br = new BufferedReader(reader)) {

            String line;

            while ((line = br.readLine()) != null) {

                if (!line.trim().isEmpty()) {

                    nombreLignes++;

                }

            }

        } catch (FileNotFoundException e) {

            throw new IllegalArgumentException("Fichier non retrouvée.");

        } catch (IOException e) {

            throw new IllegalArgumentException("Erreur de lecture du fichier.");

        }

        return nombreLignes;

    }

    public static Map<Auteur, Long> top2AuteursProlifiques() {

        return registreLivres.getLivres().values().stream()
        .collect(Collectors.groupingBy(

            // Clé -> Auteur

            Livre::getAuteur,

            // Nombre de livres par auteur

            Collectors.counting()

        ))
        .entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
        .limit(2)
        .collect(toLinkedHashMap());

    }

    public static List<Livre> homeEditorBooks(@NonNull MaisonEdition homeEditor) {

        return registreLivres.getLivres().values().stream()
        .filter(livre -> livre.getMaisonEdition().nom().equalsIgnoreCase(homeEditor.nom()))
        .toList();

    }

    public static List<Livre> authorBooks(@NonNull Auteur a) {

        return registreLivres.getLivres().values().stream()
        .filter(livre -> livre.getAuteur().equals(a))
        .toList();

    }

    public static long nombreEmpruntsEnCoursAuteur(@NonNull Auteur a) {

        Set<Livre> livresHash = new HashSet<>(authorBooks(a));

        return registreEmprunts.getEmprunts().values().stream()
        .filter(emprunt -> livresHash.contains(emprunt.getLivre()))
        .filter(emprunt -> emprunt.getEtatEmprunt() == EtatEmprunt.EN_COURS)
        .count();

    }

    public static long nombreEmpruntsHistoriqueAuteur(@NonNull Auteur a) {

        Set<Livre> livresHash = new HashSet<>(authorBooks(a));

        return registreEmprunts.getEmprunts().values().stream()
        .filter(emprunt -> livresHash.contains(emprunt.getLivre()))
        .count();

    }

    public static List<Membre> membresInactifs() {

        return registreMembres.getMembres().values().stream()
        .filter(membre -> membre.getStatut() == StatutMembre.INACTIF || membre.getStatut() == StatutMembre.SUSPENDU)
        .toList();

    }

    // Methodes de recuperation

    public static Livre getBook(String idLivre) {

        Livre bookToModify;

        try {

            bookToModify = registreLivres.findById(idLivre);

        } catch (LivreNonTrouveException e) {

            // Ne devrais jamais arriver a cette étape

            throw new IllegalStateException("Modification du titre malgré livre non trouvé !");

        }

        return bookToModify;

    }

}