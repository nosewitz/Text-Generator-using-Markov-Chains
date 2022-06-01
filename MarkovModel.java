
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;


public class MarkovModel {

	protected HashMap<String, HashMap<Character, Integer>> likelihood = new HashMap<>();
	protected HashMap<String,  HashMap<Character, Double>> probabilities =new HashMap<>();
	protected HashMap<String, Integer> freqs = new HashMap<>();
	protected int size=0;
	
	
	protected int k;
	// create a Markov model of order k from given text
    // Assume that text has length at least k.
    MarkovModel(String text, int k) {
    	//Verification
    	text = text.toLowerCase();
    	if (text.length() < k) {
    		throw new RuntimeException("Text must be of size k or larger.");
    	}
    	//Setting data
    	this.k=k;
    	size=text.length();
    	
    	
    	//Split string into k-grams and convert to collection (list)
    	String txt = text+text.substring(0, k-1);
    	ArrayList<String> kgrams = new ArrayList<String>();
    	for (int i = 0; i < txt.length() && !(i+k > txt.length()); i++) {

    		String g = txt.substring(i, i+k);
    		//Freq table, updates values or starts them off
    		if (freqs.containsKey(g)) {
        		int currFreq = freqs.get(g);
        		freqs.put(g,  currFreq+1);
    		}
    		else {freqs.put(g,  1); }
    		kgrams.add(g);
    	}
    	
    	//Used for accessing kgrams
    	int index=0;
    	while ( index < kgrams.size()  ) {
    		//Current and following char
    		String currKgram = kgrams.get(index);
    		char following = 'a';
    		
    		if (index+k < txt.length()) { following = txt.charAt( index+k); }
    		else { following = txt.charAt( k-1); }

    		// Retrieving next kgram to access the next char
    	
    		//The following char
    		if (!likelihood.containsKey(currKgram)) {
    			//Likelihood table
        		HashMap<Character, Integer> innerMap =new HashMap<Character, Integer>();
        		innerMap.put(following, 1);
        		
        		likelihood.put(currKgram, innerMap);
        		index++;
        		continue;
    		}
    		// If char is not in kgram's hashmap then it must be added
    		if ( likelihood.get(currKgram).get( following ) == null ) {
    			likelihood.get(currKgram).put(following, 1);
    			}
    		else {
    			int currCount = likelihood.get(currKgram).get( following );
    			likelihood.get(currKgram).put(following, currCount+1);

    		}
    		index++;

    	}
    	
		
		for (Entry<String, HashMap<Character, Integer>> pair : likelihood.entrySet()) {
			
			String key= pair.getKey();
			HashMap<Character, Integer> inner = pair.getValue();
			int space = freqs.get(key);
		
			for (Entry<Character,Integer> inPair : inner.entrySet()) {
				
				if (!probabilities.containsKey(key)) {
					HashMap<Character, Double> probs = new HashMap<>();
					probs.put(inPair.getKey(), inPair.getValue()/(double)space);
					probabilities.put( key, probs);
				}
				
				probabilities.get(key).put(inPair.getKey(), inPair.getValue()/(double)space);
			}
			//probabilities.put(pair.getKey(), new HashMap<Character, Double>());
		}
		

    }

	// order k of Markov model
    public int order() {
    	return k;
    }

    
	// number of occurrences of kgram in text
    public int freq(String kgram) throws RuntimeException {
    	if (kgram.length() != k) {
        	throw new RuntimeException("kgram is not of length k");
    	}
    	return freqs.get(kgram);

    }
    // (throw an exception if kgram is not of length k)

    
    // number of times that character c follows kgram
    // (throw an exception if kgram is not of length k)
    public int freq(String kgram, char c) {   
    	return likelihood.get(kgram).get(c);
    }
    	
    	
 // random character following given kgram
    // (Throw an exception if kgram is not of length k.
    //  Throw an exception if no such kgram.)
	public char rand(String kgram) {
		if ( !likelihood.containsKey(kgram) || kgram.length() != k) {
			System.out.println(kgram);
			throw new RuntimeException("Invalid kgram");
		}
		
		HashMap<Character, Double> inner = probabilities.get(kgram);		
		Double[] probs = inner.values().toArray(new Double[inner.size()]);
		Set<Character> set = inner.keySet();

		// Input probabilities and respective chars (as arraylist)
		return sample(probs, set.toArray(new Character[set.size()]));
    }
    
    
	// generate a String of length T characters
    // by simulating a trajectory through the corresponding
    // Markov chain.  The first k characters of the newly
    // generated String should be the argument kgram.
    // Throw an exception if kgram is not of length k.
    // Assume that T is at least k.
	public String gen(String kgram, int T) {    
		
		if (kgram.length() != k) {
			throw new RuntimeException("Invalid kgram.");
		}
		
		
		String shortened = kgram;
		StringBuilder str = new StringBuilder(shortened);
		
		int i = 1;
		while (str.length() < T) {

			str.append( rand(shortened) );
			shortened=str.toString().substring(i, str.length());
			++i;

		}
		
    	return str.toString();
    }
	
	
	//Taken from StackOverflow and modified to return respective observation
    static char sample(Double[] pdf, Character[] obs) {
        // Transform your probabilities into a cumulative distribution
        double[] cdf = new double[pdf.length];
        cdf[0] = pdf[0];
        for(int i = 1; i < pdf.length; i++)
            cdf[i] += pdf[i] + cdf[i-1];
        // Let ra be a probability [0,1]
        // Search the bin corresponding to that quantile
        Random ra= new Random();
        int k = Arrays.binarySearch(cdf, ra.nextDouble());
        k = k >= 0 ? k : (-k-1);
        return obs[k];
    }
	
	public static String readFileAsString(String fileName) {
		
	    String text = "";
	    try {
	      text = new String(Files.readAllBytes(Paths.get("file.txt")));
	    } 
	    catch (IOException e) {
	      e.printStackTrace();
	    }

	    return text;
	  }
	
	public static void main(String[] args) {

		
		String str="OmegaKirby Heardle #40\r\n"
				+ "\r\n"
				+ "🔊🟩⬜️⬜️⬜️⬜️⬜️";
		MarkovModel mk = new MarkovModel( str, 1);
			
			//mk.rand("WhatW");
			System.out.println(mk.gen(str.substring(0, mk.k) , 25));
	//	}
	}
	
}
