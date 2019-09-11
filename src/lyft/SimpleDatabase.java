package lyft;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * In-Memory Key-Value Database
 * Lyft 60 min round
 * You are to build a data structure for storing integers. You will not persist the database to disk,
 * you will store the data in memory.
 * For simplicity's sake, instead of dealing with multiple clients and communicating over the network,
 * your program will
 * Your database should accept the following commands.
 *
 * SET <name> <value>
 * Set the variable name to the value value. Neither variable names nor values will contain spaces.
 *
 * GET <name>
 * Print out the value of the variable name, or `NULL` if that variable is not set.
 *
 * UNSET <name>
 * Unset the variable name, making it just like that variable was never set.
 *
 * NUMWITHVALUE <value>
 * Print out the number of variables that are currently set to value. If no variables equal that value, print 0.
 *
 * END
 * Exit the program. Your program will always receive this as its last command.
 *
 * Once your database accepts the above commands and is tested and works, implement commands below
 * Open a new transaction block. Transaction blocks can be nested (`BEGIN` can be issued inside of an existing
 * block) but you should get non-nested transactions working first before starting on nested.
 * A `GET` within a transaction returns the latest value by any command. Any data command that is run outside
 * of a transaction block should commit immediately.
 *
 * ROLLBACK
 * Undo all of the commands issued in the most recent transaction block, and close the block.
 * Print nothing if successful, or print `NO TRANSACTION` if no transaction is in progress.
 *
 * COMMIT
 * Close all open transaction blocks, permanently applying the changes made in them.
 * Print nothing if successful, or print `NO TRANSACTION` if no transaction is in progress.
 * Your output should contain the output of the GET and NUMWITHVALUE commands. GET will print out the
 * value of the specified key, or NULL. NUMWITHVALUE will return the number of keys which have the specified value.
 */
public class SimpleDatabase {

  class Transaction {
    String operation;
    String key;
    int value;

    Transaction(String operation, String key, int value) {
      this.operation = operation;
      this.key = key;
      this.value = value;
    }

    Transaction(String operation, String key) {
      this.operation = operation;
      this.key = key;
    }
  }

  private final Map<String, Integer> map;
  private final Map<Integer, Set<String>> countMap;
  private Deque<Transaction> transactionStack;
  private boolean hasOpenBlock;

  public SimpleDatabase() {
    this.map = new HashMap<>();
    this.countMap = new HashMap<>();
    this.transactionStack = new ArrayDeque<>();
    this.hasOpenBlock = false;
  }

  public void operate(String line) {
    String[] commands = line.split(",");
    String operation = commands[0];
    switch (operation) {
      case "BEGIN": begin();
        break;
      case "COMMIT": commit();
        break;
      case "ROLLBACK": rollback();
        break;
      case "SET": set(commands[1], Integer.valueOf(commands[2]));
        break;
      case "UNSET": unset(commands[1]);
        break;
      case "NUMWITHVALUE": numWithValue(Integer.valueOf(commands[1]));
        break;
      case "END": System.out.println("Exit the program");
        break;
      default: System.out.println("Please enter a valid command");
        break;
    }
  }

  public void set(String key, int value) {
    set(key, value, true);
  }

  public void set(String key, int value, boolean undo) {
    if (map.containsKey(key)) {
      int previous = map.get(key);
      map.put(key, value);
      if (previous != value && undo) {
        prepareRollback(new Transaction("set", key, previous));
      }
    } else {
      map.put(key, value);
      prepareRollback(new Transaction("unset", key));
    }
    Set<String> countSet = countMap.getOrDefault(value, new HashSet<>());
    countSet.add(key);
    countMap.put(value, countSet);
  }

  public Integer get(String key) {
    return map.get(key);
  }

  public void unset(String key) {
    unset(key, true);
  }

  public void unset(String key, boolean undo) {
    if (map.containsKey(key)) {
      for (int value : countMap.keySet()) {
        Set<String> set = countMap.get(value);
        set.remove(key);
      }
      int previous = map.get(key);
      if (undo) {
        prepareRollback(new Transaction("set", key, previous));
      }
      map.remove(key);
    }
  }

  private void prepareRollback(Transaction transaction) {
    if (hasOpenBlock) {
      transactionStack.push(transaction);
    }
  }

  public int numWithValue(int value) {
    return countMap.get(value).size();
  }

  private void begin() {
    hasOpenBlock = true;
  }

  public void commit() {
    if (transactionStack.isEmpty()) {
      System.out.println("NO TRANSACTION");
    } else {
      //TODO: close all open transaction blocks, apply changes
      transactionStack = new ArrayDeque<>();
      hasOpenBlock = false;
    }
  }

  public void rollback() {
    if (!hasOpenBlock) {
      System.out.println("NO TRANSACTION");
    } else {
      Transaction lastTrans = transactionStack.pop();
      if (lastTrans.operation.equals("set")) {
        set(lastTrans.key, lastTrans.value, false);
      } else if (lastTrans.operation.equals("unset")) {
        unset(lastTrans.key, false);
      }
    }
  }

  public static void main(String[] args) {
    SimpleDatabase sd = new SimpleDatabase();
    sd.set("A", 2);
    sd.set("B", 2);
    sd.set("C", 3);
    sd.set("A", 3);
    sd.set("A", 6);
    sd.set("C", 2);
    System.out.println("A: " + sd.get("A"));
    System.out.println(sd.numWithValue(2));
    System.out.println(sd.numWithValue(3));
    System.out.println(sd.numWithValue(6));
    sd.unset("A");
    System.out.println(sd.numWithValue(2));
    System.out.println(sd.numWithValue(3));
    System.out.println(sd.numWithValue(6));

    SimpleDatabase db = new SimpleDatabase();
    db.set("A", 2);
    db.begin();
    db.set("A", 3);
    db.begin();
    db.set("A", 10);
    db.rollback();
    System.out.println("A: " + db.get("A"));
    db.rollback();
    db.commit();
    System.out.println("A: " + db.get("A"));
  }
}
