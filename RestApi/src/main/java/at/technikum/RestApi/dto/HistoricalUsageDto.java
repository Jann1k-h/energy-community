package at.technikum.RestApi.dto;

// DTO = Data Transfer Object.
// Diese Klasse beschreibt exakt, welche Daten der historische REST-Endpunkt zurückgibt.
public record HistoricalUsageDto(
        double communityProduced,
        double communityUsed,
        double gridUsed
) {
}