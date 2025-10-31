I'll provide comprehensive answers to these security and deployment questions:

## **Q1: Spring Security Integration with REST APIs**

**Core Spring Security Configuration for REST APIs:**

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtTokenProvider jwtTokenProvider;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            // Disable CSRF for stateless REST APIs
            .csrf(csrf -> csrf.disable())
            
            // Disable sessions (stateless)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Configure CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // Exception handling
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler))
            
            // Authorization rules
            .authorizeHttpRequests(authz -> authz
                // Public endpoints
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                
                // Role-based access
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers("/api/reports/**").hasAuthority("VIEW_REPORTS")
                
                // Authenticated endpoints
                .requestMatchers("/api/**").authenticated()
                .anyRequest().denyAll())
            
            // Add JWT filter
            .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), 
                UsernamePasswordAuthenticationFilter.class)
            
            .build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:4200", "https://*.yourcompany.com"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // Strong hashing with 12 rounds
    }
    
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
```


**JWT Token Provider and Filter:**

```java
@Component
public class JwtTokenProvider {
    
    private final String secretKey;
    private final long tokenValidityInMilliseconds;
    
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                           @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
    }
    
    public String createToken(String username, Collection<? extends GrantedAuthority> authorities) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("authorities", authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList()));
        
        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityInMilliseconds);
        
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact();
    }
    
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }
}

public class JwtTokenFilter extends OncePerRequestFilter {
    
    private final JwtTokenProvider jwtTokenProvider;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        String token = resolveToken(request);
        
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        
        filterChain.doFilter(request, response);
    }
    
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return (bearerToken != null && bearerToken.startsWith("Bearer ")) 
            ? bearerToken.substring(7) : null;
    }
}
```


**Method-Level Security:**

```java
@RestController
@RequestMapping("/api/banking")
@PreAuthorize("hasRole('BANK_USER')")
public class BankingController {
    
    @PostMapping("/transfer")
    @PreAuthorize("hasAuthority('TRANSFER_FUNDS') and #request.fromAccount.owner == authentication.name")
    public ResponseEntity<TransferResponse> transferFunds(@RequestBody @Valid TransferRequest request) {
        // Only users with TRANSFER_FUNDS authority can access
        // Only if they own the source account
        return ResponseEntity.ok(bankingService.transfer(request));
    }
    
    @GetMapping("/accounts/{accountId}")
    @PostAuthorize("returnObject.body.owner == authentication.name or hasRole('ADMIN')")
    public ResponseEntity<Account> getAccount(@PathVariable Long accountId) {
        // Post-authorization: users can only see their own accounts (unless admin)
        Account account = accountService.findById(accountId);
        return ResponseEntity.ok(account);
    }
    
    @GetMapping("/transactions")
    @PreAuthorize("hasAuthority('VIEW_TRANSACTIONS')")
    public ResponseEntity<Page<Transaction>> getTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        
        // Automatically filter by user's accounts
        String username = authentication.getName();
        Page<Transaction> transactions = transactionService.findByUser(username, page, size);
        return ResponseEntity.ok(transactions);
    }
}
```


## **Q2: JWT vs Session-Based Authentication**

**JWT-Based Authentication:**

**Advantages:**
- **Stateless**: Server doesn't store session data
- **Scalable**: No shared session storage needed across servers
- **Cross-domain**: Works across different domains/services
- **Mobile-friendly**: No cookies required
- **Decentralized**: Token contains all necessary information

**Disadvantages:**
- **Token size**: JWTs are larger than session IDs
- **Security risks**: Tokens stored in localStorage vulnerable to XSS
- **Revocation difficulty**: Hard to invalidate tokens before expiration
- **Storage overhead**: Client must store and send token with every request

```java
// JWT Authentication Flow
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        // Validate credentials
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        // Generate JWT token
        String token = jwtTokenProvider.createToken(
            authentication.getName(), 
            authentication.getAuthorities()
        );
        
        // Return token and user info
        User user = userService.findByUsername(authentication.getName());
        return ResponseEntity.ok(AuthResponse.builder()
            .token(token)
            .tokenType("Bearer")
            .expiresIn(3600) // 1 hour
            .user(UserDto.from(user))
            .build());
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        // Validate refresh token and generate new access token
        String newToken = jwtTokenProvider.createTokenFromRefreshToken(request.getRefreshToken());
        return ResponseEntity.ok(AuthResponse.builder()
            .token(newToken)
            .tokenType("Bearer")
            .expiresIn(3600)
            .build());
    }
}
```


**Session-Based Authentication:**

**Advantages:**
- **Smaller network overhead**: Only session ID sent
- **Server control**: Easy to invalidate sessions
- **Secure storage**: Session data stored server-side
- **Built-in support**: Mature, well-understood pattern

**Disadvantages:**
- **Stateful**: Server must store session data
- **Scaling complexity**: Requires sticky sessions or shared storage
- **CORS issues**: Cookies don't work well across domains
- **Mobile limitations**: Cookie handling in mobile apps

```java
// Session-based configuration
@Configuration
public class SessionSecurityConfig {
    
