package utils.random

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2016-11-15
 * Time: 17:19
 */

/**
 * Random value with weight
 */
data class WeightedRandomValue<E>(val value: E, val weight: Int)