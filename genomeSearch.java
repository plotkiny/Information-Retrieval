
import java.io.*; import java.util.*;
import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.SortedMap;

public class genomeSearch {
 		
	//Create TreeMap to store data and comparator to sort the keys 
	private SortedMap<String, ArrayList<String>> dictionary = new TreeMap<String, ArrayList<String>>(new Comparator<String>() {
		 
        public int compare(String a, String b) {
			String [] name = {a,b};
			ArrayList<String> array = new ArrayList<String>();
		
			for (String i: name) {
				String index = i.split("chr")[1];
				String num;
				if(index.equals("X")) {
					num = "23";
				}else if (index.equals("Y")) {
					num = "24";
				}else{
					try{
						num = index;
					}catch (NumberFormatException e) {
						continue;
					}
				}
				array.add(num);
			}
			
			return Integer.valueOf(array.get(0)).compareTo(Integer.valueOf(array.get(1)));
		}
	});
	
	
	public ArrayList<String> Codon(ArrayList<String> k) {
		
		ArrayList<String> chromosome = k;
		ArrayList<String> Codon = new ArrayList<String>();
		for ( String j : chromosome) {
			String [] t = j.split("\\s");  //Tokenize the line
			Codon.add(t[1]);
		}
		return Codon;
		
	}
	
	public String ConversionPlus(String s) {
		
		Integer a = Integer.parseInt(s.replaceAll("chr", ""));
		Integer b = a + 1;
		String c = "chr"+ b;
		
		return c;
		
	}
	
	public String ConversionMinus(String s) {
		
		Integer a = Integer.parseInt(s.replaceAll("chr", ""));
		Integer b = a - 1;
		String c = "chr"+ b;
		
		return c;
		
	}
	
