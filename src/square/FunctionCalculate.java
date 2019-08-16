package square;

/**
 * 给一个函数(只有+-，没有乘除)，让你写程序实现。要求extensible，不能hardcode
 * f(x) = 4x, if x < -2,
 *       = 2x + 1, if -2 <= x < 3,
 *       = x - 6, if x >= 3
 * Followup:
 * 在给一个：
 * g(x) = -3x, if x < -1,
 *        = 2x + 4, if -1 <= x < 4,
 *        = 10, x >= 4
 * implement f(x) + g(x).
 * 例如，
 * f(x) + g(x) = 4x - 3x = x, if x < -2
 * = 2x + 1 -3x = -x + 1, if -2 <=  x < -1
 * = 2x + 1 + 2x + 4 = 4x + 5, if -1 <= x < 3
 * = x - 6 + 2x + 4 = x - 2, if 3 <= x < 4,
 * = 10, if x >= 4,
 * 要求用OOP, ie
 * Function fx();
 * Function gx();
 * Function func = fx.add(gx());
 * int res = func.getResult();
 */
public class FunctionCalculate {
  // Merge interval
}
