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
  
  test("compare two languages") {
		val gModel=new Model(3,"German")
		Assimilator.assimilateURL("http://de.wikipedia.org/wiki/Napoleon_Bonaparte", gModel)
		
		val iModel=new Model(3,"Italian")
		Assimilator.assimilateURL("http://it.wikipedia.org/wiki/Napoleone_Bonaparte", iModel)
		
		val c=new Model(3,"checkme")
		Assimilator.assimilateURL("http://it.wikipedia.org/wiki/Idrogeno", c)
		val si=c.similarity(iModel);
		val sg=c.similarity(gModel)

		println("similarity with Italian:"+si)
		println("similarity with German:"+sg)

		assert((si>sg))
		
	}
}
