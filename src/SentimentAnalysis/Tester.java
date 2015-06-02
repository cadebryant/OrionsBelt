package SentimentAnalysis;
import com.ibm.avatar.algebra.datamodel.Text;
import com.ibm.avatar.algebra.util.lang.LangCode;
public class Tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Text inputText = new Text("the movie was good.  test test test test test test.  the movie was bad.", LangCode.en);
		Text goodWords = new Text("good good good", LangCode.en);
		Text badWords = new Text("bad bad bad", LangCode.en);
		try {
			System.out.println(SentimentAnalysis.analyze(inputText, 0, (float) 0.8, goodWords, badWords));
		} catch (Exception e) {
			
		}

	}

}
