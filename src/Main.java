import exceptions.*;
import vehicles.*;


public class Main {
    public static void main(String[] args) {
        Automobile auto = new Automobile("Skoda");
        Motocycle moto = new Motocycle("Honda");

        System.out.println("// ------------- Машины ------------- //");
        try {
            auto.addModel("Octavia", 3000000);
            auto.addModel("Yeti", 2000000);

            System.out.println("Список моделей " + auto.getBrand() + ":");
            VehicleUtils.printModels(auto);

            System.out.println("Список цен:");
            VehicleUtils.printPrices(auto);

            System.out.println("Средняя цена: " + VehicleUtils.averagePrice(auto));

            auto.setModelCost("Octavia", 3100000);
            System.out.println("Цена Octavia после изменения: " + auto.getModelCost("Octavia"));

            auto.removeModel("Yeti");
            System.out.println("Количество моделей после удаления: " + auto.getSize());

        } catch (DuplicateModelNameException e) {
            System.out.println("Ошибка: модель с таким именем уже существует");
        } catch (NoSuchModelNameException e) {
            System.out.println("Ошибка: модель не найдена");
        }

        System.out.println("// ------------- Мотоциклы ------------- //");
        try {
            moto.addModel("CBR600RR", 1200000);
            moto.addModel("AfricaTwin", 1500000);

            System.out.println("Список моделей " + moto.getBrand() + ":");
            VehicleUtils.printModels(moto);

            System.out.println("Список цен:");
            VehicleUtils.printPrices(moto);

            System.out.println("Средняя цена: " + VehicleUtils.averagePrice(moto));

            moto.setModelCost("CBR600RR", 1250000);
            System.out.println("Цена CBR600RR после изменения: " + moto.getModelCost("CBR600RR"));

            moto.setModelName("CBR600RR", "CBR650RR");
            System.out.println("Список моделей после переименования:");
            VehicleUtils.printModels(moto);

            moto.removeModel("AfricaTwin");
            System.out.println("Количество моделей после удаления: " + moto.getSize());

        } catch (NoSuchModelNameException e) {
            System.out.println("Ошибка: модель не найдена");
        } catch (ModelPriceOutOfBoundsException e) {
            System.out.println("Ошибка: цена модели не может быть отрицательной");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}