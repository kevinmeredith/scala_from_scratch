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

  test("Appending 'Empty' to a List must equal the input list") {
    forAll (intList) { (list: MyList[Int]) =>
      val appendedEmpty = MyList.append(list, Empty)
     appendedEmpty should be( list )
    }
  }

  test("Appending a list to the 'Empty' should return the appended list") {
    forAll (intList) { (list: MyList[Int]) =>
      val appendedEmpty = MyList.append(Empty, list)
      appendedEmpty should be( list )
    }
  }

  def intListWithInt: Gen[(MyList[Int], Int)] = for {
    list <- intList
    num  <- posNum[Int]
  } yield (list, num)

  test("Prepending an element to a list must equal appending the list to List(element)") {
    forAll (intListWithInt) { (x: (MyList[Int], Int) ) =>
      val (list, element) = x
      val prepended = MyList.prepend(element, list)
      val appended  = MyList.append( Cons(element, Empty), list )
      prepended should be (appended)
    }
  }

}