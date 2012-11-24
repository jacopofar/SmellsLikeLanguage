package main.scala.jacopofar.smellslikelanguage

import scala.io.Source
import scala.io.Codec
import java.io.File
import scala.xml.XML
import java.net.URL
import java.nio.charset.Charset
import java.nio.charset.CodingErrorAction

object Assimilator {

	def assimilateURL(URL:String,m:Model):Model={
		assimilateString(Source.fromURL(URL)(new Codec(Codec.UTF8).onMalformedInput(CodingErrorAction.IGNORE)).getLines.mkString.replaceAll("""<[^<>]+>""", ""),m)
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
	
		//we need to add some samples also for ending strings
		for(k<-1 to m.n-1){
			preparing=preparing.substring(1)+'0'
			m.add(preparing)
		}
		m
	}
	
	def assimilateFeed(fURL:String,m:Model)={
	  val feedXML=XML.load(new URL(fURL).openConnection().getInputStream());
	  feedXML\\"link" foreach {(lURL) =>if (lURL.text.length()>4) assimilateURL(lURL.text,m)}
	  m
	}
}
