I'll provide detailed answers to these REST API design questions:

## **Q1: REST API Design Best Practices**

**Naming Conventions:**

**1. Resource-Based URLs**
```java
// Good - Use nouns for resources
GET    /api/customers
POST   /api/customers
GET    /api/customers/{id}
PUT    /api/customers/{id}
DELETE /api/customers/{id}

// Bad - Using verbs
GET /api/getCustomers
POST /api/createCustomer
```


**2. Hierarchical Resource Relationships**
```java
// Nested resources
GET    /api/customers/{customerId}/orders
POST   /api/customers/{customerId}/orders
GET    /api/customers/{customerId}/orders/{orderId}

// Alternative for complex relationships
GET    /api/orders?customerId={customerId}
```


**3. Consistent Naming**
```java
// Use plural nouns consistently
/api/customers (not /api/customer)
/api/orders (not /api/order)

// Use kebab-case for compound words
/api/customer-profiles
/api/order-items
```


**Versioning Strategies:**

**1. URL Path Versioning (Recommended)**
```java
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerControllerV1 {
    
    @GetMapping("/{id}")
    public CustomerResponseV1 getCustomer(@PathVariable Long id) {
        // Version 1 implementation
    }
}

@RestController
@RequestMapping("/api/v2/customers")
public class CustomerControllerV2 {
    
    @GetMapping("/{id}")
    public CustomerResponseV2 getCustomer(@PathVariable Long id) {
        // Version 2 with enhanced fields
    }
}
```


**2. Header Versioning**
```java
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    
    @GetMapping(value = "/{id}", headers = "API-Version=1")
    public CustomerResponseV1 getCustomerV1(@PathVariable Long id) {
        // Version 1
    }
    
    @GetMapping(value = "/{id}", headers = "API-Version=2")
    public CustomerResponseV2 getCustomerV2(@PathVariable Long id) {
        // Version 2
    }
}
```


**Error Handling:**

```java
// Standardized error response structure
public class ApiError {
    private int status;
    private String error;
    private String message;
    private String timestamp;
    private String path;
    private List<ValidationError> validationErrors;
}

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return ApiError.builder()
            .status(404)
            .error("Resource Not Found")
            .message(ex.getMessage())
            .timestamp(Instant.now().toString())
            .path(request.getRequestURI())
            .build();
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidation(MethodArgumentNotValidException ex) {
        List<ValidationError> validationErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> new ValidationError(error.getField(), error.getDefaultMessage()))
            .collect(Collectors.toList());
            
        return ApiError.builder()
            .status(400)
            .error("Validation Failed")
            .message("Input validation errors")
            .validationErrors(validationErrors)
            .build();
    }
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrity(DataIntegrityViolationException ex) {
        return ApiError.builder()
            .status(409)
            .error("Data Conflict")
            .message("Resource already exists or constraint violation")
            .build();
    }
}

// HTTP Status Code Guidelines
// 200 OK - Successful GET, PUT
// 201 Created - Successful POST
// 204 No Content - Successful DELETE
// 400 Bad Request - Invalid input
// 401 Unauthorized - Authentication required
// 403 Forbidden - Access denied
// 404 Not Found - Resource doesn't exist
// 409 Conflict - Resource conflict
// 422 Unprocessable Entity - Business logic validation failed
// 500 Internal Server Error - Server error
```


## **Q2: Idempotent vs Non-Idempotent HTTP Methods**

**Idempotent Methods** - Multiple identical requests have the same effect as a single request:

**GET - Safe and Idempotent**
```java
@GetMapping("/customers/{id}")
public ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
    // No side effects, can be called multiple times safely
    Customer customer = customerService.findById(id);
    return ResponseEntity.ok(customer);
}
```


**PUT - Idempotent**
```java
@PutMapping("/customers/{id}")
public ResponseEntity<Customer> updateCustomer(
        @PathVariable Long id, 
        @RequestBody @Valid CustomerUpdateRequest request) {
    
    // Replace entire resource - same result regardless of how many times called
    Customer updated = customerService.updateCustomer(id, request);
    return ResponseEntity.ok(updated);
}

// Example: PUT /api/customers/123
// Body: {"name": "John Doe", "email": "john@example.com"}
// Calling this 5 times results in the same final state
```


**DELETE - Idempotent**
```java
@DeleteMapping("/customers/{id}")
public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
    customerService.deleteById(id);
    // First call deletes, subsequent calls have no effect (resource already gone)
    return ResponseEntity.noContent().build();
}
```


**Non-Idempotent Methods:**

