import java.math.BigInteger;

/**
 * Represents a point on an elliptic curve.
 * Is of the form (x, y) or the string "Identity".
 */

public class Point {

	private String o;
	private BigInteger x;
	private BigInteger y;
	
	public Point(BigInteger x, BigInteger y) {
		this.x = x;
		this.y = y;
		o = "empty";
	}
	
	public Point() {
		o = "Identity";
	}
	
	public BigInteger getX() {
		return x;
	}
	
	public BigInteger getY() {
		return y;
	}
	
	public String getO() {
		return o;
	}
	
	public boolean equals(Point p) {
		if (this.x.equals(p.getX()) && this.y.equals(p.getY())) {
			return true;
		} else {
			return false;
		}
	}
	
	public String toString() {
		if (!o.equals("Identity")) {
			return "(" + x + ", " + y + ")";
		} else {
			return o;
		}
	}
}