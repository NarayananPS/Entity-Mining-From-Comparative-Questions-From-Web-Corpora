/**
 * @author Hariprasaad
 *
 */

package entityMining;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class Sequence {

	static MaxentTagger tagger = new MaxentTagger("taggers/english-left3words-distsim.tagger");
	List<String> words = new LinkedList<String>();
	List<String> tags = new LinkedList<String>();
	Set<Integer> otherComps= new HashSet<Integer>();
	String[] comparators=new String[2];
	
	boolean cq_pattern=false;	//Comparative Question
	boolean cq_heuristics=false;	//Comparative Question
	boolean wh=false;	//Wh Question
	boolean vs=false;	//Versus
	boolean JJR=false;

	/*
	 * STEP 1: Process String
	 * Tag the given input String
	 * words - has every word in the question/pattern
	 * tags - has the corresponding tag for every word
	 */
	public void processString(String input) {
		String temp = tagger.tagString(input.replace("?", " ? ").replace("*", " * "));
		//String temp = tagger.tagString(input);
		//String temp = tagger.tagString(input.replace("?", ""));
		StringTokenizer tokenizer = new StringTokenizer(temp);
		List<String> words_ = new LinkedList<String>();
		List<String> tags_ = new LinkedList<String>();
		while (tokenizer.hasMoreTokens()) {
			String[] current = tokenizer.nextToken().split("_");
			if(current[0].equalsIgnoreCase("vrs") || current[0].equalsIgnoreCase("vs") || current[0].equalsIgnoreCase("vs.") || current[0].equalsIgnoreCase("versus."))
			{
				words_.add("versus");
				tags_.add("CC");
				//cq=true;
				vs=true;
			}
			else if(current[1].equals("FW"))
			{
				words_.add(current[0].toLowerCase());
				tags_.add("NN");
			}
			else
			{
				words_.add(current[0].toLowerCase());
				tags_.add(current[1]);
			}
		}
		//System.out.println(words_);
		//System.out.println(tags_);
		phraseChuncker(words_, tags_);
	}

	/*
	 * STEP 2: Phrase Chuncking
	 * Heuristic rules
	 * NNP* -> NNP
	 * NN* -> NN
	 * NN + NNS -> NNS
	 * NNP + NNPS -> NNPS
	 * More + ADJ -> JJR
	 * Most + ADJ -> JJS
	 * ...
	 * NN + NNP -> NNP
	 * NN + NNPS -> NNPS
	 * NNP + NN -> NN
	 * NNPS + NN -> NN
	 * NNS + NNP -> NNP
	 * NNS + NNPS -> NNPS
	 * NNP + NNS -> NNS
	 * NNPS + NNS -> NNS
	 * JJ* -> JJ
	 * JJ + NN -> NN
	 * JJ + NNS -> NNS
	 * JJ + NNP -> NNP
	 * JJ + NNPS -> NNPS
	 * CD* -> CD
	 * NN + CD -> NN
	 * NNS + CD -> NNS
	 * NNP + CD -> NNP
	 * NNPS + CD -> NNPS
	 * NN + CD + NN -> NN
	 * NN + CD + NNS -> NNS
	 * NN + CD + NNP -> NNP
	 * NN + CD + NNPS -> NNPS
	 * NNS + CD + NN -> NN
	 * NNS + CD + NNS -> NNS
	 * NNS + CD + NNP -> NNP
	 * NNS + CD + NNPS -> NNPS
	 * NNP + CD + NN -> NN
	 * NNP + CD + NNS -> NNS
	 * NNP + CD + NNP -> NNP
	 * NNP + CD + NNPS -> NNPS
	 * NNPS + CD + NN -> NN
	 * NNPS + CD + NNS -> NNS
	 * NNPS + CD + NNP -> NNP
	 * NNPS + CD + NNPS -> NNPS
	 */
	public void phraseChuncker(List<String> words_, List<String> tags_) {
		Iterator<String> wordsI = words_.iterator();
		Iterator<String> tagsI = tags_.iterator();
		String currentTag = "";
		String currentWord = "";

		if(tagsI.hasNext())
		{
			currentTag = tagsI.next();
			currentWord = wordsI.next();
		}
		else
		{
			//compress();
			return;
		}

		while (true)
		{
			if(currentWord==null)
				break;
			
			if (!(currentTag.equals("NN") || currentTag.equals("NNP") || currentTag.equals("JJ")
					|| currentWord.equals("more") || currentWord.equals("most") || currentWord.equals("$")))
			{
				words.add(currentWord);
				tags.add(currentTag);
				
				if(tagsI.hasNext())
				{
					currentTag = tagsI.next();
					currentWord = wordsI.next();
				}
				else
					break;
			} 
			else
			{
				//MORE, MOST
				if (currentWord.equals("more") || currentWord.equals("most"))
				{
					String prevWord = currentWord;
					String prevTag = currentTag;
					int flag=0;
					if (tagsI.hasNext())
					{
						flag=1;
						if((currentTag = tagsI.next()).equals("JJ") || currentTag.equals("JJR") || currentTag.equals("JJS"))
						{
							currentWord = wordsI.next();
							words.add(prevWord + " " + currentWord);
							if (prevWord.equals("more"))
								tags.add("JJR");
							else
								tags.add("JJS");
							if(tagsI.hasNext())
							{
								currentTag = tagsI.next();
								currentWord = wordsI.next();
							}
							else
								break;
						}
						else
						{
							words.add(prevWord);
							tags.add(prevTag);
							if (wordsI.hasNext())
								currentWord = wordsI.next();
							else
							{
								currentWord=null;
								currentTag=null;
							}
						}
					}
					else
					{
						if(flag==0)
						{
							words.add(prevWord);
							tags.add(prevTag);
						}
						break;
					}
				}
				//$C
				if (currentWord.equals("$"))
				{
					String prevWord = currentWord;
					String prevTag = currentTag;
					int flag=0;
					if (tagsI.hasNext())
					{
						flag=1;
						if((currentWord = wordsI.next()).equals("c"))
						{
							currentTag = tagsI.next();
							words.add(prevWord + currentWord);
							tags.add("NN");
							if(tagsI.hasNext())
							{
								currentTag = tagsI.next();
								currentWord = wordsI.next();
							}
							else
								break;
						}
						else
						{
							words.add(prevWord);
							tags.add(prevTag);
							if (tagsI.hasNext())
								currentTag = tagsI.next();
							else
							{
								currentWord=null;
								currentTag=null;
							}
						}
					}
					else
					{
						if(flag==0)
						{
							words.add(prevWord);
							tags.add(prevTag);
						}
						break;
					}
				}
				
				//JJ
				if (currentWord!=null && currentTag.equals("JJ"))
				{
					String finalTag = "JJ";
					String prevWord = currentWord;
					String cache = prevWord;
					int endReached=0;
					int tagMismatch=0;
					while (true)
					{
						if (tagsI.hasNext())
						{
							if((currentTag = tagsI.next()).equals("JJ") || currentTag.equals("CD"))
							{
								currentWord = wordsI.next();
								cache += " " + currentWord;
							}
							else if (currentTag.equals("NN") || currentTag.equals("NNS") || currentTag.equals("NNP") || currentTag.equals("NNPS"))
							{
								currentWord = wordsI.next();
								cache += " " + currentWord;
								finalTag = currentTag;
							}
							else
							{
								tagMismatch=1;
							}
						}
						else
						{
							endReached=1;
						}
						if(endReached==1)
						{
							words.add(cache);
							tags.add(finalTag);
							currentWord=null;
							currentTag=null;
							break;
						}
						else if(tagMismatch==1)
						{
							words.add(cache);
							tags.add(finalTag);
							currentWord = wordsI.next();
							break;
						}
					}
				}
				//NN
				if (currentWord!=null && currentTag.equals("NN"))
				{
					String finalTag = "NN";
					String prevWord = currentWord;
					String cache = prevWord;
					int endReached=0;
					int tagMismatch=0;
					while (true)
					{
						if (tagsI.hasNext())
						{
							if((currentTag = tagsI.next()).equals("NN") || currentTag.equals("CD"))
							{
								currentWord = wordsI.next();
								cache += " " + currentWord;
							}
							else if (currentTag.equals("NNS") || currentTag.equals("NNP") || currentTag.equals("NNPS"))
							{
								currentWord = wordsI.next();
								cache += " " + currentWord;
								finalTag = currentTag;
							}
							else
							{
								//currentWord = wordsI.next();//
								tagMismatch=1;
							}
						}
						else
						{
							endReached=1;
						}
						if(endReached==1)
						{
							words.add(cache);
							tags.add(finalTag);
							currentWord=null;
							currentTag=null;
							break;
						}
						else if(tagMismatch==1)
						{
							words.add(cache);
							tags.add(finalTag);
							currentWord = wordsI.next();
							break;
						}
					}
				}
				//NNP
				if (currentWord!=null && currentTag.equals("NNP"))
				{
					String finalTag = "NNP";
					String prevWord = currentWord;
					String cache = prevWord;
					int endReached=0;
					int tagMismatch=0;
					while (true)
					{
						if (tagsI.hasNext())
						{
							if((currentTag = tagsI.next()).equals("NNP") || currentTag.equals("CD"))
							{
								currentWord = wordsI.next();
								cache += " " + currentWord;
							}
							else if (currentTag.equals("NN") || currentTag.equals("NNS") || currentTag.equals("NNPS"))
							{
								currentWord = wordsI.next();
								cache += " " + currentWord;
								finalTag = currentTag;
							}
							else
							{
								tagMismatch=1;
							}
						}
						else
						{
							endReached=1;
						}
						if(endReached==1)
						{
							words.add(cache);
							tags.add(finalTag);
							currentWord=null;
							currentTag=null;
							break;
						}
						else if(tagMismatch==1)
						{
							words.add(cache);
							tags.add(finalTag);
							currentWord = wordsI.next();
							break;
						}
					}
				}
			}
		}
		//System.out.println(words);
		//System.out.println(tags);
		Comparators();
		compress();
	}
	
	/*
	 * STEP 3: Compress "NN*" tags to "NN"
	 */
	public void compress()
	{
		int n=tags.size();
		for(int i=0;i<n;i++)
		{
			if(tags.get(i).startsWith("NN"))
			{
				tags.set(i, "NN");
			}
		}
	}

	/*
	 * COMPARE A QUESTION WITH A PATTERN
	 */
	public Pair<String,String> compare(Sequence pattern)
	{
		String result[]=new String[2];
		int i=0,j=0;
		int maxi=this.words.size();
		int maxj=pattern.words.size();
		int oldi=0;
		int oflag=0;
		int flag=0;
		String currentWord="";
		String currentTag="";
		String otherWord="";
		String otherTag="";
		while(maxi>=maxj && i<maxi && j<maxj)
		{
			currentWord=this.words.get(i);
			currentTag=this.tags.get(i);
			otherWord=pattern.words.get(j);
			otherTag=pattern.tags.get(j);
			if((currentTag.equals(otherTag) && !currentTag.equals("CC")) || currentWord.equals(otherWord))
			{
				if(oflag==0)
				{
					oldi=i+1;
					oflag=1;
				}
				if(otherWord.equals("$c") && currentTag.startsWith("NN"))
				{
					result[flag]=currentWord;
					flag++;
				}
				j++;
			}
			else
			{
				if(oflag!=0)
				{
					oflag=0;
					i=oldi;
					j=0;
					flag=0;
				}
			}
			i++;
		}
		if(result[1]==null)
		{
			cq_pattern=false;
			result[0]=result[1]=null;
		}
		else
		{
			cq_pattern=true;
		}
		return new Pair<String, String>(result[0],result[1]);
	}
	
	public String printComps()
	{
		String result=comparators[0] + "," + comparators[1] + " OTHERS-";
		Iterator<Integer> i=otherComps.iterator();
		while(i.hasNext())
		{
			result+="::" + words.get(i.next());
		}
		return result;
	}

	public Sequence checkComparativeQuestion(Pair<String,String> pair)
	{
		Sequence result=this;
		int[] index={-1,-1};
		int flag=0;
		String fMatch="";
		for(int i=0;i<words.size();i++)
		{
			//pair.x.replace("?", "");
			//pair.y..replace("?", "\?");
			//words.get(i).replace("?", "");
			//System.out.println(pair.x + " " + pair.y);
			//if(words.get(i).matches(".*" + pair.x + ".*") || words.get(i).matches(".*" + pair.y + ".*"))
			if(words.get(i).equals(pair.x) || words.get(i).equals(pair.y))
			{
				index[flag]=i;
				flag++;
				if(flag==2) break;
				/*
				if(fMatch.equals("") || !fMatch.equals(words.get(i)))
				{
					index[flag]=i;
					flag++;
					if(flag==2) break;
				}
				if(words.get(i).equals(pair.x) && !fMatch.equals(""))
					fMatch=pair.x;
				else if(words.get(i).equals(pair.y) && !fMatch.equals(""))
					fMatch=pair.y;
				*/
			}
		}
		if(index[0]!=-1 && index[1]!=-1)
		{
			result.words.set(index[0], "$c");
			result.words.set(index[1], "$c");
			//System.out.println(result.words);
			//System.out.println(result.tags);
			return result;
		}
		return null;
	}
	
	/*
	 * EXTRACTING COMPARATORS - MODIFIED JIU LINDAL APPROACH
	 */
	public void Comparators()
	{
		List<Integer> NN= new ArrayList<Integer>();
		List<Integer> CC= new ArrayList<Integer>();
		
		int indexCC=-1;
		int indexJJR=-1;
		int index=0;
		//int OC=0;
		
		boolean diff=false;
		
		String prevTag="";
		String prevWord="";
		String currentTag="";
		String currentWord="";
		Iterator<String> wordsI = words.iterator();
		Iterator<String> tagsI = tags.iterator();
		
		while(tagsI.hasNext())
		{
			currentTag=tagsI.next();
			currentWord=wordsI.next();
			if(currentTag.equals(",") && prevTag.startsWith("NN"))
			{
				//OC++;
				otherComps.add(index-1);
			}
			if(currentTag.equals("CC") && prevTag.startsWith("NN"))
			{
				//OC++;
				otherComps.add(index-1);
			}
			else if(currentTag.equals("IN") && !currentWord.equals("for") && (prevTag.startsWith("JJR") || prevTag.equals("RBR")))//
			{
				if(NN.size()>0)
				{
					JJR=true;
					indexJJR=index-1;
				}
			}
			/*
			else if(currentTag.equals("RB") && (prevTag.startsWith("JJR") || prevTag.equals("RBR")))
			{
				if(NN.size()>0)
				{
					JJR=true;
					indexJJR=index-1;
				}
			}
			*/
			if(currentTag.startsWith("W") || currentWord.equals("?"))
				wh=true;
			else if(currentWord.matches(".*diff.*") || currentWord.matches(".*comp.*") )//
				diff=true;
			else if(currentTag.startsWith("NN") && !currentWord.matches(".*thing.*") && !currentWord.equals("conversion"))
				NN.add(index);
			else if(currentTag.equals("CC"))
			{
				if(NN.size()>0)
					CC.add(index);
				//if(indexCC==-1)	indexCC=index;
				//if(currentWord.startsWith("v")) indexCC=index;
			}
			prevTag=currentTag;
			prevWord=currentWord;
			index++;
		}
		if(NN.size()>1 && CC.size()>0)
		{
			Iterator<Integer> CCi = CC.iterator();
			int flag=0;
			while(CCi.hasNext())
			{
				Iterator<Integer> NNi = NN.iterator();
				int[] i={NNi.next(),NNi.next()};
				indexCC=CCi.next();
				while(true)
				{
					if(indexCC>i[0] && indexCC<i[1])
					{
						if(words.get(indexCC).equals("versus"))
						{
							if(flag!=1)
							{
								flag=1;
								comparators[0]=words.get(i[0]);
								comparators[1]=words.get(i[1]);
							}
							else
							{
								otherComps.add(words.indexOf(comparators[0]));
								otherComps.add(words.indexOf(comparators[1]));
								comparators[0]=words.get(i[0]);
								comparators[1]=words.get(i[1]);
							}
							break;
						}
						else if(diff==true)
						{
							if(flag!=1)
							{
								flag=1;
								comparators[0]=words.get(i[0]);
								comparators[1]=words.get(i[1]);
							}
							else
							{
								otherComps.add(words.indexOf(comparators[0]));
								otherComps.add(words.indexOf(comparators[1]));
							}
							break;
						}
						else if(((indexCC-i[0]<=3 && i[1]-indexCC<=2 && wh==true) || tags.size()<=6) && words.get(indexCC).equals("or"))//
						{
							if(flag!=1)
							{
								flag=1;
								comparators[0]=words.get(i[0]);
								comparators[1]=words.get(i[1]);
							}
							else
							{
								otherComps.add(words.indexOf(comparators[0]));
								otherComps.add(words.indexOf(comparators[1]));
							}
							break;
						}
						else
							break;
					}
					else if(NNi.hasNext())
					{
						i[0]=i[1];
						i[1]=NNi.next();
					}
					else
						break;
				}
			}
		}
		if(NN.size()>1 && JJR==true && comparators[0]==null)
		{
			Iterator<Integer> NNi = NN.iterator();
			int[] i={NNi.next(),NNi.next()};
			
			while(true)
			{
				if(indexJJR>i[0] && indexJJR<i[1])
				{
					comparators[0]=words.get(i[0]);
					comparators[1]=words.get(i[1]);
					break;
				}
				else if(NNi.hasNext())
				{
					i[0]=i[1];
					i[1]=NNi.next();
				}
				else
					break;
			}
		}
		if(comparators[1]!=null && comparators[0]!=null)
		{
			cq_heuristics=true;
		}
	}
	
	public String stringToSave()
	{
		String result="";
		for(int i=0;i<words.size();i++)
			result+=words.get(i)+" ";
		return result.trim();
	}
	
	//CONSTRUCTOR
	public Sequence(String input) {
		comparators[0]=comparators[1]=null;
		if(input!=null)
		{
			processString(input);
		}
	}
	
	public static void main(String args[]) throws Exception
	{
		System.out.println(tagger.tagString("is Ford GT 100 faster than Ferrari?"));
		Sequence s=new Sequence("is Ford GT 100 faster than Ferrari?");
		System.out.println(s.words);
		System.out.println(s.tags);
	}
}