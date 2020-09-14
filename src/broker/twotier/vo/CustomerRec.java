package broker.twotier.vo;

import java.util.Vector;
/*
 * ���� ���� ������ �����ϴ� Record Class.
 * �ش� ���� �ֽ��� ��� �Ĵµ� �������� ���� ���̴�.
 * �׷����� �ұ��ϰ� �ֽ��� �������� ����(���� �ֽ��� ��������) ���� �����ϴ�.
 * ::
 * �ֽ��� �������� ���� �� ���� (ssn, name, address ������ ����) +
 * �ֽ��� �����ϰ� �ִ� ������ (SharesRec�� ������ ���Ҽ� �ִ�....Collection)
 * ::
 * �ֽ� �ŷ� �ý����� ������� ���忡�� ������� ���� ����, ��� -->
 * 1. UseCase Diagram -->
 * 2. Erwin Data Modeler�� �̿��� ��� ����
 *    (�̶� Association Entity�� �����Ҽ� �־�� �Ѵ�) -->
 * 3. ������ Entity ������ ��� �ִ� Record Class(VO)�� �ۼ� -->
 * 4. UseCase Diagram�� �ٽ� ���� Business Logic�� �ۼ��� �޼ҵ��� Identifier�� ����-->
 * 5. DAO �ۼ�
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



















