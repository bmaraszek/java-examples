package leetcode.examples.tree;

import org.assertj.core.util.Lists;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * The right view of a Binary Tree is a set of nodes visible when the tree is visible from the right side.
 * In other words, the rightmost node at each level of the three.
 * Input:
 *      1
 *    /   \
 *   2     3
 *  / \   / \
 * 4  5  6   7
 *        \
 *         8
 * Output: Right view of the tree is 1 3 7 8

 * The idea is to use recursion and keep track of the maximum level. Traverse the tree in a manner that the right
 * subtree is visited before the left subtree.
 */
public class RightView {

    /**
     * Class to access maximum visited level by reference.
     * It has to be like this. Neither int nor Integer work.
     * It's because Integer is immutable. Overriding integer in a method won't work.
     * an AtomicInteger can be used here as a wrapper.
     */
    public static class MaxLevel {
        int maxLevel;
    }

    public static List<TreeNode> rightViewRecursive(TreeNode root) {
        List<TreeNode> result = new ArrayList<>();
        rightViewUntil(root,1, new MaxLevel(), result);
        return result;
    }

    public static void rightViewUntil(TreeNode node, int level, MaxLevel maxLevel, List<TreeNode> result) {
        if(node == null) return;

        // if this is the last node of its level
        if(maxLevel.maxLevel < level) {
            result.add(node);
            maxLevel.maxLevel = level;
        }

        // recur the right subtree first, then left subtree
        rightViewUntil(node.right, level + 1, maxLevel, result);
        rightViewUntil(node.left, level + 1, maxLevel, result);
    }

    public static List<TreeNode> rightViewIterative(TreeNode root) {
        if (root == null) return Lists.emptyList();

        List<TreeNode> result = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            // get the number of nodes at each level
            int n = queue.size();

            // traverse all the nodes of the current level
            for (int i = 0; i < n; i++) {
                TreeNode current = queue.poll();
                // add the last node at each level
                if (i == n - 1) result.add(current);

                // if the left child is not null, add it into the queue
                if (current.left != null) queue.add(current.left);
                if (current.right != null) queue.add(current.right);
            }
        }

        return result;
    }

    public static void main(String... args) {
        TreeNode root = TreeNode.aTestTree1();

        List<TreeNode> rightView = rightViewRecursive(root);
        System.out.println(rightView);

        List<TreeNode> rightView2 = rightViewIterative(root);
        System.out.println(rightView2);
    }

}
