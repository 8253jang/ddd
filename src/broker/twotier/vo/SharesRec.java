package broker.twotier.vo;
/*
 * 누가
 * 어떤 주식을 
 * 얼마만큼 보유하고 있는지의 정보를 담고 있는 클래스
 */
public class SharesRec {
	private String ssn;
	private String symbol;
	private int quantity;
	
	public SharesRec(String ssn, String symbol, int quantity) {
		super();
		this.ssn = ssn;
		this.symbol = symbol;
		this.quantity = quantity;
	}
	public SharesRec() {
		this(" "," ",0);
	}
	public SharesRec(String ssn) {
		this(ssn, " ", 0);
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	@Override
	public String toString() {
		return "SharesRec [ssn=" + ssn + ", symbol=" + symbol + ", quantity="
				+ quantity + "]";
	}
	
}


















