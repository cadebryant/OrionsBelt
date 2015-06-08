package SentimentAnalysis;

// import java.io.IOException;
import FuzzyMatch.FuzzyMatch;
import FuzzyMatch.MatchType;
import com.ibm.avatar.algebra.datamodel.Text;


public class SentimentAnalysis {

	/**
	 * @param args
	 * @throws IOException
	 * @throws LangDetectException
	 */
	public static java.lang.Integer analyze(Text inputText, java.lang.Float matchThreshold, Text posWords, Text negWords) /*throws IOException*/ {
		// used to store positive and negative words for scoring
		String[] posWordList = posWords.getText().split(" ");
		String[] negWordList = negWords.getText().split(" ");

		// keep some stats! [-1 / 0 / 1 / no text to
		// classify]
		//int[] stats = new int[6];		
		
		long startTime = System.currentTimeMillis();
		
		String[] segments = inputText.getText().split("\n\r");
		
		int len = segments.length;
		int last = len-1;
		FuzzyMatch matcher = new FuzzyMatch(MatchType.Levenshtein, matchThreshold);
		
		// weighting factors:
		java.lang.Integer beginWeight = 3;
		java.lang.Integer midWeight = 1;
		java.lang.Integer endWeight = 5;

		int score = 0;
		for (int s = 0; s <= last; s++) {
            String segment = segments[s];
                        
			java.lang.Integer weight;
			if (s == 0) {
				weight = beginWeight;
			} else if (s == last) {
				weight = endWeight;
			} else {
				weight = midWeight;
			}
			float segmentPolarity = getSentimentScore(segment, matcher, posWordList, negWordList);
			
			if (segmentPolarity > 0.0)
				score += weight;
			else if (segmentPolarity < 0.0);
				score -= weight;
		}
		// ++ index so we won't have -1 and stuff...
		//stats[score + 1]++;		// pretty sure that this is where your exception was coming from
		
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
	private static float getSentimentScore(String input, FuzzyMatch matcher, String[] posWordList, String[] negWordList) {
		// normalize!
		input = input.toLowerCase();
		input = input.trim();
		// remove all non alpha-numeric non whitespace chars
		input = input.replaceAll("[^a-zA-Z0-9\\s]", "");
		int negCounter = 0;
		int posCounter = 0;

		// so what we got?
		String[] words = input.split(" ");
		
		float lastWordWasNotSimilarity = (float) 0.0;
		boolean lastWordWasNot = false;

		// check if the current word appears in our reference lists...
		for (int i = 0; i < words.length; i++) {
			
			float thisWordWasNotSimilarity = matcher.MatchesNot(words[i]);	
			boolean isNot = (thisWordWasNotSimilarity > 0.0);			
			if (isNot)
			{
				lastWordWasNot = true;
				lastWordWasNotSimilarity = thisWordWasNotSimilarity;
				continue;	// SKIP "NOT"; it will be weighted with the next word AND reverse the polarity
			}
			float posSimilarity = matcher.MatchInArray(words[i], posWordList);
			float negSimilarity = matcher.MatchInArray(words[i], negWordList);
			
			if (lastWordWasNot)
			{
				if (posSimilarity > 0.0)
					posCounter -= (posSimilarity * lastWordWasNotSimilarity);
				if (negSimilarity > 0.0)
					negCounter += (negSimilarity * lastWordWasNotSimilarity);
				lastWordWasNot = false;
			}
			else
			{
				if (posSimilarity > 0.0)
					posCounter += posSimilarity;
				if (negSimilarity > 0.0)
					negCounter -= negSimilarity;
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