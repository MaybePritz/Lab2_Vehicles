package exceptions;

public class ModelPriceOutOfBoundsException extends RuntimeException {
  public ModelPriceOutOfBoundsException(String message) {
    super(message);
  }

  public ModelPriceOutOfBoundsException() {
    super("Неверная цена модели");
  }
}
