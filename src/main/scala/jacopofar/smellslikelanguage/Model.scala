package main.scala.jacopofar.smellslikelanguage

import scala.collection.mutable.HashMap
import scala.xml.XML
import scala.io.Source
import scala.collection.mutable.HashSet
import scala.util.Random
import scala.collection.mutable.MutableList

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
	val punctuation=".,;:-_!?$%&()“[]{}<>=@#*+\\/\"~`\n\r\t";
	def fromXLMfile(fn:String):Model=fromXMLString(Source.fromFile(fn).mkString)
	def fromXMLString(xs:String)={
		val xml=XML.loadString(xs)
		var m=new Model((xml\\"@sampleSize").text.toInt,(xml\\"@language").text)
		m.squareSum=BigInt((xml\\"@squareSum").text)
		for(val sample <- xml\\ "sample") {
			m.frequencies+= sample.text->(sample\\"@count").text.toInt
		}
		m
	}
	val r=new Random()
}
class Model(val n:Int,val language:String) {
	if (n<1) throw new InvalidSampleSizeException("The sample size has to be greater than 0");
	var ignorePunctuation:Boolean=true
	private var frequencies=new HashMap[String, Int]
	//squareSum and quickSquare contain the same value in different formats
	//squareSum is for XML record and retrieve, quickSquare for fast calculation
	private var quickSquare:Double=0
	private var squareSum:BigInt=0
	/**
	 * Increase of 1 the counter of a string presence, creating it if nonexistent
	 * It also update squareSum, the square of the sum of counters used when calculating cosine similarity
	 * */
	def add(s:String)={
		def square(x: Int) = x*x 
		squareSum-=square(frequencies.getOrElse(s, 0))
		squareSum+=square(frequencies.getOrElse(s, 0)+1)
		frequencies+= s->(frequencies.getOrElse(s, 0)+1)
		quickSquare=squareSum.toDouble
	}
	def +=(s:String)=add(s:String)

	def size:Int=frequencies.size


	def similarity(o:Model):Double=
		(if(frequencies.size<o.frequencies.size)
			frequencies.keys.map(s=>frequencies(s)*o.frequencies.getOrElse(s, 0)).reduce((a,b)=>a+b)
		else
			o.frequencies.keys.map(s=>o.frequencies(s)*frequencies.getOrElse(s, 0)).reduce((a,b)=>a+b)
				)/math.sqrt(o.quickSquare*quickSquare)
	def similarity(s:String):Double=similarity(Assimilator.assimilateString(s,new Model(n,"")))

	override def toString()="Model of "+language+" [sample size:"+n+" #samples:"+frequencies.size+"] sum of squares"+quickSquare+" ~ "+squareSum

	def toXML()=
		<model language={this.language} sampleSize={this.n.toString} squareSum={squareSum.toString}>
		{frequencies.keys.map((h:String) => <sample count={frequencies(h).toString}>{h}</sample> )}
		</model>

	def generateGibberish(length:Int):String={
		//generate some random text gibberish statistically similar to the modeled language
		//to do so, it uses a genetic algorithm which generate a pool of random strings and at each generation
		//keep the ones with the highest similarity (with the language model), mutate and cross them preparing the next generation
		val uc=new HashSet[Char]

		frequencies.keySet.foreach(e=>uc.add(e.charAt(0)))

		val v=uc.toArray

		def randomString():String=((1 to length) map (w =>v(Model.r.nextInt(v.length)))).mkString("")
		def crossString(a:String,b:String)={
			var p=Model.r.nextInt(a.length()-1)+1
			a.substring(0, p)+b.substring(p, b.length())
		}
		def mutateString(s:String)={
			var p=Model.r.nextInt(s.length()-1)+1
			s.substring(0,p-1)+v(Model.r.nextInt(v.length))+s.substring(p,s.length())
		}

		val pool=(1 to 100).map(w=>randomString).toArray
		for(k<- 1 to (length*2+10)){
			//sort the pool with similarity
			pool.sortWith((a,b) => similarity(a)<similarity(b))

			//the first 15 are kept
			//then 15 are mutations of the first ones
			for(i<- 1 to 15){
				pool(i+15)=mutateString(pool(i))
			}
			//then crossing over
			for(i<- 30 to 80){
				pool(i)=crossString(pool(Model.r.nextInt(10)),pool(Model.r.nextInt(10)))
			}
			//then 20 brand new strings
			for(i<- 80 to 99){
				pool(i)=randomString()
			}
		}
		//now after various geneations, let's take the best item
		pool.maxBy(similarity(_))
	}

}

class InvalidSampleSizeException(message:String) extends Throwable{}
