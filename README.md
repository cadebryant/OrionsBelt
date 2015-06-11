***How to use the typo generator***
Let's say your dataset is in the directories C:\some_dataset\neg and C:\some_dataset\pos. The only "piece" of the typo generator you'd actually execute is ConvertText. In the command line or Eclipse's run configurations, you'd enter the following arguments (for ConvertText):

C:\some_dataset\neg C:\some_dataset\pos

The directories you list in the arguments must be the lowest level directories, that is, the directories that contain the actual, original text files. After ConvertText is done running, a bunch of new files and directories will have been created.

First, there are the typo metrics files. They are found in the directory or directories one level up from the dataset directories. In our current example, you'd find the following files in the C:\some_dataset directory:

neg_typo_metrics_0.txt
neg_typo_metrics_10.txt
neg_typo_metrics_25.txt
neg_typo_metrics_50.txt
neg_typo_metrics_100.txt
pos_typo_metrics_0.txt
pos_typo_metrics_10.txt
pos_typo_metrics_25.txt
pos_typo_metrics_50.txt
pos_typo_metrics_100.txt

Secondly, the following directories (with their full paths) will have been created in our current example:

C:\some_dataset\neg\typos.0
C:\some_dataset\neg\typos.10
C:\some_dataset\neg\typos.25
C:\some_dataset\neg\typos.50
C:\some_dataset\neg\typos.100
C:\some_dataset\pos\typos.0
C:\some_dataset\pos\typos.10
C:\some_dataset\pos\typos.25
C:\some_dataset\pos\typos.50
C:\some_dataset\pos\typos.100

The typos.0 directories contain UTF8 text files with no typos induced, the typos.10 directories contain UTF8 text files generated with a 10% typo rate, the typos.25 directories contain UTF8 text files generated with a 25% typo rate, etc.

How to run from UnitTest/Test575.java:
	1. In Eclipse Project Explorer, expand OrionsBelt/src/UnitTest/Test575.java
	2. Right click Test575.java, select Run As -> Run Configurations
	3. Create a new Run Configuration for UnitTest.Test575
	4. In the Arguments tab of the Run Configurations window, in Program Arguments, enter <directory_path> <threshold> <grouping>
		where
			<directory_path> is the path to your local directory that contains your review files
			<threshold> is one of ( minimum | medium | exact ) and is lowercase
				minimum: threshold = 0.8
				medium: threshold = 0.9
				exact: threshold = 1.0
			<grouping> is percentage of typos to account for in document, i.e., 0, 100
			
			Example:
			/Users/kwonus/git/OrionsBelt/textAnalytics/src/main/MovieReviews exact 0
			
	5. Click Apply and Run.

	Example output:
	file,persentage typos,measured,polarity,score,correct,duration
	cv000_29416.txt,0%,neg,neg,-34,1,14938
	cv001_19502.txt,0%,neg,neg,-10,1,4131
	cv002_17424.txt,0%,neg,neg,-29,1,6036
	cv003_12683.txt,0%,neg,neg,-39,1,4964
	cv004_12641.txt,0%,neg,neg,-40,1,5872
	cv005_29357.txt,0%,pos,neg,12,0,4981
				.
				.
				.
				.
				.

How to run as an AQL script (NOTE: this portion of our implementation is buggy and not working as expected):

	1. Create an AQL script which declares a new UDF, similar to the following:
			
			module main; 
			create function udfAnalyzeSentiment(inputText Text, matchThreshold Float, posWords Text, negWords Text)
			return Integer
			external_name 'SentimentAnalysis.jar:SentimentAnalysis.SentimentAnalysis!analyze'
			language java
			deterministic
			return null on null input;
			
	2. Create a second AQL script for creating/outputting the dictionaries/views, similar to the following:
			
			module main; 

			create dictionary reviews
			from file 'MovieReviews/reviews.txt'; --path to your input file

			create dictionary posWords
			from file 'MovieReviews/positive-words.txt'; --path to file of positive polarity words

			create dictionary negWords
			from file 'MovieReviews/negative-words.txt'; --path to file of negative polarity words

			create view testView as
			select * from
			(extract dictionary 'reviews' on D.text as reviews from Document D) reviews,
			(extract dictionary 'posWords' on D.text as posWords from Document D) posWords,
			(extract dictionary 'negWords' on D.text as negWords from Document D) negWords;


			create view testSentiment as
			select t.reviews,
			udfAnalyzeSentiment(t.reviews, 0.8, t.posWords, t.negWords) as score
			from testView t;

			output view testSentiment;

		Currently the above script returns a java exception in SentimentAnalysis.java, the cause of which has not been determined.
				
				


		
