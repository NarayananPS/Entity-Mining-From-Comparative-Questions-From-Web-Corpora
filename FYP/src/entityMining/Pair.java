/**
 * @author Hariprasaad
 *
 */

package entityMining;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Pair<X, Y> {
	public X x;
	public Y y;
	int occurrence;

	public Pair(X x, Y y) {
		this.x = x;
		this.y = y;
		occurrence=1;
	}
	
	public void increment()
	{
		++occurrence;
	}

	public boolean contains(Object o) {
		increment();
		if(o == null) {
			return false;
		}
		else if(o.getClass() == x.getClass() || o.getClass() == y.getClass()) {
			return x.equals(o) || y.equals(o);
		} else {
			return false;
		}
	}

	@Override
	public boolean equals(Object o) {
		if(o == null) {
			//System.out.println("null");
			return false;
		}
		else if(getClass() != o.getClass()) {
			//System.out.println("not equal");
			return false;
		}

		Pair<X, Y> pair = (Pair<X, Y>) o;

		EqualsBuilder eq0 = new EqualsBuilder();
		EqualsBuilder eq1 = new EqualsBuilder();
		String x0 = null;
		String pairX = null;
		String y0 = null;
		String pairY = null;
		if(x instanceof String) {
			x0 = ((String) x).toLowerCase();
		}

		if(y instanceof String) {
			y0 = ((String) y).toLowerCase();
		}

		if(pair.x instanceof String) {
			pairX = ((String) pair.x).toLowerCase();
		}

		if(pair.y instanceof String) {
			pairY = ((String) pair.y).toLowerCase();
		}

		if(x0 != null) {
			if(pairX != null) {
				eq0.append(x0, pairX);
			}
			else {
				eq0.append(x0, pair.x);
			}
			if(pairY != null) {
				eq1.append(x0, pairY);
			}
			else {
				eq1.append(x0, pair.y);
			}
		} else {
			if(pairX != null) {
				eq0.append(x, pairX);
			}
			else {
				eq0.append(x, pair.x);
			}
			if(pairY != null) {
				eq1.append(x, pairY);
			}
			else {
				eq1.append(x, pair.y);
			}
		}

		if(y0 != null) {
			if(pairX != null) {
				eq1.append(y0, pairX);
			}
			else {
				eq1.append(y0, pair.x);
			}
			if(pairY != null) {
				eq0.append(y0, pairY);
			}
			else {
				eq0.append(y0, pair.y);
			}
		} else {
			if(pairX != null) {
				eq1.append(y, pairX);
			}
			else {
				eq1.append(y, pair.x);
			}
			if(pairY != null) {
				eq0.append(y, pairY);
			}
			else {
				eq0.append(y, pair.y);
			}
		}
		return eq0.isEquals() || eq1.isEquals();

	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
				// if deriving: appendSuper(super.hashCode()).
				append(x).
				append(y).
				toHashCode();
	}

	@Override
	public String toString() {
		return "<" + x.toString() + ", " + y.toString() + "> " + occurrence;
	}
}