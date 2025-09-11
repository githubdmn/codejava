
---

# ✅ Java Interview Prep Question Bank (Flashcard Style)

## **1. Core Java / OOP**

**Q: What are the four pillars of OOP?**
**A:** Encapsulation (bundling data/methods, restricting access), Abstraction (hiding complex implementation), Inheritance (IS-A relationship, code reuse), Polymorphism (one interface, many forms: overriding/overloading).

**Q: Difference between abstract class and interface? (Pre-Java 8)**
**A:** Abstract class: can have state (instance variables), constructors, and abstract/concrete methods. Interface: only constants and abstract methods. A class can extend only one abstract class but implement multiple interfaces.

**Q: Difference between abstract class and interface? (Java 8+)**
**A:** Interfaces can now have `default` and `static` methods. The key difference is that abstract classes can still have state (fields) and constructors, while interfaces cannot. The choice is now more about *what* the relationship represents ("is-a" vs "behaves-like") and whether you need state.

**Q: What is method overloading vs overriding?**
**A:** **Overloading:** Same method name, different parameters (compile-time polymorphism). **Overriding:** Subclass provides a specific implementation of a method already defined in its superclass (runtime polymorphism).

**Q: Difference between `==` and `.equals()`?**
**A:** `==` compares object references (memory address). `.equals()` compares object content (logical equality). Must override `equals()` (and `hashCode()`) in your classes to define logical equality.

**Q: What is a checked exception vs unchecked exception?**
**A:** **Checked:** Must be declared or caught (`IOException`, `SQLException`). **Unchecked:** Extends `RuntimeException`; not forced to handle (`NullPointerException`, `ArrayIndexOutOfBoundsException`).

**Q: Explain the difference between HashMap and HashSet.**
**A:** `HashMap` stores key-value pairs. `HashSet` implements the `Set` interface and stores only unique elements internally using a `HashMap` (the element is the key, and a dummy object is the value).

**Q: How does the Java memory model work?**
**A:** **Heap:** Stores objects (shared across threads). **Stack:** Stores local primitives and object references (thread-specific). Garbage Collection runs on the heap to reclaim memory from unreachable objects.

**Q: Why is `String` immutable?**
**A:** Security (e.g., not altering database URLs, parameters), thread-safety, caching hashcodes, and performance (String Pooling).

---

## **2. Java Concurrency**

**Q: Difference between process and thread?**
**A:** A **Process** has its own memory space. A **Thread** is a lightweight process that shares the same memory space within a process.

**Q: How do you create a thread?**
**A:** 1. Extend the `Thread` class and override `run()`. 2. Implement the `Runnable` interface and pass it to a `Thread` constructor. (Implementing `Runnable` is preferred to avoid single inheritance limitation).

**Q: What is a thread-safe collection? Example?**
**A:** A collection designed to be safely used by multiple threads concurrently. E.g., `ConcurrentHashMap`, `CopyOnWriteArrayList`.

**Q: Difference between synchronized method and synchronized block?**
**A:** **Synchronized method** locks on the entire object (`this`). **Synchronized block** allows you to specify a different monitor object to lock on, providing a finer-grained concurrency control.

