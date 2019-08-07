package linkedin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class BestRetainCache<K, T extends BestRetainCache.Rankable> {

  private final int entriesToRetain;
  private final HashMap<K, T> cache;
  private final DataSource<K,T> ds;
  private final TreeMap<Long, Set<K>> rankMap;

  /** Constructor with a data source (assumed to be slow) and a cache size */
  public BestRetainCache(DataSource<K, T> ds, int entriesToRetain) {
    //implement here
    this.ds = ds;
    this.entriesToRetain = entriesToRetain;
    this.cache = new HashMap<>();
    this.rankMap = new TreeMap<>();
  }

  /** Gets some data. If possible, retrieves it from cache to be fast. If the data is not cached,
   *  retrieves it from the data source. If the cache is full, attempt to cache the returned data,
   *  evicting the T with lowest rank among the ones that it has available
   *  If there is a tie, the cache may choose any T with lowest rank to evict.
   */
  public T get(K key) {
    //impliment here
    if (cache.containsKey(key)) {
      return cache.get(key);
    } else {
      return getFromDataSource(key);
    }
  }

  private T getFromDataSource(K key) {
    if (cache.size() > entriesToRetain) {
      removeKey();
    }
    T value = ds.get(key);
    cache.put(key, value);
    long rank = value.getRank();
    if (!rankMap.containsKey(rank)) {
      rankMap.put(rank, new HashSet<>());
    }
    rankMap.get(rank).add(key);
    return value;
  }

  private void removeKey() {
    Map.Entry<Long, Set<K>> entry = rankMap.firstEntry();
    K key = entry.getValue().iterator().next();
    entry.getValue().remove(key);
    cache.remove(key);
    if (entry.getValue().size() == 0) {
      rankMap.remove(entry.getKey());
    }
  }

  /**
   * For reference, here are the Rankable and DataSource interfaces.
   * You do not need to implement them, and should not make assumptions
   * about their implementations.
   */
  public interface Rankable {
    /**
     * Returns the Rank of this object, using some algorithm and potentially
     * the internal state of the Rankable.
     */
    long getRank();
  }
  public interface DataSource<K, T extends Rankable> {
    T get(K key);
  }
}
