package utils;

import exceptions.DuplicateModelNameException;
import vehicles.Automobile;
import vehicles.*;

import java.io.*;

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

    public static void writeVehicle(Vehicle vehicle, Writer out) throws IOException{
        PrintWriter pw = new PrintWriter(out);

        pw.println(vehicle.getClass().getSimpleName());
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


        String type = reader.readLine();
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

        Vehicle vehicle = switch (type) {
            case "Automobile" -> new Automobile(brand, 0);
            case "Motorcycle" -> new Motorcycle(brand, 0);
            default -> throw new IOException("Неизвестный тип транспортного средства: " + type);
        };

        for(int i = 0; i < size; i++){
            vehicle.addModel(names[i], prices[i]);
        }

        return vehicle;
    }
}
