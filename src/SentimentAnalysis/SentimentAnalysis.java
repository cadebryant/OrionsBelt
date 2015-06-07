package SentimentAnalysis;

import java.io.IOException;
import com.ibm.avatar.algebra.datamodel.Text;

import FuzzyMatch.FuzzyMatch;
import FuzzyMatch.MatchType;

public class SentimentAnalysis {

	/**
	 * @param args
	 * @throws IOException
	 * @throws LangDetectException
	 */
	public static java.lang.Integer analyze(Text inputText, java.lang.Float matchThreshold, Text posWords, Text negWords) throws IOException {
		// used to store positive and negative words for scoring
		String[] posWordList = posWords.getText().split(" ");
		String[] negWordList = negWords.getText().split(" ");

		// keep some stats! [-1 / 0 / 1 / no text to
		// classify]
		int[] stats = new int[6];		
		
		long startTime = System.currentTimeMillis();
		
		int score = 0;
		score = getSentimentScore(inputText.getText(), matchThreshold, posWordList, negWordList);
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
	private static int getSentimentScore(String input, java.lang.Float matchThreshold, String[] posWordList, String[] negWordList) {
		// normalize!
		input = input.toLowerCase();
		input = input.trim();
		// remove all non alpha-numeric non whitespace chars
		input = input.replaceAll("[^a-zA-Z0-9\\s]", "");
		FuzzyMatch fuzzy = new FuzzyMatch(MatchType.Levenshtein, matchThreshold);
		int negCounter = 0;
		int posCounter = 0;

		// so what we got?
		String[] words = input.split(" ");
		
		// weighting factors:
		int beginWeight = 3;
		int midWeight = 1;
		int endWeight = 5;

		// check if the current word appears in our reference lists...
		for (int i = 0; i < words.length; i++) {
			int weight = 1;
			if (i <= words.length / 3) {
				weight = beginWeight;
			} else if (i >= words.length - (words.length / 3)) {
				weight = endWeight;
			} else {
				weight = midWeight;
			}
			
			posCounter += (fuzzy.MatchInArray(words[i], posWordList) - fuzzy.MatchesNot(words[i])) * weight;
			negCounter += (fuzzy.MatchInArray(words[i], negWordList) - fuzzy.MatchesNot(words[i])) * weight;
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