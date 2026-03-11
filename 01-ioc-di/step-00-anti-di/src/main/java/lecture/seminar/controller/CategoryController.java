package lecture.seminar.controller;


import lecture.seminar.domain.Category;
import lecture.seminar.service.CategoryService;
import lecture.seminar.service.io.ConsoleIoService;

import java.util.List;

public class CategoryController {

    private final ConsoleIoService io = new ConsoleIoService();       // anti-DI
    private final CategoryService service = new CategoryService(); // anti-DI

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
