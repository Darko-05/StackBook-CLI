package service;

import model.*;

import java.time.LocalDate;

public interface IReportService {

    // Méthodes a implementer

    StatistiquesGlobal genererStatistiquesGlobal(long stockInitial);

    StatistiquesMensuelles genererStatistiquesMensuelles(LocalDate dateLancement);

    RapportPopularite genererRapportPopularite();

    ActivitesMembres genererRapportActivitesMembres(LocalDate dateLancement);

}
