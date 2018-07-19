package testng;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.math.Fraction;
import utils.math.MathUtil;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.testng.Assert.*;

public class TestFraction {
    private BigDecimal[] mixes, numers, denos, nMixes, nNumers, nDenos;
    
    @BeforeMethod
    public void setUp() throws Exception {
        mixes = new BigDecimal[] {
            new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"),
            new BigDecimal("1"), new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("-2.33")
        };
        nMixes = new BigDecimal[] {
            new BigDecimal("0"), new BigDecimal("1"), new BigDecimal("0"), new BigDecimal("1"),
            new BigDecimal("2"), new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("-3")
        };
        numers = new BigDecimal[] {
            new BigDecimal("2"),  new BigDecimal("2"), new BigDecimal("1"),   new BigDecimal("3"),
            new BigDecimal("10"), new BigDecimal("1"), new BigDecimal("4.5"), new BigDecimal("1.3378")
        };
        nNumers = new BigDecimal[] {
            new BigDecimal("2"),  new BigDecimal("0"), new BigDecimal("1"),   new BigDecimal("1"),
            new BigDecimal("1"), new BigDecimal("1"), new BigDecimal("1"), new BigDecimal("245909")
        };
        denos = new BigDecimal[] {
            new BigDecimal("3"), new BigDecimal("2"), new BigDecimal("2"), new BigDecimal("2"),
            new BigDecimal("9"), new BigDecimal("9"), new BigDecimal("9"), new BigDecimal("5.667")
        };
        nDenos = new BigDecimal[] {
            new BigDecimal("3"), new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("2"),
            new BigDecimal("9"), new BigDecimal("9"), new BigDecimal("2"), new BigDecimal("566700")
        };
    }
    
    @Test
    public void testFractionString() {
        String[] fractionStrings = {"2 /    3", "2/2", "1/2", "3/2", "1-10/9", "1-1/9", "2-4.5/9", "-2.33-1.3378/5.667"};
        
        for (int i = 0; i < fractionStrings.length; i++) {
            Fraction frac = new Fraction(fractionStrings[i]);
            
            assertEquals(mixes[i].compareTo(frac.getMixedNumber()), 0, "mixedNumber not match");
            assertEquals(numers[i].compareTo(frac.getNumerator()), 0, "numerator not match");
            assertEquals(denos[i].compareTo(frac.getDenominator()), 0, "denominator not match");
        }
    }
    
    @Test
    public void testFractionStringBoolean() {
        String[] fractionStrings = {"2 /    3", "2/2", "1/2", "3/2", "1-10/9", "1-1/9", "2-4.5/9", "-2.33--1.3378/5.667"};
        
        for (int i = 0; i < fractionStrings.length; i++) {
            Fraction frac = new Fraction(fractionStrings[i], true);
            
            assertEquals(nMixes[i].compareTo(frac.getMixedNumber()), 0, "mixedNumber not match");
            assertEquals(nNumers[i].compareTo(frac.getNumerator()), 0, "numerator not match");
            assertEquals(nDenos[i].compareTo(frac.getDenominator()), 0, "denominator not match");
        }
    }

    @Test
    public void testFractionInt() {
        for (int i = -99; i <= 99; i++) {
            Fraction fraction = new Fraction(i);
            
            assertEquals(fraction.getMixedNumber().compareTo(new BigDecimal(i)), 0);
            assertEquals(fraction.getNumerator().compareTo(BigDecimal.ZERO), 0);
            assertEquals(fraction.getDenominator().compareTo(BigDecimal.ONE), 0);
        }
    }

    @Test
    public void testFractionDouble() {
        for (int i = 0; i < 20000; i++) {
            double v = MathUtil.randomNumber(-999, 999);
            
            String s = String.valueOf(v);
            
            BigDecimal b = new BigDecimal(s);
            
            Fraction fraction = new Fraction(v);
            
            assertEquals(fraction.getMixedNumber().compareTo(new BigDecimal(b.toBigInteger())), 0);
            
            BigDecimal r = b.abs().remainder(BigDecimal.ONE);
            BigDecimal n = new BigDecimal(r.unscaledValue()), d = new BigDecimal(10).pow(r.scale());

            assertTrue(
                MathUtil.isDecimalEquals(
                    MathUtil.divide(fraction.getNumerator(), fraction.getDenominator()),
                    MathUtil.divide(n, d)
                )
            );
        }
    }

    @Test
    public void testFractionBigDecimal() {
        for (int i = 0; i < 20000; i++) {
            double v = MathUtil.randomNumber(-999, 999);
            
            String s = String.valueOf(v);
            
            BigDecimal b = new BigDecimal(s);
            
            Fraction fraction = new Fraction(b);

            assertTrue(
                MathUtil.isDecimalEquals(
                    fraction.getMixedNumber(),
                    new BigDecimal(b.toBigInteger())
                )
            );
            
            BigDecimal r = b.abs().remainder(BigDecimal.ONE);
            BigDecimal n = new BigDecimal(r.unscaledValue()), d = new BigDecimal(10).pow(r.scale());

            assertTrue(
                MathUtil.isDecimalEquals(
                    MathUtil.divide(fraction.getNumerator(), fraction.getDenominator()),
                    MathUtil.divide(n, d)
                )
            );
        }
    }

