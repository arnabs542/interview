package dropbox;

import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

/**
 * Things to consider:
 *
 * - what happens if all IDs used up? throws Exception or return -1
 * - release invalid ID or release id that is not allocated, how to handle? just return or throw Exception?
 */
public class IdAllocator {

  private final Queue<Integer> freeIds;
  private final Set<Integer> usedIdSet;
  private final int MAX_ID;
  // we can do it using smaller space with bitset (O(space use in bitset * 2))
  // but allocate and release will be time O(log N)
  private BitSet bitSet;
  private final int left;

  public IdAllocator(int maxId) {
    this.MAX_ID = maxId;
    this.freeIds = new ArrayDeque<>();
    for (int i = 0; i < maxId; i++) {
      freeIds.offer(i);
    }
    this.usedIdSet = new HashSet<>();
    this.bitSet = new BitSet(maxId * 2 - 1);
    this.left = findLeft();
  }

  public int allocate() {
    if (freeIds.isEmpty()) {
      return -1;
    }
    int id = freeIds.poll();
    usedIdSet.add(id);
    return id;
  }

  public void release(int id) {
    if (id < 0 || id > MAX_ID || !usedIdSet.contains(id)) {
      return;
    }
    usedIdSet.remove(id);
    freeIds.offer(id);
  }

  public boolean check(int id) {
    return !usedIdSet.contains(id);
  }

  private int findLeft() {
    int index = 1;
    while (index < MAX_ID) {
      index = 2 * index;
    }
    return index;
  }

  /**
   * Build the segment tree use bit set, the order is based on level order
   * bitset[0] is the root of the whole tree
   */
  public int allocateUpdate() {
    if (bitSet.get(1)) {
        return -1;
      }
      int index = 1;
      while (index < MAX_ID) {
        if (!bitSet.get(index * 2)) {
          index = index * 2;
        } else if (!bitSet.get(index * 2 + 1)) {
          index = index * 2 + 1;
        }
      }
      bitSet.set(index);
      updateTree(index);
      if (index - left >= 0) {
        return index - left;
      } else {
        return index - left + MAX_ID;
      }
    }

    private void updateTree(int index) {
      while (index > 0) {
        int parent = index / 2;
      if (index % 2 == 0) {
        // left child
        if (bitSet.get(index) && bitSet.get(index + 1)) {
          bitSet.set(parent);
        } else {
          bitSet.clear(parent);
        }
      } else {
        // right child
        if (bitSet.get(index) && bitSet.get(index - 1)) {
          bitSet.set(parent);
        } else {
          bitSet.clear(parent);
        }
      }
      index = parent;
    }
  }

  public void releaseUpdate(int id) {
    if (id < 0 || id > MAX_ID) {
      return;
    }
    int index = getIndex(id);
    if (bitSet.get(index)) {
      bitSet.clear(index);
      updateTree(index);
    }
  }

  public boolean checkUpdate(int id) {
    if (id < 0 || id > MAX_ID) {
      return false;
    }
    int index = getIndex(id);
    return !bitSet.get(index);
  }

  private int getIndex(int id) {
    int index = id + left;
    if (id + left >= 2 * MAX_ID) {
      index = id + left - MAX_ID;
    }
    return index;
  }

  public static void main(String[] args) {
    IdAllocator allocator = new IdAllocator(5);
    System.out.println("Generate: " + allocator.allocateUpdate());
    System.out.println("Generate: " + allocator.allocateUpdate());
    System.out.println("Generate: " + allocator.allocateUpdate());
    System.out.println("Generate: " + allocator.allocateUpdate());
    System.out.println("Generate: " + allocator.allocateUpdate());
    System.out.println("Generate: " + allocator.allocateUpdate());
    allocator.releaseUpdate(3);
    System.out.println("Generate: " + allocator.allocateUpdate());
  }
}
