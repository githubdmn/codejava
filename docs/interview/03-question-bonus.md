--------------------------------------------------------

1. Java (11-21) & Spring Ecosystem

--------------------------------------------------------
L1  
1.1 Record vs Lombok @Value – compile-time diff, serialization edge-cases?  
1.2 Switch-expression vs old switch – what bytecode is generated?  
1.3  `@RestController` vs `@Controller` + `@ResponseBody` – any runtime difference?  
Follow-up trap: “How would you return a streaming JSON array without blocking the servlet thread?”

L2  
1.4 Design a small rate-limiter in Java using only `Semaphore` and `ScheduledExecutor` – make it cluster-safe.  
1.5 Explain how Spring Boot does **conditional bean creation**; draw the `spring.factories` vs
`spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` delta (SB 2.7 → 3.x).  
1.6 You see a `LazyInitializationException` in a `@Transactional` service – root causes list at least 4.  
Follow-up trap: “Fix it without `spring.jpa.open-in-view=true` and without `JOIN FETCH`.”

L3  
1.7 Implement a custom `BeanDefinitionRegistryPostProcessor` that **replaces** the default `DataSource` with a *
*tenant-aware routing DataSource** at start-up.  
1.8 Deep-clone an object graph that contains cycles – do it **without reflection** and **without Serializable** in O(
n).  
1.9 Given a 200 TPS micro-service, trace shows 30 % CPU in `String.format` – how do you prove it and fix it in 30 min?

--------------------------------------------------------

2. Oracle SQL / PL-SQL

--------------------------------------------------------
L1  
2.1 Write a single MERGE that upserts 5M rows coming from a staging table; list the hints you would add and why.  
2.2  `INDEX RANGE SCAN` vs `INDEX FAST FULL SCAN` – cardinality threshold?  
Follow-up trap: “You add an index, execution plan still full-table – give me the **three** meta-stable reasons.”

L2  
2.3 Write a **pipelined table function** that turns a comma-separated clob into rows without creating an intermediate
collection.  
2.4 Explain **read consistency** vs **write consistency** when a PL/SQL block updates the same row twice – what SCN is
seen?  
2.5 You have a **bitmap join index** on a fact table; nightly ETL starts failing with `ORA-00060` – why?

L3  
2.6 Design a **sharding key** for a multi-tenant telco billing system (500 B rows) – justify datatype, hash vs range,
partition pruning proof.  
2.7 Trace shows **log file sync** waits > 30 ms – give your exact SQL*Plus / AWR queries to prove whether it’s storage
or application commit batching.  
2.8 Implement **row-level security** (RLS/VPD) that enforces “user can see only customers in his sales region” without
changing a single line of Java – show policy function and predicate.

--------------------------------------------------------

3. REST / SOAP / Integration

--------------------------------------------------------
L1  
3.1 Produce a **WSDL-first** SOAP endpoint in Spring-WS; generate only **interface** sources at build time – Maven
plugin config?  
3.2 Idempotent PUT vs PATCH – give an example where PATCH is **not** idempotent.  
Follow-up trap: “How do you make PATCH idempotent in a distributed cache?”

L2  
3.3 Design a **retry storm circuit-breaker** that works across a Kubernetes cluster (no Hystrix, no Resilience4j) –
sketch the algorithm.  
3.4 Map a **correlation-id** that travels JMS → REST → SOAP → Kafka – which headers do you reuse and where do you *
*generate** a new one?  
3.5 A legacy SOAP service returns 5 MB XML – how do you **stream** it to a React UI without holding it in heap?

L3  
3.6 Implement a **SAGA** (choreography style) for port-in order fulfilment: 3 services, 2 are REST, 1 is SOAP, one phase
is **manual** – show compensating actions table and idempotency keys.  
3.7 You must expose the same contract via **SOAP 1.1**, **SOAP 1.2**, **REST JSON**, **REST XML** – single codebase,
four endpoints – how do you avoid code duplication?

--------------------------------------------------------

4. Angular (only what a backend dev is expected to know)

--------------------------------------------------------
L1  
4.1 Difference between `providedIn: 'root'` and `forRoot()` – tree-shaking impact?  
4.2  `HttpClient` returns cold observable – when does the HTTP call **actually** fire?  
Follow-up trap: “How do you share the response with late subscribers without a second request?”

L2  
4.3 Build an **interceptor** that silently refreshes a JWT 30 s before expiry – no logout flash.  
4.4 You have a form with 500 rows editable grid – user sorts and filters – how do you keep form state **dirty** flags
accurate without killing performance?

--------------------------------------------------------

5. Kubernetes / Linux / CI-CD

--------------------------------------------------------
L1  
5.1 One-liner to list **all** pods that are **Ready** but have **restarted > 3 times** in the last hour.  
5.2  `kubectl apply` vs `kubectl replace` – which one drops the entire object and recreates it?  
Follow-up trap: “Your deployment has `strategy.rollingUpdate.maxUnavailable: 0` – why can the rollout still cause 502s?”

