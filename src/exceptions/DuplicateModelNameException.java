package exceptions;

public class DuplicateModelNameException extends Exception{
  public DuplicateModelNameException(String message){
    super(message);
  }

  public DuplicateModelNameException(){
    super("Дублирование названий моделей");
  }
}
