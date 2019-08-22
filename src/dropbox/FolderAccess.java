package dropbox;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Given child-parent folder relationships, and user has access to folders in the access set. Find if user has access to particular folder.
 * /A
 * |__ _ /B
 *     |___ /C <-- access
 *     |___ /D
 *
 * |___ /E <-- access
 *     |___ /F
 * |___ /G
 * folders = [[br] ('A', None),
 * ('B', 'A'),
 * ('C', 'B'),
 * ('D', 'B'),
 * ('E', 'A'),
 * ('F', 'E'),
 * ]
 * access = set(['C', 'E'])
 * has_access(String folder_name) -> boolean
 * has_access("B") -> false
 * has_access("C") -> true
 * has_access("F") -> true
 *
 */
public class FolderAccess {

  private final Map<String, String> parentMap;
  private final Set<String> access;

  public FolderAccess(Map<String, String> parentMap, Set<String> access) {
    this.parentMap = parentMap;
    this.access = access;
  }

  public boolean hasAccess(String folderName) {
    String current = folderName;
    while (current != null) {
      if (access.contains(current)) {
        return true;
      } else {
        current = parentMap.get(current);
      }
    }
    return false;
  }

  // Followup: 可访问set里有重复，比如C，B，A同时出现，要求只留一个A。
  public Set<String> simplifyAccess() {
    Set<String> result = new HashSet<>();
    for (String folder : access) {
      String current = parentMap.get(folder);
      boolean shouldRemove = false;
      while (current != null && !shouldRemove) {
        if (access.contains(current)) {
          shouldRemove = true;
        } else {
          current = parentMap.get(current);
        }
      }
      if (!shouldRemove) {
        result.add(folder);
      }
    }
    return result;
  }

  public static void main(String[] args) {
    Map<String, String> parentMap = new HashMap<>();
    parentMap.put("B", "A");
    parentMap.put("C", "B");
    parentMap.put("D", "B");
    parentMap.put("E", "A");
    parentMap.put("F", "E");

    Set<String> access = new HashSet<>();
    access.add("C");
    access.add("E");

    FolderAccess fa = new FolderAccess(parentMap, access);
    assert fa.hasAccess("B") == false;
    assert fa.hasAccess("C") == true;
    assert fa.hasAccess("F") == true;
    assert fa.hasAccess("G") == false; // we do not have folder "G"
  }
}
