package service;

import java.io.FileReader;

public interface ImportExportService {

    // Méthodes a implementer

    void exporterLivres();

    void exporterMembres();

    void exporterEmprunts();

    void exporterReservations();

    void exporterAuteurs();

    void exporterMaisonEdition();

    void exporterStatistiquesGlobal();

    void exporterStatistiquesMensuelles();

    void toutExporter();

    void importerMaisonEdition(FileReader chemin);

    void importerAuteurs(FileReader chemin);

    void importerLivres(FileReader chemin);

    void importerMembres(FileReader chemin);

}
