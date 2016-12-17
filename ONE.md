# Scala From Scratch 

## Intro to Scala

### Agenda

* Class
* Object
* Case Class
* ...

### Class

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

* We forgot to make `value` to be immutable.
  * `FooMutable`'s client could perform operations
    other than `increment` and `decrement`


```
class FooMutableFixed {
  // Keep it private to avoid a client's manipulation
  private var value = 0

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

* When learning Scala, avoid `var`'s, to embrace Functional Programming.
* Why? Mutability makes it harder to reason about code. (Mention Experience at work)



``` 
class FooImmutable(

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



* Note - immutability is more idiomatic/preferred in (general) Scala




  