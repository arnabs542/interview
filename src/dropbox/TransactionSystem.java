package dropbox;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * 设计一个系统有 开始（无输入，返回交易码），读（输入交易码和键，返回相应值），
 * 写（输入交易码，键，值，无返回值），确认服务（输入交易码，返回成功/失败）接口
 */
public class TransactionSystem {

  private Map<String, TreeMap<UUID, Integer>> map;

  public TransactionSystem() {
    this.map = new HashMap<>();
  }

  public UUID start() {
    // transaction id is UUID
    return UUID.randomUUID();
  }

  public void put(UUID transactionId, String key, int value) {
    map.putIfAbsent(key, new TreeMap<>());
    map.get(key).put(transactionId, value);
  }

  public int get(UUID transactionId, String key) {
    if (!map.containsKey(key)) {
      return -1;
    }
    TreeMap<UUID, Integer> treeMap = map.get(key);
    UUID id = treeMap.floorKey(transactionId);
    return id != null ? treeMap.get(transactionId) : -1;
  }

  public boolean commit(int transactionID) {
    return map.values().contains(transactionID);
  }

  private void rollBack() {
    // need to rollback if commit equals false
  }
}
