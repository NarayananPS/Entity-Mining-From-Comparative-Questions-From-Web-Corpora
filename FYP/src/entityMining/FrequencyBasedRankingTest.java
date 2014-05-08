/**
 * 
 */
package entityMining;

import java.util.ArrayList;

/**
 * @author Narayanan
 *
 */
public class FrequencyBasedRankingTest {

	/**
	 * @param args
	 */
	
	/**
	 * Interface function that invokes the necessary functions and returns the frequency-ranked comparators to the user  
	 * @param ent1
	 * @param ent2
	 * @return
	 */
	public static ArrayList<String> getFrequencyRankedComparators(String ent1 , String ent2){
		FrequencyBasedRanking fbr = new FrequencyBasedRanking();
		return fbr.getRankedComparators(ent1,ent2);
	}
	
	/**
	 * Function that receives the input entities from the user and returns the frequency-ranked comparators
	 * @param input
	 * @return
	 */
	public static ArrayList<String> frequencyBasedRanking(ArrayList<String> input){
		
		System.out.println("************FREQUENCY BASED RANKING****************");
		//System.out.println("Input:"+input);
		return getFrequencyRankedComparators(input.get(0),input.get(1));
	}
	
	/*public static void main(String[] args) throws Exception{
		ArrayList<String> input=new ArrayList(){{
			add("yahoo");add("duckduckgo");
		}			
		};
		String ent1 = input.get(0);
		String ent2 = input.get(1);
		System.out.println(FrequencyBasedRankingTest.getRankedComparators(ent1, ent2));		
	}*/
}
