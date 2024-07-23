package org.open_structures.avl_tree;

public class TreeUtils {

    private TreeUtils() {
    }

    public static <T> AVLNode<T> getRightmost(AVLNode<T> node) {
        if (node == null) {
            throw new IllegalArgumentException();
        }

        return node.getRight() != null ? getRightmost(node.getRight()) : node;
    }

    public static <T> AVLNode<T> getLeftmost(AVLNode<T> node) {
        if (node == null) {
            throw new IllegalArgumentException();
        }

        return node.getLeft() != null ? getLeftmost(node.getLeft()) : node;
    }

    public static <T> boolean isLeftChild(AVLNode<T> node) {
        if (node == null) {
            throw new IllegalArgumentException();
        }

        return node.getParent() != null && node.equals(node.getParent().getLeft());
    }

    public static <T> boolean isRightChild(AVLNode<T> node) {
        if (node == null) {
            throw new IllegalArgumentException();
        }

        return node.getParent() != null && node.equals(node.getParent().getRight());
    }

    public static <T> boolean isLeaf(AVLNode<T> node) {
        if (node == null) {
            throw new IllegalArgumentException();
        }

        return node.getLeft() == null && node.getRight() == null;
    }
}
