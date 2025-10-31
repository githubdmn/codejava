I'll provide detailed answers to these JPA/Hibernate questions:

## **Q1: What is ORM? Why use JPA/Hibernate instead of plain JDBC?**

**ORM (Object-Relational Mapping)** is a technique that maps database tables to Java objects, allowing you to work with database data as if it were regular Java objects.

**JDBC Approach:**
```java
// Plain JDBC - verbose and error-prone
public Customer findCustomerById(Long id) {
    String sql = "SELECT c.id, c.first_name, c.last_name, c.email, " +
                "a.street, a.city, a.zip_code FROM customers c " +
                "LEFT JOIN addresses a ON c.address_id = a.id WHERE c.id = ?";
    
    try (Connection conn = dataSource.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setLong(1, id);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            Customer customer = new Customer();
            customer.setId(rs.getLong("id"));
            customer.setFirstName(rs.getString("first_name"));
            customer.setLastName(rs.getString("last_name"));
            customer.setEmail(rs.getString("email"));
            
            if (rs.getString("street") != null) {
                Address address = new Address();
                address.setStreet(rs.getString("street"));
                address.setCity(rs.getString("city"));
                address.setZipCode(rs.getString("zip_code"));
                customer.setAddress(address);
            }
            return customer;
        }
    } catch (SQLException e) {
        throw new DatabaseException("Error fetching customer", e);
    }
    return null;
}
```


**JPA/Hibernate Approach:**
```java
// JPA - clean and maintainable
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    private String email;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;
}

// Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Method automatically implemented
}

// Usage
Customer customer = customerRepository.findById(id).orElse(null);
```


**Benefits of JPA/Hibernate:**

**1. Productivity**
- **Less boilerplate code**: No manual SQL writing for basic operations
- **Automatic CRUD operations**: findById, save, delete work out of the box
- **Type safety**: Compile-time checking instead of runtime SQL errors

**2. Database Independence**
- **Dialect abstraction**: Same code works with PostgreSQL, MySQL, Oracle
- **Automatic DDL generation**: Can generate schema from entities
- **Portable queries**: JPQL works across different databases

**3. Advanced Features**
- **Caching**: First-level and second-level cache support
- **Lazy loading**: Load data only when needed
- **Dirty checking**: Automatic detection of changed entities
- **Connection pooling**: Built-in connection management

**4. Object-Oriented Benefits**
- **Inheritance mapping**: Map class hierarchies to database tables
- **Relationships**: Automatic navigation between related entities
- **Encapsulation**: Database logic encapsulated in entity classes

## **Q2: Lazy vs Eager Fetching**

**Eager Fetching** loads associated entities immediately:
```java
@Entity
public class Customer {
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    private List<Order> orders = new ArrayList<>();
}

// When you load customer, orders are loaded immediately
Customer customer = customerRepository.findById(1L).get();
// SQL: SELECT * FROM customers c JOIN orders o ON c.id = o.customer_id WHERE c.id = 1
```


**Lazy Fetching** loads associated entities only when accessed:
```java
@Entity
public class Customer {
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY) // Default for collections
    private List<Order> orders = new ArrayList<>();
}

Customer customer = customerRepository.findById(1L).get();
// SQL: SELECT * FROM customers WHERE id = 1

int orderCount = customer.getOrders().size(); // Now orders are loaded
// SQL: SELECT * FROM orders WHERE customer_id = 1
```


**When to Use Each:**

**Use Eager Fetching When:**
- **Always need the data**: Associated entities are used in 90%+ of cases
- **Small datasets**: Related data is limited and won't cause performance issues
- **Immediate display requirements**: Need to show related data right away

```java
@Entity
public class User {
    @OneToOne(fetch = FetchType.EAGER) // User profile always needed
    private UserProfile profile;
    
    @ManyToOne(fetch = FetchType.EAGER) // Department info usually displayed
    private Department department;
}
```


**Use Lazy Fetching When:**
- **Optional data**: Associated entities might not be needed
- **Large datasets**: Collections could contain many items
- **Performance critical**: Want to minimize initial query time
- **Conditional access**: Access depends on business logic

```java
@Entity
public class Customer {
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Order> orders; // Might have hundreds of orders
    
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<SupportTicket> supportTickets; // Rarely accessed
}
```


