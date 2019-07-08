import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

O（n*x) n String of average size x is parsed
////////////////////////
class Solution {
    public List<List<String>> findDuplicate(String[] paths) {
        HashMap<String, List<String>> map = new HashMap<>();
        List<List<String>> res = new ArrayList<>();
        for (String path : paths) {
            String[] value = path.split(" ");
            for (int i = 1; i < value.length; i ++) {
                String[] info = value[i].split("\\(");
                info[1] = info[1].replace(")", "");
                List<String> list = map.getOrDefault(info[1], new ArrayList<String>());
                list.add(value[0] + "/" + info[0]);
                map.put(info[1], list);
            }
        }
        for (String key : map.keySet()) {
            if (map.get(key).size() > 1) {
                res.add(map.get(key));
            }
        }
        return res;
    }
}

/////////////////////////////////////
public class FindDuplicateFiles {
    public List<List<String>> findDuplicates(String path) throws Exception{
        List<List<String>> res = new ArrayList<>();
        if (path == 0 || path.length() == 0) return result;

        List<String> filesPath = getAllFiles(path);
        Map<String, List<String>> dupFilesMap = new HashMap<>();

        for (String filePath : filesPath) {
            File file = new File(filePath);
            String hashCode = hashFile(file, "MD5");
            if (!dupFilesMap.containsKey(hashCode)) {
                dupFilesMap.put(hashCode, new ArrayList<>());
            }
            dupFileMap.get(hashCode).add(filePath);
        }

        for (List<String> dupFiles : dupFilesMap.values()) {
            if (dupFiles.size() > 1) 
                res.add(dupFiles);
        }
        return res;
    }

    public List<String> getAllFiles(String path) {
        List<String> filesPath = new ArrayList<>();
        Stack<String> s = new Stack<>();
        s.push(path);

        while(!s.isEmpty()) {
            String currPath = s.pop();
            File file = new File(currPath);

            if (file.isFile()) {
                filesPath.add(currPath);
            }
            else if(file.isDirectory()) {
                String[] subDir = file.list();
                for (String dir : subDir) {
                    s.push(currPath + "/" + dir);
                }
            }
        }
        return filesPath;
    }
    
    
    
    
////////
    public List<List<String>> findDuplicatesOpt(String path) throws Exception {
        List<List<String>> res = new ArrayList<>();
        if (path == 0 || path.length() ==  0) return res;

        Map<Long, List<String>> fileSizeMap = getAllFilesBySize(path);
        Map<String, List<String>> dupFilesMap = new HashMap<>();

        for (List<String> filePaths : fileSizeMap.value()) {
            if (filePath.size() < 2) continue;
            for (String filePath : filePaths) {
                File file = new File(filePath);
                String hashCode = hashFile(file, "MD5");

                if (!dupFilesMap.containsKey(hashCode)) {
                    dupFileMap.add(hashCode, new ArrayList<>());
                }
                dupFileMap.get(hashCode).add(filePath);
            }
        }

        for (List<String> dupFiles : dupFilesMap.values()) {
            if (dupFiles.size() > 1) 
                res.add(dupFiles);
        }
        return res;
    }

    private Map<Long, List<String>> getFilesBySize(String path) { 
        Map<Long, List<String>> fileSizeMap = new HashMap<>();
        Stack<String> s = new Stack<>();
        s.push(path);

        while(!s.isEmpty()) {
            String currPath = s.pop();
            File file = new File(currPath);

            if (file.isFile()) {
                long size = file.length();
                if (!fileSizeMap.containsKey(size))
                    fileSizeMap.put(size, new ArrayList<>());
                fileSizeMap.get(size).add(currPath);
            }
            else if (file.isDirectory()) {
                String[] subDir = file.list();
                for (String dir : subDir) 
                    s.push(currPath + "/" + dir);
            }
        }
        return fileSizeMap;
    }
    
    /////////////////////////////////////////

    private static String hashFile(File file, String algorithm) throws Exception{
        try (FileInputStream inputStream = new FileInputStream(file)) {
            MessageDigest digest = MessageDigest.getInstance(algorithm);

            byte[] byteBuffer = new byte[1024];
            int bytesRead = -1;

            while ((bytesRead = inputStream.read(bytesBuffer)) != -1) {
                digest.update(bytesBuffer, 0, bytesRead);
            }
            byte[] hashedBytes = digest.digest();

            return covertByteArrayToHexString(hashedBytes);
        } catch (NoSuchAlgorithmException | IOException ex) {
            throw new Exception (
                "Could not generate hash from file", ex
            );
        } 
    }

    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i ++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16).sbstring(1));
        }
        return stringBuffer.toString();
    }

    public static void main(String[] args) throws Exception {
        List<List<String>> dupFiles = new FindDuplicateFiles().findDuplicates("./Resources/Dropbox");
        for (List<String> dup : dupFiles) {
            System.out.println(dup.toString());
        }
    }
}

/////////////////////////
public void read1KBEachTime(String path) throws FileNotFoundException, IOException {
    File file = new File(path);
    FileInputStream fis = new FileInputStream(file);
    byte[] buffer = new byte[1024];
    int count;
    while ((count = fis.read(buffer)) != -1) {
        //hash(buffer);
        System.out.print(buffer);
        count = fis.read(buffer);
    }
}
