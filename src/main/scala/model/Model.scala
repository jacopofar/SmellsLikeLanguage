package model

import scala.collection.mutable.HashMap

/**
 * Contains a model for a language
 * */

object Model{
	//the character ' is not present since it is useful when detecting languages
	val punctuation=".,;:-_!?$%&()[]{}<>=@#*+\\/\"~`\n\r\t";
}
class Model(val n:Int,val language:String) {
  if (n<1) throw new InvalidSampleSizeException("The sample size has to be greater than 0");
	var ignorePunctuation:Boolean=true
			private var frequencies=new HashMap[String, Int]
					/**
					 * Increase of 1 the counter of a string presence, creating it if nonexistent
					 * */
					def add(s:String)=frequencies.put(s, frequencies.getOrElse(s, 0)+1)
}

class InvalidSampleSizeException(message:String) extends Throwable{
}