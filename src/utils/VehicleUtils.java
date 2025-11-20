package utils;

import exceptions.DuplicateModelNameException;
import vehicles.Automobile;
import vehicles.*;

import java.lang.reflect.Constructor;
import java.util.Scanner;
import java.util.Locale;
import java.io.*;

public final class VehicleUtils{

    public static double getAveragePrice(Vehicle... vehicles){
        if (vehicles == null || vehicles.length == 0) {
            throw new IllegalArgumentException("Массив транспортных средств не может быть пустым");
        }

        double sum = 0;
        int count = 0;

        for (Vehicle vehicle : vehicles) {
            if (vehicle != null) {
                double[] costs = vehicle.getModelsCost();
                for (double cost : costs) {
                    sum += cost;
                    count++;
                }
            }
        }

        if (count == 0) {
            throw new IllegalArgumentException("Нет моделей для расчета средней цены");
        }

        return sum / count;
    }

    public static void printModelsPrices(Vehicle vehicle) {
        System.out.print(vehicle.toString());
    }

    public static void outputVehicle(Vehicle vehicle, OutputStream out) throws IOException{
        DataOutputStream dos = new DataOutputStream(out);

        byte[] type = vehicle.getClass().getSimpleName().getBytes();
        dos.writeInt(type.length);
        dos.write(type);

        byte[] brand =  vehicle.getBrand().getBytes();
        dos.writeInt(brand.length);
        dos.write(brand);

        dos.writeInt(vehicle.getSize());

        String[] names = vehicle.getModelsName();
        double[] prices = vehicle.getModelsCost();

        for (int i = 0; i < vehicle.getSize(); i++) {
            byte[] nameBytes = names[i].getBytes();
            dos.writeInt(nameBytes.length);
            dos.write(nameBytes);

            dos.writeDouble(prices[i]);
        }

        dos.flush();
    }

    public static Vehicle inputVehicle(InputStream in) throws IOException, DuplicateModelNameException {
        DataInputStream dis = new DataInputStream(in);

        int typeLength = dis.readInt();
        byte[] typeBytes = new byte[typeLength];
        dis.readFully(typeBytes);
        String type = new String(typeBytes);

        int brandLength = dis.readInt();
        byte[] brandBytes = new byte[brandLength];
        dis.readFully(brandBytes);
        String brand = new String(brandBytes);

        int modelCount = dis.readInt();

        Vehicle vehicle = switch (type) {
            case "Automobile" -> new Automobile(brand, 0);
            case "Motorcycle" -> new Motorcycle(brand, 0);
            case "Scooter" -> new Scooter(brand, 0);
            case "Atv" -> new Atv(brand, 0);
            case "Moped"  -> new Moped(brand, 0);
            default -> throw new IOException("Неизвестный тип транспортного средства: " + type);
        };


        for(int i = 0; i < modelCount; i++){
            int modelNameLength = dis.readInt();
            byte[] modelNameBytes = new byte[modelNameLength];
            dis.readFully(modelNameBytes);
            String modelName = new String(modelNameBytes);

            double price = dis.readDouble();

            vehicle.addModel(modelName, price);
        }

        return vehicle;
    }

    public static void writeVehicle(Vehicle vehicle, Writer out) throws IOException {
        PrintWriter pw = new PrintWriter(out);

        pw.printf("%s%n", vehicle.getClass().getSimpleName());
        pw.printf("%s%n", vehicle.getBrand());
        pw.printf("%d%n", vehicle.getSize());

        String[] names = vehicle.getModelsName();
        double[] prices = vehicle.getModelsCost();

        for (int i = 0; i < vehicle.getSize(); i++) {
            pw.printf("%s%n", names[i]);
            pw.printf(Locale.US, "%.2f%n", prices[i]);
        }

        pw.flush();
    }


    public static Vehicle readVehicle(Reader in) throws IOException, DuplicateModelNameException {

        Scanner scanner = new Scanner(in);

        String className = scanner.nextLine();
        String brand = scanner.nextLine();
        int length = Integer.parseInt(scanner.nextLine());

        Vehicle v;
        switch (className) {
            case "Automobile":
                v = new Automobile(brand, 0);
                break;
            case "Motorcycle":
                v = new Motorcycle(brand, 0);
                break;
            case "Scooter":
                v = new Scooter(brand, 0);
                break;
            case "Atv":
                v = new Atv(brand, 0);
                break;
            case "Moped":
                v = new Moped(brand, 0);
                break;
            default:
                scanner.close();
                throw new IllegalArgumentException("Неизвестный класс транспортного средства: " + className);
        }

        for (int i = 0; i < length; i++) {
            String modelName = scanner.nextLine();
            double price = Double.parseDouble(scanner.nextLine());
            v.addModel(modelName, price);
        }

        scanner.close();
        return v;
    }

    public static Vehicle createVehicle(String brand, int size, Vehicle prototype) {
        if (prototype == null) return null;

        try {
            Class<? extends Vehicle> loadClass = prototype.getClass();
            Constructor<?> constructor = loadClass.getConstructor(String.class, int.class);
            return (Vehicle) constructor.newInstance(brand, size);

        } catch (NoSuchMethodException e) {
            System.err.println("Конструктор (String, int) не найден в " + prototype.getClass().getSimpleName());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
