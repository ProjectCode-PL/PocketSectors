package pl.projectcode.pocketsectors.nukkit.util;

public interface Configuration {

    String BORDER_MESSAGE = "&6Zblizasz sie do granicy sektora &a{SECTOR} {DISTANCE}m";
    int BORDER_MESSAGE_DISTANCE = 10;

    int BREAK_BORDER_DISTANCE = 10;
    String BREAK_BORDER_DISTANCE_MESSAGE = "&cNie mozesz niszczyc blokow przy sektorze!";

    int PLACE_BORDER_DISTANCE = 10;
    String PLACE_BORDER_DISTANCE_MESSAGE = "&cNie mozesz stawiac blokow przy sektorze!";

    String SECTOR_DISABLED_TITLE = "&cBlad";
    String SECTOR_DISABLED_SUBTITLE = "&7Ten sektor jest aktualnie &cwylaczony";
}