**POST - Not Idempotent**
```java
@PostMapping("/customers")
public ResponseEntity<Customer> createCustomer(@RequestBody @Valid CustomerCreateRequest request) {
    // Each call creates a new resource
    Customer created = customerService.createCustomer(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
}

@PostMapping("/accounts/{accountId}/transactions")
public ResponseEntity<Transaction> createTransaction(
        @PathVariable Long accountId,
        @RequestBody @Valid TransactionRequest request) {
    
    // Each call creates a new transaction - NOT idempotent
    // Multiple calls could result in duplicate charges
    Transaction transaction = transactionService.processTransaction(accountId, request);
    return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
}
```


**Making POST Operations Safer with Idempotency Keys:**
```java
@PostMapping("/payments")
public ResponseEntity<Payment> createPayment(
        @RequestBody @Valid PaymentRequest request,
        @RequestHeader("Idempotency-Key") String idempotencyKey) {
    
    // Check if payment with this key already exists
    Optional<Payment> existing = paymentService.findByIdempotencyKey(idempotencyKey);
    if (existing.isPresent()) {
        return ResponseEntity.ok(existing.get()); // Return existing payment
    }
    
    // Create new payment
    Payment payment = paymentService.createPayment(request, idempotencyKey);
    return ResponseEntity.status(HttpStatus.CREATED).body(payment);
}
```


**PATCH - Generally Not Idempotent**
```java
@PatchMapping("/accounts/{id}/balance")
public ResponseEntity<Account> adjustBalance(
        @PathVariable Long id,
        @RequestBody BalanceAdjustment adjustment) {
    
    // Adding $100: first call adds $100, second call adds another $100
    // Result changes with each call - NOT idempotent
    Account updated = accountService.adjustBalance(id, adjustment.getAmount());
    return ResponseEntity.ok(updated);
}
```


## **Q3: Validation in Spring Boot APIs**

**Bean Validation with @Valid:**

```java
// DTO with validation annotations
public class CustomerCreateRequest {
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50)
    private String lastName;
    
    @NotNull(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Age should be at least 18")
    @Max(value = 120, message = "Age should be less than 120")
    private Integer age;
    
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String phoneNumber;
    
    @Valid // Nested validation
    private AddressRequest address;
}

// Controller with validation
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    
    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(
            @RequestBody @Valid CustomerCreateRequest request) {
        
        Customer customer = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(customer));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable @Min(1) Long id,
            @RequestBody @Valid CustomerUpdateRequest request) {
        
        Customer updated = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(toResponse(updated));
    }
}
```


**Custom Validators:**

```java
// Custom validation annotation
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueEmail {
    String message() default "Email already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

// Validator implementation
@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) return true; // Let @NotNull handle null validation
        return !customerRepository.existsByEmail(email);
    }
}

// Usage in DTO
public class CustomerCreateRequest {
    @NotNull
    @Email
    @UniqueEmail
    private String email;
}
```


**Global Exception Handling with @ControllerAdvice:**

```java
@RestControllerAdvice
public class ValidationExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(MethodArgumentNotValidException ex) {
        
        List<ValidationError> errors = new ArrayList<>();
        
        // Field errors
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.add(new ValidationError(
                error.getField(),
                error.getDefaultMessage(),
                error.getRejectedValue()
            ));
        });
        
        // Global errors
        ex.getBindingResult().getGlobalErrors().forEach(error -> {
            errors.add(new ValidationError(
                error.getObjectName(),
                error.getDefaultMessage(),
                null
            ));
        });
        
        return ApiError.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Validation Failed")
            .message("Request validation failed")
            .timestamp(Instant.now().toString())
            .validationErrors(errors)
            .build();
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleConstraintViolation(ConstraintViolationException ex) {
        
        List<ValidationError> errors = ex.getConstraintViolations()
            .stream()
            .map(violation -> new ValidationError(
                violation.getPropertyPath().toString(),
                violation.getMessage(),
                violation.getInvalidValue()
            ))
            .collect(Collectors.toList());
            
        return ApiError.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Constraint Violation")
            .message("Parameter validation failed")
            .validationErrors(errors)
            .build();
    }
}

// Enable method-level validation
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
}
```


**Business Logic Validation:**

