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
		TypoGen typoMaker = new TypoGen(true);

		for (String arg: args) {
			Path dir = Paths.get(arg);
			Path typoDir = Paths.get(arg + "/typo");
			Path UTF8Dir = Paths.get(arg + "/UTF8");
			int typoSum = 0;
			int docCount = 0;
			double percentSum = 0.0;
			String typoMetrics = "";
			File metricsFile = new File(arg + "_typo_metrics.txt");

			try {
				Files.createDirectories(typoDir);
				Files.createDirectories(UTF8Dir);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.{txt}")) {
				for (Path entry: stream) {
					List<String> entryLines = Files.readAllLines(entry, Charset.defaultCharset());
					String typoText = "";
					String textUTF8 = "";
					File typoFile = new File(arg + "/typo/typo_" + entry.getFileName());
					File UTF8File = new File(arg + "/UTF8/UTF8_" + entry.getFileName());

					for (String entryLine: entryLines) {
						typoText = typoText + typoMaker.insertTypos(entryLine) + '\n';
						textUTF8 = textUTF8 + entryLine + '\n';
					}

					typoMetrics = typoMetrics + entry.getFileName() + ": " + Integer.toString(typoMaker.typoCount()) + " words were changed, which is " + Double.toString(typoMaker.typoPercentage()) + "% of the words.\n";
					typoSum += typoMaker.typoCount();
					percentSum += typoMaker.typoPercentage();
					docCount++;
					typoMaker.resetCounts();
					byte[] typoFileBytes = null;
					byte[] UTF8FileBytes = null;

					try {
						typoFileBytes = UnicodeUtils.convert(typoText.getBytes(Charset.defaultCharset()), "UTF-8");
					} catch (Exception b1) {
						// TODO Auto-generated catch block
						b1.printStackTrace();
					}

					try {
						UTF8FileBytes = UnicodeUtils.convert(textUTF8.getBytes(Charset.defaultCharset()), "UTF-8");
					} catch (Exception b2) {
						// TODO Auto-generated catch block
						b2.printStackTrace();
					}

					try (FileOutputStream writer1 = new FileOutputStream(typoFile)) {
						writer1.write(typoFileBytes);
					} catch (IOException v) {
						v.printStackTrace();
					}

					try (FileOutputStream writer2 = new FileOutputStream(UTF8File)) {
						writer2.write(UTF8FileBytes);
					} catch (IOException w) {
						w.printStackTrace();
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
			} catch (Exception b3) {
				// TODO Auto-generated catch block
				b3.printStackTrace();
			}

			try (FileOutputStream writer3 = new FileOutputStream(metricsFile)) {
				writer3.write(typoMetricsBytes);
			} catch (IOException y) {
				y.printStackTrace();
			}
		}
	}
}