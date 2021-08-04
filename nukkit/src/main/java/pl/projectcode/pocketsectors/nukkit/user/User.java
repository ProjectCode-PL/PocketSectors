package pl.projectcode.pocketsectors.nukkit.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
public class User {

    private final String name;

    private double lastSectorChange;
    @Setter
    private boolean firstJoin = false;

    public double getLastSectorChange() {
        return System.currentTimeMillis() - lastSectorChange;
    }

    public void setLastSectorChange() {
        lastSectorChange = System.currentTimeMillis();
    }
}