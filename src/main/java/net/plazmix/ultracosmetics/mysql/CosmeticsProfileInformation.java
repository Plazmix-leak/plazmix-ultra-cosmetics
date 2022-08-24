package net.plazmix.ultracosmetics.mysql;

import net.plazmix.ultracosmetics.cosmetics.Category;

import java.util.HashMap;
import java.util.HashSet;

public class CosmeticsProfileInformation{
    public HashMap<String, HashSet<String>> available = new HashMap<>();
    public HashMap<String, String> selected = new HashMap<>();


    public void addAvailable(Category category, String signature) {
        if (available.containsKey(category.name())) {
            available.get(category.name()).add(signature);
            return;
        }

        HashSet<String> newSet = new HashSet<>();
        newSet.add(signature);
        available.put(category.name(), newSet);
    }

    public void removeAvailable(Category category, String signature) {
        if (!available.containsKey(category.name())) {
            return;
        }
        available.get(category.name()).remove(signature);
    }

    public void setSelected(Category category, String signature) {
        selected.put(category.name(), signature);
    }

    public void removeSelected(Category category){
        selected.remove(category.name());
    }





}
