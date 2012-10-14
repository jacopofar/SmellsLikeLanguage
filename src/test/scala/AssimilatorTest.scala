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

