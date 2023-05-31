import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class WordCounterTest extends AnyFlatSpec with Matchers {

  "addWords" should "accept a single word" in {
    val counter = WordCounter()
    counter.addWords("foo") shouldEqual {}
  }

  it should "accept multiple words" in {
    val counter = WordCounter()
    counter.addWords("the", "quick", "brown", "fox") shouldEqual {}
  }

  "countOccurrences" should "return zero if no words were added" in {
    val counter = WordCounter()
    counter.countOccurrences("foo") shouldEqual 0
  }

  it should "return number of times words were added" in {
    val counter = WordCounter()
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
