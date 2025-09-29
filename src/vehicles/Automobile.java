package vehicles;

import exceptions.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class Automobile implements Vehicle, Serializable {

    private String brand = null;
    private Model[] models = null;

    public Automobile(String brand, int size) throws DuplicateModelNameException {
        if (brand == null || brand.isEmpty())
            throw new IllegalArgumentException("Бренд не может быть пустым");
        if (size < 0)
            throw new IllegalArgumentException("Размер массива моделей не может быть отрицательным");

        this.brand = brand;
        this.models = new Model[0];

        for (int i = 0; i < size; i++) {
            this.addModel("Модель" + (i + 1), 0);
        }
    }

    @Override
    public String getBrand() {
        return this.brand;
    }

    @Override
    public void setBrand(String brand) {
        this.brand = brand;
    }

    private class Model implements Cloneable {
        private String name = null;
        private double cost = Double.NaN;

        public Model(String name, double cost) {
            this.name = name;
            this.cost = cost;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getCost() {
            return this.cost;
        }

        public void setCost(double cost) {
            this.cost = cost;
        }

        protected Object clone() {
            Object cloned = null;
            try {
                cloned = super.clone();
            } catch (CloneNotSupportedException e) {}
            return cloned;
        }
    }

    @Override
    public String[] getModelsName() {
        String[] names = new String[models.length];
        for (int i = 0; i < models.length; i++) {
            names[i] = models[i].getName();
        }
        return names;
    }

    @Override
    public double[] getModelsCost() {
        double[] costs = new double[models.length];
        for (int i = 0; i < models.length; i++) {
            costs[i] = this.models[i].getCost();
        }
        return costs;
    }

    @Override
    public double getModelCost(String name) throws NoSuchModelNameException {
        for (int i = 0; i < models.length; i++) {
            if (models[i].getName().equals(name)) return models[i].getCost();
        }
        throw new NoSuchModelNameException();
    }

    @Override
    public void setModelCost(String name, double cost) throws NoSuchModelNameException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Имя модели не может быть пустым");
        }
        if (cost < 0)
            throw new ModelPriceOutOfBoundsException("Цена модели не может быть отрицательной");

        for (int i = 0; i < models.length; i++) {
            if (models[i].getName().equals(name)) {
                models[i].setCost(cost);
                return;
            }
        }
        throw new NoSuchModelNameException();
    }

    @Override
    public void setModelName(String oldName, String newName) throws NoSuchModelNameException, DuplicateModelNameException {
        if (oldName == null || oldName.isEmpty() || newName == null || newName.isEmpty()) {
            throw new IllegalArgumentException("Имя модели не может быть пустым");
        }

        int index = -1;

        for (int i = 0; i < models.length; i++) {
            if (newName.equals(models[i].name)) {
                throw new DuplicateModelNameException();
            }
            if (index == -1 && oldName.equals(models[i].name)) {
                index = i;
            }
        }

        if (index != -1)
            models[index].setName(newName);
        else
            throw new NoSuchModelNameException();
    }

    @Override
    public void addModel(String name, double cost) throws DuplicateModelNameException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Имя модели не может быть пустым");
        }
        if (cost < 0)
            throw new ModelPriceOutOfBoundsException("Цена модели не может быть отрицательной");

        checkDuplicate(name);

        models = Arrays.copyOf(models, models.length + 1);
        models[models.length - 1] = new Model(name, cost);
    }

    @Override
    public void removeModel(String name) throws NoSuchModelNameException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Имя модели не может быть пустым");
        }

        int index = -1;
        for (int i = 0; i < models.length; i++) {
            if (this.models[i].getName().equals(name)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            throw new NoSuchModelNameException();
        }

        System.arraycopy(models, index + 1, models, index, models.length - index - 1);
        models = Arrays.copyOf(models, models.length - 1);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if(this.hashCode() != obj.hashCode()) return false;

        if (obj instanceof Vehicle comparableVehicle) {
            if (!this.getBrand().equals(comparableVehicle.getBrand())) return false;
            if (!Arrays.equals(this.getModelsName(), comparableVehicle.getModelsName())) return false;
            return Arrays.equals(this.getModelsCost(), comparableVehicle.getModelsCost());
        } else return false;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(this.brand);
        result = 31 * result + Arrays.hashCode(this.getModelsName());
        result = 31 * result + Arrays.hashCode(this.getModelsCost());
        return result;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Автомобиль: ")
                .append(brand)
                .append("\n");
        for (int i = 0; i < models.length; i++) {
            sb.append(models[i].getName())
                    .append(" -> ")
                    .append(models[i].getCost())
                    .append("\n");
        }
        return sb.toString();
    }

    @Override
    public Object clone() {
        Automobile cloned = null;
        try {
            cloned = (Automobile) super.clone();
            cloned.models = new Model[this.getSize()];
            for (int i = 0; i < cloned.models.length; i++) {
                cloned.models[i] = (Model) this.models[i].clone();
            }
        } catch (CloneNotSupportedException ex) { }
        return cloned;
    }

    @Override
    public int getSize() {
        return models.length;
    }

    private Model getModelByName(String name) throws NoSuchModelNameException {
        for (int i = 0; i < models.length; i++) {
            if (models[i].getName().equals(name))
                return models[i];
        }
        throw new NoSuchModelNameException();
    }

    private void checkDuplicate(String name) throws DuplicateModelNameException {
        for (int i = 0; i < models.length; i++) {
            if (models[i].getName().equals(name)) throw new DuplicateModelNameException();
        }
    }
}
