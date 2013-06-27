import java.math.BigInteger;

/**
 * Implements Pollard's p-1 algorithm for integer factorization.
 * The implementation is based on the algorithm given in
 * "Rational Points on Elliptic Curves", Tate, Silverman.
 * 
 * This is in no way optimized and only serves as a proof of concept.
 */

public class Pollard {

	private BigInteger k;
	private int K_max;
	
	public Pollard(int max) {
		K_max = max;
	}
	
	public BigInteger factor(BigInteger n, int tries) {
		return null;
	}
	
	public static BigInteger lcm(BigInteger... values) {
		if (values.length == 0) { 
			return BigInteger.ONE;
		}
		BigInteger lcm = values[0];
		for (int i = 1; i < values.length; i++) {
			if (values[i].signum() != 0) {
				final BigInteger gcd = lcm.gcd(values[i]);
				if (gcd.equals(BigInteger.ONE)) {
					lcm = lcm.multiply(values[i]);
				}
				else {
					if (!values[i].equals(gcd)) {
						lcm = lcm.multiply(values[i].divide(gcd));
					}
				}
			}
		}
		return lcm;
	}
	
	// Edit so it uses BigInt instead of int, may not be needed.
	public static BigInteger lcmToBound(int k) {
		BigInteger[] numbers = new BigInteger[k];
		for (int i = 1; i <= k; i++) {
			numbers[i-1] = BigInteger.valueOf(i);
		}
		return lcm(numbers);
	}
	
	public BigInteger factor(BigInteger n) {

		BigInteger result = BigInteger.valueOf(0);
		int K = 5;
		int A = 2;
		
		while (result.compareTo(BigInteger.valueOf(0)) == 0) {
			
			if (K > K_max) {
				return n;
			}
			
			// k =  LCM[1, 2, 3, ..., K]
			k = lcmToBound(K);
			
			// 1 < a < n
			BigInteger a = BigInteger.valueOf(A);
			
			// If gcd(a, n) > 1 then a is a non-trivial factor of n
			BigInteger g = a.gcd(n);
			if (g.compareTo(BigInteger.ONE) > 0) {
				return g;
			}
			
			// D = gcd(a^k - 1, n)
			a = a.modPow(k, n);
			a = a.add(BigInteger.ONE.negate());
			BigInteger D = n.gcd(a);
			
			// If 1 < D < n then D is a non-trivial factor of n.
			// else if D = 1 we go back and pick a larger k. 
			// else if D = n we go back and choose another a.
			if (!(D.compareTo(BigInteger.ONE) == 0) && !(D.compareTo(n) == 0)) {
				return D;
			} else if (D.compareTo(BigInteger.ONE) == 0) {
				K++;
			} else if (D.compareTo(n) == 0) {
				A++;
			} else {
				// System.out.println("Something went wrong.");
				break;
			}
		}
		return result;
	}
}