package oppo.lab.first.view;

import oppo.lab.first.controller.FilmController;
import oppo.lab.first.exceptions.InvalidChoiceException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class FilmView {
    private final FilmController controller;
    private final Scanner scanner;
    private boolean isChoice = false;

    public FilmView(FilmController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    public void run() {
        while (true) {
            System.out.println();
            File[] files = displayFiles();
            int choice = getFileChoice(files);
            if (choice == 0) {
                break;
            }
            try {
                BufferedReader reader = new BufferedReader(new FileReader(files[choice - 1].getAbsolutePath()));
                controller.setReader(reader);
                controller.processFile(files[choice - 1].getName());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (isChoice) {
                break;
            }
        }
    }

    private File[] displayFiles() {
        System.out.println("Файлы для обработки:");
        String currentDir = System.getProperty("user.dir");
        File[] files = new File(currentDir + "\\src\\main\\java\\oppo\\lab\\first\\input").listFiles();
        if (files != null) {
            if (files.length == 0) {
                System.out.println("В папке input нет файлов.");
                return files;
            }
            for (int i = 0; i < files.length; i++) {
                System.out.println(i + 1 + ". " + files[i].getName());
            }
        } else {
            System.out.println("Не удалось получить список файлов.");
        }
        return files;
    }

    private int getFileChoice(File[] files) {
        while (true) {
            System.out.println("Выберите файл для обработки (или введите 0 для выхода):");
            int choice = scanner.nextInt();
            if (choice == 0) {
                System.out.println("Вы уверены, что хотите выйти? (д/н)");
                String confirm = scanner.next();
                if (confirm.equalsIgnoreCase("д")) {
                    return choice;
                } else {
                    continue;
                }
            }
            try {
                validateChoice(choice, files);
                isChoice = true;
                return choice;
            } catch (InvalidChoiceException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void validateChoice(int choice, File[] files) throws InvalidChoiceException {
        if (files == null) {
            throw new InvalidChoiceException("Не удалось получить список файлов.");
        }
        if (choice < 1 || choice > files.length) {
            throw new InvalidChoiceException("Неверный выбор, попробуйте еще раз.");
        }
    }
}
