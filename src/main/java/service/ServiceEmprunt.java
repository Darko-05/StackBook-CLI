package service;

import exception.*;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import main.AppConfig;
import model.Emprunt;
import model.EtatEmprunt;
import model.Livre;
import model.Membre;
import registre.Emprunts;
import registre.Livres;
import registre.Membres;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

import static util.GeneratorUtil.genererIdEmprunt;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceEmprunt implements IBorrowManager {

    // Instance unique

    private static ServiceEmprunt INSTANCE;

    // Attributs registre

    @NonNull private final Livres registreLivres;
    @NonNull private final Emprunts registreEmprunts;
    @NonNull private final Membres registreMembres;

    // Getter

    public static ServiceEmprunt getInstance() {

        if (INSTANCE == null) INSTANCE = new ServiceEmprunt(
        AppConfig.registreLivres,
        AppConfig.registreEmprunts,
        AppConfig.registreMembres
        );

        return INSTANCE;

    }

    @Override
    @SneakyThrows
    public Emprunt emprunterLivre(String idLivre, String idMembre) {

        // Gerer l'emprunt du livre

        Livre livreAEmprunte = gererEmpruntLivre(idLivre);

        // Retrouver le membre

        Membre membreVoulantEmprunte = gererEmpruntMembre(idLivre, idMembre);

        // Créer l'emprunt

        Emprunt nouveauEmprunt = new Emprunt(genererIdEmprunt.apply(List.of(livreAEmprunte, membreVoulantEmprunte)), livreAEmprunte, membreVoulantEmprunte, LocalDate.now(), LocalDate.now().plusDays(21), null, EtatEmprunt.EN_COURS, 0, 0);

        // Ajouter l'emprunt a l'historique des emprunts du membre

        membreVoulantEmprunte.ajouterAHistoriqueEmprunt(nouveauEmprunt);

        // Ajouter l'emprunt a liste des emprunts actifs du membre

        membreVoulantEmprunte.ajouterUnEmpruntActif(nouveauEmprunt);

        // Ajouter l'emprunt au registre d'emprunt

        registreEmprunts.ajouteremprunt(nouveauEmprunt);

        // Augmenter le nombre d'emprunt actif du membre

        membreVoulantEmprunte.incrementerNombreEmpruntsEnCours();

        // Retourne le nouveau emprunt

        return nouveauEmprunt;

    }

    @Override
    @SneakyThrows
    public Emprunt retournerLivre(String idMembre, String idLivre) {

        // Retrouver le livre

        Livre livreARetourner = registreLivres.findById(idLivre);

        // Retrouver le membre

        Membre membreVoulantRetourner = registreMembres.findById(idMembre);

        // Retirer l'emprunt des emprunts actifs du membre

        Emprunt empruntAMarquerRetourner = membreVoulantRetourner.getEmpruntsActif().stream()
        .filter(emprunt -> emprunt.getLivre().equals(livreARetourner))
        .findAny()

        /* Si l'emprunt n'existe pas */

        .orElseThrow(NoSuchElementException::new);

        // Augmenter le nombres d'exemplaires disponible du livre

        livreARetourner.incrementerNombreExemplaireDisponible();

        // Retirer l'emprunt des emprunts actifs du membre

        membreVoulantRetourner.retirerEmprunt(empruntAMarquerRetourner);

        // Dimunier le nombre d'emprunt en cours du membre

        membreVoulantRetourner.decrementerNombreEmpruntsEnCours();

        // Enregistrer la date de retour effective

        empruntAMarquerRetourner.setDateRetourEffective(LocalDate.now());

        // Retrouver l'emprunt et le mettre a l'état Retourné

        empruntAMarquerRetourner.setEtatEmprunt(EtatEmprunt.RETOURNE);

        // Calculer la penalité de l'emprunt

        empruntAMarquerRetourner.setPenalite(calculerPenalite(membreVoulantRetourner.getId()));

        // Retourne l'emprunt avec les informations mis a jour

        return empruntAMarquerRetourner;

    }

    @Override
    @SneakyThrows
    public Emprunt renouvellerEmprunt(String idMembre, String idLivre) {

        // Retrouver le livre

        Livre livreARenouveller = registreLivres.findById(idLivre);

        // Retrouver le membre

        Membre membreQuiVeutRenouveller = registreMembres.findById(idMembre);

        // Verifier que le membre n'est pas suspendu

        if (membreQuiVeutRenouveller.estSuspendu()) throw new MembreSuspenduException();

        // Retrouver l'emprunt

        Emprunt empruntARenouveller = registreEmprunts.getEmprunts().values().stream()
        .filter(emprunt -> emprunt.getLivre().equals(livreARenouveller) && emprunt.getMembre().equals(membreQuiVeutRenouveller))
        .findAny()

        /* Si l'emprunt n'existait pas */

        .orElseThrow(NoSuchElementException::new);

        // Verifier la limite d'emprunt

        verifierLimiteEmprunt(idMembre);

        // Verifier que le nombre de renouvellement limite de l'emprunt n'est pas atteint

        if (empruntARenouveller.getNombreRenouvellement() >= 2) throw new LimiteRenouvellementAtteintException();

        // Verifier que l'emprunt n'est pas en retard ou que le livre n'a jamais été rendu

        if (empruntARenouveller.getDateRetourPrevu().isBefore(LocalDate.now()) || empruntARenouveller.getDateRetourEffective() == null) throw new EmpruntDejaEnRetardException();

        // Remettre l'emprunt dans ces emprunts actif

        membreQuiVeutRenouveller.ajouterUnEmpruntActif(empruntARenouveller);

        // Reincrementer son nombre d'emprunts en cours

        membreQuiVeutRenouveller.incrementerNombreEmpruntsEnCours();

        // Remettre l'emprunt a l'etat : En cours

        empruntARenouveller.setEtatEmprunt(EtatEmprunt.EN_COURS);

        // Augmenter le nombre de renouvellement de l'emprunt

        empruntARenouveller.incrementerNombreRenouvellement();

        // Remettre la date de retour prévu a dans 21 jours

        empruntARenouveller.setDateRetourPrevu(LocalDate.now().plusDays(21));

        // Remettre a null la date de retour effective

        empruntARenouveller.setDateRetourEffective(null);

        return empruntARenouveller;

    }

    @Override
    public List<Emprunt> toutLesEmprunts() {

        return registreEmprunts.getEmprunts().values().stream().toList();

    }

    @Override
    public List<Emprunt> obtenirEmpruntEnRetard() {

        // Retourne tout les emprunts en retard dans le registre

        return registreEmprunts.getEmprunts().values().stream()
        .filter(emprunt -> emprunt.getDateRetourPrevu().isBefore(LocalDate.now()))
        .toList();

    }

    @Override
    @SneakyThrows
    public double calculerPenalite(String idMembre) {

        // Retrouver le membre

        Membre membreDontOnDoitCalculerLaPenalite = registreMembres.findById(idMembre);

        /* Recuperer les dates de retour en retard de tout ces emprunts actifs */

        List<LocalDate> datesEnRetard = membreDontOnDoitCalculerLaPenalite.getEmpruntsActif().stream()
        .filter(emprunt -> emprunt.getDateRetourEffective() == null)
        .map(Emprunt::getDateRetourPrevu)
        .filter(dateRetour -> dateRetour.isBefore(LocalDate.now()))
        .toList();

        AtomicLong nombreDeJoursEnRetard = new AtomicLong();

        datesEnRetard.forEach(dateEnRetard -> {

            /* Nombre de jours en retard pour chaque date */

            nombreDeJoursEnRetard.addAndGet(ChronoUnit.DAYS.between(dateEnRetard, LocalDate.now()));

        });

        // 500 par jour de retards

        return 500 * nombreDeJoursEnRetard.get();

    }

    @Override
    @SneakyThrows
    public void verifierLimiteEmprunt(String idMembre) {

        if (registreMembres.findById(idMembre).getNombreEmpruntsEnCours() >= 5) throw new QuotaEmpruntAtteintException();

    }

    // Gerer emprunt Livre

    @SneakyThrows
    Livre gererEmpruntLivre(String idLivre) {

        // Retrouver le livre

        Livre livreAEmprunte = registreLivres.findById(idLivre);

        // Verifier que le livre est disponible

        if (!livreAEmprunte.estDisponible()) throw new LivreNonDisponibleException();

        // Dimunier le nombres d'exemplaires disponible du livre

        livreAEmprunte.decrementerNombreExemplaireDisponible();

        // Augmenter le compteur d'emprunt du livre

        livreAEmprunte.incrementerCompteurEmpruntsTotaux();

        // Retourner le livre

        return livreAEmprunte;

    }

    // Gerer emprunt membre


    Membre gererEmpruntMembre(String idLivre, String idMembre) throws LivreNonTrouveException, MembreNonTrouveException, MembreSuspenduException, LivreEnCoursEmpruntException {

        // Livre a emprunte

        Livre livreAEmprunte = registreLivres.findById(idLivre);

        // Retrouver le membre

        Membre membreVoulantEmprunte = registreMembres.findById(idMembre);

        // S'assurer que le membre n'est pas suspendu

        if (membreVoulantEmprunte.estSuspendu()) throw new MembreSuspenduException();

        // Verifier son nombre d'emprunt actif

        verifierLimiteEmprunt(idMembre);

        // Empecher l'emprunt d'un meme livre déja emprunté

        if (membreVoulantEmprunte.getEmpruntsActif().stream().anyMatch(emprunt -> emprunt.getLivre().equals(livreAEmprunte))) throw new LivreEnCoursEmpruntException();

        return membreVoulantEmprunte;

    }

}