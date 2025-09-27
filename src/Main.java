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
            Vehicle auto = new Automobile("Škoda", 10);

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
            System.out.println("❌ Ошибка: " + e.getMessage());
        }
    }

    private static void testLab3() {
        System.out.println("=".repeat(25) + " Лаб №3 " + "=".repeat(25));

        File dataDir = new File("data");
        if (!dataDir.exists()) {
            if (dataDir.mkdirs()) {
                System.out.println("✅ Папка 'data' создана");
            } else {
                System.out.println("❌ Не удалось создать папку 'data'");
            }
        }

        try {
            Vehicle auto = new Automobile("Škoda", 100);
            auto.addModel("Octavia", 3000000);
            auto.addModel("Yeti", 2000000);

            System.out.println("\n21) Исходный объект Vehicle (Automobile):");
            System.out.println("Бренд: " + auto.getBrand());
            VehicleUtils.printModelsPrices(auto);

            System.out.println("\n2) Сохранение объекта в бинарный файл 'data/car.dat'...");
            try (FileOutputStream fos = new FileOutputStream("data/car.dat")) {
                VehicleUtils.outputVehicle(auto, fos);
            }
            System.out.println("✅ Объект успешно записан в файл 'data/car.dat'");

            System.out.println("\n3) Чтение объекта из бинарного файла 'data/car.dat'...");
            Vehicle restored;
            try (FileInputStream fis = new FileInputStream("data/car.dat")) {
                restored = VehicleUtils.inputVehicle(fis);
            }
            System.out.println("✅ Объект успешно прочитан из файла 'data/car.dat'");

            System.out.println("\n4) Сохранение объекта в символьный файл 'data/car.text'...");
            try (FileWriter fw = new FileWriter("data/car.text")) {
                VehicleUtils.writeVehicle(auto, fw);
            }
            System.out.println("✅ Объект успешно записан в файл 'data/car.text'");

            System.out.println("\n5) Чтение объекта из символьного файла 'data/car.text'...");
            Vehicle readAuto;
            try (FileReader fr = new FileReader("data/car.text")) {
                readAuto = VehicleUtils.readVehicle(fr);
            }
            System.out.println("✅ Объект успешно прочитан из файла 'data/car.text'");


            Vehicle moto = new Motorcycle("Honda", 2);
            moto.setModelCost("Модель1", 500000);
            moto.setModelCost("Модель2", 600000);
            moto.addModel("CB500", 650000);
            moto.addModel("CBR1000RR", 1500000);

            System.out.println("\n6) Исходный объект Vehicle ( Motorcycle ):");
            System.out.println("Бренд: " + moto.getBrand());
            VehicleUtils.printModelsPrices(moto);

            System.out.println("\n7) Сохранение объекта в файл 'data/moto.ser'...");
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/moto.ser"))) {
                oos.writeObject(moto);
            }
            System.out.println("✅ Объект успешно сериализован в 'data/moto.ser'");

            System.out.println("\n8) Чтение объекта из файла 'data/moto.ser'...");
            Vehicle restoredMoto;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data/moto.ser"))) {
                restoredMoto = (Vehicle) ois.readObject();
            }
            System.out.println("✅ Объект успешно восстановлен из 'data/moto.ser'");
            
            System.out.println("\n9) Проверки:");
            System.out.println("\nИсходный объект (Automobile):");
            VehicleUtils.printModelsPrices(auto);

            System.out.println("\nВосстановленный объект из бинарного файла:");
            VehicleUtils.printModelsPrices(restored);

            if (VehicleUtils.compareVehicles(auto, restored)) {
                System.out.println("✅ Автомобиль совпадает с бинарным восстановлением");
            } else {
                System.out.println("❌ Автомобиль НЕ совпадает с бинарным восстановлением");
            }

            System.out.println("\nВосстановленный объект из символьного файла:");
            VehicleUtils.printModelsPrices(readAuto);

            if (VehicleUtils.compareVehicles(auto, readAuto)) {
                System.out.println("✅ Автомобиль совпадает с символьным восстановлением");
            } else {
                System.out.println("❌ Автомобиль НЕ совпадает с символьным восстановлением");
            }

            System.out.println("\nИсходный объект ( Motorcycle ):");
            VehicleUtils.printModelsPrices(moto);

            System.out.println("\nВосстановленный объект после сериализации:");
            VehicleUtils.printModelsPrices(restoredMoto);

            if (VehicleUtils.compareVehicles(moto, restoredMoto)) {
                System.out.println("✅ Мотоцикл совпадает с восстановленным объектом");
            } else {
                System.out.println("❌ Мотоцикл НЕ совпадает с восстановленным объектом");
            }

        } catch (DuplicateModelNameException | NoSuchModelNameException | IOException | ClassNotFoundException e) {
            System.err.println("❌ Ошибка: " + e.getMessage());
        } finally {
            System.out.println("=".repeat(25) + " Конец Лаб №3 " + "=".repeat(25));
        }
    }

}