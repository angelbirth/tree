package org.ric.tree;

import java.io.Serializable;
import java.util.TreeMap;
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

        public boolean equals(TreeNode<T> n) {
            if (n == null)
                return false;
            if (!data.equals(n.data) && data.compareTo(n.data) != 0)
                return false;
            return true;
        }

        @Override
        public String toString() {
            // TODO Auto-generated method stub
            return data.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null)
                return false;
            if (!(obj instanceof TreeNode<?>))
                return false;
            return equals((TreeNode<T>) obj);
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

        boolean isLeaf() {
            return left == null && right == null;
        }

        boolean isRoot() {
            return parent == null;
        }

        int height() {
            if (isLeaf()) {
                return 1;
            } else {
                if (left != null && right == null) {
                    return left.height() + 1;
                } else if (left == null && right != null) {
                    return right.height() + 1;
                } else {
                    return Math.max(left.height(), right.height()) + 1;
                }
            }
        }

        private boolean isLeftChild() {
            if (isRoot())
                return false;
            return parent.left == this;
        }

        private boolean isRightChild() {
            if (isRoot())
                return false;
            return parent.right == this;
        }

        int childCount() {
            if (isLeaf())
                return 0;
            if (left != null && right != null)
                return 2;
            return 1;
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
        System.out.println();
    }

    public TreeNode<E> find(E data) {
        TreeNode<E> i = root;
        while (i != null) {
            if (data.compareTo(i.data) < 0) {
                i = i.left;
            } else if (data.compareTo(i.data) > 0) {
                i = i.right;
            } else
                return i;
        }
        return null;
    }

    private TreeNode<E> remove01(TreeNode<E> node) {
        if (node.left != null && node.right != null) {
            throw new IllegalStateException();
        }
        if (node.isRoot()) {
            if (node.isLeaf()) {
                root = null;
            } else {
                root = node.left != null ? node.left : node.right;
            }
        } else {
            // downward
            if (node.isLeftChild()) {
                node.parent.left = node.left != null ? node.left : node.right;
            } else if (node.isRightChild()) {
                node.parent.right = node.left != null ? node.left : node.right;
            }
        }
        // upward
        if (node.left != null) {
            node.left.parent = node.parent;
        } else if (node.right != null) {
            node.right.parent = node.parent;
        }
        node.left = node.right = node.parent = null;
        return node;
    }

    private TreeNode<E> remove2(TreeNode<E> node) {
        TreeNode<E> a = successor(node);
        TreeNode<E> successor = remove01(a);
        successor.left = node.left;
        successor.right = node.right;
        successor.parent = node.parent;
        if (node.isRoot()) {
            root = successor;
        } else {
            // downward
            if (node.isLeftChild()) {
                node.parent.left = successor;
            } else if (node.isRightChild()) {
                node.parent.right = successor;
            }
        }
        // upward
        if (node.left != null)
            node.left.parent = successor;
        if (node.right != null)
            node.right.parent = successor;
        node.left = node.right = node.parent = null;
        return node;
    }

    public TreeNode<E> remove(E data) {
        TreeNode<E> del = find(data);
        if (del == null)
            return null;
        switch (del.childCount()) {
        case 0:
        case 1:
            return remove01(del);
        default:
            return remove2(del);
        }
    }

    static <E extends Comparable<E>> TreeNode<E> successor(TreeNode<E> t) {
        if (t == null)
            return null;
        else if (t.right != null) {
            TreeNode<E> p = t.right;
            while (p.left != null)
                p = p.left;
            return p;
        } else {
            TreeNode<E> p = t.parent;
            TreeNode<E> ch = t;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }
}
