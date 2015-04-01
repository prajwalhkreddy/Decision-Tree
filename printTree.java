import java.util.Vector;

import DecisionTreeAlgo.treenode;




public class printTree {
	
	DecisionTreeAlgo hello1= new DecisionTreeAlgo();
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
	
	public void printtreenode(treenode node, String tab, String[] attrNames, int attrCount,Vector vcontent)
	{

		int outputattr = attrCount - 1;

		if (node.children == null) {

			int[] values = hello1.getAllValues(node.data, outputattr);

			if (values.length == 1) {

				/*
				 * System.out.println(tab + "\t" + attrNames[outputattr] +
				 * " = \"" +
				 * 
				 * vcontent[outputattr].elementAt(values[0]) + "\"");
				 */

				System.out.println(vcontent[outputattr].elementAt(values[0]));

				return;

			}

			/*
			 * System.out.print(tab + "\t" + attrNames[outputattr]);
			 * 
			 * for (int i = 0; i < values.length; i++) {
			 * 
			 * System.out.print("\"" + vcontent[outputattr].elementAt(values[i])
			 * + "\"");
			 * 
			 * if (i != values.length - 1) System.out.print(" , ");
			 * 
			 * }
			 */
			// System.out.println(" };");

			return;

		}

		int numvalues = node.children.length;

		for (int i = 0; i < numvalues; i++) {

			if (checkIfNull(node.children[i]) == 1) {

				System.out.print(tab + "|" + attrCount[node.divAttribute]
						+ " = " + vcontent[node.divAttribute].elementAt(i)
						+ ":");

			} else {

				System.out.println(tab + "|" + attrCount[node.divAttribute]
						+ " = " + vcontent[node.divAttribute].elementAt(i)
						+ ":");
			}
			printtreenode(node.children[i], tab + "\t",attrCount,attrNames,vcontent[node.divAttribute]);

			/*
			 * if (i != numvalues - 1) System.out.print(tab + "} else ");
			 * 
			 * else System.out.println(tab + "}");
			 */

		}

	}

	public int checkIfNull(treenode node) {
		int outputattr = attrCount - 1;

		if (node.children == null) {

			int[] values = getAllValues(node.data, outputattr);

			if (values.length == 1) {

				return 1;

			}

		}
		return 0;

	}
}
