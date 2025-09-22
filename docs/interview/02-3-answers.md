I'll provide detailed answers to these Spring Boot questions:

## **Q1: Key Benefits of Spring Boot vs Plain Spring**

**Spring Boot Advantages:**

**1. Auto-Configuration**
- Automatically configures beans based on classpath dependencies
- Eliminates hundreds of lines of XML/Java configuration
- Smart defaults that work out-of-the-box

**2. Embedded Server**
- Built-in Tomcat, Jetty, or Undertow
- No need for external application servers
- JAR packaging with `java -jar` execution

**3. Starter Dependencies**
- Curated dependency sets (spring-boot-starter-web, spring-boot-starter-data-jpa)
- Eliminates dependency version conflicts
- Reduces Maven/Gradle configuration complexity

**4. Production-Ready Features**
- Built-in health checks, metrics, monitoring endpoints
- Actuator for application management
- External configuration support

**5. Developer Experience**
- Spring Boot DevTools for hot reloading
- Faster development cycle
- Comprehensive testing support

**Plain Spring requires:**
- Manual configuration of all components
- External server deployment
- Manual dependency management
- Custom monitoring and health check implementation

## **Q2: Dependency Injection in Spring**

**Dependency Injection** is a design pattern where objects don't create their dependencies - instead, dependencies are provided (injected) from external sources.

**Spring Implementation:**

```java
// Without DI (tight coupling)
public class OrderService {
    private PaymentService paymentService = new CreditCardPaymentService(); // Hard-coded dependency
}

// With Spring DI (loose coupling)
@Service
public class OrderService {
    private final PaymentService paymentService;
    
    // Constructor injection (recommended)
    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}

@Service
public class CreditCardPaymentService implements PaymentService {
    // Implementation
}
```


**Spring DI Mechanisms:**

**1. Constructor Injection (Recommended)**
```java
@Service
public class CustomerService {
    private final CustomerRepository repository;
    private final EmailService emailService;
    
    public CustomerService(CustomerRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }
}
```


**2. Field Injection**
```java
@Service
public class CustomerService {
    @Autowired
    private CustomerRepository repository;
}
```


**3. Setter Injection**
```java
@Service
public class CustomerService {
    private CustomerRepository repository;
    
    @Autowired
    public void setRepository(CustomerRepository repository) {
        this.repository = repository;
    }
}
```


**Benefits:**
- **Testability**: Easy to mock dependencies
- **Flexibility**: Can swap implementations
- **Loose Coupling**: Classes don't create their dependencies

## **Q3: @Component vs @Service vs @Repository**

These are **stereotype annotations** - all are specializations of `@Component`:

**@Component** - Generic stereotype for Spring-managed components
```java
@Component
public class EmailValidator {
    public boolean isValid(String email) {
        return email.contains("@");
    }
}
```


**@Service** - Business logic layer
```java
@Service
public class AccountService {
    public void transferMoney(Account from, Account to, BigDecimal amount) {
        // Business logic
    }
}
```


**@Repository** - Data access layer
```java
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByLastName(String lastName);
}
```


**Why Not Just Use @Component?**

**1. Semantic Clarity**
- Code is self-documenting
- Immediately understand the layer responsibility
- Better architecture visualization

