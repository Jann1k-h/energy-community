package at.technikum.gui;

// DTO für die Antwort von GET /energy/historical.
public record HistoricalUsageDto(
        double communityProduced,
        double communityUsed,
        double gridUsed
) {
}