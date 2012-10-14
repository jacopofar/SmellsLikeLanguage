package model

import scala.io.Source
import java.io.File

object Assimilator {
	def assimilateURL(URL:String,m:Model)={
		assimilateString(Source.fromURL(URL).getLines.mkString.toLowerCase().replaceAll("""<[^<>]+>""", ""),m)	  
	}

	def assimilateFile(path:String, m:Model)={
		val source = Source.fromFile(path)
				val content:String = source.mkString.toLowerCase
				source.close ()
				assimilateString(content,m)

	}
	def assimilateFolder(path:String,m:Model):Unit={
		for(file:File<-new File(path).listFiles()){
			if (file.isDirectory()) assimilateFolder(file.getAbsolutePath(),m)
			if(file.isFile()) assimilateFile(file.getAbsolutePath(),m)

		}
	}

	def assimilateString(s:String,m:Model)={
		var preparing:String="N"*m.n
				for(c<-s){
					if(c.isDigit){
						preparing=preparing.substring(1)+'D'
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
		preparing=preparing.substring(1)+'N'
				m.add(preparing)
	}
	}
}