import java.math.BigInteger;

/***
 * Represents an elliptic curve over Z/pZ = Fp of the form
 * y^2 = x^3 + Ax + B, where p is odd.
 * Tuple (A, B, p).
 */

public class EllipticCurve {

	private BigInteger p;
	private BigInteger A;
	private BigInteger B;
	
	public EllipticCurve(BigInteger A, BigInteger B, BigInteger p) {
		this.p = p;
		this.A = A;
		this.B = B;
	}
	
	public BigInteger getP() {
		return p;
	}
	
	public BigInteger getA() {
		return A;
	}
	
	public BigInteger getB() {
		return B;
	}
	
	public String toString() {
		return "(" + A + ", " + B + ", " + p + ")";
	}
}
