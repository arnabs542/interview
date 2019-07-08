//non-streaming photo id data
//Approach 1: count freq + bucket sort
class solution{
    public List<Integer> topKviewedPhoto(int[] photoIDs, int k) {
        List<Integer> res = new ArrayList<>();
        if (photoIDs == null || photoIDs.length == 0) return res;

        Map<Integer, Integer> freqMap = new HashMap<>();
        int maxFreq = 0;
        for (int id : photoIDs) {
            int freq = freqMap.getOrDefault(id, 0) + 1;
            maxFreq = Math.max(maxFreq, freq);
            freqMap.add(id, freq);
        }

        List[] freqBuckets = new List[maxFreq + 1];
        for (Map.entry<Integer, Integer> freqEntry : freqMap.entrySet()) {
            if (freqBuckets[freqEntry.getValue()] == null) {
                freqBuckets[freqEntry.getValue()] = new ArrayList<>();
            freqBuckets[freqEntry.getValue()].add(freqEntry.getKey());
            }
        }

        for (int i = freqBuckets.length - 1; i >= 0; i --) {
            if (freqBuckets[i] != null) {
                List<Integer> ids = freqBuckets[i];
                if (k >= freqBuckets[i].size()) {
                    res.addAll(ids);
                    k -= freqBuckets[i].size();
                }
                else {
                    for (int j = 0; j < k; j ++) 
                        res.add(ids.get(j));
                    break;
                }  
            }
        }
        return res;
    }
}

//Approach 2: using k-min heap to maintain k most viewed photos
//time complexity : O(nlogK)
class solution {
    public List<Integer> topKViewPhoto(int[] photoIDs, int k) {
        List<Integer> res = new ArrayList<>();
        if (photoIDs == null || photoIDs.length == 0) return res;

        Map<Integer, Integer> freqMap = new HashMap<>();
        for (int id : photoIDs) {
            int freq = freqMap.getOrDefault(id, 0) + 1;
            freqMap.add(id, freq);
        }

        PriorityQueue<View> topView = new PriorityQueue<>((a,b)->a.freq - b.freq); // ascending queue
        for (Map.Entry<Integer, Integer> freqEntry : freqMap.entrySet()) {
            View view = new View(freqEntry.getKey(), freqEntry.getValue());
            if (topView.size() < k) {
                topView.add(view);
            }
            else {
                if (freqEntry.getValue() > topKView.peek().freq) {
                    topView.poll();
                    topView.offer(view);
                }
            }      
        }

        while(!topView.isEmpty()) 
            res.add(topView.poll().id);
        
        return res;
    }

    class View {
        int id;
        int freq;
        public View(int id, int freq) {
            this.id = id; this.freq = freq;
        }
    }
}



//For steaming data
//implement the min heap ourselves, and do the keyAdjust heapify

class PhotoView implements Comparable<PhotoView> {
    int id;
    int freq;

    public PhotoView(int id, int freq) {
        this.id = id; 
        this.freq = freq;
    }

    @Override
    public int compareTo(PhotoView other) {
        if (this.freq == other.freq)
            return other.id - this.id;
        return this.freq - other.freq;
    }
}

public class MaxPhotoID {
    private PriorityQueue<PhotoView> kMostViewPhotos;
    private Map<Integer, PhotoView> photoViewFreqMap;
    private final int k;

    public MaxPhotoID(int k) {
        this.k = k;
        kMostViewPhotos = new PriorityQueue<>();
        photoViewFreqMap = new HashMap<>();
    }

    public void view(int id) {
        if (!photoViewFreMap.contains(id)) {
            photoViewFreqMap.put(id, new PhotoView(id, 0));
        }
        PhotoView view = photoViewFreqMap.get(id);
        view.freq ++;

        if (kMostViewPhotos.size() < k || view.freq >= kMostViewPhotos.peek().freq) {
            kMostViewPhotos.remove(view);
            kMostViewPhotos.offer(view);
            if (kMostViewPhotos.size() > k)
                kMostViewPhotos.poll();
        }
    }

    public List<Integer> getTopViewPhoto() {
        PhotoView[] topK = kMostViewPhoto.toArray(new PhotoView[kMostViewPhotos.size()]);

        List<Integer> result = new ArrayList<>();
        for (PhotoView photoView : topK) {
            result.add(photoView.id);
        }
        return result;
    }

    public static void main (String[] args) {
        MaxPhotoID solver = new MaxPhotoID(4);
        solve.view(1);
        solve.view(2);
        solve.view(1);
        System.out.println(solver.getTopViewPhoto());
        solve.view(3);
        
        System.out.println(solver.getTopKViewPhoto());
        solver.view(2);
        solver.view(12);
        solver.view(31);
        solver.view(101);
        solver.view(11);
        solver.view(3);
        System.out.println(solver.getTopKViewPhoto());

        solver.view(31);
        solver.view(101);
        solver.view(31);
        solver.view(101);
        System.out.println(solver.getTopKViewPhoto());
    }
}
