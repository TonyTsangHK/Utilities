package utils.data;

import utils.math.MathUtil;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2012-11-07
 * Time: 09:37
 */
public class ListUtil {
    private ListUtil() {}

    public static <T extends Comparable<T>> ListDiff<T> compareListWithoutSorting(
            List<T> list, List<T> compareList
    ) {
        return compareListWithoutSorting(list, compareList, Comparable::compareTo);
    }

    public static <T extends Comparable<T>> ListDiff<T> compareList(
            List<T> list, List<T> compareList
    ) {
        return compareList(list, compareList, Comparable::compareTo);
    }

    public static <T extends Comparable<T>> ListDiff<T> compareSortedList(
            List<T> list, List<T> compareList
    ) {
        return compareSortedList(list, compareList, Comparable::compareTo);
    }

    public static <T> ListDiff<T> compareListWithoutSorting(
        List<T> list, List<T> compareList, Comparator<T> comparator
    ) {
        List<T> removedElements = new ArrayList<>(list), newElements = new ArrayList<>(compareList),
                sameElements = new ArrayList<>(list.size());

        Iterator<T> iter = removedElements.iterator(), cIter;

        while (iter.hasNext()) {
            T ele = iter.next();

            cIter = newElements.iterator();

            while (cIter.hasNext()) {
                T cEle = cIter.next();
                if (comparator.compare(ele, cEle) == 0) {
                    sameElements.add(ele);
                    cIter.remove();
                    iter.remove();
                    break;
                }
            }
        }

        return new ListDiff<>(newElements, removedElements, sameElements);
    }

    public static <T> ListDiff<T> compareList(
            List<T> list, List<T> compareList, Comparator<T> comparator
    ) {
        List<T> newList = new ArrayList<>(list), newCompareList = new ArrayList<>(compareList);
        Collections.sort(newList, comparator);
        Collections.sort(newCompareList, comparator);

        return compareSortedList(newList, newCompareList, comparator);
    }

    public static <T> ListDiff<T> compareSortedList(
            List<T> list, List<T> compareList, Comparator<T> comparator
    ) {
        List<T> sameElements = new ArrayList<>(list.size()),
                newElements = new ArrayList<>(compareList),
                removedElements = new ArrayList<>(list);

        if (removedElements.isEmpty() || newElements.isEmpty()) {
            return new ListDiff<>(sameElements, newElements, removedElements);
        }

        Iterator<T> iter = removedElements.iterator(), cIter = newElements.iterator();

        T ele = iter.next(), cele = cIter.next();

        while (ele != null && cele != null) {
            int cmp = comparator.compare(ele, cele);

            if (cmp > 0) {
                if (cIter.hasNext()) {
                    cele = cIter.next();
                } else {
                    break;
                }
            } else if (cmp < 0) {
                if (iter.hasNext()) {
                    ele = iter.next();
                } else {
                    break;
                }
            } else {
                sameElements.add(ele);
                iter.remove();
                cIter.remove();
                if (iter.hasNext()) {
                    ele = iter.next();
                } else {
                    break;
                }
                if (cIter.hasNext()) {
                    cele = cIter.next();
                } else {
                    break;
                }
            }
        }

        return new ListDiff<>(newElements, removedElements, sameElements);
    }

    public static <T extends Comparable<T>> boolean contains(List<T> list, List<T> compareList) {
        ListDiff<T> diff = compareList(list, compareList);

        return !diff.hasNewElements();
    }

    public static <T extends Comparable<T>> boolean intercept(List<T> list, List<T> compareList) {
        ListDiff<T> diff = compareList(list, compareList);

        return diff.hasSameElements();
    }

    public static <T extends Comparable<T>> boolean isListEquals(List<T> list, List<T> compareList) {
        return isListEquals(list, compareList, Comparable::compareTo);
    }

    public static <T extends Comparable<T>> boolean isListExactlyEquals(List<T> list, List<T> compareList) {
        return isListExactlyEquals(list, compareList, Comparable::compareTo);
    }

    public static <T> boolean contains(List<T> list, List<T> compareList, Comparator<T> comparator) {
        ListDiff<T> diff = compareList(list, compareList, comparator);

        return !diff.hasNewElements();
    }

    public static <T> boolean intercept(List<T> list, List<T> compareList, Comparator<T> comparator) {
        ListDiff<T> diff = compareList(list, compareList, comparator);

        return diff.hasSameElements();
    }

    public static <T> boolean isListEquals(List<T> list, List<T> compareList, Comparator<T> comparator) {
        if (list.size() != compareList.size()) {
            return false;
        } else {
            List<T> newList = new ArrayList<>(list), newCompareList = new ArrayList<>(compareList);

            Collections.sort(newList, comparator);
            Collections.sort(newCompareList, comparator);

            for (int i = 0; i < newList.size(); i++) {
                T ele = newList.get(i), cele = newCompareList.get(i);

                if (comparator.compare(ele, cele) != 0) {
                    return false;
                }
            }

            return true;
        }
    }

    public static <T> boolean isListExactlyEquals(List<T> list, List<T> compareList, Comparator<T> comparator) {
        if (list.size() != compareList.size()) {
            return false;
        } else {
            for (int i = 0; i < list.size(); i++) {
                T e1 = list.get(i), e2 = compareList.get(i);

                if (comparator.compare(e1, e2) != 0) {
                    return false;
                }
            }

            return true;
        }
    }

    public static <T> void reorderList(List<T> list, final List<T> refList) {
        Collections.sort(
            list, (o1, o2) -> {
                int i1 = refList.indexOf(o1), i2 = refList.indexOf(o2);
                if (i1 < i2) {
                    return -1;
                } else if (i1 > i2) {
                    return 1;
                } else {
                    return 0;
                }
            }
        );
    }

    public static <T> boolean syncList(List<T> targetList, ListDiff<T> listDiff) {
        boolean modified = false;

        if (listDiff.hasRemovedElements()) {
            targetList.removeAll(listDiff.getRemovedElements());
            modified = true;
        }

        if (listDiff.hasNewElements()) {
            targetList.addAll(listDiff.getNewElements());
            modified = true;
        }

        return modified;
    }

    public static <T> boolean syncLists(List<T> targetList, List<T> sourceList, Comparator<T> comparator) {
        return syncList(targetList, compareList(targetList, sourceList, comparator));
    }

    public static class ListDiff<T> {
        private List<T> removedElements, newElements, sameElements;

        public ListDiff(
            List<T> newElements, List<T> removedElements, List<T> sameElements
        ) {
            this.newElements = newElements;
            this.removedElements = removedElements;
            this.sameElements = sameElements;
        }

        public List<T> getRemovedElements() {
            return removedElements;
        }

        public List<T> getNewElements() {
            return newElements;
        }

        public List<T> getSameElements() {
            return sameElements;
        }

        public boolean hasRemovedElements() {
            return removedElements.size() > 0;
        }

        public boolean hasNewElements() {
            return newElements.size() > 0;
        }

        public boolean hasSameElements() {
            return sameElements.size() > 0;
        }

        public boolean isSame() {
            return newElements.isEmpty() && removedElements.isEmpty();
        }

        public boolean isNotSame() {
            return !isSame();
        }

        public int getNewElementCount() {
            return newElements.size();
        }

        public int getRemovedElementCount() {
            return removedElements.size();
        }

        public int getSameElementCount() {
            return sameElements.size();
        }
    }

    private static List<Integer> createList(int size) {
        List<Integer> list = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            list.add(MathUtil.randomInteger(0, size));
        }

        return list;
    }
}
