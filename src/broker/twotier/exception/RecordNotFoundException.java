package broker.twotier.exception;

public class RecordNotFoundException extends Exception{
	public RecordNotFoundException(){
		this("그런 고객 없습니다...");
	}
	
	public RecordNotFoundException(String message){
		super(message);
	}
}
