package org.open_structures.avl_tree;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;

import static java.lang.Math.max;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * It should keep the tree balanced
 * <p>
 * <a href="https://www.youtube.com/watch?v=FNeL18KsWPc&t=2162s">AVL Trees, AVL Sort</a>
 */
public class AVLTreeTest {

    private AVLTree<Integer> tree;

    private final Comparator<Integer> comparator = Integer::compareTo;

    @Before
    public void setUp() {
        tree = new AVLTree<>(comparator);
    }

    @Test
    public void shouldRightRotate() {
        // when
        tree.insert(41);
        tree.insert(20);
        tree.insert(11);

        // then
        Assertions.assertThat(tree.getRoot()).isNotNull();
        Assertions.assertThat(tree.getRoot().getValue()).isEqualTo(20);

        AVLNode<Integer> left = tree.getRoot().getLeft();
        assertThat(left.getValue()).isEqualTo(11);
        assertThat(left.getLeft()).isNull();
        assertThat(left.getRight()).isNull();

        AVLNode<Integer> right = tree.getRoot().getRight();
        assertThat(right.getValue()).isEqualTo(41);
        assertThat(right.getLeft()).isNull();
        assertThat(right.getRight()).isNull();
    }

    @Test
    public void shouldLeftRotate() {
        // when
        tree.insert(20);
        tree.insert(41);
        tree.insert(65);

        // then
        Assertions.assertThat(tree.getRoot()).isNotNull();
        Assertions.assertThat(tree.getRoot().getValue()).isEqualTo(41);

        AVLNode<Integer> left = tree.getRoot().getLeft();
        assertThat(left.getValue()).isEqualTo(20);
        assertThat(left.getLeft()).isNull();
        assertThat(left.getRight()).isNull();

        AVLNode<Integer> right = tree.getRoot().getRight();
        assertThat(right.getValue()).isEqualTo(65);
        assertThat(right.getLeft()).isNull();
        assertThat(right.getRight()).isNull();
    }

    @Test
    public void shouldRightLeftRotate() {
        // when
        tree.insert(41);
        tree.insert(65);
        tree.insert(50);

        // then
        Assertions.assertThat(tree.getRoot()).isNotNull();
        Assertions.assertThat(tree.getRoot().getValue()).isEqualTo(50);

        AVLNode<Integer> left = tree.getRoot().getLeft();
        assertThat(left.getValue()).isEqualTo(41);
        assertThat(left.getLeft()).isNull();
        assertThat(left.getRight()).isNull();

        AVLNode<Integer> right = tree.getRoot().getRight();
        assertThat(right.getValue()).isEqualTo(65);
        assertThat(right.getLeft()).isNull();
        assertThat(right.getRight()).isNull();
    }

    @Test
    public void shouldLeftRightRotate() {
        // when
        tree.insert(41);
        tree.insert(20);
        tree.insert(29);

        // then
        Assertions.assertThat(tree.getRoot()).isNotNull();
        Assertions.assertThat(tree.getRoot().getValue()).isEqualTo(29);

        AVLNode<Integer> left = tree.getRoot().getLeft();
        assertThat(left.getValue()).isEqualTo(20);
        assertThat(left.getLeft()).isNull();
        assertThat(left.getRight()).isNull();

        AVLNode<Integer> right = tree.getRoot().getRight();
        assertThat(right.getValue()).isEqualTo(41);
        assertThat(right.getLeft()).isNull();
        assertThat(right.getRight()).isNull();
    }

    @Test
    public void shouldDeleteOnlyNodeOfTheTree() {
        // given
        tree.insert(1);

        // when
        tree.delete(1);

        // then
        Assertions.assertThat(tree.getRoot()).isNull();
    }

    @Test
    public void shouldDeleteRoot() {
        // given
        tree.insert(1);
        tree.insert(3);
        tree.insert(5);

        // when
        tree.delete(3);

        // then
        Assertions.assertThat(tree.getRoot()).isNotNull();

        if (tree.getRoot().getValue() == 1) {
            Assertions.assertThat(tree.getRoot().getParent()).isNull();
            Assertions.assertThat(tree.getRoot().getLeft()).isNull();
            Assertions.assertThat(tree.getRoot().getRight()).isNotNull();
            Assertions.assertThat(tree.getRoot().getRight().getValue()).isEqualTo(5);
            Assertions.assertThat(tree.getRoot().getRight().getParent()).isEqualTo(tree.getRoot());

        } else if (tree.getRoot().getValue() == 5) {
            Assertions.assertThat(tree.getRoot().getParent()).isNull();
            Assertions.assertThat(tree.getRoot().getLeft()).isNotNull();
            Assertions.assertThat(tree.getRoot().getLeft().getValue()).isEqualTo(1);
            Assertions.assertThat(tree.getRoot().getLeft().getParent()).isEqualTo(tree.getRoot());
            Assertions.assertThat(tree.getRoot().getRight()).isNull();
        } else {
            fail("Expected tree root to be either 1 or 5");
        }
    }

