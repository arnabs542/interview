// O(mlogn) time complexity
import java.util.*;

public class FolderAccess {
    private Map<String, String> foldersParent;
    private Set<String> access;

    public FolderAccess(Map<String, String> folderParent, Set<String> access) {
        this.folderParent = foldersParent;
        this.access = access;
    }

    public boolean hasAccess(String folderName) {
        String currFolder = folderName;
        while (currFolder != null) {
            if (access.contains(currFolder)) {
                return true;
            }
            else {
                currFolder = folderParent.get(currFolder);
            }
        }
        return false;
    }

    public Set<String> simplifyAccess() {
        Set<String> simplifiedAccess = new HashSet<>();

        for(String folder : access) {
            String currFolder = folderParent.get(folder);
            boolean shouldDelete = false;
            while (currFolder != null && !shouldDelete) {
                if (access.contains(currFolder))
                    shouldDelete = true;
                else 
                    currFolder = folderParent.get(folder);
            }

            if (!shouldDelete)
                simplifiedAccess.add(folder);
        }
        return simplifiedAccess;
    }

    public static void main(String[] args) {
        Map<String, String> folderParent = new HashMap<>();
        folderParent.put("B", "A");
        folderParent.put("C", "B");
        folderParent.put("D", "B");
        folderParent.put("E", "A");
        folderParent.put("F", "E");

        Set<String> access = new HashSet<>();
        access.add("C");
        access.add("E");

        FolderAccess solver = new FolderAccess(folderParent, access);
        assert solver.hasAccess("B") == false;
        assert solver.hasAccess("C") == true;
        assert solver.hasAccess("F") == true;
        assert solver.hasAccess("G") == false;
    }
}

