package com.dvorenenko.config;

import com.dvorenenko.entity.Entity;
import com.dvorenenko.entity.enums.EntityType;

import java.lang.reflect.InvocationTargetException;

public class MakeClassByReflection {

    public Entity MakeClassByEntity(Entity entity) {
        return getEntityByEntity(entity);
    }

    public Entity MakeClassByEntityType(EntityType entityType) {
        return getEntityByEntityType(entityType);
    }

    private Entity getEntityByEntityType(EntityType entityType) {
        Entity entityByEntityType;
        try {
            entityByEntityType = (Entity) entityType.getClazz().getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return entityByEntityType;
    }

    private Entity getEntityByEntity(Entity entity) {
        Entity entityByEntity;
        try {
            entityByEntity = entity.getClass().getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return entityByEntity;
    }
}
