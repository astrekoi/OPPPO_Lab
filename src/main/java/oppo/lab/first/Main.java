package oppo.lab.first;

import oppo.lab.first.model.CartoonMovie;
import oppo.lab.first.view.FilmView;

public class Main {
    public static void main(String[] args) {
        System.out.println("Зинченко Андрей Сергеевич");
        var variant = 5; // вариант выбранный по номеру в списке группы

        var x = (variant - 1) % 11 + 1;
        System.out.println("Вариант: " + x);

        var y = ((variant - 1) / 12) % 3 + 1;
        System.out.println("Номер контейнера: " + y);
        System.out.println("1. Однонаправленный кольцевой список");

        FilmView filmView = new FilmView();
        filmView.run();
    }
}