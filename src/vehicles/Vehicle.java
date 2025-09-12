package vehicles;

import exceptions.DuplicateModelNameException;
import exceptions.NoSuchModelNameException;

public interface Vehicle {
    String getBrand();
    void setBrand(String brand);

    String[] getModelsName();
    double[] getModelsCost();

    void changeModelName(int index, String name);

    double getModelCost(String name) throws NoSuchModelNameException;
    void setModelCost(String name, double cost) throws NoSuchModelNameException;

    void addModel(String name, double cost) throws DuplicateModelNameException;
    void removeModel(String name) throws NoSuchModelNameException;

    int getSize();
}
