package oppo.lab.first.common;

public class CircularLinkedList<T> {
    private Node<T> tail;
    private int size;

    private static class Node<T> {
        private T data;
        private Node<T> next;

        public Node(T data) {
            this.data = data;
        }
    }

    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (tail == null) {
            tail = newNode;
            tail.next = tail;
        } else {
            newNode.next = tail.next;
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public T remove() {
        if (tail == null) return null;
        Node<T> head = tail.next;
        if (head == tail) {
            tail = null;
        } else {
            tail.next = head.next;
        }
        size--;
        return head.data;
    }

    public int size() {
        return size;
    }
}
