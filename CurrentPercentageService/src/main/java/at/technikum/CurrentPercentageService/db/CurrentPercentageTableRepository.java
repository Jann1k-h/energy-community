package at.technikum.CurrentPercentageService.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

// Repository für den Zugriff auf die Tabelle hourly_usage_table
// Ohne Spring Data JPA müsste man DB-Verbindungen und SQL-Befehle selbst schreiben.
// Durch JpaRepository bekommt man fertige Methoden wie findById, save, delete, findAll, ...
// Im JpaRepository schreibt man Tabelle, mit der Repository arbeitet und Datentyp vom Primary Key, also CurrentPercentageTable und LocalDateTime
public interface CurrentPercentageTableRepository extends JpaRepository<CurrentPercentageTable, LocalDateTime> {
}