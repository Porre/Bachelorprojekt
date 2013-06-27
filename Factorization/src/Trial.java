import java.math.BigInteger;

/**
 * Naive factoring method using trial division.
 */

public class Trial {
	
	public BigInteger factor(BigInteger n, BigInteger iterations) {
		
		for (BigInteger i = BigInteger.valueOf(2); i.compareTo(iterations) < 0; i = i.add(BigInteger.ONE)) {
			while (n.mod(i).compareTo(BigInteger.ZERO) == 0) {
				// Return with the first factor we find
				return i; // we return with the first factor.
				// System.out.println(i + " ");
				// n = n.divide(i);
			}
		}
		
		if (n.compareTo(BigInteger.ONE) > 0) {
			return n; // System.out.println(n);
		} 
		
		return n;
	}
	
	public BigInteger full_factor(BigInteger n) {
		for (BigInteger i = BigInteger.valueOf(2); i.multiply(i).compareTo(n) < 0; i = i.add(BigInteger.ONE)) {
			while (n.mod(i).compareTo(BigInteger.ZERO) == 0) {
				// Return with the first factor we find
				return i; // we return with the first factor.
				// System.out.println(i + " ");
				// n = n.divide(i);
			}
		}
		
		if (n.compareTo(BigInteger.ONE) > 0) {
			return n; // System.out.println(n);
		} 
		
		return n;
	}
}