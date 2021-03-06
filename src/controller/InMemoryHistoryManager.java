package controller;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

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
    public void remove(Integer id) {
        removeNode(nodeMap.get(id));
        nodeMap.remove(id);
    }

    // удаление узла
    /*public void removeNode1(Node node) {
        if (node != null) {
            if (node.prev == null) {
                first = node.next;
            } else {
                node.prev.next = node.next;
                node.prev = null;
            }
            if (node.next == null) {
                last = null;
            } else {
                node.next.prev = null;
                node.next = null;
            }
        }
    }*/

    // метод удаления узла
    public void removeNode(Node node) {
        try {
            if (node == first) {
                if (node != last) {
                    first.prev = null;
                } else {
                    last = null;
                }
                first = node.next;
                node.next = null;
            } else if (node == last) {
                node.prev.next = null;
                node.prev = null;
                last = last.prev;
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
                node.prev = null;
                node.next = null;
            }
        } catch (NullPointerException e) {
            return;
        }
    }

    // добавление элемента на последнее место
    public void linkLast(Task task) {
        final Node newNode = new Node(task);
        if (last == null) {
            first = newNode;
        } else {
            last.next = newNode;
            newNode.prev = last;
        }
        last = newNode;
        nodeMap.put(task.getId(), newNode);
    }

    // получение истории просмотров
    @Override
    public List<Integer> getHistory() {
        return getTasks();
    }

    public List<Integer> getTasks() {
        final List<Integer> tasks = new ArrayList<>();
        Node current = first;
        while (current != null) {
            tasks.add(current.task.getId());
            current = current.next;
        }
        return tasks;
    }
}

