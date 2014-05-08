/**
 * @author Hariprasaad
 *
 */

package entityMining;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.util.Sets;

public class MainModule {
	
	static List<Sequence> loadDB(String DatasetPath) throws Exception
	{
		List<Sequence> seq= new ArrayList<Sequence>();
		BufferedReader in=new BufferedReader(new FileReader(DatasetPath));
		String current=null;
		System.out.println("Loading Dataset into Main Memory...");
		long startTime = System.nanoTime();
		while((current=in.readLine())!=null)
		{
			seq.add(new Sequence(current));
		}
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		System.out.println("Successfully Done in "+ (duration/1000000000) +"secs...!!!");
		return seq;
	}
	
	public static void Analysis(String DatasetPath, String LabelPath) throws Exception
	{
		System.out.println("HEURISTICS METHOD:");
		System.out.println();
		System.out.println("COMPARATIVE QUESTIONS:");
		System.out.println();
		List<Sequence> seq= new ArrayList<Sequence>();
		BufferedReader in=new BufferedReader(new FileReader(DatasetPath));
		BufferedReader in1=new BufferedReader(new FileReader(LabelPath));
		String current=null;
		String label=null;
		float relevant_P=0,irrelevant=0;
		float relevant_N=0;
		int i=0;//
		int Labelcomp=0,comp=0;
		int rFlag=0;
		while((current=in.readLine())!=null)
		{
				label=in1.readLine();
				if(label.equals("C") || label.equals("U"))
				{
					rFlag=1;
					Labelcomp++;
				}
				seq.add(new Sequence(current));
				if(seq.get(i).comparators[0]!=null)
				{
					if(rFlag==1)
					{
						relevant_P++;
					}
					else
					{
						irrelevant++;
					}
					System.out.println(comp+1);
					System.out.println(seq.get(i).words);
					System.out.println(seq.get(i).tags);
					//System.out.println(seq.get(i).comparators[0]+" + "+seq.get(i).comparators[1]);
					comp++;
				}
				else
				{
					if(rFlag==1)
					{
						relevant_N++;
						//System.out.println(seq.get(i).words);
						//System.out.println(seq.get(i).tags);
					}
				}
				rFlag=0;
				i++;
		}
		in.close();
		in1.close();
		float precision=((relevant_P/(relevant_P+irrelevant))*100);
		float recall=((relevant_P/(relevant_P+relevant_N))*100);
		float fscore=((2*precision*recall)/(precision+recall));
		
		System.out.println();
		System.out.println("OBJECTIVE:");
		System.out.println("Comparative Questions to be Retrieved:"+Labelcomp);
		System.out.println();
		System.out.println("STATS:");
		System.out.println("Total No. of Questions:"+i);
		System.out.println("Total Questions Identified as Comparative:"+comp);
		System.out.println("Irrelevant Questions identified as Comparative:"+(int)irrelevant);
		System.out.println("Comparative Questions identified Correctly:"+(int)relevant_P);
		System.out.println("Comparative Questions not Identified:"+(int)relevant_N);
		System.out.println();
		System.out.println("PARAMETERS:");
		System.out.println("Precision:\t"+precision + "%");
		System.out.println("Recall:\t\t"+recall + "%");
		System.out.println("F-Score:\t"+fscore + "%");
		System.out.println("DONE...!!!");
		System.out.println("Precision:\t"+recall + "%");
	}
	
