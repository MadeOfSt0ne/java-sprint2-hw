package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer {
    HTTPTaskManager httpManager = new HTTPTaskManager();
    private static final int PORT = 8080;
    private final HttpServer server;
    int id;
    Gson gson;

    public HttpTaskServer() throws IOException {
        server = HttpServer.create();
        server.bind(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks/task", (h) -> {
            try {
                System.out.println("\n/tasks/task");
                String query = h.getRequestURI().getQuery();
                id = Integer.parseInt(query.split("=")[1]);
                switch (h.getRequestMethod()) {
                    case "POST":
                        if (query.isEmpty()) {
                            System.out.println("Key для сохранения пустой. key указывается в пути: /tasks/task/?d={key}");
                            h.sendResponseHeaders(400, 0);
                            return;
                        }
                        String value = readText(h);
                        if (value.isEmpty()) {
                            System.out.println("Value для сохранения пустой. value указывается в теле запроса");
                            h.sendResponseHeaders(400, 0);
                            return;
                        }
                        httpManager.createTask(gson.fromJson(value, Task.class));
                        System.out.println("Значение для ключа " + id + " успешно обновлено!");
                        h.sendResponseHeaders(200, 0);
                        break;
                    case "GET":
                        // если не указан id, то возвращаем все задачи
                        if (query.isEmpty()) {
                            sendText(h, gson.toJson(httpManager.findAllTasks()));
                            System.out.println("Все задачи успешно получены.");
                            h.sendResponseHeaders(200, 0);
                            return;
                        }
                        sendText(h, gson.toJson(httpManager.findTaskById(id)));
                        System.out.println("Значение для ключа " + id + " успешно найдено!");
                        h.sendResponseHeaders(200, 0);
                        break;
                    case "DELETE":
                        // если не указан id, то удаляем все задачи
                        if (query.isEmpty()) {
                            httpManager.clearAllTasks();
                            System.out.println("Все задачи успешно удалены.");
                            h.sendResponseHeaders(200, 0);
                            return;
                        }
                        httpManager.deleteTask(id);
                        System.out.println("Значение для ключа " + id + " успешно удалено!");
                        h.sendResponseHeaders(200, 0);
                        break;
                    default:
                        System.out.println("Неизвестный запрос: " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                }
            } finally {
                h.close();
            }

        });
        server.createContext("/tasks/epic", (h) -> {
            try {
                System.out.println("\n/tasks/epic");
                String query = h.getRequestURI().getQuery();
                id = Integer.parseInt(query.split("=")[1]);
                switch (h.getRequestMethod()) {
                    case "POST":
                        if (query.isEmpty()) {
                            System.out.println("Key для сохранения пустой. key указывается в пути: /tasks/epic/?d={key}");
                            h.sendResponseHeaders(400, 0);
                            return;
                        }
                        String value = readText(h);
                        if (value.isEmpty()) {
                            System.out.println("Value для сохранения пустой. value указывается в теле запроса");
                            h.sendResponseHeaders(400, 0);
                            return;
                        }
                        httpManager.createEpic(gson.fromJson(value, Epic.class));
                        System.out.println("Значение для ключа " + id + " успешно обновлено!");
                        h.sendResponseHeaders(200, 0);
                        break;
                    case "GET":
                        if (query.isEmpty()) {
                            sendText(h, gson.toJson(httpManager.findAllEpics()));
                            System.out.println("Все эпики успешно получены.");
                            h.sendResponseHeaders(200, 0);
                            return;
                        }
                        sendText(h, gson.toJson(httpManager.findEpicById(id)));
                        System.out.println("Значение для ключа " + id + " успешно найдено!");
                        h.sendResponseHeaders(200, 0);
                        break;
                    case "DELETE":
                        if (query.isEmpty()) {
                            httpManager.clearAllEpics();
                            System.out.println("Все задачи успешно удалены.");
                            h.sendResponseHeaders(200, 0);
                            return;
                        }
                        httpManager.deleteEpic(id);
                        System.out.println("Значение для ключа " + id + " успешно удалено!");
                        h.sendResponseHeaders(200, 0);
                        break;
                    default:
                        System.out.println("Неизвестный запрос: " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                }
            } finally {
                h.close();
            }
        });
        server.createContext("/tasks/subtask", (h) -> {
            try {
                System.out.println("\n/tasks/subtask");
                String query = h.getRequestURI().getQuery();
                id = Integer.parseInt(query.split("=")[1]);
                switch (h.getRequestMethod()) {
                    case "POST":
                        if (query.isEmpty()) {
                            System.out.println("Key для сохранения пустой. key указывается в пути: /tasks/subtask/?d={key}");
                            h.sendResponseHeaders(400, 0);
                            return;
                        }
                        String value = readText(h);
                        if (value.isEmpty()) {
                            System.out.println("Value для сохранения пустой. value указывается в теле запроса");
                            h.sendResponseHeaders(400, 0);
                            return;
                        }
                        httpManager.createSubtask(gson.fromJson(value, Subtask.class));
                        System.out.println("Значение для ключа " + id + " успешно обновлено!");
                        h.sendResponseHeaders(200, 0);
                        break;
                    case "GET":
                        if (query.isEmpty()) {
                            sendText(h, gson.toJson(httpManager.getAllSubtasks()));
                            System.out.println("Все подзадачи успешно получены.");
                            h.sendResponseHeaders(200, 0);
                            return;
                        }
                        sendText(h, gson.toJson(httpManager.findSubtaskById(id)));
                        System.out.println("Значение для ключа " + id + " успешно найдено!");
                        h.sendResponseHeaders(200, 0);
                        break;
                    case "DELETE":
                        if (query.isEmpty()) {
                            httpManager.clearAllSubtasks();
                            System.out.println("Все подзадачи успешно удалены.");
                            h.sendResponseHeaders(200, 0);
                            return;
                        }
                        httpManager.deleteSubtask(id);
                        System.out.println("Значение для ключа " + id + " успешно удалено!");
                        h.sendResponseHeaders(200, 0);
                        break;
                    default:
                        System.out.println("Неизвестный запрос: " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                }
            } finally {
                h.close();
            }
        });
        server.createContext("/tasks/", (h) -> {
            try {
                System.out.println("\n/tasks/");
                if (h.getRequestMethod().equals("GET")) {
                    sendText(h, gson.toJson(httpManager.getPrioritizedTasks()));
                } else {
                    System.out.println("/tasks/ ждет запрос GET. Ваш запрос: " + h.getRequestMethod());
                    h.sendResponseHeaders(405, 0);
                }
            } finally {
                h.close();
            }
        });
        server.createContext("/tasks/history", (h) -> {
            try {
                System.out.println("\n/tasks/history");
                if (h.getRequestMethod().equals("GET")) {
                    sendText(h, gson.toJson(httpManager.history()));
                } else {
                    System.out.println("/tasks/history ждет запрос GET. Ваш запрос: " + h.getRequestMethod());
                    h.sendResponseHeaders(405, 0);
                }
            } finally {
                h.close();
            }
        });
        server.createContext("/tasks/subtask/epic/?id=", (h) -> {
            try {
                System.out.println("\n/tasks/subtask/epic/?id=");
                String query = h.getRequestURI().getQuery();
                id = Integer.parseInt(query.split("=")[1]);
                if (h.getRequestMethod().equals("GET")) {
                    sendText(h, gson.toJson(httpManager.getSubsByEpicId(id)));
                } else {
                    System.out.println("/tasks/history ждет запрос GET. Ваш запрос: " + h.getRequestMethod());
                    h.sendResponseHeaders(405, 0);
                }
            } finally {
                h.close();
            }
        });
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        server.start();
    }
    
    public void stop() {
        server.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        //byte[] resp = jackson.writeValueAsBytes(obj);
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}