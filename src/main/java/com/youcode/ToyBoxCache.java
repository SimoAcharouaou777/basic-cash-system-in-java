import java.util.*;
import java.util.concurrent.*;

public class ToyBoxCache<K, V> {
    private final int capacity; // Maximum number of toys in the box
    private final long ttlMillis; // Fun time for each toy in milliseconds
    private final Map<K, Toy<V>> toyBox = new LinkedHashMap<>(); // Store toys in order of use

    public ToyBoxCache(int capacity, long ttlMillis) {
        this.capacity = capacity;
        this.ttlMillis = ttlMillis;
    }

    // A toy in our toy box with its value and the time it was added
    private class Toy<V> {
        V value;
        long timeAdded;

        Toy(V value) {
            this.value = value;
            this.timeAdded = System.currentTimeMillis(); // Record when we added the toy
        }
    }

    // Add a toy to the toy box
    public void addToy(K key, V value) {
        removeExpiredToys(); // Remove expired toys before adding
        if (toyBox.size() >= capacity) {
            removeOldestToy(); // If full, remove the least recently played with toy
        }
        toyBox.put(key, new Toy<>(value)); // Add new toy
    }

    // Take a toy out to play with it
    public V getToy(K key) {
        Toy<V> toy = toyBox.get(key);
        if (toy != null && !isExpired(toy)) {
            toyBox.remove(key); // Refresh position for LRU
            toyBox.put(key, toy); // Put back to mark as recently used
            return toy.value; // Return the toy
        } else {
            toyBox.remove(key); // Remove if expired
            return null; // Toy is gone or expired
        }
    }

    // Check if a toy has expired
    private boolean isExpired(Toy<V> toy) {
        return System.currentTimeMillis() - toy.timeAdded > ttlMillis;
    }

    // Remove the oldest toy (least recently used)
    private void removeOldestToy() {
        K oldestKey = toyBox.keySet().iterator().next();
        toyBox.remove(oldestKey);
    }

    // Remove expired toys from the box
    private void removeExpiredToys() {
        Iterator<Map.Entry<K, Toy<V>>> iterator = toyBox.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<K, Toy<V>> entry = iterator.next();
            if (isExpired(entry.getValue())) {
                iterator.remove(); // Remove toy if expired
            }
        }
    }

    // Test the toy box
    public static void main(String[] args) throws InterruptedException {
        ToyBoxCache<String, String> toyBox = new ToyBoxCache<>(2, 3000); // 2 toys max, 3 seconds fun time

        toyBox.addToy("Car", "Toy Car");
        toyBox.addToy("Doll", "Toy Doll");

        System.out.println(toyBox.getToy("Car")); // Should print "Toy Car"

        Thread.sleep(4000); // Wait 4 seconds for toys to expire

        System.out.println(toyBox.getToy("Car")); // Should print null (expired)
        toyBox.addToy("Blocks", "Toy Blocks"); // Add new toy after expiration

        System.out.println(toyBox.getToy("Doll")); // Should print null (expired)
        System.out.println(toyBox.getToy("Blocks")); // Should print "Toy Blocks"
    }
}
