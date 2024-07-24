package org.open_structures.avl_tree;

import java.util.*;
import java.util.function.Consumer;

import static java.lang.Math.log;
import static java.lang.Math.max;
import static java.util.Objects.requireNonNull;


public class AVLTree<T> {

    private final Comparator<? super T> comparator;
    private final Map<T, InternalAVLNode<T>> nodesMap = new HashMap<>();

    private InternalAVLNode<T> root;

    public AVLTree(Comparator<? super T> comparator) {
        this.comparator = requireNonNull(comparator);
    }

    /**
     * Joins two trees given that every element of the left tree is not greater than any element of the right one.
     */
    public static <T> AVLTree<T> join(AVLTree<T> left, AVLTree<T> right) {
        if (left == null || right == null) {
            throw new IllegalArgumentException();
        }
        if (!left.comparator.equals(right.comparator)) {
            throw new IllegalArgumentException("trees have different comparators and therefore can't be joined into single search tree");
        }

        if (left.isEmpty()) {
            return right;
        } else if (right.isEmpty()) {
            return left;
        } else {
            Comparator<? super T> comparator = left.comparator;
            AVLNode<T> leftRightmost = TreeUtils.getRightmost(left.root);
            AVLNode<T> rightLeftmost = TreeUtils.getLeftmost(right.root);
            if (comparator.compare(leftRightmost.getValue(), rightLeftmost.getValue()) > 0) {
                throw new IllegalArgumentException("Values of left and right trees either overlap or trees are in the wrong order. Left has to be less than or equal to right");
            }
            left.delete(leftRightmost.getValue());
            return join(left, leftRightmost.getValue(), right);
        }
    }

    public AVLNode<T> insert(T value) {
        if (value == null) {
            throw new IllegalArgumentException("null is not allowed");
        }
        if (nodesMap.containsKey(value)) {
            throw new IllegalArgumentException("Tree already has value " + value + ". Addition of duplicated (equal) values is not allowed");
        }

        InternalAVLNode<T> newNode = new InternalAVLNode<>(value);
        if (root == null) {
            root = newNode;
        } else {
            insert(root, newNode);
        }
        nodesMap.put(value, newNode);

        return newNode;
    }

    /**
     * @return null if the tree is empty
     */
    public AVLNode<T> getRoot() {
        return root;
    }

    /**
     * @return true if the tree has no nodes
     */
    public boolean isEmpty() {
        return getRoot() == null;
    }

    public void delete(T key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (!nodesMap.containsKey(key)) {
            throw new IllegalArgumentException(key + " does not belong to this tree");
        }

        final InternalAVLNode<T> nodeToDelete = nodesMap.get(key);
        final InternalAVLNode<T> parent = nodeToDelete.parent;
        if (TreeUtils.isLeaf(nodeToDelete)) { // then just deleted it
            if (parent != null) {
                if (TreeUtils.isLeftChild(nodeToDelete)) {
                    parent.left = null;
                } else {
                    parent.right = null;
                }
                reBalance(parent);
            } else { // means the tree consists of a single node
                this.root = null;
            }

        } else {
            if (nodeToDelete.getLeft() == null || nodeToDelete.getRight() == null) { // if node has only one child then we replace the node with the child
                if (parent != null) {
                    if (nodeToDelete.getLeft() != null) {
                        if (TreeUtils.isLeftChild(nodeToDelete)) {
                            parent.left = nodeToDelete.left;
                        } else {
                            parent.right = nodeToDelete.left;
                        }
                        nodeToDelete.left.parent = parent;
                    } else {
                        if (TreeUtils.isLeftChild(nodeToDelete)) {
                            parent.left = nodeToDelete.right;
                        } else {
                            parent.right = nodeToDelete.right;
                        }
                        nodeToDelete.right.parent = parent;
                    }
                    reBalance(parent);
                } else {
                    if (nodeToDelete.getLeft() != null) {
                        root = nodeToDelete.left;
                    } else {
                        root = nodeToDelete.right;
                    }
                    root.parent = null;
                    reBalance(root);
                }
            } else { // node has both children
                InternalAVLNode<T> leftmost = nodesMap.get(TreeUtils.getLeftmost(nodeToDelete.right).getValue());
                InternalAVLNode<T> nodeToReBalance = leftmost;
                InternalAVLNode<T> parentOfLeftmost = leftmost.parent;
                if (!parentOfLeftmost.equals(nodeToDelete)) {
                    parentOfLeftmost.left = null;
                    nodeToReBalance = parentOfLeftmost;
                    leftmost.right = nodeToDelete.right;
                }
                leftmost.left = nodeToDelete.left;
                leftmost.left.parent = leftmost;
                if (parent != null) {
                    if (TreeUtils.isLeftChild(nodeToDelete)) {
                        parent.left = leftmost;
                    } else {
                        parent.right = leftmost;
                    }
                    leftmost.parent = parent;
                } else {
                    root = leftmost;
                    leftmost.parent = null;
                }
                reBalance(nodeToReBalance);
            }
        }
        nodesMap.remove(key);
    }

