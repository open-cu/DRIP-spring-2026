package lecture.step01.controller;


import lecture.step01.domain.Category;
import lecture.step01.service.CategoryService;
import lecture.step01.service.io.IoService;

import java.util.List;
import java.util.Scanner;

public class CategoryController {

    private final CategoryService service;
    private final IoService io;
    public CategoryController(CategoryService service, IoService io) {
        this.service = service;
        this.io = io;
    }

    public void run() {
        io.println("=== STEP 00: ANTI-DI ===");

        try (io) {
            while (true) {
                printMenu();
                String cmd = io.readLine().trim();

                try {
                    switch (cmd) {
                        case "1" -> cmdCreate();
                        case "2" -> cmdList();
                        case "3" -> cmdDelete();
                        case "0", "exit" -> {
                            return;
                        }
                        default -> io.println("Unknown command: " + cmd);
                    }
                } catch (RuntimeException e) {
                    io.println("Error: " + e.getMessage());
                }
            }
        }
    }

    private void cmdCreate() {
        io.print("Name: ");
        String name = io.readLine().trim();
        long id = service.create(name);
        io.println("OK. Created id=" + id);
    }

    private void cmdList() {
        List<Category> all = service.listAll();
        if (all.isEmpty()) {
            io.println("(empty)");
            return;
        }
        for (Category c : all) {
            io.println(c.id() + " -> " + c.name());
        }
    }

    private void cmdDelete() {
        io.print("Id: ");
        long id = Long.parseLong(io.readLine().trim());
        boolean deleted = service.delete(id);
        io.println(deleted ? "OK. Deleted id=" + id : "Not found: id=" + id);
    }

    private void printMenu() {
        io.println("");
        io.println("1) Create category");
        io.println("2) List categories");
        io.println("3) Delete category");
        io.println("0) Exit");
        io.print("> ");
    }
}
