package vehicles;

import exceptions.*;

import java.io.Serializable;
import java.util.Arrays;

public interface Vehicle extends Serializable, Cloneable {
    String getBrand();
    void setBrand(String brand);

    String[] getModelsName();
    double[] getModelsCost();

    void setModelName(String oldName, String Newname) throws NoSuchModelNameException, DuplicateModelNameException;

    double getModelCost(String name) throws NoSuchModelNameException;
    void setModelCost(String name, double cost) throws NoSuchModelNameException;

    void addModel(String name, double cost) throws DuplicateModelNameException;
    void removeModel(String name) throws NoSuchModelNameException;

    boolean equals(Object obj);

    int hashCode();

    String toString();

    Object clone() throws CloneNotSupportedException;

    int getSize();
}