	public genomeSearch(String a){
		String file = a;
		try {		
			//To read probes.txt, open input stream.
			BufferedReader br = new BufferedReader (new FileReader(file));
			String  thisLine = null;
			StringBuilder  stringBuilder = new StringBuilder();
			String ls = System.getProperty("line.separator");
					
			//Collect data from genomic file
			while ((thisLine = br.readLine()) != null) {
				if(thisLine.startsWith("Chromosome")) {
					continue;
				}else {
					String [] token = thisLine.split("\\s");  //Tokenize the line				
					String chromosome = token[0].replaceAll("[^\\d.]", "");  //Regular expression to remove all non-digits
					
					if (!dictionary.containsKey(token[0])) {
						dictionary.put(token[0], new ArrayList<String>());
						dictionary.get(token[0]).add(thisLine);
					}else{ 
						if (token[0].startsWith("chrX")) {
								dictionary.get(token[0]).add(thisLine);
						}else if (token[0].startsWith("chrY")) {
								dictionary.get(token[0]).add(thisLine);
						}else{
							dictionary.get("chr" + chromosome).add(thisLine); 
						}
					}
				}
			}		
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
	
	public void Run() {
		
		//Search for the points
		String search = JOptionPane.showInputDialog("Please enter the genomic search criteria:");
		
		//Regular expressions for search criteria: (chr1,chr2, chr1_s, chr1_e, chr1_s, chr2_e)
		String [] patterns = {"(chr?[^:]+)", "(?<=-).*(chr?[^:]+)", "(?<=:).*?(\\d+)", "(?<=-).*", "(?<=:).*?(\\d+)", "(?<=-chr[0-9]..).*"};
		ArrayList<String> match = new ArrayList<String>();
		
		for (Integer i = 0; i < patterns.length; i++ ) {
			final Pattern p = Pattern.compile(patterns[i]);
			final Matcher m = p.matcher(search);
			if (m.find()) {
				match.add(m.group(0));
				
			}else {
				match.add("N/A");
			}
		}
		
		String chr1 = match.get(0);
		String chr = match.get(1);	 
		 
		if ((!"N/A".equals(chr1)) && (!"N/A".equals(chr))) {
			Integer s = Integer.parseInt(match.get(4));
			Integer e = Integer.parseInt(match.get(5));
			
			//Get chromosome c2-1
			String chr2 =  ConversionPlus(chr);
			
			ArrayList<String> searchResult = Chromosome(dictionary, chr1, chr2, s, e);
			
			//Return the first 15 lines (header)
			if (searchResult.isEmpty()) {
				System.out.println("The search returned empty");
			}else{	
				System.out.println("The first 15 values in the search are:");
				for (Integer i=0; i<15; i++){
					System.out.println(searchResult.get(i));
				}
			}
						
		}else if(!"N/A".equals(chr1)) {
			Integer s = Integer.parseInt(match.get(2));
			Integer e = Integer.parseInt(match.get(3));
			ArrayList<String> searchResult = Chromosome(dictionary, chr1, s, e);
			
			//Return the first 15 lines (header)
			if (searchResult.isEmpty()) {
				System.out.println("The search returned empty");
			}else{
				System.out.println("The first 15 values in the search are:");
				for (Integer i=0; i<15; i++){
					System.out.println(searchResult.get(i));
				}
			}
		}	
	}
	
	public ArrayList<String> Chromosome(SortedMap<String, ArrayList<String>> d, String chr1, Integer s, Integer e) {
		
		dictionary = d;
		String c1 = chr1; 
		Integer point1 = s; //start comparison point
		Integer point2 = e; //end comparison point
		
		//Create partialMap and extract start positions
		ArrayList<String> partialMap = new ArrayList<String>();
		partialMap = dictionary.get(c1);
		ArrayList<String> Codon1 = Codon(partialMap);
					
		//Find start and stop index positions
		//Integer start=Collections.binarySearch(partialMap, point1);  
		Integer start = binarySearch(Codon1, point1);
		Integer stop = binarySearch(Codon1, point2);
		
		//Appending start key values
		ArrayList<String> valueList = new ArrayList<String>();  //Initializing empty array to hold values
		for (Integer i = start; i < stop; i++ ) {
			valueList.add(partialMap.get(i)); 
		} 
		return valueList;
	}
	
	public ArrayList<String> Chromosome(SortedMap<String, ArrayList<String>> d, String chr1, String chr2, Integer s, Integer e) {
		
		dictionary = d;
		String c1 = chr1;
		String c2 = chr2;
		
		//Get chromosome c2-1
		String c3 = ConversionMinus(c2);
		
		Integer point1 = s; //start comparison point
		Integer point2 = e; //end comparison point
			
		//Create partialMap
		SortedMap<String, ArrayList<String>> partialMap = new TreeMap<String, ArrayList<String>>();
		partialMap = dictionary.subMap(c1,c2);
		ArrayList<String> keyList = new ArrayList<String>(partialMap.keySet());
		Integer size = keyList.size();	
		
		//Extract values of starting positions in first and last search chromosome
		ArrayList<String> Codon1 = Codon(partialMap.get(c1));
		ArrayList<String> Codon2 = Codon(partialMap.get(c3));
		
		//length of chr1 ArrayList
		int a = partialMap.get(keyList.get(0)).size();
		
		//Find start and stop index positions
		Integer start = binarySearch(Codon1, point1);
		Integer stop = binarySearch(Codon2, point2);
		
		
		//Appending start key values
		ArrayList<String> valueList = new ArrayList<String>();  //Initializing empty array to hold values
		for (Integer i = start; i < a; i++ ) {
			valueList.add(partialMap.get(keyList.get(0)).get(i));			
			
		} 
			
		//Appending middle key values
		for (Integer i = 1; i < (size-1); i++ ) {
			for (String j : partialMap.get(keyList.get(i))) {
				valueList.add(j);
			} 
		}	
		
		//Appending end key values
		for (Integer i = 0; i < stop; i++ ) {
			valueList.add(partialMap.get(keyList.get(0)).get(i));
		} 
		
		return valueList;	
	}
	
	//Binary search algorithm
	public Integer binarySearch(ArrayList<String> chromosome, Integer key) {
	 
		Integer k = key;
		ArrayList<String> chr = chromosome;
		
		int start = 0;
		int array_length = chr.size();
		int end = array_length - 1;
		
		while (start <= end) {
		
			int mid = (start + end)/2;
			if (k == Integer.parseInt(chr.get(mid))) {
				break;
			}
			if (k < Integer.parseInt(chr.get(mid))) {
				end = mid -1;
			}else {
				start = mid + 1;
			}
			
		}
		return start;
	}
		
		
	//Executing queries
    public static void main(String[] args){ 
	
		//Call the constructor method
		String file = JOptionPane.showInputDialog("Please enter the genomic file name:");
		genomeSearch data = new genomeSearch(file);
		
		//Call the Run method
		data.Run();
				
	}
}






