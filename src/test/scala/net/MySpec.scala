package net

import org.scalacheck.Gen
import Gen.{oneOf, const, lzy, frequency}

object MySpec {

	def genList[A](gen: Gen[A]): Gen[MyList[A]] =
		lzy { genListHelper[A](gen) } 

	private val genEmpty: Gen[MyList[Nothing]] = Gen.const(Empty)

	// http://stackoverflow.com/questions/42810351/testing-recursive-data-structure/42816037#42816037
	private def genListHelper[A](gen: Gen[A]): Gen[MyList[A]] = 
		lzy { 
			frequency[MyList[A]](
				(10, genCons(gen)),
				(1, genEmpty     )
			)
		}

	private def genCons[A](gen: Gen[A]): Gen[MyList[A]] = for {
		list <- genList(gen)
		a    <- gen
	} yield Cons(a, list)

	
}
