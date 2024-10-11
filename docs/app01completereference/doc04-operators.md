
Java provides a rich set of operators that allow you to perform various operations on variables and values. Here's a detailed overview of the different types of operators in Java, categorized by their functionality:

## 1. Arithmetic Operators

Arithmetic operators are used to perform mathematical operations on numerical values.

| Operator | Description                     | Example         |
|----------|---------------------------------|------------------|
| `+`      | Addition                        | `a + b`          |
| `-`      | Subtraction                     | `a - b`          |
| `*`      | Multiplication                  | `a * b`          |
| `/`      | Division                        | `a / b`          |
| `%`      | Modulus (remainder)            | `a % b`          |

**Note**: Division by zero will throw an `ArithmeticException`.

### Example
```java
int a = 10;
int b = 5;
System.out.println(a + b); // 15
System.out.println(a - b); // 5
System.out.println(a * b); // 50
System.out.println(a / b); // 2
System.out.println(a % b); // 0
```

## 2. Relational Operators

Relational operators are used to compare two values. They return a boolean result (`true` or `false`).

| Operator | Description                     | Example          |
|----------|---------------------------------|-------------------|
| `==`     | Equal to                        | `a == b`          |
| `!=`     | Not equal to                    | `a != b`          |
| `>`      | Greater than                   | `a > b`           |
| `<`      | Less than                      | `a < b`           |
| `>=`     | Greater than or equal to       | `a >= b`          |
| `<=`     | Less than or equal to          | `a <= b`          |

### Example
```java
int a = 10;
int b = 5;
System.out.println(a == b); // false
System.out.println(a != b); // true
System.out.println(a > b); // true
System.out.println(a < b); // false
System.out.println(a >= b); // true
System.out.println(a <= b); // false
```

## 3. Logical Operators

Logical operators are used to perform logical operations on boolean values.

| Operator | Description                     | Example               |
|----------|---------------------------------|------------------------|
| `&&`     | Logical AND                     | `a && b`               |
| `||`     | Logical OR                      | `a || b`               |
| `!`      | Logical NOT                     | `!a`                   |

### Example
```java
boolean a = true;
boolean b = false;
System.out.println(a && b); // false
System.out.println(a || b); // true
System.out.println(!a); // false
```

## 4. Bitwise Operators

Bitwise operators operate on bits and perform bit-level operations.

| Operator | Description                     | Example           |
|----------|---------------------------------|--------------------|
| `&`      | Bitwise AND                    | `a & b`            |
| `|`      | Bitwise OR                     | `a | b`            |
| `^`      | Bitwise XOR                    | `a ^ b`            |
| `~`      | Bitwise NOT                    | `~a`               |
| `<<`     | Left shift                     | `a << 2`           |
| `>>`     | Right shift                    | `a >> 2`           |
| `>>>`    | Unsigned right shift           | `a >>> 2`          |

### Example
```java
int a = 5;  // 0101 in binary
int b = 3;  // 0011 in binary
System.out.println(a & b); // 1 (0001)
System.out.println(a | b); // 7 (0111)
System.out.println(a ^ b); // 6 (0110)
System.out.println(~a);     // -6 (inverts bits)
System.out.println(a << 1); // 10 (1010)
System.out.println(a >> 1); // 2 (0010)
```

## 5. Assignment Operators

Assignment operators are used to assign values to variables. They can also combine assignments with operations.

| Operator | Description                     | Example            |
|----------|---------------------------------|---------------------|
| `=`      | Simple assignment               | `a = 5`            |
| `+=`     | Addition assignment             | `a += 5` (same as `a = a + 5`) |
| `-=`     | Subtraction assignment          | `a -= 5` (same as `a = a - 5`) |
| `*=`     | Multiplication assignment       | `a *= 5` (same as `a = a * 5`) |
| `/=`     | Division assignment             | `a /= 5` (same as `a = a / 5`) |
| `%=`     | Modulus assignment              | `a %= 5` (same as `a = a % 5`) |
| `&=`     | Bitwise AND assignment          | `a &= b`            |
| `|=`     | Bitwise OR assignment           | `a |= b`            |
| `^=`     | Bitwise XOR assignment          | `a ^= b`            |
| `<<=`    | Left shift assignment           | `a <<= 2`           |
| `>>=`    | Right shift assignment          | `a >>= 2`           |
| `>>>=`   | Unsigned right shift assignment | `a >>>= 2`          |

### Example
```java
int a = 10;
a += 5;  // a = a + 5
System.out.println(a); // 15
a *= 2;  // a = a * 2
System.out.println(a); // 30
```

## 6. Unary Operators

Unary operators operate on a single operand and perform operations like incrementing, decrementing, negation, and logical negation.

| Operator | Description                     | Example         |
|----------|---------------------------------|------------------|
| `+`      | Unary plus (no effect)         | `+a`             |
| `-`      | Unary minus (negation)         | `-a`             |
| `++`     | Increment                       | `++a` or `a++`   |
| `--`     | Decrement                       | `--a` or `a--`   |
| `!`      | Logical NOT                    | `!a`             |

### Example
```java
int a = 10;
System.out.println(++a); // 11 (pre-increment)
System.out.println(a++); // 11 (post-increment)
System.out.println(a);   // 12
System.out.println(--a); // 11 (pre-decrement)
System.out.println(a--); // 11 (post-decrement)
System.out.println(a);   // 10
```

## 7. Ternary Operator

The ternary operator (`? :`) is a shorthand for `if-then-else` statements.

### Syntax
```java
condition ? expression1 : expression2
```

### Example
```java
int a = 10;
int b = 20;
int max = (a > b) ? a : b; // max will be 20
System.out.println(max);
```

## 8. instanceof Operator

The `instanceof` operator is used to test whether an object is an instance of a specific class or interface.

### Example
```java
String str = "Hello";
boolean result = str instanceof String; // true
System.out.println(result);
```

## 9. Operator Precedence

Operator precedence determines the order in which operators are evaluated in expressions. Operators with higher precedence are evaluated before operators with lower precedence. Here's a simplified list:

1. **Postfix**: `expr++`, `expr--`
2. **Unary**: `++expr`, `--expr`, `+expr`, `-expr`, `~`, `!`
3. **Multiplicative**: `*`, `/`, `%`
4. **Additive**: `+`, `-`
5. **Shift**: `<<`, `>>`, `>>>`
6. **Relational**: `<`, `>`, `<=`, `>=`, `instanceof`
7. **Equality**: `==`, `!=`
8. **Bitwise AND**: `&`
9. **Bitwise XOR**: `^`
10. **Bitwise OR**: `|`
11. **Logical AND**: `&&`
12. **Logical OR**: `||`
13. **Ternary**: `? :`
14. **Assignment**: `=`, `+=`, `-=`, etc. (evaluated right to left)

### Example of Precedence
```java
int a = 10;
int b = 5;
int c = 2;
int result = a + b * c; // result is 20 (b * c evaluated first)
```

## 10. Using Parentheses

You can use parentheses to explicitly control the order of operations and make expressions easier to read. For example:

```java
int result = (a + b) * c; // (a + b) is evaluated first
```
