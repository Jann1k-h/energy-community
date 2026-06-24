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

    private HourlyUsageTableRepository hourlyUsageTableRepository;
    private CurrentPercentageTableRepository currentPercentageTableRepository;

    public RestApiController(HourlyUsageTableRepository hourlyUsageTableRepository, CurrentPercentageTableRepository currentPercentageTableRepository) {
        this.hourlyUsageTableRepository = hourlyUsageTableRepository;
        this.currentPercentageTableRepository = currentPercentageTableRepository;
    }

    @GetMapping("/energy/current")
    // http://localhost:8080/energy/current
    public CurrentPercentageTable getCurrentPercentageTable() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);

        return currentPercentageTableRepository.findById(now).orElse(null);
    }

    @GetMapping("/energy/historical")
    // http://localhost:8080/energy/historical?start=2026-06-22T11:00:00&end=2026-06-24T11:00:00
    // Da aus mehrere json outputs 1 summierter JSON-Output entstehen soll, muss man irgendwie JSON zurückgeben
    // am besten mit Mapping
    // Mapping besteht aus 2 werten: Schlüssel, Wert
    public Map<String, Double> getHistoricalUsage(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end
    ) {
        List<HourlyUsageTable> hourlyUsageTableList = hourlyUsageTableRepository.findByHourBetween(start, end);

        double totalCommunityProduced = 0;
        double totalCommunityUsed = 0;
        double totalGridUsed = 0;

        for (HourlyUsageTable hourlyUsageTable : hourlyUsageTableList) {
            totalCommunityProduced += hourlyUsageTable.getCommunityProduced();
            totalCommunityUsed += hourlyUsageTable.getCommunityUsed();
            totalGridUsed += hourlyUsageTable.getGridUsed();
        }

        Map<String, Double> result = new HashMap<>();

        result.put("communityProduced", totalCommunityProduced);
        result.put("communityUsed", totalCommunityUsed);
        result.put("gridUsed", totalGridUsed);

        return result;
    }

}
