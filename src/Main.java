import exceptions.*;
import vehicles.*;

public class Main {
    public static void main(String[] args) {
        Vehicle auto = new Automobile("Skoda", 1000);
        Vehicle moto = new Motocycle("Honda", 3);

        System.out.println("// ------------- Машины ------------- //");
        try {
            auto.addModel("Octavia", 3000000);
            auto.addModel("Yeti", 2000000);

            System.out.println("Список моделей " + auto.getBrand() + ":");
            VehicleUtils.printModels(auto);

            System.out.println("Список цен:");
            VehicleUtils.printPrices(auto);

            System.out.println("Средняя цена: " + VehicleUtils.averagePrice(auto));

            auto.setModelCost("Octavia", 3100000);//setModelName
            System.out.println("Цена Octavia после изменения: " + auto.getModelCost("Octavia"));

            auto.setModelName("Yeti", "Rapid");
            System.out.println("Список моделей " + auto.getBrand() + " после переименования модели:");
            VehicleUtils.printModels(auto);

            auto.removeModel("Rapid");
            System.out.println("Количество моделей после удаления: " + auto.getSize());

        } catch (DuplicateModelNameException | NoSuchModelNameException e) {
            System.out.println(e.getMessage());
        }

        System.out.println();

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
            System.out.println("Список моделей " + moto.getBrand() + " после переименования модели:");

            VehicleUtils.printModels(moto);

            moto.removeModel("AfricaTwin");
            System.out.println("Количество моделей после удаления: " + moto.getSize());

        } catch (NoSuchModelNameException | ModelPriceOutOfBoundsException | DuplicateModelNameException  e) {
            System.out.println(e.getMessage());
        }
    }
}