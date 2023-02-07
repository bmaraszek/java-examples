package leetcode.examples.tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                                                    //
//  https://www.enjoyalgorithms.com/blog/iterative-binary-tree-traversals-using-stack                                 //
//                                                                                                                    //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class TreeTraversals {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                                                //
    // IN ORDER - from left to right                                                                                  //
    //                                                                                                                //
    //         4                                                                                                      //
    //       /   \                                                                                                    //
    //      2     6                                                                                                   //
    //     / \   / \                                                                                                  //
    //    1   3  5  7                                                                                                 //
    //                                                                                                                //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public List<Integer> inOrder(TreeNode bst, List<Integer> list) {
        LinkedList<TreeNode> stack = new LinkedList<>();
        TreeNode current = bst;
        while(!stack.isEmpty() || current != null) {
            if(current != null) {
                stack.push(current);
                current = current.left;
            } else {
                current = stack.pop();
                list.add(current.data);
                current = current.right;
            }
        }
        return list;
    }

    public List<Integer> inOrderRecursive(TreeNode bst, List<Integer> list) {
        if(bst.left != null) inOrderRecursive(bst.left, list);
        list.add(bst.data);
        if(bst.right != null) inOrderRecursive(bst.right, list);
        return list;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                                                //
    // PRE ORDER - from root all the way left                                                                         //
    //                                                                                                                //
    //         1                                                                                                      //
    //       /   \                                                                                                    //
    //      2     5                                                                                                   //
    //     / \   / \                                                                                                  //
    //    3   4  6  7                                                                                                 //
    //                                                                                                                //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<Integer> preOrder(TreeNode bst, List<Integer> list) {
        LinkedList<TreeNode> stack = new LinkedList<>();
        TreeNode current = bst;
        TreeNode previous = null;
        while(!stack.isEmpty() || current != null) {
            if(current != null) {
                list.add(current.data);
                stack.push(current);
                current = current.left;
            } else {
                previous = stack.pop();
                current = previous.right;
            }
        }
        return list;
    }

    public List<Integer> preOrderRecursive(TreeNode bst, List<Integer> list) {
        list.add(bst.data);
        if(bst.left != null) preOrderRecursive(bst.left, list);
        if(bst.right != null) preOrderRecursive(bst.right, list);
        return list;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                                                //
    // POST ORDER - children before the parent                                                                        //
    //                                                                                                                //
    //         7                                                                                                      //
    //       /   \                                                                                                    //
    //      3     6                                                                                                   //
    //     / \   / \                                                                                                  //
    //    1   2  4  5                                                                                                 //
    //                                                                                                                //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<Integer> postOrder(TreeNode bst, List<Integer> list) {
        LinkedList<TreeNode> mainStack = new LinkedList<>();
        LinkedList<TreeNode> rightChildStack = new LinkedList<>();
        TreeNode current = bst;
        while(!mainStack.isEmpty() || current != null) {
            if(current != null) {
                if(current.right != null) rightChildStack.push(current.right);
                mainStack.push(current);
                current = current.left;
            } else {
                current = mainStack.peek();
                if(!rightChildStack.isEmpty() && current.right == rightChildStack.peek()) {
                    current = rightChildStack.pop();
                } else {
                    list.add(current.data);
                    mainStack.pop();
                    current = null;
                }
            }
        }
        return list;
    }

    public List<Integer> postOrderRecursive(TreeNode bst, List<Integer> list) {
        if(bst.left != null) postOrderRecursive(bst.left, list);
        if(bst.right != null) postOrderRecursive(bst.right, list);
        list.add(bst.data);
        return list;
    }


    public static void main(String... args) {
        TreeTraversals subject = new TreeTraversals();

        System.out.println(subject.inOrder(TreeNode.inOrderTree(), new ArrayList<>()));
        System.out.println(subject.inOrderRecursive(TreeNode.inOrderTree(), new ArrayList<>()));

        System.out.println(subject.preOrder(TreeNode.preOrderTree(), new ArrayList<>()));
        System.out.println(subject.preOrderRecursive(TreeNode.preOrderTree(), new ArrayList<>()));

        System.out.println(subject.postOrder(TreeNode.postOrderTree(), new ArrayList<>()));
        System.out.println(subject.postOrderRecursive(TreeNode.postOrderTree(), new ArrayList<>()));
    }
}
