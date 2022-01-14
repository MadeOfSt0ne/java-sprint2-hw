import controller.InMemoryTasksManager;
import model.Epic;

public class Main {
    public static void main(String[] args) {
    //Праздники закончились, пришло время практики!
        InMemoryTasksManager inMemoryTasksManager = new InMemoryTasksManager();
        Epic epic = new Epic();
        epic.setName("e1");
        inMemoryTasksManager.createEpic(epic);
        System.out.println(inMemoryTasksManager.findAllEpics());

    }
}