**2. Spring's Special Processing**
- `@Repository` provides **exception translation** (converts database-specific exceptions to Spring's DataAccessException hierarchy)
- Future Spring versions may add layer-specific features

**3. AOP and Tooling**
- IDEs can provide layer-specific assistance
- AOP pointcuts can target specific stereotypes
- Monitoring tools can categorize by layer

**4. Team Communication**
- Clear architectural boundaries
- Easier code reviews and maintenance

## **Q4: @Configuration and @Bean**

**@Configuration** marks a class as a source of bean definitions:

```java
@Configuration
public class DatabaseConfig {
    
    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/erp");
        config.setUsername("admin");
        config.setPassword("password");
        return new HikariDataSource(config);
    }
    
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    @Bean
    @Primary
    public PaymentService primaryPaymentService() {
        return new CreditCardPaymentService();
    }
}
```


**Key Roles:**

**1. Third-party Library Integration**
- Configure beans from external libraries
- Libraries that don't have Spring annotations

**2. Complex Bean Creation**
- Beans requiring complex initialization
- Conditional bean creation

**3. Multiple Implementations**
```java
@Configuration
public class PaymentConfig {
    
    @Bean("creditCardPayment")
    public PaymentService creditCardPayment() {
        return new CreditCardPaymentService();
    }
    
    @Bean("paypalPayment")
    public PaymentService paypalPayment() {
        return new PayPalPaymentService();
    }
}
```


**4. Environment-Specific Configuration**
```java
@Configuration
@Profile("production")
public class ProductionConfig {
    
    @Bean
    public SecurityConfig securityConfig() {
        return new StrictSecurityConfig();
    }
}
```


## **Q5: Application Configuration Management**

**1. Properties Files**
```yaml
# application.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/erp
    username: ${DB_USER:admin}
    password: ${DB_PASSWORD:defaultpass}
  
server:
  port: 8080

# Custom properties
app:
  payment:
    timeout: 30
  email:
    enabled: true
```


**2. Profile-Specific Configuration**
```yaml
# application-dev.yml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
logging:
  level:
    com.example: DEBUG

# application-prod.yml
spring:
  datasource:
    url: ${DATABASE_URL}
logging:
  level:
    root: WARN
```


**3. Configuration Properties Classes**
```java
@ConfigurationProperties(prefix = "app.payment")
@Component
public class PaymentProperties {
    private int timeout = 30;
    private boolean retryEnabled = true;
    private String defaultGateway;
    
    // getters and setters
}

@Service
public class PaymentService {
    private final PaymentProperties properties;
    
    public PaymentService(PaymentProperties properties) {
        this.properties = properties;
    }
}
```


**4. External Configuration**
```shell script
# Command line
java -jar app.jar --spring.profiles.active=prod --server.port=9090

# Environment variables
export SPRING_DATASOURCE_URL=jdbc:postgresql://prod-db:5432/erp
export SPRING_PROFILES_ACTIVE=production
```


**5. Secrets Management**
```yaml
# Using Spring Cloud Config or external secret management
spring:
  cloud:
    config:
      uri: http://config-server:8888
  datasource:
    password: ${vault.secret.database.password}
```


## **Q6: @RestController vs @Controller**

**@Controller** - Traditional Spring MVC controller
```java
@Controller
public class CustomerController {
    
    @GetMapping("/customers")
    public String listCustomers(Model model) {
        model.addAttribute("customers", customerService.findAll());
        return "customer-list"; // Returns view name
    }
    
    @PostMapping("/customers")
    public String createCustomer(@ModelAttribute Customer customer) {
        customerService.save(customer);
        return "redirect:/customers"; // Redirect to view
    }
}
```


**@RestController** - RESTful API controller (@Controller + @ResponseBody)
```java
@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {
    
    @GetMapping
    public List<Customer> listCustomers() {
        return customerService.findAll(); // Automatically serialized to JSON
    }
    
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer saved = customerService.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
```


**Key Differences:**
- **@Controller**: Returns view names (HTML pages)
- **@RestController**: Returns data (JSON/XML) - all methods have implicit @ResponseBody
- **@Controller**: Used for traditional web applications
- **@RestController**: Used for REST APIs and SPAs

## **Q7: Spring Boot Auto-Configuration**

Auto-configuration automatically configures Spring applications based on:

**1. Classpath Dependencies**
```java
// If spring-boot-starter-data-jpa is on classpath
@ConditionalOnClass(DataSource.class)
@EnableAutoConfiguration
public class DataSourceAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource() {
        // Create default DataSource
    }
}
```


**2. Conditional Annotations**
- `@ConditionalOnClass`: Bean created if class exists on classpath
- `@ConditionalOnMissingBean`: Bean created if no other bean of same type
- `@ConditionalOnProperty`: Bean created based on property values

**3. How It Works**
```java
@SpringBootApplication
public class Application {
    // @EnableAutoConfiguration is included in @SpringBootApplication
}
```


**4. Spring Factories Mechanism**
```properties
# META-INF/spring.factories
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration,\
org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
```


**5. Customization**
```java
// Exclude specific auto-configurations
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Application {
}

// Override with custom bean
@Configuration
public class CustomConfig {
    
    @Bean
    @Primary
    public DataSource customDataSource() {
        return new CustomDataSource();
    }
}
```


## **Q8: Spring Transaction Management with @Transactional**

**How Spring Manages Transactions:**

Spring uses **AOP (Aspect-Oriented Programming)** to wrap methods with transaction management code.

**Banking System Example:**

```java
@Service
@Transactional(readOnly = true) // Default for all methods
public class BankingService {
    
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AuditService auditService;
    
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE)
    public void transferMoney(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        // 1. Validate accounts exist
        Account fromAccount = accountRepository.findById(fromAccountId)
            .orElseThrow(() -> new AccountNotFoundException(fromAccountId));
        Account toAccount = accountRepository.findById(toAccountId)
            .orElseThrow(() -> new AccountNotFoundException(toAccountId));
        
        // 2. Check sufficient balance
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
        
        // 3. Perform transfer
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
        
        // 4. Save updated accounts
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        
        // 5. Record transaction
        BankTransaction transaction = new BankTransaction(
            fromAccountId, toAccountId, amount, TransactionType.TRANSFER
        );
        transactionRepository.save(transaction);
        
        // 6. Audit log
        auditService.logTransfer(fromAccountId, toAccountId, amount);
        
        // If any step fails, entire transaction is rolled back
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processInterest(Long accountId, BigDecimal interestRate) {
        // Runs in separate transaction - won't be affected by parent rollback
        Account account = accountRepository.findById(accountId).get();
        BigDecimal interest = account.getBalance().multiply(interestRate);
        account.setBalance(account.getBalance().add(interest));
        accountRepository.save(account);
    }
    
    @Transactional(rollbackFor = {BusinessException.class})
    public void processLoanPayment(Long loanId, BigDecimal paymentAmount) {
        // Custom rollback conditions
        Loan loan = loanRepository.findById(loanId).get();
        
        if (paymentAmount.compareTo(loan.getMinimumPayment()) < 0) {
            throw new BusinessException("Payment below minimum"); // Will cause rollback
        }
        
        loan.setOutstandingBalance(loan.getOutstandingBalance().subtract(paymentAmount));
        loanRepository.save(loan);
    }
}
```


**Key Transaction Attributes:**

**1. Isolation Levels**
- `READ_UNCOMMITTED`: Allows dirty reads
- `READ_COMMITTED`: Prevents dirty reads
- `REPEATABLE_READ`: Prevents dirty and non-repeatable reads
- `SERIALIZABLE`: Full isolation (used for critical financial operations)

**2. Propagation Types**
- `REQUIRED` (default): Use existing transaction or create new one
- `REQUIRES_NEW`: Always create new transaction
- `SUPPORTS`: Use existing transaction if available
- `MANDATORY`: Must run within existing transaction

**3. Rollback Rules**
```java
@Transactional(
    rollbackFor = {BusinessException.class, DataIntegrityException.class},
    noRollbackFor = {ValidationException.class}
)
public void processPayment() {
    // Custom rollback behavior
}
```


**Real-World Banking Considerations:**
- **ACID Properties**: All banking operations must be atomic, consistent, isolated, and durable
- **Optimistic Locking**: Use `@Version` to handle concurrent account updates
- **Compensation Transactions**: For distributed systems, implement saga patterns
- **Audit Trails**: All financial transactions must be logged and immutable
- **Timeout Settings**: Set appropriate transaction timeouts for different operation types

Spring's transaction management ensures data consistency in complex banking operations where multiple database operations must succeed or fail together.