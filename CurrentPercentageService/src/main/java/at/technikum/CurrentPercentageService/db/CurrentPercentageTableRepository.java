package at.technikum.CurrentPercentageService.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

// Ohne interface müsste man DB Verbindungen selbst herstellen und sql befehle selber schreiben
// Spring Data JPA liefert funktionen wie findById, save, delete, ...
public interface CurrentPercentageTableRepository extends JpaRepository<CurrentPercentageTable, LocalDateTime> {
}