    @Bean
    public SecurityFilterChain sessionFilterChain(HttpSecurity http) throws Exception {
        return http
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1) // Only one session per user
                .maxSessionsPreventsLogin(false)
                .sessionRegistry(sessionRegistry())
                .and()
                .sessionFixation().migrateSession()
                .invalidSessionUrl("/login?expired"))
            
            .rememberMe(remember -> remember
                .key("uniqueAndSecret")
                .tokenValiditySeconds(86400)) // 24 hours
            
            .logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true))
            
            .build();
    }
}
```


**Comparison Summary:**

| Aspect | JWT | Session |
|--------|-----|---------|
| **State** | Stateless | Stateful |
| **Scalability** | High | Moderate |
| **Security** | Token-based risks | Session hijacking risks |
| **Storage** | Client-side | Server-side |
| **Revocation** | Difficult | Easy |
| **Cross-domain** | Excellent | Limited |
| **Mobile apps** | Excellent | Limited |

## **Q3: Securing Banking Transactions in ERP Software**

**Data Protection Layers:**

**1. Encryption at Rest and in Transit:**

```java
@Configuration
public class EncryptionConfig {
    
    @Bean
    public AESUtil aesUtil(@Value("${encryption.secret-key}") String secretKey) {
        return new AESUtil(secretKey);
    }
}

@Component
public class AESUtil {
    private final String secretKey;
    private final String algorithm = "AES/GCB/PKCS5Padding";
    
    public String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            
            byte[] cipherText = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception e) {
            throw new SecurityException("Encryption failed", e);
        }
    }
    
    public String decrypt(String encryptedText) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            
            byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            return new String(plainText);
        } catch (Exception e) {
            throw new SecurityException("Decryption failed", e);
        }
    }
}

// JPA Converter for automatic encryption/decryption
@Converter
public class EncryptedStringConverter implements AttributeConverter<String, String> {
    
    @Autowired
    private AESUtil aesUtil;
    
    @Override
    public String convertToDatabaseColumn(String attribute) {
        return attribute != null ? aesUtil.encrypt(attribute) : null;
    }
    
    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData != null ? aesUtil.decrypt(dbData) : null;
    }
}
```


**2. Secure Entity Design:**

```java
@Entity
@Table(name = "bank_accounts")
@EntityListeners(AuditingEntityListener.class)
public class BankAccount {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Encrypted sensitive data
    @Convert(converter = EncryptedStringConverter.class)
    @Column(name = "account_number")
    private String accountNumber;
    
    @Convert(converter = EncryptedStringConverter.class)
    @Column(name = "routing_number")
    private String routingNumber;
    
    // Hash for quick lookups without decryption
    @Column(name = "account_number_hash", unique = true)
    private String accountNumberHash;
    
    // Balance with precision for financial calculations
    @Column(name = "balance", precision = 15, scale = 2)
    private BigDecimal balance;
    
    // Audit fields
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    @CreatedBy
    private String createdBy;
    
    @LastModifiedBy
    private String lastModifiedBy;
    
    @Version
    private Long version; // Optimistic locking
    
