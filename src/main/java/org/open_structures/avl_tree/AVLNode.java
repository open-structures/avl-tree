package org.open_structures.avl_tree;

public interface AVLNode<T> {
    T getValue();

    AVLNode<T> getLeft();

    AVLNode<T> getRight();

    AVLNode<T> getParent();
}
