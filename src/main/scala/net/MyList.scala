package net

sealed abstract class MyList[+A]
case class Cons[+A](elem: A, rest: MyList[A]) extends MyList[A]
case object Empty                             extends MyList[Nothing]

object MyList {

  def reverse[A](list: MyList[A]): MyList[A] = list match {
    case Cons(elem, rest) => append( reverse(rest), Cons(elem, Empty) )
    case Empty            => Empty
  }

  private def append[A](xs: MyList[A], ys: MyList[A]): MyList[A] = xs match {
    case Empty       => ys
    case Cons(a, as) => Cons(a, append(as, ys))
  }

}