    @Override
    public String toString() {
        return TreeUtils.print(this);
    }

    private static <T> AVLTree<T> join(AVLTree<T> left, T inBetweenValue, AVLTree<T> right) {
        if (height(left) > height(right) + 1) {
            return joinRightAVL(left, inBetweenValue, right);
        } else if (height(right) > height(left) + 1) {
            return joinLeftAVL(left, inBetweenValue, right);
        } else {
            AVLTree<T> joinedTree = new AVLTree<>(left.comparator);
            joinedTree.root = newNode(left.root, inBetweenValue, right.root);
            joinedTree.nodesMap.putAll(left.nodesMap);
            joinedTree.nodesMap.putAll(right.nodesMap);
            joinedTree.nodesMap.put(inBetweenValue, joinedTree.root);
            joinedTree.reBalance(joinedTree.root);
            return joinedTree;
        }
    }

    private static <T> InternalAVLNode<T> newNode(InternalAVLNode<T> left, T inBetweenValue, InternalAVLNode<T> right) {
        InternalAVLNode<T> newRoot = new InternalAVLNode<>(inBetweenValue);
        if (left != null) {
            newRoot.left = left;
            left.parent = newRoot;
        }
        if (right != null) {
            newRoot.right = right;
            right.parent = newRoot;
        }
        return newRoot;
    }

    // right is greater
    private static <T> AVLTree<T> joinLeftAVL(AVLTree<T> left, T inBetweenValue, AVLTree<T> right) {
        AVLTree<T> newTree;
        if (height(right) > height(left) + 1) { // out of balance
            AVLTree<T> rightLeft = new AVLTree<>(right.comparator);
            rightLeft.root = right.root.left;
            if (rightLeft.root != null) rightLeft.root.parent = null;
            AVLTree<T> leftJoin = joinLeftAVL(left, inBetweenValue, rightLeft);
            right.root.left = leftJoin.root;
            leftJoin.root.parent = right.root;
            right.nodesMap.putAll(leftJoin.nodesMap);
            newTree = right;
        } else {
            newTree = newAVLTree(right.comparator, left, inBetweenValue, right);
        }
        newTree.reBalance(newTree.root);

        return newTree;
    }

    // left is greater
    private static <T> AVLTree<T> joinRightAVL(AVLTree<T> left, T inBetweenValue, AVLTree<T> right) {
        AVLTree<T> newTree;
        if (height(left) > height(right) + 1) { // out of balance
            AVLTree<T> leftRight = new AVLTree<>(left.comparator);
            leftRight.root = left.root.right;
            if (leftRight.root != null) leftRight.root.parent = null;
            AVLTree<T> rightJoin = joinRightAVL(leftRight, inBetweenValue, right);
            left.root.right = rightJoin.root;
            rightJoin.root.parent = left.root;
            left.nodesMap.putAll(rightJoin.nodesMap);
            newTree = left;
        } else {
            newTree = newAVLTree(right.comparator, left, inBetweenValue, right);
        }
        newTree.reBalance(newTree.root);
        return newTree;
    }

    private static <T> AVLTree<T> newAVLTree(Comparator<? super T> comparator, AVLTree<T> left, T inBetweenValue, AVLTree<T> right) {
        AVLTree<T> newTree = new AVLTree<>(comparator);
        newTree.root = newNode(left.root, inBetweenValue, right.root);
        newTree.nodesMap.put(inBetweenValue, newTree.root);
        newTree.nodesMap.putAll(left.nodesMap);
        newTree.nodesMap.putAll(right.nodesMap);

        return newTree;
    }

    private static <T> int height(AVLTree<T> tree) {
        return tree.root != null ? tree.root.height : -1;
    }

