package uk.co.company.wordcounter

trait Translator {
  def translate(word: String): String
}

object Translator {

  val naiveTranslator = new Translator {
    override def translate(word: String): String = {
      word.toLowerCase match {
        case "flor" => "flower"
        case "blume" => "flower"
        case "grande" => "large"
        case "gross" => "large"
        case _ => word
      }
    }
  }
}