# IntelliJ Setup
### **Schritt 1: Projekt in IntelliJ IDEA öffnen**
1. **IntelliJ IDEA starten**.
2. Auf **"Open"** klicken und das Verzeichnis auswählen, in dem das **`energy-community`**-Projekt heruntergeladen wurde (z. B. den Ordner, der aus Git geklont wurde).
3. IntelliJ wird automatisch die `pom.xml` erkennen und das Projekt als Maven-Projekt importieren.

### **Schritt 2: Maven-Projekt synchronisieren**
Sobald das Projekt geöffnet ist, sorgt IntelliJ für die automatische Synchronisation der Abhängigkeiten:
  1. IntelliJ wird in der rechten oberen Ecke eine **Maven-Tool-Box** anzeigen, wenn das Projekt erfolgreich als Maven-Projekt erkannt wurde.
  2. Auf das **„Refresh“-Symbol** (grüner Kreis mit Pfeil) in der Maven-Tool-Box klicken, um sicherzustellen, dass alle Abhängigkeiten korrekt heruntergeladen und synchronisiert werden.

### **Schritt 3: Projekt bauen und ausführen**
  1. **Projekt bauen**:
    - Zum Menü **Build** > **Build Project** gehen, um das Projekt zu kompilieren.
    - IntelliJ wird automatisch alle Abhängigkeiten gemäß der `pom.xml` herunterladen und den Build ausführen.
  2. **Projekt ausführen**:
    - Zur Datei `CommunityProducerApplication.java` oder der Hauptklasse deines Projekts gehen.
    - Auf das grüne **„Run“-Symbol** in der oberen rechten Ecke klicken, um das Projekt auszuführen.
    - Wenn das Spring Boot verwendet wird, wird der Server automatisch gestartet.

### **Schritt 4: Fehlerbehebung und Logs**
- Wenn beim Starten oder Bauen Fehler auftreten, kann die Konsole am unteren Bildschirmrand von IntelliJ verwendet werden, um Fehlerprotokolle zu sehen und Probleme zu beheben.
- IntelliJ gibt auch hilfreiche Hinweise und Vorschläge, wie Fehler behoben werden können und bei Bedarf kann die „Probleme“-Ansicht im Projektbaum verwendet werden.
