I'll provide detailed answers to all the Java interview questions from your cheat sheet:

# 1. **Core Java / OOP**

## What are the four pillars of OOP?

**1. Encapsulation**
- Bundling data (variables) and methods that operate on that data within a single unit (class)
- Hiding internal implementation details from an outside world
- Using access modifiers (private, protected, public) to control access
- Example: Private fields with public getter/setter methods

**2. Abstraction**
- Hiding complex implementation details while showing only essential features
- Achieved through abstract classes and interfaces
- Users interact with objects without knowing internal workings
- Example: Using a Car interface without knowing engine mechanics

**3. Inheritance**
- Mechanism where a new class inherits properties and methods from an existing class
- Promotes code reusability and establishes "is-a" relationship
- Child class can override parent methods (polymorphism)
- Example: Dog extends Animal

**4. Polymorphism**
- Same interface, different implementations
- Runtime polymorphism (method overriding) and compile-time polymorphism (method overloading)
- Allows treating objects of different classes uniformly
- Example: Animal reference pointing to Dog or Cat objects

## Difference between abstract class and interface?

**Abstract Class:**
- Can have both abstract and concrete methods
- Can have instance variables and constructors
- Supports single inheritance only
- Can have access modifiers for methods
- Use when classes share common code and have "is-a" relationship

**Interface:**
- All methods are implicitly public and abstract (before Java 8)
- Since Java 8: can have default and static methods
- Since Java 9: can have private methods
- All variables are implicitly public, static, and final
- Supports multiple inheritance
- Use when defining contracts that multiple unrelated classes can implement

## Method overloading vs overriding?

**Method Overloading (Compile-time polymorphism):**
- Same method name, different parameters (number, type, or order)
- Occurs within same class or inheritance hierarchy
- Return type can be different
- Resolved at compile time
- Example: `add(int a, int b)` and `add(double a, double b)`

**Method Overriding (Runtime polymorphism):**
- Same method signature in parent and child class
- Child class provides specific implementation
- Must have same return type (or covariant)
- Resolved at runtime based on actual object type
- Uses `@Override` annotation

## Difference between `==` and `.equals()`?

**`==` operator:**
- Compares references (memory addresses) for objects
- Compares actual values for primitives
- Fast operation
- Cannot be overridden

**`.equals()` method:**
- Compares object content/state
- Can be overridden to define custom equality logic
- Default implementation in Object class uses `==`
- String class overrides equals() to compare character sequences
- Always override `hashCode()` when overriding `equals()`

## Checked vs unchecked exceptions?

**Checked Exceptions:**
- Must be handled or declared in method signature
- Checked at compile time
- Extend Exception but not RuntimeException
- Examples: IOException, SQLException, ClassNotFoundException
- Used for recoverable errors

**Unchecked Exceptions:**
- Not required to be handled or declared
- Checked at runtime
- Extend RuntimeException
- Examples: NullPointerException, IllegalArgumentException, ArrayIndexOutOfBoundsException
- Usually indicate programming errors

## Difference between HashMap and HashSet?

**HashMap:**
- Stores key-value pairs
- Implements Map interface
- Keys must be unique, values can be duplicate
- Allows one null key and multiple null values
- O(1) average time complexity for get/put operations
- Used when you need to associate values with keys

**HashSet:**
- Stores unique elements only
- Implements Set interface
- Internally uses HashMap (elements as keys, dummy object as value)
- Allows one null element
- O(1) average time complexity for add/remove/contains
- Used when you need to maintain unique collection

## Java memory model (heap, stack, garbage collection)?

**Stack Memory:**
- Stores method call frames, local variables, and partial results
- Thread-specific (each thread has its own stack)
- Follows LIFO principle
- Automatically managed
- Fast allocation/deallocation
- Limited in size (can cause StackOverflowError)

**Heap Memory:**
- Stores objects and instance variables
- Shared among all threads
- Divided into Young Generation and Old Generation
- Young Generation: Eden space, Survivor spaces (S0, S1)
- Old Generation: Tenured space
- Method Area/Metaspace: stores class metadata

**Garbage Collection:**
- Automatic memory management
- Removes unreferenced objects from heap
- Types: Serial, Parallel, G1, ZGC, etc.
- Phases: Mark (identify unreachable objects), Sweep (remove them), Compact (defragment)

## Immutable objects? Why is String immutable?

**Immutable Objects:**
- State cannot be changed after creation
- Thread-safe by default
- Can be cached and reused
- Suitable as HashMap keys
- Examples: String, Integer, LocalDate

**Why String is immutable:**
1. **Security**: Prevents malicious code from changing strings used in security contexts
2. **String Pool**: Enables string interning for memory efficiency
3. **Thread Safety**: Multiple threads can access the same string safely
4. **Caching**: Hash code can be cached since it won't change
5. **Performance**: Optimizations possible due to immutability

# 2. **Java Concurrency**

## Process vs Thread?

**Process:**
- Independent execution unit with own memory space
- Heavy-weight (more resources required)
- Inter-process communication is complex and slow
- Process crash doesn't affect other processes
- Context switching is expensive
- Examples: Running applications

