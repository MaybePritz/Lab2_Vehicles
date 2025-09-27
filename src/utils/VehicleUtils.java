package utils;

import exceptions.DuplicateModelNameException;
import vehicles.Automobile;
import vehicles.Vehicle;

import java.io.*;
import java.util.Arrays;

public final class VehicleUtils{

    public static double averagePrice(Vehicle vehicle){
        double[] prices = vehicle.getModelsCost();
        double avg = 0;
        double sum = 0;
        if(prices == null || prices.length == 0)
            avg = 0;
        else {
            for(double price : prices)
                sum += price;
            avg = sum / prices.length;
        }
        return avg;
    }

    public static void printModelsPrices(Vehicle vehicle) {
        double[] prices = vehicle.getModelsCost();
        String[] models = vehicle.getModelsName();

        if (prices == null || prices.length == 0) {
            System.out.println("Список цен пуст");
        } else {
            for(int i = 0; i < models.length; i++)
                System.out.println(" " + models[i] + " -> " + prices[i]);
        }

        System.out.println();
    }

    public static boolean compareVehicles(Vehicle vehicle1, Vehicle vehicle2){
        String brand1 = vehicle1.getBrand();
        String brand2 = vehicle2.getBrand();

        String[] names1 = vehicle1.getModelsName();
        String[] names2 = vehicle2.getModelsName();

        double[] prices1 = vehicle1.getModelsCost();
        double[] prices2 = vehicle2.getModelsCost();

        return brand1.equals(brand2) && Arrays.equals(names1, names2) && Arrays.equals(prices1, prices2);
    }

    public static void outputVehicle(Vehicle vehicle, OutputStream out) throws IOException{
        DataOutputStream dos = new DataOutputStream(out);

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

        //Читаем марку
        int length = dis.readInt();
        byte[] brandBytes = new byte[length];
        dis.readFully(brandBytes);
        String brand = new String(brandBytes);

        //Читаем кол-во моделей
        int modelCount = dis.readInt();

        Vehicle auto = new Automobile(brand, 0);

        for(int i = 0; i < modelCount; i++){
            int modelNameLength = dis.readInt();
            byte[] modelNameBytes = new byte[modelNameLength];
            dis.readFully(modelNameBytes);
            String modelName = new String(modelNameBytes);

            double price = dis.readDouble();

            auto.addModel(modelName, price);
        }

        return auto;
    }

    public static void writeVehicle(Vehicle vehicle, Writer out) throws IOException{
        PrintWriter pw = new PrintWriter(out);

        pw.println(vehicle.getBrand());
        pw.println(vehicle.getSize());

        String[] names =  vehicle.getModelsName();
        double[] prices = vehicle.getModelsCost();

        for (int i = 0; i < vehicle.getSize(); i++) {
            pw.println(names[i] + " -> " + prices[i]);
        }

        pw.flush();
    }

    public static Vehicle readVehicle(Reader in) throws IOException, DuplicateModelNameException {
        BufferedReader reader = new BufferedReader(in);

        String brand = reader.readLine();
        if(brand == null || brand.isEmpty())
            throw new IOException("Пустой бренд");

        int size = Integer.parseInt(reader.readLine());

        String[] names = new String[size];
        double[] prices = new double[size];

        for (int i = 0; i < size; i++) {
            String[] line = reader.readLine().split(" -> ");

            names[i] = line[0];
            prices[i] = Double.parseDouble(line[1]);
        }

        Vehicle auto = new Automobile(brand, 0);

        for(int i = 0; i < size; i++){
            auto.addModel(names[i], prices[i]);
        }

        return auto;
    }
}