**Best Practices:**
- **Default to lazy** for collections (@OneToMany, @ManyToMany)
- **Use @EntityGraph** for query-specific fetching strategies
- **Avoid lazy loading outside transaction** (LazyInitializationException)

## **Q3: Hibernate Cache Levels**

**First-Level Cache (Session Cache):**
```java
@Transactional
public void demonstrateFirstLevelCache() {
    Customer customer1 = customerRepository.findById(1L).get(); // Database hit
    Customer customer2 = customerRepository.findById(1L).get(); // Cache hit - same instance
    
    assertThat(customer1).isSameAs(customer2); // True - same object reference
    
    customer1.setFirstName("Updated Name");
    // No need to call save() - automatic dirty checking will persist changes
}
```


**Characteristics of First-Level Cache:**
- **Scope**: Session/EntityManager level
- **Automatic**: Always enabled, cannot be disabled
- **Identity guarantee**: Same entity ID returns same object instance
- **Dirty checking**: Tracks changes automatically
- **Short-lived**: Cleared when session closes

**Second-Level Cache:**
```java
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product {
    @Id
    private Long id;
    private String name;
    private BigDecimal price;
}

// Configuration
spring.jpa.properties.hibernate.cache.use_second_level_cache=true
spring.jpa.properties.hibernate.cache.region.factory_class=org.hibernate.cache.ehcache.EhCacheRegionFactory
```


**Characteristics of Second-Level Cache:**
- **Scope**: SessionFactory level (application-wide)
- **Optional**: Must be explicitly enabled and configured
- **Shared**: Cached data shared across sessions
- **Persistent**: Survives session boundaries
- **Configurable**: Different strategies per entity

**Cache Strategies:**
```java
// Read-only data (reference data)
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Entity
public class Country {
    // Rarely changes, safe to cache
}

// Read-write data with occasional updates
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
public class Product {
    // Updated occasionally, needs cache synchronization
}

// Transactional data
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Entity
public class InventoryItem {
    // Requires distributed transaction support
}
```


**Key Differences:**
| Aspect | First-Level Cache | Second-Level Cache |
|--------|------------------|-------------------|
| **Scope** | Session | Application |
| **Lifetime** | Session duration | Application lifetime |
| **Sharing** | Single session | All sessions |
| **Configuration** | Always enabled | Must be configured |
| **Use Case** | Identity guarantee | Performance optimization |

## **Q4: Hibernate Entity States**

**Entity Lifecycle States:**

**1. Transient State**
```java
// Newly created object - not associated with Hibernate session
Customer customer = new Customer();
customer.setFirstName("John");
customer.setLastName("Doe");
// Object exists only in memory, not tracked by Hibernate
```


**2. Persistent State**
```java
@Transactional
public void persistentStateExample() {
    // Transient -> Persistent
    Customer customer = new Customer();
    customer.setFirstName("John");
    customerRepository.save(customer); // Now persistent
    
    // Or loading from database
    Customer existing = customerRepository.findById(1L).get(); // Persistent
    
    // Changes are automatically tracked
    existing.setFirstName("Updated Name"); // Dirty checking will save this
    // No need to call save() explicitly
}
```


**3. Detached State**
```java
@Transactional
public Customer getCustomer(Long id) {
    return customerRepository.findById(id).get(); // Persistent within transaction
}

public void updateCustomer() {
    Customer customer = getCustomer(1L); // Now detached (outside transaction)
    customer.setFirstName("New Name"); // Changes not tracked
    
    // To persist changes, need to merge
    customerRepository.save(customer); // Merge detached entity
}
```


**4. Removed State**
```java
@Transactional
public void removeCustomer(Long id) {
    Customer customer = customerRepository.findById(id).get(); // Persistent
    customerRepository.delete(customer); // Now in removed state
    
    // Entity scheduled for deletion but still in session
    // Will be deleted when transaction commits
}
```


**State Transitions:**
```java
@Service
@Transactional
public class CustomerService {
    
    public void demonstrateStateTransitions() {
        // 1. Transient
        Customer customer = new Customer("John", "Doe");
        
        // 2. Transient -> Persistent
        customerRepository.save(customer);
        
        // 3. Persistent -> Removed
        customerRepository.delete(customer);
        
        // 4. Create new and make detached
        Customer detached = new Customer("Jane", "Smith");
        detached.setId(999L); // Simulating detached entity
        
        // 5. Detached -> Persistent (merge)
        Customer merged = customerRepository.save(detached);
    }
}
```