```java
@Service
@Validated
public class CustomerService {
    
    public Customer createCustomer(@Valid CustomerCreateRequest request) {
        // Additional business validation
        validateCustomerBusinessRules(request);
        
        Customer customer = mapToEntity(request);
        return customerRepository.save(customer);
    }
    
    private void validateCustomerBusinessRules(CustomerCreateRequest request) {
        // Custom business validation
        if (request.getAge() < 21 && "PREMIUM".equals(request.getAccountType())) {
            throw new BusinessValidationException("Premium accounts require age 21 or older");
        }
        
        // Validate against external services
        if (!addressValidationService.isValidAddress(request.getAddress())) {
            throw new BusinessValidationException("Invalid address provided");
        }
    }
}

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class BusinessValidationException extends RuntimeException {
    public BusinessValidationException(String message) {
        super(message);
    }
}
```


## **Q4: Customer Invoices API Design**

**Complete Invoice API Design:**

```java
// Invoice DTOs
public class InvoiceCreateRequest {
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    @NotNull(message = "Due date is required")
    @FutureOrPresent(message = "Due date must be today or in the future")
    private LocalDate dueDate;
    
    private String description;
    
    @NotEmpty(message = "Invoice must have at least one line item")
    @Valid
    private List<InvoiceLineItemRequest> lineItems;
    
    private BigDecimal taxRate = BigDecimal.ZERO;
    private BigDecimal discountAmount = BigDecimal.ZERO;
}

public class InvoiceLineItemRequest {
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.01", message = "Quantity must be greater than 0")
    private BigDecimal quantity;
    
    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.01", message = "Unit price must be greater than 0")
    private BigDecimal unitPrice;
    
    private Long productId; // Optional product reference
}

public class InvoiceResponse {
    private Long id;
    private String invoiceNumber;
    private Long customerId;
    private String customerName;
    private LocalDate invoiceDate;
    private LocalDate dueDate;
    private InvoiceStatus status;
    private String description;
    private List<InvoiceLineItemResponse> lineItems;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal balanceDue;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

// Main Invoice Controller
@RestController
@RequestMapping("/api/v1/invoices")
@Validated
public class InvoiceController {
    
    private final InvoiceService invoiceService;
    
    // Create new invoice
    @PostMapping
    public ResponseEntity<InvoiceResponse> createInvoice(
            @RequestBody @Valid InvoiceCreateRequest request) {
        
        Invoice invoice = invoiceService.createInvoice(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(invoice));
    }
    
    // Get all invoices with filtering and pagination
    @GetMapping
    public ResponseEntity<PagedResponse<InvoiceResponse>> getInvoices(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "invoiceDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) InvoiceStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount) {
        
        InvoiceSearchCriteria criteria = InvoiceSearchCriteria.builder()
            .customerId(customerId)
            .status(status)
            .fromDate(fromDate)
            .toDate(toDate)
            .minAmount(minAmount)
            .maxAmount(maxAmount)
            .build();
            
        Pageable pageable = PageRequest.of(page, size, 
            Sort.Direction.fromString(sortDirection), sortBy);
            
        Page<Invoice> invoices = invoiceService.searchInvoices(criteria, pageable);
        return ResponseEntity.ok(toPagedResponse(invoices));
    }
    
    // Get specific invoice
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getInvoice(@PathVariable Long id) {
        Invoice invoice = invoiceService.findById(id);
        return ResponseEntity.ok(toResponse(invoice));
    }
    
    // Update invoice (only if not paid)
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceResponse> updateInvoice(
            @PathVariable Long id,
            @RequestBody @Valid InvoiceUpdateRequest request) {
        
        Invoice updated = invoiceService.updateInvoice(id, request);
        return ResponseEntity.ok(toResponse(updated));
    }
    
    // Delete invoice (only if draft)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.noContent().build();
    }
    
    // Send invoice to customer
    @PostMapping("/{id}/send")
    public ResponseEntity<Void> sendInvoice(@PathVariable Long id) {
        invoiceService.sendInvoice(id);
        return ResponseEntity.ok().build();
    }
    
    // Mark invoice as paid
    @PostMapping("/{id}/payments")
    public ResponseEntity<PaymentResponse> recordPayment(
            @PathVariable Long id,
            @RequestBody @Valid PaymentRequest paymentRequest) {
        
        Payment payment = invoiceService.recordPayment(id, paymentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(toPaymentResponse(payment));
    }
    
    // Get invoice payments
    @GetMapping("/{id}/payments")
    public ResponseEntity<List<PaymentResponse>> getInvoicePayments(@PathVariable Long id) {
        List<Payment> payments = invoiceService.getInvoicePayments(id);
        return ResponseEntity.ok(payments.stream().map(this::toPaymentResponse).collect(Collectors.toList()));
    }
    
    // Generate PDF
    @GetMapping("/{id}/pdf")
    public ResponseEntity<Resource> downloadInvoicePdf(@PathVariable Long id) {
        ByteArrayResource pdf = invoiceService.generatePdf(id);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + id + ".pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf);
    }
}

// Customer-specific invoice endpoints
@RestController
@RequestMapping("/api/v1/customers/{customerId}/invoices")
public class CustomerInvoiceController {
    
    @GetMapping
    public ResponseEntity<PagedResponse<InvoiceResponse>> getCustomerInvoices(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) InvoiceStatus status) {
        
        // Get invoices for specific customer
        Pageable pageable = PageRequest.of(page, size, Sort.by("invoiceDate").descending());
        Page<Invoice> invoices = invoiceService.findByCustomerId(customerId, status, pageable);
        return ResponseEntity.ok(toPagedResponse(invoices));
    }
    
    @PostMapping
    public ResponseEntity<InvoiceResponse> createCustomerInvoice(
            @PathVariable Long customerId,
            @RequestBody @Valid InvoiceCreateRequest request) {
        
        // Set customer ID from path
        request.setCustomerId(customerId);
        Invoice invoice = invoiceService.createInvoice(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(invoice));
    }
}

// Invoice summary and analytics endpoints
@RestController
@RequestMapping("/api/v1/invoices/analytics")
public class InvoiceAnalyticsController {
    
    @GetMapping("/summary")
    public ResponseEntity<InvoiceSummary> getInvoiceSummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        
        InvoiceSummary summary = invoiceService.getInvoiceSummary(fromDate, toDate);
        return ResponseEntity.ok(summary);
    }
    
    @GetMapping("/overdue")
    public ResponseEntity<List<InvoiceResponse>> getOverdueInvoices() {
        List<Invoice> overdue = invoiceService.findOverdueInvoices();
        return ResponseEntity.ok(overdue.stream().map(this::toResponse).collect(Collectors.toList()));
    }
}
```


