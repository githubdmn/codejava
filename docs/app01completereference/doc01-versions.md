
The evolution of Java from version 1.0 to Java 2 (also referred to as J2SE, Java 2 Platform Standard Edition) marked significant milestones in the language's development. Here's a breakdown of key events and features introduced during this evolution:

### **Java 1.0: The Initial Release**

- **Released**: 1996

- **Significance**: This was the first official release of Java, introducing key features such as object-oriented programming, platform independence via the Java Virtual Machine (JVM), and the core Java libraries.

### **Java 1.1: Significant Enhancements**

- **Released**: 1997

- **Significance**: Java 1.1 introduced more profound changes than just a minor version increment, redefining several core elements:

- **New Event Handling**: Java 1.1 revamped the event-handling model, moving away from the event inheritance system to a more flexible delegation-based model, making GUI programming more intuitive.

- **Inner Classes**: Support for inner classes was added, providing a way to logically group classes and increase code readability.

- **RMI (Remote Method Invocation)**: Java 1.1 introduced support for distributed computing with RMI, allowing Java objects to invoke methods on objects running in other JVMs.

- **Reflection API**: This allowed programs to inspect classes and methods at runtime.

- **Deprecations**: Several features were marked as obsolete or deprecated in this version, streamlining the language.

### **Java 2 (J2SE) and Java 1.2: The Modern Era**

- **Released**: 1998

- **Significance**: Java 2 (J2SE 1.2) was a major turning point and marked the start of Java's "modern age."

- **Swing GUI Toolkit**: Introduced the Swing library, which allowed for the creation of richer, platform-independent graphical user interfaces (GUIs).

- **Collections Framework**: Java 2 introduced the Collections Framework, a unified architecture for representing and manipulating collections of data, such as lists, sets, and maps.

- **JVM Enhancements**: Enhancements to the Java Virtual Machine made Java programs more performant and robust.

- **Thread Deprecations**: Methods like `suspend()`, `resume()`, and `stop()` in the `Thread` class were deprecated due to the potential for deadlocks and inconsistent thread states.

### **Java 1.3: Stabilization and Enhancement**

- **Released**: 2000

- **Significance**: This release mainly refined and enhanced the existing platform with improvements to performance and efficiency:

- **Source-Code Compatibility**: Java 1.3 maintained source-code compatibility with Java 1.2, meaning programs written in 1.2 would generally run without modification on 1.3.

- **HotSpot JVM**: The introduction of the HotSpot JVM, which provided improved runtime performance via optimizations like just-in-time (JIT) compilation.

### **Java 1.4: New Features and Substantial Improvements**

- **Released**: 2002

- **Significance**: Java 1.4 introduced several important features and enhancements:

- **New Keyword `assert`**: Added to facilitate runtime debugging by allowing developers to assert certain conditions in the code.

- **Chained Exceptions**: Allowed better exception handling by linking multiple exceptions to provide more detailed error reporting.

- **NIO (New Input/Output)**: Introduced a new channel-based I/O subsystem that enhanced Java's ability to handle file and network operations with higher performance.

- **Improvements to Collections and Networking**: Further refinements and enhancements to the Collections Framework and networking libraries.

### **Java's Evolution Summary**

Java's evolution from version 1.0 to Java 1.4 shows a focus on improving the language's robustness, ease of use, and performance, while maintaining backward compatibility. The significant changes made in these versions laid the foundation for Java to become one of the most widely used programming languages globally.

- **Backward Compatibility**: Despite the major changes, Java maintained strong backward compatibility throughout its versions, which made it easier for developers to transition to newer versions without breaking their code.

- **Deprecations**: Each version also saw the deprecation of older, problematic features to encourage developers to use more modern and reliable alternatives.

**Java J2SE 5** (also known as Java 5 or Java 1.5) was a groundbreaking release that fundamentally expanded Java's capabilities, both in terms of language features and the way developers wrote code. This version introduced several major features, many of which are now considered essential parts of the language. Here's a breakdown of the most important changes:

