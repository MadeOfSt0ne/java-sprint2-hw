import controller.Manager;
import model.Epic;

public class Main {
    public static void main(String[] args) {
    //Праздники закончились, пришло время практики!
        Manager manager = new Manager();
        Epic epic = new Epic(); epic.setName("e1"); manager.createEpic(epic); System.out.println(manager.findAllEpics());

    }
}
