package broker.twotier.test;

import broker.twotier.dao.Database;
import broker.twotier.vo.CustomerRec;

public class DatabaseTest {

	public static void main(String[] args)throws Exception {
		Database db = new Database("127.0.0.1");
		/*
		 * isExist() ~ sellShares() 까지 여기서 일일히 다 테스트 해보고 돌려본다
		 */
		//System.out.println(db.isExist("444-444"));
		//db.addCustomer(new CustomerRec("777-777", "하바리", "판교"));
		//db.deleteCustomer("122-122");
		//db.updateCustomer(new CustomerRec("777-777", "하바리", "서현동"));
		
		/*CustomerRec returnCust = db.getCustomer("777-777");
		System.out.println(returnCust);
		System.out.println(returnCust.getPortfolio());*/
	}
}














