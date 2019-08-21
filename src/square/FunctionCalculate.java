package square;

import java.util.ArrayList;
import java.util.List;

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
 * = x - 6 + 10, if x >= 4,
 * 要求用OOP, ie
 * Function fx();
 * Function gx();
 * Function func = fx.add(gx());
 * int res = func.getResult();
 *
 * Followup 2: 如何optimize? 我回答的是把range，funcs 都abstract 成class/object，而不是用vector{0,1} 来代替；
 * 可以把range和func 合并成一个‍‌‌‍‍‍‌‌‌‌‌‌‍‍class；还有用self-balance tree to search instead of linearing scan;
 */
public class FunctionCalculate {

  // Merge interval
  static class Function {
    private final List<int[]> intervals; // {{INT_MIN, -2}, {-2, 3}, {3, INT_MAX}}
    private final List<int[]> funcs; // {{4, 0}, {2, 1}, {1, -6}}

    Function(List<int[]>intervals, List<int[]> funcs) {
      this.intervals = intervals;
      this.funcs = funcs;
    }

    int getResult(int x) {
      return x;
    }

    Function addFunction(Function other) {
      return this;
    }
  }

  public static void main(String[] args) {
    List<int[]> intervals = new ArrayList<>();
    intervals.add(new int[]{Integer.MIN_VALUE, -2});
    intervals.add(new int[]{-2, 3});
    intervals.add(new int[]{3, Integer.MAX_VALUE});

    List<int[]> funcs = new ArrayList<>();
    funcs.add(new int[]{4, 0});
    funcs.add(new int[]{2, 1});
    funcs.add(new int[]{1, -6});

    Function fx = new Function(intervals, funcs);

    List<int[]> intervals2 = new ArrayList<>();
    intervals2.add(new int[]{Integer.MIN_VALUE, -1});
    intervals2.add(new int[]{-1, 4});
    intervals2.add(new int[]{4, Integer.MAX_VALUE});

    List<int[]> funcs2 = new ArrayList<>();
    funcs2.add(new int[]{-3, 0});
    funcs2.add(new int[]{2, 4});
    funcs2.add(new int[]{0, 10});

    Function gx = new Function(intervals2, funcs2);

    Function result = fx.addFunction(gx);
    System.out.println("Result for value 5 is: " + result.getResult(5));
  }
}