	public static void FoldRun(int fold, int runFold, String DatasetPath, String LabelPath) throws Exception
	{
		List<String> questions=new ArrayList<String>();
		List<String> labels=new ArrayList<String>();
		BufferedReader in=new BufferedReader(new FileReader(DatasetPath));
		BufferedReader in1=new BufferedReader(new FileReader(LabelPath));
		String current=null;
		String label=null;
		int count=0;
		while((current=in.readLine())!=null)
		{
				label=in1.readLine();
				questions.add(current);
				labels.add(label);
				count++;
		}
		float relevant_P=0,irrelevant=0;
		float relevant_N=0;
		int Labelcomp=0,comp=0;
		int rFlag=0;
		for(int i=(runFold-1)*(count/fold);i<(runFold)*(count/fold);i++)
		{
				current=questions.get(i);
				label=labels.get(i);
				if(label.equals("C"))
				{
					rFlag=1;
					Labelcomp++;
				}
				//seq.add(new Sequence(current));
				Sequence seq=new Sequence(current);
				if(seq.comparators[0]!=null)
				{
					if(rFlag==1)
					{
						relevant_P++;
					}
					else
					{
						irrelevant++;
					}
					System.out.println(seq.words);
					System.out.println(seq.tags);
					System.out.println(seq.comparators[0]+" + "+seq.comparators[1]);
					comp++;
				}
				else
				{
					if(rFlag==1)
					{
						relevant_N++;
						//System.out.println(seq.words);
						//System.out.println(seq.tags);
					}
				}
				rFlag=0;
				i++;
		}
		in.close();
		in1.close();
		float precision=((relevant_P/(relevant_P+irrelevant))*100);
		float recall=((relevant_P/(relevant_P+relevant_N))*100);
		float fscore=((2*precision*recall)/(precision+recall));
		
		System.out.println("FOLD:"+runFold);
		System.out.println("OBJECTIVE:");
		System.out.println("Comparative Questions to be Retrieved:"+Labelcomp);
		System.out.println();
		System.out.println("STATS:");
		System.out.println("Total No. of Questions:"+count/fold);
		System.out.println("Total Questions Identified as Comparative:"+comp);
		System.out.println("Irrelevant Questions identified as Comparative:"+(int)irrelevant);
		System.out.println("Comparative Questions identified Correctly:"+(int)relevant_P);
		System.out.println("Comparative Questions not Identified:"+(int)relevant_N);
		//System.out.println("TRUE +ve"+(int)relevant_P);
		//System.out.println("TRUE -ve"+(int)relevant_N);
		//System.out.println("FALSE +ve"+(int)relevant_N);
		System.out.println();
		System.out.println("PARAMETERS:");
		System.out.println("Precision:\t"+precision + "%");
		System.out.println("Recall:\t\t"+recall + "%");
		System.out.println("F-Score:\t"+fscore + "%");
		System.out.println("DONE...!!!");
		System.out.println("Precision:\t"+recall + "%");
		System.out.println();
	}
	
	public static int contains(List<Pair<String,String>> list,Pair<String,String> object)
	{
		int n=list.size();
		for(int i=0;i<n;i++)
		{
			if(object.equals(list.get(i)))
				return i;	
		}
		return -1;
	}
	
	public static void IEPMining() throws Exception
	{
		List<Sequence> bigdata=loadDB("Dataset/Set-A.txt");
		List<Sequence> patterns=new ArrayList<Sequence>();
		patterns.add(new Sequence("$c vs $c"));
		int patternN=0;
		List<Pair<String,String>> comparators= new ArrayList<Pair<String,String>>();
		int n=bigdata.size();
		//STEP 1: Extraction of Initial Seed Comparator Pairs
		for(int i=0;i<n;i++)
		{
			Pair<String,String> temp=bigdata.get(i).compare(patterns.get(patternN));
			if(temp.x!=null)
			{
				int index=contains(comparators,temp);
				if(index!=-1)
				{
					temp.occurrence=comparators.get(index).occurrence+1;
					comparators.set(index, temp);
				}
				else
				{
					comparators.add(temp);
				}
				bigdata.remove(i);
				i--;
				n=bigdata.size();
			}
		}
		patternN=1;
		
		System.out.println("DONE:" + comparators.size());
		/*
		 * STEP 2: For Each Comparator Pair, Find all the questions containing that pair
		 * 			and replace the pair with the comparator tag "$c"
		 */
		BufferedWriter out=new BufferedWriter(new FileWriter("Patterns/DEMO_Patterns.txt", true));
		for(int i=0;i<comparators.size();i++)
		{
			System.out.println("DONE:" + comparators.size());
			Pair<String,String> current=comparators.get(i);
			for(int j=0;j<bigdata.size();j++)
			{
				Sequence temp=bigdata.get(j).checkComparativeQuestion(current);
				if(temp!=null)
				{
					current.occurrence++;
					comparators.set(i, current);
					patterns.add(temp);
					out.write(temp.stringToSave()+"\n");
					bigdata.remove(j);
					j--;
				}
			}
			for(int j=patternN;j<patterns.size();j++)
			{
				for(int k=0;k<bigdata.size();k++)
				{
					/*
					System.out.println(patterns.get(j).words);
					System.out.println(patterns.get(j).tags);
					System.out.println(bigdata.get(k).words);
					System.out.println(bigdata.get(k).tags);
					System.out.println();
					*/
					Pair<String,String> temp=bigdata.get(k).compare(patterns.get(j));
					if(temp.x!=null)
					{
						int index=contains(comparators,temp);
						if(index!=-1)
						{
							temp.occurrence=comparators.get(index).occurrence+1;
							comparators.set(index, temp);
						}
						else
						{
							comparators.add(temp);
						}
						bigdata.remove(k);
						k--;
					}
				}
			}
			patternN=patterns.size();
		}
		
		for(int i=0;i<comparators.size();i++)
			System.out.println(comparators.get(i).toString());
		System.out.println();
		for(int i=0;i<bigdata.size();i++)
			System.out.println(bigdata.get(i).words);
		System.out.println();
		
		//BufferedWriter out=new BufferedWriter(new FileWriter("Patterns/patterns.txt", true));
		for(int i=0;i<patterns.size();i++)
		{
			//out.write(patterns.get(i).stringToSave()+"\n");
			System.out.println(patterns.get(i).stringToSave());
			//System.out.println(patterns.get(i).tags);
		}
		out.close();
		
		System.out.println("Patterns Successfully Extracted...!!!");
	}

