import java.util.ArrayList;
import java.util.List;
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

    // Find the predecessor of an item in the sequence
    public T predecessor(T item) {
        if (head == null || head.data.equals(item)) {
            return null; // No predecessor if the item is at the head or if the list is empty
        }

        Node prev = null;  // This will store the predecessor node
        Node current = head;
        T lastPredecessor = null; // Track the last predecessor found

        // Traverse the list to find the target item
        while (current != null) {
            if (current.data.equals(item)) {
                lastPredecessor = (prev != null) ? prev.data : null; // Update last predecessor found
            }
            prev = current;
            current = current.next;
        }

        return lastPredecessor; // Return the last found predecessor
    }

    // Trim the sequence to a specific size
    public boolean trim(int newSize) {
        if (newSize >= size) {
            return false; // No trimming needed if newSize is greater than or equal to current size
        }

        Node current = head;
        // Traverse to the new tail node at position (newSize - 1)
        for (int i = 0; i < newSize - 1; i++) {
            current = current.next;
        }

        // Set the next node of the new tail to null and update the tail
        current.next = null;
        tail = current;

        // Update the size of the sequence
        size = newSize;

        return true;
    }

    // Cut a part of the sequence
    public boolean cut(int start, int length) {
        if (start < 0 || length <= 0 || start + length > size) return false;

        if (start == 0) {
            for (int i = 0; i < length; i++) {
                deleteHead();
            }
            return true;
        }

        Node current = head;
        for (int i = 0; i < start - 1; i++) {
            current = current.next;
        }

        Node cutStart = current;
        for (int i = 0; i < length; i++) {
            cutStart = cutStart.next;
        }

        current.next = cutStart.next;
        if (cutStart == tail) {
            tail = current;
        }

        size -= length;
        return true;
    }

    // Shuffle the sequence based on two position arrays
    public void shuffle(int[] oldPos, int[] newPos) {
        if (oldPos.length != newPos.length || oldPos.length != size) return;

        List<Node> nodes = new ArrayList<>(); // Use ArrayList to store the nodes
        Node current = head;

        // Populate the list with nodes
        for (int i = 0; i < size; i++) {
            nodes.add(current);
            current = current.next;
        }

        // Reassign head and adjust node positions based on newPos
        head = nodes.get(newPos[0]);
        current = head;
        for (int i = 1; i < size; i++) {
            current.next = nodes.get(newPos[i]);
            current = current.next;
        }

        // Update the tail and set the last node's next to null
        tail = current;
        tail.next = null;
    }

    // Slice a portion of the sequence based on item (including the item)
    @Override
    public SequenceInterface<T> slice(T item) {
        Node current = head;
        int index = 0;

        // Find the first occurrence of the item
        while (current != null && !current.data.equals(item)) {
            current = current.next;
            index++;
        }

        if (current == null) {
            throw new NoSuchElementException("Item not found in sequence");
        }

        // Return the slice starting from the item found
        return slice(index);
    }

    // Slice a portion of the sequence from start to end (inclusive of start, exclusive of end)
    @Override
    public SequenceInterface<T> slice(int start, int end) {
        if (start < 0 || end > size || start >= end) throw new IndexOutOfBoundsException();

        LinkedDS<T> newSeq = new LinkedDS<>();
        Node current = head;

        // Move to the starting position
        for (int i = 0; i < start; i++) {
            current = current.next;
        }

        // Add items from start to end (exclusive of end)
        for (int i = start; i < end; i++) {
            newSeq.append(current.data);
            current = current.next;
        }

        return newSeq;
    }

    public SequenceInterface<T> slice(int start) {
        return slice(start, size);
    }
}
