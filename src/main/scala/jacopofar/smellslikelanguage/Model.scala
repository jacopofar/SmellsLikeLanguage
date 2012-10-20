package main.scala.jacopofar.smellslikelanguage

import scala.collection.mutable.HashMap
import scala.xml.XML

/**
 * Contains a model for a language.
 * A model is made by the count of all the substrings of assimilated text of a specified size N>0
 * A model is indifferent to different digits in texts, just to their presence (it transforms each digit in 0)
 * By default punctuation is ignored, but is possible to use it, for example to identify programming languages
 * Two models are compared using cosine similarity: the similarity between samples frequency vector is a number between 0 and 1:
 * the more two texts tend to have the same substring frequencies, the more they are likely to be in the same language.
 * By increasing the substring size is possible to increase the accuracy but is better to use a bigger dataset, especially when ideograms are involved
 * With n=3 one or two long articles from Wikipedia should fit
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
					private var squareSum:Double=0
					/**
					 * Increase of 1 the counter of a string presence, creating it if nonexistent
					 * It also update squareSum, the square of the sum of counters used when calculating cosine similarity
					 * */
					def add(s:String)={
				def square(x: Int) = { x * x }
				squareSum-=square(frequencies.getOrElse(s, 0))
						squareSum+=square(frequencies.getOrElse(s, 0)+1)
						frequencies+= s->(frequencies.getOrElse(s, 0)+1)
			}
			def +=(s:String)=add(s:String)
					def size:Int=frequencies.size


					def similarity(o:Model):Double={
				(if(frequencies.size<o.frequencies.size)
					frequencies.keys.map(s=>frequencies(s)*o.frequencies.getOrElse(s, 0)).reduce((a,b)=>a+b)
					else
						o.frequencies.keys.map(s=>o.frequencies(s)*frequencies.getOrElse(s, 0)).reduce((a,b)=>a+b)
						)/math.sqrt(o.squareSum*squareSum)
			}

			override def toString()="Model of "+language+" [sample size: "+n+" #samples:"+frequencies.size+"]"

					def toXML()=
				<model language={this.language} sampleSize={this.n.toString} squareSum={squareSum.toString}>
				{frequencies.keys.map((h:String) => <sample size={this.frequencies(h).toString}>{h}</sample> )}
				</model>
			
}

class InvalidSampleSizeException(message:String) extends Throwable{}
