package utils.data;

import utils.date.DateCalendar;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;

public class DataComparator {
    public static int compareObject(Comparable o1, Comparable o2, boolean asc) {
        return compareComparable(o1, o2, asc);
    }
    
    public static int compareInteger(Integer i1, Integer i2) {
        return compareInteger(i1, i2, true);
    }
    
    public static int compareInteger(Integer i1, Integer i2, boolean asc) {
        return compareComparable(i1, i2, asc);
    }
    
    public static int compareLong(Long l1, Long l2) {
        return compareLong(l1, l2, true);
    }
    
    public static int compareLong(Long l1, Long l2, boolean asc) {
        return compareComparable(l1, l2, asc);
    }
    
    public static int compareComparable(Comparable c1, Comparable c2) {
        return compareComparable(c1, c2, true);
    }
    
    public static int compareComparable(Comparable c1, Comparable c2, boolean asc) {
        if (c1 == null && c2 == null) {
            return 0;
        } else if (c1 == null) {
            return (asc)? -1 : 1;
        } else if (c2 == null) {
            return (asc)? 1 : -1;
        } else {
            if (asc) {
                return c1.compareTo(c2);
            } else {
                return c2.compareTo(c1);
            }
        }
    }
    
    public static int compareNumber(Double d1, Double d2) {
        return compareNumber(d1, d2, true);
    }
    
    public static int compareNumber(Double d1, Double d2, boolean asc) {
        return compareComparable(d1, d2, asc);
    }

    public static int compareBigDecimal(BigDecimal bigDecimal1, BigDecimal bigDecimal2) {
        return compareBigDecimal(bigDecimal1, bigDecimal2, true);
    }

    public static int compareBigDecimal(BigDecimal bigDecimal1, BigDecimal bigDecimal2, boolean asc) {
        return compareComparable(bigDecimal1, bigDecimal2, asc);
    }
    
    public static int compareBoolean(boolean b1, boolean b2) {
        return compareBoolean(b1, b2, true);
    }
    
    public static int compareBoolean(boolean b1, boolean b2, boolean asc) {
        if ((b1 && b2) || (!b1 && b2)) {
            return 0;
        } else if (b1 && !b2) {
            return -1;
        } else {
            return 1;
        }
    }
    
    public static int compareDate(DateCalendar c1, DateCalendar c2) {
        return compareDate(c1, c2, true);
    }
    
    public static int compareDate(DateCalendar c1, DateCalendar c2, boolean asc) {
        int result = -1;
        if ((c1 == null || c1.equals("")) && (c2 == null || c2.equals(""))) {
            result = 0;
        } else if (c1 == null || c1.equals("")) {
            if (asc) {
                result = -1;
            } else {
                result = 1;
            }
        } else if (c2 == null || c2.equals("")) {
            if (asc) {
                result = 1;
            } else {
                result = -1;
            }
        } else {
            if (asc) {
                result = c1.compareTo(c2);
            } else {
                result = c2.compareTo(c1);
            }
        }
        return result;
    }
    
    public static int compareString(String s1, String s2) {
        return compareString(s1, s2, true);
    }
    
