package SentimentAnalysis;
import com.ibm.avatar.algebra.datamodel.Text;
import com.ibm.avatar.algebra.util.lang.LangCode;
public class Tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Text inputText = new Text("good movie", LangCode.en);
		Text goodWords = new Text("good great awesome", LangCode.en);
		Text badWords = new Text("bad terrible lousy", LangCode.en);
		try {
			System.out.println(SentimentAnalysis.analyze(inputText, (float) 0.2, goodWords, badWords));
		} catch (Exception e) {
			
		}

	}

}
