# AVL Tree

Java implementation of [AVL tree](https://en.wikipedia.org/wiki/AVL_tree), a type of self-balancing binary search tree
that ensures that operations such as insertion, deletion, 
and lookup are performed in O(log n) time.

```xml
<dependency>
    <groupId>org.open-structures</groupId>
    <artifactId>avl-tree</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Quick start

AVL Tree can be used for any comparable elements. To create a tree you need to specify a `Comparator`:

    AVLTree<Integer> intTree = new AVLTree<>(Integer::compareTo);

Once you have a tree you can add a bunch of elements to it:

    intTree.insert(1);
    intTree.insert(5);
    intTree.insert(15);
    intTree.insert(999);

You can also delete elements:

    intTree.delete(15);

You can traverse the tree using `AVLNode` interface. It's returned when you are getting the root note:

    AVLNode<Integer> root = intTree.getRoot();
    root.getValue(); // 5
    root.getLeft().getValue(); // 1
    root.getRight().getValue(); // 999

`AVLNode` is also returned when you add a new element to the tree:

    AVLNode<Integer> newNode = intTree.insert(-10);
    newNode.getValue(); // -10
    newNode.getLeft(); // null
    newNode.getRight().getValue(); // 1

If you have two trees, and all the elements of one are less than or equal to any element of the other, you can join them to get a new tree:

    Comparator<Integer> c = Integer::compareTo;
    AVLTree<Integer> treeA = new AVLTree<>(c);
    AVLTree<Integer> treeB = new AVLTree<>(c);

    treeA.insert(1);
    treeA.insert(2);
    treeB.insert(3);
    treeB.insert(7);
    treeB.insert(10);

    AVLTree<Integer> joinedTree = AVLTree.join(treeA, treeB);

Calling `toString()` on `joinedTree` would output the following:

```
 2
/ \
1  7
  / \
  3 10
```    