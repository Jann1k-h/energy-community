package at.technikum.RestApi.dto;

import java.time.LocalDateTime;

// DTO = Data Transfer Object.
// Diese Klasse beschreibt exakt, welche Daten
// GET /energy/current zurückgibt.
public record CurrentPercentageDto(
        LocalDateTime hour,
        double communityDepleted,
        double gridPortion
) {
}