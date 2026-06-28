package at.technikum.RestApi;
import at.technikum.RestApi.db.CurrentPercentageTable;
import at.technikum.RestApi.db.CurrentPercentageTableRepository;
import at.technikum.RestApi.db.HourlyUsageTable;
import at.technikum.RestApi.db.HourlyUsageTableRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    // http://localhost:8080/energy/current
    // Endpoint gibt die Prozentwerte der aktuellen Stunde zurück
    public CurrentPercentageTable getCurrentPercentageTable() {

        // Aktuelle Zeit holen und auf volle Stunde runden
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);

        // Eintrag aus current_percentage_table mit aktueller Stunde suchen
        // Wenn Eintrag existiert, wird er zurückgegeben
        // Wenn kein Eintrag existiert, wird null zurückgegeben
        return currentPercentageTableRepository.findById(now).orElse(null);
    }

    @GetMapping("/energy/historical")
    // http://localhost:8080/energy/historical?start=2026-06-22T11:00:00&end=2026-06-24T11:00:00
    // Da aus mehrere json outputs 1 summierter JSON-Output entstehen soll, muss man irgendwie JSON zurückgeben
    // am besten mit Mapping
    // Mapping besteht aus 2 werten: Schlüssel, Wert
    public Map<String, Double> getHistoricalUsage(
            // Startzeit wird aus URL-Parameter start gelesen
            @RequestParam LocalDateTime start,

            // Endzeit wird aus URL-Parameter end gelesen
            @RequestParam LocalDateTime end
    ) {
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

        // Map für JSON-Ergebnis erstellen
        // Schlüssel = Name im JSON
        // Wert = berechnete Gesamtsumme
        Map<String, Double> result = new HashMap<>();

        // Summierte Werte in Map speichern
        result.put("communityProduced", totalCommunityProduced);
        result.put("communityUsed", totalCommunityUsed);
        result.put("gridUsed", totalGridUsed);

        // Map zurückgeben
        // Spring Boot wandelt Map automatisch in JSON um
        return result;
    }

}
