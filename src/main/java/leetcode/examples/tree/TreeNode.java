package leetcode.examples.tree;

public class TreeNode {
    int data;
    TreeNode left;
    TreeNode right;

    public TreeNode(int data) {
        this.data = data;
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
}
