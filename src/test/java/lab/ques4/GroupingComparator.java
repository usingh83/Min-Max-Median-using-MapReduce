package lab.ques4;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class GroupingComparator extends WritableComparator {
	public GroupingComparator() {
		super(PairWritable.class, true);
		}
	@Override
	/**
	* This comparator controls which keys are grouped
	* together into a single call to the reduce() method
	*/
	public int compare(WritableComparable wc1, WritableComparable wc2) {
			PairWritable pair = (PairWritable) wc1;
			PairWritable pair2 = (PairWritable) wc2;
			return pair.p1.compareTo(pair2.p1);
			}
		}