## **Q5: Relationship Mappings**

**@OneToOne Mapping:**
```java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private UserProfile profile;
}

@Entity
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String bio;
    private String website;
    
    @OneToOne(mappedBy = "profile")
    private User user; // Bidirectional
}
```


**@OneToMany Mapping:**
```java
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();
    
    // Helper methods
    public void addOrder(Order order) {
        orders.add(order);
        order.setCustomer(this);
    }
}

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
```


**@ManyToMany Mapping:**
```java
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "student_course",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();
}

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToMany(mappedBy = "courses")
    private Set<Student> students = new HashSet<>();
}

// With additional attributes in join table
@Entity
public class StudentCourse {
    @EmbeddedId
    private StudentCourseId id;
    
    @ManyToOne
    @MapsId("studentId")
    private Student student;
    
    @ManyToOne
    @MapsId("courseId")
    private Course course;
    
    private LocalDate enrollmentDate;
    private BigDecimal grade;
}
```


## **Q6: N+1 Query Problem**

**The Problem:**
```java
// This creates N+1 queries
List<Customer> customers = customerRepository.findAll(); // 1 query
for (Customer customer : customers) {
    List<Order> orders = customer.getOrders(); // N queries (one per customer)
    System.out.println(customer.getName() + " has " + orders.size() + " orders");
}

// If there are 100 customers, this executes 101 queries!
```


**Solutions:**

**1. Fetch Join (JPQL)**
```java
@Query("SELECT c FROM Customer c LEFT JOIN FETCH c.orders")
List<Customer> findAllWithOrders();

// Or with criteria
public List<Customer> findCustomersWithOrders() {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Customer> query = cb.createQuery(Customer.class);
    Root<Customer> root = query.from(Customer.class);
    root.fetch("orders", JoinType.LEFT);
    return entityManager.createQuery(query).getResultList();
}
```


**2. Entity Graph**
```java
@Entity
@NamedEntityGraph(
    name = "Customer.orders",
    attributeNodes = @NamedAttributeNode("orders")
)
public class Customer { ... }

// Repository method
@EntityGraph("Customer.orders")
@Query("SELECT c FROM Customer c")
List<Customer> findAllWithOrdersUsingEntityGraph();

// Or dynamic entity graph
public List<Customer> findCustomersWithOrdersAndItems() {
    EntityGraph<Customer> entityGraph = entityManager.createEntityGraph(Customer.class);
    entityGraph.addAttributeNodes("orders");
    entityGraph.addSubgraph("orders").addAttributeNodes("orderItems");
    
    return entityManager.createQuery("SELECT c FROM Customer c", Customer.class)
        .setHint("javax.persistence.fetchgraph", entityGraph)
        .getResultList();
}
```


**3. Batch Fetching**
```java
@Entity
public class Customer {
    @OneToMany(mappedBy = "customer")
    @BatchSize(size = 10) // Load orders for 10 customers at once
    private List<Order> orders;
}

# Configuration
hibernate.default_batch_fetch_size=16
```


**4. Projection Queries**
```java
// Custom projection to load only needed data
@Query("SELECT new com.example.dto.CustomerOrderSummary(c.id, c.name, COUNT(o)) " +
       "FROM Customer c LEFT JOIN c.orders o GROUP BY c.id, c.name")
List<CustomerOrderSummary> findCustomerOrderSummaries();

public class CustomerOrderSummary {
    private Long customerId;
    private String customerName;
    private Long orderCount;
    // constructors, getters
}
```


## **Q7: JPA Query Optimization**

**1. Fetch Joins**
```java
// Multiple relationships in single query
@Query("SELECT DISTINCT c FROM Customer c " +
       "LEFT JOIN FETCH c.orders o " +
       "LEFT JOIN FETCH o.orderItems oi " +
       "LEFT JOIN FETCH oi.product p")
List<Customer> findCustomersWithOrderDetails();
```


