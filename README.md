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