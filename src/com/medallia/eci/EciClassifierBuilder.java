package com.medallia.eci;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;

import challenge.lib.Classifier;
import challenge.lib.ClassifierBuilder;
import challenge.lib.Sentiment;
import challenge.lib.TaggedReview;
import challenge.run.Main;

/**
 * This class will be loaded by the execution framework via reflection. You must not change the name or the package of
 * this class. This class must implement the {@link ClassifierBuilder} interface.
 * <p>
 * This class must have a no-argument constructor. If you do not put any constructor, Java will create a no-arg
 * constructor for you.
 * <p>
 * With the provided execution framework, the {@link #training(Iterable)} method will be called several times with
 * different subset of the sample set. You should not keep information on static variable about previous sets.
 * This is pointless because during the competition this method will be called just once.
 * <p>
 * The code here must not access the filesystem or the network. It will be run with restricted security permission,
 * similar to a Java Applet in a browser. Your code will be run with -Xmx200M as VM argument. The test set will be supplied
 * as a file similar to reviews-sample.csv but larger than your heap. i.e. 200Mb+
 * <p>
 * To generate a jar for submission, a script has been provided for linux environment. It assumes that ant is available.
 * Please place all libraries in the lib directory and all your source code inside src/com.medallia.eci/ and its
 * subdirectories.  
 * run the following command in the directly in the project directory (where build.xml is found):
 * <code>
 * ant
 * <code>
 * Your jar file will be in the dist/ directory
 * <p>
 * To test run, you can either build the jar and run java -cp dist/com.medallia.challenge.jar com.medallia.eci.EciClassifierBuilder,
 * or just use an IDE and run this file.
 */
public class EciClassifierBuilder implements ClassifierBuilder {

	/**
	 * Startup method for the classifier. This method receives the sample tagged reviews. The code must learn from
	 * these examples, and build a classifier
	 *
	 * @param data Sample tagged reviews
	 * @return  A classifier built on the sample data
	 */
	@Override
	public Classifier training(Iterable<TaggedReview> data) {
		final HashSet<String> PositiveWords = new HashSet<String>();
		final HashSet<String> NegativeWords = new HashSet<String>();
		final StopWords sw = new StopWords();
		
		ArrayList<String> StopWords = new ArrayList<String>();
		StopWords.add("it");
		StopWords.add("has");
		StopWords.add("at");
		StopWords.add("the");
		StopWords.add("The");	
		StopWords.add("is");
		StopWords.add("an");
		StopWords.add("to");
		StopWords.add("this");
		StopWords.add(".");
		StopWords.add(",");
		StopWords.add("(");
		StopWords.add(")");
		StopWords.add("not");
		StopWords.add("and");
		StopWords.add("a");
		StopWords.add("of");
		
		for (TaggedReview taggedReview : data) {
			//for(String i: StopWords){
			//	taggedReview.review.replace(i, " ");
			//}
		    StringTokenizer st = new StringTokenizer(taggedReview.review);
			if(taggedReview.sentiment == Sentiment.POSITIVE){
				while (st.hasMoreTokens() ) {
					String s = st.nextToken();
					if(!sw.check(s))
						PositiveWords.add(s);
			     }
			}
			else{
				if(taggedReview.sentiment == Sentiment.NEGATIVE){
					
					while(st.hasMoreTokens()){
						String s = st.nextToken();
						if(!sw.check(s))
							NegativeWords.add(s);
					}
				}
			}
			//System.out.println(taggedReview.toString());
			// Do the training
			// Learn from the sample data
		}

		// Build the classifier
		return new Classifier() {
			/** @see Classifier#classify(String) */
			/* (non-Javadoc)
			 * @see challenge.lib.Classifier#classify(java.lang.String)
			 */
			@Override
			public Sentiment classify(String review) {
				int pos=0;
				int neg=0;
			    StringTokenizer st = new StringTokenizer(review);

				while(st.hasMoreTokens()){
					String x = st.nextToken();
					if(!sw.check(x)){
					System.out.print(x+"-");
						if(PositiveWords.contains(x)){
							pos++;
						}
						 if(NegativeWords.contains(x)){
							neg++;
						 }
					}
				}
				System.out.println(st.countTokens() + " " + pos + " " + neg);
				// Put your classifier here
				if( pos > neg)
					return Sentiment.POSITIVE;
				else if(pos < neg)
					return Sentiment.NEGATIVE;
				else 
					return Sentiment.NEUTRAL;
			}
		};
	}

	/**
	 * This main method is here for your convenience to ease debugging this class from an IDE.
	 *
	 * DO NOT PUT ANY CODE HERE!!! THIS METHOD IS NOT USED DURING THE COMPETITION !!!!
	 */
	public static void main(String[] args) throws Exception {
		Main.main(args);
	}

}
