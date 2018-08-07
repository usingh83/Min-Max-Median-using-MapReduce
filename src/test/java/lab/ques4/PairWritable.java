package lab.ques4;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

public class PairWritable implements Writable,WritableComparable<PairWritable> {
	public Long p1;
	public Long p2;

	public PairWritable(Long p1, Long p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	public PairWritable() {
		this(-1L, -1L);
	}

	public void write(DataOutput out) throws IOException {
		out.writeLong(p1);
		out.writeLong(p2);
	}

	public void readFields(DataInput in) throws IOException {
		p1 = in.readLong();
		p2 = in.readLong();
	}
	
	public Long getKey() {
		return p1;
	}
	public Long getNo() {
		return p2;
	}

	public String toString(PairWritable o) {
		return " Num: " + Long.toString(p2);
	}
	public int compareTo(PairWritable o) {
		int cmp = this.p1.compareTo(o.p1);
		if (cmp != 0) {
			return cmp;
		}
		return this.p2.compareTo(o.p2);
	}
}
