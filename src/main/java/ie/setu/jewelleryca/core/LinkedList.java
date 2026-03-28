package ie.setu.jewelleryca.core;

import java.io.Serializable;

public class LinkedList<T> implements Serializable {

    // each node holds the data and a link to the next node
    private class Node implements Serializable {
        T data;
        Node next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;
    private int size;

    public LinkedList() {
        head = null;
        size = 0;
    }

    public void add(T data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {  // goes to the last node
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    // returns the item at the given position
    public T get(int index) {
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    // removes item at the given position, returns true if it worked
    public boolean remove(int index) {
        if (index < 0 || index >= size) {
            return false;
        }
        if (index == 0) {
            head = head.next;  // removing the first one
        } else {
            Node current = head;
            for (int i = 0; i < index - 1; i++) {  // stops at the node before
                current = current.next;
            }
            current.next = current.next.next;
        }
        size--;
        return true;
    }

    // clears the whole list
    public void clear() {
        head = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}