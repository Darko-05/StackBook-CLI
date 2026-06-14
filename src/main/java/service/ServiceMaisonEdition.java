package service;

import exception.MaisonEditionEnCoursUtilisationException;
import exception.MaisonEditionNonTrouverException;
import exception.MaisonEditonDejaExistantException;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import main.AppConfig;
import model.MaisonEdition;
import registre.Livres;
import registre.MaisonEditions;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceMaisonEdition implements IMEManager{

    // Instance unique

    private static ServiceMaisonEdition INSTANCE;

    // Attributs registre

    @NonNull private final MaisonEditions registreMaisonEdition;
    @NonNull private final Livres registreLivres;

    // Getter

    public static ServiceMaisonEdition getInstance() {

        if (INSTANCE == null) INSTANCE = new ServiceMaisonEdition(
        AppConfig.registreMaisonEdition,
        AppConfig.registreLivres
        );

        return INSTANCE;

    }

    // Redefinition

    @Override
    public MaisonEdition ajouterUneMaisonEdition(String nom, String codePostal, String numeroDeTelephone, String ville, LocalDate dateDeCreation) throws MaisonEditonDejaExistantException {

        // Si une maison d'edition avec le meme nom existe déja

        if (registreMaisonEdition.getMaisonEditions().stream().anyMatch(maisonEdition -> maisonEdition.nom().equalsIgnoreCase(nom))) throw new MaisonEditonDejaExistantException();

        // Créer la maison d'édition

        MaisonEdition nouvelleMaisonEdition = new MaisonEdition(nom, codePostal, numeroDeTelephone, ville, dateDeCreation);

        // Ajouter la maison d'edition au registre

        registreMaisonEdition.ajouterMaisonEdition(nouvelleMaisonEdition);

        // Retourner la maison d'edition

        return nouvelleMaisonEdition;

    }

    @Override
    public boolean supprimerMaisonEdition(String nom) throws MaisonEditionNonTrouverException, MaisonEditionEnCoursUtilisationException {

        // Retrouver la maison d'edition

        MaisonEdition maisonEditionASupprimer = registreMaisonEdition.getMaisonEditions().stream()
        .filter(maisonEdition -> maisonEdition.nom().equalsIgnoreCase(nom))
        .findAny()
        .orElseThrow(MaisonEditionNonTrouverException::new);

        // Si la maison d'édition est lié a un livre

        if (registreLivres.getLivres().values().stream().anyMatch(livre -> livre.getMaisonEdition().nom().equalsIgnoreCase(maisonEditionASupprimer.nom()))) throw new MaisonEditionEnCoursUtilisationException();

        // Supprimer la maison d'edition

        registreMaisonEdition.supprimerMaisonEdition(maisonEditionASupprimer);

        // Renvoie true

        return true;

    }

    @Override
    public MaisonEdition findHomeEditor(String nomMaisonEdition) throws MaisonEditionNonTrouverException {

        return registreMaisonEdition.getMaisonEditions().stream()
        .filter(maisonEdition -> maisonEdition.nom().equalsIgnoreCase(nomMaisonEdition))
        .findAny()
        .orElseThrow(MaisonEditionNonTrouverException::new);

    }

    @Override
    public List<MaisonEdition> chercherParNom(String unNom) {

        // Retourne toute les maisons d'edition qui contiennent ce nom

        return registreMaisonEdition.getMaisonEditions().stream()
        .filter(maisonEdition -> maisonEdition.nom().toLowerCase().contains(unNom.toLowerCase()))
        .toList();

    }

    @Override
    public List<MaisonEdition> toutLesMaisonEdition() {

        return registreMaisonEdition.getMaisonEditions();

    }

}