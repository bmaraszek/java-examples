package leetcode.examples.tree;

public class ValidateBST {

    public boolean validateBST(TreeNode bst) {
        if(bst == null) return true;

        if(bst.left != null && bst.left.data >= bst.data) return false;
        if(bst.right != null && bst.right.data < bst.data) return false;

        return validateBST(bst.left) && validateBST(bst.right);
    }
    public static void main(String... args) {
        ValidateBST subject = new ValidateBST();

        System.out.println(subject.validateBST(TreeNode.aValidBST()));
        System.out.println(subject.validateBST(TreeNode.anInvalidBST()));
    }
}
