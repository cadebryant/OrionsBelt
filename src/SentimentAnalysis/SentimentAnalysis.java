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
	public static java.lang.Integer analyze(Text inputText, java.lang.Float matchThreshold, Text posWords, Text negWords)
	{
		// used to store positive and negative words for scoring
		String[] posWordList = posWords.getText().split(" ");
		String[] negWordList = negWords.getText().split(" ");

		String[] segments = inputText.getText().split("\n\r");
		
		int len = segments.length;
		int last = len-1;
		FuzzyMatch matcher = new FuzzyMatch(MatchType.Levenshtein, matchThreshold);
		
		// weighting factors:
		java.lang.Integer beginWeight = 3;
		java.lang.Integer midWeight = 1;
		java.lang.Integer endWeight = 5;

		int score = 0;
		for (int s = 0; s <= last; s++)
		{
            String segment = segments[s];
                        
			java.lang.Integer weight;
			if (s == 0)
				weight = beginWeight;
			else if (s == last)
				weight = endWeight;
			else
				weight = midWeight;
			
			int segmentPolarity = getSentimentScore(segment, matcher, (float) 1.0, posWordList, negWordList);
			
			if (segmentPolarity != 0)
				score += (weight * segmentPolarity);
		}
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
	public static int getSentimentScore(String input, FuzzyMatch matcher, float bias, String[] posWordList, String[] negWordList) {
		// normalize!
		input = input.toLowerCase();
		input = input.trim();
		// remove all non alpha-numeric non whitespace chars
		input = input.replaceAll("[^a-zA-Z0-9\\s]", "");
		float negAccumulator = (float) 0.0;
		float posAccumulator = (float) 0.0;

		// so what we got?
		String[] words = input.split(" ");
		
		float lastWordWasNotSimilarity = (float) 0.0;
		boolean lastWordWasNot = false;
		String thisWord;
		String lastWord = null;

		// check if the current word appears in our reference lists...
		for (int i = 0; i < words.length; i++) {
			
			thisWord = words[i];
			float thisWordWasNotSimilarity = matcher.MatchesNot(lastWord, thisWord);
			lastWord = thisWord;
			
			boolean isNot = (thisWordWasNotSimilarity > 0.0);			
			if (isNot)
			{
				lastWordWasNot = true;
				lastWordWasNotSimilarity = thisWordWasNotSimilarity;
				continue;	// SKIP "NOT"; it will be weighted with the next word AND reverse the polarity
			}
			float posSimilarity = matcher.MatchInArray(thisWord, posWordList);
			float negSimilarity = matcher.MatchInArray(thisWord, negWordList);
			
			if (lastWordWasNot)
			{
				if (posSimilarity > 0.0)
					posAccumulator -= (posSimilarity * lastWordWasNotSimilarity);
				if (negSimilarity > 0.0)
					negAccumulator += (negSimilarity * lastWordWasNotSimilarity);
				lastWordWasNot = false;
			}
			else
			{
				if (posSimilarity > 0.0)
					posAccumulator += posSimilarity;
				if (negSimilarity > 0.0)
					negAccumulator -= negSimilarity;
			}
		}
		float tuning = (float) 1.1;
		// positive matches MINUS negative matches
//		float result = (posAccumulator / (float) posWordList.length) + (negAccumulator / (float) negWordList.length);
		float result;
		if (bias > 0.0)
			result = (posAccumulator * bias) + (negAccumulator);
		else
			result = (posAccumulator) - (bias * negAccumulator);

		if (result < 0.0)
			return -1; // neg
		else if (result > 0.0)
			return  1; // pos

		return 0; // neutral
	}
}