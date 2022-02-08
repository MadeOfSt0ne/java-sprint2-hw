package controller;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    public InMemoryHistoryManager() {
    }

    HashMap<Integer, Node> nodeMap = new HashMap<>();
    Node first = null;
    Node last = null;

    // добавляем задачу в историю просмотров
    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        if (nodeMap.containsKey(task.getId())) {
            remove(task.getId());
        }
        linkLast(task);
    }

    // удаляем задачу из истории просмотров
    @Override
    public void remove(int id) {
        removeNode(nodeMap.get(id));
    }

    void removeNode(Node node) {
        Integer key = null;
        for (Map.Entry<Integer, Node> pair : nodeMap.entrySet()) {
            if (node.equals(pair.getValue())) {
                key = pair.getKey();
            }
        }
        final Node oldNode = nodeMap.remove(key);
        if (oldNode != null) {
            if (oldNode == first) {
                first = oldNode.next;
                last = last.prev;
            } else if (oldNode == last) {
                last = oldNode.prev;
                last.next = null;
            } else {
                oldNode.prev.next = oldNode.next;
            }
        }
    }

    // добавление элемента на последнее место
    public void linkLast(Task task) {
        final Node newNode = new Node(task);
        if (first == null) {
            first = newNode;
        } else {
            last.next = newNode;
            newNode.prev = last;
        }
        last = newNode;
        nodeMap.put(task.getId(), newNode);
    }

    @Override
    public List<Task> getHistory() {
        final ArrayList<Task> tasks = new ArrayList<>();
        Node current = first;
        while (current != null) {
            tasks.add(current.task);
            current = current.next;
        }
        return tasks;
    }
}

