package model;

import java.util.Map;

public record RapportPopularite(TopLivresPlusEmpruntes topLivresPlusEmprunte, Map<CategorieLivre, Double> analyseParCategorie, Map<Livre, Long> livresSousUtilises, Map<CategorieLivre, Double> tendancesEmergentes) {}
