package org.ric.tree;

import java.io.Serializable;

public class Tree<E extends Comparable<E>> implements Serializable {
    public static class TreeNode<E extends Comparable<E>> {
        private E data;
        private TreeNode<E> left, right, parent;

        public TreeNode(E data, TreeNode<E> parent) {
            this.data = data;
            this.parent = parent;
        }

        public TreeNode(E data) {
            this(data, null);
        }

        public E getData() {
            return data;
        }

        public void setData(E data) {
            this.data = data;
        }

        public TreeNode<E> getLeft() {
            return left;
        }

        void setLeft(TreeNode<E> left) {
            this.left = left;
        }

        public TreeNode<E> getRight() {
            return right;
        }

        void setRight(TreeNode<E> right) {
            this.right = right;
        }

        public TreeNode<E> getParent() {
            return parent;
        }

        void setParent(TreeNode<E> parent) {
            this.parent = parent;
        }
    }

    public Tree() {
        // TODO Auto-generated constructor stub
    }

}