**Thread:**
- Lightweight unit of execution within a process
- Shares memory space with other threads in the same process
- Communication through shared memory is fast
- Thread crash can affect an entire process
- Context switching is less expensive
- Examples: Background tasks in an application

## Creating threads in Java?

**1. Extending Thread class:**
```java
class MyThread extends Thread {
    public void run() {
        // Thread logic
    }
}
MyThread thread = new MyThread();
thread.start();
```


**2. Implementing Runnable interface (Preferred):**
```java
class MyTask implements Runnable {
    public void run() {
        // Thread logic
    }
}
Thread thread = new Thread(new MyTask());
thread.start();
```


**Why Runnable is preferred:**
- Allows a class to extend another class
- Better separation of concerns
- Can be reused with different execution mechanisms (ExecutorService)

## Thread-safe collections?

**Thread-safe collections:**
- Can be safely accessed by multiple threads concurrently
- Internal synchronization mechanisms prevent data corruption
- Examples: Vector, Hashtable, ConcurrentHashMap, CopyOnWriteArrayList

**ConcurrentHashMap:**
- Segment-based locking (Java 7) or node-based locking (Java 8+)
- Better performance than synchronized HashMap
- Allows concurrent reads and limited concurrent writes
- Null keys/ values are not allowed

## Synchronized method vs synchronized block?

**Synchronized Method:**
- The entire method is synchronized
- Uses object's intrinsic lock (these, for instance, methods, Class object for static methods)
- Simpler syntax but less flexible
- Can lead to performance issues if the method is large

**Synchronized Block:**
- Only a specific code block is synchronized
- Can specify which object's lock to use
- More flexible and granular control
- Better performance as only a critical section is locked
- Allows multiple locks for different resources

## Volatile keyword?

**Purpose:**
- Ensures visibility of changes across threads
- Prevents instruction reordering around volatile operations
- Lighter alternative to synchronization for simple cases

**Use cases:**
- Boolean flags for thread coordination
- When only one thread writes, multiple threads read
- Simple state variables

**Limitations:**
- Doesn't provide atomicity for compound operations
- Not suitable for operations like increment (i++)

## Deadlock and prevention?

**Deadlock:**
- Situation where two or more threads are permanently blocked
- Each thread waits for a resource held by another thread
- Results in circular dependency

**Four conditions for deadlock:**
1. Mutual Exclusion
2. Hold and Wait
3. No Preemption
4. Circular Wait

**Prevention strategies:**
1. **Lock Ordering**: Always acquire locks in the same order
2. **Lock Timeout**: Use tryLock() with timeout
3. **Deadlock Detection**: Monitor and detect deadlocks
4. **Avoid Nested Locks**: Minimize holding multiple locks
5. **Use concurrent utilities**: ReentrantLock, Semaphore

# 3. **Java & Spring Boot**

## Key features of Spring Boot?

1. **Auto-configuration**: Automatically configures application based on dependencies
2. **Starter Dependencies**: Pre-configured dependency sets for common use cases
3. **Embedded Servers**: Tomcat, Jetty, Undertow embedded by default
4. **Production-ready Features**: Health checks, metrics, externalized configuration
5. **Spring Boot CLI**: Command-line tool for rapid prototyping
6. **Spring Boot Actuator**: Monitoring and management endpoints
7. **No XML Configuration**: Annotation-based configuration
8. **Opinionated Defaults**: Sensible defaults that can be overridden

## Dependency Injection in Spring?

**Dependency Injection:**
- Design pattern where objects receive dependencies rather than creating them
- Promotes loose coupling and testability
- Spring acts as IoC (Inversion of Control) container

**Types:**
1. **Constructor Injection** (Recommended)
2. **Setter Injection**
3. **Field Injection** (Not recommended)

**Spring Implementation:**
- Uses reflection and annotations
- Manages object lifecycle
- Resolves dependencies at runtime
- Supports circular dependency detection

## @Component, @Service, @Repository differences?

**@Component:**
- Generic stereotype annotation
- Marks class as a Spring-managed component
- Base annotation for other specializations

**@Service:**
- Specialization of @Component
- Indicates class contains business logic
- Semantic meaning for service layer
- No additional functionality over @Component