	public static void setA() throws Exception
	{
		System.out.println("PATTERN METHOD:");
		System.out.println();
		System.out.println("COMPARATIVE QUESTIONS:");
		System.out.println();
		List<Sequence> bigdata=loadDB("Dataset/Set-A.txt");
		List<Sequence> patterns=loadDB("Patterns/patterns.txt");
		BufferedReader in1=new BufferedReader(new FileReader("Dataset/Labels-A.txt"));
		List<Pair<String,String>> comparators= new ArrayList<Pair<String,String>>();
		String label="";
		int rFlag=0,Labelcomp=0,loop=0;
		double truePositive=0;
		double trueNegative=0;
		double falsePositive=0;
		double falseNegative=0;
		int comp=1;
		for(int i=0;i<bigdata.size();i++)
		{
			Sequence currentQ=bigdata.get(i);
			label=in1.readLine();
			if(label.equals("C") || label.equals("U"))
			{
				rFlag=1;
				Labelcomp++;
			}
			for(int j=0;j<patterns.size();j++)
			{
				Sequence currentPattern=patterns.get(j);
				Pair<String,String> temp=currentQ.compare(currentPattern);
				if(temp.x!=null)
				{
					if(rFlag==1)
						truePositive++;
					else if(rFlag==0)
						falseNegative++;
					comparators.add(temp);
					//System.out.println(temp.toString());
					
					System.out.println(comp++);
					System.out.println(currentPattern.words);
					System.out.println(currentPattern.tags);
					loop=1;
					break;
				}
			}
			if(loop!=1)
			{
				if(rFlag==1)
					trueNegative++;
				else
					falsePositive++;
			}
			rFlag=0;
			loop=0;
		}
		double precision=(truePositive/(truePositive+falseNegative));
		double recall=(truePositive/(truePositive+trueNegative));
		double fscore=((2*precision*recall)/(precision+recall));
		System.out.println();
		System.out.println("OBJECTIVE:");
		System.out.println("Comparative Questions to be Retrieved:"+Labelcomp);
		System.out.println();
		System.out.println("STATS:");
		System.out.println("Total No. of Questions:"+bigdata.size());
		System.out.println("Total Questions Identified as Comparative:"+(int)(truePositive+falseNegative));
		System.out.println("Irrelevant Questions identified as Comparative:"+(int)falseNegative);
		System.out.println("Comparative Questions identified Correctly:"+(int)truePositive);
		System.out.println("Comparative Questions not Identified:"+(int)trueNegative);
		System.out.println();
		System.out.println("PARAMETERS:");
		System.out.println("Precision:\t"+precision*100 + "%");
		System.out.println("Recall:\t\t"+recall*100 + "%");
		System.out.println("F-Score:\t"+fscore*100 + "%");
		System.out.println("DONE...!!!");
	}
	
