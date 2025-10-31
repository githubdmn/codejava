
## **1. General Software Engineering & SDLC**

---

### **Q1: Walk me through your process when you get a new feature request — from requirements to deployment.**

**Answer:**
When I receive a new feature request, I follow a structured SDLC process:

1. **Requirements Gathering** – Meet with the product owner/BA to understand the business need, clarify acceptance criteria, and identify constraints.
2. **Design** – Decide on architecture and data flow. For example, if it touches multiple services, I define APIs, DB schema changes, and integration points.
3. **Estimation & Planning** – Break down into user stories and assign story points.
4. **Implementation** – Develop following clean code and SOLID principles. Keep commits small and meaningful.
5. **Testing** – Write unit tests and integration tests. Ensure coverage and test edge cases.
6. **Code Review & CI/CD** – Open a PR, address feedback, ensure pipeline passes (build, test, static analysis).
7. **Deployment** – Deploy to staging for UAT, then roll out to production (often with feature flags).
8. **Monitoring** – Track metrics, logs, and error reports post-deployment to verify stability.

---

### **Q2: What are some best practices you follow for code reviews and Git workflow?**

**Answer:**

* **Git Workflow**

    * Feature branches off `develop`/`main`.
    * Clear, descriptive commit messages.
    * Rebase or squash to maintain clean history.
    * Protect main branch with PR approvals and CI checks.

* **Code Review Practices**

    * Focus on readability, maintainability, and performance.
    * Enforce coding standards and security best practices.
    * Ensure proper test coverage.
    * Give constructive feedback with reasoning (not just “change this”).
    * Keep PRs small and focused for easier review.

---

### **Q3: In Agile teams, how do you break down complex tasks into deliverables for a sprint?**

**Answer:**

* Start from the **epic/feature** → break into **user stories** that follow the **INVEST principle** (Independent, Negotiable, Valuable, Estimable, Small, Testable).
* Slice **vertically** (end-to-end deliverables) rather than horizontally (just DB or just frontend).
  Example: Instead of “create DB schema,” define: *“As a user, I can submit a transaction and see a confirmation.”*
* Prioritize based on business value and dependencies.
* Estimate with planning poker or story points.
* Ensure each task is small enough to be completed within a sprint.

---

### **Q4: Suppose you’re building a banking transaction module. How do you ensure reliability, scalability, and maintainability?**

**Answer:**

* **Reliability**

    * Use ACID transactions in the DB to guarantee consistency.
    * Idempotency keys to avoid duplicate transactions.
    * Strong validation and error handling.
    * Logging and auditing for traceability.

* **Scalability**

    * Design services stateless → easy to scale horizontally.
    * Use caching (Redis) for frequent lookups.
    * Optimize queries and indexes.
    * Partition/shard large transaction tables if needed.

* **Maintainability**

    * Layered architecture (Controller → Service → Repository).
    * Follow SOLID and clean code principles.
    * Unit + integration tests with mocks and real DB.
    * Swagger/OpenAPI for API documentation.
    * Consistent coding standards and CI/CD automation.

* **Security (bonus)**

    * JWT/OAuth2 for authentication.
    * Encrypt sensitive data at rest and in transit.
    * Role-based access control.

---