    @Test
    public void testFractionIntInt() {
        for (int i = 0; i <= 20000; i++) {
            int n = MathUtil.randomInteger(-999, 999), d = MathUtil.randomInteger(-999, 999);
            while (d == 0) {
                d = MathUtil.randomInteger(-999, 999);
            }
            
            Fraction f = new Fraction(n, d);

            assertTrue(
                MathUtil.isDecimalEquals(
                    f.getMixedNumber(), BigDecimal.ZERO
                )
            );
            assertEquals(f.getNumerator().intValue(), n);
            assertEquals(f.getDenominator().intValue(), d);
        }
    }

    @Test
    public void testFractionDoubleDouble() {
        for (int i = 0; i <= 20000; i++) {
            double n = MathUtil.randomNumber(-999, 999), d = MathUtil.randomNumber(-999, 999);
            while (d == 0) {
                d = MathUtil.randomNumber(-999, 999);
            }
            
            String ns = String.valueOf(n), ds = String.valueOf(d);
            
            Fraction f = new Fraction(n, d);

            assertTrue(
                MathUtil.isDecimalEquals(
                    f.getMixedNumber(), BigDecimal.ZERO
                )
            );
            assertTrue(
                MathUtil.isDecimalEquals(
                    f.getNumerator(), new BigDecimal(ns)
                )
            );
            assertTrue(
                MathUtil.isDecimalEquals(
                    f.getDenominator(), new BigDecimal(ds)
                )
            );
        }
    }

    @Test
    public void testFractionBigDecimalBigDecimal() {
        for (int i = 0; i <= 20000; i++) {
            double n = MathUtil.randomNumber(-999, 999), d = MathUtil.randomNumber(-999, 999);
            while (d == 0) {
                d = MathUtil.randomNumber(-999, 999);
            }
            
            String ns = String.valueOf(n), ds = String.valueOf(d);
            
            Fraction f = new Fraction(new BigDecimal(ns), new BigDecimal(ds));

            assertTrue(
                MathUtil.isDecimalEquals(
                    f.getMixedNumber(), BigDecimal.ZERO
                )
            );
            assertTrue(
                MathUtil.isDecimalEquals(
                    f.getNumerator(), new BigDecimal(ns)
                )
            );
            assertTrue(
                MathUtil.isDecimalEquals(
                    f.getDenominator(), new BigDecimal(ds)
                )
            );
        }
    }

