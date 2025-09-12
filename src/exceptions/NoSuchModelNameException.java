package exceptions;

public class NoSuchModelNameException extends Exception{
  public NoSuchModelNameException(String message){
    super(message);
  }

  public NoSuchModelNameException(){
    super("Обращение к несуществующей модели");
  }
}
