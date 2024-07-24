package org.open_structures.avl_tree;

import java.util.*;

public final class TreeUtils {

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

    public static <T> String print(AVLTree<T> tree) {
        if (tree.isEmpty()) {
            return "";
        }

        Queue<AVLNode<T>> bfsQueue = new LinkedList<>();
        Map<AVLNode<T>, Integer> positionsMap = new HashMap<>();
        Map<AVLNode<T>, Integer> lvlMap = new HashMap<>();

        int currentLvl = tree.getHeight();
        AVLNode<T> root = tree.getRoot();
        bfsQueue.add(root);
        lvlMap.put(root, currentLvl);
        List<String> rows = new LinkedList<>();
        StringBuilder currentValuesRow = new StringBuilder();
        StringBuilder currentEdgesRow = new StringBuilder();
        while (!bfsQueue.isEmpty()) {
            AVLNode<T> n = bfsQueue.poll();
            int lvl = lvlMap.get(n);
            if (lvl != currentLvl) {
                // starting new row
                if (!currentEdgesRow.isEmpty()) {
                    rows.add(currentEdgesRow.toString());
                }
                rows.add(currentValuesRow.toString());
                currentValuesRow = new StringBuilder();
                currentEdgesRow = new StringBuilder();
                currentLvl = lvl;
            }
            if (n.getParent() == null) {
                // root node
                int leftChildWidth = n.getLeft() != null ? width(n.getLeft()) : 0;
                positionsMap.put(n, leftChildWidth);
                currentValuesRow.append(" ".repeat(leftChildWidth)).append(n.getValue());
            } else {
                // parent is already printed
                int parentPos = positionsMap.get(n.getParent());
                int thisNodePos;
                if (isRightChild(n)) {
                    thisNodePos = parentPos + n.getParent().getValue().toString().length() + (n.getLeft() != null ? width(n.getLeft()) : 0);
                    int spacesToAddForEdge = parentPos + n.getParent().getValue().toString().length() - currentEdgesRow.length();
                    currentEdgesRow.append(" ".repeat(spacesToAddForEdge)).append("\\");
                } else {
                    thisNodePos = parentPos - n.getValue().toString().length() - (n.getRight() != null ? width(n.getRight()) : 0);
                    int spacesToAddForEdge = parentPos - 1 - currentEdgesRow.length();
                    currentEdgesRow.append(" ".repeat(spacesToAddForEdge)).append("/");
                }
                positionsMap.put(n, thisNodePos);
                int spacesToAddForValue = thisNodePos - currentValuesRow.length();
                currentValuesRow.append(" ".repeat(spacesToAddForValue)).append(n.getValue());
            }

            if (n.getLeft() != null) {
                bfsQueue.add(n.getLeft());
                lvlMap.put(n.getLeft(), lvl - 1);
            }
            if (n.getRight() != null) {
                bfsQueue.add(n.getRight());
                lvlMap.put(n.getRight(), lvl - 1);
            }
        }
        StringBuffer result = new StringBuffer();
        rows.forEach(s -> result.append(s).append("\n"));
        if (!currentEdgesRow.isEmpty()) {
            result.append(currentEdgesRow).append("\n");
        }
        result.append(currentValuesRow);


        return result.toString();
    }

    private static <T> int width(AVLNode<T> node) {
        int w = node.getValue().toString().length();
        if (node.getLeft() != null) {
            w += width(node.getLeft());
        }
        if (node.getRight() != null) {
            w += width(node.getRight());
        }
        return w;
    }
}
