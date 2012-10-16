package main.scala.jacopofar.smellslikelanguage

import scala.collection.mutable.HashMap

class Recognizer(var l:List[Model]) {
  if (l.map(e=>e.n).exists(m=>m!=l.head.n))
    throw new InvalidSampleSizeException("the recognizer needs models with the same sample size")
  
  def identifiedLanguage(m:Model)=ranking(m) reduce ((a,b)=>if(a._2>b._2) a else b)._1
  def ranking(e:Model):HashMap[String,Double]={
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
  
  def +=(m:Model)=if (m.n!=l.head.n)
    throw new InvalidSampleSizeException("the recognizer needs models with the same sample size")
  else
    l::=m
    
}

