import scala.collection.mutable

class WordCounter {

  private val wordCounts = mutable.Map[String,Int]()

  /**
   * method that allows you to add one or more words
   * @param words
   */
  def addWords(word: String, moreWords: String*): Unit = {
    val words = word :: moreWords.toList
    words.foreach { word =>
      wordCounts.addOne(word, 1 + countOccurrences(word))
    }
  }

  /**
   * method that returns the count of how many times a given word was added to the word counter
   * @param word
   * @return
   */
  def countOccurrences(word: String): Int = {
    wordCounts.getOrElse(word, 0)
  }
}

object WordCounter {

  def apply() = new WordCounter

}