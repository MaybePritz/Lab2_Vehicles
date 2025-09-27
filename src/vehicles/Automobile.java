package vehicles;

import exceptions.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Класс {@code Automobile} - автомобиль с брендом и списком моделей с их ценами.
 */
public class Automobile implements Vehicle, Serializable {

    /**
     * Название бренда автомобиля.
     */
    private String brand = null;

    /**
     * Массив моделей автомобиля.
     */
    private Model[] models = null;

    /**
     * Конструктор
     *
     * @param brand название бренда автомобиля
     * @param size  количество моделей
     * @throws IllegalArgumentException   если бренд пустой или null, или размер отрицательный
     * @throws DuplicateModelNameException если при добавлении моделей возникнет дублирование
     */
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

    /**
     * Возвращает бренд автомобиля.
     *
     * @return {@code brand}
     */
    @Override
    public String getBrand() {
        return this.brand;
    }

    /**
     * Устанавливает бренд автомобиля.
     *
     * @param brand название бренда
     */
    @Override
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Внутренний класс, представляющий модель автомобиля с именем и ценой.
     */
    private class Model {
        private String name = null;
        private double cost = Double.NaN;

        /**
         * Конструктор модели.
         *
         * @param name имя модели
         * @param cost цена модели
         */
        public Model(String name, double cost) {
            this.name = name;
            this.cost = cost;
        }

        /**
         * Возвращает имя модели.
         *
         * @return имя модели
         */
        public String getName() {
            return this.name;
        }

        /**
         * Устанавливает имя модели.
         *
         * @param name имя модели
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Возвращает цену модели.
         *
         * @return цена модели
         */
        public double getCost() {
            return this.cost;
        }

        /**
         * Устанавливает цену модели.
         *
         * @param cost цена модели
         */
        public void setCost(double cost) {
            this.cost = cost;
        }
    }

    /**
     * Возвращает массив имен всех моделей автомобиля.
     *
     * @return массив имён моделей
     */
    @Override
    public String[] getModelsName() {
        String[] names = new String[models.length];
        for (int i = 0; i < models.length; i++) {
            names[i] = models[i].getName();
        }
        return names;
    }

    /**
     * Возвращает массив цен всех моделей автомобиля.
     *
     * @return массив цен моделей
     */
    @Override
    public double[] getModelsCost() {
        double[] costs = new double[models.length];
        for (int i = 0; i < models.length; i++) {
            costs[i] = this.models[i].getCost();
        }
        return costs;
    }

    /**
     * Возвращает цену модели по имени.
     *
     * @param name имя модели
     * @return цена модели
     * @throws NoSuchModelNameException если модель с указанным именем не найдена
     */
    @Override
    public double getModelCost(String name) throws NoSuchModelNameException {
        for (int i = 0; i < models.length; i++) {
            if (models[i].getName().equals(name)) return models[i].getCost();
        }
        throw new NoSuchModelNameException();
    }

    /**
     * Устанавливает цену модели по имени.
     *
     * @param name имя модели
     * @param cost цена модели
     * @throws NoSuchModelNameException    если модель с указанным именем не найдена
     * @throws IllegalArgumentException    если имя модели пустое или null
     * @throws ModelPriceOutOfBoundsException если цена отрицательная
     */
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

    /**
     * Изменяет имя существующей модели автомобиля.
     *
     * <p>Ищет модель по имени {@code oldName} и присваивает ей новое имя {@code newName}.
     * Если модель с именем {@code oldName} не найдена, или если уже существует модель с именем {@code newName},
     * метод генерирует соответствующее исключение.</p>
     *
     * @param oldName текущее имя модели
     * @param newName новое имя модели
     * @throws NoSuchModelNameException     если модель с именем {@code oldName} не найдена
     * @throws DuplicateModelNameException  если модель с именем {@code newName} уже существует
     * @throws IllegalArgumentException     если {@code oldName} или {@code newName} пустые
     */
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

    /**
     * Добавляет модель автомобиля в конец списка моделей.
     *
     * @param name имя модели
     * @param cost цена модели
     * @throws DuplicateModelNameException   если модель с таким именем уже существует
     * @throws IllegalArgumentException      если имя пустое или null
     * @throws ModelPriceOutOfBoundsException если цена отрицательная
     */
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

    /**
     * Удаляет модель автомобиля по имени.
     *
     * @param name имя модели
     * @throws NoSuchModelNameException если модель с указанным именем не найдена
     */
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

    /**
     * Сравнивает текущее транспортное средство с другим объектом.
     *
     * <p>Метод возвращает {@code true} только если объект {@code obj} является {@link Vehicle},
     * и имеет тот же бренд, список моделей и цены моделей.</p>
     *
     * @param obj объект для сравнения
     * @return {@code true}, если объекты равны по бренду, моделям и ценам моделей
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj instanceof Vehicle comparableVehicle) {
            if (!this.getBrand().equals(comparableVehicle.getBrand())) return false;
            if (!Arrays.equals(this.getModelsName(), comparableVehicle.getModelsName())) return false;
            return Arrays.equals(this.getModelsCost(), comparableVehicle.getModelsCost());
        } else return false;
    }

    /**
     * Вычисляет hashCode автомобиля, основываясь на бренде, именах и ценах моделей.
     *
     * @return hash-код объекта
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(this.brand);
        result = 31 * result + Arrays.hashCode(this.getModelsName());
        result = 31 * result + Arrays.hashCode(this.getModelsCost());
        return result;
    }

    /**
     * Возвращает количество моделей автомобиля.
     *
     * @return размер массива моделей
     */
    @Override
    public int getSize() {
        return models.length;
    }

    /**
     * Получает модель по имени.
     *
     * @param name имя модели
     * @return модель с указанным именем
     * @throws NoSuchModelNameException если модель не найдена
     */
    private Model getModelByName(String name) throws NoSuchModelNameException {
        for (int i = 0; i < models.length; i++) {
            if (models[i].getName().equals(name))
                return models[i];
        }
        throw new NoSuchModelNameException();
    }

    /**
     * Проверяет, существует ли модель с указанным именем.
     *
     * @param name имя модели для проверки
     * @throws DuplicateModelNameException если модель с таким именем уже существует
     */
    private void checkDuplicate(String name) throws DuplicateModelNameException {
        for (int i = 0; i < models.length; i++) {
            if (models[i].getName().equals(name)) throw new DuplicateModelNameException();
        }
    }
}