    @Test
    public void shouldDeleteLeaf() {
        // given
        tree.insert(2);
        tree.insert(3);
        tree.insert(5);
        tree.insert(7);

        // when
        tree.delete(7);

        // then
        Assertions.assertThat(tree.getRoot().getValue()).isEqualTo(3);
        Assertions.assertThat(tree.getRoot().getParent()).isNull();
        Assertions.assertThat(tree.getRoot().getLeft().getValue()).isEqualTo(2);
        Assertions.assertThat(tree.getRoot().getLeft().getParent()).isNotNull();
        Assertions.assertThat(tree.getRoot().getLeft().getParent().getValue()).isEqualTo(3);
        Assertions.assertThat(tree.getRoot().getRight().getValue()).isEqualTo(5);
        Assertions.assertThat(tree.getRoot().getRight().getParent()).isNotNull();
        Assertions.assertThat(tree.getRoot().getRight().getParent().getValue()).isEqualTo(3);
        Assertions.assertThat(tree.getRoot().getRight().getRight()).isNull();
    }

    @Test
    public void shouldDeleteLeaf2() {
        // given
        tree.insert(2);
        tree.insert(3);
        tree.insert(5);
        tree.insert(7);

        // when
        tree.delete(2);

        // then
        Assertions.assertThat(tree.getRoot().getValue()).isEqualTo(5);
        Assertions.assertThat(tree.getRoot().getLeft().getValue()).isEqualTo(3);
        Assertions.assertThat(tree.getRoot().getLeft().getLeft()).isNull();
        Assertions.assertThat(tree.getRoot().getRight().getValue()).isEqualTo(7);
    }

    @Test
    public void shouldDeleteMiddleNode() {
        // given
        tree.insert(2);
        tree.insert(3);
        tree.insert(5);
        tree.insert(7);

        // when
        tree.delete(3);

        // then
        Assertions.assertThat(tree.getRoot().getValue()).isEqualTo(5);
        Assertions.assertThat(tree.getRoot().getLeft().getValue()).isEqualTo(2);
        Assertions.assertThat(tree.getRoot().getRight().getValue()).isEqualTo(7);
    }

    @Test
    public void shouldDeleteMiddleNode2() {
        // given
        tree.insert(2);
        tree.insert(3);
        tree.insert(5);
        tree.insert(7);

        // when
        tree.delete(5);

        // then
        Assertions.assertThat(tree.getRoot().getValue()).isEqualTo(3);
        Assertions.assertThat(tree.getRoot().getLeft().getValue()).isEqualTo(2);
        Assertions.assertThat(tree.getRoot().getRight().getValue()).isEqualTo(7);
    }

