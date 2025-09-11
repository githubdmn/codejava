
1. **Concise flashcard-style definitions** (fast recall).
2. **Deeper, scenario-driven answers** (mock interview style).

That way, you’ll have layered preparation: quick review + real-interview depth.

---

# 🎯 Enhanced Java Interview Prep (Flashcards + Mock Q\&A)

---

## **1. Encapsulation vs Abstraction**

**Flashcard:**

* **Encapsulation:** Bundling data and methods, restricting direct access (via private fields, getters/setters).
* **Abstraction:** Hiding implementation details, exposing only essential features (via abstract classes, interfaces).

**Mock Answer:**
"Encapsulation is about restricting direct access to the internal state of an object, usually by making fields private and exposing behavior through public methods. Example: a `BankAccount` class with private `balance` and a method `deposit(amount)`.
Abstraction is about exposing *what* a class does while hiding *how* it does it. Example: an interface `PaymentProcessor` with a method `processPayment()`—the user doesn’t care whether it's implemented with Stripe or PayPal."

---

## **2. Overloading vs Overriding**

**Flashcard:**

* **Overloading (Compile-time polymorphism):** Same method name, different parameter list.
* **Overriding (Runtime polymorphism):** Subclass changes behavior of a superclass method with the same signature.

**Mock Answer:**
"Overloading is resolved at **compile-time** by the compiler based on the parameter list. For example, `print(String)` and `print(int)` are two overloads.
Overriding is resolved at **runtime** using dynamic dispatch. Example: A `Shape` class with `draw()` overridden by `Circle` and `Rectangle`. The JVM decides at runtime which implementation to call depending on the actual object reference."

---

## **3. HashMap & equals/hashCode**

**Flashcard:**

* Always override `equals()` and `hashCode()` consistently.
* Rule: if `equals()` returns true for two objects, their `hashCode()` must be equal.

**Mock Answer:**
"When using a `User` object as a key in a `HashMap`, we must override `equals()` and `hashCode()` properly. Otherwise, two logically equal `User` objects could be placed in different buckets, breaking lookups.
For example, if `equals()` checks equality by `userId`, then `hashCode()` must also be based on `userId`. The contract ensures that logically equal objects go to the same bucket, avoiding inconsistent lookups."

---

## **4. Why String is Immutable**

**Flashcard:**

* Security, caching (String pool), thread-safety, performance.

**Mock Answer:**
"`String` is immutable for multiple reasons:

1. **Security:** Credentials in URLs or config strings can’t be modified after creation.
2. **Thread-safety:** Multiple threads can share the same `String` without synchronization.
3. **Hash caching:** Since the string content never changes, its hash code is cached, making it efficient in hash-based collections.
4. **Pooling:** Immutability allows for the String Pool to reuse objects safely."

---

## **5. Modifying List in For-Each**

**Flashcard:**

* For-each + modifying collection → `ConcurrentModificationException`.
* Fix: use `Iterator.remove()` or `removeIf()`.

**Mock Answer:**
"This code throws a `ConcurrentModificationException` because you can’t structurally modify a list while iterating with the enhanced for-loop.
Fixes:

* Use an explicit `Iterator` and call `iterator.remove()`.
* Or use `list.removeIf(s -> s.equals("A"));` which is clean and safe."

---

## **6. JWT Authentication Flow (Spring Boot)**

**Flashcard:**

* Client logs in → Server issues signed JWT → Client sends JWT in `Authorization` header → Server validates signature.

**Mock Answer:**
"In Spring Boot, I’d implement JWT authentication like this:

1. **Login endpoint:** User sends credentials, we validate against DB.
2. **Token creation:** On success, generate JWT signed with a secret (HS256). Add claims like `username` and `roles`.
3. **Client stores token:** Typically in local storage or cookies.
4. **Subsequent requests:** Client includes JWT in `Authorization: Bearer <token>`.
5. **Filter:** A custom `OncePerRequestFilter` intercepts requests, validates the token signature and expiry, extracts user details, and populates the SecurityContext.
6. **Authorization:** Spring Security uses roles/authorities from the JWT to enforce access control."

---

✅ Now you have **layered answers**:

* Flashcards → for speed.
* Mock answers → for depth in a real interview.

---
