package main.scala.jacopofar.smellslikelanguage

import scala.collection.mutable.HashMap
import scala.xml.Elem
import scala.xml.XML
import scala.io.Source

class Recognizer(private var l:List[Model]) {
	if (l.map(e=>e.n).exists(m=>m!=l.head.n))
		throw new InvalidSampleSizeException("the recognizer needs models with the same sample size")
	def apply(lang:String)={
			l.filter(p=>p.language.equals(lang)).head
	}
	def identifiedLanguage(m:Model):String=ranking(m).reduce((a,b)=>if(a._2>b._2) a else b)._1
			def identifiedLanguage(s:String):String=identifiedLanguage(Assimilator.assimilateString(s, new Model(l.head.n,"")))
			def ranking(e:Model):HashMap[String,Double]={
			if(e.size==0) throw new EmptyModelException("Can't confront an empty model, use the assimilator to integrate text samples in the model")
			var r=new HashMap[String,Double]()
			var scoresum=0.0
			var tmp=0.0
			for(model<-l){
				tmp=model.similarity(e)
						scoresum+=tmp
						r+= model.language-> tmp
			}
			for(k<-r.keys){
				r+= k-> (r(k)*100/scoresum)
			}
			return r
	}

	def +=(m:Model)=if (if (l.size==0) false else m.n!=l.head.n)
		throw new InvalidSampleSizeException("the recognizer needs models with the same sample size")
	else
		l::=m

		def toXML():Elem = <recognizer>{l.map(_.toXML)}</recognizer>

}

object Recognizer{
	def fromXLMfile(fn:String):Recognizer=fromXMLString(Source.fromFile(fn).mkString)
			def fromXMLString(xs:String)={
		val xml=XML.loadString(xs)
				var r=new Recognizer(Nil)
		for(val m <- xml\\ "model") {
			r+=Model.fromXMLString(m.toString)
		}
		r
	}
}
class EmptyModelException(message:String) extends Exception{}
