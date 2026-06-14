package model;

import java.time.LocalDate;

public record EvolutionMensuelle(LocalDate dateAFormater, long nombreEmprunts, long nombreReservations) {}