**2. Projections (DTOs)**
```java
// Load only required fields
@Query("SELECT new com.example.dto.CustomerSummaryDto(c.id, c.name, c.email) " +
       "FROM Customer c WHERE c.active = true")
List<CustomerSummaryDto> findActiveCustomerSummaries();

// Interface projections
public interface CustomerProjection {
    Long getId();
    String getName();
    String getEmail();
    
    @Value("#{target.firstName + ' ' + target.lastName}")
    String getFullName();
}

List<CustomerProjection> findByActiveTrue();
```


**3. Batch Fetching**
```java
@Entity
public class Order {
    @ManyToOne
    @BatchSize(size = 20) // Batch load customers
    private Customer customer;
    
    @OneToMany(mappedBy = "order")
    @BatchSize(size = 50) // Batch load order items
    private List<OrderItem> orderItems;
}
```


**4. Query Hints**
```java
@QueryHints({
    @QueryHint(name = "hibernate.cacheable", value = "true"),
    @QueryHint(name = "hibernate.fetch_size", value = "50"),
    @QueryHint(name = "hibernate.read_only", value = "true")
})
@Query("SELECT c FROM Customer c WHERE c.region = :region")
List<Customer> findByRegion(@Param("region") String region);
```


**5. Native Queries for Complex Operations**
```java
@Query(value = "SELECT c.*, " +
               "(SELECT COUNT(*) FROM orders o WHERE o.customer_id = c.id) as order_count " +
               "FROM customers c WHERE c.registration_date > :date",
       nativeQuery = true)
List<Object[]> findCustomersWithOrderCount(@Param("date") LocalDate date);
```


## **Q8: Loan Repayment System Design**

**Entity Design:**

```java
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    private String phoneNumber;
    
    @Embedded
    private Address address;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Loan> loans = new ArrayList<>();
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Version
    private Long version;
}

@Entity
@Table(name = "loans")
@NamedEntityGraph(
    name = "Loan.withPayments",
    attributeNodes = {
        @NamedAttributeNode("customer"),
        @NamedAttributeNode("payments")
    }
)
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal principalAmount;
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal outstandingBalance;
    
    @Column(nullable = false, precision = 5, scale = 4)
    private BigDecimal interestRate;
    
    @Column(nullable = false)
    private Integer termInMonths;
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal monthlyPaymentAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status;
    
    @Column(nullable = false)
    private LocalDate startDate;
    
    @Column(nullable = false)
    private LocalDate maturityDate;
    
    private LocalDate nextPaymentDue;
    
    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("paymentDate DESC")
    private List<Payment> payments = new ArrayList<>();
    
    @Version
    private Long version;
    
    // Business methods
    public BigDecimal calculateInterestPayment() {
        return outstandingBalance.multiply(interestRate).divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
    }
    
    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setLoan(this);
    }
}

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal principalAmount;
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal interestAmount;
    
    @Column(nullable = false)
    private LocalDateTime paymentDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;
    
    private String transactionReference;
    
    @Column(precision = 15, scale = 2)
    private BigDecimal balanceAfterPayment;
}

public enum LoanStatus {
    ACTIVE, PAID_OFF, DEFAULTED, RESTRUCTURED
}

public enum PaymentStatus {
    COMPLETED, PENDING, FAILED, REFUNDED
}

public enum PaymentMethod {
    BANK_TRANSFER, CREDIT_CARD, DEBIT_CARD, CHECK, CASH
}
```


**Optimized Repository Layer:**

