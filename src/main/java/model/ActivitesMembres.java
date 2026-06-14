package model;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

public record ActivitesMembres(Periode periode, long totalMembresActifs, long nouveauMembres, long empruntsEffectues, double moyenne, long retours, long renouvellements, long reservationsActives, long reservationAnnule, double penaliteGenerees, Map<Membre, Long> top5MembresLesPlusActifs, double variationEmprunts, double variationRetards, List<DayOfWeek> jourPicActivites, List<Membre> membresInactif) {}
