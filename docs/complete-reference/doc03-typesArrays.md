### **Arrays in Java**

In Java, an **array** is a data structure that stores a fixed-size sequential collection of elements of the same type. Arrays are used to store multiple values in a single variable, rather than declaring separate variables for each value. Each element in the array is identified by an index, starting from 0.

#### **One-Dimensional Arrays**

A **one-dimensional array** is a list of variables of the same type, accessible by a single index. The length of the array is determined when the array is created and cannot be changed once set.

##### **Declaring an Array**
You can declare an array as follows:
```java
type[] arrayName;  // or: type arrayName[];
```
For example:
```java
int[] numbers;
```
This creates an array reference but does not allocate memory for the array elements.

##### **Allocating Memory**
To allocate memory for the array, you use the `new` keyword:
```java
arrayName = new type[size];
```
For example:
```java
numbers = new int[10];  // Allocates memory for 10 integers.
```

You can combine the declaration and allocation into a single statement:
```java
int[] numbers = new int[10];  // Declares and allocates memory for an array of 10 integers.
```

##### **Accessing Elements**
You can access array elements using the array index:
```java
numbers[0] = 1;  // Assigns 1 to the first element of the array.
System.out.println(numbers[0]);  // Prints the first element of the array.
```
Array indexes start at 0, so the first element is at index 0, the second element is at index 1, and so on.

##### **Initializing Arrays**
Arrays can be initialized when declared:
```java
int[] numbers = {1, 2, 3, 4, 5};  // Initializes the array with five values.
```

##### **Length of an Array**
You can find the length of an array using the `length` attribute:
```java
System.out.println(numbers.length);  // Prints the length of the array.
```

##### **Example: Array of Integers**
```java
int[] numbers = new int[5];
numbers[0] = 10;
numbers[1] = 20;
numbers[2] = 30;
numbers[3] = 40;
numbers[4] = 50;

for (int i = 0; i < numbers.length; i++) {
    System.out.println(numbers[i]);
}
```

### **Multidimensional Arrays**

Java also supports **multidimensional arrays**, which are arrays of arrays. The most common type is the **two-dimensional array**, but arrays can have as many dimensions as needed.

#### **Two-Dimensional Arrays**

A **two-dimensional array** is essentially an array of arrays. You can think of it as a table with rows and columns, where each element is accessed by two indexes: one for the row and one for the column.

##### **Declaring a Two-Dimensional Array**
```java
int[][] matrix;
```
This creates a reference to a two-dimensional array but does not allocate memory for the array.

##### **Allocating Memory**
To allocate memory for a two-dimensional array, you use `new`:
```java
matrix = new int[3][4];  // Allocates a 3x4 array.
```
Here, `matrix` is a 2D array with 3 rows and 4 columns.

##### **Accessing Elements**
You can access the elements of a two-dimensional array using two indexes:
```java
matrix[0][0] = 10;  // Assigns 10 to the first element of the array.
System.out.println(matrix[0][0]);  // Prints the first element of the array.
```

##### **Initializing a Two-Dimensional Array**
You can initialize a two-dimensional array as follows:
```java
int[][] matrix = {
    {1, 2, 3, 4},
    {5, 6, 7, 8},
    {9, 10, 11, 12}
};
```

##### **Example: Two-Dimensional Array**
```java
int[][] matrix = new int[3][3];
matrix[0][0] = 1;
matrix[0][1] = 2;
matrix[0][2] = 3;
matrix[1][0] = 4;
matrix[1][1] = 5;
matrix[1][2] = 6;
matrix[2][0] = 7;
matrix[2][1] = 8;
matrix[2][2] = 9;

for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 3; j++) {
        System.out.print(matrix[i][j] + " ");
    }
    System.out.println();
}
```
This will output the following:
```
1 2 3 
4 5 6 
7 8 9 
```

#### **Jagged Arrays (Irregular Arrays)**

In Java, multidimensional arrays don’t need to have uniform sizes. You can create **jagged arrays** where each sub-array can have a different length:
```java
int[][] jaggedArray = new int[3][];
jaggedArray[0] = new int[2];  // First row has 2 columns.
jaggedArray[1] = new int[4];  // Second row has 4 columns.
jaggedArray[2] = new int[3];  // Third row has 3 columns.
```

#### **Higher Dimensional Arrays**
You can declare and use arrays with more than two dimensions:
```java
int[][][] threeD = new int[3][4][5];  // A 3D array with dimensions 3x4x5.
```

### **Strings in Java**

In Java, a **string** is an object that represents a sequence of characters. The `String` class is part of the Java standard library and provides a wide range of methods for manipulating and processing strings.

#### **Declaring a String**
Strings are declared like this:
```java
String str = "Hello, World!";
```

#### **String Characteristics**
- **Immutable**: Strings in Java are **immutable**, meaning that once a string is created, it cannot be changed. Any modification to a string creates a new string object.
  
- **String Pool**: Java maintains a pool of string literals. When a string is created as a literal, it is stored in the string pool, and the same string literal is reused to save memory.

#### **Common String Operations**

- **Concatenation**: You can concatenate two strings using the `+` operator:
  ```java
  String greeting = "Hello, " + "World!";
  ```

- **Length of a String**: You can find the length of a string using the `length()` method:
  ```java
  int len = greeting.length();  // Returns the length of the string.
  ```

- **Accessing Characters**: You can access individual characters in a string using the `charAt()` method:
  ```java
  char ch = greeting.charAt(0);  // Gets the first character ('H').
  ```

- **Substring**: You can extract a substring from a string using the `substring()` method:
  ```java
  String sub = greeting.substring(7);  // Extracts "World!".
  ```

- **String Comparison**: Use the `equals()` method to compare two strings for equality:
  ```java
  String s1 = "Java";
  String s2 = "Java";
  boolean isEqual = s1.equals(s2);  // Returns true.
  ```

#### **String Methods**
The `String` class provides a variety of useful methods for manipulating strings:

- `toUpperCase()` and `toLowerCase()`: Convert a string to upper or lower case.
- `replace()`: Replace characters or substrings in a string.
- `trim()`: Removes leading and trailing whitespace from a string.
- `split()`: Splits a string into an array based on a delimiter.

##### **Example: String Operations**
```java
String s = "Java Programming";
System.out.println(s.length());            // Output: 16
System.out.println(s.charAt(5));           // Output: P
System.out.println(s.substring(5));        // Output: Programming
System.out.println(s.toUpperCase());       // Output: JAVA PROGRAMMING
System.out.println(s.toLowerCase());       // Output: java programming
```

#### **String Arrays**
You can also create arrays of strings, just as with any other data type:
```java
String[] words = {"Java", "Python", "C++"};
System.out.println(words[0]);  // Output: Java
```

#### **Immutable Nature of Strings**
As mentioned earlier, strings are immutable. If you modify a string, a new object is created:
```java
String s = "Hello";
s = s + " World";  // Creates a new string "Hello World"
```

### **Summary**

- **Arrays**: Arrays in Java are used to store multiple values of the same type. Arrays can be one-dimensional or multidimensional. Java also supports jagged arrays.
- **Multidimensional Arrays**: These are arrays of arrays and can be irregular. They allow more complex data structures, such as tables or matrices.
- **Strings**: Strings in Java are immutable objects of the `String` class, used to represent sequences of characters. Strings provide powerful methods for manipulation, concatenation, and comparison
