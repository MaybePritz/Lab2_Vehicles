package vehicles;

import exceptions.*;

public interface Vehicle {
    String getBrand();
    void setBrand(String brand);

    String[] getModelsName();
    double[] getModelsCost();

    void setModelName(String oldName, String Newname) throws NoSuchModelNameException, DuplicateModelNameException;

    double getModelCost(String name) throws NoSuchModelNameException;
    void setModelCost(String name, double cost) throws NoSuchModelNameException;

    void addModel(String name, double cost) throws DuplicateModelNameException;
    void removeModel(String name) throws NoSuchModelNameException;

    int getSize();

    public static void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Имя модели не может быть пустым");
        }
    }

    public static void validateCost(double cost) {
        if (cost < 0)
            throw new ModelPriceOutOfBoundsException("Цена модели не может быть отрицательной");
    }
}
