/***********************************
 * Course: Lehigh CSE017-SU2024
 * Assignment: Final (v01)
 * Name: Yinglong Lin
 * UID: yile22
 * **********************************/

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
    Doubly LinkedList Generic Class.
    based in part on slide 30+ of
    https://docs.google.com/presentation/d/1KgWd9D5LVe2H2tpcWYgNnk8GQxp3yJWZ1u6yA1eg9UU/edit?usp=sharing
 */
public class DoublyLinkedList<E> implements Iterable<E>{
    // Data members
    private Node head, tail;
    private int size;
    /**
        Inner class Node
    */
    private class Node {
        E value;
        Node next;
        Node previous;
        Node(E initialValue) {
            value = initialValue;
            next = previous = null;
        }
    }
    /**
        Default Constructor
        creates an empty linkedlist
        Time complexity: O(1)
    */
    public DoublyLinkedList() {
        head = tail = null;
        size = 0;
    }
    /**
        size method
        @return the number of nodes in the list
        Time complexity: O(1)
    */
    public int size() {
        return size;
    }
    /**
        clear method
        resets size to 0 and head and tail to null
        Time complexity: O(1)
    */
    public void clear() {
        head = tail = null;
        size = 0;
    }
    /**
        isEmpty method
        @return true if the list is empty
        Time complexity: O(1)
    */
    public boolean isEmpty() {
        return (size == 0);
    }
    /**
        Adding a value at the tail of the list
        calls addLast
        @param value to be added
        @return true if the operation was successful
        Time complexity: O(1)
    */
    public boolean add(E item) {
        return addLast(item);
    }
    /**
     * Linear search method
     * @param o the object being searched
     * @return true if o was found in this list, false otherwise
     * Time complexity: O(n)
     */
    public boolean contains(Object o){
        Iterator<E> iter = iterator();
        while(iter.hasNext()){
            E element = iter.next();
            if(element.equals(o)){
                return true;
            }
        }
        return false;
    }
    /**
        Adding a value at the head of the list
        @param value to be added
        @return true if the operation was successful
        Time complexity: O(1)
    */
    public boolean addFirst(E value) {
        Node newNode = new Node(value);
        if (head == null) {
            head = tail = newNode;
        }
        else {
            newNode.next = head;
            head.previous = newNode;
            head = newNode;
        }
        size++;
        return true;
    }
    /**
        Adding a value at the tail of the list
        @param value to be added
        @return true if the operation was successful
        Time complexity: O(1)
    */
    public boolean addLast(E item) {
        Node newNode = new Node(item);
        if (head == null) {
            head = tail = newNode;
        }
        else{
            tail.next = newNode;
            newNode.previous = tail;
            tail = newNode;
        }
        size++;
        return true;
    }
    /**
        Method to add a new item at a given position index
        @param index the position where item should be added
        @param item the value of the element to be added
        @return true if item was added successfully, false otherwise
        @throws ArrayIndexOutOfBoundsException if index < 0 or index > size
        Time complexity: O(n)
    */
    public void add(int index, E item){
        if(index < 0 || index > size){
            throw new ArrayIndexOutOfBoundsException();
        }
        if(index == 0){
            addFirst(item);
        }
        else if (index == size){
            addLast(item);
        }
        else{
            Node current = head;
            Node previous = null;
            for(int i=0; i<index; i++){
                previous = current;
                current = current.next;
            }
            Node newNode = new Node(item);
            previous.next = newNode;
            newNode.previous = previous;
            newNode.next = current;
            current.previous = newNode;
            size++;
        }
    }
    /**
        Get the value of the node at the head of the list
        @return value of the node at the head
        @throws NoSuchElementException if the list is empty
        Time complexity: O(1)
    */
    public E getFirst() {
        if (head == null)
            throw new NoSuchElementException();
        return head.value;
    }
    /**
        Get the value of the node at the tail of the list
        @return value of the node at the tail
        @throws NoSuchElementException if the list is empty
        Time complexity: O(1)
     */
    public E getLast() {
        if (head == null)
            throw new NoSuchElementException();
        return tail.value;
    }
    /**
        Removes the node at the head of the list
        @return true if the remove operation was successful and decrements the size
        @throws NoSuchElementException if the list is empty
        Time complexity: O(1)
     */
    public boolean removeFirst() {
        if (head == null)
            throw new NoSuchElementException();
        head = head.next;
        if (head == null) // removed the only node in the LL
            tail = null;
        else
            head.previous = null;
        size--;
        return true;
    }
    /**
        Removes the node at the tail of the list
        @return true if the remove operation was successful and decrements the size
        @throws NoSuchElementException if the list is empty
        Time complexity: O(1)
    */
    public boolean removeLast() {
        if (head == null)
            throw new NoSuchElementException();
        if (size == 1)
            return removeFirst();
        tail = tail.previous;
        tail.next = null;
        size--;
        return true;
    }
    /**
     * Remove an object o from the list
     * @param o the object to be removed
     * @return true if o was found and removed, false if o not found
     * Time complexity: O(n)
     */
    public boolean remove(Object o){
        Node current = head;
        Node previous = null;
        while(current != null  && !current.value.equals(o)){
            previous = current;
            current = current.next;
        }
        if(current == null){
            return false;
        }
        if(previous == null){ // remove the first node
            return removeFirst();
        }
        previous.next = current.next;
        current.next.previous = previous;
        size--;
        return true;
    }
    /**
        toString method
        @return a formatted string that contains the values of all the nodes in the list
        Time complexity: O(n)
    */
    public String toString() {
        String output = "[";
        Node node = head;
        while (node.next != null) {
            output += node.value + " ";
            node = node.next;
        }
        output += node.value + "]";
        return output;
    }
    

