/**
 * @author Narayanan
 * 
 */
package entityMining;

import java.util.ArrayList;

/**
 * @author Narayanan
 *
 */
public class GraphBasedRankingTest {

	/**
	 * Interface function that invokes the necessary functions and returns the graph-ranked comparators to the user  
	 * @param ent1
	 * @param ent2
	 * @return
	 */
	public static ArrayList<String> getGraphRankedComparators(String ent1 , String ent2){
		GraphBasedRanking gbr = new GraphBasedRanking();		
		return gbr.getRankedComparators(ent1,ent2);
	}
	
	/**
	 * Function that receives the input entities from the user and returns the graph-ranked comparators
	 * @param input
	 * @return
	 */
	public static ArrayList<String> graphBasedRanking(ArrayList<String> input){
				
		return getGraphRankedComparators(input.get(0),input.get(1));
	}
	
	/*public static void main(String[] args) throws Exception{
		@SuppressWarnings("serial")
		ArrayList<String> input=new ArrayList<String>(){{
			add("baidu");add("bing");
		}};
		String ent1 = input.get(0);
		String ent2 = input.get(1);		
		System.out.println("**USER OUTPUT**\n"+GraphBasedRankingTest.getRankedComparators(ent1, ent2));		
	}*/
}