    // Security: Never expose sensitive data in toString()
    @Override
    public String toString() {
        return "BankAccount{" +
            "id=" + id +
            ", balance=" + balance +
            ", createdAt=" + createdAt +
            '}';
    }
}

@Entity
@Table(name = "financial_transactions")
public class FinancialTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "transaction_id", unique = true, nullable = false)
    private String transactionId; // UUID for external reference
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account_id")
    private BankAccount fromAccount;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account_id")
    private BankAccount toAccount;
    
    @Column(name = "amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal amount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType type;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status;
    
    // Encrypted memo field
    @Convert(converter = EncryptedStringConverter.class)
    private String memo;
    
    // Digital signature for transaction integrity
    @Column(name = "digital_signature")
    private String digitalSignature;
    
    @CreatedDate
    private LocalDateTime processedAt;
    
    @CreatedBy
    private String processedBy;
}
```


**3. Transaction Security Service:**

```java
@Service
@Transactional
public class SecureBankingService {
    
    private final BankAccountRepository accountRepository;
    private final FinancialTransactionRepository transactionRepository;
    private final AuditService auditService;
    private final DigitalSignatureService signatureService;
    private final FraudDetectionService fraudDetectionService;
    
    @PreAuthorize("hasAuthority('TRANSFER_FUNDS')")
    @Transactional(isolation = Isolation.SERIALIZABLE) // Highest isolation level
    public TransferResponse transferFunds(@Valid TransferRequest request, Authentication auth) {
        
        // 1. Fraud detection
        FraudCheck fraudCheck = fraudDetectionService.checkTransaction(request, auth.getName());
        if (fraudCheck.isHighRisk()) {
            auditService.logSecurityEvent("HIGH_RISK_TRANSACTION_BLOCKED", auth.getName(), request);
            throw new SecurityException("Transaction blocked due to fraud detection");
        }
        
        // 2. Load accounts with pessimistic locking
        BankAccount fromAccount = accountRepository.findByIdWithLock(request.getFromAccountId())
            .orElseThrow(() -> new AccountNotFoundException("Source account not found"));
        
        BankAccount toAccount = accountRepository.findByIdWithLock(request.getToAccountId())
            .orElseThrow(() -> new AccountNotFoundException("Destination account not found"));
        
        // 3. Validate ownership and permissions
        validateAccountOwnership(fromAccount, auth.getName());
        validateTransactionLimits(fromAccount, request.getAmount(), auth);
        
        // 4. Check sufficient balance
        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            auditService.logSecurityEvent("INSUFFICIENT_FUNDS_ATTEMPT", auth.getName(), request);
            throw new InsufficientFundsException("Insufficient balance");
        }
        
        // 5. Create transaction record
        FinancialTransaction transaction = new FinancialTransaction();
        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(request.getAmount());
        transaction.setType(TransactionType.TRANSFER);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setMemo(request.getMemo());
        
        // 6. Generate digital signature
        String signature = signatureService.signTransaction(transaction);
        transaction.setDigitalSignature(signature);
        
        // 7. Update balances
        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));
        
        // 8. Save everything
        transaction.setStatus(TransactionStatus.COMPLETED);
        transactionRepository.save(transaction);
        accountRepository.saveAll(Arrays.asList(fromAccount, toAccount));
        
        // 9. Audit logging
        auditService.logTransfer(transaction, auth.getName());
        
        // 10. Return response (no sensitive data)
        return TransferResponse.builder()
            .transactionId(transaction.getTransactionId())
            .status(transaction.getStatus())
            .processedAt(transaction.getProcessedAt())
            .build();
    }
    
    private void validateAccountOwnership(BankAccount account, String username) {
        if (!account.getOwner().getUsername().equals(username)) {
            auditService.logSecurityEvent("UNAUTHORIZED_ACCOUNT_ACCESS", username, 
                Map.of("accountId", account.getId()));
            throw new UnauthorizedException("Account access denied");
        }
    }
    
    private void validateTransactionLimits(BankAccount account, BigDecimal amount, Authentication auth) {
        UserLimits limits = userLimitsService.getLimits(auth.getName());
        
        if (amount.compareTo(limits.getDailyTransferLimit()) > 0) {
            throw new LimitExceededException("Daily transfer limit exceeded");
        }
        
        BigDecimal dailyTotal = transactionRepository.getDailyTransferTotal(account.getId(), LocalDate.now());
        if (dailyTotal.add(amount).compareTo(limits.getDailyTransferLimit()) > 0) {
            throw new LimitExceededException("Daily transfer limit would be exceeded");
        }
    }
}
```


**4. Additional Security Measures:**

```java
// IP-based access control
@Component
public class IPWhitelistService {
    
