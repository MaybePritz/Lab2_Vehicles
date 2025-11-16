import exceptions.*;
import utils.VehicleUtils;
import vehicles.*;

import java.io.*;
import java.util.Scanner;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[] options = {
                "Выход",
                "Лаб №2",
                "Лаб №3",
                "Лаб №4",
                "Лаб №5",
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
                System.out.println("Выход...");
                break;
            case "2":
                testLab2();
                break;
            case "3":
                testLab3();
                break;
            case "4":
                testLab4();
                break;
            case "5":
                new TestLab5().run(new String[]{
                        "vehicles.Automobile",
                        "setModelCost",
                        "Octavia",
                        "3500000"
                });
                break;
            default:
                System.out.println("Неверный выбор.");
        }

        scanner.close();
    }

    private static void testLab2() {
        System.out.println("=".repeat(25) + " Лаб №2 " + "=".repeat(25));
        try {
            Vehicle auto = new Scooter("Škoda", 1);

            auto.addModel("Octavia", 3000000);
            auto.addModel("Yeti", 2000000);

            System.out.println("Список моделей " + auto.getBrand() + " и цен:");
            VehicleUtils.printModelsPrices(auto);

            System.out.println("Средняя цена: " + VehicleUtils.getAveragePrice(auto));
            System.out.println();

            auto.setModelCost("Octavia", 3100000);
            System.out.println("Цена Octavia после изменения: " + auto.getModelCost("Octavia"));
            System.out.println();

            auto.setModelName("Yeti", "Rapid");
            System.out.println("Список моделей " + auto.getBrand() + " после переименования:");
            System.out.println(auto);

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
            Vehicle auto = new Motorcycle("Skoda", 6);

            Vehicle clone = (Vehicle) auto.clone();

            System.out.println("Оригинал: " + auto);
            System.out.println("Клон: " + clone);
            System.out.println("Авто равно клону?: " + auto.equals(clone));

            System.out.println("Хэш-код оригинала: " + auto.hashCode());
            System.out.println("Хэш-код клона: " + clone.hashCode() );

            //3  clone.addModel("Модель12", 337);

            clone.setModelName("Модель1", "NewModel");

            System.out.println("Оригинал: " + auto);
            System.out.println("Клон: " + clone);

            System.out.println("Авто равно клону?: " + auto.equals(clone));

            System.out.println("Хэш-код оригинала: " + auto.hashCode());
            System.out.println("Хэш-код клона: " + clone.hashCode());

        } catch (NoSuchModelNameException | DuplicateModelNameException | CloneNotSupportedException e) {
            System.err.println(e.getMessage());
        }


    }

    private static class TestLab5 {

        public void run(String[] args) {
            System.out.println("=".repeat(25) + " Лаб №5 " + "=".repeat(25));
            test1();
            test2();
            test3();
            test4();
        }

        private void test1() {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("ЗАДАНИЕ 1, 2");
            System.out.println("=".repeat(60));

            try {
                String className = "vehicles.Automobile";
                String methodName = "setModelCost";
                String modelName = "Octavia";
                double newPrice = 3500000.0;

                System.out.println("Класс: " + className);
                System.out.println("Метод: " + methodName);
                System.out.println("Модель: " + modelName);
                System.out.println("Новая цена: " + newPrice);

                Class<?> vehicleClass = Class.forName(className);
                System.out.println("\nКласс успешно загружен: " + vehicleClass.getSimpleName());

                Constructor<?> constructor = vehicleClass.getConstructor(String.class, int.class);
                Vehicle vehicle = (Vehicle) constructor.newInstance("Škoda", 2);
                System.out.println("Объект создан через рефлексию");

                Method addModel = vehicleClass.getMethod("addModel", String.class, double.class);
                addModel.invoke(vehicle, "Octavia", 3000000.0);
                addModel.invoke(vehicle, "Yeti", 2500000.0);
                System.out.println("Модели добавлены");

                System.out.println("\nОбъект ДО изменения:");
                System.out.println(vehicle);

                Method targetMethod = vehicleClass.getMethod(methodName, String.class, double.class);
                targetMethod.invoke(vehicle, modelName, newPrice);
                System.out.println("Метод " + methodName + " успешно вызван через рефлексию");

                System.out.println("\nОбъект ПОСЛЕ изменения:");
                System.out.println(vehicle);

                Method getModelCost = vehicleClass.getMethod("getModelCost", String.class);
                double actualPrice = (double) getModelCost.invoke(vehicle, modelName);

                if (actualPrice == newPrice) {
                    System.out.println("Цена успешно изменена");
                } else {
                    System.out.println("ОШИБКА: Ожидалось " + newPrice + ", получено " + actualPrice);
                }

                System.out.println("\n--- Проверка метода createVehicle ---");
                Vehicle newVehicle = VehicleUtils.createVehicle("Volkswagen", 3, vehicle);

                if (newVehicle != null) {
                    System.out.println("createVehicle: объект создан");

                    System.out.println("\n" + vehicle);
                    System.out.println(newVehicle);

                    System.out.println("Класс нового объекта: " + newVehicle.getClass().getSimpleName());
                    System.out.println("Совпадает с прототипом: " + (newVehicle.getClass() == vehicle.getClass()));
                } else {
                    System.out.println("createVehicle вернул null");
                }

            } catch (Exception e) {
                System.err.println("ОШИБКА: " + e.getMessage());
                e.printStackTrace();
            }
        }

        private static  void test2() {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("ЗАДАНИЕ 3,4,5");
            System.out.println("=".repeat(60));
            try {
                Scooter scooter = new Scooter("Vespa", 2);

                System.out.println("Scooter создан: " + scooter.getBrand());
                System.out.println("Начальный размер: " + scooter.getSize());

                scooter.addModel("Primavera", 250000);
                scooter.addModel("Sprint", 280000);

                System.out.println("\nМодели после добавления:");
                VehicleUtils.printModelsPrices(scooter);

                scooter.setModelCost("Primavera", 260000);
                System.out.println("\nЦена модели Primavera изменена на 260000");

                scooter.setModelName("Sprint", "GTS");
                System.out.println("Модель Sprint переименована в GTS");

                System.out.println("\nИтоговое состояние:");
                System.out.println(scooter);

                scooter.removeModel("Модель1");
                System.out.println("Модель1 удалена");
                System.out.println("Размер после удаления: " + scooter.getSize());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private static void test3() {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("ЗАДАНИЕ 6");
            System.out.println("=".repeat(60));
            try {
                Automobile auto = new Automobile("BMW", 0);
                auto.addModel("X5", 5000000);
                auto.addModel("X3", 4000000);

                Motorcycle moto = new Motorcycle("Kawasaki", 0);
                moto.addModel("Ninja", 800000);
                moto.addModel("Z900", 700000);

                Scooter scooter = new Scooter("Vespa", 0);
                scooter.addModel("Primavera", 250000);

                double avg1 = VehicleUtils.getAveragePrice(auto);
                System.out.println("\nТест 1: Одно ТС (Automobile)");
                System.out.println("Модели: X5 (5000000), X3 (4000000)");
                System.out.println("Средняя цена: " + avg1);
                System.out.println("Ожидается: 4500000.0");
                System.out.println(avg1 == 4500000.0 ? "Корректно" : "Ошибка");

                double avg2 = VehicleUtils.getAveragePrice(auto, moto);
                System.out.println("\nТест 2: Два ТС (Automobile + Motorcycle)");
                System.out.println("Все модели: 5000000, 4000000, 800000, 700000");
                System.out.println("Средняя цена: " + avg2);
                double expected2 = (5000000.0 + 4000000.0 + 800000.0 + 700000.0) / 4;
                System.out.println("Ожидается: " + expected2);
                System.out.println(avg2 == expected2 ? "Корректно" : "Ошибка");

                double avg3 = VehicleUtils.getAveragePrice(auto, moto, scooter);
                System.out.println("\nТест 3: Три ТС (Automobile + Motorcycle + Scooter)");
                System.out.println("Все модели: 5000000, 4000000, 800000, 700000, 250000");
                System.out.println("Средняя цена: " + avg3);
                double expected3 = (5000000.0 + 4000000.0 + 800000.0 + 700000.0 + 250000.0) / 5;
                System.out.println("Ожидается: " + expected3);
                System.out.println(avg3 == expected3 ? "Корректно" : "Ошибка");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private static void test4() {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("ЗАДАНИЕ 7");
            System.out.println("=".repeat(60));
            try {
                Automobile auto = new Automobile("Skoda", 0);
                auto.addModel("Octavia", 3000000);
                auto.addModel("Yeti", 2500000);

                System.out.println("\nИсходный объект:");
                VehicleUtils.printModelsPrices(auto);

                String filename = "data/test_formatted.txt";
                System.out.println("\n1) Запись в файл с использованием printf...");
                try (FileWriter writer = new FileWriter(filename)) {
                    VehicleUtils.writeVehicle(auto, writer);
                }
                System.out.println("Записано в " + filename);

                System.out.println("\nСодержимое файла:");
                try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("  " + line);
                    }
                }

                System.out.println("\n2) Чтение из файла с использованием Scanner...");
                Vehicle restored;
                try (FileReader reader = new FileReader(filename)) {
                    restored = VehicleUtils.readVehicle(reader);
                }
                System.out.println("Прочитано из файла");

                System.out.println("\nВосстановленный объект:");
                VehicleUtils.printModelsPrices(restored);

                System.out.println("\n3) Проверка равенства объектов:");
                System.out.println("Исходный бренд: " + auto.getBrand());
                System.out.println("Восстановленный бренд: " + restored.getBrand());
                System.out.println("Исходный размер: " + auto.getSize());
                System.out.println("Восстановленный размер: " + restored.getSize());

                boolean brandsEqual = auto.getBrand().equals(restored.getBrand());
                boolean sizesEqual = auto.getSize() == restored.getSize();
                boolean modelsEqual = true;

                String[] originalNames = auto.getModelsName();
                String[] restoredNames = restored.getModelsName();
                double[] originalCosts = auto.getModelsCost();
                double[] restoredCosts = restored.getModelsCost();

                for (int i = 0; i < originalNames.length; i++) {
                    if (!originalNames[i].equals(restoredNames[i])) {
                        modelsEqual = false;
                        break;
                    }
                    if (Math.abs(originalCosts[i] - restoredCosts[i]) > 0.01) {
                        modelsEqual = false;
                        break;
                    }
                }

                System.out.println("\nБренды совпадают: " + (brandsEqual ? "да" : "нет"));
                System.out.println("Размеры совпадают: " + (sizesEqual ? "да" : "нет"));
                System.out.println("Модели совпадают: " + (modelsEqual ? "да" : "нет"));

                if (brandsEqual && sizesEqual && modelsEqual) {
                    System.out.println("\nФорматированный ввод/вывод работает");
                } else {
                    System.out.println("\nДанные не совпадают");
                }

                System.out.println("\n4) Тест записи в консоль:");
                VehicleUtils.writeVehicle(auto, new OutputStreamWriter(System.out));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}