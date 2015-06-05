***How to use the typo generator***
Let's say we want to have a 55% typo rate and our dataset is in the directories C:\some_dataset\neg and C:\some_dataset\pos. The only "piece" of my typo generator you'd actually execute is ConvertText. In the command line or Eclipse's run configurations, you'd enter the following arguments (for ConvertText):

55 C:\some_dataset\neg C:\some_dataset\pos

The directories you list in the arguments must be the lowest level directories, that is, the directories that contain the actual, original text files. After ConvertText is done running, a bunch of new files and directories will have been created.

First, there are the typo metrics files. They are found in the directory/ies one level up from the dataset directories. In our current example, you'd find both neg_typo_metrics_55.txt and pos_typo_metrics_55.txt in the C:\some_dataset directory.

Then, there are some new directories created within each dataset directory. In our example, C:\some_dataset\neg and C:\some_dataset\pos would each have two new directories inside of it, typo_55 and UTF8. The typo_55 directory would contain the text files injected with typos, while the UTF8 directory would contain the text files without typos; files in both directories would be in UTF8. To clarify, the new directories (with their full paths) would be C:\some_dataset\neg\typo_55, C:\some_dataset\neg\UTF8, C:\some_dataset\pos\typo_55, and C:\some_dataset\pos\UTF8.

Also, if you run it again with a 60% typo rate, you'd get new C:\some_dataset\neg\typo_60 and C:\some_dataset\pos\typo_60 directories, but the existing UTF8 directories would be overridden (shouldn't be a big deal; the files written in the UTF8 directories should be exactly the same every time anyway).