    @Value("${security.allowed-ips}")
    private List<String> allowedIPs;
    
    public boolean isIPAllowed(String clientIP) {
        return allowedIPs.contains(clientIP) || isInAllowedSubnet(clientIP);
    }
}

// Rate limiting
@Component
public class RateLimitingService {
    private final RedisTemplate<String, String> redisTemplate;
    
    public boolean isAllowed(String userKey, int maxRequests, Duration window) {
        String key = "rate_limit:" + userKey;
        String current = redisTemplate.opsForValue().get(key);
        
        if (current == null) {
            redisTemplate.opsForValue().set(key, "1", window);
            return true;
        }
        
        int requestCount = Integer.parseInt(current);
        if (requestCount >= maxRequests) {
            return false;
        }
        
        redisTemplate.opsForValue().increment(key);
        return true;
    }
}

// Multi-factor authentication for sensitive operations
@PostMapping("/api/banking/transfer/confirm")
@PreAuthorize("hasAuthority('TRANSFER_FUNDS')")
public ResponseEntity<TransferResponse> confirmTransfer(
        @RequestBody @Valid TransferConfirmationRequest request,
        Authentication auth) {
    
    // Verify MFA token
    if (!mfaService.validateToken(auth.getName(), request.getMfaToken())) {
        auditService.logSecurityEvent("INVALID_MFA_TOKEN", auth.getName(), request);
        throw new InvalidMFATokenException("Invalid MFA token");
    }
    
    return ResponseEntity.ok(bankingService.executeTransfer(request.getTransactionId()));
}
```


## **Q4: Production Deployment - Spring Boot + Angular**

**Build Pipeline Configuration:**

**1. Frontend Build (Angular):**

```yaml
# .github/workflows/frontend-deploy.yml
name: Frontend Deploy
on:
  push:
    branches: [main]
    paths: ['frontend/**']

jobs:
  build-and-deploy:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    
    - name: Setup Node.js
      uses: actions/setup-node@v3
      with:
        node-version: '18'
        cache: 'npm'
        cache-dependency-path: frontend/package-lock.json
    
    - name: Install dependencies
      run: |
        cd frontend
        npm ci
    
    - name: Lint and Test
      run: |
        cd frontend
        npm run lint
        npm run test:ci
        npm run e2e:ci
    
    - name: Build for production
      run: |
        cd frontend
        npm run build:prod
      env:
        NODE_ENV: production
    
    - name: Build Docker image
      run: |
        docker build -t ${{ secrets.REGISTRY_URL }}/erp-frontend:${{ github.sha }} ./frontend
        docker tag ${{ secrets.REGISTRY_URL }}/erp-frontend:${{ github.sha }} \
                   ${{ secrets.REGISTRY_URL }}/erp-frontend:latest
    
    - name: Push to registry
      run: |
        echo ${{ secrets.REGISTRY_PASSWORD }} | docker login ${{ secrets.REGISTRY_URL }} -u ${{ secrets.REGISTRY_USERNAME }} --password-stdin
        docker push ${{ secrets.REGISTRY_URL }}/erp-frontend:${{ github.sha }}
        docker push ${{ secrets.REGISTRY_URL }}/erp-frontend:latest
    
    - name: Deploy to production
      run: |
        kubectl set image deployment/erp-frontend \
          erp-frontend=${{ secrets.REGISTRY_URL }}/erp-frontend:${{ github.sha }}
        kubectl rollout status deployment/erp-frontend
```


**2. Backend Build (Spring Boot):**

```yaml
# .github/workflows/backend-deploy.yml
name: Backend Deploy
on:
  push:
    branches: [main]
    paths: ['backend/**']

