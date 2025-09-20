package vehicles;

import exceptions.*;
import java.util.Arrays;

public class Automobile implements Vehicle{
    private String brand = null;
    private Model[] models = null;
    private int count = 0;

    public Automobile(String brand, int size) {
        if (brand == null || brand.isEmpty())
            throw new IllegalArgumentException("Бренд не может быть пустым");

        if (size < 0)
            throw new IllegalArgumentException("Размер массива моделей не может быть отрицательным");

        this.brand = brand;
        this.models = new Model[size];
    }

    @Override
    public String getBrand() { return this.brand; }

    @Override
    public void setBrand(String brand) {
        if (brand == null || brand.isEmpty())
            throw new IllegalArgumentException("Бренд не может быть пустым");

        this.brand = brand;
    }

    private class Model {
        private String name = null;
        private double cost = Double.NaN;

        public Model(String name, double cost) {
            Vehicle.validateName(name);
            Vehicle.validateCost(cost);

            this.name = name;
            this.cost = cost;
        }

        public String getName() { return this.name; }

        public void setName(String name) {
            Vehicle.validateName(name);
            this.name = name;
        }

        public double getCost() { return this.cost; }

        public void setCost(double cost) {
            Vehicle.validateCost(cost);

            this.cost = cost;
        }

    }

    @Override
    public String[] getModelsName() {
        String[] names = new String[this.count];
        for (int i = 0; i < this.count; i++) { names[i] = models[i].getName(); }

        return names;
    }

    @Override
    public double[] getModelsCost(){
        double[] costs = new double[this.count];
        for(int i = 0; i < this.count; i++) { costs[i] = this.models[i].getCost(); }
        return costs;
    }

    @Override
    public double getModelCost(String name) throws NoSuchModelNameException {
        for (int i = 0; i < this.count; i++) {
            if (models[i].getName().equals(name)) return models[i].getCost();
        }
        throw new NoSuchModelNameException();
    }

    @Override
    public void setModelCost(String name, double cost) throws NoSuchModelNameException {
        Vehicle.validateName(name);
        Vehicle.validateCost(cost);

        for (int i = 0; i < this.count; i++) {
            if (models[i].getName().equals(name)) {
                models[i].setCost(cost);
                return;
            }
        }
        throw new NoSuchModelNameException();
    }

    @Override
    public void setModelName(String oldName, String newName) throws NoSuchModelNameException, DuplicateModelNameException {
        Vehicle.validateName(oldName);
        Vehicle.validateName(newName);

        checkDuplicate(newName);
        getModelByName(oldName).setName(newName);
    }

    @Override
    public void addModel(String name, double cost) throws DuplicateModelNameException {
        Vehicle.validateName(name);
        checkDuplicate(name);
        Vehicle.validateCost(cost);

        //Че я тут сделал, если количество моделей равно размеру массива, то увеличиваем его путем копирования, а если не, то просто добавляем в след ячейку
        if (this.count == models.length)
            models = Arrays.copyOf(models, models.length + 1);

        models[this.count] = new Model(name, cost);
        this.count++;
    }

    @Override
    public void removeModel(String name) throws NoSuchModelNameException{
        Vehicle.validateName(name);

        int index = -1;
        for(int i = 0; i < this.count && index == -1; i++){
            if(this.models[i].getName().equals(name)) { index = i; }
        }

        if(index == -1){ throw new NoSuchModelNameException(); }

        //я кароч добавил count, чтоб смореть кол-во и, по идее, можно искать модель по имени, потом сдвигать ее вправо, в последнюю ячейку и удалять ее, но тогда размер массива не будет уменьшаться.
        for (int i = index; i < this.count - 1; i++)
            models[i] = models[i + 1];

        this.count--;
        models[this.count] = null;
    }

    @Override
    public int getSize() { return this.count ; }

    private Model getModelByName(String name) throws NoSuchModelNameException{
        for (int i = 0; i < this.count; i++){
            if(models[i].getName().equals(name))
                return models[i];
        }
        throw new NoSuchModelNameException();
    }

    private void checkDuplicate(String name) throws DuplicateModelNameException {
        for (int i = 0; i < this.count; i++) {
            if (models[i].getName().equals(name)) throw new DuplicateModelNameException();
        }
    }


}
