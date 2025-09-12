package vehicles;

public final class VehicleUtils{
    private VehicleUtils() {}

    public static double averagePrice(Vehicle vehicle){
        double[] prices = vehicle.getModelsCost();
        double avg = 0;
        double sum = 0;
        if(prices == null || prices.length == 0)
            avg = 0;
        else {
            for(double price : prices)
                sum += price;
            avg = sum / prices.length;
        }
        return avg;
    }

    public static void printModels(Vehicle vehicle) {
        String[] models = vehicle.getModelsName();

        if (models == null || models.length == 0) {
            System.out.println("Список моделей пуст");
        } else {
            for(String model : models)
                System.out.println(model);
        }
    }

    public static void printPrices(Vehicle vehicle) {
        double[] prices = vehicle.getModelsCost();
        if (prices == null || prices.length == 0) {
            System.out.println("Список цен пуст");
        } else {
            for(double price : prices)
                System.out.println(price);
        }
    }
}
