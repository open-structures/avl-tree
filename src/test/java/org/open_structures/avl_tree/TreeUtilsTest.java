package org.open_structures.avl_tree;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TreeUtilsTest {

    @Test
    public void shouldPrintTheTree() {
        // given
        AVLTree<Integer> tree = new AVLTree<>(Integer::compareTo);

        // and
        tree.insert(1);

        // when and then
        assertThat(TreeUtils.print(tree)).isEqualTo("1");

        // and given
        tree.insert(-7);
        tree.insert(99);
        tree.insert(-10);
        tree.insert(-2);
        tree.insert(3);
        tree.insert(1000);

        // when and then
        assertThat(TreeUtils.print(tree)).isEqualTo("""
                       1
                      / \\
                   -7    99
                  /  \\  /  \\
                -10  -2 3  1000""");
    }
}
