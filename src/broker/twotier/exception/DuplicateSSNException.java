package broker.twotier.exception;

public class DuplicateSSNException extends Exception{
	public DuplicateSSNException(){
		this("�׷� ��� �̹� �ֽ��ϴ�...");
	}
	
	public DuplicateSSNException(String message){
		super(message);
	}
}
