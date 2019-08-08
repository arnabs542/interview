package linkedin;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 *  Given  a root of a tree. The tree may be of any depth and width.
 *  Transform it in a way that each node(except probably one) would either have N or 0 children.
 *  detail please see: https://www.1point3acres.com/bbs/thread-106498-1-1.html
 */
interface CompactTreeBuilder {
  /** compact the original tree into N-ary tree */
  Node compact(Node root, int N);
}

class Node {
  int val;
  List<Node> descendants;

  Node (int val) {
    this.val = val;
    this.descendants = new ArrayList<>();
  }
}

public class CompactTree implements CompactTreeBuilder {

  @Override
  public Node compact(Node root, int n) {
    if (root == null || n == 0) {
      return root;
    }
    Deque<Node> q1 = new ArrayDeque<>();
    Deque<Node> q2 = new ArrayDeque<>();
    q1.offer(root);
    q2.offer(root);
    int currentLevel = 1;
    int nextLevel = 0;
    while (!q1.isEmpty()) {
      Node current = q1.poll();
      for (Node descendant : current.descendants) {
        q1.offer(descendant);
        q2.offer(descendant);
        nextLevel++;
      }
      if (currentLevel-- == 0) {
        currentLevel = nextLevel;
        nextLevel = 0;
      }
    }
    Node newRoot = q2.poll();
    fillTree(newRoot, q2, n);
    return newRoot;
  }

  private void fillTree(Node root, Deque<Node> queue, int n) {
    if (root == null || queue.isEmpty()) {
      return;
    }
    int count = 0;
    root.descendants = new ArrayList<>();
    while (!queue.isEmpty() && count < n) {
      root.descendants.add(queue.poll());
      count++;
    }
    for (Node descendant : root.descendants) {
      fillTree(descendant, queue, n);
    }
  }
}
