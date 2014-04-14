package org.ric.tree;

import java.io.Serializable;
import java.util.function.Consumer;

public class Tree<E extends Comparable<E>> implements Serializable {
    public static final int PREORDER = 0, INORDER = 1, POSTORDER = 2;

    public static class TreeNode<T extends Comparable<T>> {
        private T data;
        private TreeNode<T> left, right, parent;

        public TreeNode(T data, TreeNode<T> parent) {
            this.data = data;
            this.parent = parent;
        }

        public TreeNode(T data) {
            this(data, null);
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public TreeNode<T> getLeft() {
            return left;
        }

        void setLeft(TreeNode<T> left) {
            this.left = left;
        }

        public TreeNode<T> getRight() {
            return right;
        }

        void setRight(TreeNode<T> right) {
            this.right = right;
        }

        public TreeNode<T> getParent() {
            return parent;
        }

        void setParent(TreeNode<T> parent) {
            this.parent = parent;
        }
    }

    private TreeNode<E> root;

    public Tree() {
        root = null;
    }

    public TreeNode<E> getRoot() {
        return root;
    }

    private void insert(E data, TreeNode<E> root) {
        if (data.compareTo(root.data) < 0) {
            if (root.left == null) {
                root.left = new TreeNode<>(data, root);
            } else {
                insert(data, root.left);
            }
        } else if (data.compareTo(root.data) > 0) {
            if (root.right == null) {
                root.right = new TreeNode<>(data, root);
            } else {
                insert(data, root.right);
            }
        }
    }

    public void insert(E data) {
        if (root == null) {
            root = new TreeNode<>(data);
        } else {
            insert(data, root);
        }
    }

    private void preOrder(TreeNode<E> localRoot, Consumer<E> action) {
        if (localRoot == null) {
            return;
        }
        action.accept(localRoot.data);
        preOrder(localRoot.left, action);
        preOrder(localRoot.right, action);
    }

    private void inOrder(TreeNode<E> localRoot, Consumer<E> action) {
        if (localRoot == null) {
            return;
        }
        inOrder(localRoot.left, action);
        action.accept(localRoot.data);
        inOrder(localRoot.right, action);
    }

    private void postOrder(TreeNode<E> localRoot, Consumer<E> action) {
        if (localRoot == null) {
            return;
        }
        postOrder(localRoot.left, action);
        postOrder(localRoot.right, action);
        action.accept(localRoot.data);
    }

    public void traverse(int order, Consumer<E> action) {
        switch (order) {
        case PREORDER:
            preOrder(root, action);
            break;
        case INORDER:
            inOrder(root, action);
            break;
        case POSTORDER:
            postOrder(root, action);
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    public void traverse(int order) {
        traverse(order, x -> System.out.print(x + " "));
    }
}
