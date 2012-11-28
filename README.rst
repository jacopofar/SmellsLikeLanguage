Smells Like Language
====================

A Scala language identifier. Reads text in different human languages (or other languages), generate a model and uses it to detect the language of a new text.

The comparison of two models is based on cosine similarity of the two vectors containing all the samples of analyzed text of a specified size.

It can assimilate text in three ways:

* from text files
* From folders of text files, recursively
* From URLs, by downloading webpages, stripping HTML tags and extracting text
* From RSS feeds, following all links inside <link> tags

Installation
============
The only dependency is Scalatest for tests, and of course the Scala library, it was created with Scala 2.9.2 and may not work with older versions.

Download the package and run the command *sbt test* to test the application. It will download some pages from Wikipedia to build language models, so **you'll need an internet connection to run tests**.

Training
========
When creating the model, you have to choose a sample size. A sample of 1 or 2 is sufficient for most of the cases, but by increasing it you may obtain more accuracy.

Thought, be careful to increase the training dataset (e.g.: give it more webpages or text files), especially when training for Chinese or Japanese since ideograms make necessary to manage a bigger amount of possible character combinations.

**Programming languages:**
The program can be used to recognize programming or markup languages, but consider that by default it ignores punctuation signs defined in *Model.punctuation*.

So, when comparing non-human languages, change *Model.ignorePunctuation* value before training. Consider that this value is used by the assimilator, so you can change it every time you assimilate something to decide  wheter or not to assimilate punctuation signs **for that document**. If not, punctuation character will be ignored.

**digits:**
The program ignores digit values, converting any digit (0-9) to the digit 1.

**non latin characters:**
The program has been designed to manage every language, and has been tested with Chinese, Japanese and Arabic.

Usage
=====
**For lazy people:**
You can load the provided model huge_regognizer.xml, created with GenerateModel.scala, which contains 117 languages from the wikipedia editions with more than 10000 articles.

	val r=Recognizer.fromXLMfile("huge_recognizer.xml")
	
	r.identifiedLanguage("Ofte premilo estas parto de ia aparato, sed ankaŭ estadas premiloj kiel apartaj iloj")
	
will return "Esperanto"

To recognize a language you have to:

1. Create an instance of *Model*, specifying sample size and name of the language
	val m=new Model(3,"Italian")
	val g=new Model(3,"German")
2. Use the *Assimilator* static methods to assimilate content and train the model.
	Assimilator.assimilateURL("it.wikipedia.org/wiki/Napoleone_Bonaparte",m)
	Assimilator.assimilateURL("de.wikipedia.org/wiki/Napoleon_Bonaparte",g)
	Assimilator.assimilateString("ciao, come va?",m)
	
	you can use different assimilators for a model
3. Now you can confront two models with the **similarity** method of *Model*:
		m.similarity(g) //return a number like 0.2
		m.similarity(m) //return 1
4. To make things easier you can use a Recognizer to confront many models
		val r=new Recognizer(List(gModel,iModel,jaModel,chModel))

		r+=arModel

		r.identifiedLanguage(Assimilator.assimilateString("来るものは拒まず去るものは追わず", new Model(2,"japanese proverb"))))
5. Or you can use identifiedLanguage directly on a string:
		r.identifiedLanguage("sono una persona pigra")
		//will return "Italian"

Save a model or a recognizer
===========================

The Model and the Recognizer class have the methods *toXML*, *fromXLMfile* and *fromXLMString* which allows to save and load a model or a recognizer using XML format.

Generate text gibberish
=======================

The program can generate a random text from a model using a genetic algorithm to find a string of a given length which fits better the language model.

The approach is *far from perfection*, it's slow and give poor results, but it's still better than nothing.

How does it work?
=================

The application extract all substrings of contiguous characters of length N from a text and generate a frequency vector.
For example:

"nonno nanni" with N=2 becomes

"0n"=1
"no"=2
"on"=1
"o "=1
" n"=1
"na"=1
"an"=1
"nn"=1
"ni"=1
"i0"=1
where '0' is used as a placeholder for text boundaries.

The frequency vectors of two texts can be compared through cosine similarity, that is the dot product of the two frequency vectors divided by the product of magnitudes.

This value is a number between 0 (no common values) and 1 (identical frequencies ratio), the program takes the highest similarity to decide which is the language of a given text.

Usually, this kind of analysis is done using a Markov model, which is based on *transistions* between characters more than their frequencies. There's not a big difference, it's possible to transform one model in the other, the advantage of cosine similarity is the ease of confronting models.

Futher improvements and applications
======================================
The program could be made lighter by reducing the vector size, for example by using an hashing functions to map different elements to the same frequency.
Instead of a generic string hash it could be possible to analyze a dataset (like huge_recognizer.xml) to create an ad-hoc function to reduce information loss in this case, for example by using SVD to reduce the number of dimensions.

It could be interesting to use a self organizing map or other ML techniques to get a graphical visualization of similar languages.