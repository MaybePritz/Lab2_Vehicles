package vehicles;

import exceptions.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;


public class Moped implements Vehicle {

    private String brand = null;
    private LinkedList<Model> models = new LinkedList<>();

    public Moped(String brand, int size) throws DuplicateModelNameException {
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
        return models.stream()
                .map(Model::getName)
                .toArray(String[]::new);
    }

    @Override
    public double[] getModelsCost() {
        return  models.stream()
                .mapToDouble(Model::getCost)
                .toArray();
    }

    @Override
    public double getModelCost(String name) throws NoSuchModelNameException {
        Model m = this.findModel(name);
        return m.getCost();
    }

    @Override
    public void setModelCost(String name, double cost) throws NoSuchModelNameException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Имя модели не может быть пустым");
        }
        if (cost < 0)
            throw new ModelPriceOutOfBoundsException("Цена модели не может быть отрицательной");

        Model model = this.findModel(name);
        model.setCost(cost);
    }

    @Override
    public void setModelName(String oldName, String newName) throws NoSuchModelNameException, DuplicateModelNameException {
        if (oldName == null || oldName.isEmpty() || newName == null || newName.isEmpty()) {
            throw new IllegalArgumentException("Имя модели не может быть пустым");
        }

        for(Model m : this.models) {
            if (m.getName().equals(newName)) {
                throw new DuplicateModelNameException();
            }
        }

        Model model = this.findModel(oldName);

        model.setName(newName);
    }

    @Override
    public void addModel(String name, double cost) throws DuplicateModelNameException {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Имя модели не может быть пустым");

        for(Model m : this.models) {
            if (m.getName().equals(name)) {
                throw new DuplicateModelNameException();
            }
        }

        if (cost < 0)
            throw new ModelPriceOutOfBoundsException("Цена модели не может быть отрицательной");

        models.add(new Model(name, cost));
    }

    @Override
    public void removeModel(String name) throws NoSuchModelNameException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Имя модели не может быть пустым");
        }

        for(int i  = 0; i < models.size(); i++) {
            if(models.get(i).getName().equals(name)) {
                models.remove(i);
                return;
            }
        }

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
        result = 31 * result + Arrays.hashCode(this.getModelsName());
        result = 31 * result + Arrays.hashCode(this.getModelsCost());
        return result;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Мопед: ")
                .append(brand)
                .append("\n");

        for (Model m : this.models) {
            sb.append(m.getName())
                    .append(" -> ")
                    .append(m.getCost())
                    .append("\n");
        }

        return sb.toString();
    }

    @Override
    public Object clone() {
        Moped cloned = null;
        try {
            cloned = (Moped) super.clone();
            cloned.models = new LinkedList<Model>();

            for(Model m : this.models) {
                cloned.models.add((Model) m.clone());
            }

        } catch (CloneNotSupportedException ex) { }
        return cloned;
    }

    @Override
    public int getSize() {
        return models.size();
    }

    private Model findModel(String name) throws NoSuchModelNameException {
        for (Model m : models) {
            if (m.getName().equals(name)) {
                return m;
            }
        }
        throw new NoSuchModelNameException();
    }
}
