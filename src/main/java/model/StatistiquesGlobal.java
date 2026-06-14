package model;

import java.util.List;
import java.util.Map;

/* Elements des statistiques globaux */

public record StatistiquesGlobal(long stockTotalDeLivres, long empruntsCumules, double tauxDeRotationGlobal, long membreActifs, long livresDisponibles, long nombreTotalDeRetour, long reservationsActives, double tauxDeReservations, double penalitesTotal, List<EvolutionMensuelle> evolutionMensuellesDes3DerniersMois, Map<Livre, Long> top5LivresDeToutePeriode) {}