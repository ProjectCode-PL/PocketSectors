package pl.projectcode.pocketsectors.waterdog.manager;

import lombok.Getter;
import pl.projectcode.pocketsectors.common.sector.SectorData;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SectorManager {

    private final List<SectorData> sectorsData = new ArrayList<>();

    public void addSectorData(SectorData sectorData) {
        this.sectorsData.add(sectorData);
    }

    public SectorData getSectorData(String sectorName) {
        for(SectorData sectorData : this.sectorsData) {
            if(sectorData.getName().equalsIgnoreCase(sectorName)) {
                return sectorData;
            }
        }

        return null;
    }
}
