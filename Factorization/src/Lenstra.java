import java.math.BigInteger;
import java.util.Random;

/***
 * Implements Lenstra factorization using elliptic curves. Also known as ECM (Elliptic Curve Method).
 * @author Jakob Ørhøj
 * 
 * Contains methods to add points on an elliptic curve, find the multiple
 * kP for some integer k and a point P.
 * Can generate a random elliptic curve of the form y^2 = x^3 + Ax + B with the
 * point P=(x, y) on it. 
 */

public class Lenstra {	
	
	private static BigInteger possibleFactor;
	
	public BigInteger factor(BigInteger n, int K, int curves) {
		BigInteger result = n;
		for (int i = 1; i < K; i++) {
			result = elliptic_curve_method(n, Pollard.lcmToBound(i), curves);
			if (!result.equals(n)) {
				return result;
			}
		}
		return result;
	}
	
	public BigInteger elliptic_curve_method(BigInteger p, BigInteger k, int curves) {
		for (int i = 0; i < curves; i++) {
			EllipticCurve E = random_curve(p);
			// Point P = new Point(BigInteger.valueOf(0), BigInteger.valueOf(1));
			Random rnd = new Random();
			BigInteger x, y;
			do {
				x = new BigInteger(p.bitLength(), rnd);
				y = new BigInteger(p.bitLength(), rnd);
			} while(x.compareTo(p) >= 0 && y.compareTo(p) >= 0);
			
			Point P = new Point(x, y);
			
			try {
				elliptic_multiple(E, k, P);
			} catch (ArithmeticException e) {
				if (!possibleFactor.equals(BigInteger.ONE) && !possibleFactor.equals(p)) {
					return possibleFactor;
				}
			}
		}
		return p;
	}
	

	/***
	 * @param P a point P
	 * @param n the integer to be factored
	 * @return A random elliptic curve on which the point P is placed.
	 */
	
	public static EllipticCurve random_curve_silver(Point P, BigInteger n) {
		Random rnd = new Random();
		BigInteger x, y, A, B, s;
		x = P.getX();
		y = P.getY();
		
		do {
			A = new BigInteger(n.bitLength(), rnd);
			B = y.pow(2).subtract(x.pow(3)).subtract(A.multiply(x));
			s = BigInteger.valueOf(4).multiply(A.pow(3)).add(BigInteger.valueOf(27).multiply(B.pow(2)));
		} while (A.compareTo(n) >= 0 && s.gcd(n).compareTo(BigInteger.ONE) != 0);
		
		return new EllipticCurve(A, B, n);
	}
	
	/***
	 * Simplistic version of the algorithm where we only use a limited 
	 * form of elliptic curves.
	 * 
	 * @param p number we want to factor
	 * @return a random elliptic curve E of the form y^2=x^3+ax+1
	 */
	public static EllipticCurve random_curve(BigInteger p) {
		Random rnd = new Random();
		BigInteger a, s;
		
		do {
		    a = new BigInteger(p.bitLength(), rnd);
		    s = BigInteger.valueOf(4).multiply(a.pow(3)).add(BigInteger.valueOf(27));
		} while (a.compareTo(p) >= 0 && s.gcd(p).compareTo(BigInteger.ONE) != 0);
		
		return new EllipticCurve(a, BigInteger.ONE, p);
	}

	/***
	 * @param E the elliptic curve we find the multiple on
	 * @param k a positive integer
	 * @param P a point on E
	 * @return the multiple kP of the point P on the elliptic curve E
	 */
	public static Point elliptic_multiple(EllipticCurve E, BigInteger k, Point P) throws ArithmeticException {
		
		Point power = P;
		Point kP = new Point();
		
		while (k.compareTo(BigInteger.valueOf(0)) != 0) {
			if (k.mod(BigInteger.valueOf(2)).compareTo(BigInteger.ZERO) != 0) {
				kP = elliptic_add(E, kP, power);
			}
			power = elliptic_add(E, power, power);
			k = k.divide(BigInteger.valueOf(2));
		}
		return kP;
	}

	/***
	 * @param E an elliptic curve 
	 * @param P1 a point on E
	 * @param P2 a point on E
	 * @return the point P = P1 + P2 where the addition is as defined in chapter 1
	 */
	public static Point elliptic_add(EllipticCurve E, Point P1, Point P2) throws ArithmeticException {
	
		if (P1.getO().equals("Identity")) {
			return P2;
		} else if (P2.getO().equals("Identity")) {
			return P1;
		}
		
		BigInteger p = E.getP();
		
		BigInteger x1 = P1.getX();
		BigInteger y1 = P1.getY();
		
		BigInteger x2 = P2.getX();
		BigInteger y2 = P2.getY();
		
		x1 = x1.mod(p);
		y1 = y1.mod(p);
		
		x2 = x2.mod(p);
		y2 = y2.mod(p);
		
		BigInteger la;
		
		// new Point() constructs the identity.
		if (x1.equals(x2) && !y1.equals(y2)) {
			return new Point();
		}
		
		if (P1.equals(P2)) {
			if (y1.compareTo(BigInteger.ZERO) == 0) {
				return new Point();
			}
			
			possibleFactor = y1.multiply(BigInteger.valueOf(2)).gcd(p);
			la = (BigInteger.valueOf(3).multiply(x1.pow(2)).add(E.getA())).multiply((y1.multiply(BigInteger.valueOf(2)).modInverse(p)));
		} else {
			possibleFactor = x1.subtract(x2).gcd(p);
			la = (y1.subtract(y2)).multiply((x1.subtract(x2).modInverse(p)));
		}
		
		BigInteger x3 = la.pow(2).subtract(x1).subtract(x2);
		BigInteger y3 = (la.negate()).multiply(x3).subtract(y1).add(la.multiply(x1));
		
		x3 = x3.mod(p);
		y3 = y3.mod(p);
		
		return new Point(x3, y3);
	}
}
