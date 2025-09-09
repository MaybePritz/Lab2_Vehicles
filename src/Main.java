import java.util.Arrays;

/**
 * Задание 5:
 *  - Создать утилитарный класс со статическими методами:
 *    • среднее арифметическое цен всех моделей,
 *    • вывод списка всех моделей,
 *    • вывод списка цен моделей.
 *
 * Результат:
 *  - Реализованы классы Автомобиль и Мотоцикл,
 *  - Используются собственные исключения,
 *  - Интерфейс ТранспортноеСредство,
 *  - Утилитарный класс для работы с интерфейсом.
 */

//чета поахуевали с такими заданиями

// ------------- Исключения ------------- //

class NoSuchModelNameException extends Exception{
    public NoSuchModelNameException(String message){
        super(message);
    }

    public NoSuchModelNameException(){
        super("Обращение к несуществующей модели");
    }
}

class DuplicateModelNameException extends Exception{
    public DuplicateModelNameException(String message){
        super(message);
    }

    public DuplicateModelNameException(){
        super("Дублирование названий моделей");
    }
}

class ModelPriceOutOfBoundsException extends RuntimeException {
    public ModelPriceOutOfBoundsException(String message) {
        super(message);
    }

    public ModelPriceOutOfBoundsException() {
        super("Неверная цена модели");
    }
}

// ------------- Исключения ------------- //


public class Main {
    public static void main(String[] args) {
        Automobile auto = new Automobile("Skoda");
        Motocycle moto = new Motocycle("Honda");

        System.out.println("// ------------- Машины ------------- //");
        try {
            auto.addModel("Octavia", 3000000);
            auto.addModel("Yeti", 2000000);

            System.out.println(Arrays.toString(auto.getModelsName())); // Октавия и Йети
            System.out.println(Arrays.toString(auto.getModelsCost())); // 3кк и 2кк

            System.out.println(auto.getModelCost("Octavia")); // 3кк

            auto.setModelCost("Octavia", 3100000);
            System.out.println(auto.getModelCost("Octavia")); // 3.1кк

            auto.removeModel("Yeti");
            System.out.println(auto.getSize()); // 1

        } catch (DuplicateModelNameException e) {
            System.out.println("Ошибка: модель с таким именем уже существует");
        } catch (NoSuchModelNameException e) {
            System.out.println("Ошибка: модель не найдена");
        }

        System.out.println("// ------------- Мотоциклы ------------- //");

        try {
            moto.addModel("CBR600RR", 1200000);
            moto.addModel("AfricaTwin", 1500000);

            System.out.println(Arrays.toString(moto.getModelsName())); // CBR600RR и AfricaTwin
            System.out.println(Arrays.toString(moto.getModelsCost())); // 1.2кк и 1.5кк

            System.out.println(moto.getModelCost("CBR600RR")); // 1.2кк

            moto.setModelCost("CBR600RR", 1250000);
            System.out.println(moto.getModelCost("CBR600RR")); // 1.25кк

            moto.changeModelName(0, "CBR650RR");
            System.out.println(Arrays.toString(moto.getModelsName())); // CBR650RR и AfricaTwin

            moto.removeModel("AfricaTwin");
            System.out.println(moto.getSize()); // 1
            System.out.println(Arrays.toString(moto.getModelsName())); // CBR650RR

        } catch (NoSuchModelNameException e) {
            System.out.println("Ошибка: модель не найдена");
        } catch (ModelPriceOutOfBoundsException e) {
            System.out.println("Ошибка: цена модели не может быть отрицательной");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}

interface Vehicle {
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


class Automobile implements Vehicle{
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
    public double getModelCost(String name) throws NoSuchModelNameException{
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
    public void addModel(String name, double cost) throws DuplicateModelNameException{
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

class Motocycle implements Vehicle{
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

    public Motocycle(String brand) {
        if (brand == null || brand.isEmpty())
            throw new IllegalArgumentException("Бренд не может быть пустым");
        else {
            this.brand = brand;
            this.lastModified = System.currentTimeMillis();
        }
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
    public double getModelCost(String name) throws NoSuchModelNameException{
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