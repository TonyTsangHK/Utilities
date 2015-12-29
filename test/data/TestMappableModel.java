package data;

import utils.data.Mappable;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2014-06-30
 * Time: 12:55
 */
public class TestMappableModel implements Mappable<String, Object> {
    private String stringValue;
    private int integerValue;
    private BigDecimal bigDecimalValue;

    private Set<String> keyset;

    private void ensureKeyset() {
        if (keyset == null) {
            keyset = new HashSet<String>();
            keyset.add("stringValue");
            keyset.add("integerValue");
            keyset.add("bigDecimalValue");
        }
    }

    public TestMappableModel() {
        this.stringValue = "";
        this.integerValue = 0;
        this.bigDecimalValue = BigDecimal.ZERO;
    }

    public TestMappableModel(String stringValue, int integerValue, BigDecimal bigDecimalValue) {
        this.stringValue = stringValue;
        this.integerValue = integerValue;
        this.bigDecimalValue = bigDecimalValue;
    }

    @Override
    public Set<String> keyset() {
        ensureKeyset();
        return keyset;
    }

    @Override
    public Collection<Object> values() {
        List<Object> results = new ArrayList<Object>();

        results.add(stringValue);
        results.add(integerValue);
        results.add(bigDecimalValue);

        return results;
    }

    @Override
    public Object getAsMapValue(String key) {
        if ("stringValue".equals(key)) {
            return stringValue;
        } else if ("integerValue".equals(key)) {
            return integerValue;
        } else if ("bigDecimalValue".equals(key)) {
            return bigDecimalValue;
        } else {
            return null;
        }
    }

    @Override
    public void putAsMapValue(String key, Object value) {
        if ("stringValue".equals(key)) {
            this.stringValue = (String) value;
        } else if ("integerValue".equals(key)) {
            this.integerValue = (Integer) value;
        } else if ("bigDecimalValue".equals(key)) {
            this.bigDecimalValue = (BigDecimal) value;
        }
    }

    @Override
    public void putAsMapValues(Map<String, Object> mapValues) {
        for (String key : mapValues.keySet()) {
            putAsMapValue(key, mapValues.get(key));
        }
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("stringValue", stringValue);
        map.put("integerValue", integerValue);
        map.put("bigDecimalValue", bigDecimalValue);

        return map;
    }

    public String getStringValue() {
        return stringValue;
    }

    public int getIntegerValue() {
        return integerValue;
    }

    public BigDecimal getBigDecimalValue() {
        return bigDecimalValue;
    }

    @Override
    public String toString() {
        return "stringValue: " + stringValue + ", integerValue: " + integerValue + ", bigDecimalValue: " + bigDecimalValue;
    }
}
