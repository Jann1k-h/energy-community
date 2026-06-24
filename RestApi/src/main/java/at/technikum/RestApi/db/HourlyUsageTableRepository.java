package at.technikum.RestApi.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

// Ohne interface müsste man DB Verbindungen selbst herstellen und sql befehle selber schreiben
// Spring Data JPA liefert funktionen wie findById, save, delete, ...
public interface HourlyUsageTableRepository extends JpaRepository<HourlyUsageTable, LocalDateTime> {
    List<HourlyUsageTable> findByHourBetween(LocalDateTime hourAfter, LocalDateTime hourBefore);
}