    @Test
    public void testFractionIntIntBoolean() {
        for (int i = 0; i <= 20000; i++) {
            int n = MathUtil.randomInteger(-999, 999), d = MathUtil.randomInteger(-999, 999);
            while (d == 0) {
                d = MathUtil.randomInteger(-999, 999);
            }
            
            int c = MathUtil.randomInteger(1, 2);
            
            Fraction f = new Fraction(n, d, c == 1);
            
            if (c == 1) {
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getMixedNumber().remainder(BigDecimal.ONE),
                        BigDecimal.ZERO
                    )
                );
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getNumerator().remainder(BigDecimal.ONE),
                        BigDecimal.ZERO
                    )
                );
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getDenominator().remainder(BigDecimal.ONE),
                        BigDecimal.ZERO
                    )
                );
                
                BigInteger ni = f.getNumerator().toBigInteger(), di = f.getDenominator().toBigInteger();

                assertEquals(
                    MathUtil.gcd(ni, di).abs(),
                    BigInteger.ONE
                );
                assertTrue(
                    MathUtil.isDecimalEquals(
                        MathUtil.add(MathUtil.divide(f.getNumerator(), f.getDenominator()), f.getMixedNumber()),
                        MathUtil.divide(new BigDecimal(n), new BigDecimal(d))
                    )
                );
            } else {
                assertTrue(
                    MathUtil.isDecimalEquals(f.getMixedNumber(), BigDecimal.ZERO)
                );
                assertEquals(f.getNumerator().intValue(), n);
                assertEquals(f.getDenominator().intValue(), d);
            }
        }
    }
    
    @Test
    public void testFractionDoubleDoubleBoolean() {
        for (int i = 0; i <= 20000; i++) {
            double n = MathUtil.randomNumber(-999, 999), d = MathUtil.randomNumber(-999, 999);
            while (d == 0) {
                d = MathUtil.randomNumber(-999, 999);
            }
            
            String ns = String.valueOf(n), ds = String.valueOf(d);
            
            int c = MathUtil.randomInteger(1, 2);
            
            Fraction f = new Fraction(n, d, c == 1);
            
            if (c == 1) {
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getMixedNumber().remainder(BigDecimal.ONE),
                        BigDecimal.ZERO
                    )
                );
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getNumerator().remainder(BigDecimal.ONE),
                        BigDecimal.ZERO
                    )
                );
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getDenominator().remainder(BigDecimal.ONE),
                        BigDecimal.ZERO
                    )
                );
                
                BigInteger ni = f.getNumerator().toBigInteger(), di = f.getDenominator().toBigInteger();

                assertEquals(MathUtil.gcd(ni, di).abs(), BigInteger.ONE);

                assertTrue(
                    MathUtil.isDecimalEquals(
                        MathUtil.add(MathUtil.divide(f.getNumerator(), f.getDenominator()), f.getMixedNumber()),
                        MathUtil.divide(new BigDecimal(ns), new BigDecimal(ds))
                    )
                );
            } else {
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getMixedNumber(), BigDecimal.ZERO
                    )
                );
                assertTrue(
                    MathUtil.isDecimalEquals(f.getNumerator(), new BigDecimal(ns))
                );
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getDenominator(), new BigDecimal(ds)
                    )
                );
            }
        }
    }

    @Test
    public void testFractionBigDecimalBigDecimalBoolean() {
        for (int i = 0; i <= 20000; i++) {
            double n = MathUtil.randomNumber(-999, 999), d = MathUtil.randomNumber(-999, 999);
            while (d == 0) {
                d = MathUtil.randomNumber(-999, 999);
            }
            
            String ns = String.valueOf(n), ds = String.valueOf(d);
            
            int c = MathUtil.randomInteger(1, 2);
            
            Fraction f = new Fraction(new BigDecimal(ns), new BigDecimal(ds), c == 1);
            
            if (c == 1) {
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getMixedNumber().remainder(BigDecimal.ONE), BigDecimal.ZERO
                    )
                );
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getNumerator().remainder(BigDecimal.ONE), BigDecimal.ZERO
                    )
                );
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getDenominator().remainder(BigDecimal.ONE), BigDecimal.ZERO
                    )
                );
                
                BigInteger ni = f.getNumerator().toBigInteger(), di = f.getDenominator().toBigInteger();

                assertEquals(MathUtil.gcd(ni, di).abs(), BigInteger.ONE);

                assertTrue(
                    MathUtil.isDecimalEquals(
                        MathUtil.add(
                            MathUtil.divide(f.getNumerator(), f.getDenominator()),
                            f.getMixedNumber()
                        ),
                        MathUtil.divide(new BigDecimal(ns), new BigDecimal(ds))
                    )
                );
            } else {
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getMixedNumber(), BigDecimal.ZERO
                    )
                );
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getNumerator(), new BigDecimal(ns)
                    )
                );
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getDenominator(), new BigDecimal(ds)
                    )
                );
            }
        }
    }

    @Test
    public void testFractionIntIntInt() {
        for (int i = 0; i <= 20000; i++) {
            int n = MathUtil.randomInteger(-999, 999),
                d = MathUtil.randomInteger(-999, 999),
                m = MathUtil.randomInteger(-990, 999);
            
            while (d == 0) {
                d = MathUtil.randomInteger(-999, 999);
            }
            
            Fraction f = new Fraction(m, n, d);
            
            assertEquals(f.getMixedNumber().intValue(), m);
            assertEquals(f.getNumerator().intValue(), n);
            assertEquals(f.getDenominator().intValue(), d);
        }
    }

    @Test
    public void testFractionDoubleDoubleDouble() {
        for (int i = 0; i <= 20000; i++) {
            double
                n = MathUtil.randomNumber(-999, 999),
                d = MathUtil.randomNumber(-999, 999),
                m = MathUtil.randomNumber(-999, 999);
            
            while (d == 0) {
                d = MathUtil.randomNumber(-999, 999);
            }
            
            String ms = String.valueOf(m), ns = String.valueOf(n), ds = String.valueOf(d);
            
            Fraction f = new Fraction(m, n, d);

            assertTrue(
                MathUtil.isDecimalEquals(
                    f.getMixedNumber(), new BigDecimal(ms)
                )
            );
            assertTrue(
                MathUtil.isDecimalEquals(
                    f.getNumerator(), new BigDecimal(ns)
                )
            );
            assertTrue(
                MathUtil.isDecimalEquals(
                    f.getDenominator(), new BigDecimal(ds)
                )
            );
        }
    }

    @Test
    public void testFractionBigDecimalBigDecimalBigDecimal() {
        for (int i = 0; i <= 20000; i++) {
            double m = MathUtil.randomNumber(-999, 999),
                   n = MathUtil.randomNumber(-999, 999),
                   d = MathUtil.randomNumber(-999, 999);
            
            while (d == 0) {
                d = MathUtil.randomNumber(-999, 999);
            }
            
            String ms = String.valueOf(m), ns = String.valueOf(n), ds = String.valueOf(d);
            
            Fraction f = new Fraction(new BigDecimal(ms), new BigDecimal(ns), new BigDecimal(ds));

            assertTrue(
                MathUtil.isDecimalEquals(
                    f.getMixedNumber(), new BigDecimal(ms)
                )
            );
            assertTrue(
                MathUtil.isDecimalEquals(
                    f.getNumerator(), new BigDecimal(ns)
                )
            );
            assertTrue(
                MathUtil.isDecimalEquals(
                    f.getDenominator(), new BigDecimal(ds)
                )
            );
        }
    }

    @Test
    public void testFractionIntIntIntBoolean() {
        for (int i = 0; i <= 20000; i++) {
            int m = MathUtil.randomInteger(-999, 999),
                n = MathUtil.randomInteger(-999, 999),
                d = MathUtil.randomInteger(-999, 999);
            
            while (d == 0) {
                d = MathUtil.randomInteger(-999, 999);
            }
            
            int c = MathUtil.randomInteger(1, 2);
            
            Fraction f = new Fraction(m, n, d, c == 1);
            
            if (c == 1) {
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getMixedNumber().remainder(BigDecimal.ONE), BigDecimal.ZERO
                    )
                );
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getNumerator().remainder(BigDecimal.ONE), BigDecimal.ZERO
                    )
                );
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getDenominator().remainder(BigDecimal.ONE), BigDecimal.ZERO
                    )
                );
                
                BigInteger ni = f.getNumerator().toBigInteger(), di = f.getDenominator().toBigInteger();

                assertEquals(MathUtil.gcd(ni, di).abs(), BigInteger.ONE);

                assertTrue(
                    MathUtil.isDecimalEquals(
                        MathUtil.add(
                            MathUtil.divide(f.getNumerator(), f.getDenominator()),
                            f.getMixedNumber()
                        ),
                        MathUtil.add(
                            MathUtil.divide(new BigDecimal(n), new BigDecimal(d)),
                            new BigDecimal(m)
                        )
                    )
                );
            } else {
                assertEquals(f.getMixedNumber().intValue(), m);
                assertEquals(f.getNumerator().intValue(), n);
                assertEquals(f.getDenominator().intValue(), d);
            }
        }
    }
    
    @Test
    public void testFractionDoubleDoubleDoubleBoolean() {
        for (int i = 0; i <= 20000; i++) {
            double m = MathUtil.randomNumber(-999, 999),
                   n = MathUtil.randomNumber(-999, 999),
                   d = MathUtil.randomNumber(-999, 999);
            
            while (d == 0) {
                d = MathUtil.randomNumber(-999, 999);
            }
            
            String ms = String.valueOf(m), ns = String.valueOf(n), ds = String.valueOf(d);
            
            int c = MathUtil.randomInteger(1, 2);
            
            Fraction f = new Fraction(m, n, d, c == 1);
            
            if (c == 1) {
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getMixedNumber().remainder(BigDecimal.ONE), BigDecimal.ZERO
                    )
                );
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getNumerator().remainder(BigDecimal.ONE), BigDecimal.ZERO
                    )
                );
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getDenominator().remainder(BigDecimal.ONE), BigDecimal.ZERO
                    )
                );
                
                BigInteger ni = f.getNumerator().toBigInteger(), di = f.getDenominator().toBigInteger();
                
                assertEquals(MathUtil.gcd(ni, di).abs(), BigInteger.ONE);

                assertTrue(
                    MathUtil.isDecimalEquals(
                        MathUtil.add(
                            MathUtil.divide(f.getNumerator(), f.getDenominator()), f.getMixedNumber()
                        ),
                        MathUtil.add(
                            MathUtil.divide(new BigDecimal(ns), new BigDecimal(ds)), new BigDecimal(ms)
                        )
                    )
                );
            } else {
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getMixedNumber(), new BigDecimal(ms)
                    )
                );
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getNumerator(), new BigDecimal(ns)
                    )
                );
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getDenominator(), new BigDecimal(ds)
                    )
                );
            }
        }
    }

    @Test
    public void testFractionBigDecimalBigDecimalBigDecimalBoolean() {
        for (int i = 0; i <= 20000; i++) {
            double m = MathUtil.randomNumber(-999, 999),
                   n = MathUtil.randomNumber(-999, 999),
                   d = MathUtil.randomNumber(-999, 999);
            
            while (d == 0) {
                d = MathUtil.randomNumber(-999, 999);
            }
            
            String ms = String.valueOf(m), ns = String.valueOf(n), ds = String.valueOf(d);
            
            int c = MathUtil.randomInteger(1, 2);
            
            Fraction f = new Fraction(new BigDecimal(ms), new BigDecimal(ns), new BigDecimal(ds), c == 1);
            
            if (c == 1) {
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getMixedNumber().remainder(BigDecimal.ONE),
                        BigDecimal.ZERO
                    )
                );
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getNumerator().remainder(BigDecimal.ONE),
                        BigDecimal.ZERO
                    )
                );
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getDenominator().remainder(BigDecimal.ONE), BigDecimal.ZERO
                    )
                );
                
                BigInteger ni = f.getNumerator().toBigInteger(), di = f.getDenominator().toBigInteger();

                assertEquals(MathUtil.gcd(ni, di).abs(), BigInteger.ONE);
                assertTrue(
                    MathUtil.isDecimalEquals(
                        MathUtil.add(
                            MathUtil.divide(f.getNumerator(), f.getDenominator()), f.getMixedNumber()
                        ),
                        MathUtil.add(
                            MathUtil.divide(new BigDecimal(ns), new BigDecimal(ds)), new BigDecimal(ms)
                        )
                    )
                );
            } else {
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getMixedNumber(), new BigDecimal(ms)
                    )
                );
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getNumerator(), new BigDecimal(ns)
                    )
                );
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getDenominator(), new BigDecimal(ds)
                    )
                );
            }
        }
    }

    @Test
    public void testIsProper() {
        Fraction f = new Fraction(1, 2);
        assertTrue(f.isProper());
        
        f = new Fraction(2, 2);
        assertFalse(f.isProper());
        
        f = new Fraction(3, 2);
        assertFalse(f.isProper());
        
        f = new Fraction(1, 1, 2);
        assertTrue(f.isProper());
        
        f = new Fraction(1, 2, 2);
        assertFalse(f.isProper());
        
        f = new Fraction(1, 3, 2);
        assertFalse(f.isProper());
        
        f = new Fraction(1, -2);
        assertTrue(f.isProper());
        
        f = new Fraction(2, -2);
        assertFalse(f.isProper());
        
        f = new Fraction(3, -2);
        assertFalse(f.isProper());
        
        f = new Fraction(-1, 2);
        assertTrue(f.isProper());
        
        f = new Fraction(-2, 2);
        assertFalse(f.isProper());
        
        f = new Fraction(-3, 2);
        assertFalse(f.isProper());
        
        f = new Fraction(-1, -2);
        assertTrue(f.isProper());
        
        f = new Fraction(-2, -2);
        assertFalse(f.isProper());
        
        f = new Fraction(-3, -2);
        assertFalse(f.isProper());
    }

    @Test
    public void testIsNormalized() {
        Fraction f = new Fraction(1, 2);
        assertTrue(f.isNormalized());
        
        f = new Fraction(2, 2);
        assertFalse(f.isNormalized());
        
        f = new Fraction(3, 2);
        assertFalse(f.isNormalized());
        
        f = new Fraction(1, 1, 2);
        assertTrue(f.isNormalized());
        
        f = new Fraction(1, 2, 2);
        assertFalse(f.isNormalized());
        
        f = new Fraction(1, 3, 2);
        assertFalse(f.isNormalized());
        
        f = new Fraction(1, -2);
        assertFalse(f.isNormalized());
        
        f = new Fraction(2, -2);
        assertFalse(f.isNormalized());
        
        f = new Fraction(3, -2);
        assertFalse(f.isNormalized());
        
        f = new Fraction(-1, 2);
        assertTrue(f.isNormalized());
        
        f = new Fraction(-2, 2);
        assertFalse(f.isNormalized());
        
        f = new Fraction(-3, 2);
        assertFalse(f.isNormalized());
        
        f = new Fraction(-1, -2);
        assertFalse(f.isNormalized());
        
        f = new Fraction(-2, -2);
        assertFalse(f.isNormalized());
        
        f = new Fraction(-3, -2);
        assertFalse(f.isNormalized());
        
        f = new Fraction(13, 26);
        assertFalse(f.isNormalized());
        
        f = new Fraction(-13, 26);
        assertFalse(f.isNormalized());
        
        f = new Fraction(13, -26);
        assertFalse(f.isNormalized());
        
        f = new Fraction(-13, 26);
        assertFalse(f.isNormalized());
        
        f = new Fraction(13, 13, 26);
        assertFalse(f.isNormalized());
        
        f = new Fraction(15, -13, 26);
        assertFalse(f.isNormalized());
        
        f = new Fraction(15, 13, -26);
        assertFalse(f.isNormalized());
        
        f = new Fraction(15, -13, 26);
        assertFalse(f.isNormalized());
        
        f = new Fraction(13, 17);
        assertTrue(f.isNormalized());
        
        f = new Fraction(1.3, 1.7);
        assertFalse(f.isNormalized());
    }

    @Test
    public void testGetDecimalValue() {
        for (int i = 0; i <= 20000; i++) {
            int m = MathUtil.randomInteger(-999, 999),
                n = MathUtil.randomInteger(-999, 999),
                d = MathUtil.randomInteger(-999, 999);
            
            while (d == 0) {
                d = MathUtil.randomInteger(-999, 999);
            }
            
            int c = MathUtil.randomInteger(1, 2);
            
            Fraction f = new Fraction(m, n, d, c == 1);

            assertTrue(
                MathUtil.isDecimalEquals(
                    f.getDecimalValue(),
                    MathUtil.add(
                        MathUtil.divide(new BigDecimal(n), new BigDecimal(d)), new BigDecimal(m)
                    )
                )
            );
        }
    }
    
    @Test
    public void testToImproperFraction() {
        for (int i = 0; i <= 20000; i++) {
            int m = MathUtil.randomInteger(-999, 999),
                n = MathUtil.randomInteger(-999, 999),
                d = MathUtil.randomInteger(-999, 999);
            
            while (d == 0) {
                d = MathUtil.randomInteger(-999, 999);
            }
            
            int c = MathUtil.randomInteger(1, 2);
            
            Fraction f = new Fraction(m, n, d, c == 1);
            Fraction ifr = f.toImproperFraction();

            assertTrue(
                MathUtil.isDecimalEquals(
                    f.getWholeNumerator(), ifr.getNumerator()
                )
            );
        }
    }

    @Test
    public void testToProperFraction() {
        for (int i = 0; i <= 20000; i++) {
            int n = MathUtil.randomInteger(-999, 999),
                d = MathUtil.randomInteger(-999, 999);
            
            while (d == 0) {
                d = MathUtil.randomInteger(-999, 999);
            }
            
            Fraction f = new Fraction(n, d, false);
            Fraction ifr = f.toProperFraction();
            
            if (Math.abs(n) >= Math.abs(d)) {
                assertTrue(
                    ifr.getNumerator().abs().compareTo(ifr.getDenominator().abs()) < 0
                );
                assertTrue(
                    MathUtil.isDecimalEquals(
                        ifr.getWholeNumerator(), f.getNumerator()
                    )
                );
            } else {
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getNumerator(), ifr.getNumerator()
                    )
                );
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f.getDenominator(), ifr.getDenominator()
                    )
                );
            }
        }
    }
    
    private interface ExceptionProcess {
        void process();
    }
    
    private boolean testException(ExceptionProcess proc, Class<? extends Exception> exceptionClass) {
        try {            
            proc.process();
        } catch (Exception e) {
            return e.getClass() == exceptionClass;
        }
        return exceptionClass == null;
    }
    
    @Test
    public void testFractionNumberFormatException() {
        assertTrue(
            testException(
                new ExceptionProcess() {
                    @Override
                    public void process() {
                        new Fraction(1, 2, 0);
                    }
                },
                NumberFormatException.class
            )
        );
        
        assertTrue(
            testException(
                new ExceptionProcess() {
                    @Override
                    public void process() {
                        new Fraction(1, 2, 3);
                    }
                },
                null
            )
        );
        
        assertTrue(
            testException(
                new ExceptionProcess() {
                    @Override
                    public void process() {
                        new Fraction(0, 35).toReciprocal();
                    }
                }, NumberFormatException.class
            )
        );
    }
    
    @Test
    public void testToReciprocal() {
        for (int i = 0; i <= 20000; i++) {
            int n = MathUtil.randomInteger(-999, 999),
                d = MathUtil.randomInteger(-999, 999);
            
            while (d == 0) {
                d = MathUtil.randomInteger(-999, 999);
            }
            
            while (n == 0) {
                n = MathUtil.randomInteger(-999, 999);
            }
            
            Fraction f  = new Fraction(n, d, false);
            Fraction rp = f.toReciprocal();

            assertTrue(
                MathUtil.isDecimalEquals(
                    f.getWholeNumerator(), rp.getDenominator()
                )
            );
            assertTrue(
                MathUtil.isDecimalEquals(
                    f.getDenominator(), rp.getWholeNumerator()
                )
            );
        }
    }

    @Test
    public void testAbs() {
        for (int i = 0; i <= 20000; i++) {
            int n = MathUtil.randomInteger(-999, 999),
                d = MathUtil.randomInteger(-999, 999);
            
            while (d == 0) {
                d = MathUtil.randomInteger(-999, 999);
            }
            
            Fraction f  = new Fraction(n, d, false);

            assertTrue(f.abs().getDecimalValue().signum() >= 0);
        }
    }

    @Test
    public void testSignum() {
        for (int i = 0; i <= 20000; i++) {
            int n = MathUtil.randomInteger(-999, 999),
                d = MathUtil.randomInteger(-999, 999);
            
            while (d == 0) {
                d = MathUtil.randomInteger(-999, 999);
            }
            
            Fraction f  = new Fraction(n, d, false);
            
            if (n == 0) {
                assertEquals(f.signum(), 0);
            } else {
                assertEquals(f.signum() < 0, (n < 0 && d > 0) || (n >= 0 && d < 0));
            }
        }
    }

    @Test
    public void testGetWholeNumerator() {
        for (int i = 0; i <= 20000; i++) {
            int m = MathUtil.randomInteger(-999, 999),
                n = MathUtil.randomInteger(-999, 999),
                d = MathUtil.randomInteger(-999, 999);
            
            while (d == 0) {
                d = MathUtil.randomInteger(-999, 999);
            }
            
            Fraction f  = new Fraction(m, n, d, false);

            assertTrue(
                MathUtil.isDecimalEquals(
                    f.getWholeNumerator(), new BigDecimal(m * d + n)
                )
            );
        }
    }

    @Test
    public void testAdd() {
        for (int i = 0; i <= 20000; i++) {
            int m1 = MathUtil.randomInteger(-999, 999),
                n1 = MathUtil.randomInteger(-999, 999),
                d1 = MathUtil.randomInteger(-999, 999);
            
            int m2 = MathUtil.randomInteger(-999, 999),
                n2 = MathUtil.randomInteger(-999, 999),
                d2 = MathUtil.randomInteger(-999, 999);
            
            while (d1 == 0) {
                d1 = MathUtil.randomInteger(-999, 999);
            }
            
            while (d2 == 0) {
                d2 = MathUtil.randomInteger(-999, 999);
            }
            
            Fraction f1  = new Fraction(m1, n1, d1, false), f2 = new Fraction(m2, n2, d2, false);
            
            Fraction sum = f1.add(f2);
            
            assertTrue(sum.isNormalized());

            assertTrue(
                MathUtil.isDecimalEquals(
                    MathUtil.divide(
                        f1.getWholeNumerator().multiply(f2.getDenominator()).add(
                            f2.getWholeNumerator().multiply(f1.getDenominator())
                        ),
                        f1.getDenominator().multiply(f2.getDenominator())
                    ),
                    sum.getDecimalValue()
                )
            );
        }
    }

    @Test
    public void testSubtract() {
        for (int i = 0; i <= 20000; i++) {
            int m1 = MathUtil.randomInteger(-999, 999),
                n1 = MathUtil.randomInteger(-999, 999),
                d1 = MathUtil.randomInteger(-999, 999);
            
            int m2 = MathUtil.randomInteger(-999, 999),
                n2 = MathUtil.randomInteger(-999, 999),
                d2 = MathUtil.randomInteger(-999, 999);
            
            while (d1 == 0) {
                d1 = MathUtil.randomInteger(-999, 999);
            }
            
            while (d2 == 0) {
                d2 = MathUtil.randomInteger(-999, 999);
            }
            
            Fraction f1  = new Fraction(m1, n1, d1, false), f2 = new Fraction(m2, n2, d2, false);
            
            Fraction diff = f1.subtract(f2);
            
            assertTrue(diff.isNormalized());

            assertTrue(
                MathUtil.isDecimalEquals(
                    MathUtil.divide(
                        f1.getWholeNumerator().multiply(f2.getDenominator()).subtract(
                            f2.getWholeNumerator().multiply(f1.getDenominator())
                        ),
                        f1.getDenominator().multiply(f2.getDenominator())
                    ),
                    diff.getDecimalValue()
                )
            );
        }
    }

    @Test
    public void testMultiply() {
        for (int i = 0; i <= 20000; i++) {
            int m1 = MathUtil.randomInteger(-999, 999),
                n1 = MathUtil.randomInteger(-999, 999),
                d1 = MathUtil.randomInteger(-999, 999);
            
            int m2 = MathUtil.randomInteger(-999, 999),
                n2 = MathUtil.randomInteger(-999, 999),
                d2 = MathUtil.randomInteger(-999, 999);
            
            while (d1 == 0) {
                d1 = MathUtil.randomInteger(-999, 999);
            }
            
            while (d2 == 0) {
                d2 = MathUtil.randomInteger(-999, 999);
            }
            
            Fraction f1  = new Fraction(m1, n1, d1, false), f2 = new Fraction(m2, n2, d2, false);
            
            Fraction product = f1.multiply(f2);
            
            assertTrue(product.isNormalized());

            assertTrue(
                MathUtil.isDecimalEquals(
                    MathUtil.divide(
                        f1.getWholeNumerator().multiply(f2.getWholeNumerator()),
                        f1.getDenominator().multiply(f2.getDenominator())
                    ),
                    product.getDecimalValue()
                )
            );
        }
    }

    @Test
    public void testDivide() {
        boolean dividedByZeroTested = false;
        for (int i = 0; i <= 20000 || !dividedByZeroTested; i++) {
            int m1 = MathUtil.randomInteger(-999, 999),
                n1 = MathUtil.randomInteger(-999, 999),
                d1 = MathUtil.randomInteger(-999, 999);
            
            int m2 = MathUtil.randomInteger(-999, 999),
                n2 = MathUtil.randomInteger(-999, 999),
                d2 = MathUtil.randomInteger(-999, 999);

            while (n1 == 0) {
                n1 = MathUtil.randomInteger(-999, 999);
            }

            while (n2 == 0) {
                n2 = MathUtil.randomInteger(-999, 999);
            }
            
            while (d1 == 0) {
                d1 = MathUtil.randomInteger(-999, 999);
            }
            
            while (d2 == 0) {
                d2 = MathUtil.randomInteger(-999, 999);
            }
            
            Fraction f1  = new Fraction(m1, n1, d1, false), f2 = new Fraction(m2, n2, d2, false);

            boolean expectingNumberFormatException = false;

            if (f2.getDecimalValue().compareTo(BigDecimal.ZERO) == 0) {
                expectingNumberFormatException = true;
            }

            Fraction quotient = null;

            try {
                quotient = f1.divide(f2);

                if (expectingNumberFormatException) {
                    fail("Expecting number format exception, but none was thrown!");
                }
            } catch (NumberFormatException nfe) {
                if (expectingNumberFormatException) {
                    dividedByZeroTested = true;
                }
            }

            if (!expectingNumberFormatException) {
                assertTrue(quotient.isNormalized());

                assertTrue(
                    MathUtil.isDecimalEquals(
                        MathUtil.divide(
                            f1.getWholeNumerator().multiply(f2.getDenominator()),
                            f1.getDenominator().multiply(f2.getWholeNumerator())
                        ),
                        quotient.getDecimalValue()
                    )
                );
            } else {
                if (!dividedByZeroTested) {
                    fail("Expecting number format exception, but never caught one!");
                }
            }
        }
    }
    
    @Test
    public void testPow() {
        Fraction f = new Fraction("9/16");
        
        assertEquals(f.pow(2), new Fraction("81/256"));
        
        assertEquals(f.pow(0.5), new Fraction("3/4"));
        
        assertEquals(f.pow(0), new Fraction("1"));
        
        assertEquals(f.pow(-1), new Fraction("16/9"));
        
        // Huge fraction power not tested
    }
    
    @Test
    public void testEqualsObject() {
        for (int i = 0; i <= 20000; i++) {
            int m1 = MathUtil.randomInteger(-999, 999),
                n1 = MathUtil.randomInteger(-999, 999),
                d1 = MathUtil.randomInteger(-999, 999);
            
            while (d1 == 0) {
                d1 = MathUtil.randomInteger(-999, 999);
            }
            
            Fraction f1  = new Fraction(m1, n1, d1, false), f2 = new Fraction(m1, n1*2, d1*2, false),
                nf1 = f1.normalize(), nf2 = f2.normalize();
            
            if (f1.equals(f2)) {
                assertTrue(
                    MathUtil.isDecimalEquals(
                        nf1.getNumerator(), nf2.getNumerator()
                    )
                );
                assertTrue(
                    MathUtil.isDecimalEquals(
                        nf1.getMixedNumber(), nf2.getMixedNumber()
                    )
                );
                assertTrue(
                    MathUtil.isDecimalEquals(
                        nf1.getDenominator(), nf2.getDenominator()
                    )
                );
            } else {
                assertFalse(
                    MathUtil.isDecimalEquals(
                        nf1.getNumerator(), nf2.getNumerator()
                    ) &&
                    MathUtil.isDecimalEquals(
                        nf1.getMixedNumber(), nf2.getMixedNumber()
                    ) &&
                    MathUtil.isDecimalEquals(
                        nf1.getDenominator(), nf2.getDenominator()
                    )
                );
            }
            
            assertTrue(f1.equals(f2));
        }
    }
    
    @Test
    public void testCompareTo() {
        for (int i = 0; i <= 20000; i++) {
            int m1 = MathUtil.randomInteger(-999, 999),
                n1 = MathUtil.randomInteger(-999, 999),
                d1 = MathUtil.randomInteger(-999, 999);
            
            int m2 = MathUtil.randomInteger(-999, 999),
                n2 = MathUtil.randomInteger(-999, 999),
                d2 = MathUtil.randomInteger(-999, 999);
            
            while (d1 == 0) {
                d1 = MathUtil.randomInteger(-999, 999);
            }
            
            while (d2 == 0) {
                d2 = MathUtil.randomInteger(-999, 999);
            }
            
            Fraction f1  = new Fraction(m1, n1, d1, false), f2 = new Fraction(m2, n2, d2, false);
            
            int r = f1.compareTo(f2);
            
            if (r == 0) {
                assertTrue(
                    MathUtil.isDecimalEquals(
                        f1.getWholeNumerator().multiply(f2.getDenominator()),
                        f2.getWholeNumerator().multiply(f1.getDenominator())
                    )
                );
            } else if (r > 0) {
                assertTrue(
                    f1.getWholeNumerator().multiply(f2.getDenominator()).compareTo(
                        f2.getWholeNumerator().multiply(f1.getDenominator())
                    ) > 0
                );
            } else {
                assertTrue(
                    f1.getWholeNumerator().multiply(f2.getDenominator()).compareTo(
                        f2.getWholeNumerator().multiply(f1.getDenominator())
                    ) < 0
                );
            }
        }
    }
}