```java
@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    
    // Efficient query with fetch join
    @EntityGraph("Loan.withPayments")
    @Query("SELECT l FROM Loan l WHERE l.customer.id = :customerId AND l.status = :status")
    List<Loan> findByCustomerIdAndStatus(@Param("customerId") Long customerId, 
                                        @Param("status") LoanStatus status);
    
    // Find loans requiring payment processing
    @Query("SELECT l FROM Loan l WHERE l.nextPaymentDue <= :date AND l.status = 'ACTIVE'")
    List<Loan> findLoansRequiringPayment(@Param("date") LocalDate date);
    
    // Portfolio summary with aggregation
    @Query("SELECT new com.example.dto.LoanPortfolioSummary(" +
           "COUNT(l), SUM(l.outstandingBalance), AVG(l.interestRate)) " +
           "FROM Loan l WHERE l.status = 'ACTIVE'")
    LoanPortfolioSummary getPortfolioSummary();
    
    // Customer loan summary
    @Query("SELECT new com.example.dto.CustomerLoanSummary(" +
           "c.id, c.firstName, c.lastName, COUNT(l), SUM(l.outstandingBalance)) " +
           "FROM Customer c LEFT JOIN c.loans l " +
           "WHERE l.status = 'ACTIVE' OR l IS NULL " +
           "GROUP BY c.id, c.firstName, c.lastName")
    List<CustomerLoanSummary> findCustomerLoanSummaries();
}

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    // Payment history for a loan
    @Query("SELECT p FROM Payment p WHERE p.loan.id = :loanId ORDER BY p.paymentDate DESC")
    List<Payment> findByLoanIdOrderByPaymentDateDesc(@Param("loanId") Long loanId);
    
    // Recent payments with loan and customer info
    @Query("SELECT p FROM Payment p " +
           "JOIN FETCH p.loan l " +
           "JOIN FETCH l.customer c " +
           "WHERE p.paymentDate >= :fromDate " +
           "ORDER BY p.paymentDate DESC")
    List<Payment> findRecentPaymentsWithDetails(@Param("fromDate") LocalDateTime fromDate);
    
    // Payment statistics
    @Query("SELECT new com.example.dto.PaymentStats(" +
           "COUNT(p), SUM(p.amount), AVG(p.amount)) " +
           "FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate")
    PaymentStats getPaymentStats(@Param("startDate") LocalDateTime startDate,
                                @Param("endDate") LocalDateTime endDate);
}
```


**Service Layer with Transaction Management:**

```java
@Service
@Transactional
public class LoanRepaymentService {
    
    private final LoanRepository loanRepository;
    private final PaymentRepository paymentRepository;
    private final NotificationService notificationService;
    
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Payment processPayment(Long loanId, BigDecimal paymentAmount, PaymentMethod method) {
        // Load with pessimistic lock to prevent concurrent modifications
        Loan loan = loanRepository.findById(loanId)
            .orElseThrow(() -> new LoanNotFoundException(loanId));
        
        validatePayment(loan, paymentAmount);
        
        // Calculate interest and principal portions
        BigDecimal interestPortion = loan.calculateInterestPayment();
        BigDecimal principalPortion = paymentAmount.subtract(interestPortion);
        
        if (principalPortion.compareTo(BigDecimal.ZERO) < 0) {
            principalPortion = paymentAmount;
            interestPortion = BigDecimal.ZERO;
        }
        
        // Create payment record
        Payment payment = new Payment();
        payment.setLoan(loan);
        payment.setAmount(paymentAmount);
        payment.setPrincipalAmount(principalPortion);
        payment.setInterestAmount(interestPortion);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentMethod(method);
        payment.setStatus(PaymentStatus.COMPLETED);
        
        // Update loan balance
        loan.setOutstandingBalance(loan.getOutstandingBalance().subtract(principalPortion));
        payment.setBalanceAfterPayment(loan.getOutstandingBalance());
        
        // Update next payment due date
        loan.setNextPaymentDue(loan.getNextPaymentDue().plusMonths(1));
        
        // Check if loan is paid off
        if (loan.getOutstandingBalance().compareTo(BigDecimal.ZERO) <= 0) {
            loan.setStatus(LoanStatus.PAID_OFF);
        }
        
        // Save entities
        paymentRepository.save(payment);
        loanRepository.save(loan);
        
        // Send notification
        notificationService.sendPaymentConfirmation(payment);
        
        return payment;
    }
    
    @Transactional(readOnly = true)
    public List<Loan> getCustomerActiveLoans(Long customerId) {
        return loanRepository.findByCustomerIdAndStatus(customerId, LoanStatus.ACTIVE);
    }
}
```


**Key Optimization Strategies:**

1. **Entity Graphs**: Pre-load related entities to avoid N+1 problems
2. **Versioning**: Use `@Version` for optimistic locking on financial data
3. **Indexing**: Add database indexes on frequently queried fields
4. **Batch Processing**: Process multiple payments in batches for performance
5. **Read-Only Transactions**: Use for reporting queries
6. **Projection DTOs**: Load only required data for summaries and reports
7. **Caching**: Cache reference data and frequently accessed loan details

This design ensures efficient queries, data integrity, and good performance for a loan repayment system handling potentially thousands of loans and payments.