    private void insert(InternalAVLNode<T> subtree, InternalAVLNode<T> node) {
        if (comparator.compare(subtree.getValue(), node.getValue()) < 0) {
            if (subtree.getRight() == null) {
                subtree.setRight(node);
                reBalance(node);
            } else {
                insert(subtree.right, node);
            }
        } else {
            if (subtree.getLeft() == null) {
                subtree.setLeft(node);
                reBalance(node);
            } else {
                insert(subtree.left, node);
            }
        }
    }

    private void reBalance(InternalAVLNode<T> subtreeRoot) {
        setHeightAndBalance(subtreeRoot);
        if (subtreeRoot.balanceFactor < -1) { // out of balance and left heavy
            if (subtreeRoot.left.balanceFactor > 0) { // left child is right heavy
                rotateLeft(subtreeRoot.left);
            }
            subtreeRoot = rotateRight(subtreeRoot);
        } else if (subtreeRoot.balanceFactor > 1) { // out of balance and right heavy
            if (subtreeRoot.right.balanceFactor < 0) { // right child is left heavy
                rotateRight(subtreeRoot.right);
            }
            subtreeRoot = rotateLeft(subtreeRoot);
        }
        if (subtreeRoot.getParent() != null) {
            reBalance(subtreeRoot.parent);
        }
    }

    // means it becomes left child or its right child
    private InternalAVLNode<T> rotateLeft(InternalAVLNode<T> node) {
        InternalAVLNode<T> rightChild = node.right;
        InternalAVLNode<T> parent = node.parent;
        InternalAVLNode<T> rightLeft = rightChild.left;
        rightChild.setLeft(node);
        node.setRight(rightLeft);
        setHeightAndBalance(node);
        setHeightAndBalance(rightChild);
        if (parent != null) {
            boolean parentRight = node.equals(parent.getRight());
            if (parentRight) {
                parent.setRight(rightChild);
            } else {
                parent.setLeft(rightChild);
            }
            setHeightAndBalance(parent);
        } else {
            this.root = rightChild;
            rightChild.setParent(null);
        }

        return rightChild;
    }

    // means it becomes right child of its left child
    private InternalAVLNode<T> rotateRight(InternalAVLNode<T> node) {
        InternalAVLNode<T> leftChild = node.left;
        InternalAVLNode<T> parent = node.parent;
        InternalAVLNode<T> leftRight = leftChild.right;
        leftChild.setRight(node);
        node.setLeft(leftRight);
        setHeightAndBalance(node);
        setHeightAndBalance(leftChild);
        if (parent != null) {
            boolean parentLeft = node.equals(parent.getLeft());
            if (parentLeft) {
                parent.setLeft(leftChild);
            } else {
                parent.setRight(leftChild);
            }
            setHeightAndBalance(parent);
        } else {
            this.root = leftChild;
            leftChild.setParent(null);
        }
        return leftChild;
    }

    private static <T> void setHeightAndBalance(InternalAVLNode<T> subtreeRoot) {
        int leftChildHeight = subtreeRoot.getLeft() != null ? subtreeRoot.left.height : -1;
        int rightChildHeight = subtreeRoot.getRight() != null ? subtreeRoot.right.height : -1;
        subtreeRoot.setHeight(max(leftChildHeight, rightChildHeight) + 1);
        subtreeRoot.balanceFactor = rightChildHeight - leftChildHeight;
    }

    public void clear() {
        nodesMap.clear();
        root = null;
    }

    private static class InternalAVLNode<T> implements AVLNode<T> {
        private final T value;
        private InternalAVLNode<T> parent;
        private InternalAVLNode<T> left, right;
        private int height = 0;
        private int balanceFactor = 0;

        private InternalAVLNode(T value) {
            this.value = requireNonNull(value);
        }

        @Override
        public AVLNode<T> getLeft() {
            return left;
        }

        @Override
        public AVLNode<T> getRight() {
            return right;
        }

        @Override
        public AVLNode<T> getParent() {
            return parent;
        }

        @Override
        public T getValue() {
            return value;
        }

        void setHeight(int height) {
            this.height = height;
        }

        void setLeft(InternalAVLNode<T> leftChild) {
            this.left = leftChild;
            if (left != null) this.left.parent = this;
        }

        void setRight(InternalAVLNode<T> rightChild) {
            this.right = rightChild;
            if (right != null) this.right.parent = this;
        }

        void setParent(InternalAVLNode<T> parent) {
            this.parent = parent;
        }
    }

    public int getHeight() {
        return (int) Math.ceil(log(nodesMap.size() + 1) / log(2));
    }
}
