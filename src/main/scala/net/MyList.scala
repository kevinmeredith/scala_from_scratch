package net

sealed abstract class MyList[+A]
case class Cons[+A](elem: A, rest: MyList[A]) extends MyList[A]
case object Empty                             extends MyList[Nothing]

object MyList {

  // Add an element to the front of a list
  // Example:  prepend( 10, [1,2] ) == [10,1,2]
  def prepend[A](elem: A, list: MyList[A]): MyList[A] = list match {
    case Cons(e, rest) => Cons(elem, prepend(e, rest) )
    case Empty         => Cons(elem, Empty)
  }

  // Combine the first list with the second
  // Example:  append([1,2],[3,4]) == [1,2,3,4]
  def append[A](list1: MyList[A], list2: MyList[A]): MyList[A] = list1 match {
    case Empty            => list2
    case Cons(elem, rest) => prepend(elem, append(rest, list2))
  }

  // Given a list, reverse the list's elements.
  // Example: reverse( [1,2,3] ) == [3,2,1]
  def reverse[A](list: MyList[A]): MyList[A] = list match {
    case Cons(elem, rest) => append( reverse(rest), Cons(elem, Empty) )
    case Empty            => Empty
  }
}