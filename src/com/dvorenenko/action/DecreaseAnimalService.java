package com.dvorenenko.action;

import com.dvorenenko.config.FieldSizeConfig;
import com.dvorenenko.entity.Entity;
import com.dvorenenko.location.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecreaseAnimalService {

    public Location decreaseAnimalOnLocation(Location location) {
        Map<FieldSizeConfig, List<Entity>> island = new HashMap<>();
        for (Map.Entry<FieldSizeConfig, List<Entity>> listEntry : location.getIsland().entrySet()) {
            island.put(listEntry.getKey(), new ArrayList<>());
            for (Entity entity : listEntry.getValue()) {
                if (entity.isAlive()) {
                    island.get(listEntry.getKey()).add(entity);
                }
            }
        }
        return new Location(island);
    }
}
