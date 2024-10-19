import java.util.NoSuchElementException;

public class LinkedDS<T extends Comparable<? super T>> implements SequenceInterface<T>, ReorderInterface, Comparable<LinkedDS<T>> {

    private Node head; // First node in the chain
    private Node tail; // Last node in the chain
    private int size;  // Tracks the number of items

    // Node inner class to represent each element in the linked chain
    private class Node {
        T data;
        Node next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    // Default constructor
    public LinkedDS() {
        head = null;
        tail = null;
        size = 0;
    }

    // Copy constructor
    public LinkedDS(LinkedDS<T> other) {
        if (other == null || other.isEmpty()) {
            head = null;
            tail = null;
            size = 0;
        } else {
            for (Node current = other.head; current != null; current = current.next) {
                append(current.data);
            }
        }
    }

    // Append an item to the end of the sequence
    @Override
    public void append(T item) {
        Node newNode = new Node(item);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    // Prefix an item to the beginning of the sequence
    @Override
    public void prefix(T item) {
        Node newNode = new Node(item);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }
        size++;
    }

    // Insert an item at a specific position in the sequence
    @Override
    public void insert(T item, int position) {
        if (position < 0 || position > size) throw new IndexOutOfBoundsException();

        if (position == 0) {
            prefix(item);
            return;
        }

        if (position == size) {
            append(item);
            return;
        }

        Node newNode = new Node(item);
        Node previous = null;
        Node current = head;

        for (int i = 0; i < position; i++) {
            previous = current;
            current = current.next;
        }

        newNode.next = current;
        previous.next = newNode;
        size++;
    }

    // Delete and return the head of the sequence
    @Override
    public T deleteHead() {
        if (isEmpty()) throw new EmptySequenceException("Sequence is empty");
        T data = head.data;
        head = head.next;
        size--;
        if (size == 0) tail = null;
        return data;
    }

    // Delete and return the tail of the sequence
    @Override
    public T deleteTail() {
        if (isEmpty()) throw new EmptySequenceException("Sequence is empty");
        if (size == 1) {
            T data = head.data;
            head = null;
            tail = null;
            size = 0;
            return data;
        }

        Node current = head;
        while (current.next != tail) {
            current = current.next;
        }

        T data = tail.data;
        tail = current;
        tail.next = null;
        size--;
        return data;
    }

    // Return the size of the sequence
    @Override
    public int size() {
        return size;
    }

    // Return whether the sequence is empty
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Return the first item in the sequence
    @Override
    public T first() {
        return (isEmpty()) ? null : head.data;
    }

    // Return the last item in the sequence
    @Override
    public T last() {
        return (isEmpty()) ? null : tail.data;
    }

    // Return the item at a specific position in the sequence
    @Override
    public T itemAt(int position) {
        if (position < 0 || position >= size) throw new IndexOutOfBoundsException();
        Node current = head;
        for (int i = 0; i < position; i++) {
            current = current.next;
        }
        return current.data;
    }

    // Return the number of occurrences of an item
    @Override
    public int getFrequencyOf(T item) {
        int count = 0;
        for (Node current = head; current != null; current = current.next) {
            if (current.data.equals(item)) {
                count++;
            }
        }
        return count;
    }

    // Return the last occurrence of an item
    @Override
    public int lastOccurrenceOf(T item) {
        int lastPosition = -1;
        int index = 0;
        for (Node current = head; current != null; current = current.next) {
            if (current.data.equals(item)) {
                lastPosition = index;
            }
            index++;
        }
        return lastPosition;
    }

    // Reverse the sequence
    @Override
    public void reverse() {
        if (isEmpty()) return;

        Node prev = null;
        Node current = head;
        tail = head;

        while (current != null) {
            Node nextNode = current.next;
            current.next = prev;
            prev = current;
            current = nextNode;
        }
        head = prev;
    }

    // Rotate the sequence to the right
    @Override
    public void rotateRight() {
        if (size <= 1) return;

        Node current = head;
        while (current.next != tail) {
            current = current.next;
        }

        current.next = null;
        tail.next = head;
        head = tail;
        tail = current;
    }

    // Rotate the sequence to the left
    @Override
    public void rotateLeft() {
        if (size <= 1) return;

        tail.next = head;
        head = head.next;
        tail = tail.next;
        tail.next = null;
    }

    // Compare two LinkedDS objects lexicographically
    @Override
    public int compareTo(LinkedDS<T> other) {
        Node currentThis = head;
        Node currentOther = other.head;

        while (currentThis != null && currentOther != null) {
            int comparison = currentThis.data.compareTo(currentOther.data);
            if (comparison != 0) return comparison;

            currentThis = currentThis.next;
            currentOther = currentOther.next;
        }

        return Integer.compare(size, other.size());
    }

    // Convert the sequence to a string (no spaces)
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Node current = head; current != null; current = current.next) {
            sb.append(current.data);
        }
        return sb.toString();
    }

    // Clear the sequence
    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }
}
