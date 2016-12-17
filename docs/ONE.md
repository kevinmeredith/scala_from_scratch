# Scala From Scratch 

## Intro to Scala

### Agenda

* REPL
* `var` versus `val`
* Class
* Abstract Class
* Trait
* Object
* How to Run a Program
* Case Class
* Equality
* Collections
* Recursion
* Currying
* Partial Application
* Bread and Butter List Functions
* Try, Option, and Either
* Programming with Types

### REPL

* Read-Eval-Print-Loop
* Very useful for understanding code

### `var` versus `val`

* `var` - mutable reference
* `val` - immutable reference
* Says nothing about the referenced value
  * could be mutable or immutable

```
var x = 42
x = 66 // legal

val y = 66
y = 42 // illegal
```

### Class

* "A class is a blueprint for objects. Once you define a class, 
   you can create objects from the class blueprint with the 
   keyword new." (Prog in Scala)

```
class FooMutable {
  var value = 0

  // Increase internal counter by 1, 
  // returning the new counter
  def increment(): Int = 
    value += 1

  // Same as decrement, but subtract counter
  // by 1
  def decrement(): Int = 
    value -= 1
}
```

Let's use it from the Scala REPL.

* REPL - Read/Eval/Print/Loop
* Useful for understanding code to try it out

```
val x = new FooMutable
x.increment
x.decrement
```

* We forgot to make `value` to be immutable.
 * We defined a public contract of `increment` and `decrement`
 * `FooMutable`'s client could perform operations
    other than `increment` and `decrement`


```
class FooMutableFixed {
  // Keep it private to avoid a client's manipulation
  private var value = 0

  // Increase internal counter by 1,
  // returning the new counter
  def increment(): Unit =
    value += 1

  // Same as decrement, but subtract counter
  // by 1
  def decrement(): Unit =
    value -= 1

  // Output current `value`
  def current: Int = value

}
```

```
val x = new FooMutableFixed
x.value // compile-time error
x.increment
x.current
x.decrement
x.current
```

* It's standard to put `()` on a method call if it has a side effect
  * http://docs.scala-lang.org/style/method-invocation.html
* Side effect - "modifies some state outside its scope or has an 
  observable interaction with its calling functions or the outside world."
  - https://en.wikipedia.org/wiki/Side_effect_(computer_science)
* When learning Scala, avoid `var`'s, to embrace Functional Programming(FP)
  * FP in Scala favors immutability 
* Why? Immutability makes it easier to reason about/understand code
  * Story - work experience

``` 
class FooImmutable(val value: Int) {
 def increment = new FooImmutable(value + 1)
 def decrement = new FooImmutable(value - 1) 
}
```

```
val x = new FooImmutable(42)
x.increment
x.decrement
x.increment.decrement
```

* `value` is immutable and private
* To make it private, remove `val` 
* Observe that there's no `()` appended to each method's name

### Abstract Class

* Cannot be directly instantiated
* Has constructor parameters
* Can define fields and methods

```
// http://docs.oracle.com/javase/tutorial/java/IandI/abstract.html
abstract class GraphicObject() {
  def draw(): Unit
}
```

### Trait

* Similar to a Java `Interface`, but it can:
  * Define fields and implement methods
  * "Unlike class inheritance, in which each class must inherit from just
    one superclass, a class can mix in any number of traits" (Prog in Scala, 3rd edition).
* No parameters allowed

```
trait HasBrain {
  def intelligentQuote: String
}

trait HasMouth {
  def talk: String 
}
```

```
class Human extends HasBrain with HasMouth {
  override def intelligentQuote = "I think, therefore I am."

  override def talk = "blah blah blah"
}

class Dog extends HasBrain with HasMouth {
  override def intelligentQuote = "my owner is home - time to eat!"

  override def talk = "woof"
}
```

* "If the behavior will not be reused, then make it a concrete class. It is not
  reusable behavior after all.
  If it might be reused in multiple, unrelated classes, make it a trait. Only
  traits can be mixed into different parts of the class hierarchy.
  If you want to inherit from it in Java code, use an abstract class" (Prog in Scala, 3rd edition).


### Object

