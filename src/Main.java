import controller.InMemoryTasksManager;
import model.Epic;

public class Main {
    public static void main(String[] args) {
    // Тесты
        InMemoryTasksManager inMemoryTasksManager = new InMemoryTasksManager();
        Epic epic = new Epic("epic 1", "descriprtion 1");
        inMemoryTasksManager.createEpic(epic);
        System.out.println(inMemoryTasksManager.findAllEpics());

    }
}