    /**
        iterator method
        @override iterator() from the interface Collection
        @return an iterator object pointing to the first value in the list
        Time complexity: O(1)
    */
    public Iterator<E> iterator() {
        return new LinkedListIterator();
    }
    /**
        Inner class that implements the interface Iterator
    */
    private class LinkedListIterator implements Iterator<E> {
        private Node current = head;
        /**
            hasNext method
            @return true if the current is not null
            Time complexity: O(1)
         */
        public boolean hasNext() {
            return (current != null);
        }
        /**
            next method
            @return the value of the node referenced by current and 
                    modifies current to hold the reference of the next node
            @throws NoSuchElementException if current is null
            Time complexity: O(1)
         */
        public E next() {
            if (current == null)
                throw new NoSuchElementException();
            E value = current.value;
            current = current.next;
            return value;
        }
    }
    

    public E get(int index){
        if(index < 0 || index >= size){
            throw new ArrayIndexOutOfBoundsException();
        }
        Node current = head;
        for(int i=0; i<index; i++){
            current = current.next;
        }
        return current.value;
    }

    public E set(int index, E newVal){
        if(index < 0 || index >= size){
            throw new ArrayIndexOutOfBoundsException();
        }
        Node current = head;
        for(int i=0; i<index; i++){
            current = current.next;
        }
        E oldVal = current.value;
        current.value = newVal;
        return oldVal;
    }
    public int indexOf(Object o){
        Node current = head;
        for(int i=0; i<size; i++){
            if(current.value.equals(o))
                return i;
            current = current.next;
        }
        return -1;
    }
    public int lastIndexOf(Object o){
        Node current = tail;
        for(int i=size-1; i>=0; i--){
            if(current.value.equals(o))
                return i;
            current = current.previous;
        }
        return -1;
    }
    public E remove(int index){
        if(index < 0 || index >= size){
            throw new ArrayIndexOutOfBoundsException();
        }
        E value = null;
        if (index == 0){
            value = getFirst();
            removeFirst();
        }
        else if(index == size-1){
            value = getLast();
            removeLast();
        }
        else{
            Node current = head;
            Node previous = null;
            for(int i=0; i<index; i++){
                previous = current;
                current = current.next;
            }
            previous.next = current.next;
            current.next.previous = previous;
            size--;
            value = current.value;
        }
        return value;
    }
}