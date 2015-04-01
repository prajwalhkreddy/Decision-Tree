import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DT {

	public static void main(String[] args) {

		try {
			readData("src/train-3.dat");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void readData(String filename) throws Exception {

		FileInputStream in = null;

		try {

			File inputFile = new File(filename);

			in = new FileInputStream(inputFile);

		} catch (Exception e) {

			System.out.println("Unable to open data file: " + filename + "\n"
					+ e);

		}

		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		String input1, input2;
		int counter=0,colcount=0;
		HashMap<String, Integer> index = new HashMap<String, Integer>();
		HashMap<Integer, ArrayList<Integer>> content=new HashMap<Integer, ArrayList<Integer>>();
		
		while (true) {

			while((input1 = br.readLine())!=null){
				counter++;
				
				if(counter==1){
					
				System.out.println(input1);
				
				String[] result = input1.split("\\s");
				for (int x = 0; x < result.length; x = x + 2) {

					index.put(result[x], Integer.parseInt(result[x + 1]));
				}

				for (HashMap.Entry<String, Integer> e : index.entrySet()) {

					
					System.out.println("Attribute: " + e.getKey()
							+ " Possible Values: " + e.getValue());
				}
				
				}
				else{
					System.out.println(input1);
					ArrayList<Integer> ar=new ArrayList<Integer>();
					ar.add(
					content.put(counter--, Integer.parseInt(ar[colcount]));
					colcount++;
					for (HashMap.Entry<Integer, Integer> x : content.entrySet()) {

						
						System.out.println("Col Number: " + x.getKey()
								+ " Col Contents: " + x.getValue());
					}
				}
			}

			

			

			break;
			
			

		}

	}
}