### **Key Features Introduced in J2SE 5:**

1\. **Generics**:

- Generics provided a way to specify types for collections and other generic classes, offering greater type safety at compile time.

- Example:

```java 
List<String> list = new ArrayList<>(); 
list.add("Hello"); // No need for casting when retrieving elements. 
```

2\. **Annotations**:

- Annotations added metadata to classes, methods, and fields. They can be used to generate code, configure frameworks, and even enforce constraints at compile time.

- Example:

```java 
@Override public void someMethod() { // code } 
```

3\. **Autoboxing and Auto-unboxing**:

- Autoboxing automatically converts between primitive types (like `int`, `double`) and their wrapper classes (`Integer`, `Double`).

- Example:

```java
Integer i = 10; // Autoboxing: int to Integer.
int j = i;      // Auto-unboxing: Integer to int.
```

4\. **Enumerations**:

- Java 5 introduced `enum`, which allows the creation of a set of named constants, making code more readable and less error-prone.

- Example:

```java
enum Day { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY }
```

5\. **Enhanced `for-each` Loop**:

- The enhanced `for` loop provided a cleaner syntax for iterating over collections and arrays.

- Example:

```java
for (String item : list) {
System.out.println(item);
}
```

6\. **Varargs (Variable-length Arguments)**:

- Varargs allow a method to accept an arbitrary number of arguments of a specific type.

- Example:

```java
public static void printNumbers(int... numbers) {
    for (int num : numbers) {
        System.out.println(num);
    }
}
```

7\. **Static Import**:

- Static import allows static members (fields and methods) to be used without class qualification.

- Example:

```java
import static java.lang.Math.\*;
System.out.println(sqrt(4)); // No need for Math.sqrt().
```

8\. **Formatted I/O**:

- Java 5 introduced `java.util.Formatter` and `String.format()`, offering more sophisticated ways to format output.

- Example:

```java
System.out.printf("PI is approximately %.2f", Math.PI);
```

9\. **Concurrency Utilities**:

- Java 5 introduced a new package, `java.util.concurrent`, which included powerful concurrency tools such as thread pools, executors, locks, and atomic variables.

- Example:

```java
ExecutorService executor = Executors.newFixedThreadPool(10);
```

### **Significance of Java 5**

The magnitude of these changes was so substantial that the version number was bumped from 1.4 to 5. The "5" emphasized the revolutionary nature of the changes, though internally it was still referred to as Java 1.5.

---

### **Java SE 6 (Java 1.6): Incremental Enhancements**

Java SE 6 built upon the foundation laid by J2SE 5 but did not introduce major new language features. It primarily focused on:

- **API Improvements**: Many existing libraries were improved and new APIs were introduced, such as scripting integration (with `javax.script`), improvements to Web Services, and an enhanced GUI toolkit (Swing).

- **Performance and Runtime Enhancements**: Java SE 6 included optimizations to the Java Virtual Machine (JVM) and garbage collection, leading to better performance and reduced memory usage.

- **Compiler API**: Introduced the ability to use the Java compiler as part of Java applications (`javax.tools`).

### **Java 6's Lifecycle**

Java 6 went through several updates, refining the platform and adding smaller improvements over time. While it didn't introduce major language changes, its long life cycle saw the evolution of tooling and stability.

---

### **Impact on Java Development**

The changes introduced in **Java 5** and **Java 6** revolutionized how developers approached problems:

- **Generics** made Java more type-safe and reduced the need for casting.

- **Annotations** paved the way for modern frameworks like Spring and Hibernate.

- **Concurrency utilities** improved multi-threaded programming, making it easier to handle concurrent tasks.

- **Enhanced for loop** and **varargs** simplified everyday programming tasks.

These versions fundamentally reshaped Java, setting the foundation for modern enterprise Java development.

**Java SE 7** (released as JDK 7) marked a significant step in the evolution of Java, particularly because it was the first major release after Oracle's acquisition of Sun Microsystems. This release introduced several language enhancements as part of **Project Coin**, as well as important additions to the Java API libraries, improving Java's capabilities and ease of use.

