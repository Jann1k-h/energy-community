package at.technikum.RestApi;
import at.technikum.RestApi.db.CurrentPercentageTableRepository;
import at.technikum.RestApi.db.HourlyUsageTable;
import at.technikum.RestApi.db.HourlyUsageTableRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import at.technikum.RestApi.dto.HistoricalUsageDto;
import at.technikum.RestApi.dto.CurrentPercentageDto;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import java.util.List;


@RestController
public class RestApiController {

    // Feld für Objekt CurrentPercentageTableRepository deklarieren,
    // ohne dem kann REST API nicht auf hourlyUsageTableRepository zugreifen und befehle wie .findById .save,... ausführen
    private final CurrentPercentageTableRepository currentPercentageTableRepository;

    // Feld für Objekt HourlyUsageTableRepository deklarieren,
    // ohne dem kann REST API nicht auf hourlyUsageTableRepository zugreifen und befehle wie .findById .save,... ausführen
    private final HourlyUsageTableRepository hourlyUsageTableRepository;

    // --------------------------------------------------
    // Konstruktor-Injection
    // CurrentPercantageService braucht currentPercentageTableRepository / HourlyUsageTableRepository zum Zugriff auf die Datenbank
    // Spring Boot erstellt die Repository-Implementierung automatisch.
    // Spring Boot übergibt sie in den Konstruktor.
    // Die Klasse speichert sie in ihrer Variable.
    public RestApiController(HourlyUsageTableRepository hourlyUsageTableRepository, CurrentPercentageTableRepository currentPercentageTableRepository) {
        this.hourlyUsageTableRepository = hourlyUsageTableRepository;
        this.currentPercentageTableRepository = currentPercentageTableRepository;
    }
    // --------------------------------------------------


    @GetMapping("/energy/current")
// Endpoint gibt die Prozentwerte der aktuellen Stunde zurück.
    public CurrentPercentageDto getCurrentPercentageTable() {

        // Aktuelle Zeit holen und auf volle Stunde runden.
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);

        // Eintrag aus current_percentage_table mit aktueller Stunde suchen.
        // Wenn kein Eintrag existiert, wird eine verständliche Exception geworfen.
        return currentPercentageTableRepository.findById(now)
                .map(currentPercentageTable -> new CurrentPercentageDto(
                        currentPercentageTable.getHour(),
                        currentPercentageTable.getCommunityDepleted(),
                        currentPercentageTable.getGridPortion()
                ))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Für die aktuelle Stunde sind noch keine Energiedaten vorhanden."
                ));
    }

    @GetMapping("/energy/historical")
    // http://localhost:8080/energy/historical?start=2026-06-22T11:00:00&end=2026-06-24T11:00:00
    // Da aus mehrere json outputs 1 summierter JSON-Output entstehen soll, muss man irgendwie JSON zurückgeben
    // am besten mit Mapping
    // Mapping besteht aus 2 werten: Schlüssel, Wert
    public HistoricalUsageDto getHistoricalUsage(
            // Startzeit wird aus URL-Parameter start gelesen
            @RequestParam LocalDateTime start,

            // Endzeit wird aus URL-Parameter end gelesen
            @RequestParam LocalDateTime end
    ) {
        // Der Startzeitpunkt darf nicht nach dem Endzeitpunkt liegen.
        if (start.isAfter(end)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Der Startzeitpunkt darf nicht nach dem Endzeitpunkt liegen."
            );
        }
        // Alle Einträge aus hourly_usage_table zwischen start und end aus DB holen
        List<HourlyUsageTable> hourlyUsageTableList = hourlyUsageTableRepository.findByHourBetween(start, end);

        // Variablen für summierte Werte erstellen
        double totalCommunityProduced = 0;
        double totalCommunityUsed = 0;
        double totalGridUsed = 0;

        // Jeden gefundenen Stunden-Eintrag durchgehen
        for (HourlyUsageTable hourlyUsageTable : hourlyUsageTableList) {

            // communityProduced zur Gesamtsumme addieren
            totalCommunityProduced += hourlyUsageTable.getCommunityProduced();

            // communityUsed zur Gesamtsumme addieren
            totalCommunityUsed += hourlyUsageTable.getCommunityUsed();

            // gridUsed zur Gesamtsumme addieren
            totalGridUsed += hourlyUsageTable.getGridUsed();
        }

        // DTO erstellen und zurückgeben.
// Spring Boot wandelt das DTO automatisch in JSON um.
        return new HistoricalUsageDto(
                totalCommunityProduced,
                totalCommunityUsed,
                totalGridUsed
        );

    }

}