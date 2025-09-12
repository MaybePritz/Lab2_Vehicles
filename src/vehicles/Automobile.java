package vehicles;

import exceptions.DuplicateModelNameException;
import exceptions.ModelPriceOutOfBoundsException;
import exceptions.NoSuchModelNameException;

import java.util.Arrays;

public class Automobile implements Vehicle{
    private String brand = null;
    private Model[] models = null;

    public Automobile(String brand) {
        if (brand == null || brand.isEmpty())
            throw new IllegalArgumentException("Бренд не может быть пустым");
        this.brand = brand;
        this.models = new Model[0];
    }

    @Override
    public String getBrand() { return this.brand; }

    @Override
    public void setBrand(String brand) { this.brand = brand; }

    private class Model {
        private String name = null;
        private double cost = Double.NaN;

        public Model(String name, double cost) {
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Название модели не может быть пустым");
            }
            if (cost < 0) {
                throw new ModelPriceOutOfBoundsException("Цена модели не может быть отрицательной");
            }
            this.name = name;
            this.cost = cost;
        }

        public String getName() { return this.name; }
        public void setName(String name) { this.name = name; }

        public double getCost() { return this.cost; }
        public void setCost(double cost) { this.cost = cost; }

    }

    @Override
    public String[] getModelsName() {
        String[] names = new String[models.length];
        for (int i = 0; i < models.length; i++) { names[i] = models[i].getName(); }

        return names;
    }

    @Override
    public double[] getModelsCost(){
        double[] costs = new double[this.models.length];
        for(int i = 0; i < this.models.length; i++) { costs[i] = this.models[i].getCost(); }
        return costs;
    }

    @Override
    public double getModelCost(String name) throws NoSuchModelNameException {
        for(Model model :  this.models){
            if(model.getName().equals(name)) { return model.cost; }
        }
        throw new NoSuchModelNameException("Модель не найдена");
    }

    @Override
    public void setModelCost(String name, double cost) throws NoSuchModelNameException {
        if (cost < 0) {
            throw new ModelPriceOutOfBoundsException("Цена не может быть отрицательной");
        } else {
            for (Model model : models) {
                if (model.getName().equals(name)) {
                    model.setCost(cost);
                    return;
                }
            }
            throw new NoSuchModelNameException();
        }

    }

    @Override
    public void changeModelName(int index, String name) {
        if (index < 0 || index >= models.length) {
            throw new IndexOutOfBoundsException("Индекс вне диапазона массива");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Название модели не может быть пустым");
        }
        models[index].setName(name);
    }

    @Override
    public void addModel(String name, double cost) throws DuplicateModelNameException {
        for(Model model :  this.models){
            if(model.getName().equals(name)){ throw new DuplicateModelNameException(); }
        }

        models = Arrays.copyOf(this.models, this.models.length + 1);
        models[this.models.length-1] = new Model(name, cost);
    }

    @Override
    public void removeModel(String name) throws NoSuchModelNameException{
        int index = -1;
        for(int i = 0; i < this.models.length && index == -1; i++){
            if(this.models[i].getName().equals(name)) { index = i; }
        }

        if(index == -1){ throw new NoSuchModelNameException(); }

        Model[] newArray = new Model[this.models.length - 1];
        System.arraycopy(this.models, 0, newArray, 0, index);
        System.arraycopy(this.models, index + 1, newArray, index, newArray.length - index);
        models = Arrays.copyOf(newArray, newArray.length);
    }

    @Override
    public int getSize() { return this.models.length; }
}
