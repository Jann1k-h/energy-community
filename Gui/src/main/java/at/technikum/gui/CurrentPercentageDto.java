package at.technikum.gui;

// DTO für die Antwort von GET /energy/current.
public record CurrentPercentageDto(
        double communityDepleted,
        double gridPortion
) {
}