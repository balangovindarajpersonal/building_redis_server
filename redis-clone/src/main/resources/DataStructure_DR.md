# ADR 2: Use ConcurrentHashMap with Singleton Instance for Key-Value Store Management

## Status

Accepted

## Context

The Redis clone I am developing requires an in-memory data structure to store key-value pairs. This structure must be capable of supporting high concurrency, allowing for multiple client operations to simultaneously occur without conflict or data corruption.

A thread-safe implementation is necessary to efficiently handle concurrent reads and writes. It is also critical to ensure that the key-value store maintains data integrity and consistency in a multithreaded environment.

## Decision

I have decided to use a `ConcurrentHashMap` with a Singleton instance to manage the key-value store within the Redis clone. `ConcurrentHashMap` is a thread-safe collection introduced in Java 5 and is optimized for concurrent operations, enabling fast retrievals and maintaining adequate performance for write operations.

Implementing it as a Singleton ensures that a single, globally accessible instance of the `ConcurrentHashMap` is used throughout the application, avoiding the creation of multiple instances or the need to pass the map around.

## Rationale

The reasons for choosing `ConcurrentHashMap` with a Singleton instance are as follows:

- **Concurrency Support:** `ConcurrentHashMap` is designed for concurrent use, permitting multiple threads to read and write without the need for explicit synchronization.
- **Performance:** It offers better performance than `Hashtable` or `Collections.synchronizedMap` by facilitating concurrent reads and partitioned write operations.
- **Memory Consistency:** It assures memory consistency guarantees (happens-before relationships).
- **Scalability:** It is capable of scaling with the anticipated number of threads and concurrent operations.
- **Singleton Instance:** It guarantees that the same instance is consistently utilized throughout the server application, which is vital for the integrity of the key-value store.

## Consequences

Choosing to use `ConcurrentHashMap` with a Singleton instance results in the following consequences:

- **Positive:**
    - Eliminates the need for external synchronization when accessing the map, reducing the likelihood of deadlocks.
    - Enhances the performance and responsiveness of the server under high concurrent load.
    - Assures thread safety and data integrity across server operations.

- **Negative:**
    - Employing a Singleton can complicate testing due to the introduction of global state within the application.
    - The overhead associated with managing the segments of the `ConcurrentHashMap` may result in greater memory usage compared to other structures in scenarios with low concurrency.

In conclusion, the advantages of using `ConcurrentHashMap` are well-suited to the concurrency requirements and the anticipated operation patterns of my Redis clone.
