package vehicles;

import exceptions.*;

public class Motocycle implements Vehicle{

    // ------------- Класс модели ------------- //
    private class Model {
        private String name = null;
        private double cost = Double.NaN;
        private Model next =  null;
        private Model prev = null;

        public Model(String name, double cost) {
            Vehicle.validateCost(cost);

            this.name = name;
            this.cost = cost;
        }

        public String getName() { return name; }

        public void setName(String name) {
            Vehicle.validateName(name);
            this.name = name;
        }

        public double getCost() { return cost; }

        public void setCost(double cost) {
            Vehicle.validateCost(cost);
            this.cost = cost;
        }
    }
    // ------------- Класс модели ------------- //

    private int size = 0;
    private String brand = null;
    private Model head = null;
    private long lastModified = 0;

    {
        lastModified = System.currentTimeMillis();
        head = new Model(null, 0);
        head.next = head;
        head.prev = head;
    }

    public Motocycle(String brand, int size) {//size
        if (brand == null || brand.isEmpty())
            throw new IllegalArgumentException("Бренд не может быть пустым");
        if (size < 0)
            throw new IllegalArgumentException("Размер не может быть отрицательным");
        else {
            this.brand = brand;
            for (int i = 0; i < size; i++) {
                Model node = new Model("Model" + (i + 1), 0);
                Model last = head.prev;
                last.next = node;
                node.prev = last;
                node.next = head;
                head.prev = node;
                this.size++;
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
    public void setModelName(String oldName, String newName) //Добавлена проверка на дубликат
            throws NoSuchModelNameException, DuplicateModelNameException {
        Vehicle.validateName(oldName);
        Vehicle.validateName(newName);

        checkDuplicate(newName);

        Model node = getModelByName(oldName);
        node.setName(newName);
        this.lastModified = System.currentTimeMillis();
    }


    @Override
    public double getModelCost(String name) throws NoSuchModelNameException {
        Vehicle.validateName(name);
        return getModelByName(name).getCost();
    }

    @Override
    public void setModelCost(String name, double cost) throws NoSuchModelNameException{
        Vehicle.validateName(name);
        Vehicle.validateCost(cost);

        getModelByName(name).setCost(cost);
        this.lastModified = System.currentTimeMillis();
    }

    @Override
    public void addModel(String name, double cost) throws DuplicateModelNameException { //Переделал голову, теперь меньше проверок
        Vehicle.validateName(name);
        Vehicle.validateCost(cost);

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
        Vehicle.validateName(name);

        if(head != null) {
            Model node = getModelByName(name);

            node.prev.next = node.next;
            node.next.prev = node.prev;

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
