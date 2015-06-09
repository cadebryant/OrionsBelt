package TypoGen;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.List;

public class ConvertText {
	/**
	 * @param args
	 * **COMMAND LINE ARGUMENTS FOR CONVERTTEXT**
	 * *Use dataset directories as your command line arguments.
	 * *For example: C:\txt_sentoken\neg C:\txt_sentoken\pos
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] typoRates = {"0", "10", "25", "50", "100"};

		for (String typoRate: typoRates) {
			TypoGen typoMaker = new TypoGen(Integer.parseInt(typoRate));

			for (String arg: args) {
				Path dir = Paths.get(arg);
				Path typoDir = Paths.get(arg + "/typos." + typoRate);
				int typoSum = 0;
				int docCount = 0;
				double percentSum = 0.0;
				String typoMetrics = "";
				File metricsFile = new File(arg + "_typo_metrics_" + typoRate + ".txt");

				try {
					Files.createDirectories(typoDir);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.{txt}")) {
					for (Path entry: stream) {
						List<String> entryLines = Files.readAllLines(entry, Charset.defaultCharset());
						String typoText = "";
						String typoText2 = "";
						String[] pivotWords = {"and", "adn", "anbd", "annd", "although", "allthough", "althought", "altough", "but", "bu", "however", "howver", "then", "hten", "thn", "thne", "though", "yet", "yot"};
						File typoFile = new File(arg + "/typos." + typoRate + '/' + entry.getFileName());

						for (String entryLine: entryLines) {
							String typoedLine = typoMaker.insertTypos(entryLine);
							typoText = typoText + typoedLine.replaceAll("[ \t\r\n]+", " ") + ' ';
						}

						for (char tc: typoText.toCharArray()) {
							if (Character.isLetterOrDigit(tc)) {
								typoText2 = typoText2 + tc;
							} else if (tc == ' ' || tc == '\'') {
								typoText2 = typoText2 + tc;
							} else {
								typoText2 = typoText2 + "\n\r" + tc;
							}
						}

						for (String pivotWord: pivotWords) {
							String typoText3 = typoText2.replaceAll(' ' + pivotWord + ' ', "\n\r" + pivotWord + ' ');
							typoText2 = typoText3;
						}

						typoMetrics = typoMetrics + entry.getFileName() + ": " + Integer.toString(typoMaker.typoCount()) + " words were changed, which is " + Double.toString(typoMaker.typoPercentage()) + "% of the words.\n";
						typoSum += typoMaker.typoCount();
						percentSum += typoMaker.typoPercentage();
						docCount++;
						typoMaker.resetCounts();
						byte[] typoFileBytes = null;
						String typoText4 = typoText2.replaceAll("[ ]+", " ").replaceAll(" \n", "\n").trim();

						try {
							typoFileBytes = UnicodeUtils.convert(typoText4.getBytes(Charset.defaultCharset()), "UTF-8");
						} catch (Exception b1) {
							// TODO Auto-generated catch block
							b1.printStackTrace();
						}

						try (FileOutputStream writer1 = new FileOutputStream(typoFile)) {
							writer1.write(typoFileBytes);
						} catch (IOException v) {
							v.printStackTrace();
						}
					}
				} catch (IOException x) {
					x.printStackTrace();
				}

				double avgTypos = (double) typoSum / (double) docCount;
				double avgPercent = percentSum / (double) docCount;
				typoMetrics = "Per document average: " + Double.toString(avgTypos) + " words were changed, which is " + Double.toString(avgPercent) + "% of the words.\n---------\n" + typoMetrics;
				byte[] typoMetricsBytes = null;

				try {
					typoMetricsBytes = UnicodeUtils.convert(typoMetrics.getBytes(Charset.defaultCharset()), "UTF-8");
				} catch (Exception b2) {
					// TODO Auto-generated catch block
					b2.printStackTrace();
				}

				try (FileOutputStream writer2 = new FileOutputStream(metricsFile)) {
					writer2.write(typoMetricsBytes);
				} catch (IOException y) {
					y.printStackTrace();
				}
			}
		}
	}
}