class Solution {
    public int[] searchRange(int[] nums, int target) {
        int left = binarySearch(nums, target);
        if(left >= nums.length || nums[left] != target) {
            return new int[] {-1,-1};
        }
        return new int[] {left, binarySearch(nums,target+1) - 1};
    }
    
    public int binarySearch(int[] nums, int target) {
        int left = 0, right = nums.length;
        while(left < right) {
            int mid = left + (right - left) / 2;
            if(nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }
}

class Solution {
    public int[] searchRange(int[] A, int target) {
        int start = Solution.firstGreaterEqual(A, target);
        if (start == A.length || A[start] != target) {
            return new int[]{-1, -1};
        }
        return new int[]{start, Solution.firstGreaterEqual(A, target + 1) - 1};
    }
    private static int firstGreaterEqual(int[] A, int target) {
        int low = 0, high = A.length;
        while (low < high) {
            int mid = low + ((high - low) >> 1);
            if (A[mid] < target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }
}