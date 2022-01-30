package controller;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    HashMap<Integer, Node> nodeMap = new HashMap<>();
    List<Task> history = new DoubleLinkedList<>();

    public void removeNode(Integer id) {
        nodeMap.remove(id);
    }

    // добавляем задачу в историю просмотров
    @Override
    public void add(Task task) {
        if (nodeMap.containsKey(task.getId())) {
            removeNode(task.getId());
        } else {
            history.add(task);
        }
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

    // получаем историю просмотров
    @Override
    public List<Task> getHistory() {
        return null;
    }

    public class DoubleLinkedList<Task> extends LinkedList<Task> {
        private Node first;
        private Node last;

        public void linkLast(Task task) {
            Node l = last;
            Node newNode = new Node(l, (model.Task) task, null);
            if (l == null) {
                first = newNode;
            } else {
                l.next = newNode;
            }
        }

        public List<Task> getTasks(DoubleLinkedList list) {
            return new ArrayList<>(history);
        }
    }
}