L2  
5.3 Write a **multi-stage Dockerfile** for a Spring Boot + Angular app that keeps the final image < 120 MB – show exact
`COPY` statements.  
5.4 You need **zero-downtime** schema migration in Oracle inside Kubernetes – describe the **init-container** strategy
including rollback.

L3  
5.5 Design a **GitOps** repo layout (ArgoCD) for 15 micro-services, 3 environments, 2 regions – folder hierarchy and
ApplicationSet template.  
5.6 A pod shows `OOMKilled` but JVM heap is only 60 % used – exact steps to prove whether it’s **off-heap** or **cgroup
v2** memory pressure.

--------------------------------------------------------

6. Architectural & Agile “curve-balls”

--------------------------------------------------------
A1 You inherit a **monolith** that takes 45 min to start – give a **3-week plan** to cut it to < 3 min without touching
code logic.  
A2 PO suddenly moves 40 % of stories to the next sprint – how do you **rebaseline** the release burn-up without
re-estimating every card?  
A3 Draw a **CNCF-style** diagram for a converged charging system (OCS) that must **sustain 40 k TPS** with < 10 ms
latency – mark where you would place **eBPF** probes.

--------------------------------------------------------
How to use the bank
--------------------------------------------------------

1. Record yourself answering – limit 2 min per L1, 5 min per L2, 10 min per L3.
2. Where you stall, **write the smallest possible spike** (code, SQL, or manifest) and push it to a private repo –
   interviewers love live links.
3. After every session write a **“cheat headline”** (one sentence) on a sticky note; wall-full of stickies == ready.

### **The Interviewer's Mindset for this Role**

They aren't hiring a junior. They need someone who can:

1. **Build and Maintain Critical Systems:** Telecom fulfillment is the core revenue engine. It must be robust.
2. **Navigate Complexity:** Legacy systems (SOAP, Oracle) alongside modern ones (Kubernetes, REST).
3. **Own Problems:** From code to production, including on-call troubleshooting.

Your answers should reflect ownership, pragmatism, and a focus on production stability.

---

### **Pillar 1: Core Java & Spring Boot (The Bread and Butter)**

Go beyond syntax. Focus on performance, design, and maintainability.

**Key Areas & Probing Questions:**

1. **Concurrency in a Telco Context:**
    * **Question:** "Our fulfillment system processes thousands of orders in batches. How would you design a Java
      service to handle this efficiently without overwhelming the database?"
    * **What they want to hear:** Discussion of `ExecutorService` with a fixed thread pool, `CompletableFuture` for
      asynchronous processing, idempotency (crucial for retries!), and database connection pool tuning. Mentioning
      back-pressure or circuit breakers is a plus.
    * **Follow-up:** "What are the common pitfalls when using `synchronized` on a service method in a Spring
      application?"

2. **Design for Maintainability & "Clean Code":**
    * **Question:** "The job description emphasizes 'clean, scalable, and maintainable code.' What does that mean to you
      in practice, specifically for a Java service?"
    * **What they want to hear:** Talk about **SOLID principles** (give a brief example of SRP or DIP), using design
      patterns appropriately (e.g., Strategy for different provisioning logic, Factory for creating service orders).
      Mention the importance of clear package structure, separation of concerns (Controller -> Service -> Repository),
      and writing unit-testable code.

3. **Spring Boot Deep Dive:**
    * **Question:** "How does `@Transactional` really work under the hood? What happens if you call a `@Transactional`
      method from within the same class?"
    * **What they want to hear:** Understanding of Spring's proxy-based AOP. The caller must come "from the outside"
      through the proxy for the transaction advice to kick in. This leads to a discussion of self-invocation and how to
      solve it (e.g., `@Autowired` the proxy itself).
    * **Question:** "How would you handle a scenario where one of your service fulfillment SOAP calls is consistently
      slow and timing out?"
    * **What they want to hear:** Practical troubleshooting: checking logs, using metrics (Micrometer), setting
      appropriate timeouts at the HTTP client level, implementing a retry mechanism with exponential backoff, and
      finally, considering a circuit breaker pattern (Hystrix/Resilience4j).

---

### **Pillar 2: Oracle & Data Processing (The Source of Truth)**

This is about performance and data integrity.

**Key Areas & Probing Questions:**

1. **Performance Tuning & Explain Plans:**
    * **Question:** "A customer reports that their order is stuck. You suspect a slow database query. Walk me through
      your troubleshooting steps."
    * **What they want to hear:** A systematic approach:
        1. Identify the slow query from logs or by querying `V$SESSION`/`V$SQL`.
        2. Run an `EXPLAIN PLAN` to see the execution path.
        3. Look for full table scans, missing indexes, inefficient joins.
        4. Propose solutions: adding an index, rewriting the query, suggesting an index-organized table.
    * **Be ready to:** Read a simple `EXPLAIN PLAN` output if they provide one.

2. **Handling Large Datasets:**
    * **Question:** "You need to generate a daily report on all services provisioned. The dataset is 10 million records.
      How do you avoid loading everything into memory at once in your Java application?"
    * **What they want to hear:** Use of Oracle cursors or JDBC `ResultSet` with `FETCH_SIZE` for forward-only,
      read-only streaming. This is a classic data-processing question.

