package broker.twotier.exception;

public class DuplicateSSNException extends Exception{
	public DuplicateSSNException(){
		this("그런 사람 이미 있습니다...");
	}
	
	public DuplicateSSNException(String message){
		super(message);
	}
}
