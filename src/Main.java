import java.util.Arrays;

/**
 * Лабораторная работа №2
 * Тема: Исключения, интерфейсы и работа с коллекциями в Java
 *
 * Цель работы:
 *  - закрепить навыки объектно-ориентированного программирования в Java,
 *  - изучить вложенные классы, работу с массивами и связными списками,
 *    обработку исключений (checked и unchecked),
 *    использование интерфейсов,
 *    организацию кода с помощью утилитарных классов.
 *
 * Задание 1:
 *  - Создать класс Автомобиль:
 *    - поле String марка,
 *    - методы get/set марки,
 *    - внутренний класс Модель (название + цена),
 *    - массив моделей внутри Автомобиль,
 *    - методы:
 *      • изменить название модели,
 *      • получить массив названий моделей,
 *      • получить цену модели по названию,
 *      • изменить цену модели по названию,
 *      • получить массив цен моделей,
 *      • добавить модель (Arrays.copyOf),
 *      • удалить модель (System.arraycopy + Arrays.copyOf),
 *      • получить размер массива моделей.
 *    - Конструктор: принимает марку и размер массива моделей,
 *      заполняет уникальными названиями и случайными ценами.
 *
 * Задание 2:
 *  - Создать класс Мотоцикл:
 *    • аналогично Автомобилю, но использовать двусвязный циклический список с головой,
 *    • хранить поле lastModified (дата последнего изменения),
 *      инициализировать в блоке инициализации и обновлять при модификации.
 *
 * Задание 3:
 *  - Создать исключения:
 *    • NoSuchModelNameException (checked) – обращение к несуществующей модели,
 *    • DuplicateModelNameException (checked) – дублирование названий моделей,
 *    • ModelPriceOutOfBoundsException (unchecked) – неверная цена модели.
 *
 * Задание 4:
 *  - Создать интерфейс ТранспортноеСредство:
 *    • общие методы для Автомобиля и Мотоцикла.
 *  - Реализовать его в обоих классах.
 *
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

public class Main {
    public static void main(String[] args) {
        Automobile auto = new Automobile("Skoda", 0);

        auto.addModel("Octavia", 3000000);
        auto.addModel("Yeti", 2000000);

        System.out.println(Arrays.toString(auto.getModelsName())); // Октавия и йети, должно быть
        System.out.println(Arrays.toString(auto.getModelsCost()));    // 3кк и 2кк

        System.out.println(auto.getModelCost("Octavia")); // 3кк

        auto.setModelCost("Octavia", 3100000);
        System.out.println(auto.getModelCost("Octavia")); // 3.1кк

        auto.removeModel("Yeti");
        System.out.println(auto.getModelsCount()); // 1
    }
}


class Automobile{
    private String brand;
    private Model[] models;

    public Automobile(String brand, int size) {
        this.brand = brand;
        this.models = new Model[size];
    }

    public String getBrand() { return this.brand; }
    public void setBrand(String brand) { this.brand = brand; }

    static class Model {
        private String name;
        private int cost;

        public Model(String name, int cost) {
            this.name = name;
            this.cost = cost;
        }

        public String getName() { return this.name; }
        public void setName(String name) { this.name = name; }

        public int getCost() { return this.cost; }
        public void setCost(int cost) { this.cost = cost; }

    }

    public String[] getModelsName() {
        String[] names = new String[models.length];
        for (int i = 0; i < models.length; i++) { names[i] = models[i].getName(); }

        return names;
    }

    public int getModelCost(String name){
        for(Model model :  this.models){
            if(model.getName().equals(name)) { return model.cost; }
        }
        throw new IllegalArgumentException("Модель не найдена");
    }

    public void setModelCost(String name, int cost){
        if(cost > 0){
            for(Model model :  this.models){
                if(model.getName().equals(name)) {
                    model.cost = cost;
                    return;
                }
            }

            throw new IllegalArgumentException("Модель не найдена");
        }
        else { throw new IllegalArgumentException("Стоимость должна быть положительной"); }
    }

    public int[] getModelsCost(){
        int[] costs = new int[this.models.length];
        for(int i = 0; i < this.models.length; i++) { costs[i] = this.models[i].getCost(); }
        return costs;
    }

    public void addModel(String name, int cost){
        for(Model model :  this.models){
            if(model.getName().equals(name)){ throw new IllegalArgumentException("Модель с таким названием уже существует"); }
        }

        models = Arrays.copyOf(this.models, this.models.length + 1);
        models[this.models.length-1] = new Model(name, cost);
    }

    public void removeModel(String name){
        int index = -1;
        for(int i = 0; i < this.models.length && index == -1; i++){
            if(this.models[i].getName().equals(name)) { index = i; }
        }

        if(index == -1){ throw new IllegalArgumentException("Модель с таким названием не найдена"); }

        Model[] newArray = new Model[this.models.length - 1];
        System.arraycopy(this.models, 0, newArray, 0, index);
        System.arraycopy(this.models, index + 1, newArray, index, newArray.length - index);
        models = Arrays.copyOf(newArray, newArray.length);
    }

    public int getModelsCount() { return this.models.length; }
}
