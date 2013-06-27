import java.math.BigInteger;
import java.util.Random;

/*
 * Miller-Rabin test for (probable) primality.
 * Code based on the pseudo-code from CLRS.
 */

public class MillerRabin {

	private final static BigInteger zero = new BigInteger("0");
	private final static BigInteger one = new BigInteger("1");
	private final static BigInteger two = new BigInteger("2");
	private final static BigInteger three = new BigInteger("3");
	
	/**
	 * Finds a probable prime by generating random numbers
	 * until the test returns TRUE.
	 */
	
	public static BigInteger probablePrime(int bitLength, int s) {
		Random rnd = new Random();
		BigInteger x;
		do {
			x = new BigInteger(bitLength, rnd);
		} while (!isProbablePrime(x, s));
		return x;
	}

	/**
	 * Returns TRUE if n is a probable prime after 
	 * s attempts of finding a witness for n's 
	 * compositeness. If it finds a witness it returns FALSE.
	 */
	
	public static boolean isProbablePrime(BigInteger n, int s) {
		
		// Rule out special cases
		if (n.equals(zero))
			return false;
		if (n.equals(one))
			return false;
		if (n.equals(two))
			return true;
		if (n.equals(three))
			return true;
		if (n.mod(two).equals(zero))
			return false;
	
		Random rnd = new Random();	
		
		for (int j = 1; j < s; j++) {
			
			// Random integer between 1 and n-1 (inclusive)
			BigInteger a;
			do {
				a = new BigInteger(n.bitLength(), rnd);
			} while (a.compareTo(one) <= 0 || a.compareTo(n.subtract(one)) >= 0);

			if (isWitness(a, n)) 
				return false; // n is a composite
		}
		return true; // n is probably a prime
	}
	
	/**
	 * Returns TRUE if integer a witness' that n is a composite,
	 * otherwise returns FALSE.
	 */
	
	public static boolean isWitness(BigInteger a, BigInteger n) {
		
		int t = 1;
		BigInteger u = n.subtract(one).divide(two);
		while (u.mod(two).equals(zero)) {
			u = u.divide(two);
			t++;
		}
		
		BigInteger x = a.modPow(u, n);
		
		for (int i = 1; i <= t; i++) {
			BigInteger x_new = x.multiply(x).mod(n);
			if (x_new.equals(one) && !x.equals(one) && !x.equals(n.subtract(one))) 
				return true;
			x = x_new;
		}
		return !x.equals(one);
	}
}