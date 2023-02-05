package leetcode.examples.tree;

public class BinarySearchTreeFromArray {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                                                //
    // Naive approach: use the insert method for every new element to insert                                          //
    // Use binary search to find new left and right elements.                                                         //
    // This is O(n) space and O(n log n) time - we insert n elements and each insertion is O(log n)                   //
    //                                                                                                                //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    TreeNode minHeightBst(int[] arr) {
        return minHeightBst(arr, null, 0, arr.length - 1);
    }

    TreeNode minHeightBst(int[] arr, TreeNode bst, int startIdx, int endIdx) {
        if(endIdx < startIdx) return bst;
        int midIdx = Math.floorDiv((startIdx + endIdx), 2);
        int valueToAdd = arr[midIdx];
        if(bst == null) bst = new TreeNode(valueToAdd);
        else bst.insert(valueToAdd);
        minHeightBst(arr, bst, startIdx, midIdx - 1);
        minHeightBst(arr, bst, midIdx + 1, endIdx);
        return bst;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                                                //
    // Improved approach: insert elements manually                                                                    //
    // Use binary search to find new left and right elements.                                                         //
    // This is O(n) space and O(n) time                                                                               //
    //                                                                                                                //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    TreeNode minHeightBst2(int[] arr) {
        return minHeightBst2(arr, null, 0, arr.length - 1);
    }

    TreeNode minHeightBst2(int[] arr, TreeNode bst, int startIdx, int endIdx) {
        if(endIdx < startIdx) return bst;
        int midIdx = Math.floorDiv((startIdx + endIdx), 2);
        TreeNode newBstNode = new TreeNode(arr[midIdx]);

        if(bst == null) bst = newBstNode;
        else {
            if(arr[midIdx] < bst.data) {
                bst.left = newBstNode;
                bst = bst.left;
            } else {
                bst.right = newBstNode;
                bst = bst.right;
            }
        }
        minHeightBst2(arr, bst, startIdx, midIdx - 1);
        minHeightBst2(arr, bst, midIdx + 1, endIdx);
        return bst;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                                                //
    // Execute the algorithm                                                                                          //
    //                                                                                                                //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void main(String... args) {
        BinarySearchTreeFromArray subject = new BinarySearchTreeFromArray();

        int[] arr = new int[]{1, 2, 5, 7, 10, 13, 14, 15, 22};

        TreeNode minBST = subject.minHeightBst(arr);
        System.out.println(minBST);

        TreeNode minBST2 = subject.minHeightBst2(arr);
        System.out.println(minBST2);
    }
}
