package UnitTest;

import FuzzyMatch.FuzzyMatch;
import FuzzyMatch.MatchType;
import SentimentAnalysis.SentimentAnalysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
        long startTime = System.currentTimeMillis();

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
        
        if (args.length > 0)
            root = args[0];

        BufferedReader reader = null;        
        FuzzyMatch matcher = new FuzzyMatch(MatchType.Levenshtein, (float) 0.8);
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
            
            polarityReader = new BufferedReader(new FileReader(root + "/negative-words.txt"));
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
            
            System.out.println("file" + "," + "persentage typos" + "," + "measured" + "," + "polarity" + "," + "score" + "," + "correct");
            Test575 test = new Test575();

            String[] polarity = { "pos", "neg" };
            String[] typos = { "0", "10", "25", "50", "100" };
            
            for (int p = polarity.length-1; p >= 0; p--)
            {
                for (int t = typos.length - 1; t >= 0; t--)
                {
                    String dir = root + "/" + polarity[p] + "/typos." + typos[t];
            
                    String[] files = test.GetFiles(dir);
                    int fcnt = files.length;

                    for (int f = 0; f < fcnt; f++)
                    {
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
                        System.out.println(files[f] + "," + typos[t] + "%," + measured + "," + polarity[p] + "," + score + "," + correct);
                    }                    
                }
            }
            long endTime = System.currentTimeMillis();
            System.out.println("RUN TIME (milliseconds) = " + (startTime-endTime));
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