### **Key Language Features Introduced in Java SE 7 (JDK 7):**

1\. **Strings in `switch` Statements**:

- Java SE 7 added the long-awaited ability to use `String` objects in `switch` statements, which simplifies code that needs to branch on string values.

- Example:

```java
String day = "Monday";
switch (day) {
    case "Monday":
        System.out.println("Start of the week");
        break;
    case "Friday":
        System.out.println("End of the work week");
        break;
    default:
        System.out.println("Middle of the week");
}
```

2\. **Binary Literals**:

- You can now represent integer values using binary literals, which is helpful when dealing with binary numbers, bitmasking, and low-level operations.

- Example:

```java
int binaryValue = 0b1010;  // Equal to decimal 10.
```

3\. **Underscores in Numeric Literals**:

- Java SE 7 allows underscores in numeric literals, which helps improve readability of large numbers.

- Example:

```java
int million = 1_000_000;   // Easier to read.
long hexBytes = 0xFF_EC_DE_5E;  // Underscores can be used in hex numbers too.
```

4\. **Try-with-Resources (Automatic Resource Management)**:

- The `try-with-resources` statement ensures that resources (like files, streams, or sockets) are automatically closed after the program is done using them. This reduces the chances of resource leaks.

- Example:

```java
try (BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
    String line;
    while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
} catch (IOException e) {
    e.printStackTrace();
}
```

- Resources opened within parentheses will be automatically closed at the end of the block.

5\. **Type Inference with the Diamond Operator (`<>`)**:

- Java SE 7 introduced the diamond operator for type inference in generic class instantiation, making the code cleaner by removing redundant type information.

- Example:

```java
List<String> list = new ArrayList<>();  // No need to specify <String> again.
```

6\. **Multi-catch Blocks**:

- Multi-catch enables catching multiple exception types in a single `catch` block, reducing duplicate code when handling different types of exceptions similarly.

- Example:

```java
try {
    // Code that may throw multiple exceptions
} catch (IOException | SQLException e) {
    e.printStackTrace();
}
```

7\. **Improved Exception Handling (Precise Re-throw)**:

- The compiler can now more precisely infer the type of an exception being re-thrown from a `catch` block, making exception handling more efficient.

8\. **Varargs Warnings Control**:

- Java SE 7 improved how the compiler handles unchecked warnings related to variable-length argument (varargs) methods. It gives developers more control over these warnings.

### **Key API Enhancements in Java SE 7:**

1\. **NIO.2 (New I/O Framework Enhancements)**:

- The NIO framework introduced in Java 1.4 was significantly enhanced, so much so that it's often referred to as **NIO.2**. The new API makes it easier to work with file systems, directories, and file attributes.

- **Features of NIO.2**:

- **File and Directory Operations**: With the `java.nio.file` package, developers can easily perform file manipulation, such as copying, moving, and deleting files.

- **Path Class**: A new class `Path` represents a file or directory path and allows for more flexible file handling.

- **Asynchronous I/O**: NIO.2 added support for asynchronous file operations, which can enhance performance, especially in I/O-heavy applications.

2\. **Fork/Join Framework (Parallel Programming Support)**:

- The **Fork/Join Framework** is a new API introduced to simplify the task of parallel programming. It allows developers to break tasks into smaller parts that can be executed concurrently, leveraging multiple processors (or cores).

- **How It Works**:

- The framework divides a task into smaller sub-tasks (forks) that can be executed in parallel and then combines their results (join).

- **Use Case**: The Fork/Join Framework is ideal for divide-and-conquer algorithms, where a large problem is recursively split into smaller problems, processed in parallel, and then the results are combined.

- Example:

```java
ForkJoinPool pool = new ForkJoinPool();
pool.invoke(new RecursiveTaskExample());
```

### **Other Features and Improvements:**

- **Concurrency Utilities**: Enhancements were made to Java's concurrency libraries (e.g., `java.util.concurrent`), providing more robust tools for handling concurrent tasks.