jobs:
  test:
    runs-on: ubuntu-latest
    services:
      postgres:
        image: postgres:15
        env:
          POSTGRES_PASSWORD: test
          POSTGRES_DB: erp_test
        options: >-
          --health-cmd pg_isready
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 21
      uses: actions/setup-java@v3
      with:
        java-version: '21'
        distribution: 'temurin'
    
    - name: Cache Maven dependencies
      uses: actions/cache@v3
      with:
        path: ~/.m2
        key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
    
    - name: Run tests
      run: |
        cd backend
        mvn clean test
      env:
        SPRING_PROFILES_ACTIVE: test
        SPRING_DATASOURCE_URL: jdbc:postgresql://localhost:5432/erp_test
        SPRING_DATASOURCE_USERNAME: postgres
        SPRING_DATASOURCE_PASSWORD: test
    
    - name: Run integration tests
      run: |
        cd backend
        mvn verify -Pintegration-tests
    
    - name: Security scan
      run: |
        cd backend
        mvn org.owasp:dependency-check-maven:check
    
    - name: Code quality
      run: |
        cd backend
        mvn sonar:sonar \
          -Dsonar.projectKey=erp-backend \
          -Dsonar.host.url=${{ secrets.SONAR_HOST_URL }} \
          -Dsonar.login=${{ secrets.SONAR_TOKEN }}

  build-and-deploy:
    needs: test
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 21
      uses: actions/setup-java@v3
      with:
        java-version: '21'
        distribution: 'temurin'
    
    - name: Build application
      run: |
        cd backend
        mvn clean package -DskipTests
    
    - name: Build Docker image
      run: |
        cd backend
        docker build -t ${{ secrets.REGISTRY_URL }}/erp-backend:${{ github.sha }} .
        docker tag ${{ secrets.REGISTRY_URL }}/erp-backend:${{ github.sha }} \
                   ${{ secrets.REGISTRY_URL }}/erp-backend:latest
    
    - name: Security scan Docker image
      run: |
        docker run --rm -v /var/run/docker.sock:/var/run/docker.sock \
          -v $HOME/Library/Caches:/root/.cache/ \
          aquasec/trivy:latest image \
          --exit-code 1 --severity HIGH,CRITICAL \
          ${{ secrets.REGISTRY_URL }}/erp-backend:${{ github.sha }}
    
    - name: Push to registry
      run: |
        echo ${{ secrets.REGISTRY_PASSWORD }} | docker login ${{ secrets.REGISTRY_URL }} -u ${{ secrets.REGISTRY_USERNAME }} --password-stdin
        docker push ${{ secrets.REGISTRY_URL }}/erp-backend:${{ github.sha }}
        docker push ${{ secrets.REGISTRY_URL }}/erp-backend:latest
    
    - name: Deploy to production
      run: |
        kubectl set image deployment/erp-backend \
          erp-backend=${{ secrets.REGISTRY_URL }}/erp-backend:${{ github.sha }}
        kubectl rollout status deployment/erp-backend
```


**3. Docker Configuration:**

```dockerfile
# Frontend Dockerfile
FROM node:18-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm ci --only=production
COPY . .
RUN npm run build:prod

FROM nginx:alpine
COPY --from=builder /app/dist/erp-frontend /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```


```dockerfile
# Backend Dockerfile
FROM openjdk:21-jdk-slim AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN ./mvnw clean package -DskipTests

FROM openjdk:21-jre-slim
RUN adduser --system --group spring
USER spring:spring
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```


**4. Kubernetes Deployment:**

```yaml
# k8s/backend-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: erp-backend
  labels:
    app: erp-backend
