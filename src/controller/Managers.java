package controller;

import http.HTTPTaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new HTTPTaskManager();
    }
}
