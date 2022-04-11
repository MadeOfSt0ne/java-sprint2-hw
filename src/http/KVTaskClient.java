package http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String API_KEY = "MY_KEY";
    private final int PORT = 8078;

    // Конструктор принимает URL к серверу хранилища и регистрируется. При регистрации выдаётся ключ (API_KEY),
    // который нужен при работе с сервером. Я сделал как смог, но оно не работает должным образом.
    // Консоль в тестах выглядит так, будто клиент пытается зарегистрироваться раньше запуска сервера.

    public KVTaskClient() {
        //API_KEY = registerKey();
    }

    // метод для получения АПИ ключа через регистрацию на сервере
    public String registerKey() {
        // создаем запрос и регистрируемся
        URI uri = URI.create("http://localhost:" + PORT + "/register");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        // создаем http клиент с настройками по умолчанию
        HttpClient client = HttpClient.newHttpClient();
        // стандартный обработчик тела запроса с конвертацией содержимого в строку
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            // отправка запроса и получение ответа от сервера
            HttpResponse<String> response = client.send(request, handler);
            System.out.println("Код ответа: " + response.statusCode());
            System.out.println("Ключ: " + response.body());
            return response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + uri + "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return "DEBUG";
    }

    // метод для сохранения состояния менеджера задач
    public void put(String key, String json) {
        URI uri = URI.create("http://localhost:" + PORT + "/save/" + key + "?API_KEY=" + API_KEY);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Код ответа: " + response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + uri + "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    // метод для получения состояния менеджера задач
    public String load(String key) {
        URI uri = URI.create("http://localhost:" + PORT + "/load/" + key + "?API_KEY=" + API_KEY);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Код ответа: " + response.statusCode());
            return response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + uri + "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return "empty";
    }
}
