package lyft;

public class MultiStream {

  interface Stream {
    int[] read(int len);
  }

  public MultiStream() {

  }

  public int[] read(int len) {
    return new int[len];
  }

  public void add(Stream stream) {

  }

  public void remove(Stream stream) {

  }
}
