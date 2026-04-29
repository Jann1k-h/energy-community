RabbitMq läuft aktuell lokal, auf Docker evtl umstellen

1. Docker starten

2. CommunityProducerApplication starten
   → erstellt exchange, queue und binding
   → sendet PRODUCER-Nachrichten

3. UsageServiceApplication starten
   → konsumiert Nachrichten aus energy-queue
   → schreibt in energy_usage

4. CurrentPercentageApplication starten
   → liest energy_usage
   → schreibt current_percentage

5. CommunityUserApplication starten
   → sendet USER-Nachrichten