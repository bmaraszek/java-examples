package leetcode.examples.tree;

public class TreeNode {
    int data;
    TreeNode left;
    TreeNode right;

    public TreeNode(int data) {
        this.data = data;
    }

    /**
     * Insert a value into a BST represented by this TreeNode
     */
    public void insert(int value) {
        if(value < this.data) {
            // go left
            if(left == null) left = new TreeNode(value);
            else left.insert(value);
        } else {
            // go right
            if(right == null) right = new TreeNode(value);
            else right.insert(value);
        }
    }

    @Override
    public String toString() {
        return String.format("TreeNode(%d)", data);
    }

    /**
     *            1
     *         /     \
     *       2        3
     *     /   \     /  \
     *    4     5   6    7
     *                    \
     *                     8
     */
    public static TreeNode aTestTree1() {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(7);
        root.right.left.right = new TreeNode(8);
        return root;
    }

    public static TreeNode aValidBST() {
        TreeNode root = new TreeNode(10);
        root.left = new TreeNode(5);
        root.right = new TreeNode(15);
        root.left.left = new TreeNode(2);
        root.left.right = new TreeNode(5);
        root.right.left = new TreeNode(13);
        root.right.right = new TreeNode(22);
        root.left.left.left = new TreeNode(1);
        root.right.left.right = new TreeNode(13);
        return root;
    }

    public static TreeNode anInvalidBST() {
        TreeNode root = new TreeNode(10);
        root.left = new TreeNode(5);
        root.right = new TreeNode(15);
        root.left.left = new TreeNode(2);
        root.left.right = new TreeNode(5);
        root.right.left = new TreeNode(9); // invalid
        root.right.right = new TreeNode(22);
        root.left.left.left = new TreeNode(1);
        root.right.left.right = new TreeNode(13);
        return root;
    }

    public static TreeNode inOrderTree() {
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2);
        root.right = new TreeNode(6);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(3);
        root.right.left = new TreeNode(5);
        root.right.right = new TreeNode(7);
        return root;
    }

    public static TreeNode preOrderTree() {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(5);
        root.left.left = new TreeNode(3);
        root.left.right = new TreeNode(4);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(7);
        return root;
    }

    public static TreeNode postOrderTree() {
        TreeNode root = new TreeNode(7);
        root.left = new TreeNode(3);
        root.right = new TreeNode(6);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(2);
        root.right.left = new TreeNode(4);
        root.right.right = new TreeNode(5);
        return root;
    }
}
