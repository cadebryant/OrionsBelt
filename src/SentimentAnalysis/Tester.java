package SentimentAnalysis;
import com.ibm.avatar.algebra.datamodel.Text;
import com.ibm.avatar.algebra.util.lang.LangCode;
public class Tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Text text = new Text("good bad ugly", LangCode.en);
		try {
			System.out.println(SentimentAnalysis.analyze(text, 1, (float) 0.8));
		} catch (Exception e) {
			
		}

	}

}