	public static void HybridizedModel() throws Exception
	{
		System.out.println("HYBRIDIZED MODEL:");
		System.out.println();
		System.out.println("COMPARATIVE QUESTIONS:");
		System.out.println();
		List<Sequence> bigdata=loadDB("Dataset/Set-A.txt");
		List<Sequence> patterns=loadDB("Patterns/patterns.txt");
		BufferedReader in1=new BufferedReader(new FileReader("Dataset/Labels-A.txt"));
		List<Pair<String,String>> comparators= new ArrayList<Pair<String,String>>();
		String label="";
		int rFlag=0,Labelcomp=0,loop=0;
		double truePositive=0;
		double trueNegative=0;
		double falsePositive=0;
		double falseNegative=0;
		int comp=1;
		for(int i=0;i<bigdata.size();i++)
		{
			Sequence currentQ=bigdata.get(i);
			label=in1.readLine();
			if(label.equals("C") || label.equals("U"))
			{
				rFlag=1;
				Labelcomp++;
			}
			for(int j=0;j<patterns.size();j++)
			{
				Sequence currentPattern=patterns.get(j);
				Pair<String,String> temp=currentQ.compare(currentPattern);
				if((currentQ.cq_pattern==true && currentQ.cq_heuristics==true))// || currentQ.cq_heuristics==true)
				{
					if(rFlag==1)
						truePositive++;
					else if(rFlag==0)
						falseNegative++;
					comparators.add(temp);
					//System.out.println(temp.toString());
					
					System.out.println(comp++);
					System.out.println(currentPattern.words);
					System.out.println(currentPattern.tags);
					loop=1;
					break;
				}
			}
			if(loop!=1)
			{
				if(rFlag==1)
					trueNegative++;
				else
					falsePositive++;
			}
			rFlag=0;
			loop=0;
		}
		double precision=(truePositive/(truePositive+falseNegative));
		double recall=(truePositive/(truePositive+trueNegative));
		double fscore=((2*precision*recall)/(precision+recall));
		System.out.println();
		System.out.println("OBJECTIVE:");
		System.out.println("Comparative Questions to be Retrieved:"+Labelcomp);
		System.out.println();
		System.out.println("STATS:");
		System.out.println("Total No. of Questions:"+bigdata.size());
		System.out.println("Total Questions Identified as Comparative:"+(int)(truePositive+falseNegative));
		System.out.println("Irrelevant Questions identified as Comparative:"+(int)falseNegative);
		System.out.println("Comparative Questions identified Correctly:"+(int)truePositive);
		System.out.println("Comparative Questions not Identified:"+(int)trueNegative);
		System.out.println();
		System.out.println("PARAMETERS:");
		System.out.println("Precision:\t"+precision*100 + "%");
		System.out.println("Recall:\t\t"+recall*100 + "%");
		System.out.println("F-Score:\t"+fscore*100 + "%");
		System.out.println("DONE...!!!");
	}
	
	public static void extractComparators() throws Exception
	{
		BufferedWriter out1=new BufferedWriter(new FileWriter("Comparators/comparator1.txt", true));
		BufferedWriter out2=new BufferedWriter(new FileWriter("Comparators/comparator2.txt", true));
		
		List<Sequence> bigdata=loadDB("Dataset/Bigdata_Demo.txt");
		List<Sequence> patterns=loadDB("Patterns/patterns.txt");
		List<Pair<String,String>> comparators= new ArrayList<Pair<String,String>>();
		for(int i=0;i<bigdata.size();i++)
		{
			Sequence currentQ=bigdata.get(i);
			for(int j=0;j<patterns.size();j++)
			{
				Sequence currentPattern=patterns.get(j);
				Pair<String,String> temp=currentQ.compare(currentPattern);
				if((currentQ.cq_pattern==true && currentQ.cq_heuristics==true))// || currentQ.cq_heuristics==true)
				{
					out1.write(currentQ.comparators[0]+","+currentQ.comparators[1]+"\n");
					out2.write(temp.x+","+temp.y+"\n");
					comparators.add(temp);
					break;
				}
			}
		}
		
		out1.close();
		out2.close();
		System.out.println("DONE...!!!");
	}
	
