import java.util.Arrays;

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