spec:
  replicas: 3
  selector:
    matchLabels:
      app: erp-backend
  template:
    metadata:
      labels:
        app: erp-backend
    spec:
      containers:
      - name: erp-backend
        image: registry.company.com/erp-backend:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "production"
        - name: SPRING_DATASOURCE_URL
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: url
        - name: SPRING_DATASOURCE_USERNAME
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: username
        - name: SPRING_DATASOURCE_PASSWORD
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: password
        - name: JWT_SECRET
          valueFrom:
            secretKeyRef:
              name: jwt-secret
              key: secret
        resources:
          requests:
            memory: "512Mi"
            cpu: "500m"
          limits:
            memory: "1Gi"
            cpu: "1000m"
        livenessProbe:
          httpGet:
            path: /actuator/health/liveness
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 30
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
---
apiVersion: v1
kind: Service
metadata:
  name: erp-backend-service
spec:
  selector:
    app: erp-backend
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
  type: ClusterIP
```


## **Q5: DevOps Practices for CI/CD, Monitoring, and Logging**

**1. CI/CD Pipeline Best Practices:**

```yaml
# Complete CI/CD Pipeline
name: Full Deploy Pipeline
on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main]

env:
  REGISTRY: ghcr.io
  IMAGE_NAME: erp-application

jobs:
  changes:
    runs-on: ubuntu-latest
    outputs:
      backend: ${{ steps.filter.outputs.backend }}
      frontend: ${{ steps.filter.outputs.frontend }}
      infrastructure: ${{ steps.filter.outputs.infrastructure }}
    steps:
    - uses: actions/checkout@v3
    - uses: dorny/paths-filter@v2
      id: filter
      with:
        filters: |
          backend:
            - 'backend/**'
          frontend:
            - 'frontend/**'
          infrastructure:
            - 'infrastructure/**'
            - '.github/workflows/**'

  quality-gates:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    
    - name: Security scan
      run: |
        # Run security scans
        docker run --rm -v "${{ github.workspace }}:/src" \
          shiftleft/sast-scan scan --src /src --type java,javascript
    
    - name: License compliance
      run: |
        # Check license compliance
        npx license-checker --production --csv --out licenses.csv
    
    - name: Dependency vulnerability scan
      run: |
        # Scan for vulnerable dependencies
        npm audit --audit-level high
        mvn org.owasp:dependency-check-maven:check

  build-backend:
    needs: [changes, quality-gates]
    if: needs.changes.outputs.backend == 'true'
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    
    - name: Setup Java
      uses: actions/setup-java@v3
      with:
        java-version: '21'
        distribution: 'temurin'
    
    - name: Test and build
      run: |
        cd backend
        mvn clean verify
        mvn package -DskipTests
    
    - name: Build and push Docker image
      run: |
        docker build -t $REGISTRY/$IMAGE_NAME-backend:$GITHUB_SHA ./backend
        docker push $REGISTRY/$IMAGE_NAME-backend:$GITHUB_SHA

  deploy-staging:
    needs: [build-backend, build-frontend]
    if: github.ref == 'refs/heads/develop'
    runs-on: ubuntu-latest
    environment: staging
    steps:
    - name: Deploy to staging
      run: |
        # Deploy to staging environment
        kubectl apply -f k8s/staging/
        kubectl set image deployment/erp-backend \
          erp-backend=$REGISTRY/$IMAGE_NAME-backend:$GITHUB_SHA
        kubectl rollout status deployment/erp-backend

  integration-tests:
    needs: deploy-staging
    runs-on: ubuntu-latest
    steps:
    - name: Run integration tests
      run: |
        # Run end-to-end tests against staging
        npm run test:e2e:staging
    
    - name: Performance tests
      run: |
        # Run performance tests
        docker run --rm -v $PWD:/app \
          loadimpact/k6 run --vus 50 --duration 5m /app/performance-tests.js

  deploy-production:
    needs: integration-tests
    if: github.ref == 'refs/heads/main'
    runs-on: ubuntu-latest
    environment: production
    steps:
    - name: Blue-Green Deployment
      run: |
        # Implement blue-green deployment
        ./scripts/blue-green-deploy.sh $REGISTRY/$IMAGE_NAME-backend:$GITHUB_SHA
    
    - name: Health check
      run: |
        # Verify deployment health
        kubectl wait --for=condition=available --timeout=300s deployment/erp-backend
        curl -f https://api.company.com/actuator/health
    
    - name: Rollback on failure
      if: failure()
      run: |
        kubectl rollout undo deployment/erp-backend
