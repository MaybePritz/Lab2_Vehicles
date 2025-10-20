import exceptions.*;
import utils.VehicleUtils;
import vehicles.*;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[] options = {
                "Лаб №2",
                "Лаб №3",
                "Лаб №4",
                "Выход"
        };

        System.out.println("=".repeat(30));
        System.out.println("Меню:");
        for (int i = 0; i < options.length; i++) {
            System.out.printf("%d - %s%n", i + 1, options[i]);
        }
        System.out.print("Введите номер: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                testLab2();
                break;
            case "2":
                testLab3();
                break;
            case "3":
                testLab4();
                break;
            case "4":
                System.out.println("Выход...");
                break;
            default:
                System.out.println("Неверный выбор.");
        }

        scanner.close();
    }

    private static void testLab2() {
        System.out.println("=".repeat(25) + " Лаб №2 " + "=".repeat(25));
        try {
            Vehicle auto = new Automobile("Škoda", 1);

            auto.addModel("Octavia", 3000000);
            auto.addModel("Yeti", 2000000);

            System.out.println("Список моделей " + auto.getBrand() + " и цен:");
            VehicleUtils.printModelsPrices(auto);

            System.out.println("Средняя цена: " + VehicleUtils.averagePrice(auto));
            System.out.println();

            auto.setModelCost("Octavia", 3100000);
            System.out.println("Цена Octavia после изменения: " + auto.getModelCost("Octavia"));
            System.out.println();

            auto.setModelName("Yeti", "Rapid");
            System.out.println("Список моделей " + auto.getBrand() + " после переименования:");
            VehicleUtils.printModelsPrices(auto);

            auto.removeModel("Rapid");
            System.out.println("Количество моделей после удаления: " + auto.getSize());
        } catch (DuplicateModelNameException | NoSuchModelNameException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void testLab3() {
        System.out.println("=".repeat(25) + " Лаб №3 " + "=".repeat(25));

        File dataDir = new File("data");
        if (!dataDir.exists()) {
            if (dataDir.mkdirs()) {
                System.out.println("Папка 'data' создана");
            } else {
                System.out.println("Не удалось создать папку 'data'");
            }
        }

        try {
            Vehicle auto = new Automobile("Škoda", 1);
            auto.addModel("Octavia", 3000000);
            auto.addModel("Yeti", 2000000);

            System.out.println("\n1) Исходный объект Vehicle (Automobile):");
            System.out.println("Бренд: " + auto.getBrand());
            VehicleUtils.printModelsPrices(auto);

            System.out.println("\n2) Сохранение объекта в бинарный файл 'data/car.dat'...");
            try (FileOutputStream fos = new FileOutputStream("data/car.dat")) {
                VehicleUtils.outputVehicle(auto, fos);
            }
            System.out.println("Объект успешно записан в файл 'data/car.dat'");

            System.out.println("\n3) Чтение объекта из бинарного файла 'data/car.dat'...");
            Vehicle restored;
            try (FileInputStream fis = new FileInputStream("data/car.dat")) {
                restored = VehicleUtils.inputVehicle(fis);
            }
            System.out.println("Объект успешно прочитан из файла 'data/car.dat'");

            System.out.println("\n4) Сохранение объекта в символьный файл 'data/car.text'...");
            try (FileWriter fwr = new FileWriter("data/car.txt")) {
                VehicleUtils.writeVehicle(auto, fwr);
            }
            System.out.println("Объект успешно записан в символьный файл 'data/car.txt'");

            System.out.println("\n5) Чтение объекта из бинарного файла 'data/car.dat'...");
            Vehicle readedAuto;
            try (FileReader reader = new FileReader("data/car.txt")) {
                readedAuto = VehicleUtils.readVehicle(reader);
            }
            System.out.println("Объект успешно прочитан из символьного файла");

            System.out.println("\n7) Запись объекта в консоль (символьный поток): \n");
            VehicleUtils.writeVehicle(auto, new OutputStreamWriter(System.out));

            System.out.println("\n8)Скопируйте текст выше и вставьте его сюда (для восстановления):");
            Vehicle readAuto = VehicleUtils.readVehicle(new InputStreamReader(System.in));

            System.out.println("\n9) Восстановленный объект:");
            VehicleUtils.printModelsPrices(readAuto);

            System.out.println("\n10) Сохранение объекта в файл 'data/auto.ser'...");
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/auto.ser"))) {
                oos.writeObject(auto);
            }
            System.out.println("Объект успешно сериализован в 'data/auto.ser'");

            System.out.println("\n11) Чтение объекта из файла 'data/auto.ser'...");
            Vehicle restoredAuto;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data/auto.ser"))) {
                restoredAuto = (Vehicle) ois.readObject();
            }
            System.out.println("Объект успешно восстановлен из 'data/auto.ser'");
            
            System.out.println("\n12) Проверки:");
            System.out.println("\nИсходный объект (Automobile):");
            VehicleUtils.printModelsPrices(auto);

            System.out.println("\nВосстановленный объект из бинарного файла:");
            VehicleUtils.printModelsPrices(restored);

            if (auto.equals(restored)) {
                System.out.println("Автомобиль совпадает с бинарным восстановлением");
            } else {
                System.out.println("Автомобиль НЕ совпадает с бинарным восстановлением");
            }

            System.out.println("\nВосстановленный объект из символьного файла:");
            VehicleUtils.printModelsPrices(readedAuto);

            if (auto.equals(readedAuto)) {
                System.out.println("Автомобиль совпадает с символьным восстановлением");
            } else {
                System.out.println("Автомобиль НЕ совпадает с символьным восстановлением");
            }

            System.out.println("\nВосстановленный объект из System.in:");
            VehicleUtils.printModelsPrices(readAuto);

            if (auto.equals(readAuto)) {
                System.out.println("Автомобиль совпадает с символьным восстановлением");
            } else {
                System.out.println("Автомобиль НЕ совпадает с символьным восстановлением");
            }

            System.out.println("\nВосстановленный объект после сериализации:");
            VehicleUtils.printModelsPrices(restoredAuto);

            if (auto.equals(restoredAuto)) {
                System.out.println("Автомобиль совпадает с восстановленным объектом");
            } else {
                System.out.println("Автомобиль НЕ совпадает с восстановленным объектом");
            }
        } catch (DuplicateModelNameException | IOException | ClassNotFoundException e) {
            System.err.println("Ошибка: " + e.getMessage());
        } finally {
            System.out.println("=".repeat(25) + " Конец Лаб №3 " + "=".repeat(25));
        }
    }

    private static void testLab4() {
        System.out.println("=".repeat(25) + " Лаб №4 " + "=".repeat(25));
        try {
            Vehicle motorcycle = new Motorcycle("yamaha", 2);
            Vehicle cloned = (Vehicle) motorcycle.clone();

            VehicleUtils.printModelsPrices(motorcycle);
            VehicleUtils.printModelsPrices(cloned);

           System.out.println(motorcycle.equals(cloned));
        } catch (DuplicateModelNameException | CloneNotSupportedException e) {
            System.err.println(e.getMessage());
        }


    }

}