- **Elliptic Curve Cryptography (ECC)**: Enhanced security with support for ECC algorithms.

- **InvokeDynamic (JVM Support for Non-Java Languages)**: The `invokedynamic` bytecode instruction was introduced, which improved the JVM's support for dynamically typed languages like Groovy and JRuby.

### **Significance of Java SE 7**:

Java SE 7, with its **Project Coin** improvements and enhancements to concurrency and file handling (NIO.2), made coding more concise, efficient, and parallelized. For many Java developers, the try-with-resources and Fork/Join Framework had the most immediate practical impact, as they simplified error handling and boosted performance in multi-core environments, respectively.

Java SE 7 set the stage for future improvements in Java while solidifying its use as a modern, versatile programming language capable of handling the demands of both enterprise applications and modern concurrent systems.

**Java SE 8 (JDK 8)** was a pivotal release in the evolution of the Java language. It introduced several groundbreaking features, most notably **lambda expressions**, which significantly expanded Java's capabilities and marked the language's full embrace of **functional programming** concepts. Here's an overview of the key features introduced in **Java SE 8**:

### **1. Lambda Expressions**:

- **Lambda expressions** are essentially anonymous functions---functions that don't have a name and can be passed as arguments to methods or stored in variables. They provide a clear and concise way to represent a method interface and significantly reduce the verbosity of code, especially in situations that involve the use of collections, event handling, and thread management.

- **Example**:

```java
List<String> list = Arrays.asList("apple", "banana", "orange");
list.forEach(fruit -> System.out.println(fruit));
```

### **2. Functional Interfaces**:

- Lambda expressions work with **functional interfaces**---interfaces that have exactly one abstract method. Common examples are `Runnable`, `Callable`, and custom interfaces. To indicate an interface is intended to be functional, Java 8 introduced the `@FunctionalInterface` annotation.

- Example of a custom functional interface:

```java
@FunctionalInterface
interface MyFunction {
    void apply();
}
MyFunction func = () -> System.out.println("Lambda called!");
func.apply();
```

### **3. Stream API**:

- The **Stream API** provides a powerful abstraction for processing sequences of elements (like collections) in a functional programming style. Streams allow for easy manipulation of data, such as filtering, mapping, and reducing, often in a **declarative** and **lazy** manner.

- **Example**:

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
numbers.stream()
    .filter(n -> n % 2 == 0)
    .forEach(System.out::println); // Prints even numbers: 2, 4
```

### **4. Default Methods (Defender Methods) in Interfaces**:

- Prior to Java 8, if a new method was added to an interface, all implementing classes had to implement it. **Default methods** solved this issue by allowing interfaces to have methods with default implementations. This allowed interfaces to evolve without breaking the code of existing implementations.

- **Example**:

```java
interface MyInterface {
    void normalMethod();
    default void defaultMethod() {
        System.out.println("This is a default method.");
    }
}

class MyClass implements MyInterface {
    public void normalMethod() {
        System.out.println("Normal method implementation.");
    }
}

