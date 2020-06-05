# Review and Overview
This is the final discussion for CSE11 this quarter, so while the folliwng will review some of the new concepts we learned since the last discussion, I would like to also encourage you to go back and look over the other review sheets, since they provide a decent summary of some of the things we learned this quarter. Those review sheets cover:

- [The boolean `Not` operator, and the String `contains()` method](https://github.com/ucsd-cse11-s20/DI03-Class-Drills-17-04-2020/blob/master/ProblemSheet.md)
- [Interfaces and Abstract Classes](https://github.com/ucsd-cse11-s20/DI04-Interfaces-24-04-2020/blob/master/ProblemSheet.md)
- [Arrays, Variables, and the Main Method](https://github.com/ucsd-cse11-s20/DI05-Arrays-Variables-Loops-01-05-2020/blob/master/ProblemSheet.md)
- [Loops](https://github.com/ucsd-cse11-s20/DI05-Arrays-Variables-Loops-01-05-2020/blob/master/ProblemSheet.md)
- [More on Arrays, and the `null` Operator](https://github.com/ucsd-cse11-s20/DI07-Arrays-and-Lambdas-15-05-2020/blob/master/ProblemSheet.md)
- [Lists and Generics](https://github.com/ucsd-cse11-s20/DI08-Lists-and-Generics-22-05-2020)

Keep in mind that these do **not** cover everything! Other resources include your programming assignments, lecture videos, quizzes and the weekly review slides. And as always, please ask questions on Piazza!

But right now, let's review some of the things we learned about the past two weeks:

## Review

### Bounded numbers
Last week, we learned that the Java `int` type cannot represent all integers, but is in fact bounded between -2<sup>31</sup> and 2<sup>31</sup>-1 (or `[-2147483648, 2147483647)`). If we try to represent a number that is outside of these limits, Java might show and error and not build:

```java
int x = 2147483648
```
Causes:
```
error: integer number too large
   int x = 2147483648;
           ^
```

But if we go out of bounds as a result of some computation (where Java won't know that it is occurring), the numbers instead wrap around from the smallest to the largest possible integer!

```java
// oneMoreThanMaxInt == -2147483648
int oneMoreThanMaxInt = 2147483647  + 1 

// oneLessThanMinInt == 2147483647
int oneLessThanMinInt = -2147483648 - 1
```

### Java Primitives
`int`, `boolean` and `double` are arguable the most useful primitive types in Java, and the default type of `true`/`false`, whole numbers, and decimal numbers. But they are not the only primitives. Here is the list of primitive types in Java, and how to represent them:

```java
// Whole numbers
byte  y = 1;  // Byte,    bounded between -2^7  and 2^7 - 1
short s = 1;  // Short,   bounded between -2^15 and 2^15 - 1
int   i = 1;  // Integer, bounded between -2^31 and 2^31 - 1
long  l = 1L; // Long,    bounded between -2^63 and 2^63 - 1

// Decimal numbers
float  f = 1.0f; // Less "precise" than doubles
double d = 1.0; 

// Special types
char c = 'a'; // Represents a single character. Mostly used when looping over characters in a String

boolean b = false; // Either `true` or `false`
```

Each of these types has a matching Reference Type, and for the number types, you can get their maximum and minimum representable values using the `MAX_VALUE` and `MIN_VALUE` static fields on those classes:

```java
Byte      y = 1;
Short     s = 1;
Integer   i = 1;
Long      l = 1L;
Float     f = 1.0f;
Double    d = 1.0;
Character c = 'a';
Boolean   b = false;
```

For more information about these, and some cool ways you can represent them in Java, see [The Java<sup>TM</sup> Tutorials](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html) on Primitive Data Types!

### Overriding methods
Though it sounds similar, "overriding" and "overloading" methods have two different meanings in Java. Overriding a method means providing a different implementation of an existing method in Java. It comes up most often when a class that extends some superclass wants to behave differently for some method that the superclass provides. We will see examples of this in the [Object section](#the-object-class), but for now, keep in mind that to override a method, the method's header must be _identical_ to the method in the superclass. The body then can be something different.

### The `instanceof` operator
`instanceof` is an operator in Java that, though not frequently used, is sometimes necessary. It is used between an object and the name of a class, and returns `true` if that object's type is equal to or is a subclass of that class. However, if the object we are using cannot be an instance of the class, Java will not build, but will instead show an error:

```java
class A {}
class B extends A {}
class C extends A {}

// ...

A a = new A();
B b = new B();
C c = new C();

boolean isAAnInstanceOfA = a instanceof A; // true

boolean isBAnInstanceOfA = b instanceof A; // true
boolean isBAnInstanceOfB = b instanceof B; // true

boolean isCAnInstanceOfA = c instanceof A; // true
boolean isCAnInstanceOfC = c instanceof C; // true


// The following will fail to build with the message:
// error: incompatible types: C cannot be converted to B
boolean isCAnInstanceOfB = c instanceof B;

// ...
```

### Casting
Similar to `instanceof`, casting is a Java feature that is used mostly only where necessary. We have seen that we can create an instance of a class, and assign it to a field or variable of a superclass's type. Java will then use treat that instance as having the type of the superclass, hiding any fields or methods that is only defined on the subclass. But sometimes, we might need to directly access a field or method that is only defined on the subclass. In these cases, we can _cast_ the object to the subclass type, which forces Java to treat that object as an instance of the subclass, giving us access to the fields or methods we need:

```java
class A {}

class B extends A {
    int x;

    B(int x) {
        this.x = x;
    }
}

class C extends B { // Note that this is different to the example above!
    int y;

    C(int x, int y) {
        super(x);
        this.y = y;
    }
}

// ...
A a = new B(1);
A a2 = new C(2, 3);

// This will show the following error message:
// error: cannot find symbol
//   int error = a.x;
//                ^
// symbol:   variable x
// location: variable a of type A
int error = a.x;

// This works, and assigns 1 to x
B b = (B) a;
int x = b.x;

// We don't have to create a new variable, but then
// we need to wrap the cast object with parentheses
int cX = ((B) a2).x;
int cY = ((C) a2).y;

// ...
```

### The `Object` class
_All_ classes in Java, including all that we write, implicitly extend the built-in `Object` class. This class provides some useful methods that we can override. We saw two of these methods last week:

```java
class Object {

    // ...

    /**
     * Indicates whether some other object is "equal to" this one.
     * ...
     */
    public boolean equals(Object obj) { /* ... */ }

    // ...

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * ...
     */
    public String toString() { /* ... */ }

    // ...
}
```

We can provide our own implementation of these methods by overriding them in the classes we define:

```java
class Point {
    int x;
    int y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // ...

    // Notice that the header is identical to the one
    // defined in `Object`
    public String toString() { 
        return "(" + this.x + ", " + this.y + ")";
    }

    
    public boolean equals(Object obj) { 
        boolean rs = false;
        if (obj != null && obj instanceof Point) {
            Point other = (Point) obj;
            rs = this.x == other.x && this.y == other.y;
        }
        return rs;
    }
}
```

Most pre-defined classes in Java also override these methods to give us better String outputs (see the `Boolean` class for instance) and let us check equality (see the `String` class for example). One notable exception to this are arrays. Even though arrays are reference types and we can call `toString` and `equals` on them, the `toString` does not give us a human-readable output, and the `equals` does not call compare the array's contents using their `equals` methods, which can have surprising results. Fortunately, Java provides the `Arrays` class as a helper class which defined `toString` and `equals` _statis_ methods we can call on arrays to perform these operations:

```java
import java.util.Arrays;

// ...

String[] arrOne = {"a", "b", "c"};
String[] arrTwo = {"a", "b", "c"};

boolean equalsWithoutHelper = arrOne.equals(arrTwo); // false
boolean equalsWithHelper = Arrays.equals(arrOne, arrTwo); // true

String toStringWithoutHelper = arrOne.toString(); // Something similar to: "[Ljava.lang.String;@14acaea5"
String toStringWithHelper = Arrays.toString(arrOne); // "[a, b, c]"

// ...
```

### Access Modifiers
For most of this quarter, we have been writing all related pieces of code in a single file. However, Java allows us to organize our code by placing it in multiple files in multiple directories (called "packages") and `import` them into other files in other directories. However, with this, Java also limits each piece of code's access to other parts of the program. So to define this access ourselves, we need to use the `public`, `protected` and `private` access modifiers to clearly indicate the access to different classes, fields and methods.

1. The `public` modifier allows access anywhere.
2. The `protected` modifier allows access anywhere within the same package, or in any of the subclasses of the protected class.
3. The `private` modifier only allows access within the class that contains them.
4. No modifier allows access anywhere within the same package. (You might hear this referred to as "package visibility" or being "package private")

The [pre-recorded lecture videos](https://drive.google.com/drive/folders/1OnchIdC7C2XojUB8xI6sCih2zsUGSuhi?usp=sharing) cover this in much more detail, but here is a short example on the `private` modifier:

```java
class A {
    private int usesOfX;
    private int x;

    A(int x) {
        this.x = x;
        this.usesOfX = 0;
    }

    public int getX() {
        this.usesOfX += 1;
        return this.x;
    }

    public void setX(int x) {
        this.usesOfX += 1;
        this.x = x;
    }
}
```

### Recursion
Recursion is not a particular Java feature, but rather a common topic in Computer Science in general. It refers to the idea of a method that may call itself, and while there are some really interesting properties for methods like this, there isn't anything particularly special about them, and in fact we have been writing recursive methods since very early in this course. Thinking of recursive methods as such can be somewhat more challenging though, so I put the topic here to encourage you to revisit and understand the trace of the [File System](https://drive.google.com/file/d/17X29IkHTGWzX0fuxXbLPocEOiS2kG6aM/view?usp=sharing) code we wrote in the final lectures.