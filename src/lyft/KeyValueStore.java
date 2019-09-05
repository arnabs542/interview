package lyft;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Every time we add a new key or change the value of that key, we increment the version.
 * Return the value of a specific key in a specific version.
 *
 * PUT <key> <value>
 * Set the key name to the value. Key strings will not contain spaces. Print out the version number,
 * the key and the value as PUT(#<version number>) <key> = <value>. The first write in the file
 * should be version number 1, the second should be version number 2, etc.
 *
 * GET <key>
 * Print out the key and the last value of the key, or <NULL> if that key has never been set as in:
 * GET <key> = <value>
 *
 * GET <key> <version number>
 * Print out the key, the version number and the value of key as it was at the time of the version number,
 * or <NULL> if that key was not set at that time, as in GET <key>(#version) = <value>.
 * If the version number has not yet been recorded, return the most recent value for the key.
 * See below for examples of formatted output.
 */
public class KeyValueStore {
    private final Map<String, TreeMap<Integer, String>> map;
    private int currentVersion;

    KeyValueStore() {
        this.map = new HashMap<>();
        this.currentVersion = 1;
    }

    public int put(String key, String value) {
        if (map.isEmpty()) {
            TreeMap<Integer, String> versionMap = new TreeMap<>();
            versionMap.put(currentVersion, value);
            map.put(key, versionMap);
        } else {
            TreeMap<Integer, String> versionMap = map.getOrDefault(key, new TreeMap<>());
            //System.out.println(versionMap.toString());
            currentVersion += 1;
            versionMap.put(currentVersion, value);
            map.put(key, versionMap);
        }
        return currentVersion;
    }

    public String get(String key) {
        if (!map.containsKey(key)) {
            return null;
        }
        TreeMap<Integer, String> versionMap = map.get(key);
        //System.out.println(versionMap.toString());
        Integer hightVersion = versionMap.floorKey(currentVersion);
        return hightVersion != null ? versionMap.get(hightVersion) : null;
    }

    public String get(String key, int version) {
        return map.get(key).get(version);
    }

    public static void main(String[] args) {
        KeyValueStore kvStore = new KeyValueStore();
        System.out.println(kvStore.put("name", "Mike"));
        System.out.println(kvStore.put("name", "Jack"));
        System.out.println(kvStore.put("age", "23"));
        System.out.println(kvStore.put("name", "Stacy"));
        System.out.println(kvStore.put("age", "31"));

        System.out.println(kvStore.get("name"));
        System.out.println(kvStore.get("name", 2));
        System.out.println(kvStore.get("name", 5));
        System.out.println(kvStore.get("age"));
        System.out.println(kvStore.get("age", 3));
        System.out.println(kvStore.get("age", 1));
    }
}
