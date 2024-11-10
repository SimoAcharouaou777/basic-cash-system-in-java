# ToyBoxCache

A simple in-memory cache implementation for managing temporary data storage with time-to-live (TTL) and maximum capacity constraints. This project demonstrates how to use caching techniques to store and retrieve objects efficiently.

## What is Caching?

Caching is a process used to store frequently accessed data in a temporary storage area (cache) to speed up data retrieval and reduce the need for repeated data processing. In a typical caching mechanism:
- **Capacity**: Limits the maximum number of items that can be stored.
- **Time-to-Live (TTL)**: Defines the lifespan of each cached item before it is automatically removed.
  
By using caching, applications can save time and resources by accessing stored copies of data instead of recalculating or retrieving them repeatedly.

## Project Overview

`ToyBoxCache` is a Java class that manages a fixed number of objects (referred to as "toys") in a cache with a specified capacity and TTL. Once the cache reaches its maximum capacity, it removes the oldest (least recently used) item to make room for new entries. Expired items are also removed automatically upon retrieval attempts.

### Features

- **Capacity Management**: Limits the cache to a specified number of entries.
- **Time-to-Live (TTL)**: Items have a set expiration time, and expired items are removed.
- **Least Recently Used (LRU) Eviction**: Removes the oldest item if the cache reaches its maximum capacity.

## Code Structure

- **ToyBoxCache Class**: The main class, implementing a generic cache with capacity and TTL features.
  - `addToy(K key, V value)`: Adds a new toy to the cache. Removes the oldest toy if the cache is full.
  - `getToy(K key)`: Retrieves a toy from the cache. Refreshes the item if it hasn’t expired; otherwise, removes it.
  - `isExpired(Toy<V> toy)`: Checks if a toy has expired based on TTL.
  - `removeOldestToy()`: Removes the oldest entry in the cache when the capacity is full.
  - `removeExpiredToys()`: Cleans up expired items from the cache.

### Toy Class
A nested class inside `ToyBoxCache` that represents an individual item (toy) stored in the cache. Each toy keeps track of:
- `value`: The data stored.
- `timeAdded`: The time when it was added to the cache.

### Usage Example

The main method demonstrates how to interact with `ToyBoxCache`:

```java
ToyBoxCache<String, String> toyBox = new ToyBoxCache<>(2, 3000); // Max 2 items, 3-second TTL

toyBox.addToy("Car", "Toy Car");
toyBox.addToy("Doll", "Toy Doll");

System.out.println(toyBox.getToy("Car"));   // Outputs: Toy Car
System.out.println(toyBox.getToy("Doll"));  // Outputs: Toy Doll

Thread.sleep(4000); // Wait 4 seconds (exceeding TTL)

System.out.println(toyBox.getToy("Car"));   // Outputs: null (expired)
toyBox.addToy("Blocks", "Toy Blocks");

System.out.println(toyBox.getToy("Doll"));  // Outputs: null (expired)
System.out.println(toyBox.getToy("Blocks"));// Outputs: Toy Blocks
