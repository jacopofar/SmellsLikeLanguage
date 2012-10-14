Smells Like Language
====================

A Scala language identifier based on comparing samples. Reads text in different human languages (or other languages) and tries to detect the language of a new text.

The comparison of two models is based on cosine similarity of the two vectors containing all the samples of analyzed text of a specified size.
It can assimilate text in three ways:

*from text files

*From folders of text files, recursively

*From URLs, downloading webpages and extracting text



TO DO
=====
Write the code to:

*use the model to calculate the probability of a string

*generate gibberish statistically similar to the analyzed text

*save and load the model to a file