## **Q5: Pagination and Filtering with Spring Data**

**Repository Layer:**

```java
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    
    // Simple query methods with pagination
    Page<Customer> findByActiveTrue(Pageable pageable);
    
    Page<Customer> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
        String firstName, String lastName, Pageable pageable);
    
    // Custom query with pagination
    @Query("SELECT c FROM Customer c WHERE c.registrationDate >= :fromDate")
    Page<Customer> findCustomersRegisteredAfter(@Param("fromDate") LocalDate fromDate, Pageable pageable);
}

// Advanced filtering with Specifications
@Component
public class CustomerSpecifications {
    
    public static Specification<Customer> hasFirstName(String firstName) {
        return (root, query, criteriaBuilder) ->
            firstName == null ? null : criteriaBuilder.like(
                criteriaBuilder.lower(root.get("firstName")), 
                "%" + firstName.toLowerCase() + "%"
            );
    }
    
    public static Specification<Customer> hasLastName(String lastName) {
        return (root, query, criteriaBuilder) ->
            lastName == null ? null : criteriaBuilder.like(
                criteriaBuilder.lower(root.get("lastName")), 
                "%" + lastName.toLowerCase() + "%"
            );
    }
    
    public static Specification<Customer> hasEmail(String email) {
        return (root, query, criteriaBuilder) ->
            email == null ? null : criteriaBuilder.like(
                criteriaBuilder.lower(root.get("email")), 
                "%" + email.toLowerCase() + "%"
            );
    }
    
    public static Specification<Customer> isActive(Boolean active) {
        return (root, query, criteriaBuilder) ->
            active == null ? null : criteriaBuilder.equal(root.get("active"), active);
    }
    
    public static Specification<Customer> registeredBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) return null;
            if (startDate == null) return criteriaBuilder.lessThanOrEqualTo(root.get("registrationDate"), endDate);
            if (endDate == null) return criteriaBuilder.greaterThanOrEqualTo(root.get("registrationDate"), startDate);
            return criteriaBuilder.between(root.get("registrationDate"), startDate, endDate);
        };
    }
    
    public static Specification<Customer> hasOrdersCountGreaterThan(Long count) {
        return (root, query, criteriaBuilder) -> {
            if (count == null) return null;
            
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Order> orderRoot = subquery.from(Order.class);
            subquery.select(criteriaBuilder.count(orderRoot))
                   .where(criteriaBuilder.equal(orderRoot.get("customer"), root));
                   
            return criteriaBuilder.greaterThan(subquery, count);
        };
    }
}
```


**Controller with Advanced Filtering:**

