
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TextGenerator {

	public static String readFileAsString(String fileName) {
		
	    String text = "";
	    try {
	      text = new String(Files.readAllBytes(Paths.get(fileName)));
	    } 
	    catch (IOException e) {
	      e.printStackTrace();
	    }

	    return text;
	  }
	
	public static void main(String[] args) {
		
		if (args.length != 3) {
			System.out.println("Incorrect number of arguments.");
		}
		else {
			String str = readFileAsString(args[2]);
			//System.out.println(str);
			int k = Integer.parseInt(args[0]);
			int T = Integer.parseInt(args[1]);
			MarkovModel mk = new MarkovModel(str, k);
			
			System.out.println(mk.gen(str.substring(0, k), T));
		}
	}
}
