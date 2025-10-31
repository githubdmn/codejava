I'll answer these Java & OOP Design questions based on best practices for ERP/business applications:

## **Q1: Key OOP Principles in ERP/Business Applications**

The most important OOP principles I apply in ERP systems are:

**Encapsulation**: Critical for protecting sensitive business data like account balances, customer information, and financial records. I use private fields with controlled access through getters/setters and validation.

**Abstraction**: Essential for creating clean interfaces between business layers. For example, abstracting payment processing behind interfaces so we can support multiple payment gateways without changing business logic.

**Inheritance**: Used for modeling business hierarchies like different account types (SavingsAccount, CheckingAccount extending Account) or employee roles (Manager, Clerk extending Employee).

**Polymorphism**: Enables flexible business rule processing. Different customer types can have different discount calculation strategies, all implementing the same interface but with varying behaviors.

**SOLID Principles**: Particularly important in ERP:
- **Single Responsibility**: Each class handles one business concern
- **Open/Closed**: Easy to add new business rules without modifying existing code
- **Dependency Inversion**: Business logic doesn't depend on specific implementations

## **Q2: Recent Java Design Pattern Example**

**Strategy Pattern** - I recently used this for implementing different pricing strategies in an ERP system.

```java
public interface PricingStrategy {
    BigDecimal calculatePrice(Order order, Customer customer);
}

@Component
public class RegularPricingStrategy implements PricingStrategy {
    public BigDecimal calculatePrice(Order order, Customer customer) {
        return order.getBasePrice();
    }
}

@Component
public class VIPPricingStrategy implements PricingStrategy {
    public BigDecimal calculatePrice(Order order, Customer customer) {
        return order.getBasePrice().multiply(new BigDecimal("0.85")); // 15% discount
    }
}

@Service
public class PricingService {
    public BigDecimal calculateFinalPrice(Order order, Customer customer) {
        PricingStrategy strategy = getPricingStrategy(customer.getType());
        return strategy.calculatePrice(order, customer);
    }
}
```


**Why Strategy Pattern?**
- **Flexibility**: Easy to add new pricing rules without modifying existing code
- **Testability**: Each pricing strategy can be tested independently
- **Business Rule Separation**: Pricing logic is cleanly separated from order processing
- **Runtime Selection**: Can dynamically choose pricing based on customer type, season, etc.

## **Q3: Checked vs Unchecked Exceptions**

**Checked Exceptions**:
- Must be declared in method signature or handled with try-catch
- Used for recoverable conditions
- Examples: `IOException`, `SQLException`, `ClassNotFoundException`

**Unchecked Exceptions**:
- Extend `RuntimeException`
- Don't need to be declared or caught
- Usually indicate programming errors
- Examples: `NullPointerException`, `IllegalArgumentException`

**When to Create Custom Exceptions**:

```java
// Business-specific checked exception
public class InsufficientBalanceException extends Exception {
    private final BigDecimal requestedAmount;
    private final BigDecimal availableBalance;
    
    public InsufficientBalanceException(BigDecimal requested, BigDecimal available) {
        super(String.format("Insufficient balance. Requested: %s, Available: %s", 
              requested, available));
        this.requestedAmount = requested;
        this.availableBalance = available;
    }
}

// Business-specific unchecked exception
public class InvalidCustomerDataException extends RuntimeException {
    public InvalidCustomerDataException(String message) {
        super(message);
    }
}
```


**Create custom exceptions when**:
- You need specific business context in error handling
- Different recovery strategies are needed for different business errors
- You want to provide meaningful error messages to users
- Integration with error handling frameworks requires specific exception types

## **Q4: Thread-Safety in Multi-User ERP Systems**

For ensuring thread-safety when multiple operations update balances simultaneously:

**1. Database-Level Concurrency Control**:
```java
@Entity
public class Account {
    @Id
    private Long id;
    
    @Version  // Optimistic locking
    private Long version;
    
    private BigDecimal balance;
}

@Transactional
public void updateBalance(Long accountId, BigDecimal amount) {
    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new AccountNotFoundException(accountId));
    
    account.setBalance(account.getBalance().add(amount));
    accountRepository.save(account); // Version check prevents concurrent modifications
}
```


**2. Pessimistic Locking for Critical Operations**:
```java
@Query("SELECT a FROM Account a WHERE a.id = :id")
@Lock(LockModeType.PESSIMISTIC_WRITE)
Optional<Account> findByIdWithLock(@Param("id") Long id);

@Transactional
public void transferFunds(Long fromId, Long toId, BigDecimal amount) {
    // Lock accounts in consistent order to prevent deadlocks
    Long firstId = fromId < toId ? fromId : toId;
    Long secondId = fromId < toId ? toId : fromId;
    
    Account firstAccount = accountRepository.findByIdWithLock(firstId).get();
    Account secondAccount = accountRepository.findByIdWithLock(secondId).get();
    
    // Perform transfer logic
    if (fromId < toId) {
        performTransfer(firstAccount, secondAccount, amount);
    } else {
        performTransfer(secondAccount, firstAccount, amount);
    }
}
```


**3. Application-Level Synchronization**:
```java
@Service
public class AccountService {
    private final ConcurrentHashMap<Long, ReentrantLock> accountLocks = 
        new ConcurrentHashMap<>();
    
    public void updateAccountBalance(Long accountId, BigDecimal amount) {
        ReentrantLock lock = accountLocks.computeIfAbsent(accountId, 
            k -> new ReentrantLock());
        
        lock.lock();
        try {
            // Perform balance update
            performBalanceUpdate(accountId, amount);
        } finally {
            lock.unlock();
        }
    }
}
```


**4. Queue-Based Processing for High Concurrency**:
```java
@Component
public class BalanceUpdateProcessor {
    @Async("balanceUpdateExecutor")
    @Transactional
    public CompletableFuture<Void> processBalanceUpdate(BalanceUpdateRequest request) {
        // Process balance updates sequentially per account
        return CompletableFuture.completedFuture(null);
    }
}
```


**Best Practices for ERP Thread-Safety**:
- Use **optimistic locking** for most operations (better performance)
- Use **pessimistic locking** for critical financial operations
- Implement **consistent lock ordering** to prevent deadlocks
- Use **database transactions** with appropriate isolation levels
- Consider **event-driven architecture** for high-throughput scenarios
- Implement **audit trails** to track all balance changes
- Use **immutable objects** where possible to reduce shared mutable state

These approaches ensure data consistency while maintaining good performance in multi-user ERP environments.