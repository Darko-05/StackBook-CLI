package model;

import java.time.DayOfWeek;
import java.util.Map;

public record StatistiquesMensuelles(long nombreEmpruntsDuMois, long nouveauxMembres, long retours, long reservations, double tauxRotation, double penalitesDuMois, Map<DayOfWeek, Long> tendanceHebdomadaire, Map<CategorieLivre, Long> top5Categories, Map<Membre, Long> top5MembresActifs) {}