package controller;

import model.Epic;
import model.Subtask;
import model.Task;

import java.io.File;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTasksManager implements TaskManager {
   private File file;
   HistoryManager history = new InMemoryHistoryManager();
   public FileBackedTasksManager(File file) {
       this.file = file;
   }

   public void save() {

   }

   public String toString(Task task) {

   }

   public Task fromString(String value) {

   }

   public static String toString(HistoryManager history) {

   }

   public static List<Integer> fromString(String value) {

   }

   @Override
   public Task findTaskById(Integer id) {
      super.findTaskById(id);
      save();
      return null;
   }

   @Override
   public Subtask findSubtaskById(Integer id) {
      super.findSubtaskById(id);
      save();
      return null;
   }

   @Override
   public Epic findEpicById(Integer id) {
      super.findEpicById(id);
      save();
      return null;
   }

   @Override
   public Task createTask(Task task) {
      super.createTask(task);
      save();
      return null;
   }

   @Override
   public Subtask createSubtask(Subtask task) {
      super.createSubtask(task);
      save();
      return null;
   }

   @Override
   public Epic createEpic(Epic epic) {
      super.createEpic(epic);
      save();
      return null;
   }

   @Override
   public void deleteTask(Integer id) {
      super.deleteTask(id);
      save();
   }

   @Override
   public void deleteSubtask(Integer id) {
      super.deleteSubtask(id);
      save();
   }

   @Override
   public void deleteEpic(Integer id) {
      super.deleteEpic(id);
      save();
   }
}
