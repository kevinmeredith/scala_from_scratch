package net

import org.scalacheck.Gen
import Gen.{oneOf, const, sized, resize, frequency, posNum}

object MySpec {

	val genListInt: Gen[MyList[Int]] = Gen.resize(1000, genList( posNum[Int] ) )

	private def genList[A](gen: Gen[A]): Gen[MyList[A]] = Gen.sized { n => 
		println(n)
		if(n <= 0) {
			genEmpty
		}
		else {
			frequency( (1, genEmpty), (20, genCons(gen)) )
		}
	}

	private val genEmpty: Gen[MyList[Nothing]] = Gen.const(Empty)

	private def genCons[A](gen: Gen[A]): Gen[MyList[A]] = 
		for {
			list <- sized { n => resize(n/2, genList(gen) ) }
			a    <- gen
		} yield Cons(a, list)
}


// http://stackoverflow.com/questions/42810351/testing-recursive-data-structure/42816037#42816037