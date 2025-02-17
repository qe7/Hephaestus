package io.github.qe7.managers.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.qe7.managers.api.Manager;
import io.github.qe7.relations.Relation;
import io.github.qe7.relations.enums.RelationType;
import io.github.qe7.utils.config.FileUtil;

public final class RelationManager extends Manager<Relation, String> {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public void initialise() {
        this.loadRelations();

        System.out.println("RelationManager initialised!");
    }

    public void register(final Relation relation) {
        this.getRegistry().put(relation, relation.getName());
    }

    public boolean isFriend(final String name) {
        for (final Relation relation : this.getRegistry().keySet()) {
            if (relation.getName().equalsIgnoreCase(name) && relation.getType() == RelationType.FRIEND) {
                return true;
            }
        }
        return false;
    }

    public boolean isEnemy(final String name) {
        for (final Relation relation : this.getRegistry().keySet()) {
            if (relation.getName().equalsIgnoreCase(name) && relation.getType() == RelationType.ENEMY) {
                return true;
            }
        }
        return false;
    }

    public void addRelation(final String name, final RelationType type) {
        final Relation relation = new Relation(name, name, type);

        this.register(relation);

        this.saveRelations();
    }

    public void removeRelation(final String name) {
        this.getRegistry().entrySet().removeIf(entry -> entry.getKey().getName().equalsIgnoreCase(name));

        this.saveRelations();
    }

    public void saveRelations() {
        final JsonObject object = new JsonObject();

        this.getRegistry().forEach((relation, name) -> object.add(name, relation.serialize()));

        FileUtil.writeFile("relations", GSON.toJson(object));
    }

    public void loadRelations() {
        final String config = FileUtil.readFile("relations");

        if (config == null) {
            return;
        }

        final JsonObject object = GSON.fromJson(config, JsonObject.class);

        object.entrySet().forEach(entry -> {
            final Relation relation = new Relation(entry.getKey(), entry.getKey(), RelationType.valueOf(entry.getValue().getAsJsonObject().get("type").getAsString()));

            this.getRegistry().put(relation, entry.getKey());
        });
    }
}
