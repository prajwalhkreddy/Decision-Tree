import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class DecisionTreeAlgo

{
	public static void main(String[] args) throws Exception {

		DecisionTreeAlgo hw1decisiontreenode = new DecisionTreeAlgo();

		hw1decisiontreenode.readData(args[0]);

		hw1decisiontreenode.buildtreenode();

		hw1decisiontreenode.readTestData(args[1]);

		hw1decisiontreenode.checkTestAccuracy();

	}

	String[] attrNames;
	int attrCount;
	Vector[] vcontent;
	ArrayList<ArrayList<Integer>> temp = new ArrayList<ArrayList<Integer>>();

	class DataPoint {

		public int[] attributes;

		public DataPoint(int attrCount) {

			attributes = new int[attrCount];

		}

	};

	// Decomposed tree node values are being stored 

	class treenode {

		public double entropy;

		public Vector data;

		public int divAttribute;

		public int divValue;

		public treenode[] children;

		public treenode parent;

		public treenode() {

			data = new Vector();

		}

	};

	treenode root = new treenode();

	
	
	//get tokenized values in the form of symbols
	
	public int getSymbolValue(int attribute, String symbol) {

		int index = vcontent[attribute].indexOf(symbol);
		// System.out.println("Attr "+attribute+"\n"+"Symbol "+symbol);
		// System.out.println(symbol);
		// System.out.println("Index"+index);
		if (index < 0) {

			vcontent[attribute].addElement(symbol);

			return vcontent[attribute].size() - 1;

		}

		return index;

	}

	/*
	 * public int[] getAllValues(Vector data, int attribute) {
	 * 
	 * Vector values = new Vector();
	 * 
	 * int num = data.size();
	 * 
	 * for (int i = 0; i < num; i++) {
	 * 
	 * DataPoint point = (DataPoint) data.elementAt(i);
	 * 
	 * String symbol =
	 * 
	 * (String) vcontent[attribute].elementAt(point.attributes[attribute]);
	 * 
	 * int index = values.indexOf(symbol);
	 * 
	 * if (index < 0) {
	 * 
	 * values.addElement(symbol);
	 * 
	 * }
	 * 
	 * }
	 * 
	 * int[] array = new int[values.size()];
	 * 
	 * for (int i = 0; i < array.length; i++) {
	 * 
	 * String symbol = (String) values.elementAt(i);
	 * 
	 * array[i] = vcontent[attribute].indexOf(symbol);
	 * 
	 * }
	 * 
	 * values = null;
	 * 
	 * return array;
	 * 
	 * }
	 */

	public int[] getAllValuesLeaf(Vector data, int attribute) {

		Vector values = new Vector();

		int num = data.size();
		int countOne = 0, countZero = 0;

		for (int i = 0; i < num; i++) {

			DataPoint point = (DataPoint) data.elementAt(i);

			String symbol =

			(String) vcontent[attribute].elementAt(point.attributes[attribute]);

			int index = values.indexOf(symbol);

			if (symbol.equals("0")) {
				countZero++;

			} else if (symbol.equals("1")) {
				countOne++;
			}
			if (index < 0) {

				values.addElement(symbol);

			}

		}
		if (values.size() != 1) {
			if (countOne > countZero) {
				values.addElement("1");
			} else {
				values.addElement("0");
			}

		}
		int[] array = new int[values.size()];

		for (int i = 0; i < array.length; i++) {

			String symbol = (String) values.elementAt(i);

			array[i] = vcontent[attribute].indexOf(symbol);

		}

		values = null;

		return array;

	}

	public Vector getSubset(Vector data, int attribute, int value) {

		Vector subset = new Vector();

		int num = data.size();

		for (int i = 0; i < num; i++) {

			DataPoint point = (DataPoint) data.elementAt(i);

			if (point.attributes[attribute] == value)
				subset.addElement(point);

		}

		return subset;

	}

	public double calcEntropy(Vector data) {

		int numdata = data.size();

		if (numdata == 0)
			return 0;

		int attribute = attrCount - 1;

		int numvalues = vcontent[attribute].size();

		double sum = 0;

		for (int i = 0; i < numvalues; i++) {

			int count = 0;

			for (int j = 0; j < numdata; j++) {

				DataPoint point = (DataPoint) data.elementAt(j);

				if (point.attributes[attribute] == i)
					count++;

			}

			double probability = 1. * count / numdata;

			if (count > 0)
				sum += -probability * Math.log(probability);

		}

		return sum;

	}

	public boolean previouslyUsed(treenode node, int attribute) {

		if (node.children != null) {

			if (node.divAttribute == attribute)

				return true;

		}

		if (node.parent == null)
			return false;

		return previouslyUsed(node.parent, attribute);

	}

	public void decomposeNode(treenode node) {

		double bestEntropy = 0;

		boolean selected = false;

		int selecAttribute = 0;

		int numdata = node.data.size();
		// System.out.println("Num Data "+numdata);

		int ipAttrCount = attrCount - 1;

		DataPoint tempDatapoint;

		/*
		 * for(int x =0; x < node.data.size(); x++) { tempDatapoint =
		 * (DataPoint) node.data.elementAt(x);
		 * System.out.println("Node content is "); for (int j =0; j <
		 * tempDatapoint.attributes.length; j++) { System.out.print(
		 * tempDatapoint.attributes[j] + " "); } System.out.println(); }
		 */

		node.entropy = calcEntropy(node.data);

		if (node.entropy == 0)
			return;

		for (int i = 0; i < ipAttrCount; i++) {

			int numvalues = vcontent.length;

			if (previouslyUsed(node, i))
				continue;

			double avgEntropy = 0;

			for (int j = 0; j < numvalues; j++) {

				Vector subset = getSubset(node.data, i, j);

				if (subset.size() == 0)
					continue;

				double subEntropy = calcEntropy(subset);

				avgEntropy += subEntropy *

				subset.size();

			}

			avgEntropy = avgEntropy / numdata; //

			// Taking the weighted average

			if (selected == false) {

				selected = true;

				bestEntropy = avgEntropy;

				selecAttribute = i;

			} else {

				if (avgEntropy < bestEntropy) {

					selected = true;

					bestEntropy = avgEntropy;

					selecAttribute = i;

				}

			}

		}

		if (selected == false)
			return;

		int numvalues = vcontent[selecAttribute].size();

		node.divAttribute = selecAttribute;

		node.children = new treenode[numvalues];

		for (int j = 0; j < numvalues; j++) {

			node.children[j] = new treenode();

			node.children[j].parent = node;

			node.children[j].data = getSubset(node.data,

			selecAttribute, j);

			node.children[j].divValue = j;

		}

		for (int j = 0; j < numvalues; j++) {

			decomposeNode(node.children[j]);

		}

		node.data = null;

	}

	public int readData(String filename) throws Exception {

		FileInputStream in = null;

		try {

			File ipfile = new File(filename);

			in = new FileInputStream(ipfile);

		} catch (Exception e) {

			System.out.println("Not able to open file: " + filename + "\n"
					+ e);

			return 0;

		}

		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		String input;
		int lineCounter = 0;
		HashMap<String, Integer> lineOne = new HashMap<String, Integer>();
		// ArrayList<Integer> temp=new ArrayList<Integer>();

		StringTokenizer tokenizer;

		while ((input = br.readLine()) != null) {

			lineCounter++;

			if (lineCounter == 1) {
				String[] result = input.split("\\s");
				for (int x = 0; x < result.length; x = x + 2) {

					lineOne.put(result[x], Integer.parseInt(result[x + 1]));
				}
				int x = 0;
				attrNames = new String[result.length];
				for (HashMap.Entry<String, Integer> e : lineOne.entrySet()) {

					attrNames[x] = e.getKey();
					x++;

					// System.out.println("Attribute: " + e.getKey()
					// + " Possible Values: " + e.getValue());
					// System.out.println("Attribute Names:" + attrNames[x -
					// 1]);

				}
				tokenizer = new StringTokenizer(input);

				attrCount = (tokenizer.countTokens() / 2) + 1;

				// System.out.println("Column Count:" + attrCount);
				vcontent = new Vector[attrCount];

				for (int i = 0; i < attrCount; i++)
					vcontent[i] = new Vector();

			} else {
				tokenizer = new StringTokenizer(input);

				DataPoint point = new DataPoint(attrCount);

				ArrayList<Integer> newtemp = new ArrayList<Integer>();

				for (int i = 0; i < attrCount; i++) {

					String ch = tokenizer.nextToken();
					point.attributes[i] = getSymbolValue(i, ch);
					// newtemp =new ArrayList<Integer>();
					newtemp.add(Integer.parseInt(ch));

					// System.out.println("Point:"+point.attributes[i]);

				}
				temp.add(newtemp);

				// System.out.println(newtemp);

				root.data.addElement(point);

			}

		}

		br.close();
		// System.out.println(temp);

		return 1;

	}

	public int readTestData(String filename) throws Exception {

		FileInputStream in = null;

		try {

			File ipfile = new File(filename);

			in = new FileInputStream(ipfile);

		} catch (Exception e) {

			System.out.println("Unable to open data file: " + filename + "\n"
					+ e);

			return 0;

		}

		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		String input;
		int lineCounter = 0;
		HashMap<String, Integer> lineOne = new HashMap<String, Integer>();
		// ArrayList<Integer> temp=new ArrayList<Integer>();

		StringTokenizer tokenizer;

		while ((input = br.readLine()) != null) {

			lineCounter++;

			if (lineCounter == 1) {
				String[] result = input.split("\\s");
				for (int x = 0; x < result.length; x = x + 2) {

					lineOne.put(result[x], Integer.parseInt(result[x + 1]));
				}
				int x = 0;
				attrNames = new String[result.length];
				for (HashMap.Entry<String, Integer> e : lineOne.entrySet()) {

					attrNames[x] = e.getKey();
					x++;

					// System.out.println("Attribute: " + e.getKey()
					// + " Possible Values: " + e.getValue());
					// System.out.println("Attribute Names:" + attrNames[x -
					// 1]);

				}
				tokenizer = new StringTokenizer(input);

				attrCount = (tokenizer.countTokens() / 2) + 1;

				// System.out.println("Column Count:" + attrCount);
				/*
				 * vcontent = new Vector[attrCount];
				 * 
				 * for (int i = 0; i < attrCount; i++) vcontent[i] = new
				 * Vector();
				 */
			} else {
				tokenizer = new StringTokenizer(input);

				DataPoint point = new DataPoint(attrCount);

				ArrayList<Integer> newtemp = new ArrayList<Integer>();

				for (int i = 0; i < attrCount; i++) {

					String ch = tokenizer.nextToken();
					point.attributes[i] = getSymbolValue(i, ch);
					// newtemp =new ArrayList<Integer>();
					newtemp.add(Integer.parseInt(ch));

					// System.out.println("Point:"+point.attributes[i]);

				}
				temp.add(newtemp);

				// System.out.println(newtemp);

				// root.data.addElement(point);

			}

		}

		br.close();
		// System.out.println(temp);

		return 1;

	}

	public void printtreenode(treenode node, String tab) {

		int outputattr = attrCount - 1;

		if (node.children == null) {

			int[] values = getAllValuesLeaf(node.data, outputattr);

			if (values.length == 1) {

				/*
				 * System.out.println(tab + "\t" + attrNames[outputattr] +
				 * " = \"" +
				 * 
				 * vcontent[outputattr].elementAt(values[0]) + "\"");
				 */

				System.out.println(vcontent[outputattr].elementAt(values[0]));

				return;

			} else {
				System.out.println(vcontent[outputattr]
						.elementAt(values[values.length - 1]));
			}
			// System.out.print(tab + "\t" + attrNames[outputattr] + " = {");
			/*
			 * for (int i=0; i < values.length; i++) {
			 * 
			 * System.out.print("\"" + vcontent[outputattr].elementAt(values[i])
			 * + "\"");
			 * 
			 * }
			 */
			return;

		}

		int numvalues = node.children.length;

		for (int i = 0; i < numvalues; i++) {

			if (checkIfNull(node.children[i]) == 1
					|| checkIfNull(node.children[i]) == 2) {
				for (int k = 0; k < tab.length(); k++) {

					System.out.print("| ");
				}
				System.out.print(attrNames[node.divAttribute] + " = "
						+ vcontent[node.divAttribute].elementAt(i) + ":");

			} else {
				for (int k = 0; k < tab.length(); k++) {
					System.out.print("| ");
				}
				System.out.println(attrNames[node.divAttribute] + " = "
						+ vcontent[node.divAttribute].elementAt(i) + ":");
			}
			printtreenode(node.children[i], tab + "|");

			/*
			 * if (i != numvalues - 1) System.out.print(tab + "} else ");
			 * 
			 * else System.out.println(tab + "}");
			 */

		}

	}

	public double checkAccuracy(treenode node,
			ArrayList<ArrayList<Integer>> records) {
		double accuracy = 0.0;

		ArrayList<Integer> singleRec = null;

		int temprecvalue = 0, tempdivattribute = 0, counter = 0;
		int tempRecLabel = 0;
		treenode tempNode = null;

		for (int i = 0; i < records.size(); i++) {

			tempNode = node;
			singleRec = records.get(i);
			int recValue;
			tempRecLabel = singleRec.get(attrCount - 1);
			// System.out.println("Single Row"+singleRec);

			while (tempNode.children != null) {
				tempdivattribute = tempNode.divAttribute;
				// System.out.println("node's division attribute is " +
				// tempdivattribute);
				recValue = singleRec.get(tempdivattribute);
				// temprecvalue= singleRec.get(tempdivattribute);
				temprecvalue = (vcontent[tempdivattribute]).indexOf(Integer
						.toString(recValue));
				// System.out.println("Temp rec value");
				tempNode = tempNode.children[temprecvalue];
			}

			int outputattr = attrCount - 1;
			String x = null;
			if (tempNode.children == null) {
				// Comenting out the following as we have defined new funtion to
				// get leafLabel

				int[] values = getAllValuesLeaf(tempNode.data, outputattr);
				// System.out.println("Testing" +
				// vcontent[outputattr].elementAt(values[0]));
				if (values.length == 1) {

					// System.out.println("Leaf node: "+vcontent[outputattr].elementAt(values[0]));
					x = (vcontent[outputattr].elementAt(values[0])).toString();
				} else {
					// System.out.println(vcontent[outputattr].elementAt(values[values.length-1]));
					x = (vcontent[outputattr]
							.elementAt(values[values.length - 1])).toString();
				}

				// x = getLeafLabel(tempNode);
				// System.out.println("\nx is " + x);

				// System.out.println("Temp rec label is  " + tempRecLabel);
				if (Integer.parseInt(x) == tempRecLabel) {
					// System.out.println("Entering counter update loop");
					counter++;
				}
			}

		}
		// System.out.println("Counter is  " + counter);
		accuracy = (double) counter / records.size();
		// System.out.println("Accuracy:" + accuracy*100);

		return accuracy;

	}

	/*
	 * public int getLeafLabel(treenode root) {
	 * System.out.println("Into getleafLabel"); // int leafLabel; int size =
	 * root.data.size(); int countOne = 0; int countZero = 0; for (int i = 0; i
	 * < size; i++) { DataPoint records = (DataPoint) root.data.elementAt(i);
	 * System.out.println("\nRecord for root " + root.divAttribute + " ");
	 * 
	 * for( int j =0; j< attrCount; j++) { System.out.print(" " +
	 * records.attributes[j]); }
	 * 
	 * int label = records.attributes[attrCount - 1]; //
	 * System.out.println("Label is " + label);
	 * 
	 * if (label == 0) { countZero++; } else { countOne++; } }
	 * System.out.println("Count0 =" + countZero + " Countone =" + countOne); if
	 * (countZero > countOne) { return 0;
	 * 
	 * } else { return 1; } }
	 */
	
	//Checking for leaf node while printing the tree
	
	public int checkIfNull(treenode node) {
		int outputattr = attrCount - 1;

		if (node.children == null) {

			int[] values = getAllValuesLeaf(node.data, outputattr);

			if (values.length == 1) {

				return 1;

			} else {
				return 2;
			}

		}
		return 0;

	}

	public void buildtreenode() {

		decomposeNode(root);
		printtreenode(root, "");
		double trainAccuracy;
		trainAccuracy = checkAccuracy(root, temp);
		System.out.println("Training Data Accuracy:" + trainAccuracy * 100);

	}

	public void checkTestAccuracy() {

		// checkAccuracy(root, temp);
		double testAccuracy;
		testAccuracy = checkAccuracy(root, temp);
		System.out.println("Test Data Accuracy:" + testAccuracy * 100);

	}

}