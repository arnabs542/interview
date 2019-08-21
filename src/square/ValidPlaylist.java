package square;

import java.util.List;

/**
 * 有一个class叫Song, 里面的key是一个int（0到11） 。value是歌名。现在给你一个playlist, 判断这个playlist是不是valid的。
 * Follow的rule是他们的key是adjacent或者是它本身。但是这个adjacent是针对一个环。
 * 比如 0, 1, 2, 3是valid的。10，11，0，1也是valid的。 1， 0 ，0， 11是valid的。但是10，1，2不是valid的。
 * follow up:
 * 现在给你一个valid的playlist和一首‍‌‌‍‍‍‌‌‌‌‌‌‍‍歌， 返回所有可以插入这首歌还能保证playlist是valid的位置
 */
public class ValidPlaylist {
  class Song {
    int key;
    String value;
    public Song(int key, String value) {
      this.key = key;
      this.value = value;
    }
  }

  boolean validPlaylist(List<Song> playlist) {
    return true;
  }

  public static void main(String[] args) {
    System.out.println("Valid playlist");
  }
}
