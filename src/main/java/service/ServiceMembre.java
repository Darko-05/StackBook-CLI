package service;

import exception.MembreSuspenduException;
import exception.QuotaEmpruntAtteintException;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import main.AppConfig;
import model.*;
import registre.Livres;
import registre.Membres;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static util.GeneratorUtil.genererIdMembre;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceMembre implements IMemberManager {

    // Instance unique

    private static ServiceMembre INSTANCE;

    // Attribut registre

    @NonNull private final Membres registreMembres;
    @NonNull private final Livres registreLivres;

    // Getter

    public static ServiceMembre getInstance() {

        if (INSTANCE == null) INSTANCE = new ServiceMembre(
        AppConfig.registreMembres,
        AppConfig.registreLivres
        );

        return INSTANCE;

    }

    @Override
    public Membre inscrireMembre(String nom, String prenom, String email, String numeroTelephone) {

        // Creation du membre

        Membre nouveauMembre = new Membre(null, 0, nom, prenom, email, numeroTelephone, LocalDate.now(), StatutMembre.ACTIF, 0, new ArrayList<>(), new LinkedHashMap<>());

        // Assigner un id au membre

        nouveauMembre.setId(genererIdMembre.apply(nouveauMembre));

        // Ajout du membre au registre

        registreMembres.ajouterMembre(nouveauMembre);

        return nouveauMembre;

    }

    @Override
    public Membre trouverMembreParId(String idMembre) {

        return registreMembres.findById(idMembre);

    }

    @Override
    public List<Membre> trouverMembreParNom(String nom) {

        return registreMembres.getMembres().values().stream()
        .filter(membre -> membre.getNom().equalsIgnoreCase(nom))
        .toList();

    }

    @Override
    public List<Membre> toutLesMembres() {

        return registreMembres.getMembres().values().stream().toList();

    }

    @Override
    @SneakyThrows
    public Membre modifierNom(String idMembre, String nouveauNom) {

        Membre modifierNom = trouverMembreParId(idMembre);

        // Changer le nom

        modifierNom.setNom(nouveauNom);

        return modifierNom;

    }

    @Override
    @SneakyThrows
    public Membre modifierPrenom(String idMembre, String nouveauPrenom) {

        Membre  modifierPrenom = trouverMembreParId(idMembre);

        // Changer le prenom

        modifierPrenom.setPrenom(nouveauPrenom);

        return modifierPrenom;

    }

    @Override
    @SneakyThrows
    public Membre modifierEmail(String idMembre, String nouveauEmail) {

        Membre modifierEmail = trouverMembreParId(idMembre);

        // Changer son email

        modifierEmail.setEmail(nouveauEmail);

        return modifierEmail;

    }

    @Override
    @SneakyThrows
    public Membre modiferNumeroTelephone(String idMembre, String nouveauNumero) {

        Membre modifierNumeroTelephone = trouverMembreParId(idMembre);

        // Modifier le numero de telephone

        modifierNumeroTelephone.setNumeroDeTelephone(nouveauNumero);

        return modifierNumeroTelephone;

    }

    @Override
    @SneakyThrows
    public Membre changerStatutMembre(String idMembre, StatutMembre nouveauStatut) {

        Membre changerStatut = trouverMembreParId(idMembre);

        // Verifier si le membre est suspendu

        if (changerStatut.estSuspendu()) throw new MembreSuspenduException();

        // Changer le statut

        changerStatut.setStatut(nouveauStatut);

        return changerStatut;

    }

    @Override
    public void verifierEligibiliteEmprunt(String idMembre) {

        Membre verifierEligibilite = trouverMembreParId(idMembre);

        // S'assurer que le membre n'est pas suspendu

        if (verifierEligibilite.estSuspendu()) throw new MembreSuspenduException();

        // S'assurer que le membre a moins de 5 livres en cours emprunt

        if ((verifierEligibilite.getNombreEmpruntsEnCours() >= 5)) throw new QuotaEmpruntAtteintException();

    }

    @Override
    @SneakyThrows
    public List<Emprunt> consulterHistoriqueMembre(String idMembre) {

        Membre consulter = trouverMembreParId(idMembre);

        // Renvoyer la liste d'emprunts

        return consulter.getHistoriqueEmprunts().values().stream().toList();

    }

    @Override
    @SneakyThrows
    public List<Livre> suggererLivreMembre(String idMembre) {

        // Liste des livres suggerer

        List<Livre> livresSuggerer = new ArrayList<>();

        livresSuggerer.addAll(suggestionLivreCategorie(idMembre));
        livresSuggerer.addAll(suggestionLivreMotTitre(idMembre));
        livresSuggerer.addAll(suggestionLivreAuteur(idMembre));

        return livresSuggerer;

    }

    // Méthodes de matching - suggestion

    @SneakyThrows
    List<Livre> suggestionLivreCategorie(String idMembre) {

        // Membre

        Membre suggestion = trouverMembreParId(idMembre);

        // Regrouper les categories de livre emprunté par le membre par nombre d'emprunts

        Map<CategorieLivre, Long> classementCategorie = suggestion.getHistoriqueEmprunts().values().stream()
        .map(Emprunt::getLivre)
        .map(Livre::getCategorie)
        .collect(Collectors.groupingBy(
                categorie -> categorie,
                Collectors.counting()
        ));

        // Recuperer la categorie qui a le plus d'occurences

        CategorieLivre categoriePrefere = classementCategorie.entrySet().stream()
        .reduce((cle_valeurA, cle_valeurB) -> cle_valeurA.getValue() > cle_valeurB.getValue() ? cle_valeurA : cle_valeurB)
        .map(Map.Entry::getKey)
        .orElse(null);

        // 3 livres de la meme categorie qui n'est pas un livre déja emprunté

        return registreLivres.getLivres().values().stream()
        .filter(livre -> livre.getCategorie() == categoriePrefere && !suggestion.getHistoriqueEmprunts().values().stream().map(Emprunt::getLivre).toList().contains(livre) )
        .limit(3)
        .toList();

    }

    @SneakyThrows
    List<Livre> suggestionLivreMotTitre(String idMembre) {

        // Recuperer le titre des livres emprunté par le membre

        List<String> livresTitre = trouverMembreParId(idMembre).getHistoriqueEmprunts().values().stream()
        .map(Emprunt::getLivre)
        .map(Livre::getTitre)
        .toList();

        // Parcourir chaque titre de livre et decouper a chaque espace puis filtrer les mots d'une certaine longueur

        List<String> longMotsTitre = livresTitre.stream()
        .map(titre -> titre.split(" +"))
        .flatMap(Arrays::stream)
        .filter(mot -> mot.length() >= 4)
        .distinct()
        .toList();

        // Recuperer 3 livres dont le titre contient un long mot d'un livre que le membre a déja emprunté et qui n'est pas un livre que le membre a déja emprunté

        return registreLivres.getLivres().values().stream()
        .filter(livre -> (

                // Pour chaque titre de livre emprunté recupère que le titre des livres qui contiennent un long mot d'un livre déja emprunté

                longMotsTitre.stream().anyMatch(

                mot -> livre.getTitre().toLowerCase().contains(mot.toLowerCase())

                )

                &&

                // En eliminant les titres de livres déja emprunté qui contient un titre d'un livre déja emprunté

                !livresTitre.contains(livre.getTitre())

                )

                )
        .limit(3)
        .toList();

    }

    @SneakyThrows
    List<Livre> suggestionLivreAuteur(String idMembre) {

        // Membre

        Membre suggestion = trouverMembreParId(idMembre);

        // Recuperer la liste des auteurs dont un livre a été déja emprunté par le membre

        List<Auteur> auteursLivre = suggestion.getHistoriqueEmprunts().values().stream()
        .map(Emprunt::getLivre)
        .map(Livre::getAuteur)
        .toList();

        // Recuperer 3 livre de chaque auteur dont le membre a déja emprunté un livre qui n'est parmis la liste des livres qu'il a déja emprunté

        return registreLivres.getLivres().values().stream()
        .filter(livre ->

                // Recupère les livres dont l'auteur est parmis les auteurs d'un livre déja emprunté par le membre

                auteursLivre.contains(livre.getAuteur())



                &&

                // Verifie que le livre ne fait pas partie des livres déja emprunté

                !suggestion.getHistoriqueEmprunts().values().stream().map(Emprunt::getLivre).toList().contains(livre)

        )
        .limit(3)
        .toList();

    }

}