* Unlike Java, Scala does not have `static` members.
* "Sometimes, you want to have variables that are common to all objects" 
  (https://docs.oracle.com/javase/tutorial/java/javaOO/classvars.html).

```
// Java
public class MathUtility {
 public static int add(int x, int y) {
   return x + y;
 }
}
```

* "Methods and values that aren’t associated with individual instances of a class belong in singleton objects"
  (http://docs.scala-lang.org/tutorials/tour/singleton-objects.html)

```
object MathUtility {
  val Pi = 3.14

  def add(x: Int, y: Int): Int      = x + y
}
```

* A `companion` object shares the same name as a class in the same source

#### Person.scala

```
object Person {
  def reversedNames(first: String, last: String): String = 
    last + "," + first
}
class Person(first: String, last: String) 
```
* Additionally, a `companion` object has access to its companion 
  class's private fields, and vice-versa

```
object A {
 private val objX = 42
}
class A {
 private val classX = 66
}
```
 
* "If you are a Java programmer, one way to think of singleton objects is
   as the home for any static methods you might have written in Java" (Prog in Scala).

### How to Run a Program

```
object Foo {
 def main(args: Array[String]): Unit = {
   println("hello world!")
 }
```

`Application`

* Scala provides another way to run an application:

```
object Foo extends App {
  // put main code here
  println("hello world!")
}
```

* However, due to initialization nuances, I recommend using `def main`, avoiding
  `App`. (http://www.scala-lang.org/api/2.12.x/scala/App.html)

```
scala> object F extends App {
     |   val x = 42
     |   println("hi")
     | }
defined object F

scala> F.x
res0: Int = 0

scala> F.main(Array.empty)
"hi"

scala> F.x
res2: Int = 42
```

### Case Classes

* Immutable by default
* Decomposable through pattern matching
* Compared by structural equality instead of by reference
* Succinct to instantiate and operate on

- http://docs.scala-lang.org/tutorials/tour/case-classes.html

```
case class Person(name: String, age: Int)
```

```
// Java implementation, but missing features of case classes
public class Person {
  private final String name;
  private final int age;

  public Person(final String name, final int age) {
    this.name = name;
    this.age = age;
  }

  @Override 
  public boolean equals(Object o) {
   final boolean result;

    if(o instanceof Person) {
      Person other = (Person) o;
      result = this.name.equals(other.getName()) && this.age == other.getAge();
    }
    else {
      result = false;
    }
    return result;
  }

  public String getName() {
    return this.name;
  }

  public int getAge() {
    return this.age;
  }
```

#### Immutable by Default

```
scala> case class F(a: Int) {
     |   a = 666
     | }
<console>:12: error: reassignment to val
         a = 666
           ^
```

#### Decomposable through Pattern Matching

```
def namePlusAge(p: Person): String = p match {
  case Person(name, age) => name + age.toString
}

scala>namePlusAge( Person("Jane", 33) )
res4: String = Jane33
```

### Equality

* Use `==` to compare value (stack) or reference types (heap)
* `==` "routes to .equals, except that it treats nulls properly" (http://stackoverflow.com/a/7681243/409976)
* To compare objects via "reference equality," use `eq` (rarely used)

#### Value Types

* scala.Double, scala.Float, scala.Long, scala.Int, scala.Char, scala.Short, and scala.Byte are the numeric value types.
* scala.Unit and scala.Boolean
  -http://www.scala-lang.org/api/2.12.x/scala/AnyVal.html

#### Reference Types

* objects that are `new`'d, i.e. stored on the heap

#### `==` Examples

```
42 == 42 // outputs true

// Recall that case classes provide a reasonable
// 'equals' implementation
case class Person(id: Long, name: String)
Person(42, "Joe") == Person(42, "Joe") // outputs true
```

### Collections

#### List - `List[A]`

* Linked List (https://en.wikipedia.org/wiki/Linked_list)

- [1] --> [2] --> [3] --> Nil (termination of list)

* The `bread` in bread and butter of Scala and Functional Programming
* Bunch of elements with constant prepend, i.e. add to head, but linear append, add to tail

```
1 :: List[Int](2,3) // prepend
List[Int](2,3) :+ 4 // append
```

#### Set - `Set[A]`

* Collection of elements that contains no duplicates
* Calling `Set(1,2,3)` in Scala will, by default, use a `HashSet`.
* `HashSet` uses a Hash Trie (http://docs.scala-lang.org/overviews/collections/concrete-immutable-collection-classes.html#hash_tries)
  * Ensures a "reasonably fast lookups and reasonably efficient functional insertions and deletions"

```
Set[Int](1,2,3)
```

#### Map - `Map[K, V]`

* Consists of key-value pairs

```
val map = Map[Int, String]( (1, "hello"), (2, "world") )
map.get(1)  // returns Some("hello")
map.get(42) // returns None
```

### Recursion

* "Recursion occurs when a thing is defined in terms of itself or of its type."
  * https://en.wikipedia.org/wiki/Recursion

```
def factorial(n: Int): Int = {
  if (n < 0)       throw new RuntimeException("Invalid input. Must be >= 0.")
  else if (n == 0) 1 
  else             n * factorial(n - 1)
}
```

Let's break down `factorial(3)`:

```
factorial(3) 
3 * factorial(2)
3 * 2 * factorial(1)
3 * 2 * 1 * factorial(0)
3 * 2 * 1 * 1
```

Let's look at a possible implementation using Java:

```
public static int factorial(int x) {
  int start = 1;

  if(x < 0) {
    throw new RuntimeException("Invalid input. Must be >= 0.");
  }
  else {
    while(x != 0) {
      start = x * start;
      x--;
    }
    return start;
  }
}
```

* We cannot reason about the Java `factorial` method in the same way
* It's necessary to keep track of state, i.e. `start`

### Currying

* "technique of translating the evaluation of a function that takes multiple arguments ... 
   into evaluating a sequence of functions, each with a single argument."
   - https://en.wikipedia.org/wiki/Currying


```
def add2(x: Int, y: Int): Int = x + y 

def add2Curried(x: Int)(y: Int): Int = x + y 
```

```
scala> add2Curried(5)(_)
res11: Int => Int = ...

scala> res11(10)
res12: Int = 15

scala> res11(42)
res13: Int = 47

```

### Partial Application

```
def add2(x: Int, y: Int): Int = x + y

scala> val partiallyApplied: Int => Int = add2(10, _)
partiallyApplied: Int => Int = ...

scala> partiallyApplied(10)
res14: Int = 20

scala> partiallyApplied(5)
res15: Int = 15
```

### Bread and Butter List Functions

* `map`

```
def map[A, B](list: List[A], f: A => B): List[B] = list match {
   case x :: xs => f(x) :: map(xs, f) 
   case Nil     => Nil
}
```

```
scala> def add1(x: Int): Int = x + 1
add1: (x: Int)Int

scala> map( List(1,2,3), add1 )
res3: List[Int] = List(2, 3, 4)
```

* `filter`

```
def filter[A](list: List[A], pred: A => Boolean): List[A] = list match {
   case x :: xs => if ( pred(x) ) x :: filter(xs, pred) else filter(xs, pred)
   case Nil     => Nil
}
```

```
scala> def even(x: Int): Boolean = (x % 2) == 0
even: (x: Int)Boolean


scala> filter( List(1,2,3), even )
res3: List[Int] = List(2)
```

* `foldLeft`

```
def foldLeft[A, B](xs: List[A])(accumulator: B)(f: (B, A) => B): B = xs match {
  case x :: xs => foldLeft( xs )( f(accumulator, x) )( f )
  case Nil     => accumulator
}
```

```
scala> def add(x: Int, y: Int): Int = x + y
add: (x: Int, y: Int)Int

scala> List(1,2,3).foldLeft(0)(add)
res20: Int = 6
```

```
0 - foldLeft( List(1,2,3) )( 0 )       (add) 
1 - foldLeft( List(2,3)   )( add(0, 1))(add)
2 - foldLeft( List(2,3)   )( 1 )       (add)
3 - foldLeft( List(3)     )( add(1, 2))(add)
4 - foldLeft( List(3)     )(3)         (add)
5 - foldLeft( List()      )( add(3, 3))(add)
6 - foldLeft( List()      )( 6 )       (add)
7 - 6 // complete

```

* `find`

```
def find[A](list: List[A], pred: A => Boolean): Option[A] = 
  list.foldLeft[Option[A]](None) { (acc: Option[A], elem: A) => 
    acc.orElse {
      if( pred(elem) ) Some(elem) else None
    }
  }

scala> find(List(1,2,3), { x: Int => x == 3 } )
res5: Option[Int] = Some(3)

scala> find(List(1,2,3), { x: Int => x == 42 } )
res6: Option[Int] = None
```

### Try, Option and Either

* `Try` is a data structure that encapsulates the `Success`
   and `Failure`, i.e. threw a non-fatal exceptionm cases.

```
sealed trait Try[+A]
case class Success[+A](value: A) extends Try[A]
case class Failure(t: Throwable) extends Try[Nothing] 
```

```
import scala.util.{Try, Success, Failure}

scala> Try { "42".toInt } match {
     |   case Success(int) => s"converted to int: $int"
     |   case Failure(_)   => "not an int"
     | }
res22: String = converted to int: 42

scala> Try { "foobar".toInt } match {
     |   case Success(int) => s"converted to int: $int"
     |   case Failure(_)   => "not an int"
     | }
res23: String = not an int
```

* "This method will ensure any non-fatal exception is caught and a Failure object is returned."
  - http://www.scala-lang.org/api/2.11.8/#scala.util.Try$
  * "Will not match fatal errors like VirtualMachineError (for example, OutOfMemoryError and StackOverflowError, ..."
    - http://www.scala-lang.org/api/2.11.8/#scala.util.control.NonFatal$

```
scala> Try { throw new OutOfMemoryError("!") } match {
     |   case Success(a) => "good"
     |   case Failure(t) => "bad"
     | }
java.lang.OutOfMemoryError: !
```

* Option is a data structure that represents the presence
  or absence of a value.
* Superior alternative to `null`

```
sealed trait Option[+A]
case class Some[+A](x: A) extends Option[A]
case object None extends Option[Nothing]
```

```
case class Person(id: Long)

def find(persons: List[Person], id: Long): Option[Person] = 
  persons.find(_.id == id)

scala> find( List(Person(42)), 42 )
res0: Option[Person] = Some(Person(42))

scala> find( List(Person(1)), 66 )
res2: Option[Person] = None
```

* Either - has two states: `Left` and `Right`
  * When representing success and failure states, `Left`, by convention
    represents the failure case
* More detailed (and better in my experience) choice for handling failures

```
sealed trait Either[+A, +B]
case class Right[B](x: B) extends Either[Nothing, B]
case class Left[A](x: A)  extends Either[A, Nothing]
```

```
sealed trait DivError
case object NegativeNumber extends DivError
case object DivByZero      extends DivError

def divPositiveByN(input: Int, n: Int): Either[DivError, Int] = {
  if (n < 0)       Left(NegativeNumber)
  else if (n == 0) Left(DivByZero)
  else             Right( input / n )
}

scala> divPositiveByN(10, -3)
res9: Either[DivError,Int] = Left(NegativeNumber)

scala> divPositiveByN(10, 0)
res10: Either[DivError,Int] = Left(DivByZero)

scala> divPositiveByN(10, 10)
res11: Either[DivError,Int] = Right(1)
```

### Programming with Types

* Benefits
  * Detecting Errors
  * Abstraction
  * Documentation 

* `???`
  * can be used for marking methods that remain to be implemented.
    http://www.scala-lang.org/api/2.11.8/index.html#scala.Predef$@???:Nothing


```
case class User(id: Long)

object UserRepository {
  sealed trait UserLookupError
  case class DbLookupError(t: Throwable) extends UserLookupError

  sealed trait FailedDeleteUser
  case object UserNotFound               extends FailedDeleteUser
  case class DbDeleteError(t: Throwable) extends FailedDeleteUser


  case object NegativeId extends UserLookupError with FailedDeleteUser
}
trait UserRepository {
  import UserRepository._

  def find(id: Long): Either[UserLookupError, Option[User]] = ???

  def delete(id: Long): Either[FailedDeleteUser, Unit]        = ???
}
```

## References

* Programming in Scala, 3rd Edition; (Odersky, Spoon, and Venners)