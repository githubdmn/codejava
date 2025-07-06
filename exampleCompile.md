
## Project Structure and Compilation Instructions

### 1. Directory Structure

In the `src` directory, the project will follow this structure:

```md
src/
└── main/
    └── java/
        └── com/
            └── example/
                ├── app/
                │   ├── App.java
                │   └── Run.java
                └── utils/
                    ├── message/
                    │   └── Message.java
                    └── math/
                        └── Add.java
```
- `/app/`: Contains the main classes for the application.
- `App.java`: The main entry point for the application.
- `Run.java`: A helper class that handles application logic.
- `/utils/`: Contains utility classes for different functionalities.
- `/message/Message.java`: Utility class for handling messages.
- `/math/Add.java`: Utility class for handling math-related operations.

## 2. Compile the Java Files

To compile all the Java files in the `src` directory and store the compiled `.class` files in the `bin` directory, run the following command:

```bash

javac -d bin src/**/*.java
||
javac -d bin src/com/example/app/*.java src/com/example/utils/math/*.java src/com/example/utils/message/*.java
||
find src -name "*.java" | xargs javac -d bin
```

- **`-d bin`**: Specifies that the compiled class files should be placed in the `bin` directory.

- **`src/**/*.java`**: Recursively compiles all `.java` files in the `src` directory.

### 2.1 alias 
- `alias` javaCompile="find src -name "*.java" | xargs javac -d bin"
- `alias` javaClean="rm -rf bin"


## 3. Run the Application

Once the compilation is done, you can run the main class (`App.java`) using the `java` command:

```bash
java -classpath bin main.java.com.example.app.App 
```
```bash
java -classpath bin com.example.app.App
```

- **`-classpath bin`**: Sets the classpath to the `bin` directory, where the compiled `.class` files are stored.

- **`com.example.app.App`**: Specifies the main class to run, using its fully qualified class name (package and class name).
