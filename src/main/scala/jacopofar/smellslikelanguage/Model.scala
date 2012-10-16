package main.scala.jacopofar.smellslikelanguage

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
					private var numSamples:Int=0
					/**
					 * Increase of 1 the counter of a string presence, creating it if nonexistent
					 * It also update squareSum, the square of the sum of counters used when calculating cosine similarity
					 * This approach make faster calculating the cosine and slower constructing the model
					 * */
				def add(s:String)={
				def square(x: Int) = { x * x }
				squareSum-=square(frequencies.getOrElse(s, 0))
						squareSum+=square(frequencies.getOrElse(s, 0)+1)
						frequencies+= s->(frequencies.getOrElse(s, 0)+1)
			}
			def +=(s:String)=add(s:String)
			private var squareSum:Double=0


				def similarity(o:Model):Double={
			
			  (if(frequencies.size<o.frequencies.size)
							frequencies.keys.map(s=>frequencies(s)*o.frequencies.getOrElse(s, 0)).reduce((a,b)=>a+b)
							else
								o.frequencies.keys.map(s=>o.frequencies(s)*frequencies.getOrElse(s, 0)).reduce((a,b)=>a+b)
								)/math.sqrt(o.squareSum*squareSum)
				}
}

class InvalidSampleSizeException(message:String) extends Throwable{}