**Q: What is the `volatile` keyword used for?**
**A:** Ensures variable reads/writes are directly done from/to main memory, not thread caches. It guarantees visibility of changes across threads but not atomicity (e.g., `volatile` won't make `count++` atomic).

**Q: Explain deadlock and how to prevent it.**
**A:** Deadlock occurs when two or more threads are waiting forever for locks held by each other. **Prevention:** Avoid nested locking, acquire locks in a fixed, consistent order, use lock timeouts.

---

## **3. Java & Spring Boot**

**Q: Key features of Spring Boot?**
**A:** Auto-configuration, embedded servers (Tomcat), standalone production-ready apps, simplified dependency management via starters, no XML configuration.

**Q: Explain Dependency Injection (DI).**
**A:** A design pattern where an object receives its dependencies from an external source (an IoC container) rather than creating them itself. Promotes loose coupling and testability. Spring implements it via `@Autowired`.

**Q: Difference between `@Component`, `@Service`, `@Repository`?**
**A:** All are stereotypes that mark a class for Spring's component scanning. `@Service` and `@Repository` are specializations of `@Component` for semantic clarity. `@Repository` also automatically translates persistence-specific exceptions into Spring's unified `DataAccessException`.

**Q: Role of `@Configuration` and `@Bean`?**
**A:** `@Configuration` indicates a class defines Spring beans. `@Bean` marks a method within such a class; the method's return value is registered as a bean in the Spring context.

**Q: How to handle application configuration?**
**A:** Use `application.properties` or `application.yml` files. Use `@Value("${property.name}")` to inject values or `@ConfigurationProperties` for type-safe binding to a POJO.

**Q: Difference between `@RestController` and `@Controller`?**
**A:** `@Controller` is used to mark a class as a web request handler, often returning a view name. `@RestController` is a combination of `@Controller` and `@ResponseBody`, meaning its methods return data (JSON/XML) directly written to the HTTP response body.

**Q: How does Spring Boot autoconfiguration work?**
**A:** It uses conditions (`@ConditionalOnClass`, `@ConditionalOnProperty`, etc.) to automatically configure beans based on the libraries (jars) on the classpath, reducing manual configuration.

**Q: How does Spring manage transactions?**
**A:** Using the `@Transactional` annotation. It provides declarative transaction management, automatically starting a transaction when the method is called and committing/rolling back upon method completion/exception.

---

## **4. JPA / Hibernate**

**Q: What is an ORM?**
**A:** Object-Relational Mapping. It maps Java objects to database tables, allowing developers to interact with a database using OOP paradigms instead of writing raw SQL.

**Q: Explain lazy vs eager fetching.**
**A:** **Eager:** Related entities are loaded immediately with the parent. **Lazy:** Related entities are loaded only when explicitly accessed. Lazy is the default for `@OneToMany` and `@ManyToMany` to avoid loading unnecessary data.

**Q: What is the N+1 query problem?**
**A:** When you fetch a list of entities (1 query) and then access a lazy-loaded collection for each entity, triggering N additional queries. This is inefficient.

**Q: How do you fix the N+1 problem?**
**A:** Use a `JOIN FETCH` in your JPQL query (e.g., `SELECT e FROM Employee e JOIN FETCH e.department`) or use `@EntityGraph` to dynamically fetch the required associations in a single query.

**Q: What are the entity states?**
**A:** **Transient:** New object not associated with a session. **Persistent:** Object associated with a session and mapped to a DB row. **Detached:** Object was persistent but the session was closed. **Removed:** Object is scheduled for deletion.

**Q: How do you map a `@OneToMany` relationship?**
**A:**
```java
// Parent Side (Department)
@OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
private List<Employee> employees;

// Child Side (Employee)
@ManyToOne
@JoinColumn(name = "dept_id")
private Department department;
```
Using `mappedBy` makes it a bidirectional relationship.

---

## **5. SQL / Databases**

**Q: Difference between INNER JOIN, LEFT JOIN, RIGHT JOIN?**
**A:** **INNER JOIN:** Returns records with matching values in both tables. **LEFT (OUTER) JOIN:** Returns all records from the left table, and matched records from the right (nulls if no match). **RIGHT JOIN:** The opposite of LEFT JOIN.

**Q: What is normalization?**
**A:** The process of organizing data to reduce redundancy and improve data integrity by dividing tables and defining relationships.

**Q: How do indexes improve performance? Downsides?**
**A:** **Improvement:** Speed up data retrieval (SELECT queries) by creating a sorted data structure. **Downsides:** Slows down INSERT/UPDATE/DELETE operations because the index also must be updated. Consumes additional storage space.

**Q: What are ACID properties?**
**A:** **Atomicity** (all-or-nothing), **Consistency** (keeps DB valid), **Isolation** (transactions don't interfere), **Durability** (committed changes persist).

**Q: How does PostgreSQL handle concurrency? (Isolation Levels)**
**A:** It uses MVCC (Multi-Version Concurrency Control). Common levels: **Read Committed** (default, see only committed data), **Repeatable Read** (prevent non-repeatable reads), **Serializable** (highest isolation, prevents phantom reads).

**Q: Write a query to find the second highest salary.**
**A:**
```sql
SELECT MAX(salary) FROM employees
WHERE salary < (SELECT MAX(salary) FROM employees);
-- OR using LIMIT/OFFSET (PostgreSQL/MySQL)
SELECT salary FROM employees ORDER BY salary DESC LIMIT 1 OFFSET 1;
```

---

## **6. Security**

**Q: Difference between Authentication and Authorization?**
**A:** **Authentication (AuthN):** Verifying *who* you are (e.g., login). **Authorization (AuthZ):** Verifying what you are *allowed* to do (e.g., permissions/roles).

**Q: How does JWT work?**
**A:** After login, the server creates a signed JSON Web Token (JWT) containing user claims. The client sends this token in the `Authorization: Bearer <token>` header for subsequent requests. The server verifies the signature to trust the token's contents.

**Q: How to prevent SQL Injection?**
**A:** **Never** use string concatenation for queries. **Always** use Prepared Statements (with parameterized queries) which treat user input as data, not executable SQL code.

**Q: How to prevent XSS attacks?**
**A:** Validate and sanitize all user input. Escape data before rendering it in HTML (e.g., using OWASP Java Encoder library). Use Content Security Policy (CSP) headers.

---

## **7. REST APIs & Microservices**

**Q: What are idempotent HTTP methods?**
**A:** Methods that can be called multiple times without changing the result (only the first call has an effect). **GET, PUT, DELETE** are idempotent. **POST** is not.

**Q: How do microservices communicate?**
**A:** **Synchronously:** HTTP/REST, gRPC. **Asynchronously:** Message brokers (Kafka, RabbitMQ) for event-driven communication, improving decoupling and resilience.

**Q: What is a Circuit Breaker pattern?**
**A:** A pattern to prevent a cascade of failures. If a service call fails repeatedly, the circuit breaker "trips" and temporarily stops all calls to that failing service, allowing it to recover. Implemented with Resilience4j or Hystrix.

---

## **8. Software Engineering Practices**

**Q: What are the SOLID principles?**
**A:**
*   **S**ingle Responsibility: A class should have one reason to change.
*   **O**pen/Closed: Open for extension, closed for modification.
*   **L**iskov Substitution: Subtypes must be substitutable for their base types.
*   **I**nterface Segregation: Prefer small, specific interfaces over large ones.
*   **D**ependency Inversion: Depend on abstractions, not concretions.

**Q: What do you look for in a code review?**
**A:** Correctness, readability/clarity, design (SOLID, patterns), test coverage, security pitfalls, error handling, and performance considerations.

**Q: How do you handle technical debt?**
**A:** Acknowledge it, track it (e.g., in tickets), and proactively allocate time (e.g., during sprints) to refactor and pay it down, preventing it from becoming unmanageable.

---