```


**2. Monitoring and Observability:**

```yaml
# Prometheus monitoring configuration
apiVersion: v1
kind: ConfigMap
metadata:
  name: prometheus-config
data:
  prometheus.yml: |
    global:
      scrape_interval: 15s
    
    scrape_configs:
    - job_name: 'spring-boot-app'
      metrics_path: '/actuator/prometheus'
      static_configs:
      - targets: ['erp-backend-service:8080']
    
    - job_name: 'angular-app'
      static_configs:
      - targets: ['erp-frontend-service:80']
    
    rule_files:
    - "alert_rules.yml"
    
    alerting:
      alertmanagers:
      - static_configs:
        - targets: ['alertmanager:9093']
---
apiVersion: v1
kind: ConfigMap
metadata:
  name: alert-rules
data:
  alert_rules.yml: |
    groups:
    - name: application_alerts
      rules:
      - alert: HighErrorRate
        expr: rate(http_server_requests_seconds_count{status=~"5.."}[5m]) > 0.1
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: High error rate detected
      
      - alert: HighMemoryUsage
        expr: jvm_memory_used_bytes / jvm_memory_max_bytes > 0.8
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: High memory usage detected
      
      - alert: DatabaseConnectionsHigh
        expr: hikaricp_connections_active > 80
        for: 2m
        labels:
          severity: critical
        annotations:
          summary: Database connection pool nearly exhausted
```


```java
// Custom metrics in Spring Boot
@Component
public class CustomMetrics {
    
    private final Counter transactionCounter;
    private final Timer transactionTimer;
    private final Gauge activeUsersGauge;
    private final Counter fraudAttempts;
    
    public CustomMetrics(MeterRegistry meterRegistry) {
        this.transactionCounter = Counter.builder("banking.transactions.total")
            .description("Total number of transactions")
            .tag("type", "transfer")
            .register(meterRegistry);
        
        this.transactionTimer = Timer.builder("banking.transactions.duration")
            .description("Transaction processing time")
            .register(meterRegistry);
        
        this.activeUsersGauge = Gauge.builder("banking.users.active")
            .description("Number of active users")
            .register(meterRegistry, this, CustomMetrics::getActiveUserCount);
        
        this.fraudAttempts = Counter.builder("banking.fraud.attempts")
            .description("Number of fraud attempts detected")
            .register(meterRegistry);
    }
    
    public void recordTransaction() {
        transactionCounter.increment();
    }
    
    public Timer.Sample startTransactionTimer() {
        return Timer.start();
    }
    
    public void recordFraudAttempt(String type) {
        fraudAttempts.increment(Tags.of("type", type));
    }
    
    private double getActiveUserCount() {
        return sessionRegistry.getAllPrincipals().size();
    }
}
```


**3. Centralized Logging:**

```yaml
# ELK Stack configuration
version: '3.8'
services:
  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:8.5.0
    environment:
      - discovery.type=single-node
      - xpack.security.enabled=false
    ports:
      - "9200:9200"
    volumes:
      - elasticsearch-data:/usr/share/elasticsearch/data

  logstash:
    image: docker.elastic.co/logstash/logstash:8.5.0
    ports:
      - "5000:5000"
      - "5044:5044"
    volumes:
      - ./logstash.conf:/usr/share/logstash/pipeline/logstash.conf
    depends_on:
      - elasticsearch

  kibana:
    image: docker.elastic.co/kibana/kibana:8.5.0
    ports:
      - "5601:5601"
    environment:
      - ELASTICSEARCH_HOSTS=http://elasticsearch:9200
    depends_on:
      - elasticsearch

volumes:
  elasticsearch-data:
