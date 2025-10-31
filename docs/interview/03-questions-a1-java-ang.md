## 🔹 1. Core Java / Enterprise Backend

**Brush up on:**

* Java 11+ features (Streams API, Optionals, Records, var, switch expressions).
* OOP design & SOLID principles — they’ll test your reasoning.
* Collections, concurrency (`ExecutorService`, `CompletableFuture`), immutability, exception hierarchy.
* Writing clean, modular service layers.

**Spring Boot essentials**

* Lifecycle: controllers → services → repositories.
* Dependency Injection (`@Component`, `@Service`, `@Repository`, `@Autowired`).
* REST controllers (`@RestController`, `@RequestMapping`, validation with `@Valid`).
* Configuration management (`application.yml`, `@Value`, `@ConfigurationProperties`).
* Transaction management (`@Transactional`) and exception handling (`@ControllerAdvice`).

**Interview-style questions to expect**

* “Explain difference between REST and SOAP and how you’d expose a SOAP service in Spring.”
* “How does Spring Boot autoconfiguration work?”
* “What’s the difference between checked and unchecked exceptions in your service layer?”

---

## 🔹 2. Oracle / SQL

**Hands-on prep**

* Practice writing `SELECT` with `JOIN`s, `GROUP BY`, `HAVING`, and `WINDOW` functions.
* Review PL/SQL basics: stored procedures, functions, triggers, cursors.
* Know how to optimize queries: indexing, execution plans, avoiding N+1 queries.
* Transactions & isolation levels in Oracle (Read Committed, Serializable).
* Understand how JPA/Hibernate maps to Oracle (schema generation, sequences).

**Sample exercise**

> Write a PL/SQL block that updates customer status based on subscription expiry and logs changes into an audit table.

---

## 🔹 3. Integration & Architecture (REST, SOAP, SOA)

**Conceptual review**

* How REST resources are modeled; idempotent HTTP methods.
* JSON serialization/deserialization with Jackson.
* SOAP basics: WSDL, envelopes, XML schemas.
* Handling large data processing / pagination.
* Designing DTOs and mapping entities (MapStruct or manual mappers).

**Key question types**

* “How would you expose a legacy SOAP service to a modern REST client?”
* “Explain how you’d secure a REST API (Spring Security + JWT).”

---

## 🔹 4. Angular (Frontend Integration)

You only need **practical working knowledge** to connect UI with backend.

**Focus areas**

* Component structure, lifecycle hooks.
* `HttpClient` calls to backend endpoints.
* Observables vs Promises.
* Simple two-way data binding and form handling.
* Error handling & interceptors (for JWT or session).

**Typical question**

> “How would you call a secured Spring Boot endpoint from Angular and handle token expiration?”

---

## 🔹 5. DevOps & Deployment (Linux, Kubernetes, CI/CD)

**Linux**

* Navigating logs (`grep`, `tail`, `journalctl`).
* Managing processes and environment variables.
* Basic networking commands (`curl`, `netstat`, `ping`, `telnet`).

**Kubernetes**

* Understand **Pods, Deployments, Services, ConfigMaps, Secrets**.
* Know how to view logs and restart pods (`kubectl logs`, `kubectl rollout restart`).
* Concept of liveness/readiness probes.

**CI/CD & Tooling**

* Jenkins pipelines: build → test → deploy.
* ArgoCD: GitOps basics (auto-sync from Git repo to K8s).
* Atlassian Suite (Jira for stories, Bitbucket for code review).

**Likely questions**

* “What’s the difference between a Docker container and a Kubernetes pod?”
* “Describe how you’d set up a CI/CD pipeline for a Spring Boot + Angular project.”

---

## 🔹 6. Agile & Soft Skills

They explicitly mention Agile — expect:

* Understanding of Scrum ceremonies and sprint workflow.
* How you estimate tasks and handle production incidents.
* Collaboration with BAs and testers.

Practice short, specific answers like:

> “I prefer to estimate in story points based on complexity. Once development starts, I sync daily with QA and BA, and
> we use Jira boards to track blockers.”

---

## 🔹 7. Sample Questions to Rehearse Verbally

| Area            | Example                                                                                          |
|-----------------|--------------------------------------------------------------------------------------------------|
| **Java**        | “Explain difference between `HashMap` and `ConcurrentHashMap`.”                                  |
| **Spring**      | “How do you configure transactions and why would you mark a service method as `@Transactional`?” |
| **Oracle**      | “What steps would you take to tune a slow query?”                                                |
| **Integration** | “How would you consume an external SOAP service from Spring?”                                    |
| **Angular**     | “How do you share data between components?”                                                      |
| **DevOps**      | “How would you deploy a new version of your service in Kubernetes with zero downtime?”           |

---

## 🔹 8. 5-Day Refresher Plan (Working 9–5)

| Day   | After-Work Focus         | Deliverable                                      |
|-------|--------------------------|--------------------------------------------------|
| **1** | Java & Spring Boot recap | Write a small REST API with `GET/POST` endpoints |
| **2** | Oracle SQL/PLSQL         | Practice joins, triggers, and a stored procedure |
| **3** | REST + SOAP Integration  | Mock external SOAP and REST services             |
| **4** | Angular basics           | Build a small UI calling your REST API           |
| **5** | CI/CD + K8s + Linux      | Simulate deployment (Docker compose or Minikube) |

---

## ✅ Final Advice

* **Bring examples**: prepare one or two projects where you’ve improved performance or refactored messy code.
* **Talk architecture**: mention layers (Controller–Service–DAO) and how you structure modules.
* **Emphasize maintainability**: clean code, reusable components, logging, monitoring.
* **Brush up on telecom basics** (optional): provisioning, service activation, order management — common in telco
  fulfillment systems.

---
