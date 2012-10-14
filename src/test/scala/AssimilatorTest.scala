package test.scala

import org.scalatest.FunSuite
import model.Model
import model.Assimilator


class AssimilatorTest extends FunSuite {

	test("assimilate URLs") {
		val aModel=new Model(5,"German")
		Assimilator.assimilateURL("http://de.wikipedia.org/wiki/Napoleon_Bonaparte", aModel)
	}

}

class CosineTest extends FunSuite{
	val gModel=new Model(3,"German")
	Assimilator.assimilateURL("http://de.wikipedia.org/wiki/Napoleon_Bonaparte", gModel)
	Assimilator.assimilateURL("http://de.wikipedia.org/wiki/Akkordeon", gModel)
	val iModel=new Model(3,"Italian")
	Assimilator.assimilateURL("http://it.wikipedia.org/wiki/Napoleone_Bonaparte", iModel)
	Assimilator.assimilateURL("http://it.wikipedia.org/wiki/Fisarmonica", iModel)

	test("compare a page of wikipedia") {


		val c=new Model(3,"checkme")
		Assimilator.assimilateURL("http://it.wikipedia.org/wiki/Idrogeno", c)

		val si=iModel.similarity(c);
		val sg=gModel.similarity(c)

				println("similarity")

				println("similarity with Italian:"+si)
				println("similarity with German:"+sg)

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

					  val si2=iModel.similarity(smg);
		val sg2=gModel.similarity(smg)
				
		
		println("German proverb in Italian:"+si2)
				println("German proverb in German:"+sg2)

				assert((si2<sg2))
		
	}

}

