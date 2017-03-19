package net

import org.scalatest._
import org.scalatest.prop.PropertyChecks
import org.scalacheck.Gen
import org.scalacheck.Gen._

class MyListPropertySpec extends FunSuite with PropertyChecks with Matchers {

  private def sizedNList(n: Int): MyList[Int] = {
    if(n <= 0) Empty
    else       Cons(n, sizedNList(n - 1) )
  }

  private val MaxMyListLength = 100

  def intList: Gen[MyList[Int]] = {
    val gen: Gen[Int] = posNum[Int].suchThat(int => int < MaxMyListLength)
    gen.map(int => sizedNList(int) )
  }

  test("Reversing a list twice must equal the input list") {
    forAll ( intList ) { (list: MyList[Int]) =>
      val reversedTwice = MyList.reverse( MyList.reverse(list) )
      reversedTwice should be ( list )
    }
  }

  def intListWithInt: Gen[(MyList[Int], Int)] = for {
    list <- intList
    num  <- posNum[Int]
  } yield (list, num)

}