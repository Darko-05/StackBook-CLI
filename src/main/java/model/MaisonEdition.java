package model;

import java.time.LocalDate;

public record MaisonEdition(String nom, String codePostal, String numeroDeTelephone, String ville, LocalDate dateDeCreation) {}