    public static int compareString(String s1, String s2, boolean asc) {
        return compareComparable(s1, s2, asc);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <E> int compare(E e1, E e2, boolean asc, boolean nullAsSmaller) {
        if (e1 != null && e2 != null) {
            if (e1 instanceof Comparable && e2 instanceof Comparable) {
                int r = ((Comparable)e1).compareTo(e2);
                return (asc)? r : -r;
            } else {
                if (e1.equals(e2)) {
                    return 0;
                } else {
                    return (asc)? e1.hashCode() - e2.hashCode() : e2.hashCode() - e1.hashCode();
                }
            }
        } else {
            if (e1 == null && e2 == null) {
                return 0;
            } else if (e1 == null) {
                return (asc ^ nullAsSmaller)? 1 : -1;
            } else {
                return (asc ^ nullAsSmaller)? -1 : 1;
            }
        }
    }

    public static <K, V> int compareMap(
            Map<K, V> map1, Map<K, V> map2, boolean asc, boolean nullAsSmaller, K ... keySeqs
    ) {
        if (map1 == null && map2 == null) {
            return 0;
        } else if (map1 == null) {
            return (nullAsSmaller)? -1 : 1;
        } else if (map2 == null) {
            return (nullAsSmaller)? 1 : -1;
        } else {
            for (K key : keySeqs) {
                int cmp = compare(map1.get(key), map2.get(key), asc, nullAsSmaller);

                if (cmp != 0) {
                    return cmp;
                }
            }

            return 0;
        }
    }
    
    private static <E> int compareValueAsc(E e1, E e2) {
        if (e1 instanceof Comparable && e2 instanceof Comparable) {
            return compareComparableAsc(e1, e2);
        } else {
            return compareNonComparableAsc(e1, e2);
        }
    }
    
    private static <E> int compareValueDesc(E e1, E e2) {
        if (e1 instanceof Comparable && e2 instanceof Comparable) {
            return compareComparableDesc(e1, e2);
        } else {
            return compareNonComparableDesc(e1, e2);
        }
    }
    
    public static <E> int compareComparable(E e1, E e2) {
        return compareComparableAsc(e1, e2);
    }
    
    public static <E> int compareComparable(E e1, E e2, boolean asc) {
        return (asc)? compareComparableAsc(e1, e2) : compareComparableDesc(e1, e2);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static <E> int compareComparableAsc(E e1, E e2) {
        return ((Comparable)e1).compareTo(e2);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static <E> int compareComparableDesc(E e1, E e2) {
        return ((Comparable)e2).compareTo(e1);
    }
    
    private static <E> int compareNonComparableAsc(E e1, E e2) {
        if (e1.equals(e2)) {
            return 0;
        } else {
            return e1.hashCode() - e2.hashCode();
        }
    }
    
    private static <E> int compareNonComparableDesc(E e1, E e2) {
        if (e1.equals(e2)) {
            return 0;
        } else {
            return e2.hashCode() - e1.hashCode();
        }
    }
    
    public static int compareNull(Object o1, Object o2) {
        return compareNullTT(o1, o2);
    }
    
    public static int compareNull(Object o1, Object o2, boolean asc, boolean nullAsSmaller) {
        return (asc == nullAsSmaller)? compareNullTT(o1, o2) : compareNullTF(o1, o2);
    }
    
    private static <E> int compareNullTT(E e1, E e2) {
        if (e1 == null && e2 == null) {
            return 0;
        } else if (e1 == null) {
            return -1;
        } else {
            return 1;
        }
    }
    
    private static <E> int compareNullTF(E e1, E e2) {
        if (e1 == null && e2 == null) {
            return 0;
        } else if (e1 == null) {
            return 1;
        } else {
            return -1;
        }
    }
    
    public static <E> Comparator<E> buildComparator(final Comparator<E> comparator) {
        return buildComparator(comparator, true);
    }
    
    public static <E> Comparator<E> buildComparator(final Comparator<E> comparator, boolean nullAsSmaller) {
        if (nullAsSmaller) {
            return new Comparator<E>() {
                @Override
                public int compare(E e1, E e2) {
                    if (e1 != null && e2 != null) {
                        return comparator.compare(e1, e2);
                    } else {
                        if (e1 == null && e2 == null) {
                            return 0;
                        } else if (e1 == null) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                }
            };
        } else {
            return new Comparator<E>() {
                @Override
                public int compare(E e1, E e2) {
                    if (e1 != null && e2 != null) {
                        return comparator.compare(e1, e2);
                    } else {
                        if (e1 == null && e2 == null) {
                            return 0;
                        } else if (e1 == null) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                }
            };
        }
    }
    
    public static <E extends Comparable<E>> Comparator<E> buildSimpleComparator() {
        return new Comparator<E>() {
            @Override
            public int compare(E e1, E e2) {
                return e1.compareTo(e2);
            }
        };
    }
    
    public static <E extends Comparable<E>> Comparator<E> buildComparatorForComparable() {
        return buildComparatorForComparable(true, true);
    }
    
    public static <E extends Comparable<E>> Comparator<E> buildComparatorForComparable(boolean asc) {
        return buildComparatorForComparable(asc, true);
    }
    
    public static <E extends Comparable<E>> Comparator<E> buildComparatorForComparable(
            final boolean asc, final boolean nullAsSmaller
    ) {
        if (asc == nullAsSmaller) {
            if (asc) {
                return new Comparator<E>() {
                    @Override
                    public int compare(E e1, E e2) {
                        if (e1 != null && e2 != null) {
                            return compareComparableAsc(e1, e2);
                        } else {
                            return compareNullTT(e1, e2);
                        }
                    }
                };
            } else {
                return new Comparator<E>() {
                    @Override
                    public int compare(E e1, E e2) {
                        if (e1 != null && e2 != null) {
                            return compareComparableDesc(e1, e2);
                        } else {
                            return compareNullTT(e1, e2);
                        }
                    }
                };
            }
        } else {
            if (asc) {
                return new Comparator<E>() {
                    @Override
                    public int compare(E e1, E e2) {
                        if (e1 != null && e2 != null) {
                            return compareComparableAsc(e1, e2);
                        } else {
                            return compareNullTF(e1, e2);
                        }
                    }
                };
            } else {
                return new Comparator<E>() {
                    @Override
                    public int compare(E e1, E e2) {
                        if (e1 != null && e2 != null) {
                            return compareComparableDesc(e1, e2);
                        } else {
                            return compareNullTF(e1, e2);
                        }
                    }
                };
            }
        }
    }
    
    public static <E> Comparator<E> buildComparator() {
        return buildComparator(true, true);
    }
    
    public static <E> Comparator<E> buildComparator(boolean asc) {
        return buildComparator(asc, true);
    }
    
    public static <E> Comparator<E> buildComparator(final boolean asc, final boolean nullAsSmaller) {
        if (asc == nullAsSmaller) {
            if (asc) {
                    return new Comparator<E>() {
                        @Override
                        public int compare(E e1, E e2) {
                            if (e1 != null && e2 != null) {
                                return compareValueAsc(e1, e2);
                            } else {
                                return compareNullTT(e1, e2);
                            }
                        }
                    };
            } else {
                return new Comparator<E>() {
                    @Override
                    public int compare(E e1, E e2) {
                        if (e1 != null && e2 != null) {
                            return compareValueDesc(e1, e2);
                        } else {
                            return compareNullTT(e1, e2);
                        }
                    }
                };
            }
        } else {
            if (asc) {
                return new Comparator<E>() {
                    @Override
                    public int compare(E e1, E e2) {
                        if (e1 != null && e2 != null) {
                            return compareValueAsc(e1, e2);
                        } else {
                            return compareNullTF(e1, e2);
                        }
                    }
                };
            } else {
                return new Comparator<E>() {
                    @Override
                    public int compare(E e1, E e2) {
                        if (e1 != null && e2 != null) {
                            return compareValueDesc(e1, e2);
                        } else {
                            return compareNullTF(e1, e2);
                        }
                    }
                };
            }
        }
    }

    public static <K, V> Comparator<Map<K, V>> buildMapComparator(
            final boolean asc, final boolean nullAsSmall, final K ... keySeqs
    ) {
        return new Comparator<Map<K, V>>() {
            @Override
            public int compare(Map<K, V> map1, Map<K, V> map2) {
                return compareMap(map1, map2, asc, nullAsSmall, keySeqs);
            }
        };
    }
}
