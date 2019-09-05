package dropbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  input "/foo"
 * - /foo
 *   - /images
 *     - /foo.png  <------|
 *   - /temp                   | same file contents
 *     - /baz                   |
 *       - /that.foo  <- - - |-- |
 *   - /bar.png  <------- |     |
 *   - /file.tmp  <------------| same file contents
 *   - /other.temp  <--------|
 *   - /blah.txt
 *
 *
 * Output:
 * [
 *    ['/foo/bar.png', '/foo/images/foo.png'],
 *    ['/foo/file.tmp', '/foo/other.temp', '/foo/temp/baz/that.foo']
 * ]
 */
public class FindDuplicate {
  // There are two inputs, String root or all paths
  public List<List<String>> findDuplicate(String root) {
    // First need to get all file by using root
    List<String> fileList = getAllFiles(root);
    return findDuplicate(fileList.toArray(new String[0]));
  }

  private List<String> getAllFiles(String root) {
    List<String> fileList = new ArrayList<>();
    Deque<String> stack = new ArrayDeque<>();
    stack.push(root);

    while (!stack.isEmpty()) {
      String currentPath = stack.pop();
      File file = new File(currentPath);

      if (file.isFile()) {
        fileList.add(currentPath);
      } else if (file.isDirectory()) {
        String[] subDir = file.list();
        for (String subFile : subDir) {
          String newPath = currentPath + "/" + subFile;
          stack.push(newPath);
        }
      }
    }
    return fileList;
  }

  public List<List<String>> findDuplicate(String[] paths) {
    List<List<String>> result = new ArrayList<>();
    Map<String, List<String>> map = new HashMap<>(); // Key: checksum; Value: list of file path

    for (String path : paths) {
      File file = new File(path);
      String checksum = hashFile(file, "MD5");

      List<String> fileList = map.getOrDefault(checksum, new ArrayList<>());
      fileList.add(path);
      map.put(checksum, fileList);
    }

    for (List<String> fileList : map.values()) {
      if (fileList.size() > 1) {
        result.add(fileList);
      }
    }
    return result;
  }

  private String hashFile(File file, String hashAlgorithm) {
    String checksum = "";
    try {
      MessageDigest md5Digest = MessageDigest.getInstance(hashAlgorithm);
      checksum = getChecksum(md5Digest, file);
    } catch (NoSuchAlgorithmException | IOException exception) {
      System.out.println("Cannot get checksum");
    }
    return checksum;
  }

  private String getChecksum(MessageDigest digest, File file)
      throws IOException {
    //Get file input stream for reading the file content
    FileInputStream fis = new FileInputStream(file);

    //Create byte array to read data in chunks
    byte[] byteArray = new byte[1024];
    int bytesCount;

    //Read file data and update in message digest
    while ((bytesCount = fis.read(byteArray)) != -1) {
      digest.update(byteArray, 0, bytesCount);
    }

    //close the stream; We don't need it now.
    fis.close();

    //Get the hash's bytes
    byte[] bytes = digest.digest();

    //This bytes[] has bytes in decimal format;
    //Convert it to hexadecimal format
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < bytes.length; i++) {
      sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
    }

    //return complete hash
    return sb.toString();

  }

  /**
   *  Follow up: If the directory only contains a few duplicate files, we still need to search through
   *  the whole directory.
   *  MD5 is slow if each file is very large. How can we optimize the above solution?
   *
   *  Optimize: use the metadata of the file to first hashing the files and then use MD5 to hash the files.
   */
  public List<List<String>> findDuplicateUpdate(String[] paths) {
    List<List<String>> result = new ArrayList<>();
    // Key: fiel size; Value: list of file path
    Map<Long, List<String>> sizeMap = getFilesBySize(paths);
    // Key: hash value; Value: list of file path
    Map<String, List<String>> hashMap = new HashMap<>();

    for (List<String> filePahs : sizeMap.values()) {
      if (filePahs.size() < 2) {
        continue;
      }
      for (String path : filePahs) {
        File file = new File(path);
        String checksum = hashFile(file, "MD5");

        List<String> fileList = hashMap.getOrDefault(checksum, new ArrayList<>());
        fileList.add(path);
        hashMap.put(checksum, fileList);
      }
    }

    for (List<String> fileList : hashMap.values()) {
      if (fileList.size() > 1) {
        result.add(fileList);
      }
    }
    return result;
  }

  private Map<Long, List<String>> getFilesBySize(String[] paths) {
    Map<Long, List<String>> sizeMap = new HashMap<>();
    for (String path : paths) {
      File file = new File(path);
      long size = file.length();
      List<String> fileList = sizeMap.getOrDefault(size, new ArrayList<>());
      fileList.add(path);
      sizeMap.put(size, fileList);
    }
    return sizeMap;
  }

  // Further optimization: we do not calculate MD5 value of the whole file. Instead we divide the
  // file into blocks of 1kb.
  // First hash the file with MD5 of the first 1kb and then hash by the second 1kb.....and so on.
  public void readOneKb(String path) throws IOException {
    File file = new File(path);
    FileInputStream fis = new FileInputStream(file);
    byte[] buffer = new byte[1024];
    int count = fis.read(buffer, 0, 1024);
    while (count != -1) {
      // hash(buffer);
      count = fis.read(buffer);
    }
  }
}
