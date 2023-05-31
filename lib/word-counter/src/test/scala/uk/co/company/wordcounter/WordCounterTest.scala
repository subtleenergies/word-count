package uk.co.company.wordcounter

import com.sun.java.accessibility.util.Translator
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class WordCounterTest extends AnyFlatSpec with Matchers {

  def newCounter() = new WordCounter(Translator.naiveTranslator)

  "addWords" should "accept a single word" in {
    val counter = newCounter()
    counter.addWords("foo") shouldEqual {}
  }

  it should "accept multiple words" in {
    val counter = newCounter()
    counter.addWords("the", "quick", "brown", "fox") shouldEqual {}
  }

  it should "NOT allow addition of words with non-alphabetic characters" in {
    val counter = newCounter()
    val thrown = the [IllegalArgumentException] thrownBy counter.addWords("foo2")
    thrown.getMessage should equal ("Words must not contain non-alphabetical characters")
  }

  it should "treat same words written in different languages as the same word" in {
    // for example if
    // adding "flower", "flor" (Spanish word for flower) and "blume" (German word for flower)
    // the counting method should return 3.
    //
    // You may assume that translation of words will be done via external class provided to you called "Translator"
    // that will have method "translate" accepting word as an argument and it will return English name for it.
    val counter = newCounter()
    counter.addWords("flower", "flor", "Blume")
    counter.countOccurrences("flower") shouldEqual 3
    counter.countOccurrences("flor") shouldEqual 3
    counter.countOccurrences("Blume") shouldEqual 3

    counter.addWords("large", "grande", "gross")
    counter.countOccurrences("large") shouldEqual 3
    counter.countOccurrences("grande") shouldEqual 3
    counter.countOccurrences("gross") shouldEqual 3
  }

  "countOccurrences" should "return zero if no words were added" in {
    val counter = newCounter()
    counter.countOccurrences("foo") shouldEqual 0
  }

  it should "return number of times words were added" in {
    val counter = newCounter()
    counter.addWords("foo", "bar", "baz")
    counter.countOccurrences("foo") shouldEqual 1
    counter.countOccurrences("bar") shouldEqual 1
    counter.countOccurrences("baz") shouldEqual 1

    counter.addWords("foo", "bar", "baz")
    counter.countOccurrences("foo") shouldEqual 2
    counter.countOccurrences("bar") shouldEqual 2
    counter.countOccurrences("baz") shouldEqual 2
  }
}
