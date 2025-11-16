package vehicles;

import exceptions.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;


public class Scooter implements Vehicle {

    private String brand = null;
    private HashMap<String, Model> models = new HashMap<>();

    public Scooter(String brand, int size) throws DuplicateModelNameException {
        if (brand == null || brand.isEmpty())
            throw new IllegalArgumentException("Бренд не может быть пустым");
        if (size < 0)
            throw new IllegalArgumentException("Размер моделей не может быть отрицательным");

        this.brand = brand;

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

    private class Model implements Serializable, Cloneable {
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
        return models.keySet().toArray(new String[0]);
    }

    @Override
    public double[] getModelsCost() {
        return  models.values()
                .stream()
                .mapToDouble(Model::getCost)
                .toArray();
    }

    @Override
    public double getModelCost(String name) throws NoSuchModelNameException {
        Model m = models.get(name);
        if (m == null)
            throw new NoSuchModelNameException();
        return m.getCost();
    }

    @Override
    public void setModelCost(String name, double cost) throws NoSuchModelNameException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Имя модели не может быть пустым");
        }
        if (cost < 0)
            throw new ModelPriceOutOfBoundsException("Цена модели не может быть отрицательной");

        Model model = models.get(name);
        if (model == null)
            throw new NoSuchModelNameException();
        model.setCost(cost);
    }

    @Override
    public void setModelName(String oldName, String newName) throws NoSuchModelNameException, DuplicateModelNameException {
        if (oldName == null || oldName.isEmpty() || newName == null || newName.isEmpty()) {
            throw new IllegalArgumentException("Имя модели не может быть пустым");
        }

        if(!models.containsKey(oldName))
            throw new NoSuchModelNameException();

        if(models.containsKey(newName))
            throw new DuplicateModelNameException();

        Model model = models.remove(oldName);
        model.setName(newName);
        models.put(newName, model);
    }

    @Override
    public void addModel(String name, double cost) throws DuplicateModelNameException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Имя модели не может быть пустым");
        }

        if(models.containsKey(name))
            throw new DuplicateModelNameException();

        if (cost < 0)
            throw new ModelPriceOutOfBoundsException("Цена модели не может быть отрицательной");


        models.put(name, new Model(name, cost));
    }

    @Override
    public void removeModel(String name) throws NoSuchModelNameException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Имя модели не может быть пустым");
        }

        if(models.remove(name) == null)
            throw new NoSuchModelNameException();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj instanceof Vehicle comparableVehicle) {
            if (!this.getBrand().equals(comparableVehicle.getBrand())) return false;
            if (!Arrays.equals(this.getModelsName(), comparableVehicle.getModelsName())) return false;
            return Arrays.equals(this.getModelsCost(), comparableVehicle.getModelsCost());
        } else return false;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(this.brand);
        result = 31 * result + models.keySet().hashCode();
        result = 31 * result + models.values().hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Скутер: ")
                .append(brand)
                .append("\n");
        models.forEach((name, model) ->
                sb.append(name)
                        .append(" -> ")
                        .append(model.getCost())
                        .append("\n"));
        return sb.toString();
    }

    @Override
    public Object clone() {
        Scooter cloned = null;
        try {
            cloned = (Scooter) super.clone();
            cloned.models = new  HashMap<>();
            for (HashMap.Entry<String, Model> entry :this.models.entrySet()) {
                cloned.models.put(entry.getKey(),
                        (Model) entry.getValue().clone()
                );
            }
        } catch (CloneNotSupportedException ex) { }
        return cloned;
    }

    @Override
    public int getSize() {
        return models.size();
    }
}