**@Repository:**
- Specialization of @Component
- Indicates class is a data access object
- Enables automatic exception translation (SQL exceptions to Spring's DataAccessException)
- Used for database operations

**All are functionally equivalent but provide semantic meaning and enable specific Spring features.**

## @Configuration and @Bean?

**@Configuration:**
- Indicates class contains bean definitions
- Replacement for XML configuration
- Spring container processes methods annotated with @Bean
- Creates CGLIB proxy for configuration classes

**@Bean:**
- Method-level annotation
- Indicates method produces bean managed by Spring container
- Method name becomes bean name (unless specified otherwise)
- Used when you can't add @Component to third-party classes

## Application configuration in Spring Boot?

**1. application.properties:**
```properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost/mydb
```


**2. application.yml:**
```yaml
server:
  port: 8080
spring:
  datasource:
    url: jdbc:mysql://localhost/mydb
```


**Configuration hierarchy (highest to lowest precedence):**
1. Command line arguments
2. System properties
3. Environment variables
4. application-{profile}.properties
5. application.properties
6. @PropertySource
7. Default properties

**@ConfigurationProperties:**
- Type-safe configuration binding
- Maps properties to Java objects
- Supports validation and relaxed binding

## @RestController vs @Controller?

**@Controller:**
- Returns view name (String) for view resolution
- Used in traditional MVC applications
- Requires @ResponseBody for JSON/XML responses
- Works with ViewResolver

**@RestController:**
- Combination of @Controller + @ResponseBody
- All methods return data directly (JSON/XML)
- Used for REST APIs
- No view resolution involved
- Automatic serialization to JSON/XML

## Spring Boot autoconfiguration?

**How it works:**
1. **@EnableAutoConfiguration**: Enables auto-configuration
2. **Conditional Annotations**: @ConditionalOnClass, @ConditionalOnMissingBean, etc.
3. **spring.factories**: Lists auto-configuration classes
4. **Configuration Classes**: Automatically configured based on classpath and properties

**Process:**
1. Spring Boot scans classpath for dependencies
2. Matches dependencies to auto-configuration classes
3. Applies configurations based on conditions
4. Creates beans if not already present
5. Uses default properties that can be overridden

## Spring transaction management (@Transactional)?

**@Transactional:**
- Declarative transaction management
- Can be applied to classes or methods
- Creates proxy for transactional behavior

**Key attributes:**
- **propagation**: How transactions relate to each other
- **isolation**: Transaction isolation level
- **readOnly**: Optimization for read-only operations
- **timeout**: Transaction timeout
- **rollbackFor**: Exceptions that cause rollback

**Transaction propagation:**
- REQUIRED (default): Join existing or create new
- REQUIRES_NEW: Always create a new transaction
- SUPPORTS: Join if exists, run without if not
- NOT_SUPPORTED: Run without transaction

# 4. **JPA / Hibernate**

## What is ORM? Why use Hibernate/JPA?

**ORM (Object-Relational Mapping):**
- Programming technique that maps objects to relational database tables
- Bridges gap between object-oriented programming and relational databases
- Converts data between incompatible type systems

**Benefits of Hibernate/JPA:**
1. **Database Independence**: Abstract away database-specific details
2. **Reduced Boilerplate**: Less SQL code to write
3. **Object-Oriented**: Work with objects instead of tables
4. **Caching**: First and second level caching
5. **Lazy Loading**: Load data on demand
6. **Transaction Management**: Automatic transaction handling
7. **Query Language**: HQL/JPQL instead of SQL

## Lazy vs Eager fetching?

**Lazy Loading:**
- Data is loaded on-demand when accessed
- Default for @OneToMany and @ManyToMany
- Improves performance by avoiding unnecessary queries
- Can cause LazyInitializationException if the session is closed
- Uses proxy objects

**Eager Loading:**
- All related data loaded immediately
- Default for @ManyToOne and @OneToOne
- Simple to use, no session management issues
- Can cause performance problems (N+1 queries)
- Loads more data than needed

**Best Practices:**
- Use lazy loading as default
- Use eager loading for frequently accessed relationships
- Use JOIN FETCH queries for specific cases
- Consider DTO projections for read-only data

## Hibernate caching?

**First-Level Cache (Session Cache):**
- Enabled by default
- Session-scoped
- Stores entities within single session
- Automatic management
- Prevents duplicate queries within session

**Second-Level Cache:**
- Optional, must be configured
- SessionFactory-scoped (application-wide)
- Shared across sessions
- Provider-specific (EHCache, Hazelcast, etc.)
- Configurable per entity
- Improves performance for frequently accessed data

**Query Cache:**
- Caches query results
- Works with second-level cache
- Must be explicitly enabled
- Cache invalidation on entity changes

## Entity states in Hibernate?

**1. Transient:**
- New object, not associated with session
- Not persisted to a database
- Changes not tracked by Hibernate

**2. Persistent:**
- Associated with session
- Has database identity (primary key)
- Changes automatically synchronized to a database
- Managed by Hibernate

**3. Detached:**
- Was persistent but the session is closed
- Has database identity
- Changes not tracked
- Can be reattached using merge() or update()

**4. Removed:**
- Marked for deletion
- Still in session but will be deleted on a flush
- Object exists in memory but will be removed from a database

## Relationship mappings?

**@OneToOne:**
- One entity relates to exactly one other entity
- Can be unidirectional or bidirectional
- Uses foreign key or shared primary key

**@OneToMany:**
- One entity relates to multiple entities
- Parent side of a relationship
- Usually mapped by @JoinColumn or @JoinTable

**@ManyToOne:**
- Multiple entities relate to one entity
- Child side of relationship
- Uses foreign key

**@ManyToMany:**
- Multiple entities relate to multiple entities
- Requires junction table
- Can have additional attributes in the junction entity

**Best Practices:**
- Use bidirectional relationships carefully
- Set cascade and fetch types appropriately
- Consider using @JoinColumn for better control

## N+1 query problem and solutions?

**N+1 Problem:**
- Initial query loads N records
- Each record triggers an additional query for related data
- Results in N+1 total queries instead of efficient joins

**Solutions:**

**1. JOIN FETCH:**
```java
@Query("SELECT p FROM Post p JOIN FETCH p.comments")
List<Post> findAllWithComments();
```


**2. Entity Graphs:**
```java
@EntityGraph(attributePaths = {"comments"})
List<Post> findAll();
```


**3. Batch Fetching:**
```java
@BatchSize(size = 10)
@OneToMany(mappedBy = "post")
private List<Comment> comments;
```


**4. DTO Projections:**
- Load only required data
- Avoid object graph navigation

**5. Second-Level Cache:**
- Cache frequently accessed entities

## Optimizing database queries in JPA?

**1. Use Projections:**
- Select only required fields
- DTO projections or interface projections
- Reduces data transfer and memory usage

**2. Pagination:**
- Use Pageable for large result sets
- Avoid loading all records at once

**3. Indexing:**
- Create database indexes on frequently queried columns
- Monitor query execution plans

**4. Batch Operations:**
- Use batch inserts/updates
- Configure hibernate.jdbc.batch_size

**5. Native Queries:**
- Use for complex queries where JPA is inefficient
- Database-specific optimizations

**6. Connection Pooling:**
- Configure the proper connection pool size
- Monitor connection usage

**7. Query Hints:**
- Provide hints to query optimizer
- Control fetch strategies per query

# 5. **SQL / Databases**

## SQL query for customers spending more than X last month?

```sql
SELECT c.customer_id, c.customer_name, SUM(o.total_amount) as total_spent
FROM customers c
JOIN orders o ON c.customer_id = o.customer_id
WHERE o.order_date >= DATE_SUB(CURRENT_DATE, INTERVAL 1 MONTH)
  AND o.order_date < CURRENT_DATE
GROUP BY c.customer_id, c.customer_name
HAVING SUM(o.total_amount) > X
ORDER BY total_spent DESC;
```


## JOIN types differences?

**INNER JOIN:**
- Returns records matching in both tables
- Excludes non-matching records
- Most restrictive join
- Best performance for matched data

**LEFT JOIN (LEFT OUTER JOIN):**
- Returns all records from the left table
- Matching records from the right table
- NULL values for non-matching right table columns
- Use when you need all records from the primary table

**RIGHT JOIN (RIGHT OUTER JOIN):**
- Returns all records from the right table
- Matching records from the left table
- NULL values for non-matching left table columns
- Less commonly used than LEFT JOIN

**FULL OUTER JOIN:**
- Returns all records from both tables
- NULL values for non-matching columns
- Combines LEFT and RIGHT JOIN results
- Not supported by all databases (MySQL)

**CROSS JOIN:**
- Cartesian product of both tables
- Every row from the first table with every row from the second
- No ON clause required
- Use with caution due to large result sets

## Database normalization?

**Purpose:**
- Eliminate data redundancy
- Ensure data integrity
- Reduce storage space
- Prevent update anomalies

**Normal Forms:**

**First Normal Form (1NF):**
- Atomic values (no repeating groups)
- Each column contains single value
- No duplicate rows

**Second Normal Form (2NF):**
- Must be in 1NF
- No partial dependencies
- Non-key attributes fully dependent on a primary key

**Third Normal Form (3NF):**
- Must be in 2NF
- No transitive dependencies
- Non-key attributes are not dependent on other non-key attributes

**Boyce-Codd Normal Form (BCNF):**
- Stricter version of 3NF
- Every determinant is a candidate key

**Benefits:** Data integrity, storage efficiency, easier maintenance
**Drawbacks:** Complex queries, potential performance impact, more joins required

## Primary, Foreign, and Unique keys?

**Primary Key:**
- Uniquely identifies each row in the table
- Cannot be NULL
- Only one per table
- Automatically creates unique index
- Referenced by foreign keys

**Foreign Key:**
- References primary key of another table
- Establishes relationships between tables
- Can be NULL (unless specified otherwise)
- Enforces referential integrity
- Multiple foreign keys allowed per table

**Unique Key:**
- Ensures uniqueness of column values
- Can be NULL (usually one NULL allowed)
- Multiple unique keys allowed per table
- Automatically creates unique index
- Alternative candidate keys

## Database indexes?

**Benefits:**
1. **Faster Query Performance**: Quick data retrieval
2. **Efficient Sorting**: ORDER BY operations
3. **Unique Constraints**: Enforce data integrity
4. **Join Performance**: Faster table joins

**Types:**
- **B-Tree Index**: Default, good for equality and range queries
- **Hash Index**: Fast for equality comparisons
- **Bitmap Index**: Good for low-cardinality data
- **Partial Index**: Index subset of rows
- **Composite Index**: Multiple columns

**Downsides:**
1. **Storage Overhead**: Additional disk space required
2. **Slower Writes**: INSERT/UPDATE/DELETE operations slower
3. **Maintenance Cost**: Indexes need to be maintained
4. **Memory Usage**: Indexes consume memory

**Best Practices:**
- Index frequently queried columns
- Avoid over-indexing
- Consider composite indexes for multi-column queries
- Monitor index usage and remove unused indexes

## ACID properties?

**Atomicity:**
- Transaction is all-or-nothing
- Either all operations succeed or all fail
- No partial completion of transactions
- Rollback on failure ensures consistency

**Consistency:**
- Database remains in valid state
- All integrity constraints are maintained
- Transaction moves a database from one valid state to another
- Business rules are enforced

**Isolation:**
- Concurrent transactions don't interfere
- Each transaction appears to run in isolation
- Different isolation levels control visibility
- Prevents dirty reads, phantom reads, etc.

**Durability:**
- Committed changes are permanent
- Survive system failures
- Data persisted to non-volatile storage
- Recovery mechanisms ensure durability

## Transactions vs Savepoints?

**Transactions:**
- Unit of work that maintains ACID properties
- Begin with START TRANSACTION or BEGIN
- End with COMMIT or ROLLBACK
- All changes are atomic

**Savepoints:**
- Named points within transaction
- Allow partial rollback within transaction
- Useful for complex operations
- Don't affect transaction isolation

**Usage:**
```sql
BEGIN TRANSACTION;
-- Some operations
SAVEPOINT sp1;
-- More operations
SAVEPOINT sp2;
-- Error occurs
ROLLBACK TO sp1; -- Partial rollback
-- Continue transaction
COMMIT; -- Commit everything since last rollback
```


## PostgreSQL concurrency (isolation levels)?

**Read Uncommitted:**
- Lowest isolation level
- Allows dirty reads
- Not commonly used
- Performance benefit minimal in PostgreSQL

**Read Committed (Default):**
- Prevents dirty reads
- Allows non-repeatable reads and phantom reads
- Each statement sees committed data
- Good balance of consistency and performance

**Repeatable Read:**
- Prevents dirty and non-repeatable reads
- Still allows phantom reads
- Consistent snapshot within transaction
- Uses snapshot isolation

**Serializable:**
- Highest isolation level
- Prevents all phenomena
- Transactions appear to run serially
- Performance impact due to conflict detection

**PostgreSQL-specific features:**
- Uses Multi-Version Concurrency Control (MVCC)
- Readers don't block writers
- Writers don't block readers
- Efficient handling of concurrent access

# 6. **Security**

## Authentication vs Authorization?

**Authentication:**
- Process of verifying identity
- "Who are you?"
- Validates credentials (username/password, certificates, biometrics)
- Precedes authorization
- Examples: Login, JWT tokens, OAuth

**Authorization:**
- Process of granting access rights
- "What can you do?"
- Determines permitted actions/resources
- Based on roles, permissions, policies
- Examples: Role-based access, ACL, resource permissions

**Relationship:**
- Authentication comes first
- Authorization depends on authenticated identity
- Both are essential for complete security
- Can be implemented separately or together

## JWT-based authentication?

**JSON Web Token (JWT):**
- Self-contained token containing claims
- Digitally signed for integrity
- Stateless authentication mechanism

**Structure:**
1. **Header**: Token type and signing algorithm
2. **Payload**: Claims (user data, permissions, expiration)
3. **Signature**: Verification of token integrity

**Flow:**
1. User authenticates with credentials
2. Server creates and signs JWT
3. Client stores JWT (localStorage, cookie)
4. Client sends JWT with each request
5. Server validates JWT and extracts claims

**Benefits:**
- Stateless (no server-side session storage)
- Scalable across multiple services
- Self-contained (all info in token)
- Cross-domain authentication

**Security considerations:**
- Use HTTPS always
- Set appropriate expiration times
- Store securely on a client
- Implement token refresh mechanism
- Use strong signing algorithms (RS256)

## OAuth2 vs SSO?

**OAuth2:**
- Authorization framework
- Delegates authorization to third parties
- Token-based access control
- Primarily for API access
- Four grant types: Authorization Code, Implicit, Client Credentials, Password

**SSO (Single Sign-On):**
- Authentication pattern
- One login for multiple applications
- User authenticated once, accessed multiple systems
- Can use various protocols (SAML, OAuth2, OpenID Connect)

**Key Differences:**
- **Purpose**: OAuth2 for authorization, SSO for authentication
- **Scope**: OAuth2 for API access, SSO for application access
- **Implementation**: OAuth2 is protocol, SSO is patterned
- **Use case**: OAuth2 for third-party access, SSO for enterprise applications

**Integration:**
- OpenID Connect combines OAuth2 with SSO
- OAuth2 can be used to implement SSO solutions

## Securing REST APIs in Spring Boot?

**1. Spring Security Configuration:**
- Add Spring Security dependency
- Configure security filter chain
- Define authentication and authorization rules

**2. Authentication Mechanisms:**
- Basic Authentication
- JWT tokens
- OAuth2/OpenID Connect
- API keys

**3. Authorization:**
- Method-level security (@PreAuthorize, @PostAuthorize)
- URL-based security
- Role-based access control (RBAC)

**4. Input Validation:**
- Validate all input parameters
- Use @Valid annotations
- Custom validators for business rules
- Prevent SQL injection, XSS

**5. HTTPS:**
- Enable SSL/TLS
- Configure security headers
- Use secure cookies

**6. Rate Limiting:**
- Prevent abuse and DoS attacks
- Implement throttling mechanisms

**7. Error Handling:**
- Don't expose sensitive information
- Generic error messages
- Proper HTTP status codes

## Spring Security purpose?

**Core Functions:**
1. **Authentication**: User identity verification
2. **Authorization**: Access control and permissions
3. **Protection**: Against common security vulnerabilities
4. **Integration**: With various authentication systems

**Key Features:**
- Comprehensive security framework
- Declarative security (annotations)
- Integration with Spring ecosystem
- Extensible architecture
- Support for various authentication methods

**Components:**
- **SecurityFilterChain**: Request processing pipeline
- **AuthenticationManager**: Handles authentication
- **AccessDecisionManager**: Authorization decisions
- **UserDetailsService**: Load user information
- **PasswordEncoder**: Password hashing

## Preventing SQL Injection and XSS?

**SQL Injection Prevention:**

**1. Parameterized Queries:**
```java
String query = "SELECT * FROM users WHERE id = ?";
PreparedStatement stmt = connection.prepareStatement(query);
stmt.setLong(1, userId);
```


**2. ORM Usage:**
- Use JPA/Hibernate instead of raw SQL
- Automatic parameterization

**3. Input Validation:**
- Validate and sanitize all inputs
- Whitelist allowed characters
- Use proper data types

**4. Stored Procedures:**
- Encapsulate database logic
- Limited SQL execution context

**XSS Prevention:**

**1. Output Encoding:**
- Encode data before displaying
- Context-specific encoding (HTML, JavaScript, URL)

**2. Input Validation:**
- Validate on the server side
- Reject suspicious input
- Use allowlists, not deny-lists

**3. Content Security Policy (CSP):**
- Control resource loading
- Prevent inline scripts
- Report violations

**4. Security Headers:**
- X-XSS-Protection
- X-Content-Type-Options
- X-Frame-Options

**5. Template Engines:**
- Use frameworks that auto-escape (Thymeleaf, JSP with JSTL)
- Avoid innerHTML manipulation

# 7. **REST APIs & Microservices**

## REST architecture principles?

**1. Client-Server Architecture:**
- Separation of concerns
- Client handles UI, server handles data
- Independent evolution of components

**2. Stateless:**
- Each request contains all necessary information
- Server doesn't store client context
- Improves scalability and reliability

**3. Cacheable:**
- Responses should be cacheable when appropriate
- Improves performance and scalability
- Use proper HTTP cache headers

**4. Uniform Interface:**
- Consistent interaction patterns
- Resource identification through URIs
- Manipulation through representations
- Self-descriptive messages
- HATEOAS (Hypermedia as the Engine of Application State)

**5. Layered System:**
- Architecture can have intermediary layers
- Load balancers, proxies, caches
- Each layer only knows about adjacent layers

**6. Code on Demand (Optional):**
- Server can send executable code to client
- JavaScript, applets
- Rarely used in practice

## Idempotent HTTP methods?

**Idempotent Methods:**
- Multiple identical requests have the same effect as a single request
- Safe to retry without side effects

**GET:**
- Retrieves data
- Should not modify the server state
- Cacheable and safe

**PUT:**
- Creates or completely replaces resource
- Same result regardless of number of calls
- Should be idempotent by design

**DELETE:**
- Removes resource
- Multiple deletes have same result
- Resource remains deleted

**HEAD:**
- Like GET but returns only headers
- Used for checking resource existence/metadata

**OPTIONS:**
- Returns supported methods for resource
- Used in CORS preflight requests

**Non-idempotent Methods:**
- **POST**: Creates resources, may have different results
- **PATCH**: Partial updates, may not be idempotent

## REST API versioning strategies?

**1. URI Versioning:**
```
/api/v1/users
/api/v2/users
```

- Most common and visible
- Easy to implement and understand
- Can lead to URI proliferation

**2. Header Versioning:**
```
Accept: application/vnd.api.v1+json
API-Version: v1
```

- Clean URIs
- More complex to implement
- Harder to test with browser

**3. Query Parameter:**
```
/api/users?version=1
```

- Simple to implement
- Easy to test
- Can be ignored by clients

**4. Media Type Versioning:**
```
Accept: application/vnd.company.app-v1+json
```

- RESTful approach
- Complex implementation
- Good for content negotiation

**Best Practices:**
- Choose one strategy and stick to it
- Version only when breaking changes occur
- Support multiple versions temporarily
- Communicate deprecation timeline
- Use semantic versioning

## SOAP vs REST?

**SOAP (Simple Object Access Protocol):**
- Protocol with strict standards
- XML-based messaging
- WSDL for service description
- Built-in security (WS-Security)
- ACID compliance support
- Language and platform agnostic
- Heavy overhead

**REST (Representational State Transfer):**
- Architectural style
- Multiple data formats (JSON, XML, HTML)
- Lightweight and fast
- Uses standard HTTP methods
- Stateless communication
- Better performance
- Simpler to implement

**When to use SOAP:**
- Enterprise applications
- Formal contracts required
- ACID transactions needed
- Advanced security requirements
- Legacy system integration

**When to use REST:**
- Web applications and mobile apps
- Performance is critical
- Simple operations
- Rapid development needed
- Modern microservices

## HATEOAS in REST?

**HATEOAS (Hypermedia as the Engine of Application State):**
- REST constraint requiring hypermedia links
- Client discovers available actions through links
- Reduces client-server coupling
- Self-documenting APIs

**Example Response:**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "_links": {
    "self": { "href": "/users/1" },
    "edit": { "href": "/users/1" },
    "delete": { "href": "/users/1" },
    "orders": { "href": "/users/1/orders" }
  }
}
```


**Benefits:**
- Discoverability of API functionality
- Reduced documentation needs
- Flexible client implementation
- Server can guide client behavior

**Challenges:**
- Increased response size
- Complex client implementation
- Not widely adopted
- Additional overhead

## Microservices communication?

**Synchronous Communication:**

**1. REST APIs:**
- HTTP-based communication
- Request-response pattern
- Simple and widely supported
- Can cause cascading failures

**2. GraphQL:**
- Single endpoint for all data needs
- Client specifies required data
- Reduces over-fetching
- Complex server implementation

**Asynchronous Communication:**

**1. Message Queues:**
- **Apache Kafka**: High-throughput, distributed streaming
- **RabbitMQ**: Reliable message broker with routing
- **Amazon SQS**: Managed message queue service

**2. Event-Driven Architecture:**
- Services publish events
- Other services subscribe to relevant events
- Loose coupling between services
- Better fault tolerance

**Communication Patterns:**
- **Request-Response**: Direct service calls
- **Publish-Subscribe**: Event broadcasting
- **Message Queues**: Asynchronous task processing
- **Event Sourcing**: State changes as events

**Considerations:**
- **Service Discovery**: How services find each other
- **Load Balancing**: Distribute requests across instances
- **Circuit Breakers**: Prevent cascading failures
- **Retry Mechanisms**: Handle transient failures

## Circuit Breaker pattern implementation?

**Purpose:**
- Prevent cascading failures in distributed systems
- Fail fast when downstream service is unavailable
- Allow system recovery without overwhelming failed service

**States:**
1. **Closed**: Normal operation, requests pass through
2. **Open**: Service is down, requests fail immediately
3. **Half-Open**: Testing if service is recovered

**Implementation with Resilience4j:**

**Configuration:**
```java
@Component
public class ServiceA {
    