MyClass obj = new MyClass();
obj.normalMethod();     // Prints: Normal method implementation.
obj.defaultMethod();    // Prints: This is a default method.
```

### **5. Method References**:

- **Method references** provide a shorthand for writing lambda expressions. Instead of using a lambda expression to call a method, you can simply refer to the method by its name.

- **Example**:

```java
List<String> list = Arrays.asList("apple", "banana", "orange");
list.forEach(System.out::println); // Method reference to println
```

### **6. Optional Class**:

- **`Optional`** is a container object that may or may not contain a non-null value. It's used to avoid `NullPointerException` and to handle the absence of a value more gracefully, rather than relying on `null`.

- **Example**:

```java
Optional<String> optional = Optional.ofNullable(null);
System.out.println(optional.orElse("Default Value"));  // Prints: Default Value
```

### **7. New Date and Time API (java.time)**:

- Java SE 8 introduced the **java.time** package to provide a more comprehensive, fluent, and flexible date and time handling mechanism compared to the outdated `java.util.Date` and `java.util.Calendar`.

- The **`LocalDate`, `LocalTime`, `LocalDateTime`**, and **`ZonedDateTime`** classes represent immutable date-time objects and are much easier to work with than the old date-time APIs.

- **Example**:

```java
LocalDate today = LocalDate.now();
LocalTime currentTime = LocalTime.now();
LocalDateTime dateTime = LocalDateTime.of(today, currentTime);
System.out.println(today);       // Prints current date (e.g., 2024-10-03)
System.out.println(currentTime); // Prints current time (e.g., 14:45:30)
System.out.println(dateTime);    // Prints date and time together
```

### **8. Nashorn JavaScript Engine**:

- **Nashorn** is a new, lightweight, high-performance JavaScript engine introduced in Java SE 8. It allows developers to embed JavaScript code inside Java applications and interact with Java objects from JavaScript.

### **9. Type Annotations**:

- Java SE 8 extended the annotation support in Java to **types**, allowing annotations to be applied anywhere a type is used (e.g., in generics, type casts, etc.).

- **Example**:

```java
@NonNull String str;
List<@NonNull String> strings;
```

### **10. Parallel Arrays (Parallel Stream API)**:

- Java SE 8 introduced parallel processing capabilities with the **parallelStream()** method, enabling efficient parallel operations on collections, taking full advantage of multi-core systems.

- **Example**:

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
numbers.parallelStream().forEach(System.out::println);  // Process elements in parallel
```

### **Significance of Java SE 8**:

- **Lambda expressions** and the **Stream API** revolutionized how Java developers write code, enabling a more **functional programming** style that is both more concise and expressive. Java SE 8's default methods made interface evolution easier, while the **java.time** package modernized date and time handling.

- This version laid the foundation for concurrent, parallel computing, and more expressive data manipulation, making it a major milestone in the evolution of the Java language. The additions made Java more versatile, powerful, and able to handle modern development challenges more effectively.

**Java SE 9 (JDK 9)** introduced a major shift in the Java ecosystem with the introduction of **modules**, along with other significant enhancements. The JDK 9 release represented another key milestone, affecting both the Java language and its API libraries. Here's a breakdown of the most important features:

### **1. Modular System (Project Jigsaw)**

- The biggest and most impactful feature in **JDK 9** was the introduction of the **Java Platform Module System** (JPMS), also known as **Project Jigsaw**. This system allows developers to organize their code into **modules**, which can specify their dependencies and control access to internal code.

- Modules make it easier to manage large applications by providing better separation of concerns and stronger encapsulation. It introduced a new syntax element: the `module-info.java` file, which defines the dependencies and accessibility of modules.

**Example of `module-info.java`:**

```java
module my.module {
requires another.module;
exports com.mycompany.mypackage;
}
```

- With modules, Java's JDK is now modularized, meaning the JDK is broken down into several modules, reducing memory footprint by loading only the necessary modules for an application.

- **JLink** is another important tool introduced in Java SE 9, enabling the creation of a custom runtime image that contains only the modules required by the application, improving efficiency and performance.

### **2. JShell (Java REPL)**

- **JShell** is a new tool introduced in **Java SE 9** that provides an **interactive read-eval-print loop (REPL)** for Java. This allows developers to experiment with Java code snippets interactively without needing to write a complete program, making it easier to prototype and learn Java.

**Example:**

```shell
jshell> int x = 5;
jshell> x + 10;
$2 ==> 15
```

### **3. Private Methods in Interfaces**

- In **Java SE 9**, interfaces were enhanced further by allowing **private methods**. These private methods can be used to share code between **default** and **static methods** in interfaces, helping to reduce code duplication.

**Example of private method in an interface:**

```java
interface MyInterface {
    default void method1() {
        helper();
    }

    static void method2() {
        helper();
    }

    private static void helper() {
        System.out.println("Helper method.");
    }
}
```

