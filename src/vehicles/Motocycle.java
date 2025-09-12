package vehicles;

import exceptions.*;

public class Motocycle implements Vehicle{
    private static class Model {
        private String name;
        private double cost;
        private Model next;
        private Model prev;

        public Model(String name, double cost) {
            if (name == null || name.isEmpty())
                throw new IllegalArgumentException("Название модели не может быть пустым");
            else {
                if(cost < 0) {
                    throw new ModelPriceOutOfBoundsException();
                } else {
                    this.name = name;
                    this.cost = cost;
                }
            }
        }

        public String getName() { return name; }

        public void setName(String name) {
            if (name == null || name.isEmpty())
                throw new IllegalArgumentException("Название модели не может быть пустым");
            else
                this.name = name;
        }

        public double getCost() { return cost; }

        public void setCost(double cost) {
            if(cost < 0)
                throw new ModelPriceOutOfBoundsException();
            else
                this.cost = cost;
        }
    }

    private int size = 0;
    private String brand = null;
    private Model head = null;
    private long lastModified = 0;

    {
        lastModified = System.currentTimeMillis();
    }

    public Motocycle(String brand) {
        if (brand == null || brand.isEmpty())
            throw new IllegalArgumentException("Бренд не может быть пустым");
        else
            this.brand = brand;
    }

    @Override
    public String[] getModelsName() {
        String[] names = new String[size];
        Model p = head;
        for (int i = 0; i < size; i++) {
            names[i] = p.getName();
            p = p.next;
        }
        return names;
    }

    @Override
    public double[] getModelsCost(){
        double[] costs = new double[size];
        Model p = head;
        for(int i = 0; i < size; i++){
            costs[i] = p.getCost();
            p = p.next;
        }
        return costs;
    }

    @Override
    public void changeModelName(int index, String name) {
        Model node = getModelByIndex(index);
        node.setName(name);
        this.lastModified = System.currentTimeMillis();
    }

    @Override
    public double getModelCost(String name) throws NoSuchModelNameException {
        return getModelByName(name).getCost();
    }

    @Override
    public void setModelCost(String name, double cost) throws NoSuchModelNameException{
        if (cost < 0) {
            throw new ModelPriceOutOfBoundsException("Цена не может быть отрицательной");
        } else{
            getModelByName(name).setCost(cost);
            this.lastModified = System.currentTimeMillis();
        }
    }

    @Override
    public void addModel(String name, double cost){
        Model newNode = new Model(name, cost);

        if (head == null) {
            head = newNode;
            newNode.next = newNode;
            newNode.prev = newNode;
        } else {
            Model last = head.prev;

            last.next = newNode;
            newNode.prev = last;

            newNode.next = head;
            head.prev = newNode;
        }

        this.size++;
        this.lastModified = System.currentTimeMillis();
    }

    @Override
    public void removeModel(String name) throws NoSuchModelNameException {
        if(head != null) {
            Model node = getModelByName(name);

            if(this.size == 1){
                head = null;
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;

                if(node == head){
                    head = node.next;
                }
            }

            this.size--;
            this.lastModified = System.currentTimeMillis();
        }
    }

    @Override
    public int getSize() { return this.size; }
    @Override
    public String getBrand() { return this.brand; }
    @Override
    public void setBrand(String brand) { this.brand = brand; }

    // ------------- Приватные методы ------------- //

    private Model getModelByIndex(int index){
        if(index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Индекс вне списка");
        } else{
            Model p = head;
            for (int i = 0; i < index; i++){
                p = p.next;
            }
            return p;
        }
    }
    private Model getModelByName(String name) throws NoSuchModelNameException{
        if (head == null) {
            throw new NoSuchModelNameException("Список пуст");
        } else{
            Model p = head;
            while (p.next != head) {
                if (p.getName().equals(name)) {
                    return p;
                }
                p = p.next;
            }

            if (p.getName().equals(name)) {
                return p;
            } else{
                throw new NoSuchModelNameException("Модель с таким именем не найдена");
            }
        }
    }

    // ------------- Приватные методы ------------- //
}
