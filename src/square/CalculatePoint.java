package square;

/**
 * 抓动物算积分游戏， A 抓了 3 头牛，2头猪
 * 积分是按照 抓到动物的头数的排名来算积分， 如果有三个玩家，第一名就是三分
 *
 * 最后算出每个人的积分，并且打印出来
 *
 * // Player . Cows . Pigs . Total
 * // A .         3 .       2 .
 * // B .         5         1
 * // C .         4         3
 *
 * // Player . Cows . Pigs . Total
 * // A .         3(1)        2(2)  3.
 * // B .         5(3)        1(1)  4
 * // C .         4(2)        3(3)  5
 *
 *
 * Followup 是 如果有重复怎么handle，
 * // Player . Cows . Pigs . Total
 * // A .       3 .         2 .
 * // B  .    3       ‍‌‌‍‍‍‌‌‌‌‌‌‍‍  1
 * // C .     4        3
 *
 * A 和 B 每人都拿到 （1 +2）=  1.5 分
 */
public class CalculatePoint {
}
