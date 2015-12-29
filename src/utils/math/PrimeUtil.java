package utils.math;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class PrimeUtil {
    private PrimeUtil() {}
    
    public static List<Integer> getPrimeListWithin(int limit) {
        List<Integer> primeList = new ArrayList<Integer>();
        
        boolean[] s = getPrimeArray(limit);
        for (int i = 0; i < s.length; i++) {
            if (s[i]) {
                primeList.add(i);
            }
        }
        return primeList;
    }
    
    public static List<PrimeResult> checkPrimes(Collection<Integer> nums) {
        List<PrimeResult> resultList = new ArrayList<PrimeResult>(nums.size());
        Integer max = null;
        for (Integer num : nums) {
            if (max == null) {
                max = num;
            } else if (max.intValue() < num.intValue()) {
                max = num;
            }
            resultList.add(new PrimeResult(num.intValue()));
        }
        
        boolean[] s = getPrimeArray(max.intValue());
        
        for (PrimeResult primeResult : resultList) {
            primeResult.setResult(s[primeResult.getValue()]);
        }
        return resultList;
    }
    
    public static PrimeResult[] checkPrimes(int ... nums) {
        PrimeResult[] results = new PrimeResult[nums.length];
        int max = -1;
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            if (max < num) {
                max = num;
            }
            results[i] = new PrimeResult(num);
        }
        
        boolean[] s = getPrimeArray(max);
        for (PrimeResult primeResult : results) {
            primeResult.setResult(s[primeResult.getValue()]);
        }
        return results;
    }
    
    public static boolean[] getPrimeArray(int limit) {
        boolean[] s = new boolean[limit+1];
        if (s.length > 2) {
            s[2] = true;
        }
        if (s.length > 3) {
            s[3] = true;
        }
        
        int rt = (int)Math.sqrt(limit);
        int n;
        
        for (int x = 1; x <= rt; x++) {
            for (int y = 1; y <= rt; y++) {
                n = 4*x*x+y*y;
                if (n <= limit && (n % 12 == 1 || n % 12 == 5)) {
                    s[n] ^= true;
                }
                n = 3*x*x+y*y;
                if (n <= limit && n % 12 == 7) {
                    s[n] ^= true;
                }
                n = 3*x*x-y*y;
                if (x > y && n <= limit && n % 12 == 11) {
                    s[n] ^= true;
                }
            }
        }
        
        for (int i = 5; i <= rt; i++) {
            if (s[i]) {
                int sq = i*i;
                for (int j = sq; j <= limit; j+=sq) {
                    s[j] = false;
                }
            }
        }
        return s;
    }
    
    public static boolean isPrime(int number) {
        if (number == 1) {
            return false;
        }
        if (number == 2) {
            return true;
        }
        if (number % 2 == 0) {
            return false;
        }
        int n = (int)Math.round(Math.sqrt(number));
        for (int i = 3; i <= n; i += 2) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean millerRabinTest(BigInteger n, int k) {
        // Skip 2
        if (n.compareTo(new BigInteger("2")) == 0) {
            return true;
        }
        
        BigInteger a;
        BigInteger m = n.subtract(BigInteger.ONE);
        boolean isPrime = true;
        
        if (n.compareTo(BigInteger.ONE) == 0) return false;
        if (n.mod(BigInteger.valueOf(2)).intValue() == 0) return false;       
      
        for (int i = 0; i < k; i++) {
            a = randNumber(1000);
            if (a.compareTo(m) > 0) a = a.mod(m);
            if (a.compareTo(BigInteger.ZERO) == 0) a = BigInteger.ONE;
           
            if (isWitness(a, n)) return false;
        }
        return isPrime;
    }
    
    private static boolean isWitness(BigInteger a, BigInteger n) {
        int s;
        BigInteger apow;
        BigInteger firstEqua, secondEqua;
        BigInteger m = n.subtract(BigInteger.ONE);
        BigInteger d;
        s = factorOfTwo(m);
        d = m.divide(BigInteger.valueOf((int)Math.pow(2, s)));  

        firstEqua = modExpo(a, d, n);
        if (firstEqua.intValue() == 1 || firstEqua.compareTo(m) == 0 ) return false;      
      
        for (int r = 0; r < s; r++) {
            apow = d.multiply(BigInteger.valueOf((int)Math.pow(2, r)));
            secondEqua = modExpo(a, apow, n);
            if (secondEqua.intValue() == -1 || secondEqua.compareTo(m) == 0) {              
                return false;
            }
        }
        return true;
    }
    
    private static BigInteger modExpo(BigInteger aBigInt, BigInteger pow, BigInteger n) {
        BigInteger e = pow;
        BigInteger a = aBigInt;
        BigInteger result = BigInteger.ONE;
        while (e.compareTo(BigInteger.ZERO) > 0 ) {
            if (e.and(BigInteger.ONE).compareTo(BigInteger.ONE) == 0 ){              
                result = (result.multiply(a)).mod(n);
            }          
            e = e.shiftRight(1);
            a = (a.multiply(a)).mod(n);
        }
        return result;
    }
    
    private static int factorOfTwo(BigInteger aNumber) {
        int i = 0;
        BigInteger myNumber = aNumber;
        BigInteger myRemainer;
        while (true) {          
            myRemainer = myNumber.mod(BigInteger.valueOf(2));
            if (myRemainer.compareTo(BigInteger.ZERO) != 0 ) break;
            myNumber = myNumber.divide(BigInteger.valueOf(2));
            i++;
        }
        return i;
    }
    
    public static BigInteger randNumber(int numBits) {
        BigInteger aBigInterger = new BigInteger(numBits, new Random(System.currentTimeMillis()));
        aBigInterger = aBigInterger.abs();
        return aBigInterger;
    }
    
    public static class PrimeResult {
        private int value;
        private boolean result;
        
        public PrimeResult(int v) {
            this(v, false);
        }
        
        public PrimeResult(int v, boolean isPrime) {
            this.value = v;
            this.result = isPrime;
        }
        
        public void setResult(boolean result) {
            this.result = result;
        }
        
        public int getValue() {
            return value;
        }
        
        public boolean getResult() {
            return result;
        }
        
        public boolean isPrime() {
            return result;
        }
    }
}
