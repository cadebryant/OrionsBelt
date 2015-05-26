package SentimentAnalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import FuzzyMatch.FuzzyMatch;
import FuzzyMatch.MatchType;

public class SentimentAnalysis {
	
	// declare our fuzzy matcher:
	private static FuzzyMatch fuzzy;

	// used to store positive and negative words for scoring
	static List<String> posWords = new ArrayList<String>();
	static List<String> negWords = new ArrayList<String>();

	// keep some stats! [-1 / 0 / 1 / no text to
	// classify]
	static int[] stats = new int[6];

	/**
	 * @param args
	 * @throws IOException
	 * @throws LangDetectException
	 */
	public static int analyze(String inputText, boolean useFuzzy, float matchThreshold) throws IOException {

		fuzzy = new FuzzyMatch(MatchType.Levenshtein, matchThreshold);
		long startTime = System.currentTimeMillis();

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
		score = getSentimentScore(inputText, useFuzzy);
		// ++ index so we won't have -1 and stuff...
		stats[score + 1]++;
		
		return score;
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
	private static int getSentimentScore(String input, boolean useFuzzyMatching) {
		// apply fuzzy matching:
		// TODO: this is a hack for now; obviously comparing the input to itself will return true :-)
		boolean isMatch =  fuzzy.Compare(input, input);
		// don't go into SA logic if fuzzy match fails, 
		// unless the user specifies to NOT use fuzzy matching:
		if (isMatch || !useFuzzyMatching) {
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
		}
		// neutral to the rescue!
		return 0;
	}
}