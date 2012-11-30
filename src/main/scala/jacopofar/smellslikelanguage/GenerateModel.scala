package main.scala.jacopofar.smellslikelanguage

import java.io.BufferedWriter
import java.io.FileWriter
import scala.xml.PrettyPrinter

object GenerateModel {
	def main(argc:Array[String])={
		//a map with a list of languages and their language code used for Wikipedia
		//see https://meta.wikimedia.org/wiki/List_of_Wikipedias
		//it contains all the languages having a wiki with more than 10000 articles (it includes simple english)
		val langs=List(
				"English"->"en",
				"German"->"de",
				"French"->"fr",
				"Dutch"->"nl",
				"Italian"->"it",
				"Spanish"->"es",
				"Polish"->"pl",
				"Russian"->"ru",
				"Japanese"->"ja",
				"Portuguese"->"pt",
				"Chinese"->"zh",
				"Swedish"->"sv",
				"Vietnamese"->"vi",
				"Ukrainian"->"uk",
				"Catalan"->"ca",
				"Norwegian (Bokmål)"->"no",
				"Finnish"->"fi",
				"Czech"->"cs",
				"Persian"->"fa",
				"Hungarian"->"hu",
				"Korean"->"ko",
				"Romanian"->"ro",
				"Indonesian"->"id",
				"Arabic"->"ar",
				"Turkish"->"tr",
				"Kazakh"->"kk",
				"Slovak"->"sk",
				"Esperanto"->"eo",
				"Danish"->"da",
				"Serbian"->"sr",
				"Lithuanian"->"lt",
				"Basque"->"eu",
				"Malay"->"ms",
				"Hebrew"->"he",
				"Bulgarian"->"bg",
				"Slovenian"->"sl",
				"Volapük"->"vo",
				"Croatian"->"hr",
				"Waray-Waray"->"war",
				"Hindi"->"hi",
				"Estonian"->"et",
				"Galician"->"gl",
				"Norwegian (Nynorsk)"->"nn",
				"Azerbaijani"->"az",
				"Simple English"->"simple",
				"Latin"->"la",
				"Greek"->"el",
				"Thai"->"th",
				"Serbo-Croatian"->"sh",
				"Occitan"->"oc",
				"Newar / Nepal Bhasa"->"new",
				"Macedonian"->"mk",
				"Georgian"->"ka",
				"Aromanian"->"roa-rup",
				"Tagalog"->"tl",
				"Piedmontese"->"pms",
				"Uzbek"->"uz",
				"Belarusian"->"be",
				"Haitian"->"ht",
				"Telugu"->"te",
				"Tamil"->"ta",
				"Belarusian (Taraškievica)"->"be-x-old",
				"Latvian"->"lv",
				"Breton"->"br",
				"Albanian"->"sq",
				"Cebuano"->"ceb",
				"Javanese"->"jv",
				"Malagasy"->"mg",
				"Welsh"->"cy",
				"Marathi"->"mr",
				"Luxembourgish"->"lb",
				"Icelandic"->"is",
				"Armenian"->"hy",
				"Bosnian"->"bs",
				"Burmese"->"my",
				"Yoruba"->"yo",
				"Aragonese"->"an",
				"Lombard"->"lmo",
				"Malayalam"->"ml",
				"West Frisian"->"fy",
				"Western Panjabi"->"pnb",
				"Afrikaans"->"af",
				"Bishnupriya Manipuri"->"bpy",
				"Bengali"->"bn",
				"Swahili"->"sw",
				"Ido"->"io",
				"Sicilian"->"scn",
				"Nepali"->"ne",
				"Gujarati"->"gu",
				"Cantonese"->"zh-yue",
				"Urdu"->"ur",
				"Bashkir"->"ba",
				"Low Saxon"->"nds",
				"Kurdish"->"ku",
				"Kirghiz"->"ky",
				"Asturian"->"ast",
				"Quechua"->"qu",
				"Sundanese"->"su",
				"Tatar"->"tt",
				"Irish"->"ga",
				"Chuvash"->"cv",
				"Interlingua"->"ia",
				"Neapolitan"->"nap",
				"Samogitian"->"bat-smg",
				"Zazaki"->"diq",
				"Banyumasan"->"map-bms",
				"Alemannic"->"als",
				"Walloon"->"wa",
				"Kannada"->"kn",
				"Amharic"->"am",
				"Scots"->"sco",
				"Sorani"->"ckb",
				"Scottish Gaelic"->"gd",
				"Fiji Hindi"->"hif",
				"Buginese"->"bug",
				"Tajik"->"tg",
				"Mazandarani"->"mzn",
				"Min Nan"->"zh-min-nan"
				)
			//create an empty Recognizer
			val r=new Recognizer(List())
			for(k<-langs){
				println("assimilating language "+k._1)
				val m=new Model(3,k._1)
				for(x<- 1 to 40){
					println("reading page "+x+" of 40...")
					Assimilator.assimilateURL("http://"+k._2+".wikipedia.org/wiki/Special:Random?printable=yes", m)
				}
				r+=m
			}
			val out= new BufferedWriter(new FileWriter("huge_recognizer.xml"))
			out.write(new PrettyPrinter(80,2).format(r.toXML));
			out.close()

	}
}
