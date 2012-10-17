package test.scala

import org.scalatest.FunSuite
import main.scala.jacopofar.smellslikelanguage.Model
import main.scala.jacopofar.smellslikelanguage.Assimilator
import main.scala.jacopofar.smellslikelanguage.Recognizer
import scala.io.Source



class AssimilatorTest extends FunSuite {

	test("assimilate URLs") {
		val aModel=new Model(5,"german")
		Assimilator.assimilateURL("http://de.wikipedia.org/wiki/Napoleon_Bonaparte", aModel)
	}

}

class CosineTest extends FunSuite{
	val gModel=new Model(3,"german")
	Assimilator.assimilateURL("http://de.wikipedia.org/wiki/Napoleon_Bonaparte", gModel)
	Assimilator.assimilateURL("http://de.wikipedia.org/wiki/Akkordeon", gModel)
	val iModel=new Model(3,"italian")
	Assimilator.assimilateURL("http://it.wikipedia.org/wiki/Napoleone_Bonaparte", iModel)
	Assimilator.assimilateURL("http://it.wikipedia.org/wiki/Fisarmonica", iModel)

	test("compare a page of wikipedia") {

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


				println("Italian proverb in Italian:"+si1)
				println("Italian proverb in German:"+sg1)

				assert((si1>sg1))
				println("----------")
				val si2=iModel.similarity(smg);
		val sg2=gModel.similarity(smg)


				println("German proverb in Italian:"+si2)
				println("German proverb in German:"+sg2)
				assert((si2<sg2))

	}
	val jaModel=Assimilator.assimilateURL("https://ja.wikipedia.org/wiki/ナポレオン・ボナパルト", new Model(3,"japanese"))
			val chModel=Assimilator.assimilateURL("https://zh.wikipedia.org/wiki/拿破仑一世", new Model(3,"chinese"))
			//found some problems in using arab, probably due to LTR, needed to use the ugly encoding
			val arModel=Assimilator.assimilateURL("https://ar.wikipedia.org/wiki/%D9%86%D8%A7%D8%A8%D9%84%D9%8A%D9%88%D9%86_%D8%A7%D9%84%D8%A3%D9%88%D9%84", new Model(3,"arabic"))
			println(List(gModel,iModel,jaModel,chModel,arModel))
			test("recognize with the recognizer") {


		val r=new Recognizer(List(gModel,iModel,jaModel,chModel,arModel))
		assert("japanese"==r.identifiedLanguage(Assimilator.assimilateString("来るものは拒まず去るものは追わず", new Model(3,"japanese proverb"))))
		assert("italian"==r.identifiedLanguage(Assimilator.assimilateString("Tanto va la gatta al lardo che ci lascia lo zampino", new Model(3,"italian proverb"))))
		assert("chinese"==r.identifiedLanguage(Assimilator.assimilateString("十類煩惱：一兇、二吉、三毒、四倒、五蘊、六入、七識、八邪、九結、十惡", new Model(3,"chinese proverb"))))
	}


}

