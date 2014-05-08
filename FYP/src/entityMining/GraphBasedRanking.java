/**
 * 
 */
package entityMining;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;


/**
 * @author Narayanan
 *
 */

class Vertex {
	
    public final String name;
    public ArrayList<Edge> adjacencies;
    public double weight;
    
    /**
     *  Constructor
     * @param argName
     */
    public Vertex(String argName) { 
    	name = argName;
    	weight=0;
    }
    
    /** 
     * Returns the Vertex as a String 
     */
    public String toString() { 
    	return name; 
    }
}

/**
 *  Class for Edges
 * @author Narayanan
 *
 */
class Edge{
	
    public final Vertex target;
    public final double weight;
    
    /**
     *  Constructor
     * @param argTarget
     * @param argWeight
     */
    public Edge(Vertex argTarget, double argWeight){ 
    	target = argTarget; weight = argWeight; 
    }
    
    /**
     * Returns the edge weight, given the source and destination vertex of an Edge
     * @param a
     * @param b
     * @return double
     */
    double getWeight(Vertex a, Vertex b){
    	if(a==b)
    		return 0.0;
    	ArrayList<Edge> ed = a.adjacencies;
    	//double ret = 0.0;
    	for(Edge e : ed){
    		if(e.target == b){
    			return e.weight;
    		}
    	}
    	return 0.0;
    }
}

public class GraphBasedRanking {
	
	/**
	 *  Prints the Adjacency Table 
	 * created using constructGraph() 	
	 * @param t
	 */
	private void printAdjacencyTable(Table<String,String,Integer> t){
		System.out.println("** ADJACENCY TABLE **");
		Set<String> rows = t.rowKeySet();
		Set<String> columns = t.columnKeySet();
		System.out.print("Adj-Table"+"\t");
		for(String i : columns)
			System.out.print(i+"\t");
		System.out.println("\n----------------------------------------------------------------------------------------------");
		for(String i : rows){
			System.out.print(i+"\t\t");
			for(String j : columns){
				System.out.print(t.get(i, j)+"\t");
			}
			System.out.println();
		}
		System.out.println("\n----------------------------------------------------------------------------------------------\n");
	}

	/**
	 * Prints the Probability Table
	 * created using constructProbabilityTable()
	 * @param t
	 */
	private void printProbabilityTable(ArrayList<String> comparators,Table<String,String,Double> t){
		System.out.println("** PROBABILITY TABLE **");
		Set<String> rows = t.rowKeySet();
		Set<String> columns = t.columnKeySet();
		System.out.print("Prob-Table"+"\t\t");
		for(String i : columns)
			System.out.print(i.toString()+"\t");
		System.out.println("\n----------------------------------------------------------------------------------------------");
		for(String i : rows){
			System.out.print(i+"\t\t");
			for(String j : columns){
				System.out.print(t.get(i, j)+"\t");
			}
			System.out.println();
		}
		System.out.println("\n----------------------------------------------------------------------------------------------\n");
	}
	
	/**
	 * Prints the Score table constructed using calculateScores()
	 * @param scores
	 */
	private static void printScoreTable(ArrayList<String> comparators,Table<String,Integer,Double> scores){
		
		System.out.println("** SCORE TABLE **");
		System.out.println("\n----------------------------------------------------------------------------------------------\n");
		Set<String> rows = scores.rowKeySet();
		for(String v : rows){
			if(comparators.contains(v)){
				System.out.print(v+"=>  ");
				for(int i=0;i<=10;i++)
					System.out.print(scores.get(v, i)+", ");
				System.out.println();
			}
		}
		System.out.println("\n----------------------------------------------------------------------------------------------\n");
	}
	
	/**
	 * Prints the Graph constructed using constructGraph()
	 * @param graph
	 */
	private static void printGraph(ArrayList<String> comparators, ArrayList<Vertex> graph){
		System.out.println("**THE CONSTRUCTED GRAPH**\n");
		for(Vertex v : graph){
			if(comparators.contains(v.toString())){
				System.out.println("Node: "+v.toString());
				System.out.println("Weight: "+v.weight);
				System.out.print("Adjacent Nodes: ");
				ArrayList<Edge> e = v.adjacencies;			
				for(Edge ee : e){
					System.out.print(ee.target.toString()+"-"+ee.weight+",");
				}
				System.out.println();			
				System.out.println();			
			}
		}
		System.out.println("\n----------------------------------------------------------------------------------------------\n");
	}
	
