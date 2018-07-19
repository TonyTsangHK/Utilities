package utils.data

import utils.math.MathUtil
import java.util.*

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2017-07-05
 * Time: 17:02
 */
class ListUtil private constructor() {
    companion object {
        @JvmStatic
        fun <T : Comparable<T>> compareListWithoutSorting(
            list: List<T>, compareList: List<T>
        ): ListDiff<T> {
            return compareListWithoutSorting(
                list, compareList, kotlin.Comparator { o1, o2 -> o1.compareTo(o2) }
            )
        }

        @JvmStatic
        fun <T : Comparable<T>> compareList(
            list: List<T>, compareList: List<T>
        ):ListDiff<T> {
            return compareList(list, compareList, Comparator { o1, o2 -> o1.compareTo(o2) })
        }

        @JvmStatic
        fun <T : Comparable<T>>  compareSortedList(
            list:List<T>, compareList: List<T>
        ):ListDiff<T> {
            return compareSortedList(list, compareList, Comparator { o1, o2 -> o1.compareTo(o2) })
        }

        @JvmStatic
        fun <T: Comparable<T>> isListEquals(list: List<T>, compareList: List<T>): Boolean {
            return isListEquals(list, compareList, Comparator { o1, o2 -> o1.compareTo(o2) })
        }

        @JvmStatic
        fun <T: Comparable<T>> isListExactlyEquals(list: List<T>, compareList: List<T>): Boolean {
            return isListExactlyEquals(list, compareList, Comparator { o1, o2 -> o1.compareTo(o2) })
        }

        @JvmStatic
        fun <T> compareListWithoutSorting(
            list: List<T>, compareList: List<T>, comparator: Comparator<T>
        ): ListDiff<T> {
            val removedElements = ArrayList<T>(list)
            val newElements = ArrayList<T>(compareList)
            val sameElements = ArrayList<T>(list.size)

            val iter = removedElements.iterator()
            var cIter: Iterator<T>?

            while (iter.hasNext()) {
                val ele = iter.next()

                cIter = newElements.iterator()

                while (cIter.hasNext()) {
                    val cEle = cIter.next()
                    if (comparator.compare(ele, cEle) == 0) {
                        sameElements.add(ele)
                        cIter.remove()
                        iter.remove()
                        break
                    }
                }
            }

            return ListDiff<T>(newElements, removedElements, sameElements)
        }

        @JvmStatic
        fun <T> compareList(
            list: List<T>, compareList: List<T>, comparator: Comparator<T>
        ): ListDiff<T> {
            val newList = ArrayList<T>(list)
            val newCompareList = ArrayList<T>(compareList)
            Collections.sort(newList, comparator)
            Collections.sort(newCompareList, comparator)

            return compareSortedList(newList, newCompareList, comparator)
        }

        @JvmStatic
        fun <T> compareSortedList(
            list: List<T>, compareList: List<T>, comparator: Comparator<T>
        ): ListDiff<T> {
            val sameElements = ArrayList<T>()
            val newElements = ArrayList<T>(compareList)
            val removedElements = ArrayList<T>(list)

            if (removedElements.isEmpty() || newElements.isEmpty()) {
                return ListDiff(newElements = newElements, removedElements = removedElements, sameElements = sameElements)
            }

            val iter = removedElements.iterator()
            val cIter = newElements.iterator()

            var ele = iter.next()
            var cele = cIter.next()

            while (ele != null && cele != null) {
                val cmp = comparator.compare(ele, cele)

                if (cmp > 0) {
                    if (cIter.hasNext()) {
                        cele = cIter.next()
                    } else {
                        break
                    }
                } else if (cmp < 0) {
                    if (iter.hasNext()) {
                        ele = iter.next()
                    } else {
                        break
                    }
                } else {
                    sameElements.add(ele)
                    iter.remove()
                    cIter.remove()
                    if (iter.hasNext()) {
                        ele = iter.next()
                    } else {
                        break
                    }
                    if (cIter.hasNext()) {
                        cele = cIter.next()
                    } else {
                        break
                    }
                }
            }

            return ListDiff<T>(newElements, removedElements, sameElements)
        }

        @JvmStatic
        fun <T: Comparable<T>> contains(list: List<T>, compareList: List<T>): Boolean {
            val diff = compareList(list, compareList)

            return !diff.hasNewElements()
        }

        @JvmStatic
        fun <T: Comparable<T>> intercept(list: List<T>, compareList: List<T>): Boolean {
            val diff = compareList(list, compareList)

            return diff.hasSameElements()
        }

        @JvmStatic 
        fun <T> contains(list: List<T>, compareList: List<T>, comparator: Comparator<T>): Boolean {
            val diff = compareList(list, compareList, comparator)

            return !diff.hasNewElements()
        }

        @JvmStatic 
        fun <T> intercept(list: List<T>, compareList: List<T>, comparator: Comparator<T>): Boolean {
            val diff = compareList(list, compareList, comparator)

            return diff.hasSameElements()
        }

        @JvmStatic 
        fun <T> isListEquals(list: List<T>, compareList: List<T>, comparator: Comparator<T>): Boolean {
            if (list.size != compareList.size) {
                return false
            } else {
                val newList = ArrayList<T>(list)
                val newCompareList = ArrayList<T>(compareList)

                Collections.sort(newList, comparator)
                Collections.sort(newCompareList, comparator)

                for (i in 0 .. newList.size - 1) {
                    val ele = newList[i]
                    val cele = newCompareList[i]

                    if (comparator.compare(ele, cele) != 0) {
                        return false
                    }
                }

                return true
            }
        }

        @JvmStatic
        fun <T> isListExactlyEquals(list: List<T>, compareList: List<T>, comparator: Comparator<T>): Boolean {
            if (list.size != compareList.size) {
                return false
            } else {
                for (i in 0 .. list.size - 1) {
                    val e1 = list[i]
                    val e2 = compareList[i]

                    if (comparator.compare(e1, e2) != 0) {
                        return false
                    }
                }

                return true
            }
        }

        @JvmStatic 
        fun <T> reorderList(list: List<T>, refList: List<T>) {
            Collections.sort(
                list, fcn@{
                    o1, o2 ->
                    val i1 = refList.indexOf(o1)
                    val i2 = refList.indexOf(o2)
                    if (i1 < i2) {
                        return@fcn - 1
                    } else if (i1 > i2) {
                        return@fcn 1
                    } else {
                        return@fcn 0
                    }
                }
            )
        }

        @JvmStatic
        fun <T> syncList(targetList: MutableList<T>, listDiff: ListDiff<T>): Boolean {
            var modified = false

            if (listDiff.hasRemovedElements()) {
                targetList.removeAll(listDiff.removedElements)
                modified = true
            }

            if (listDiff.hasNewElements()) {
                targetList.addAll(listDiff.newElements)
                modified = true
            }

            return modified
        }

        @JvmStatic 
        fun <T> syncLists(targetList: MutableList<T>, sourceList: List<T>, comparator: Comparator<T>): Boolean {
            return syncList(targetList, compareList(targetList, sourceList, comparator))
        }

        private fun createList(size: Int): List<Int> {
            val list = ArrayList<Int>(size)

            for (i in 0 .. size-1) {
                list.add(MathUtil.randomInteger(0, size))
            }

            return list
        }
    }

    class ListDiff<T>(val newElements: List<T>, val removedElements: List<T>, val sameElements: List<T>) {
        fun hasRemovedElements(): Boolean {
            return removedElements.isNotEmpty()
        }

        fun hasNewElements(): Boolean {
            return newElements.isNotEmpty()
        }

        fun hasSameElements(): Boolean {
            return sameElements.isNotEmpty()
        }

        val isSame: Boolean
            get() = newElements.isEmpty() && removedElements.isEmpty()

        val isNotSame: Boolean
            get() = !isSame

        val newElementCount: Int
            get() = newElements.size

        val removedElementCount: Int
            get() = removedElements.size

        val sameElementCount: Int
            get() = sameElements.size
    }
}