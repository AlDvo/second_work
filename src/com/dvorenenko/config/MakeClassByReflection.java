package com.dvorenenko.config;

import com.dvorenenko.constants.Constants;
import com.dvorenenko.entity.Entity;
import com.dvorenenko.entity.enums.EntityType;

import java.lang.reflect.InvocationTargetException;

public class MakeClassByReflection {

    public Entity makeClassByEntity(Entity entity, double weight, int speed, double mealKg, int maxQtyOnCell) {
        Entity entityByEntity;
        try {
            entityByEntity = entity.getClass().getConstructor(double.class, int.class, double.class, int.class).newInstance(weight, speed, mealKg, maxQtyOnCell);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            System.err.println(Constants.ERROR_MAKE_ENTITY);
            throw new RuntimeException(e);
        }
        return entityByEntity;
    }

    public Entity makeClassByEntity(Entity entity) {
        Entity entityByEntity;
        try {
            entityByEntity = entity.getClass().getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            System.err.println(Constants.ERROR_MAKE_ENTITY);
            throw new RuntimeException(e);
        }
        return entityByEntity;
    }

    public Entity makeClassByEntityType(EntityType entityType, double weight, int speed, double mealKg, int maxQtyOnCell) {
        Entity entityByEntityType;
        try {
            entityByEntityType = (Entity) entityType.getClazz().getConstructor(double.class, int.class, double.class, int.class).newInstance(weight, speed, mealKg, maxQtyOnCell);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            System.err.println(Constants.ERROR_MAKE_ENTITY);
            throw new RuntimeException(e);
        }
        return entityByEntityType;
    }

    public Entity makeClassByEntityType(EntityType entityType) {
        Entity entityByEntityType;
        try {
            entityByEntityType = (Entity) entityType.getClazz().getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            System.err.println(Constants.ERROR_MAKE_ENTITY);
            throw new RuntimeException(e);
        }
        return entityByEntityType;
    }

}
