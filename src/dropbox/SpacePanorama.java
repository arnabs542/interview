package dropbox;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * NASA selects Dropbox as its official partner, and we’re tasked with managing
 * a panorama for the universe. The Hubble telescope (or some other voyager we
 * have out there) will occasionally snap a photo of a sector of the universe,
 * and transmit it to us. You are to help write a data structure to manage this.
 * For the purpose of this problem, assume that the observable universe has been .
 * divided into 2D sectors. Sectors are indexed by x- and y-coordinates.
 */
public class SpacePanorama {

  private class Sector {
    int column;
    int row;
    String path;
    Sector next;

    public Sector(int column, int row) {
      this.row = row;
      this.column = column;
      this.path = "";
      this.next = null;
    }

    public Sector(int column, int row, String path) {
      this.row = row;
      this.column = column;
      this.path = path;
      this.next = null;
    }
  }

  int numOfRows;
  int numOfCols;
  String basePath;
  /** The following are added */
  int capacity;
  int size;
  Sector head;
  Sector tail;
  // Key: sector; Value: previous sector
  Map<Sector, Sector> map;

  public SpacePanorama(int capacity, int numOfRows, int numOfColumns, String basePath) {
    this.capacity = capacity;
    this.numOfRows = numOfRows;
    this.numOfCols = numOfColumns;
    this.basePath = basePath;
    this.size = 0;
    this.head = new Sector(-1, -1);
    this.tail = head;
    this.map = new HashMap<>();
  }

  public void update(int row, int col, Image image) {
    Sector current = null;
    // Check whether current exist
    // TODO: why not use map.containsKey()?
    for (Sector sector : map.keySet()) {
      if (sector.row == row && sector.column == col) {
        current = sector;
        break;
      }
    }

    String imagePath = basePath + hash(image);
    if (current != null) {
      Sector previous = map.get(current);
      if (previous.next != null) {
        moveToTail(previous);
        tail.path = imagePath;
        return;
      }
      tail.path = imagePath;
      return;
    }

    //TODO: can optimal here
    if (size < capacity) {
      current = new Sector(row, col, imagePath);
      tail.next = current;
      map.put(current, tail);
      tail = current;
      size++;
      return;
    }

    current = new Sector(row, col, imagePath);
    tail.next = current;
    map.put(current, tail);
    tail = current;

    map.remove(head.next);
    head.next = head.next.next;
    map.put(head.next, head);
  }

  private void moveToTail(Sector previous) {
    Sector current = previous.next;
    previous.next = current.next;
    map.put(current.next, previous);
    tail.next = current;
    map.put(current, tail);
    tail = current;
    current.next = null;
  }

  public Image fetch(int row, int col) {
    Sector result = null;
    for (Sector sector : map.keySet()) {
      if (sector.row == row && sector.column == col) {
        result = sector;
        break;
      }
    }

    if (result != null) {
      String path = map.get(result).path;
      return read_file(path);
    }

    return null;
  }

  public int[] getStalest() {
    return new int[]{head.row, head.column};
  }

  /* return unique string given i‍‌‌‍‍‍‌‌‌‌‌‌‍‍mage */
  private String hash(Image image) {
    return image.toString();
  }

  /* 假设如果已经给了api用来read/write imge
   *  这个我有点记不清了 but 这个不重要就就是一些I/O api 他会给出
   */
  private Image read_file(String pathOfImage) {
    //return Image;
    return null;
  }

  private void save_file(Image image){

  }
}
