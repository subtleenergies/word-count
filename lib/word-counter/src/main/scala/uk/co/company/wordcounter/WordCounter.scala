package uk.co.company.wordcounter

import scala.collection.mutable

/**
 * Word counter class as described by the README requirements.
 *
 * A simple, final OO class is used here allow multiple instances and encapsulation of the internal implementation.
 * To make it simple an lightweight we avoid using any other libraries.
 *
 * Error handling uses runtime exceptions to make the library compatible with other JVM languages such as Java.
 * If we were using in a Scala-only context, I would probably prefer to use Either[Error, Unit] to make for
 * more elegant error processing.
 *
 * @param translator
 */
final class WordCounter(translator: Translator) {

  /**
   * A HashMap is an efficient way to store and retrieve words using a hash table.
   * To work in any execution context, java.util.concurrent.ConcurrentHashMap provides a thread-safe implementation.
   * If we know we don't need concurrent operation, we could use scala.mutable.HashMap for better performance.
   */
  private val wordCounts = new java.util.concurrent.ConcurrentHashMap[String,Int]()

  /**
   * transforms a given word into a map key assuming that:
   * - different case will be treated as the same word
   * - different languages will be treated as the same word (this is a much thornier issue than the exercise suggests)
   * @param word
   * @return
   */
  private def getKeyFor(word: String): String = {
    translator.translate(word).toLowerCase
  }

  /**
   * Method that allows you to add one or more words.
   *
   * Assumption is that zero words is not permitted, so this implementation rejects calls with no words.
   * Alternatively we could:
   * - do nothing / call has no effect
   * - use something like cats NonEmptyList
   *
   * The alphabetic character check is a naive implementation that only accepts english alphabetical characters.
   * Depending on requirements we could use unicode letters \p{L} instead.
   *
   * @param words
   */
  def addWords(words: String*): Unit = {
    if (words.isEmpty)
      throw new IllegalArgumentException("Provide at least one word")

    words.foreach { word =>
      if (!word.matches("^[a-zA-Z]+$"))
        throw new IllegalArgumentException("Words must not contain non-alphabetical characters")
    }

    words.foreach { word =>
      val key = getKeyFor(word)
      val count = wordCounts.getOrDefault(key, 0)
      wordCounts.put(key, count + 1)
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
    val key = getKeyFor(word)
    wordCounts.getOrDefault(key, 0)
  }
}
