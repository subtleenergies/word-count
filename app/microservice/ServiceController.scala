package microservice

import play.api.Logging
import play.api.mvc._
import uk.co.company.wordcounter.{Translator, WordCounter}

import javax.inject.Inject

/**
 * This is a very basic microservice that could be hosted on something like an AWS EC2 server or a container.
 *
 * For production it's good practice to use autoscaling and load balancing so that the service will be resilient to
 * instance failures.  If we are using the cloud, it might be better implemented as a serverless function.
 *
 * A production service would probably require user authentication/authorisation and stronger input validation.
 *
 * The word counter is in global scope for the controller so different clients will see counts
 * for each other's words, which is unlikely to be the requirement.
 *
 * A more likely requirement would be for a particular client to be able to add and count their own words,
 * in which case they would need to be stored in a session-specific way.  This could not be done reliably
 * using the word counter as it is, because best practice is for the microservice to be stateless.
 *
 * So for a real service to work resiliently, there would likely need to be some persistent store, such
 * as Redis or ElasticSearch.
 *
 * @param cc
 */
class ServiceController @Inject() (cc: ControllerComponents) extends AbstractController(cc) with Logging {

  val wordCounter = new WordCounter(Translator.naiveTranslator)

  def index = Action {
    Ok("It works!")
  }

  def postWords() = Action { request =>
    val words = request.body.asText.getOrElse("").split(" ")
    if (words.isEmpty || words.head.isEmpty) {
      BadRequest("Need at least one word")
    } else try {
      wordCounter.addWords(words.head, words.tail: _*)
      Ok("Thanks for the words!")
    } catch {
      case e: IllegalArgumentException =>
        BadRequest(e.getMessage)
      case e =>
        logger.error(e.getMessage, e)
        InternalServerError("The server was unable to process your request")
    }
  }

  def getWordCount(word: String) = Action {
    if (word.isEmpty || word.length > 100 || !word.matches("^[a-zA-Z]+")) {
      BadRequest("Unexpected word")
    } else {
      val count = wordCounter.countOccurrences(word)
      Ok(s"There are $count occurrences of $word!")
    }
  }
}