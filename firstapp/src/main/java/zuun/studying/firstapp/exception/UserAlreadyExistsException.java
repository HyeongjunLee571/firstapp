package zuun.studying.firstapp.exception;

public class UserAlreadyExistsException extends RuntimeException {

  public UserAlreadyExistsException(String message){
    super(message);
  }
}
