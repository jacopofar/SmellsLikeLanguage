package main.scala.jacopofar.smellslikelanguage

import scala.io.Source
import java.io.File

object Assimilator {
	def assimilateURL(URL:String,m:Model):Model={
		assimilateString(Source.fromURL(URL).getLines.mkString.replaceAll("""<[^<>]+>""", ""),m)	  
	}

	def assimilateFile(path:String, m:Model):Model={
		val source = Source.fromFile(path)
				val content:String = source.mkString
				source.close ()
				assimilateString(content,m)
	}
	def assimilateFolder(path:String,m:Model):Model={
		for(file:File<-new File(path).listFiles()){
			if (file.isDirectory()) assimilateFolder(file.getAbsolutePath(),m)
			if(file.isFile()) assimilateFile(file.getAbsolutePath(),m)
		}
		m
	}

	def assimilateString(s:String,m:Model):Model={
		var preparing:String="0"*m.n
				for(c<-s){
					if(c.isDigit){
						preparing=preparing.substring(1)+'1'
								m.add(preparing)
					}
					else
						if(!m.ignorePunctuation || !(Model.punctuation.contains(c))){
							preparing=preparing.substring(1)+c
									m.add(preparing)		
						}
				}

	//we need to add some N-grams also for ending strings
	for(k<-1 to m.n){
		preparing=preparing.substring(1)+'0'
				m.add(preparing)
	}
	m
	}
}