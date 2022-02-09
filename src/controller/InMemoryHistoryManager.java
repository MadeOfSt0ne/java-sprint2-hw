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
    public void remove(Integer id) {
        removeNode(nodeMap.get(id));
    }

    // удаление узла
    public void removeNode(Node node) {
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
    }

    // второй метод удаления узла, пока что не работает
    public void removeNode1(Node node) {
        if (node != null) {
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
                node.prev.next = node.next;  // NPE
                node.next.prev = node.prev;  // NPE
                node.prev = null;
                node.next = null;
            }
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
    public List<Task> getHistory() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node current = first;
        while (current != null) {
            tasks.add(current.task);
            current = current.next;
        }
        return tasks;
    }
}

