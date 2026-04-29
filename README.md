# Energy-community
Ein verteiltes System zur Simulation von Energieproduktion und -verbrauch in einer Gemeinschaft. Das System nutzt eine Message Queue zur Entkopplung der Komponenten, eine Datenbank zur Speicherung von Stundendaten und eine REST-API mit JavaFX-GUI zur Visualisierung.

## Architektur
Das System besteht aus 6 unabhängigen Komponenten:
1. **Community Producer**: Simuliert Solarproduktion basierend auf Wetterdaten.
2. **Community User**: Simuliert Verbrauch basierend auf Tageszeit.
3. **Usage Service**: Verarbeitet Nachrichten, aggregiert Daten auf Stundenbasis und berechnet Grid-Nutzung.
4. **Percentage Service**: Berechnet aktuelle Depletion- und Grid-Anteile.
5. **Energy API**: Spring Boot REST API (Endpunkte: `/energy/current`, `/energy/historical`).
6. **Energy GUI**: JavaFX Client zur Visualisierung.
```
├── docker-compose.yml
├── Dockerfile
├── community-producer
├── community-user
├── energy-api
├── energy-gui
├── percentage-service
├── usage-service
```

## Voraussetzungen
- Java 17
- Maven 4.0.0
- Docker (PostgreSQL, RabbitMQ und pgAdmin)
- IntelliJ IDEA, VS Code oder VSCodium als IDE

## Installation & Start
### 1. Infrastruktur starten
Startet PostgreSQL, RabbitMQ und pgAdmin
````markdown
docker-compose up -d
````
### 2. Komponenten bauen
Alle Komponenten mit Maven bauen:
````markdown
mvn clean install
````
### 3. Konfiguration
Überprüfen, ob die Wetter-API-URL korrekt konfiguriert ist (Standard für Wien):
````markdown
# Standard: Wien (48.2082, 16.3738) 
weather.api.url=https://api.open-meteo.com/v1/forecast?latitude=48.2082&longitude=16.3738&current=cloud_cover
````
### 4. Dienste starten
1. Überprüfen, ob Docker läuft
2. **CommunityProducerApplication** starten (erstellt exchange, queue und binding → sendet PRODUCER-Nachrichten)
3. **UsageServiceApplication** starten (konsumiert Nachrichten aus energy-queue und schreibt in energy_usage)
4. **CurrentPercentageApplication** starten (liest energy_usage und schreibt current_percentage)
5. **CommunityUserApplication** starten (sendet USER-Nachrichten)

## Verfügbare REST-API-Endpunkte
#### 1. Aktuelle Energiedaten - `/energy/current`
Dieser Endpunkt gibt die **aktuellen Energiedaten** zurück. Der Endpunkt wird verwendet, um die aktuellen Werte zu sehen.

- **URL**: [http://localhost:8083/energy/current](http://localhost:8083/energy/current)
- **Methode**: `GET`
- **Beispielantwort**:
  ```json
  {
    "currentEnergy": "100 kW"
  }
#### 2. Historische Energiedaten - `/energy/historical`
Dieser Endpunkt gibt die **historischen Energiedaten** für einen bestimmten Zeitraum zurück. Das Start- und Enddatum muss im **ISO 8601**-Format angeben werden.
- **URL**: [http://localhost:8083/energy/historical?start=2026-04-29T18:00:00&end=2026-04-29T23:00:00](http://localhost:8083/energy/historical?start=2026-04-29T18:00:00&end=2026-04-29T23:00:00)
- **Methode**: `GET`
- **Abfrageparameter**:
  - `start`: Startzeit im ISO 8601-Format (z. B. `2026-04-29T18:00:00`).
  - `end`: Endzeit im ISO 8601-Format (z. B. `2026-04-29T23:00:00`).
- **Beispielantwort**:
  ```json
  {
    "historicalEnergy": [
      { "timestamp": "2026-04-29T18:00:00", "energy": "90 kW" },
      { "timestamp": "2026-04-29T19:00:00", "energy": "95 kW" },
      { "timestamp": "2026-04-29T20:00:00", "energy": "100 kW" }
    ]
  }