### **4. Improved Javadoc with Search Capability**

- **Javadoc** in **Java SE 9** was enhanced by adding a **search feature**, making it easier to locate information within the generated documentation. The new `@index` tag allows developers to add keywords that can be indexed for the search feature.

### **5. Deprecated Applet API**

- With **JDK 9**, the **applet API** was deprecated. This deprecation came as browsers gradually stopped supporting Java applets due to security concerns and the rise of alternative technologies (such as HTML5). As a result, applets were no longer recommended for use in new projects.

### **6. Stream API Enhancements**

- Several new methods were added to the **Stream API** in Java SE 9, improving its functionality and versatility. Some of the important additions include:

- `takeWhile()`: Returns elements until a condition is met.

- `dropWhile()`: Discards elements while a condition is met, returning the remaining elements.

- `ofNullable()`: Creates a stream from a nullable value.

- Improved `Optional` handling with new methods like `ifPresentOrElse()`.

### **7. Factory Methods for Collections**

- Java SE 9 introduced **convenience factory methods** for creating immutable instances of collections (e.g., `List`, `Set`, `Map`). This provides an easier way to instantiate collections without boilerplate code.

**Example:**

```java
List<String> list = List.of("A", "B", "C");
Set<Integer> set = Set.of(1, 2, 3);
Map<String, Integer> map = Map.of("key1", 1, "key2", 2);
```

### **8. Process API Updates**

- The **Process API** was updated to improve the handling of operating system processes. This includes the ability to manage process streams and the ability to get more detailed information about processes (e.g., PID, command line, and children processes).

**Example:**

```java
ProcessHandle processHandle = ProcessHandle.current();
System.out.println("Process ID: " + processHandle.pid());
```

### **9. HTTP/2 Client (Incubating Feature)**

- Java SE 9 introduced an **incubating HTTP/2 Client API** that provided support for modern web protocols like HTTP/2 and WebSockets. This feature, while incubating in Java SE 9, provided better performance and efficiency compared to the older `HttpURLConnection` API.

**Example:**

```java
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://example.com"))
    .build();
HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
System.out.println(response.body());
```

### **10. Other Miscellaneous Improvements**

- **Multi-release JARs**: Allows developers to package different versions of class files targeted for specific Java platform versions in a single JAR.

- **Compact Strings**: Java SE 9 introduced compact strings, which optimize memory usage for strings by using byte arrays rather than `char[]` when possible.

### **Significance of Java SE 9**

- The **module system** was a major architectural change that restructured the Java platform, making it easier to manage dependencies, improve encapsulation, and support more scalable application development.

- **JShell** revolutionized the way Java developers experiment with code, enabling real-time feedback and rapid prototyping.

- With the modularization of the JDK, developers gained more control over the application footprint and performance, making Java more adaptable to modern development environments, including microservices.

Java SE 9 was a transformative release that laid the groundwork for the future of the Java platform, pushing it forward with better modularity, improved language features, and greater efficiency.

**Java SE 10 (JDK 10)** and **Java SE 11 (JDK 11)** were the next iterations in Java's development cycle, both released under the new time-based release schedule, which introduced new Java versions every six months. Here are the key features of these releases:

### **Java SE 10 (JDK 10)**

Released in **March 2018**, **JDK 10** brought some significant updates, particularly focusing on making Java more flexible and modern. The primary feature of JDK 10 was **local variable type inference**.

#### **1. Local Variable Type Inference (`var`)**

- **Local variable type inference** was the most notable language feature introduced in **JDK 10**. Using the new `var` keyword, developers can allow the type of local variables to be inferred by the compiler from the initializer. This removes the need for explicit type declarations in certain cases, simplifying code and reducing verbosity.

**Example:**

```java
var list = new ArrayList<String>(); // Compiler infers that 'list' is of type ArrayList<String>
for (var item : list) {
    System.out.println(item);       // 'item' is inferred as String
}
```

This feature helped streamline Java code by avoiding redundant type declarations and aligning Java with the practices of other modern programming languages that support type inference.

