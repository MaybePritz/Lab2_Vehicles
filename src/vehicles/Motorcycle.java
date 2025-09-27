package vehicles;

import exceptions.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class Motorcycle implements Vehicle, Serializable{

    // ------------- Класс модели ------------- //
    private static class Model implements Serializable {
        private String name = null;
        private double cost = Double.NaN;
        private Model next =  null;
        private Model prev = null;

        public Model(String name, double cost) {
            this.name = name;
            this.cost = cost;
        }

        public String getName() { return name; }

        public void setName(String name) {
            this.name = name;
        }

        public double getCost() { return cost; }

        public void setCost(double cost) {
            this.cost = cost;
        }
    }
    // ------------- Класс модели ------------- //

    private int size = 0;
    private String brand = null;
    private Model head = null;
    private transient long lastModified = 0;

    {
        lastModified = System.currentTimeMillis();

        head = new Model(null, 0);
        head.next = head;
        head.prev = head;
    }

    public Motorcycle(String brand, int size) throws DuplicateModelNameException {//size
        if (brand == null || brand.isEmpty())
            throw new IllegalArgumentException("Бренд не может быть пустым");
        if (size < 0)
            throw new IllegalArgumentException("Размер не может быть отрицательным");
        else {
            this.brand = brand;
            for (int i = 0; i < size; i++) {
                this.addModel("Модель" + (i + 1), 0);
            }
        }
    }

    @Override
    public String[] getModelsName() {
        String[] names = new String[size];
        Model p = head.next;
        for (int i = 0; i < size; i++) {
            names[i] = p.getName();
            p = p.next;
        }
        return names;
    }

    @Override
    public double[] getModelsCost(){
        double[] costs = new double[size];
        Model p = head.next;
        for(int i = 0; i < size; i++){
            costs[i] = p.getCost();
            p = p.next;
        }
        return costs;
    }

    @Override
    public void setModelName(String oldName, String newName)
            throws NoSuchModelNameException, DuplicateModelNameException {

        if (oldName == null || oldName.isEmpty() || newName == null || newName.isEmpty()) {
            throw new IllegalArgumentException("Имя модели не может быть пустым");
        }

        Model p = head.next;

        if (p.next == head) {
            throw new NoSuchModelNameException();
        }

        Model findModel = null;
        while(p != head){
            if(newName.equals(p.name)){
                throw new DuplicateModelNameException();
            }
            if(findModel == null && oldName.equals(p.name)){
                findModel = p;
            }
            p = p.next;
        }
        if(findModel == null){
            throw new NoSuchModelNameException();
        }
        else{
            findModel.setName(newName);
            this.lastModified = System.currentTimeMillis();
        }
    }

    @Override
    public double getModelCost(String name) throws NoSuchModelNameException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Имя модели не может быть пустым");
        }

        return getModelByName(name).getCost();
    }

    @Override
    public void setModelCost(String name, double cost) throws NoSuchModelNameException{
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Имя модели не может быть пустым");
        }

        if (cost < 0)
            throw new ModelPriceOutOfBoundsException("Цена модели не может быть отрицательной");

        getModelByName(name).setCost(cost);
        this.lastModified = System.currentTimeMillis();
    }

    @Override
    public void addModel(String name, double cost) throws DuplicateModelNameException { //Переделал голову, теперь меньше проверок
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Имя модели не может быть пустым");
        }

        if (cost < 0)
            throw new ModelPriceOutOfBoundsException("Цена модели не может быть отрицательной");

        checkDuplicate(name);

        Model newNode = new Model(name, cost);
        Model last = head.prev;

        last.next = newNode;
        newNode.prev = last;

        newNode.next = head;
        head.prev = newNode;

        this.size++;
        this.lastModified = System.currentTimeMillis();
    }

    @Override
    public void removeModel(String name) throws NoSuchModelNameException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Имя модели не может быть пустым");
        }

        if(head != null) {
            Model node = getModelByName(name);

            node.prev.next = node.next;
            node.next.prev = node.prev;

            this.size--;
            this.lastModified = System.currentTimeMillis();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(this.hashCode() == obj.hashCode()) return true;

        if(obj instanceof Vehicle comparableVehicle) {

            if(!this.getBrand().equals(comparableVehicle.getBrand()))  return false;

            if(!Arrays.equals(this.getModelsName(), comparableVehicle.getModelsName()))  return false;
            return Arrays.equals(this.getModelsCost(), comparableVehicle.getModelsCost());
        }
        else return false;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(this.brand);
        result = 31 * result + Arrays.hashCode(this.getModelsName());
        result = 31 * result + Arrays.hashCode(this.getModelsCost());
        return result;
    }

    @Override
    public int getSize() { return this.size; }
    @Override
    public String getBrand() { return this.brand; }
    @Override
    public void setBrand(String brand) { this.brand = brand; }

    // ------------- Приватные методы ------------- //

    private Model getModelByName(String name) throws NoSuchModelNameException {
         Model p = head.next;
            while (p != head) {
                if (p.getName().equals(name))
                    return p;
                p = p.next;
            }
            throw new NoSuchModelNameException("Модель с таким именем не найдена");
    }

    private void checkDuplicate(String name) throws DuplicateModelNameException {
        Model p = head.next;
        while (p != head) {
            if (p.getName().equals(name)) {
                throw new DuplicateModelNameException(name);
            }
            p = p.next;
        }
    }

    // ------------- Приватные методы ------------- //
}
