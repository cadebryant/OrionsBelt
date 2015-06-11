package UnitTest;

import FuzzyMatch.FuzzyMatch;
import FuzzyMatch.MatchType;
import SentimentAnalysis.SentimentAnalysis;
//import com.ibm.avatar.algebra.datamodel.Text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
//import java.io.FileWriter;
import java.io.File;
import java.io.FilenameFilter;

public class Test575 implements FilenameFilter
{
    public Test575()
    {
        ;
    }
    @Override
    public boolean accept(File dir, String name)
    {
        return name.matches(".*.txt");
    }
    
    public static int analyze(BufferedReader reader, FuzzyMatch matcher, String[] posWordList, String[] negWordList) throws IOException
    {
        String nextLine = null;
        int weight = 3;
        int score = 0;
        
        String nextSegment = null;
        for (String segment = reader.readLine(); segment != null; segment = nextSegment)
        {
            nextSegment = reader.readLine();
            
            if (nextSegment == null)
                weight = 5;
            
            int segmentPolarity = SentimentAnalysis.getSentimentScore(segment, matcher, posWordList, negWordList);

            if (segmentPolarity != 0)
                score += (weight * segmentPolarity);
            
            weight = 1;
        }
        return score;
    }
    String[] GetFiles(String path)
    {
        File dir = new File(path);
        
        return dir.list((FilenameFilter) this);
    }
    public static void main(String[] args)
    {
	long startTime = System.currentTimeMillis();
		
        String[] positives;
        String[] negatives;
        String root = "/Users/kwonus/git/OrionsBelt/textAnalytics/src/main/MovieReviews";
        String grouping = "100";
        float similarityThreshold = (float) 0.0;
        
        if (args.length > 0)
            root = args[0];
        if (args.length > 1)
        {
            if (args[1] == "minimum")
                similarityThreshold = (float) 0.8;
            if (args[1] == "medium")
                similarityThreshold = (float) 0.9;
            else // "exact"
                similarityThreshold = (float) 1.0;
        }
        if (args.length > 2)
            grouping = args[2];

        BufferedReader reader = null;        
        FuzzyMatch matcher = new FuzzyMatch(MatchType.Levenshtein, similarityThreshold);
        int score = 0;
        
        try
        {
            //  First load polarity lists
            //
            String word;
            BufferedReader polarityReader = new BufferedReader(new FileReader(root + "/positive-words.txt"));
            String polarityStrings = null;
            for (word = polarityReader.readLine(); word != null; word = polarityReader.readLine())
            {
                if (word.length() < 1 || word.charAt(0) == ';')
                    continue;

                if (polarityStrings == null)
                    polarityStrings = "";
                else
                    polarityStrings += " ";
                polarityStrings += word;
            }
            polarityReader.close();
            positives = polarityStrings.split(" ");
            
            polarityReader = new BufferedReader(new FileReader(root + "/negative-words-halved.txt"));
            polarityStrings = null;
            for (word = polarityReader.readLine(); word != null; word = polarityReader.readLine())
            {
                if (word.length() < 1 || word.charAt(0) == ';')
                    continue;

                if (polarityStrings == null)
                    polarityStrings = "";
                else
                    polarityStrings += " ";
                polarityStrings += word;
            }
            polarityReader.close();
            negatives = polarityStrings.split(" ");
            // end loading lists
            
            System.out.println("file" + "," + "persentage typos" + "," + "measured" + "," + "polarity" + "," + "score" + "," + "correct" + "," + "duration");
            Test575 test = new Test575();

            String[] polarity = { "pos", "neg" };
            String[] typos = { "0", "10", "25", "50", "100" };
            
            for (int p = polarity.length-1; p >= 0; p--)
            {
                String dir = root + "/" + polarity[p] + "/typos." + grouping;

                String[] files = test.GetFiles(dir);
                int fcnt = files.length;

                for (int f = 0; f < fcnt; f++)
                {
                    long docStartTime = System.currentTimeMillis();
                    String file = dir + '/' + files[f];
                    reader = new BufferedReader(new FileReader(file));
                    score = Test575.analyze(reader, matcher, positives, negatives);
                    reader.close();
                    reader = null;

                    String measured = "";
                    if (score > 0)
                        measured = "pos";
                    else if (score < 0)
                        measured = "neg";
                    else
                        measured = "";
                    String correct = (measured == polarity[p]) ? "1" : "0";
                    long docEndTime = System.currentTimeMillis();
                    System.out.println(files[f] + "," + grouping + "%," + measured + "," + polarity[p] + "," + score + "," + correct + "," + (docEndTime-docStartTime));
                }                    
            }
            long endTime = System.currentTimeMillis();
            System.out.println("RUN TIME (milliseconds) = " + (endTime-startTime));
        }
        catch (IOException e)
        {
            System.err.println("Error: " + e.getMessage());
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException ex)
                {
                    ;
                }
            }
        }
    }
}