	/**
	 * Helper function that sorts the keys of a HashMap based on their values in non-increasing order 
	 * @param map
	 * @return
	 */
	private <K,V extends Comparable<? super V>>List<Entry<K, V>> entriesSortedByValues(HashMap<K,V> map) {
		
		List<Entry<K,V>> sortedEntries = new ArrayList<Entry<K,V>>(map.entrySet());		
		Collections.sort(sortedEntries, 
		    new Comparator<Entry<K,V>>() {
		        @Override
		        public int compare(Entry<K,V> e1, Entry<K,V> e2) {
		            return e2.getValue().compareTo(e1.getValue());
		        }
		    }
		);
		
		return sortedEntries;
	}

	/**
	 * Constructs the Adjacency Table to be used in the construction of the Graph
	 * It specifies the no. of times two entities are compared in the repository 
	 * @param table
	 * @param comparators
	 * @param input
	 * @return
	 */
	public Table<String,String,Integer> constructAdjacencyTable(Table<String,String,Integer> table, ArrayList<String> comparators, ArrayList<String> input){
		
		for(String i : comparators){
			for(String j : input){
				table.put(i, j, 0);
			}
		}
		
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader("Comparators\\comparator.txt"));
			String text="";
			String words[] = new String[10];
			for(text=br.readLine();text!=null&&!text.equals(" ");text=br.readLine()){
				words=text.split(",");
				if(input.contains(words[0].toLowerCase())&&comparators.contains(words[1].toLowerCase())){
					if(table.contains(words[1].toLowerCase(), words[0].toLowerCase())){
						int v= table.get(words[1].toLowerCase(), words[0].toLowerCase());
						table.put(words[1].toLowerCase(), words[0].toLowerCase(), v+1);
					}
				}
				else if(input.contains(words[1].toLowerCase())&&comparators.contains(words[0].toLowerCase())){
					if(table.contains(words[0].toLowerCase(), words[1].toLowerCase())){
						int v= table.get(words[0].toLowerCase(), words[1].toLowerCase());
						table.put(words[0].toLowerCase(), words[1].toLowerCase(), v+1);
					}
				}
			}
			//printAdjacencyTable(table);		
		}		
		catch(Exception e){
			
		}		
		return table;
	}
	
	/**
	 * Constructs an auxilliary table to be used in the construction of graph
	 * @param tableCopy
	 * @param listV
	 * @param pairs
	 * @param graph
	 * @return
	 */
	public Table<String,String,Integer> constructTable(Table<String,String,Integer> tableCopy,ArrayList<Vertex> listV,ArrayList<String> pairs,ArrayList<Vertex> graph){
		
			
		for(Vertex i : listV){
			for(Vertex j : listV){
				tableCopy.put(i.toString(), j.toString(), 0);
			}
		}
		
		//printAdjacencyTable(tableCopy);
		
		for(String text : pairs){
			String words[]=text.split(",");
			//System.out.println(words[0]+" "+words[1]);
			if(tableCopy.contains(words[0].toLowerCase(), words[1].toLowerCase())){
				int val = tableCopy.get(words[0].toLowerCase(), words[1].toLowerCase());//System.out.print("v:"+val+" ");
				tableCopy.put(words[0].toLowerCase(), words[1].toLowerCase(), val+1);
				val = tableCopy.get(words[1].toLowerCase(), words[0].toLowerCase());
				tableCopy.put(words[1].toLowerCase(), words[0].toLowerCase(), val+1);
			}
		}		
		
		return tableCopy;
	}
	
	/**
	 * Constructs the Graph
	 * @param vertexWeight
	 * @param tableCopy
	 * @param allV
	 * @param graph
	 * @return
	 */
	public ArrayList<Vertex> constructGraph(HashMap<Vertex,Integer> vertexWeight, Table<String,String,Integer> tableCopy , ArrayList<Vertex> allV, ArrayList<Vertex> graph){
	
		for(Vertex V : allV){
			vertexWeight.put(V, 0);
		}
				
		//System.out.println("List: "+list);
		//sSystem.out.println("Vertex Weights:");
		Set<String> columns = tableCopy.columnKeySet();
		for(Entry<Vertex,Integer> entry : vertexWeight.entrySet()){
			Vertex v = entry.getKey();
			int val = 0;
			
			for(String s : columns){
				val+= tableCopy.get(v.toString().toLowerCase(), s);
								
			}
			vertexWeight.put(v, val);
			//System.out.println(entry.getKey()+"=> "+entry.getValue());
		}
		/*for(Entry<Vertex,Integer> entry : vertexWeight.entrySet()){
			System.out.println(entry.getKey()+" => "+entry.getValue());
		}*/
		
		Set<String> rows = tableCopy.rowKeySet();
		Set<String> cols = tableCopy.columnKeySet();
		
		for(String s : rows){
			Vertex v = new Vertex(s);
			ArrayList<Edge> temp = new ArrayList<Edge>();
			double ans = 0;
			for(String t : cols){
				double val=(double)tableCopy.get(s, t);
				ans+=val;
				if(val>0){
					temp.add(new Edge(new Vertex(t),val));
				}				
			}
			v.adjacencies=temp;
			v.weight = ans;
			graph.add(v);
		}		
		
		return graph;
	}
	
	/**
	 * Constructs the Probability Table 
	 * @param probability
	 * @param tableCopy
	 * @param graph
	 * @return
	 */
	public Table<String,String,Double> constructProbabilityTable(Table<String,String,Double> probability , Table<String,String,Integer> tableCopy , ArrayList<Vertex> graph){
		
		for(Vertex i : graph){
			for(Vertex j : graph){
				double ans = tableCopy.get(i.toString(), j.toString());
				double val = (ans/j.weight);
				probability.put(i.toString(), j.toString(), val);
			}
		}		
		
		return probability;
	}
	
	/**
	 * Function to calculate the Final Scores 
	 * @param scores
	 * @param probability
	 * @param tableCopy
	 * @param graph
	 * @param comparatorsV
	 * @param input
	 * @return
	 */
	public Table<String,Integer,Double> calculateScores(Table<String,Integer,Double> scores, Table<String,String,Double> probability,Table<String,String,Integer> tableCopy,ArrayList<Vertex> graph, ArrayList<Vertex> comparatorsV,ArrayList<String> input){
		
		//System.out.println("Here:"+tableCopy.get("baidu", "baidu"));
		ArrayList<Vertex> entities = new ArrayList<Vertex>(); 
		for(Vertex v : graph){
			if(input.contains(v.toString())){
				entities.add(v);
			}
		}
		for(Vertex v : graph){
			//System.out.println("Ver: "+v.toString()+"   "+entities.get(0));
			int val = tableCopy.get(v.toString(), entities.get(0).toString());
			double ans=0.0;
			ans=(double)val;
			ans=ans/entities.get(0).weight;
			//scores.put(v,  0 , ans);
			//System.out.println("Ver: "+v.toString().toLowerCase()+"   "+entities.get(1));
			val = tableCopy.get(v.toString().toLowerCase(), entities.get(1).toString());			
			//ans=0.0;
			ans=ans+(double)val;
			ans=ans*v.adjacencies.size();
			ans=ans/entities.get(1).weight;
			ans=ans*v.adjacencies.size();
			scores.put(v.toString(),  0 , ans); 
			for(int i=1;i<=10;i++)
				scores.put(v.toString(), i, 0.0);			
		}
		/*System.out.println("Scores Table:");
		
		for(Vertex v : graph){
			System.out.println(v.toString()+" -> "+scores.get(v.toString(), 0));
		}*/
		
		double LAMBDA = 0.8;
		
		for(int i=1;i<=10;i++){
			for(Vertex v : graph){
				double ans = scores.get(v.toString(), 0) * LAMBDA;
				//System.out.println("Vertex : "+v);
				ArrayList<Edge> adj = v.adjacencies;
				for(Edge e : adj){
					//System.out.print("Target :"+e.target+"\t");
					//System.out.print(probability.get(v.toString(), e.target.toString())+" ");
					//System.out.println(scores.get(e.target.toString(), i-1));
					ans= ans + ((1-LAMBDA) * (probability.get(v.toString(), e.target.toString()) * scores.get(e.target.toString(), i-1)));
				}
				scores.put(v.toString(), i, ans);
				//System.out.println();
			}
		}		
		
		return scores;
	}
	
	/**
	 * The utility function that returns the ranked comparators 
	 * @param ent1
	 * @param ent2
	 * @return
	 */
	public ArrayList<String> getRankedComparators(String ent1,String ent2){
		
		BufferedReader br = null;
		System.out.println("Entities: "+ent1+"  "+ent2);
		Table<String,String,Integer> table = HashBasedTable.create();
		Table<String,String,Integer> tableCopy = HashBasedTable.create();
		ArrayList<String> input = new ArrayList<String>();
		ArrayList<Vertex> inputV = new ArrayList<Vertex>();
		ArrayList<String> comparators = new ArrayList<String>();
		ArrayList<Vertex> comparatorsV = new ArrayList<Vertex>();
		ArrayList<String> all = new ArrayList<String>();
		ArrayList<Vertex> allV = new ArrayList<Vertex>();
		ArrayList<String> results = new ArrayList<String>();
		HashMap<Vertex,Integer> vertexWeight = new HashMap<Vertex,Integer>(); 
		ArrayList<String> pairs = new ArrayList<String>();
		ArrayList<Vertex> listV = new ArrayList<Vertex>();
		ArrayList<String> list = new ArrayList<String>();
		Table<String,Integer,Double> scores = HashBasedTable.create();
				
		ArrayList<Vertex> graph = new ArrayList<Vertex>();	
		Table<String,String,Double> probability = HashBasedTable.create();
				
		String acr1=null;
		String acr2=null;
		
		int ITER = 10;
		try{
			if(ent1.length()<=5){
				List<String> acr = Acro.getacronyms(ent1.toUpperCase());
				if(acr.size()>0)
					acr1=acr.get(0);
				System.out.println("Acr1 : "+acr1);
			}
			if(ent2.length()<=5){
				List<String> acr = Acro.getacronyms(ent2.toUpperCase());
				if(acr.size()>0)
					acr2=acr.get(0);
				System.out.println("Acr2 : "+acr2);
			}
		}
		catch(Exception e){
			
		}
		input.add(ent1);
		input.add(ent2);
		
		inputV.add(new Vertex(ent1));
		inputV.add(new Vertex(ent2));
		
		/** 
		 * ADJACENCY TABLE CONSTRUCTION
		 */
		
		try{
			br = new BufferedReader(new FileReader("Comparators\\comparator.txt"));
			String text="";
			String words[] = new String[10];
			for(text=br.readLine();text!=null&&!text.equals(" ");text=br.readLine()){				
				pairs.add(text);
				words=text.split(",");				
				if(!list.contains(words[0].toLowerCase())){
					list.add(words[0].toLowerCase());
					listV.add(new Vertex(words[0].toLowerCase()));
				}
				if(!list.contains(words[1].toLowerCase())){
					list.add(words[1].toLowerCase());
					listV.add(new Vertex(words[1].toLowerCase()));
				}
				if(StringUtils.containsIgnoreCase(words[0], ent1)||StringUtils.containsIgnoreCase(ent1, words[0])||StringUtils.containsIgnoreCase(words[0], ent2)||StringUtils.containsIgnoreCase(ent2, words[0])){
					if(!words[1].toLowerCase().equals(ent1)&&!words[1].toLowerCase().equals(ent2)&&!comparators.contains(words[1].toLowerCase())){
						comparators.add(words[1].toLowerCase());
						comparatorsV.add(new Vertex(words[1].toLowerCase()));
					}
				}
				if(StringUtils.containsIgnoreCase(words[1], ent1)||StringUtils.containsIgnoreCase(ent1, words[1])||StringUtils.containsIgnoreCase(words[1], ent2)||StringUtils.containsIgnoreCase(ent2, words[1])){
					if(!words[0].toLowerCase().equals(ent1)&&!words[0].toLowerCase().equals(ent2)&&!comparators.contains(words[0].toLowerCase())){
						comparators.add(words[0].toLowerCase());
						comparatorsV.add(new Vertex(words[0].toLowerCase()));
					}
				}
				if(acr1!=null&&StringUtils.containsIgnoreCase(words[0].toLowerCase(), acr1.toLowerCase())||(acr1!=null&&StringUtils.containsIgnoreCase(acr1.toLowerCase(), words[0].toLowerCase()))){
					if(acr2!=null&&!StringUtils.containsIgnoreCase(words[1],acr2.toLowerCase())){
						if(!comparators.contains(words[1].toLowerCase())){
							comparators.add(words[1].toLowerCase());
						}
					}
					else if(acr2==null){
						if(!comparators.contains(words[1].toLowerCase())){
							comparators.add(words[1].toLowerCase());
						}
					}
				}		
				//System.out.println(words[0].toLowerCase()+" "+words[1].toLowerCase());
				if(acr1!=null&&StringUtils.containsIgnoreCase(words[1].toLowerCase(), acr1.toLowerCase())||(acr1!=null&&StringUtils.containsIgnoreCase(acr1.toLowerCase(), words[1].toLowerCase()))){
					if(acr2!=null&&!StringUtils.containsIgnoreCase(words[0],acr2.toLowerCase())){
						if(!comparators.contains(words[0].toLowerCase())){
							comparators.add(words[0].toLowerCase());
						}
					}
					else if(acr2==null){
						if(!comparators.contains(words[0].toLowerCase())){
							comparators.add(words[0].toLowerCase());
						}
					}
				}
				if(acr2!=null&&StringUtils.containsIgnoreCase(words[1].toLowerCase(), acr2.toLowerCase())||(acr2!=null&&StringUtils.containsIgnoreCase(acr2.toLowerCase(), words[1].toLowerCase()))){
					//System.out.println(acr2.toLowerCase()+" ! "+words[1].toLowerCase());
					if(acr1!=null&&!StringUtils.containsIgnoreCase(words[0],acr1.toLowerCase())){
						if(!comparators.contains(words[0].toLowerCase())){
							comparators.add(words[0].toLowerCase());
						}
					}
					else if(acr1==null){
						if(!comparators.contains(words[0].toLowerCase())){
							comparators.add(words[0].toLowerCase());
						}
					}
				}
				if(acr2!=null&&StringUtils.containsIgnoreCase(words[0].toLowerCase(), acr2.toLowerCase())||(acr2!=null&&StringUtils.containsIgnoreCase(acr2.toLowerCase(), words[0].toLowerCase()))){
					//System.out.println(acr2.toLowerCase()+" ! "+words[1].toLowerCase());
					if(acr1!=null&&!StringUtils.containsIgnoreCase(words[1],acr1.toLowerCase())){
						if(!comparators.contains(words[1].toLowerCase())){
							comparators.add(words[1].toLowerCase());
						}
					}
					else if(acr2==null){
						if(!comparators.contains(words[1].toLowerCase())){
							comparators.add(words[1].toLowerCase());
						}
					}
				}
			}
			br.close();			
		}
		catch(Exception e){
			
		}
		if(comparators.isEmpty()){
			System.out.println("No entries found!");
			return null;
		}
		else{
			
			
			System.out.println("Comparators: "+comparators);
			
			all.addAll(input);
			all.addAll(comparators);
			allV.addAll(inputV);
			allV.addAll(comparatorsV);
			
			//System.out.println("All: "+all);
			
			table=constructAdjacencyTable(table,comparators,input);
			
			printAdjacencyTable(table);
			tableCopy=constructTable(tableCopy,listV,pairs,graph);
			/*System.out.println("Table Copy:");
			printAdjacencyTable(tableCopy);*/
					
			/**
			 *  GRAPH CONSTRUCTION
			 */
			
			graph = constructGraph(vertexWeight,tableCopy,allV,graph);
			
			printGraph(comparators,graph);
			
			/**
			 * PROBABILITY TABLE 
			 */
			
			probability = constructProbabilityTable(probability,tableCopy,graph);
				
			//printProbabilityTable(comparators,probability);
			
			/**
			 * SCORE CALCULATION
			 */
			scores = calculateScores(scores,probability,tableCopy,graph,comparatorsV,input);
			printScoreTable(comparators,scores);
			
			HashMap<String,Double> finalScores = new HashMap<String,Double>();
			for(Vertex v : graph){
				if(comparators.contains(v.toString()))
					finalScores.put(v.toString(), scores.get(v.toString(), ITER));
			}
			System.out.println("**FINAL SCORES AFTER CONVERGENCE**:");
			for(Entry<String,Double> entry : finalScores.entrySet()){
				if(comparators.contains(entry.getKey()))
					System.out.print(entry.getKey()+"=>"+entry.getValue()+" , ");
			}		
			System.out.println("\n----------------------------------------------------------------------------------------------");
			List<Map.Entry<String,Double>> FinalList = entriesSortedByValues(finalScores);
			System.out.println("**RESULTS**");
			for(Map.Entry<String,Double> entry : FinalList){
				String key = entry.getKey();
				if(comparators.contains(key)){
					System.out.print(entry.getKey()+"=>"+entry.getValue()+" , ");
					if(input.contains(key));
					else if(comparators.contains(key)){
						results.add(key);
					}
				}
			}		
			System.out.println("\n----------------------------------------------------------------------------------------------");
			System.out.println("**ALTERNATIVES**");
			System.out.println(results);
			System.out.println("\n----------------------------------------------------------------------------------------------");
			return results;
		}
	}
}
