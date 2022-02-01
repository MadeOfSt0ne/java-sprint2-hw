package controller;

import model.Task;

public class Node {
    Node prev;
    Task task;
    Node next;

    public Node(Task task) {
        this.prev = null;
        this.task = task;
        this.next = null;
    }
}
