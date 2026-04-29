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

## Voraussetzungen
- Java 17
- Maven 4.0.0
- Docker (PostgreSQL, RabbitMQ und pgAdmin)
- IntelliJ IDEA / VS Code / VCCodium
## Installation & Start
### 1. Infrastruktur starten
Startet PostgreSQL & RabbitMQ
````markdown
docker-compose up -d
````
### 2. Komponenten bauen
````markdown
mvn clean install
````
### 3. Konfiguration

````markdown
# Standard: Wien (48.2082, 16.3738) 
weather.api.url=https://api.open-meteo.com/v1/forecast?latitude=48.2082&longitude=16.3738&current=cloud_cover`
````
### 4. Dienste starten
````markdown
cd community-producer 
java -jar target/community-producer.jar

cd community-user 
java -jar target/community-user.jar

cd energy-api 
java -jar target/energy-api.jar

cd energy-gui 
java -jar target/energy-gui.jar

cd percentage-service 
java -jar target/percentage-service.jar

cd usage-service 
java -jar target/usage-service.jar
````
### Projekt Struktur
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