#### **2. Garbage Collector Interface**

- JDK 10 introduced a **clean garbage collector (GC) interface**, which makes it easier to add and remove garbage collectors. It separates GC code from the rest of the HotSpot VM, making it modular and cleaner.

#### **3. Application Data-Class Sharing (AppCDS)**

- **Application Class-Data Sharing (AppCDS)** was introduced to reduce startup times by sharing common class metadata between Java processes.

#### **4. Parallel Full GC for G1**

- The **G1 Garbage Collector** in JDK 10 was enhanced to support **parallel full garbage collection**, improving the overall performance and reducing GC pauses in applications.

#### **5. Java Version String Redefinition**

- JDK 10 redefined the **Java version string** to reflect the new time-based release model. The versioning format was aligned to make it easier to manage the six-month release cadence.

### **Java SE 11 (JDK 11)**

Released in **September 2018**, **JDK 11** was a **Long-Term Support (LTS)** version, meaning it will receive long-term support from Oracle, unlike the shorter-lived feature releases like JDK 9 and JDK 10. It included new language features, API updates, and the removal of outdated technologies.

#### **1. Use of `var` in Lambda Expressions**

- **JDK 11** extended the use of **`var`** to **lambda expressions**, allowing developers to use the `var` keyword for lambda parameters. This brought more consistency to the language and made lambda syntax more flexible.

**Example:**

```java
(var x, var y) -> x + y
```

#### **2. HTTP Client API**

- JDK 11 introduced the new **HTTP Client API** (in `java.net.http`), providing a modern, efficient, and fully-featured way to handle HTTP communication in Java applications. This API supports both HTTP/1.1 and HTTP/2 protocols, and it offers asynchronous communication using **CompletableFuture**.

**Example:**

```java
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://example.com"))
    .build();
HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
System.out.println(response.body());
```

#### **3. Single-File Source Code Programs**

- JDK 11 added the ability to run **single-file programs** directly without the need for compilation via the `java` launcher. This feature makes it easier for quick prototyping and scripting in Java.

**Example:**

```bash
java HelloWorld.java
```

You can run a Java file without needing to explicitly compile it with `javac`.

#### **4. Deprecation and Removal of Applet and Java Web Start**

- **Applet support** was completely removed in JDK 11, following its deprecation in JDK 9. Applets had long lost relevance due to security concerns and the lack of browser support. Additionally, **Java Web Start**, a technology for launching Java applications from the web, was also removed.

#### **5. Removal of JavaFX from JDK**

- **JavaFX**, the GUI framework that had been bundled with Java in previous versions, was removed from the JDK starting with JDK 11. It became a standalone, open-source project that developers can download and use separately if needed.

#### **6. Z Garbage Collector (Experimental)**

- **JDK 11** introduced the **Z Garbage Collector (ZGC)** as an experimental feature. ZGC is a scalable, low-latency garbage collector designed for applications requiring large heaps with minimal GC pause times.

#### **7. Deprecation of Nashorn JavaScript Engine**

- **Nashorn**, the JavaScript engine included with the JDK, was deprecated in JDK 11. With the advent of more advanced JavaScript engines like V8 (used in Node.js), Nashorn had become outdated.

#### **8. Flight Recorder**

- **Java Flight Recorder (JFR)** was added in JDK 11, providing a powerful tool for profiling and diagnosing applications in production environments.

### **Conclusion**

- **JDK 10** introduced **local variable type inference** with `var`, making Java more concise and modern, aligning with other programming languages that support type inference. It also made improvements in garbage collection and class-data sharing.

- **JDK 11**, being an **LTS release**, was packed with features such as the **HTTP Client API**, **var in lambda expressions**, and **single-file execution**. It also removed deprecated technologies like **applets** and **Java Web Start**, reflecting the evolution of Java in line with modern development practices.

Java SE 11 marked a major milestone as a **long-term support** release, which organizations could adopt for production environments, ensuring stability and continued support for years to come.
