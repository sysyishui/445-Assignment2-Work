public class LinkedDS<T extends Comparable<? super T>> implements SequenceInterface<T>, ReorderInterface, Comparable<LinkedDS<T>> {

    private Node firstNode;  // Reference to the first node
    private int numberOfEntries;  // Number of elements in the list

    private class Node {
        private T data; // Entry in the sequence
        private Node next; // Link to the next node

        private Node(T data) {
            this.data = data;
            this.next = null;
        }

        private Node(T data, Node nextNode) {
            this.data = data;
            this.next = nextNode;
        }
    }

    // Default constructor
    public LinkedDS() {
        firstNode = null;
        numberOfEntries = 0;
    }

    // Copy constructor
    public LinkedDS(LinkedDS<T> other) {
        if (other == null || other.isEmpty()) {
            firstNode = null;
            numberOfEntries = 0;
        } else {
            this.firstNode = new Node(other.firstNode.data);
            Node currentNode = this.firstNode;
            Node otherNode = other.firstNode.next;

            while (otherNode != null) {
                currentNode.next = new Node(otherNode.data);
                currentNode = currentNode.next;
                otherNode = otherNode.next;
            }
            numberOfEntries = other.numberOfEntries;
        }
    }

    // SequenceInterface methods
    @Override
    public int size() {
        return numberOfEntries;
    }

    @Override
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

    @Override
    public T first() {
        if (isEmpty()) {
            return null;
        }
        return firstNode.data;
    }

    @Override
    public T last() {
        if (isEmpty()) {
            return null;
        }

        Node currentNode = firstNode;
        while (currentNode.next != null) {
            currentNode = currentNode.next;
        }
        return currentNode.data;
    }

    @Override
    public T itemAt(int index) {
        if (index < 0 || index >= numberOfEntries) {
            throw new IndexOutOfBoundsException();
        }

        Node currentNode = firstNode;
        for (int i = 0; i < index; i++) {
            currentNode = currentNode.next;
        }
        return currentNode.data;
    }

    @Override
    public void append(T newEntry) {
        Node newNode = new Node(newEntry);

        if (isEmpty()) {
            firstNode = newNode;
        } else {
            Node lastNode = firstNode;
            while (lastNode.next != null) {
                lastNode = lastNode.next;
            }
            lastNode.next = newNode;
        }
        numberOfEntries++;
    }

    @Override
    public void prefix(T newEntry) {
        Node newNode = new Node(newEntry, firstNode);
        firstNode = newNode;
        numberOfEntries++;
    }

    @Override
    public T deleteHead() {
        if (!isEmpty()) {
            T data = firstNode.data;
            firstNode = firstNode.next;
            numberOfEntries--;
            return data;
        }
        return null;
    }

    @Override
    public T deleteTail() {
        if (!isEmpty()) {
            if (numberOfEntries == 1) {
                T data = firstNode.data;
                firstNode = null;
                numberOfEntries--;
                return data;
            } else {
                Node currentNode = firstNode;
                while (currentNode.next.next != null) {
                    currentNode = currentNode.next;
                }
                T data = currentNode.next.data;
                currentNode.next = null;
                numberOfEntries--;
                return data;
            }
        }
        return null;
    }

    @Override
    public int getFrequencyOf(T entry) {
        int frequency = 0;
        Node currentNode = firstNode;
        while (currentNode != null) {
            if (currentNode.data.equals(entry)) {
                frequency++;
            }
            currentNode = currentNode.next;
        }
        return frequency;
    }

    @Override
    public void clear() {
        firstNode = null;
        numberOfEntries = 0;
    }

    @Override
public boolean cut(int index1, int index2) {
    if (index1 < 0 || index2 >= numberOfEntries || index1 > index2) {
        return false; // If the indices are invalid, return false
    }

    Node currentNode = firstNode;
    Node prevNode = null;
    
    // Traverse to the first index
    for (int i = 0; i < index1; i++) {
        prevNode = currentNode;
        currentNode = currentNode.next;
    }

    // Remove nodes from index1 to index2
    for (int i = index1; i <= index2; i++) {
        if (currentNode != null) {
            currentNode = currentNode.next;
            numberOfEntries--;
        }
    }

    // Link previous node to the node after the cut range
    if (prevNode != null) {
        prevNode.next = currentNode;
    } else {
        firstNode = currentNode;  // If starting from the head, update the head
    }

    return true; // Return true to indicate a successful operation
}


    @Override
public LinkedDS<T> slice(int index1, int index2) {
    if (index1 < 0 || index2 >= numberOfEntries || index1 > index2) {
        throw new IndexOutOfBoundsException("Invalid indices for slicing.");
    }

    LinkedDS<T> result = new LinkedDS<>();
    Node currentNode = firstNode;
    
    // Traverse to index1
    for (int i = 0; i < index1; i++) {
        currentNode = currentNode.next;
    }

    // Add nodes from index1 to index2 to the result
    for (int i = index1; i <= index2; i++) {
        if (currentNode != null) {
            result.append(currentNode.data);
            currentNode = currentNode.next;
        }
    }

    return result; // Return the sliced linked list
    }


    @Override
    public LinkedDS<T> slice(T entry) {
        LinkedDS<T> result = new LinkedDS<>();
        Node currentNode = firstNode;
        while (currentNode != null) {
            if (currentNode.data.equals(entry)) {
                result.append(currentNode.data);
            }
            currentNode = currentNode.next;
        }
        return result;
    }

    @Override
    public void rotateRight() {
        if (numberOfEntries <= 1) return;

        Node lastNode = firstNode;
        Node secondLastNode = null;
        while (lastNode.next != null) {
            secondLastNode = lastNode;
            lastNode = lastNode.next;
        }

        lastNode.next = firstNode;
        firstNode = lastNode;
        secondLastNode.next = null;
    }

    @Override
    public void rotateLeft() {
        if (numberOfEntries <= 1) return;

        Node oldHead = firstNode;
        Node lastNode = firstNode;
        while (lastNode.next != null) {
            lastNode = lastNode.next;
        }
        firstNode = oldHead.next;
        oldHead.next = null;
        lastNode.next = oldHead;
    }

    @Override
    public void reverse() {
        if (isEmpty()) return;

        Node prev = null;
        Node current = firstNode;
        Node next;
        while (current != null) {
            next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        firstNode = prev;
    }

    @Override
    public void shuffle(int[] arr1, int[] arr2) {
        // Implement shuffle logic as required, possibly interleaving arr1 and arr2 based on the assignment requirements.
    }

    @Override
    public T predecessor(T entry) {
        Node currentNode = firstNode;
        T predecessor = null;
        while (currentNode != null && !currentNode.data.equals(entry)) {
            predecessor = currentNode.data;
            currentNode = currentNode.next;
        }
        return predecessor;
    }

    @Override
    public void insert(T entry, int index) {
        if (index < 0 || index > numberOfEntries) {
            throw new IndexOutOfBoundsException();
        }

        if (index == 0) {
            prefix(entry);
        } else {
            Node currentNode = firstNode;
            for (int i = 0; i < index - 1; i++) {
                currentNode = currentNode.next;
            }
            Node newNode = new Node(entry, currentNode.next);
            currentNode.next = newNode;
            numberOfEntries++;
        }
    }

    @Override
    public int lastOccurrenceOf(T entry) {
        int lastOccurrence = -1;
        Node currentNode = firstNode;
        int index = 0;
        while (currentNode != null) {
            if (currentNode.data.equals(entry)) {
                lastOccurrence = index;
            }
            index++;
            currentNode = currentNode.next;
        }
        return lastOccurrence;
    }

    @Override
    public boolean trim(int numberOfElements) {
        if (numberOfElements >= numberOfEntries) {
            return false;
        }
        for (int i = 0; i < numberOfEntries - numberOfElements; i++) {
            deleteTail();
        }
        return true;
    }

    // Comparable Interface method
    @Override
    public int compareTo(LinkedDS<T> other) {
        Node thisNode = this.firstNode;
        Node otherNode = other.firstNode;

        while (thisNode != null && otherNode != null) {
            int comparison = thisNode.data.compareTo(otherNode.data);
            if (comparison != 0) {
                return comparison;
            }
            thisNode = thisNode.next;
            otherNode = otherNode.next;
        }

        if (thisNode == null && otherNode == null) {
            return 0;  // Equal
        } else if (thisNode == null) {
            return -1; // This is smaller
        } else {
            return 1;  // This is larger
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node currentNode = firstNode;
        while (currentNode != null) {
            sb.append(currentNode.data);
            currentNode = currentNode.next;
        }
        return sb.toString();  // Return the concatenated string
    }
}
