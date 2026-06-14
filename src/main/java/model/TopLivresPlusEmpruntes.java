package model;

import java.util.List;
import java.util.Map;

public record TopLivresPlusEmpruntes( Map<Livre, Long> nombreEmpruntsParLivre, List<Double> taux) {}