    @Test
    public void shouldDeleteMiddleNode3() {
        // given
        tree.insert(2);
        tree.insert(3);
        tree.insert(4);
        tree.insert(5);
        tree.insert(7);

        // when
        tree.delete(3);

        // then
        Assertions.assertThat(tree.getRoot().getValue()).isEqualTo(4);
        Assertions.assertThat(tree.getRoot().getLeft().getValue()).isEqualTo(2);
        Assertions.assertThat(tree.getRoot().getRight().getValue()).isEqualTo(5);
        Assertions.assertThat(tree.getRoot().getRight().getRight().getValue()).isEqualTo(7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfAttemptingToDeleteNodeTwice() {
        // given
        tree.insert(2);
        tree.insert(3);

        // when
        tree.delete(3);
        tree.delete(3);

        // then throw exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfAttemptingToInsertSameElementTwice() {
        // when
        tree.insert(3);
        tree.insert(3);

        // then throw exception
    }

    @Test
    public void shouldJoinWithEmptyLeftTree() {
        // given
        AVLTree<Integer> emptyLeft = new AVLTree<>(comparator);
        AVLTree<Integer> right = new AVLTree<>(comparator);
        right.insert(1);

        // when
        AVLTree<Integer> joined = AVLTree.join(emptyLeft, right);

        // then
        assertThat(joined).isNotNull();
        assertThat(joined.isEmpty()).isFalse();
        Assertions.assertThat(joined.getRoot().getValue()).isEqualTo(1);
    }

    @Test
    public void shouldJoinWithEmptyRightTree() {
        // given
        AVLTree<Integer> left = new AVLTree<>(comparator);
        left.insert(1);
        AVLTree<Integer> emptyRight = new AVLTree<>(comparator);

        // when
        AVLTree<Integer> joined = AVLTree.join(left, emptyRight);

        // then
        assertThat(joined).isNotNull();
        assertThat(joined.isEmpty()).isFalse();
        Assertions.assertThat(joined.getRoot().getValue()).isEqualTo(1);
    }

    @Test
    public void shouldJoinTwoEquallyBalancedTrees() {
        // given
        AVLTree<Integer> left = new AVLTree<>(comparator);
        left.insert(1);
        left.insert(2);
        left.insert(3);
        AVLTree<Integer> right = new AVLTree<>(comparator);
        right.insert(4);
        right.insert(5);
        right.insert(6);
        right.insert(7);

        // when
        AVLTree<Integer> joined = AVLTree.join(left, right);

        // then
        assertThat(joined).isNotNull();
        assertThat(joined.isEmpty()).isFalse();
        Assertions.assertThat(joined.getRoot().getValue()).isEqualTo(3);
        Assertions.assertThat(joined.getRoot().getLeft().getValue()).isEqualTo(2);
        Assertions.assertThat(joined.getRoot().getRight().getValue()).isEqualTo(5);
    }

    @Test
    public void shouldJoinLargeLeftWithRightTree() {
        // given
        AVLTree<Integer> left = new AVLTree<>(comparator);
        left.insert(1);
        left.insert(2);
        left.insert(3);
        left.insert(4);
        left.insert(5);
        AVLTree<Integer> right = new AVLTree<>(comparator);
        right.insert(6);

        // when
        AVLTree<Integer> joined = AVLTree.join(left, right);

        // then
        assertThat(joined).isNotNull();
        assertThat(joined.isEmpty()).isFalse();
        Assertions.assertThat(joined.getRoot().getValue()).isEqualTo(4);
        Assertions.assertThat(joined.getRoot().getLeft().getValue()).isEqualTo(2);
        Assertions.assertThat(joined.getRoot().getLeft().getLeft().getValue()).isEqualTo(1);
        Assertions.assertThat(joined.getRoot().getLeft().getRight().getValue()).isEqualTo(3);
        Assertions.assertThat(joined.getRoot().getRight().getValue()).isEqualTo(5);
        Assertions.assertThat(joined.getRoot().getRight().getRight().getValue()).isEqualTo(6);
    }

    @Test
    public void shouldJoinLeftWithLargeLeftTree() {
        // given
        AVLTree<Integer> left = new AVLTree<>(comparator);
        left.insert(1);
        AVLTree<Integer> right = new AVLTree<>(comparator);
        right.insert(2);
        right.insert(3);
        right.insert(4);
        right.insert(5);
        right.insert(6);

        // when
        AVLTree<Integer> joined = AVLTree.join(left, right);

        // then
        assertThat(joined).isNotNull();
        assertThat(joined.isEmpty()).isFalse();
        Assertions.assertThat(joined.getRoot().getValue()).isEqualTo(3);
        Assertions.assertThat(joined.getRoot().getLeft().getValue()).isEqualTo(1);
        Assertions.assertThat(joined.getRoot().getLeft().getRight().getValue()).isEqualTo(2);
        Assertions.assertThat(joined.getRoot().getRight().getValue()).isEqualTo(5);
        Assertions.assertThat(joined.getRoot().getRight().getLeft().getValue()).isEqualTo(4);
        Assertions.assertThat(joined.getRoot().getRight().getRight().getValue()).isEqualTo(6);
    }

    // integration test from https://www.youtube.com/watch?v=FNeL18KsWPc&t=2162s
    @Test
    public void shouldBalanceTheTree() {
        // when
        tree.insert(11);
        tree.insert(41);
        tree.insert(65);
        tree.insert(55);
        tree.insert(50);
        tree.insert(20);
        tree.insert(29);
        tree.insert(26);
        tree.insert(23);

        // then
        Assertions.assertThat(tree.getRoot()).isNotNull();
        assertThat(height(tree.getRoot())).isLessThanOrEqualTo(4);
    }

    @Test
    public void shouldClear() {
        // give
        tree.insert(1);
        tree.insert(2);
        tree.insert(3);

        // when
        tree.clear();

        // then
        assertThat(tree.isEmpty()).isTrue();
    }

    private static int height(AVLNode<Integer> node) {
        int leftChildHeight = node.getLeft() != null ? height(node.getLeft()) : 0;
        int rightChildHeight = node.getRight() != null ? height(node.getRight()) : 0;
        return max(leftChildHeight, rightChildHeight) + 1;
    }
}
