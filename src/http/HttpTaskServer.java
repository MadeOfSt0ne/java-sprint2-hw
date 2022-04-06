package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HTTPTaskManager httpManager;
    private HttpServer server;
    private Map<Integer, String> taskData = new HashMap<>();
    private Map<Integer, String> epicData = new HashMap<>();
    private Map<Integer, String> subtaskData = new HashMap<>();
    Integer id;

    public HttpTaskServer(HTTPTaskManager httpManager) throws IOException {
        this.httpManager = httpManager;
        server = HttpServer.create();
        server.bind(new InetSocketAddress(PORT), 0);

        server.createContext("/tasks/task", (h) -> {
            try {
                System.out.println("\n/tasks/task");
                id = Integer.parseInt(h.getRequestURI().getPath().substring("/tasks/task".length()));
                switch (h.getRequestMethod()) {
                    case "POST":
                        if (id == null) {
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
                        taskData.put(id, value);
                        System.out.println("Значение для ключа " + id + " успешно обновлено!");
                        h.sendResponseHeaders(200, 0);
                        break;
                    case "GET":
                        // если не указан id, то возвращаем все задачи
                        if (id == null) {
                            for (Map.Entry<Integer, String> entry : taskData.entrySet()) {
                                sendText(h, entry.getValue());
                            }
                            System.out.println("Все задачи успешно получены.");
                            h.sendResponseHeaders(200, 0);
                            return;
                        }
                        if (!taskData.containsKey(id)) {
                            System.out.println("Ключ " + id + " не найден.");
                            h.sendResponseHeaders(404, 0);
                            return;
                        }
                        sendText(h, taskData.get(id));
                        System.out.println("Значение для ключа " + id + " успешно найдено!");
                        h.sendResponseHeaders(200, 0);
                        break;
                    case "DELETE":
                        // если не указан id, то удаляем все задачи
                        if (id == null) {
                            taskData.clear();
                            System.out.println("Все задачи успешно удалены.");
                            h.sendResponseHeaders(200, 0);
                            return;
                        }
                        if (!taskData.containsKey(id)) {
                            System.out.println("Ключ " + id + " не найден.");
                            h.sendResponseHeaders(404, 0);
                            return;
                        }
                        taskData.remove(id);
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
                id = Integer.parseInt(h.getRequestURI().getPath().substring("/tasks/epic".length()));
                switch (h.getRequestMethod()) {
                    case "POST":
                        if (id == null) {
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
                        epicData.put(id, value);
                        System.out.println("Значение для ключа " + id + " успешно обновлено!");
                        h.sendResponseHeaders(200, 0);
                        break;
                    case "GET":
                        if (id == null) {
                            for (Map.Entry<Integer, String> entry : epicData.entrySet()) {
                                sendText(h, entry.getValue());
                            }
                            System.out.println("Все эпики успешно получены.");
                            h.sendResponseHeaders(200, 0);
                            return;
                        }
                        if (!epicData.containsKey(id)) {
                            System.out.println("Ключ " + id + " не найден.");
                            h.sendResponseHeaders(404, 0);
                            return;
                        }
                        sendText(h, epicData.get(id));
                        System.out.println("Значение для ключа " + id + " успешно найдено!");
                        h.sendResponseHeaders(200, 0);
                        break;
                    case "DELETE":
                        if (id == null) {
                            epicData.clear();
                            System.out.println("Все задачи успешно удалены.");
                            h.sendResponseHeaders(200, 0);
                            return;
                        }
                        if (!epicData.containsKey(id)) {
                            System.out.println("Ключ " + id + " не найден.");
                            h.sendResponseHeaders(404, 0);
                            return;
                        }
                        epicData.remove(id);
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
                id = Integer.parseInt(h.getRequestURI().getPath().substring("/tasks/subtask".length()));
                switch (h.getRequestMethod()) {
                    case "POST":
                        if (id == null) {
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
                        subtaskData.put(id, value);
                        System.out.println("Значение для ключа " + id + " успешно обновлено!");
                        h.sendResponseHeaders(200, 0);
                        break;
                    case "GET":
                        if (id == null) {
                            for (Map.Entry<Integer, String> entry : subtaskData.entrySet()) {
                                sendText(h, entry.getValue());
                            }
                            System.out.println("Все сабтаски успешно получены.");
                            h.sendResponseHeaders(200, 0);
                            return;
                        }
                        if (!taskData.containsKey(id)) {
                            System.out.println("Ключ " + id + " не найден.");
                            h.sendResponseHeaders(404, 0);
                            return;
                        }
                        sendText(h, subtaskData.get(id));
                        System.out.println("Значение для ключа " + id + " успешно найдено!");
                        h.sendResponseHeaders(200, 0);
                        break;
                    case "DELETE":
                        if (id == null) {
                            subtaskData.clear();
                            System.out.println("Все сабтаски успешно удалены.");
                            h.sendResponseHeaders(200, 0);
                            return;
                        }
                        if (!subtaskData.containsKey(id)) {
                            System.out.println("Ключ " + id + " не найден.");
                            h.sendResponseHeaders(404, 0);
                            return;
                        }
                        subtaskData.remove(id);
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
       /* server.createContext("/tasks/", (h) -> {
            try {
                System.out.println("\n/tasks/");
                if (h.getRequestMethod().equals("GET")) {
                    for (Integer i : httpManager.getPrioritizedTasks().size())
                }
            }
        });*/




    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        //System.out.println("API_KEY: " + API_KEY);
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), "UTF-8");
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        //byte[] resp = jackson.writeValueAsBytes(obj);
        byte[] resp = text.getBytes("UTF-8");
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}