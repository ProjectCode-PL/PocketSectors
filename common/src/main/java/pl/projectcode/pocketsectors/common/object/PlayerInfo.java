package pl.projectcode.pocketsectors.common.object;

import lombok.Getter;
import lombok.Setter;
import pl.projectcode.pocketsectors.common.util.Vector3;

import java.io.Serializable;

@Getter
@Setter
public class PlayerInfo implements Serializable {

    private String playerName;
    private String sectorName;
    private Vector3 position;
    private double yaw;
    private double pitch;
    private String inventory;
    private String armorInventory;
    private String enderChest;
    private int heldItemIndex;
    private int gameMode;
    private String potionEffects;
    private float health;
    private int foodLevel;
    private int experience;
    private int experienceLevel;
    private int fireTicks;
    private boolean allowFlight;
    private boolean flying;
}
