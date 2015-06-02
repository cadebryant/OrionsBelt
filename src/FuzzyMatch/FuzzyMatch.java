package FuzzyMatch;

import uk.ac.shef.wit.simmetrics.similaritymetrics.*;

public class FuzzyMatch {
	
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
	public boolean Compare(String phrase1, String phrase2)
	{
		float similarity = Metric.getSimilarity(phrase1, phrase2);
		
		return (similarity >= 1 - AcceptableDistance);
	}
}
