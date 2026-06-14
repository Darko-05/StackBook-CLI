package model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class Membre {

    // Attributs d'un membre

    @EqualsAndHashCode.Include
    private String id;
    private long numeroMembre;
    private String nom;
    private String prenom;
    private String email;
    private String numeroDeTelephone;
    private LocalDate dateInscription;
    private StatutMembre statut;
    private long nombreEmpruntsEnCours;
    private List<Emprunt> empruntsActif;
    private Map<String, Emprunt> historiqueEmprunts;

    // Méthodes utilitaire

    public long nombreEmpruntsEnRetards() {

        return empruntsActif.stream()
        .filter(emprunt -> emprunt.getEtatEmprunt() == EtatEmprunt.EN_RETARD)
        .count();

    }

    public long nombreJoursEnRetards() {

        return empruntsActif.stream()
        .filter(emprunt -> emprunt.getEtatEmprunt() == EtatEmprunt.EN_RETARD)
        .mapToLong(emprunt -> ChronoUnit.DAYS.between(emprunt.getDateRetourPrevu(), LocalDate.now()))
        .sum();

    }

    public void incrementerNombreEmpruntsEnCours() {

        nombreEmpruntsEnCours++;

    }

    public void decrementerNombreEmpruntsEnCours() {

        nombreEmpruntsEnCours--;

    }

    public void ajouterAHistoriqueEmprunt(Emprunt e) {

        historiqueEmprunts.put(e.getId(), e);

    }

    public void ajouterUnEmpruntActif(Emprunt e) {

        empruntsActif.add(e);

    }

    public void retirerEmprunt(Emprunt e) {


        empruntsActif.remove(e);
    }

    public boolean estSuspendu() {

        return statut == StatutMembre.SUSPENDU;

    }

}
