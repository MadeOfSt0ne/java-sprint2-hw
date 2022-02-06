package controller;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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
        linkLast(task);
    }

    // удаляем задачу из истории просмотров
    @Override
    public void remove(int id) {
        if (!nodeMap.containsKey(id)) {
            System.out.println("Ключ не найден = " + id);
        } else {
            nodeMap.remove(id);
            System.out.println("Просмотр удален: " + id);
        }
    }

    // добавление элемента на последнее место
    public void linkLast(Task task) {
        final Node oldNode = nodeMap.remove(task.getId());
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