    @CircuitBreaker(name = "serviceB", fallbackMethod = "fallbackMethod")
    public String callServiceB() {
        // Call to external service
        return restTemplate.getForObject("/serviceB/data", String.class);
    }
    
    public String fallbackMethod(Exception ex) {
        return "Fallback response due to: " + ex.getMessage();
    }
}
```


**Configuration Properties:**
```yaml
resilience4j:
  circuitbreaker:
    instances:
      serviceB:
        failure-rate-threshold: 50
        wait-duration-in-open-state: 30s
        sliding-window-size: 10
        minimum-number-of-calls: 5
```


**Benefits:**
- Prevents resource exhaustion
- Faster failure detection
- Automatic recovery testing
- System stability improvement

**Best Practices:**
- Set appropriate thresholds
- Implement meaningful fallbacks
- Monitor circuit breaker metrics
- Combine with retry and timeout patterns

# 8. **Software Engineering Practices**

## SOLID principles?

**S - Single Responsibility Principle:**
- Class should have only one reason to change
- Each class should have a single, well-defined responsibility
- Promotes high cohesion
- Example: Separate data access from business logic

**O - Open/Closed Principle:**
- Open for extension, closed for modification
- Add new functionality without changing existing code
- Use inheritance, interfaces, composition
- Example: Strategy pattern for different algorithms

**L - Liskov Substitution Principle:**
- Objects of superclass should be replaceable with objects of subclass
- Subclasses must honor contracts of the parent class
- Prevents inheritance misuse
- Example: Square-Rectangle problem

**I - Interface Segregation Principle:**
- Clients shouldn't depend on interfaces they don't use
- Create specific, focused interfaces
- Prevents fat interfaces
- Example: Separate read and write interfaces

**D - Dependency Inversion Principle:**
- High-level modules shouldn't depend on low-level modules
- Both should depend on abstractions
- Abstractions shouldn't depend on details
- Example: Depend on interfaces, not concrete classes

## Clean Code practices?

**1. Meaningful Names:**
- Use intention-revealing names
- Avoid mental mapping
- Use searchable names
- Avoid misleading names

**2. Functions:**
- Small and focused (do one thing)
- Descriptive names
- Minimal parameters (ideally 0-2)
- No side effects
- Return early, avoid deep nesting

**3. Comments:**
- Code should be self-documenting
- Good code > comments
- Explain why, not what
- Keep comments up-to-date

**4. Formatting:**
- Consistent indentation and spacing
- Logical grouping of related code
- Proper line breaks and blank lines
- Team-agreed formatting standards

**5. Error Handling:**
- Use exceptions, not error codes
- Write try-catch-finally blocks first
- Provide context with exceptions
- Don't return null

**6. Classes:**
- Small and focused
- Single responsibility
- Minimal public interface
- High cohesion, low coupling

## SDLC (Software Development Lifecycle)?

**Phases:**

**1. Planning:**
- Requirements gathering
- Feasibility analysis
- Resource allocation
- Timeline estimation

**2. Analysis:**
- Detailed requirements analysis
- System architecture design
- Technology stack selection
- Risk assessment

**3. Design:**
- System design
- Database design
- UI/UX design
- API design

**4. Implementation:**
- Coding based on design
- Code reviews
- Unit testing
- Integration

**5. Testing:**
- System testing
- User acceptance testing
- Performance testing
- Security testing

**6. Deployment:**
- Production deployment
- Environment configuration
- Data migration
- Go-live activities

**7. Maintenance:**
- Bug fixes
- Feature enhancements
- Performance optimization
- Security updates

**SDLC Models:**
- **Waterfall**: Sequential phases
- **Agile**: Iterative and incremental
- **DevOps**: Continuous integration/deployment
- **Spiral**: Risk-driven approach

## Agile vs Scrum vs Kanban?

**Agile:**
- Software development methodology
- Values individuals, working software, customer collaboration, responding to change
- Principles: early delivery, welcome change, frequent delivery, collaboration
- Framework for iterative development

**Scrum:**
- Agile framework implementation
- Fixed-length sprints (1-4 weeks)
- Defined roles: Product Owner, Scrum Master, Development Team
- Ceremonies: Sprint Planning, Daily Standup, Sprint Review, Retrospective
- Artifacts: Product Backlog, Sprint Backlog, Increment

**Kanban:**
- Visual workflow management
- Continuous flow (no fixed sprints)
- Work-in-progress (WIP) limits
- Pull-based system
- Focus on continuous improvement
- Visual board with columns (To Do, In Progress, Done)

**Key Differences:**
- **Structure**: Scrum has fixed sprints, Kanban is continuous
- **Roles**: Scrum has defined roles, Kanban is flexible
- **Planning**: Scrum plans in sprints, Kanban plans continuously
- **Changes**: Scrum resists changes within sprint, Kanban welcomes changes anytime

## Code review practices?

**What to Look For:**

**1. Functionality:**
- Does code solve the intended problem?
- Are edge cases handled?
- Is error handling appropriate?
- Are there potential bugs?

**2. Design:**
- Is code well-structured?
- Does it follow design patterns appropriately?
- Is it maintainable and extensible?
- Are SOLID principles followed?

**3. Code Quality:**
- Is code readable and self-documenting?
- Are naming conventions followed?
- Is there code duplication?
- Are functions and classes appropriately sized?

**4. Performance:**
- Are there performance bottlenecks?
- Is resource usage appropriate?
- Are algorithms efficient?
- Is caching used where beneficial?

**5. Security:**
- Are there security vulnerabilities?
- Is input validation proper?
- Are credentials hardcoded?
- Is data properly encrypted?

**Best Practices:**
- Review small, focused changes
- Provide constructive feedback
- Focus on code, not the person
- Use automated tools for style/syntax
- Be thorough but efficient
- Follow up on requested changes

## Handling technical debt?

**What is Technical Debt:**
- Code shortcuts that need to be addressed later
- Compromises made due to time constraints
- Accumulated maintenance burden
- Impact on future development speed

**Types:**
1. **Deliberate**: Conscious decisions to ship faster
2. **Accidental**: Poor design due to lack of knowledge
3. **Environmental**: Changes in requirements/technology

**Management Strategies:**

**1. Identification:**
- Code quality metrics
- Developer feedback
- Performance monitoring
- Regular code audits

**2. Prioritization:**
- Impact on business functionality
- Cost of delay
- Risk assessment
- Development team productivity

**3. Allocation:**
- Dedicate percentage of sprint capacity (20% rule)
- Technical debt sprints
- Continuous refactoring
- Include in story estimation

**4. Prevention:**
- Code reviews
- Pair programming
- Automated testing
- Clear coding standards
- Documentation

**5. Tracking:**
- Technical debt register
- Regular assessment meetings
- Metrics and KPIs
- Progress reporting to stakeholders

**Communication:**
- Explain business impact
- Quantify risks and costs
- Propose gradual improvement plan
- Get stakeholder buy-in

This comprehensive overview covers all the major Java interview topics with detailed explanations and practical insights that would be valuable for interview preparation.