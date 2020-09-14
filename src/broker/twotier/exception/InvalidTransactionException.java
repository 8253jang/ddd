package broker.twotier.exception;

public class InvalidTransactionException extends Exception{
	public InvalidTransactionException(){
		this("가지고 있는 주식보다 팔려는 주식이 넘 많아여..");
	}
	
	public InvalidTransactionException(String message){
		super(message);
	}
}
