package test.scala

import org.scalatest.FunSuite
import main.scala.jacopofar.smellslikelanguage.Model
import main.scala.jacopofar.smellslikelanguage.Assimilator
import main.scala.jacopofar.smellslikelanguage.Recognizer
import scala.io.Source
import java.io.FileWriter
import java.io.BufferedWriter
import scala.xml.PrettyPrinter



class AssimilatorTest extends FunSuite {

	test("assimilate URLs") {
		Assimilator.assimilateURL("http://de.wikipedia.org/wiki/Napoleon_Bonaparte", new Model(5,"german"))
	}
	test("assimilate RSS feed"){
		Assimilator.assimilateFeed("http://tinaleins.blogger.de/rss", new Model(3,"german"))
	}
}

class CosineTest extends FunSuite{
	val gModel=new Model(3,"german")
	Assimilator.assimilateURL("http://de.wikipedia.org/wiki/Napoleon_Bonaparte", gModel)
	Assimilator.assimilateURL("http://de.wikipedia.org/wiki/Akkordeon", gModel)
	val iModel=new Model(3,"italian")
	Assimilator.assimilateURL("http://it.wikipedia.org/wiki/Napoleone_Bonaparte", iModel)
	Assimilator.assimilateURL("http://it.wikipedia.org/wiki/Fisarmonica", iModel)
	test("generate gibberish.t"){
	  val out= new BufferedWriter(new FileWriter("gibberish.txt"))
		out.write(iModel.generateGibberish(50))
		out.write(iModel.generateGibberish(50))
		out.write(gModel.generateGibberish(50))
		out.write(gModel.generateGibberish(50))
		out.close();

	  
	}
	test("compare a page of Wikipedia") {

		val c=new Model(3,"checkme")
		Assimilator.assimilateURL("http://it.wikipedia.org/wiki/Idrogeno", c)

		val si=iModel.similarity(c);
		val sg=gModel.similarity(c)
		assert((si>sg))
	}

	test("compare simple statements") {
		val smi=new Model(3,"italian proverb")
		Assimilator.assimilateString("Tanto va la gatta al lardo che ci lascia lo zampino", smi)

		val smg=new Model(3,"German proverb")
		Assimilator.assimilateString("Aus Schaden wird man klug. (Darum ist einer nicht genug.)", smg)

		val si1=iModel.similarity(smi);
		val sg1=gModel.similarity(smi)
		
		assert((si1>sg1))
		val si2=iModel.similarity(smg);
		val sg2=gModel.similarity(smg)
		assert((si2<sg2))
	}


	val jaModel=Assimilator.assimilateURL("https://ja.wikipedia.org/wiki/ナポレオン・ボナパルト", new Model(3,"japanese"))
			val chModel=Assimilator.assimilateURL("https://zh.wikipedia.org/wiki/拿破仑一世", new Model(3,"chinese"))
			Assimilator.assimilateURL("https://zh.wikipedia.org/wiki/蒂埃里·布雷顿", chModel)
			Assimilator.assimilateURL("https://zh.wikipedia.org/wiki/反恐精英Online武装列表", chModel)
			//since Chinese use a big number of different ideograms, may be useful add more documents
			//Assimilator.assimilateURL("https://zh.wikipedia.org/wiki/FAIRY TAIL角色列表", chModel)
			//Assimilator.assimilateURL("https://zh.wikipedia.org/wiki/六四事件", chModel)

			//found some problems in using arab URLs, probably due to LTR, needed to use the ugly encoding
			val arModel=Assimilator.assimilateURL("https://ar.wikipedia.org/wiki/%D9%86%D8%A7%D8%A8%D9%84%D9%8A%D9%88%D9%86_%D8%A7%D9%84%D8%A3%D9%88%D9%84", new Model(3,"arabic"))
			test("recognize with the recognizer") {
			val r=new Recognizer(List(gModel,iModel,jaModel,chModel))
			r+=arModel

			
			val out= new BufferedWriter(new FileWriter("huge_recognizer.xml"))
			out.write(new PrettyPrinter(80,2).format(r.toXML));
			out.close()
			
			assert("japanese"==r.identifiedLanguage("来るものは拒まず去るものは追わず"))
			assert("italian"==r.identifiedLanguage("Tanto va la gatta al lardo che ci lascia lo zampino"))
			assert("chinese"==r.identifiedLanguage("今川義元掌權後，不斷於與北條家、織田家爭權，而且勢力不斷擴大，並且於1554年訂立了今川家、武田家與北條家的同盟，史稱善德­寺三國同盟"))

	}
}

class LoadSaveTest extends FunSuite{
	test("save a model") {
		val m1=new Model(2,"turkish")
		Assimilator.assimilateFeed("http://feeds.feedburner.com/portakalagaci/Kyco?format=xml", m1)
		val out= new BufferedWriter(new FileWriter("turkish_test_model.xml"))
		out.write(new PrettyPrinter(80,2).format(m1.toXML));
		out.close();
	}
	test("load a model") {
		val m2=Model.fromXLMfile("turkish_test_model.xml")
		assert(m2.language.equals("turkish"))
		assert(m2.similarity(Assimilator.assimilateString("tahin helvası", new Model(2,"turkish food name")))>0.1)
	}
	
	
	test("save a recognizer"){
		val smi=new Model(2,"italian proverb")
		Assimilator.assimilateString("Tanto va la gatta al lardo che ci lascia lo zampino", smi)
		val smg=new Model(2,"German proverb")
		Assimilator.assimilateString("Aus Schaden wird man klug. (Darum ist einer nicht genug.)", smg)
		val smj=new Model(2,"japanese proverb") 
		Assimilator.assimilateString("来るものは拒まず去るものは追わず",smj)
		val out= new BufferedWriter(new FileWriter("stupid_recognizer.xml"))
		out.write(new PrettyPrinter(80,2).format(new Recognizer(List(smg,smi,smj)).toXML));
		out.close()
	}
	test("load a recognizer") {
		val r=Recognizer.fromXLMfile("stupid_recognizer.xml")
		assert(r.identifiedLanguage("la gatta fa miao")=="italian proverb")
	}
}

