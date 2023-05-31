import scala.collection.mutable

/**
 * This would be defined in an external translation library
 */
trait Translator {
  def translate(word: String): String
}

/**
 * Word counter class as described by the README requirements.
 *
 * A simple, final OO class is used here allow multiple instances and encapsulation of the internal implementation.
 * To make it simple an lightweight we avoid using any other libraries.
 *
 * Error handling uses runtime exceptions to make the library compatible with other JVM languages such as Java.
 * If we were using in a Scala-only context, I would probably prefer to use Either[Error, Unit] to make
 * more elegant error processing.
 *
 * @param translator
 */
final class WordCounter(translator: Translator) {

  /**
   * A mutable Map (default implementation HashMap) is an efficient way to store and retrieve words using a hash table.
   * However this is not thread-safe.
   * If (and only if) we needed thread-safety I would use java.util.concurrent.ConcurrentHashMap.
   */
  private val wordCounts = mutable.Map[String,Int]()

  /**
   * transforms a given word into a map key assuming that:
   * - different case will be treated as the same word
   * - different languages will be treated as the same word (this is a much thornier issue than the exercise suggests)
   * @param word
   * @return
   */
  private def getKey(word: String): String = {
    translator.translate(word).toLowerCase
  }

  /**
   * Method that allows you to add one or more words.
   *
   * Assumption is that zero words is not permitted, so this implementation requires at least one at compile time.
   * Alternatively we could:
   * - check for empty list and raise a runtime error
   * - do nothing / call has no effect
   * - use something like cats NonEmptyList
   *
   * The alphabetic character check is a naive implementation that only accepts english alphabetical characters.
   * Depending on requirements we could use unicode letters \p{L} instead.
   *
   * @param words
   */
  def addWords(word: String, moreWords: String*): Unit = {
    val words = word :: moreWords.toList
    words.foreach { word =>
      if (!word.matches("^[a-zA-Z]+$"))
        throw new IllegalArgumentException("Words must not contain non-alphabetical characters")
    }
    words.foreach { word =>
      val key = getKey(word)
      val count = wordCounts.getOrElse(key, 0)
      wordCounts.addOne(key, count + 1)
    }
  }

  /**
   * Method that returns the count of how many times a given word was added to the word counter.
   *
   * Assumes that non-english words are looked up in the same way as addWords
   * so that:  countOccurrences("flor") === countOccurrences("flower")
   *
   * @param word
   * @return
   */
  def countOccurrences(word: String): Int = {
    val key = getKey(word)
    wordCounts.getOrElse(key, 0)
  }
}