```


```java
// Structured logging in Spring Boot
@RestController
public class TransactionController {
    
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
    
    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(@RequestBody TransferRequest request) {
        
        // Structured logging with MDC
        MDC.put("transactionId", UUID.randomUUID().toString());
        MDC.put("userId", getCurrentUserId());
        MDC.put("amount", request.getAmount().toString());
        
        try {
            logger.info("Starting transaction processing", 
                kv("fromAccount", request.getFromAccountId()),
                kv("toAccount", request.getToAccountId()),
                kv("amount", request.getAmount()));
            
            TransferResponse response = bankingService.transfer(request);
            
            logger.info("Transaction completed successfully",
                kv("transactionId", response.getTransactionId()),
                kv("status", response.getStatus()));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Transaction failed",
                kv("error", e.getMessage()),
                kv("errorType", e.getClass().getSimpleName()),
                e);
            throw e;
        } finally {
            MDC.clear();
        }
    }
}

// Application configuration for logging
# application.yml
logging:
  level:
    com.company.erp: INFO
    org.springframework.security: DEBUG
    org.hibernate.SQL: DEBUG
  pattern:
    console: "%d{HH:mm:ss.SSS} [%thread] %-5level [%X{traceId:-},%X{spanId:-}] %logger{36} - %msg%n"
    file: "%d{ISO8601} [%thread] %-5level [%X{traceId:-},%X{spanId:-}] %logger - %msg%n"
  file:
    name: logs/erp-application.log
    max-size: 100MB
    max-history: 30

# Logback configuration for JSON logging
<!-- logback-spring.xml -->
<configuration>
    <springProfile name="production">
        <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
            <encoder class="net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder">
                <providers>
                    <timestamp/>
                    <logLevel/>
                    <loggerName/>
                    <message/>
                    <mdc/>
                    <stackTrace/>
                </providers>
            </encoder>
        </appender>
    </springProfile>
    
    <springProfile name="!production">
        <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
            <encoder>
                <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
            </encoder>
        </appender>
    </springProfile>
    
    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
    </root>
</configuration>
```


**4. Infrastructure as Code:**

```yaml
# Terraform for infrastructure
# main.tf
provider "aws" {
  region = "us-west-2"
}

module "vpc" {
  source = "terraform-aws-modules/vpc/aws"
  
  name = "erp-vpc"
  cidr = "10.0.0.0/16"
  
  azs             = ["us-west-2a", "us-west-2b", "us-west-2c"]
  private_subnets = ["10.0.1.0/24", "10.0.2.0/24", "10.0.3.0/24"]
  public_subnets  = ["10.0.101.0/24", "10.0.102.0/24", "10.0.103.0/24"]
  
  enable_nat_gateway = true
  enable_vpn_gateway = true
}

module "rds" {
  source = "terraform-aws-modules/rds/aws"
  
  identifier = "erp-database"
  
  engine            = "postgres"
  engine_version    = "15.3"
  instance_class    = "db.t3.medium"
  allocated_storage = 100
  
  db_name  = "erp_prod"
  username = "erp_admin"
  password = random_password.db_password.result
  
  vpc_security_group_ids = [aws_security_group.rds.id]
  subnet_ids             = module.vpc.private_subnets
  
  backup_retention_period = 7
  backup_window          = "03:00-04:00"
  maintenance_window     = "sun:04:00-sun:05:00"
  
  deletion_protection = true
  encrypted          = true
}

resource "aws_eks_cluster" "erp_cluster" {
  name     = "erp-cluster"
  role_arn = aws_iam_role.eks_cluster.arn
  version  = "1.25"
  
  vpc_config {
    subnet_ids = module.vpc.private_subnets
  }
  
  enabled_cluster_log_types = ["api", "audit", "authenticator", "controllerManager", "scheduler"]
}
```


**Best Practices Summary:**

1. **Automated Testing**: Unit, integration, and E2E tests in pipeline
2. **Security Scanning**: SAST, DAST, dependency scanning
3. **Blue-Green Deployments**: Zero-downtime deployments
4. **Infrastructure as Code**: Version-controlled infrastructure
5. **Monitoring**: Comprehensive metrics, alerting, and dashboards
6. **Centralized Logging**: Structured logging with correlation IDs
7. **Secrets Management**: Never store secrets in code
8. **Rollback Strategy**: Quick rollback capabilities
9. **Performance Testing**: Load testing in staging
10. **Documentation**: Runbooks and deployment documentation

This comprehensive approach ensures secure, reliable, and maintainable production deployments with proper observability and incident response capabilities.