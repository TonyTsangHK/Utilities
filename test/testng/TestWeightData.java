package testng;

import static org.testng.Assert.*;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.data.WeightData;
import utils.date.*;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2014-09-22
 * Time: 15:38
 */
public class TestWeightData {
    private WeightData largeWeight, mediumWeight, smallWeight, zeroWeight,
        negativeSmallWeight, negativeMediumWeight, negativeLargeWeight;

    @BeforeMethod
    public void setUp() {
        // 0, 0
        zeroWeight = new WeightData();

        // 10623, 15
        largeWeight = new WeightData(9999, new BigDecimal("9999"));
        // 211, 7
        mediumWeight = new WeightData(new BigDecimal("199"), new BigDecimal("199"));
        // 10, 10
        smallWeight = new WeightData(10, 10);

        // -10623, -15
        negativeLargeWeight = largeWeight.negate();
        // -211, -7
        negativeMediumWeight = mediumWeight.negate();
        // -10, -10
        negativeSmallWeight = smallWeight.negate();
    }

    private String translateWeight(WeightData weightData) {
        BigDecimal pd = weightData.getPound(), oz = weightData.getOz();

        boolean pdIsZero = pd.compareTo(BigDecimal.ZERO) == 0, ozIsZero = oz.compareTo(BigDecimal.ZERO) == 0;

        if (pdIsZero && ozIsZero) {
            return "0 pd";
        } else if (pdIsZero) {
            return oz + " oz";
        } else if (ozIsZero) {
            return pd + " pd";
        } else {
            return pd + " pd, " + oz + " oz";
        }
    }

    private void expectGreater(WeightData w1, WeightData w2) {
        assertTrue(w1.compareTo(w2) > 0, translateWeight(w1) + " should be greater than " + translateWeight(w2));
    }

    private void expectSmaller(WeightData w1, WeightData w2) {
        assertTrue(w1.compareTo(w2) < 0, translateWeight(w1) + " should be smaller than " + translateWeight(w2));
    }

    private void expectEqual(WeightData w1, WeightData w2) {
        assertTrue(w1.compareTo(w2) == 0, translateWeight(w1) + " should be equal to " + translateWeight(w2));
    }

    @Test
    public void testCompare() {
        expectEqual(largeWeight, largeWeight);
        expectEqual(mediumWeight, mediumWeight);
        expectEqual(smallWeight, smallWeight);

        expectGreater(largeWeight, mediumWeight);
        expectGreater(mediumWeight, smallWeight);
        expectGreater(smallWeight, zeroWeight);
        expectGreater(zeroWeight, negativeSmallWeight);
        expectGreater(negativeSmallWeight, negativeMediumWeight);
        expectGreater(negativeMediumWeight, negativeLargeWeight);

        expectSmaller(negativeLargeWeight, negativeMediumWeight);
        expectSmaller(negativeMediumWeight, negativeSmallWeight);
        expectSmaller(negativeSmallWeight, zeroWeight);
        expectSmaller(zeroWeight, smallWeight);
        expectSmaller(smallWeight, mediumWeight);
        expectSmaller(mediumWeight, largeWeight);
    }

    @Test
    public void testNegate() {
        expectEqual(largeWeight, negativeLargeWeight.negate());
        expectEqual(mediumWeight, negativeMediumWeight.negate());
        expectEqual(smallWeight, negativeSmallWeight.negate());
    }

    @Test
    public void testAdd() {
        expectEqual(zeroWeight, negativeSmallWeight.add(smallWeight));
        expectEqual(zeroWeight, smallWeight.add(negativeSmallWeight));
        expectEqual(largeWeight, zeroWeight.add(largeWeight));

        expectGreater(largeWeight, negativeSmallWeight.add(largeWeight));
        expectSmaller(smallWeight, negativeSmallWeight.add(mediumWeight));
        expectGreater(zeroWeight, negativeSmallWeight.add(negativeLargeWeight));
        expectGreater(zeroWeight, negativeSmallWeight.add(negativeSmallWeight));
    }

    @Test
    public void testSubtract() {
        expectEqual(zeroWeight, negativeSmallWeight.subtract(negativeSmallWeight));
        expectEqual(zeroWeight, smallWeight.subtract(smallWeight));
        expectEqual(largeWeight, largeWeight.subtract(zeroWeight));

        expectGreater(largeWeight, negativeSmallWeight.subtract(largeWeight));
        expectGreater(smallWeight, negativeSmallWeight.subtract(mediumWeight));
        expectSmaller(zeroWeight, negativeSmallWeight.subtract(negativeLargeWeight));
        expectGreater(zeroWeight, negativeSmallWeight.subtract(smallWeight));
    }

    @Test
    public void testGetPound() {
        assertEquals(BigDecimal.ZERO, zeroWeight.getPound());

        // 10623, 15
        assertEquals(new BigDecimal("10623"), largeWeight.getPound());
        // 211, 7
        assertEquals(new BigDecimal("211"), mediumWeight.getPound());
        // 10, 10
        assertEquals(new BigDecimal("10"), smallWeight.getPound());

        // -10623, -15
        assertEquals(new BigDecimal("-10623"), negativeLargeWeight.getPound());
        // -211, -7
        assertEquals(new BigDecimal("-211"), negativeMediumWeight.getPound());
        // -10, -10
        assertEquals(new BigDecimal("-10"), negativeSmallWeight.getPound());

        // After add
        assertEquals(BigDecimal.ZERO, largeWeight.add(negativeLargeWeight).getPound());
        assertEquals(BigDecimal.ZERO, mediumWeight.add(negativeMediumWeight).getPound());
        assertEquals(BigDecimal.ZERO, smallWeight.add(negativeSmallWeight).getPound());

        // After subtract
        assertEquals(BigDecimal.ZERO, largeWeight.subtract(new WeightData(largeWeight.getPound(), null)).getPound());
        assertEquals(BigDecimal.ZERO, mediumWeight.subtract(new WeightData(mediumWeight.getPound(), null)).getPound());
        assertEquals(BigDecimal.ZERO, smallWeight.subtract(new WeightData(smallWeight.getPound(), null)).getPound());
    }

    @Test
    public void testGetOz() {
        assertEquals(BigDecimal.ZERO, zeroWeight.getOz());

        // 10623, 15
        assertEquals(new BigDecimal("15"), largeWeight.getOz());
        // 211, 7
        assertEquals(new BigDecimal("7"), mediumWeight.getOz());
        // 10, 10
        assertEquals(new BigDecimal("10"), smallWeight.getOz());

        // -10623, -15
        assertEquals(new BigDecimal("-15"), negativeLargeWeight.getOz());
        // -211, -7
        assertEquals(new BigDecimal("-7"), negativeMediumWeight.getOz());
        // -10, -10
        assertEquals(new BigDecimal("-10"), negativeSmallWeight.getOz());

        // After add
        assertEquals(BigDecimal.ZERO, largeWeight.add(negativeLargeWeight).getOz());
        assertEquals(BigDecimal.ZERO, mediumWeight.add(negativeMediumWeight).getOz());
        assertEquals(BigDecimal.ZERO, smallWeight.add(negativeSmallWeight).getOz());

        // After subtract
        assertEquals(BigDecimal.ZERO, largeWeight.subtract(new WeightData(null, largeWeight.getOz())).getOz());
        assertEquals(BigDecimal.ZERO, mediumWeight.subtract(new WeightData(null, mediumWeight.getOz())).getOz());
        assertEquals(BigDecimal.ZERO, smallWeight.subtract(new WeightData(null, smallWeight.getOz())).getOz());
    }
}
