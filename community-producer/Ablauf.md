Application startet Anwendung

in config/RabbitMQConfig werden Konfigurationen festgelegt

scheduler wird alle 5 Sekunden aufgerufen, dieser ruft sendEnergyProduction in service/EnergyProducerService auf

in service/EnergyProducerService wird service/WeatherService mit API wetterzugriff aufgerufen und danach kWh berechnet

service/EnergyProducerService erzeugt mithilfe von model/EnergyMessage Nachricht und sendet sie in RabbitMQ queue