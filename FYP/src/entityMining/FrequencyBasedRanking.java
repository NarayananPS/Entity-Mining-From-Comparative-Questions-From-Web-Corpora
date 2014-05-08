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
public class FrequencyBasedRanking {
	
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

	public ArrayList<String> getRankedComparators(String ent1,String ent2){
		
		BufferedReader br = null;
		String acr1=null;
		String acr2=null;
		System.out.println("Entities: "+ent1+"  "+ent2);
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
		Table<String,String,Integer> table = HashBasedTable.create();
		ArrayList<String> input = new ArrayList<String>();
		ArrayList<String> comparators = new ArrayList<String>();
		ArrayList<String> all = new ArrayList<String>();
		ArrayList<String> results = new ArrayList<String>();
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		
		input.add(ent1);
		input.add(ent2);
		
		try{
			br = new BufferedReader(new FileReader("Comparators\\comparator.txt"));
			String text="";
			String words[] = new String[10];
			for(text=br.readLine();text!=null&&!text.equals(" ");text=br.readLine()){
				words=text.split(",");
				//System.out.println(words[0]+" "+words[1]);
				if(StringUtils.containsIgnoreCase(words[0], ent1)||StringUtils.containsIgnoreCase(ent1, words[0])||StringUtils.containsIgnoreCase(words[0], ent2)||StringUtils.containsIgnoreCase(ent2, words[0])){
					if(!words[1].toLowerCase().equals(ent1)&&!words[1].toLowerCase().equals(ent2)&&!comparators.contains(words[1].toLowerCase())){
					//if(!StringUtils.containsIgnoreCase(words[1].toLowerCase(),ent1)&&!!StringUtils.containsIgnoreCase(words[1].toLowerCase(),ent2)&&!comparators.contains(words[1].toLowerCase())){
						comparators.add(words[1].toLowerCase());
					}
				}
				if(StringUtils.containsIgnoreCase(words[1], ent1)||StringUtils.containsIgnoreCase(ent1, words[1])||StringUtils.containsIgnoreCase(words[1], ent2)||StringUtils.containsIgnoreCase(ent2, words[1])){
					if(!words[0].toLowerCase().equals(ent1)&&!words[0].toLowerCase().equals(ent2)&&!comparators.contains(words[0].toLowerCase())){
					//if(!StringUtils.containsIgnoreCase(words[0].toLowerCase(),ent1)&&!!StringUtils.containsIgnoreCase(words[0].toLowerCase(),ent2)&&!comparators.contains(words[0].toLowerCase())){
						comparators.add(words[1].toLowerCase());
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
			//System.out.println("All: "+all);
			
			for(String i : comparators){
				for(String j : input){
					table.put(i, j, 0);
				}
			}
			
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
				printAdjacencyTable(table);
				
				Set<String> rows = table.rowKeySet();
				Set<String> columns = table.columnKeySet();
				
				for(String r : rows){
					map.put(r, 0);
					for(String c : columns){
							map.put(r, (map.get(r) + table.get(r, c)) );
					}
				}
				
				List<Map.Entry<String,Integer>> ret = entriesSortedByValues(map);
				System.out.println("**FINAL SCORES ***\n------------------------------------------------------------------------");
				System.out.println(ret);
				for(Map.Entry<String,Integer> entry : ret){
					results.add(entry.getKey());
				}
				System.out.println("-------------------------------------------------------------------------------\n\n**RESULTS**");
				System.out.println("--------------------------------------------------------------------------------------------");
				//System.out.println(results+"\n");
				
			}
			catch(Exception e){
				
			}
			return results;
		}
	}
}

