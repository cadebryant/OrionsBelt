package FuzzyMatch;

import FuzzyMatch.MatchType;
import uk.ac.shef.wit.simmetrics.similaritymetrics.*;;

public class FuzzyMatch
{	
    private AbstractStringMetric Metric;
    private float AcceptableDistance;
    public FuzzyMatch(MatchType type, float threshhold)
    {
        switch (type)
        {
            case Levenshtein:		Metric = new Levenshtein();			break;
            case CosineSimilarity:	Metric = new CosineSimilarity();	break;
            case EuclideanDistance: Metric = new EuclideanDistance();	break;
            case MongeElkan:		Metric = new MongeElkan();			break;
            case JaroWinkler:		Metric = new JaroWinkler();			break;
            default:				Metric = new Levenshtein();			break;
        }
        AcceptableDistance = threshhold;
    }
    public float Compare(String string1, String string2)
    {
        float similarity = Metric.getSimilarity(string1, string2);

        if (similarity >= 1 - AcceptableDistance)
            return similarity;

        return (float) 0.0;
    }
    public float MatchesNot(String prev, String word)
    {
    	if ( (prev == "'") && (word == "nt") )
	    	return (float) 1.0;  // matches contractions like don't or can't (but not with fuzzy logic)

    	return Compare(word, "not");
    }
    public float MatchInArray(String word, String[] array)
    {
        float similarity = (float) 0.0;
        int len = array.length;
        for (int i = 0; i < len; i++)
        {
            float check = Compare(word, array[i]);

            if (check >= 1.0)
                return (float) 1.0;

            else if (check > similarity)
                similarity = check;
        }
        return similarity;
    }
}
