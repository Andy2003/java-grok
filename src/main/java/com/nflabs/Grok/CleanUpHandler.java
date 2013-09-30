package com.nflabs.Grok;

import java.util.*;


@SuppressWarnings("UnusedDeclaration")
public class CleanUpHandler {

    private Set<String> remove;
    private Map<String, String> rename;

    /**
     * Set a map of matched field to re name
     *
     * @param key   name
     * @param value name
     * @see #rename(java.util.Map)
     */
    public void addToRename(String key, String value) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("key is null or empty");
        }
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("value is null or empty");
        }
        if (rename == null) {
            rename = new HashMap<String, String>();
        }
        rename.put(key, value);
    }

    /**
     * Set a field list to remove from the final matched map
     *
     * @param item to remove
     * @see #remove(java.util.Map)
     */
    public void addToRemove(String item) {
        if (item == null || item.isEmpty()) {
            throw new IllegalArgumentException("item is null or empty");
        }
        if (remove == null) {
            remove = new HashSet<String>();
        }
        remove.add(item);
    }

    /**
     * @param fields the list of strings, to remove from the final match
     * @see #addToRemove(String)
     */
    public void addToRemove(Collection<String> fields) {
        if (fields == null) {
            throw new IllegalArgumentException("fields are null");
        }
        if (remove == null) {
            remove = new HashSet<String>();
        }
        remove.addAll(fields);
    }

    /**
     * Remove from the data the unwilling items
     *
     * @param data to clean
     */
    public void remove(Map<String, String> data) {
        if (remove == null || remove.isEmpty()) {
            return;
        }
        if (data == null || data.isEmpty()) {
            return;
        }
        data.keySet().removeAll(remove);
    }

    /**
     * @param data the Map
     * @see #addToRename(String, Object)
     */
    public void rename(Map<String, String> data) {
        if (rename == null || rename.isEmpty()) {
            return;
        }
        if (data == null || data.isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> r : rename.entrySet()) {
            if (data.containsKey(r.getKey())) {
                data.put(r.getValue(), data.remove(r.getKey()));
            }
        }
    }

}
