package lyft;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

/**
 * 设计excel, A1=B1+C1, B1=D1+1这种情况如何处理
 * 是A=B+C或A=B-C的格式，支持+-两种就行了，主要是这里B和C可以是常数或者表达式的，A必须是表达式
 * 如果当前的一个表达式，比如A=B+C，造成冲突的话，则直接ignore这个表达式。处理方法的话是topological sorting
 */

class Equation {
  private final static String DELIMITER = "\\|";
  private String position;
  private int initValue;
  private Map<String, Integer> positionToSign;

  public Equation(String s) {
    String[] parts = s.split(DELIMITER);
    position = parts[0].trim();
    equationBuilder(parts[1]);
  }

  private void equationBuilder(String equation) {
    this.positionToSign = new HashMap<>();
    initValue = 0;
    equation.trim();
    StringBuilder curPosition = new StringBuilder();

    int curNum = 0;
    boolean isNum = true;
    boolean isPositive = true;
    for (int i = 0; i < equation.length(); ++i) {
      if (equation.charAt(i) == ' ') {
        continue;
      }
      if (Character.isAlphabetic(equation.charAt(i))) {
        isNum = false;
        curPosition.append(equation.charAt(i));
      } else if (Character.isDigit(equation.charAt(i))) {
        if (isNum) {
          curNum *= 10;
          curNum += equation.charAt(i) - '0';
        } else {
          curPosition.append(equation.charAt(i));
        }
      } else {
        // c in [+-]
        if (isNum) {
          initValue += isPositive ? curNum : -curNum;
        } else {
          positionToSign.put(curPosition.toString(), isPositive ? 1 : -1);
        }
        isPositive = equation.charAt(i) == '+';
        curPosition = new StringBuilder();
        curNum = 0;
        isNum = true;
      }
    }
    // add the final one into the equation
    if (isNum) {
      initValue += isPositive ? curNum : -curNum;
    } else {
      positionToSign.put(curPosition.toString(), isPositive ? 1 : -1);
    }
    System.out.println("builder: " + positionToSign.toString());
  }

  @Override
  public String toString() {
    StringBuilder res = new StringBuilder();
    res.append("Position: ")
        .append(position)
        .append(". ")
        .append("InitialValue: ")
        .append(initValue)
        .append(". ")
        .append("Dependencies: ")
        .append(positionToSign.toString());
    return res.toString();
  }
  public Collection<String> getDependency() {
    return positionToSign.keySet();
  }

  public int getValue(Map<String, Integer> positionToInteger) {
    int res = initValue;
    for (String position : positionToSign.keySet()) {
      res += positionToSign.get(position) * positionToInteger.get(position);
    }
    return res;
  }

  public String getPosition() {
    return position;
  }
}

public class SimpleExcel {

  private final Map<String, Integer> positionToRes;

  public SimpleExcel() {
    this.positionToRes = new HashMap<>();
  }

  public Map<String, Integer> solveEquations(List<String> rawEquations) {
    Map<String, Equation> positionToEquation = new HashMap<>();
    Map<String, List<String>> dependencies = new HashMap<>();
    Map<String, Integer> solvedDependencyCount = new HashMap<>();
    Queue<Equation> queue = new ArrayDeque<>();
    for (String eq : rawEquations) {
      Equation equation = new Equation(eq);
      positionToEquation.put(equation.getPosition(), equation);
      if (!equation.getDependency().isEmpty()) {
        for (String pre : equation.getDependency()) {
          dependencies.computeIfAbsent(pre, key -> new LinkedList<>()).add(equation.getPosition());
        }
        solvedDependencyCount.put(equation.getPosition(), equation.getDependency().size());
      } else {
        queue.offer(equation);
      }
    }

    Equation cur;
    while (!queue.isEmpty()) {
      cur = queue.poll();
      positionToRes.put(cur.getPosition(), cur.getValue(positionToRes));
      if (dependencies.containsKey(cur.getPosition())) {
        for (String dep : dependencies.get(cur.getPosition())) {
          solvedDependencyCount.put(dep, solvedDependencyCount.get(dep) - 1);
          if (solvedDependencyCount.get(dep) == 0) {
            queue.offer(positionToEquation.get(dep));
          }
        }
      }
    }
    if (positionToRes.size() != rawEquations.size()) {
      throw new IllegalArgumentException("Equations are not valid");
    }
    System.out.println("result: " + positionToRes.toString());
    return positionToRes;
  }

  public int getValue(String position) {
    return positionToRes.get(position);
  }

  public static void main(String[] args) throws FileNotFoundException {
    File file = new File("/Users/bozhou/Dropbox/interview/src/lyft/excelinput.txt");
    Scanner sc = new Scanner(file);
    List<String> equations = new ArrayList<>();
    while (sc.hasNextLine()) {
      String input = sc.nextLine();
      equations.add(input);
    }
    sc.close();

    System.out.println(equations.toString());
    SimpleExcel se = new SimpleExcel();
    System.out.println(se.solveEquations(equations));
    System.out.println(se.getValue("A1"));
    System.out.println(se.getValue("A3"));
    System.out.println(se.getValue("A4"));
  }
}
