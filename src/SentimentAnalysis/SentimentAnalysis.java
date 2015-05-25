package SentimentAnalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

public class SentimentAnalysis {

	// path to language profiles for classifier
	private static String langProfileDirectory = "./src/profiles/";

	// lucene queryParser for saving
	//private static QueryParser queryParser;

	// used to store positive and negative words for scoring
	static List<String> posWords = new ArrayList<String>();
	static List<String> negWords = new ArrayList<String>();

	// keep some stats! [-1 / 0 / 1 / not english / foursquare / no text to
	// classify]
	static int[] stats = new int[6];

	/**
	 * @param args
	 * @throws IOException
	 * @throws LangDetectException
	 */
	public static void analyze(String inputText) throws IOException,
			LangDetectException {

		long startTime = System.currentTimeMillis();

		// open lucene index
		Directory dir;
		IndexReader docReader = null;

		// source: www.cs.uic.edu/~liub/FBS/sentiment-analysis.html
		BufferedReader negReader = new BufferedReader(new FileReader(new File(
				"./src/SentimentAnalysis/negative-words.txt")));
		BufferedReader posReader = new BufferedReader(new FileReader(new File(
				"./src/SentimentAnalysis/positive-words.txt")));

		// currently read word
		String word;

		// add words to comparison list
		while ((word = negReader.readLine()) != null) {
			negWords.add(word);
		}
		while ((word = posReader.readLine()) != null) {
			posWords.add(word);
		}

		// cleanup
		negReader.close();
		posReader.close();
		
		int score = 0;
		
		// prepare language classifier
		DetectorFactory.loadProfile(langProfileDirectory);
		// store different languages
		Map<String, Integer> langHitList = new HashMap<String, Integer>();

		// detect language, using http://code.google.com/p/language-detection/
		// has 99% accuracy
		Detector detector;

		// we need a new instance every time unfortunately...
		detector = DetectorFactory.create();
		detector.append(inputText);
		// classify language!
		String detectedLanguage = detector.detect();

		// if it is not english...
		if (detectedLanguage.equals("en") == false) {
			stats[3]++;
			score = 0;
		} else {
			score = getSentimentScore(inputText);
			// ++ index so we won't have -1 and stuff...
			stats[score + 1]++;
		}

		Integer currentCount = langHitList.get(detectedLanguage);
		// ...save the detected language for some stats
		langHitList.put(detectedLanguage,
				(currentCount == null) ? 1 : currentCount + 1);
		
		// tweet.set("language", detectedLanguage)
		// tweet.set("sentiment", score);
		// tweet.get("ID");
	}

	/**
	 * does some string mangling and then calculates occurrences in positive /
	 * negative word list and finally the delta
	 * 
	 * 
	 * @param input
	 *            String: the text to classify
	 * @return score int: if < 0 then -1, if > 0 then 1 otherwise 0 - we don't
	 *         care about the actual delta
	 */
	private static int getSentimentScore(String input) {
		// normalize!
		input = input.toLowerCase();
		input = input.trim();
		// remove all non alpha-numeric non whitespace chars
		input = input.replaceAll("[^a-zA-Z0-9\\s]", "");

		int negCounter = 0;
		int posCounter = 0;

		// so what we got?
		String[] words = input.split(" ");

		// check if the current word appears in our reference lists...
		for (int i = 0; i < words.length; i++) {
			if (posWords.contains(words[i])) {
				posCounter++;
			}
			if (negWords.contains(words[i])) {
				negCounter++;
			}
		}

		// positive matches MINUS negative matches
		int result = (posCounter - negCounter);

		// negative?
		if (result < 0) {
			return -1;
			// or positive?
		} else if (result > 0) {
			return 1;
		}

		// neutral to the rescue!
		return 0;
	}

}