3. **Transactions and Locking:**
    * **Question:** "Two customers try to order the same limited-resource service (like a specific phone number) at the
      same time. How do you prevent a 'double-sell' in the database?"
    * **What they want to hear:** Discussion of database isolation levels (READ_COMMITTED might not be enough),
      pessimistic locking (`SELECT ... FOR UPDATE`), or optimistic locking with a version column. Explain the trade-offs
      of each.

---

### **Pillar 3: Integration & Architecture (The Glue)**

This is where backend engineers are separated from the rest.

**Key Areas & Probing Questions:**

1. **SOAP vs. REST - Pragmatics, not just theory:**
    * **Question:** "Our legacy provisioning system uses SOAP. A new partner wants a REST API. How would you bridge this
      gap?"
    * **What they want to hear:** Don't just list differences. Propose a solution: build a RESTful facade in your new
      Spring Boot service that internally makes SOAP calls to the legacy system. Use a library like
      `org.springframework.ws` for the SOAP client. This shows architectural thinking.

2. **API Design for Fulfillment:**
    * **Question:** "Design a REST API for initiating a new internet service fulfillment order. What are the endpoints,
      HTTP methods, and request/response bodies?"
    * **What they want to hear:** `POST /api/v1/orders` to create. The response should include an `orderId` and a
      status (e.g., "IN_PROGRESS"). A `GET /api/v1/orders/{id}` to check status. Discuss idempotency keys for `POST` to
      prevent duplicate orders.

---

### **Pillar 4: DevOps & Production Readiness (Where the Rubber Meets the Road)**

This proves you understand the full lifecycle.

**Key Areas & Probing Questions:**

1. **Kubernetes Troubleshooting - A Scenario:**
    * **Question:** "You deploy a new version of your Java service to Kubernetes. The new pods are stuck in
      `CrashLoopBackOff`. What's your debugging process?"
    * **What they want to hear:**
        1. `kubectl get pods` to see the state.
        2. `kubectl describe pod <pod-name>` to check events (e.g., failed to pull image, resource limits).
        3. `kubectl logs <pod-name> --previous` to get the logs from the last crashed instance.
        4. Check for common issues: misconfigured environment variables, missing ConfigMaps/Secrets, memory limits too
           low causing OOMKills.

2. **CI/CD Philosophy:**
    * **Question:** "What should be the stages of a CI/CD pipeline for this service? What quality gates would you insist
      on?"
    * **What they want to hear:** Build -> Unit Tests -> Integration Tests -> Security Scan (SAST) -> Build Docker
      Image -> (Optional) Deploy to Staging -> End-to-End Tests. Emphasize that the pipeline should be fast and
      reliable. Mention ArgoCD for GitOps: "The deployment manifests in Git are the desired state, and ArgoCD syncs the
      cluster to match."

---

### **The "Killer Question" to Prepare For**

This combines everything:

**"Imagine you are tasked with adding a new feature to the service fulfillment system: 'Schedule a service activation
for a future date.' Walk me through how you would approach this, from discussing requirements with the business analyst,
to design, implementation, and deployment."**

**Your structured answer should cover:**

* **Clarification:** Ask the BA questions (What happens if the system is down on that date? Can the customer cancel?).
* **Design:** A new database table `scheduled_activations`. A scheduler (e.g., Quartz) or a Kubernetes CronJob that runs
  every minute to find due activations.
* **Implementation:** The job retrieves due orders and calls the existing provisioning logic. Idempotency is key here.
* **Testing:** How you'd test the scheduler logic.
* **Deployment:** CI/CD pipeline, feature flags, and a plan for monitoring the new scheduled job in production.

### **Your 5-Day Intensive Drill Plan**

| Day   | Focus               | Actionable Drill                                                                                                                                                                                                                        |
|:------|:--------------------|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **1** | **Java & Spring**   | Code a small service that uses `CompletableFuture` to simulate calling two external SOAP/REST services concurrently. Handle timeouts. Write a test for it.                                                                              |
| **2** | **Oracle & Data**   | On a free Oracle instance (or even PostgreSQL, the concepts are similar), write a query that uses a `WINDOW` function (e.g., `ROW_NUMBER()`) to find the latest order for each customer. Practice reading an `EXPLAIN PLAN`.            |
| **3** | **Integration**     | Use Spring Boot to create a REST endpoint that acts as a facade. Make this endpoint call a public SOAP weather service (there are many free ones), parse the response, and return a simplified JSON.                                    |
| **4** | **Architecture**    | Whiteboard (or draw with a tool) the solution to the "Killer Question" above. Think about all the components and data flow.                                                                                                             |
| **5** | **DevOps & Review** | Take a simple Spring Boot app, dockerize it, and write a basic Kubernetes Deployment YAML file. Deploy it to a local Minikube cluster. Intentionally break the image name in the YAML and use `kubectl` commands to diagnose the error. |

