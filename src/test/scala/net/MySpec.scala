package net

import org.scalacheck._
import Gen.{oneOf, const, lzy}

// abstract class MyList[+A]
// case class Cons[+A](elem: A, rest: MyList[A]) extends MyList[A]
// case object Empty                             extends MyList[Nothing]

object MySpec {

	def genList[A](gen: Gen[A]): Gen[MyList[A]] =
		lzy { oneOf(genCons(gen), Gen.const(Empty)) } 

	def genCons[A](gen: Gen[A]): Gen[MyList[A]] = for {
		list <- genList(gen)
		a    <- gen
	} yield Cons(a, list)

	
}
