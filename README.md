# PocketSectors
Sektory na MCBE napisane przez [ProjectCode](https://projectcode.pl). Do ich odpalenia wymagany jest [NukkitX](https://github.com/CloudburstMC/Nukkit) oraz [WaterdogPE](https://github.com/WaterdogPE/WaterdogPE).

## Konfiguracja
Konfiguracja sektorów odbywa się w plugnie Waterdoga, a config wygląda następująco:
```yaml
sectors:
  spawn01:
    x1: -250
    z1: -250
    x2: 250
    z2: 250
    type: "SPAWN"
    world: world
    address: "localhost:19132"
  spawn02:
    x1: -250
    z1: -250
    x2: 250
    z2: 250
    type: "SPAWN"
    world: world
    address: "localhost:19132"
  north:
    x1: 251
    z1: -250
    x2: 751
    z2: 250
    type: "SECTOR"
    world: world
    address: "localhost:19132"
  south:
    x1: -251
    z1: -250
    x2: -751
    z2: 250
    type: "SECTOR"
    world: world
    address: "localhost:19132"
  east:
    x1: 250
    z1: 251
    x2: -250
    z2: 751
    type: "SECTOR"
    world: world
    address: "localhost:19132"
  west:
    x1: 250
    z1: -251
    x2: -250
    z2: -751
    type: "SECTOR"
    world: world
    address: "localhost:19132"
```
W przyszłości MOŻE (zależy od chęci) dodam możliwość zmiany w configu takich rzeczy jak wiadomości, odległości od róźnych rzeczy itp. Aktualnie znajduje się to w interfejsie ```pl.projectcode.pocketsectors.nukkit.util.Configuration```, gdyż zostało to zrobione na łatwiznę.

## Komunikacja
Komunikacja między sektorami odbywa się za pomocą Pub/Sub messaging w Redisie. Klientem redisa używanym przez sektory jest [Redisson](https://github.com/redisson/redisson). Poniżej znajduje się prosty opis tego, jak stworzyć swój własny pakiet, wysłać go i odebrać.
**UWAGA: Każdy obiekt wysyłany w pakiecie (łącznie z pakietem) musi implementować interfejs Serializable!**
### Tworzenie własnego pakietu
Do stworzenia własnego pakietu musimy użyć klasy ```Packet```. Oto przykładowy kod:
```java
@Getter
@AllArgsConstructor
public class YourPacket extends Packet {

    private final String message;
    private final int number;
}
```
### Wysyłanie pakietu
Aby pakiet został wysłany, musimy podać nazwę kanału publikacji pakietu. Dostępne są trzy gotowe kanały (można użyć też swoich własnych):
```java
PacketChannel.PROXY; // pakiet zostanie wysłany do proxy
PacketChannel.SECTORS; // pakiet zostanie wysłany do wszystkich sektorów
PacketChannel.GLOBAL; // pakiet zostanie wysłany do proxy i wszystkich sektorów
```

Wysyłanie pakietu:
```java
YourPacket packet = new YourPacket("wiadomosc!", 10);
RedisManager.getInstance().publish(PacketChannel.GLOBAL, packet); // pakiet ExamplePacket zostanie wysłany do proxy i wszystkich sektorów
```
### Odbieranie pakietu
Aby odebrać pakiet, musimy stworzyć nasłuchiwacz (eng. ```listener```). Tworzy się go następująco:
```java
public class YourPacketListener extends RedisPacketListener<YourPacket> {

    public YourPacketListener() {
        super(YourPacket.class);
    }

    @Override
    public void handle(YourPacket packet) {
        System.out.println("Otrzymano YourPacket!");
        System.out.println(packet.getMessage()); // wiadomosc!
        System.out.println(packet.getNumber()); // 10
    }
}
```
Teraz, gdy stworzyliśmy swój nasłuchiwacz, musimy go jakoś zarejestrować. Do tego przyda nam się klasa ```RedisManager```. Oto przykładowy kod:
```java
RedisManager.getInstance().subscribe(new YourPacketListener()); // pierwszy sposób
RedisManager.getInstance().subscribe("channel", new YourPacketListener()); // drugi sposób
```
Jak widać są dwa sposoby na zarejestrowanie nasłuchiwacza pakietów. Jeżeli nie podamy kanału, na jakim nasz nasłuchiwacz ma działać, domyślnie zostanie wybrany kanał o nazwie sendera pakietów. Na ten moment są trzy senderzy pakietów (nazwaSektora, PROXY, GLOBAL).

## Zdarzenia
Aktualnie sektory posiadają dwa zdarzenia (eng. ```events```). Są to następująco:
```
SectorChangeEvnet - Wykonuje się przed zmianą sektora przez gracza, można anulować,
SectorPlayerFirstJoinEvent - Wykonuje się tylko wtedy, gdy gracz pierwszy raz wejdzie na serwer. Działa podobnie jak PlayerJoinEvent, ale nie wykonuje się z każdą zmianą sektora.
```

## Inne przydatne metody
Wysyłanie wiadomości do graczy:
```java
MessageUtil.sendBroadcastMessage("globalna wiadomosc!"); // wysyła wiadomość do wszystkich graczy
MessageUtil.sendBroadcastPermissionMessage("permisja.do.wiadomosci", "globalna wiadomosc z permisja!"); // wysyła wiadomość do wszystkich graczy, którzy posiadają podaną przez nas permisję
MessageUtil.sendMessageToPlayer("nickGracza", "wiadomosc do gracza!"); // wysyła wiadomość do określonego przez nas gracza
MessageUtil.sendBroadcastTitle("title", "subtitle"); // wysyła title do wszystkich graczy
```
Informacje o graczach:
```java
SectorManager sectorManager = NukkitSector.getInstance().getSectorManager();

sectorManager.getOnlinePlayers(); // zwraca listę ze wszystkimi nickami graczy online
sectorManager.isPlayerOnline("nickGracza"); // sprawdza, czy podany przez nas gracz jest online
```
Informacje o konkretnym graczu:
```java
CompletableFuture<PlayerInfo> playerInfoFuture = PlayerInfoUtil.getPlayerInfo("nickGracza"); // zwraca NULL jeżeli gracz jest offline

if(playerInfoFuture == null) {
    return; // gracz jest offline
}

playerInfoFuture.thenAccept(playerInfo -> {
    playerInfo.getName(); // nick gracza
    playerInfo.getSectorName(); // aktualny sektor gracza
    playerInfo.getPosition(); // aktualna pozycja gracza
    playerInfo.getInventory(); // zserializowane inventory gracza
    // itp.
});
```
## Kontakt
W razie jakichkolwiek pytań, błedów, problemów z odpaleniem proszę o kontakt na Discordzie: ```xStrixU#0001```
## License
MIT License, zobacz [LICENSE](LICENSE)
