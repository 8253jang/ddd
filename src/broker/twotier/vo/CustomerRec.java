package broker.twotier.vo;

import java.util.Vector;
/*
 * 고객에 대한 정보를 저장하는 Record Class.
 * 해당 고객은 주식을 사고 파는데 연관성을 지닌 고객이다.
 * 그럼에도 불구하고 주식을 보유하지 않은(아직 주식을 사지않은) 고객도 가능하다.
 * ::
 * 주식을 보유하지 않은 고객 정보 (ssn, name, address 만으로 가능) +
 * 주식을 보유하고 있는 고객정보 (SharesRec을 여러개 지닐수 있다....Collection)
 * ::
 * 주식 거래 시스템을 사용자적 입장에서 기능적인 면을 정리, 토론 -->
 * 1. UseCase Diagram -->
 * 2. Erwin Data Modeler를 이용한 디비 설계
 *    (이때 Association Entity를 설정할수 있어야 한다) -->
 * 3. 각각의 Entity 정보를 담고 있는 Record Class(VO)를 작성 -->
 * 4. UseCase Diagram을 다시 보고 Business Logic을 작성할 메소드의 Identifier를 지정-->
 * 5. DAO 작성
 */
public class CustomerRec {
	private String ssn;
	private String name;
	private String address;
	
	private Vector<SharesRec> portfolio;

	public CustomerRec(String ssn, String name, String address,
			Vector<SharesRec> portfolio) {		
		this.ssn = ssn;
		this.name = name;
		this.address = address;
		this.portfolio = portfolio;
	}

	public CustomerRec(String ssn, String name, String address) {
		this(ssn,name, address, null);
	}

	public CustomerRec(String ssn) {
		this(ssn, " ", " ");
	}

	public String getSsn() {
		return ssn;
	}

/*	public void setSsn(String ssn) {
		this.ssn = ssn;
	}*/

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Vector<SharesRec> getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(Vector<SharesRec> portfolio) {
		this.portfolio = portfolio;
	}

	@Override
	public String toString() {
		return "CustomerRec [ssn=" + ssn + ", name=" + name + ", address="
				+ address + ", portfolio=" + portfolio + "]";
	}
	
	
	
}



















