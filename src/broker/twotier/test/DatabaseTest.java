package broker.twotier.test;

import broker.twotier.dao.Database;
import broker.twotier.vo.CustomerRec;

public class DatabaseTest {

	public static void main(String[] args)throws Exception {
		Database db = new Database("127.0.0.1");
		/*
		 * isExist() ~ sellShares() ���� ���⼭ ������ �� �׽�Ʈ �غ��� ��������
		 */
		//System.out.println(db.isExist("444-444"));
		//db.addCustomer(new CustomerRec("777-777", "�Ϲٸ�", "�Ǳ�"));
		//db.deleteCustomer("122-122");
		//db.updateCustomer(new CustomerRec("777-777", "�Ϲٸ�", "������"));
		
		/*CustomerRec returnCust = db.getCustomer("777-777");
		System.out.println(returnCust);
		System.out.println(returnCust.getPortfolio());*/
	}
}














