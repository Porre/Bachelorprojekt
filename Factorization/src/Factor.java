import java.math.BigInteger;
import java.util.ArrayList;

/***
 * Combines trial division, Pollards p-1 algorithm and Lenstras elliptic curve method
 * to factor a BigInteger.
 * @author Orhoj
 *
 */

public class Factor {
	
	private BigInteger trialIterations;
	private int pollardsMax;
	private int lenstrasMax;
	private int curves;
	
	// Holds all factors of the factorization
	private ArrayList<BigInteger> factors;
	
	public Factor(BigInteger iterations, int pollards_max, int lenstras_max, int number_curves) {
		trialIterations = iterations;
		pollardsMax = pollards_max;
		lenstrasMax = lenstras_max;
		curves = number_curves;
		factors = new ArrayList<BigInteger>();
	}
	
	public ArrayList<BigInteger> getFactors() {
		return factors;
	}
	
	public void lenstraFactor(BigInteger n) {
		BigInteger factor;
		
		// If the number is a probable prime, we just return.
		if (MillerRabin.isProbablePrime(n, 20)) {
			factors.add(n);
			return;
		}
		
		Lenstra l = new Lenstra();
		factor = l.factor(n, lenstrasMax, curves);
		
		// If we found a factor - that is if factor != n
		if (!factor.equals(n)) {
			if (!MillerRabin.isProbablePrime(n, 20)) {
				lenstraFactor(factor);
			} else {
				factors.add(factor);
			}
			
			if (!MillerRabin.isProbablePrime(n.divide(factor), 20)) {
				lenstraFactor(n.divide(factor));
			} else {
				factors.add(n.divide(factor));
			}
			return;
		}
	}
	
	public void pollardFactor(BigInteger n) {
		BigInteger factor;
		
		// If the number is a probable prime, we just return.
		if (MillerRabin.isProbablePrime(n, 20)) {
			factors.add(n);
			return;
		}
		
		Pollard p = new Pollard(pollardsMax);
		factor = p.factor(n);
		
		// If we found a factor - that is if factor != n
		if (!factor.equals(n)) {
			if (!MillerRabin.isProbablePrime(n, 20)) {
				pollardFactor(factor);
			} else {
				factors.add(factor);
			}
			
			if (!MillerRabin.isProbablePrime(n.divide(factor), 20)) {
				pollardFactor(n.divide(factor));
			} else {
				factors.add(n.divide(factor));
			}
			return;
		}
		
	}
	
	public void trialFactor(BigInteger n) {
		BigInteger factor;
		
		// If the number is a probable prime, we just return.
		if (MillerRabin.isProbablePrime(n, 20)) {
			factors.add(n);
			return;
		}
	
		Trial t = new Trial();
		factor = t.factor(n, trialIterations);
		
		if (!factor.equals(n)) {
			if (!MillerRabin.isProbablePrime(n, 20)) {
				trialFactor(factor);
			} else {
				factors.add(factor);
			}
			
			if (!MillerRabin.isProbablePrime(n.divide(factor), 20)) {
				trialFactor(n.divide(factor));
			} else {
				factors.add(n.divide(factor));
			}
			return;
		}
	}
	
	public void findFactor(BigInteger n) {
		
		// If the number is a probable prime, we just return.
		if (MillerRabin.isProbablePrime(n, 20)) {
			factors.add(n);
			return;
		}
		
		BigInteger factor;
		
		Trial t = new Trial();
		factor = t.factor(n, trialIterations);
		
		// If we found a factor - that is if factor != n
		if (!factor.equals(n)) {
			if (!MillerRabin.isProbablePrime(n, 20)) {
				findFactor(factor);
			} else {
				factors.add(factor);
			}
			
			if (!MillerRabin.isProbablePrime(n.divide(factor), 20)) {
				findFactor(n.divide(factor));
			} else {
				factors.add(n.divide(factor));
			}
			return;
		}
		
		Pollard p = new Pollard(pollardsMax);
		factor = p.factor(n);
		
		// If we found a factor - that is if factor != n
		if (!factor.equals(n)) {
			if (!MillerRabin.isProbablePrime(n, 20)) {
				findFactor(factor);
			} else {
				factors.add(factor);
			}
			
			if (!MillerRabin.isProbablePrime(n.divide(factor), 20)) {
				findFactor(n.divide(factor));
			} else {
				factors.add(n.divide(factor));
			}
			return;
		}
		
		Lenstra l = new Lenstra();
		factor = l.factor(n, lenstrasMax, curves);	// 100 means we try till k = lcm[1, 2, ..., 100]
		
		// If we found a factor - that is if factor != n
		if (!factor.equals(n)) {
			if (!MillerRabin.isProbablePrime(n, 20)) {
				findFactor(factor);
			} else {
				factors.add(factor);
			}
			
			if (!MillerRabin.isProbablePrime(n.divide(factor), 20)) {
				findFactor(n.divide(factor));
			} else {
				factors.add(n.divide(factor));
			}
			return;
		}
	}
}