package lab1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * Skeleton for an evaluator
 */
public class Evaluator {

	/**
	 * Takes as arguments (1) the gold standard and (2) the output of a program.
	 * Prints to the screen one line with the precision
	 * and one line with the recall.
	 */
	public static void main(String[] args) throws Exception {
		if( args.length < 2){
			System.err.println("usage: Evaluator <gold standard> <results>");
			return;
		}
		File goldStandard = new File(args[0]);
		File ouputProgram = new File(args[1]);

		// precision and recall
		double pre = 0.0, rec = 0.0;
		double n, m = 0.0;
		BufferedReader reader1 = new BufferedReader(new InputStreamReader(new FileInputStream(goldStandard), "UTF-8"));
		BufferedReader reader2 = new BufferedReader(new InputStreamReader(new FileInputStream(ouputProgram), "UTF-8"));
		String line1, line2;
		Set s1  = new HashSet();
		Set s1_1 = new HashSet();
		Set s2 = new HashSet();
		Set s  = new HashSet();
		Set s_1 = new HashSet();
		while( (line1 = reader1.readLine()) != null && line1 != "" ){
			s1.add(line1.replaceAll("\\s", ""));
			s1_1.add(line1.split("\\s")[0]);
			s_1.add(line1.split("\\s")[0]);
		}
		while ( (line2 = reader2.readLine()) != null && line2 != "" ){
			s2.add(line2.replaceAll("\\s", ""));
			s_1.add(line2.split("\\s")[0]);
		}
		s.addAll(s1);
		s.addAll(s2);

		// System.out.println(s1);
		n = (double) (s1.size() + s2.size() - s.size());
		m = (double) (s1_1.size() + s2.size() - s_1.size());
		// pre = n/s2.size();
		pre = n/(m);
		rec = n/s1.size();

		// System.out.println("n: " + n);
		// System.out.println("the output program: " + m);
		// System.out.println("the gold standard : " + s1.size());

		System.out.println("the precision: " + pre);
		System.out.println("the recall: " + rec);

	}
}