	public static ArrayList<String> graphBasedComparatorRanking(ArrayList<String> input)throws Exception{
		
		System.out.println("---------------------------------------------------------------------------");
		System.out.println("**ALTERNATIVES :- GRAPH-BASED RANKING**");
		System.out.println("---------------------------------------------------------------------------");
		return GraphBasedRankingTest.graphBasedRanking(input);		
	}
	
	public static ArrayList<String> getFrequencyBasedRanking(ArrayList<String> input) throws Exception{
		
		System.out.println("**ALTERNATIVES :- FREQUENCY-BASED RANKING**");
		System.out.println("---------------------------------------------------------------------------");
		return FrequencyBasedRankingTest.frequencyBasedRanking(input);		
	}
	
	public static void userInputHelper(List<Sequence> patterns, BufferedReader br) throws Exception
	{
		System.out.println("QUERY:");
		String ip=br.readLine();
		Sequence s=new Sequence(ip);
		ArrayList<String> input=new ArrayList<String>();
		BufferedWriter out1=new BufferedWriter(new FileWriter("Comparators/comparator.txt", true));
		//BufferedWriter out2=new BufferedWriter(new FileWriter("Comparators/comparator2.txt", true));
		int flag=0;
		for(int j=0;j<patterns.size();j++)
		{
			Sequence currentPattern=patterns.get(j);
			Pair<String,String> temp=s.compare(currentPattern);
			if((s.cq_pattern==true && s.cq_heuristics==true) || s.cq_heuristics==true)// || currentQ.cq_heuristics==true)
			{
				input.add(s.comparators[0]);
				input.add(s.comparators[1]);
				System.out.println("USER ENTITIES: "+s.comparators[0]+","+s.comparators[1]+"\n");
				out1.write(s.comparators[0]+","+s.comparators[1]+"\n");
				out1.close();
				ArrayList<String> result1 = getFrequencyBasedRanking(input);
				if(result1!=null)
					System.out.println(result1);
				result1 = graphBasedComparatorRanking(input);
				if(result1!=null)
					System.out.println(result1);								
				flag=1;
				break;
			}
		}
		if(flag==0)
			System.out.println("Not a Comparative Question");
	}
	
	public static void userInput() throws Exception
	{
		System.out.println("Initialising Please Wait....");
		System.out.println();
		List<Sequence> patterns=loadDB("Patterns/patterns.txt");
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		System.out.println();
		System.out.println("Initialisation Complete....!!!!");
		System.out.println();
		String flag="y";
		while(flag.equalsIgnoreCase("y"))
		{
			userInputHelper(patterns,br);
			System.out.println("Continue? (Y/N)");
			flag=br.readLine();
		}
	}
	
	public static void main(String args[]) throws Exception
	{
		userInput();
		//new Dataset().getSetA();
		//IEPMining();
		//setA();
		//Analysis("Dataset/Set-A.txt","Dataset/Labels-A.txt");
		//HybridizedModel();
		//extractComparators();
		
		/*
		int fold=10;
		for(int i=1;i<=fold;i++)
			FoldRun(fold,i,"Dataset/Set-A.txt","Dataset/Labels-A.txt");
		*/
			
	}
}
/*
Freebase temp=new Freebase();
System.out.println(temp.getEntities("TV Drama"));
*/

/*
Lemmatizer lemma=new Lemmatizer();
Set<String> unwanted=new HashSet<String>();
unwanted.add("-LSB-");
unwanted.add("-RSB-");
unwanted.add(",");

Set<String> set1=temp.getDomain("apple iphone");
Set<String> set2=temp.getDomain("micromax canvas");
Set<String> common1=Sets.intersection(set1, set2);
System.out.println(common1+"\n");

Set<String> set3=lemma.lemmatize(set1.toString());
Set<String> set4=lemma.lemmatize(set2.toString());
Set<String> common2=Sets.intersection(set3, set4);
common2.removeAll(unwanted);
System.out.println(common2);
*/