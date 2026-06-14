package service;

import exception.AuteurEnCoursUtilisationException;
import exception.AuteurDejaExistantException;
import exception.AuteurNonTrouveException;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import main.AppConfig;
import model.Auteur;
import registre.Auteurs;
import registre.Livres;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static util.DataUtil.authorBooks;

@RequiredArgsConstructor (access = AccessLevel.PRIVATE)
public class ServiceAuteur implements IAuthorService {

    // Instance unique

    private static ServiceAuteur INSTANCE;

    // Attributs registre

    @NonNull private final Auteurs registreAuteurs;
    @NonNull private final Livres registreLivres;

    // Getter

    public static ServiceAuteur getInstance() {

        if (INSTANCE == null) INSTANCE = new ServiceAuteur(
        AppConfig.registreAuteurs,
        AppConfig.registreLivres
        );

        return INSTANCE;

    }

    // Redefinition

    @Override
    public Auteur ajouterAuteur(String nom, String prenom, String nationalite, String biographie) throws AuteurDejaExistantException {

        // Si un auteur existe déja avec le meme nom et prenom

        if (registreAuteurs.getAuteurs().stream().anyMatch(auteur -> auteur.nom().equalsIgnoreCase(nom) && auteur.prenom().equalsIgnoreCase(prenom))) {

            throw new AuteurDejaExistantException();

        }

        // Creation de l'auteur

        Auteur nouveauAuteur = new Auteur(nom, prenom, nationalite, biographie);

        // Ajout de l'auteur dans le registre

        registreAuteurs.ajouterAuteur(nouveauAuteur);

        // Retourner l'auteur

        return nouveauAuteur;

    }

    @Override
    public Auteur supprimerAuteur(String nom, String prenom) throws AuteurNonTrouveException, AuteurEnCoursUtilisationException {

        // Recuperer l'auteur

        Auteur toDelete;

        // Retrouver l'auteur

        Auteur auteurASupprimer = registreAuteurs.getAuteurs().stream()
        .filter(auteur -> auteur.nom().equalsIgnoreCase(nom) && auteur.prenom().equalsIgnoreCase(prenom))
        .findAny()
        .orElseThrow(AuteurNonTrouveException::new);

        toDelete = auteurASupprimer;

        // Si l'auteur est actuellement associée a un livre

        if (registreLivres.getLivres().values().stream().anyMatch(livre -> livre.getAuteur().nom().equalsIgnoreCase(auteurASupprimer.nom()) && livre.getAuteur().prenom().equalsIgnoreCase(auteurASupprimer.prenom()))) throw new AuteurEnCoursUtilisationException();

        // Supprimer l'auteur

        registreAuteurs.supprimerAuteur(auteurASupprimer);

        return toDelete;

    }

    @Override
    public Auteur findAuthor(String nom, String prenom) throws AuteurNonTrouveException {

        return registreAuteurs.getAuteurs().stream()
        .filter(auteur -> auteur.nom().equalsIgnoreCase(nom) && auteur.prenom().equalsIgnoreCase(prenom))
        .findAny()
        .orElseThrow(AuteurNonTrouveException::new);

    }

    @Override
    public Map<Auteur, Long> chercherAuteurParNom(String unNom) {

       return registreAuteurs.getAuteurs().stream()
       .filter(auteur -> auteur.nom().equalsIgnoreCase(unNom) || auteur.prenom().equalsIgnoreCase(unNom))
       .collect(Collectors.toMap(

           auteur -> auteur,

           auteur -> (long) authorBooks(auteur).size(),

           (a, _) -> a,

           HashMap::new

       ));

    }

    @Override
    public List<Auteur> toutLesAuteurs() {

        return registreAuteurs.getAuteurs();

    }

}