```java
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    
    private final CustomerService customerService;
    
    @GetMapping
    public ResponseEntity<PagedResponse<CustomerResponse>> getCustomers(
            // Pagination parameters
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "lastName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            
            // Filter parameters
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate registeredFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate registeredTo,
            @RequestParam(required = false) @Min(0) Long minOrdersCount,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state) {
        
        // Build dynamic specifications
        Specification<Customer> spec = Specification.where(null);
        
        spec = spec.and(CustomerSpecifications.hasFirstName(firstName));
        spec = spec.and(CustomerSpecifications.hasLastName(lastName));
        spec = spec.and(CustomerSpecifications.hasEmail(email));
        spec = spec.and(CustomerSpecifications.isActive(active));
        spec = spec.and(CustomerSpecifications.registeredBetween(registeredFrom, registeredTo));
        spec = spec.and(CustomerSpecifications.hasOrdersCountGreaterThan(minOrdersCount));
        
        // Address filtering (assuming embedded address)
        if (city != null) {
            spec = spec.and((root, query, cb) -> 
                cb.like(cb.lower(root.get("address").get("city")), "%" + city.toLowerCase() + "%"));
        }
        
        if (state != null) {
            spec = spec.and((root, query, cb) -> 
                cb.equal(cb.lower(root.get("address").get("state")), state.toLowerCase()));
        }
        
        // Create pageable
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        // Execute query
        Page<Customer> customers = customerService.findAll(spec, pageable);
        
        // Convert to response
        PagedResponse<CustomerResponse> response = PagedResponse.<CustomerResponse>builder()
            .content(customers.getContent().stream()
                .map(this::toResponse)
                .collect(Collectors.toList()))
            .page(customers.getNumber())
            .size(customers.getSize())
            .totalElements(customers.getTotalElements())
            .totalPages(customers.getTotalPages())
            .first(customers.isFirst())
            .last(customers.isLast())
            .numberOfElements(customers.getNumberOfElements())
            .build();
            
        return ResponseEntity.ok(response);
    }
}

// Generic Paged Response DTO
@Data
@Builder
public class PagedResponse<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
    private int numberOfElements;
    
    // Metadata for client-side pagination
    private boolean hasNext;
    private boolean hasPrevious;
    
    @JsonIgnore
    public boolean getHasNext() {
        return !last;
    }
    
    @JsonIgnore
    public boolean getHasPrevious() {
        return !first;
    }
}
```


**Service Layer:**

```java
@Service
@Transactional(readOnly = true)
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    
    public Page<Customer> findAll(Specification<Customer> spec, Pageable pageable) {
        return customerRepository.findAll(spec, pageable);
    }
    
    // Alternative approach using Criteria API directly
    public Page<Customer> searchCustomers(CustomerSearchCriteria criteria, Pageable pageable) {
        return customerRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (criteria.getFirstName() != null) {
                predicates.add(cb.like(cb.lower(root.get("firstName")), 
                    "%" + criteria.getFirstName().toLowerCase() + "%"));
            }
            
            if (criteria.getEmail() != null) {
                predicates.add(cb.like(cb.lower(root.get("email")), 
                    "%" + criteria.getEmail().toLowerCase() + "%"));
            }
            
            if (criteria.getActive() != null) {
                predicates.add(cb.equal(root.get("active"), criteria.getActive()));
            }
            
            if (criteria.getRegisteredFrom() != null && criteria.getRegisteredTo() != null) {
                predicates.add(cb.between(root.get("registrationDate"), 
                    criteria.getRegisteredFrom(), criteria.getRegisteredTo()));
            }
            
            // Add sorting for consistent pagination
            query.orderBy(cb.asc(root.get("id")));
            
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }
}

// Search criteria DTO
@Data
@Builder
public class CustomerSearchCriteria {
    private String firstName;
    private String lastName;
    private String email;
    private Boolean active;
    private LocalDate registeredFrom;
    private LocalDate registeredTo;
    private Long minOrdersCount;
    private String city;
    private String state;
}
```


**Best Practices for Pagination and Filtering:**

1. **Always validate pagination parameters** (min/max page size)
2. **Use consistent sort criteria** to avoid duplicate results across pages
3. **Implement cursor-based pagination** for large datasets
4. **Cache expensive filter queries** when appropriate
5. **Provide metadata** in responses (total count, has next/previous)
6. **Index database columns** used in filtering and sorting
7. **Use projections** for list views to reduce data transfer
8. **Implement search result highlighting** for text-based filters

This comprehensive approach provides flexible, efficient, and user-friendly pagination and filtering capabilities for REST APIs.