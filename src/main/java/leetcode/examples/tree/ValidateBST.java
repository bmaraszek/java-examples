package leetcode.examples.tree;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                                                //
// Every node in a Binary Search Tree has a min value and a max value                                             //
//                                                                                                                //
//        10                                                                                                      //
//       /  \                                                                                                     //
//      5    15                                                                                                   //
//     / \   / \                                                                                                  //
//    2  5  13  22    (5) has to be >= than its parent (5) and strictly smaller than its grandparent (10)         //
//   /       \        (13) has to be < than its parent (15) and >= its grandparent (10)                           //
//  1         14                                                                                                  //
//                                                                                                                //
// Time Complexity O(n) - we're traversing every node in a tree                                                   //
// Space Complexity O(d) - where d is the depth of the tree                                                       //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ValidateBST {

    public boolean validateBST(TreeNode bst) {
        return validateBST(bst, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public boolean validateBST(TreeNode bst, Integer minValue, Integer maxValue) {
        if(bst == null) return true;
        if(bst.data < minValue || bst.data >= maxValue) return false;

        boolean isLeftValid = validateBST(bst.left, minValue, bst.data);
        boolean isRightValid = validateBST(bst.right, bst.data, maxValue);

        return isLeftValid && isRightValid;
    }
    public static void main(String... args) {
        ValidateBST subject = new ValidateBST();

        System.out.println(subject.validateBST(TreeNode.aValidBST()));
        System.out.println(subject.validateBST(TreeNode.anInvalidBST()));
    }
}
