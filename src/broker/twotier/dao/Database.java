package broker.twotier.dao;
/*
 * Exception ::
 * DuplicateSSNException,
 * RecordNotFoundException,
 * InvalidTransactionException
 * : �ȷ��� �ֽ��� ���ڰ� ������ �ִ°� ���� �� ������
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.naming.PartialResultException;

import config.OracleInfo;
import broker.twotier.exception.DuplicateSSNException;
import broker.twotier.exception.InvalidTransactionException;
import broker.twotier.exception.RecordNotFoundException;
import broker.twotier.vo.CustomerRec;
import broker.twotier.vo.SharesRec;
import broker.twotier.vo.StockRec;

/*
 * DB�� Access�ϴ� �����Ͻ� ������ ��� �ִ� Ŭ����
 * UseCase Diagram�� ���� ����� ����θ� �̾Ƴ��ڴ�.
 */
public class Database {
	 public Database(String server)throws ClassNotFoundException{
		 Class.forName(OracleInfo.DRIVER);
		 System.out.println("����̹� �ε� ����...");
	 }
	 
	 //////// �������� ���� /////////////////////
	 public Connection getConnect() throws SQLException{
		Connection conn = 
				DriverManager.getConnection(OracleInfo.URL, OracleInfo.USER, OracleInfo.PASS);
		System.out.println("��� ���� ����...getConnect()...");
		 return conn;
	 }
	 
	 public void closeAll(PreparedStatement ps, Connection conn)throws SQLException{
		 if(ps != null) ps.close();
		 if(conn != null) conn.close();
	 }
	 public void closeAll(ResultSet rs,PreparedStatement ps, Connection conn)throws SQLException{
		 if(rs != null) rs.close();
			closeAll(ps, conn);
	 }
	 
	 //////////////// �����Ͻ� ���� ////////////////////////////////////
	 private boolean isExist(Connection conn,String ssn) throws SQLException{
		//Connection conn = getConnect();
		String query = "SELECT ssn FROM customer WHERE ssn=?";
		PreparedStatement ps = conn.prepareStatement(query);
		ps.setString(1, ssn);
		ResultSet rs = ps.executeQuery();
		return rs.next();
	 }
	 
	 public void addCustomer(CustomerRec cust)throws SQLException, DuplicateSSNException{
		Connection conn = null;
		PreparedStatement ps = null;
		try{
			conn = getConnect();
			if(!isExist(conn,cust.getSsn())){
				String query = "INSERT INTO customer VALUES(?,?,?)";
				ps = conn.prepareStatement(query);
				ps.setString(1, cust.getSsn());
				ps.setString(2, cust.getName());
				ps.setString(3, cust.getAddress());
				System.out.println(ps.executeUpdate()+" �� insert success....addCustomer()");
			}else{
				throw new DuplicateSSNException("�׷� ��� �̹� �־...");
			}
		}finally{
			closeAll(ps, conn);
		}
				 
	 }
	 public void deleteCustomer(String ssn)throws SQLException,RecordNotFoundException{
		 Connection conn = null;
		 PreparedStatement ps = null;		 
		 try{
			 conn = getConnect();
			 if(isExist(conn, ssn)){
				 String query = "DELETE FROM customer WHERE ssn=?";			
				 ps = conn.prepareStatement(query);
				 ps.setString(1,ssn);			
				 System.out.println(ps.executeUpdate()+"�� delete success...deleteCustomer()");
			 }else{
				 throw new RecordNotFoundException("������ ��� ���..");
			 }
		 }finally{
			 closeAll(ps, conn);			 
		 }
	 }
	 public void updateCustomer(CustomerRec cust)throws SQLException, RecordNotFoundException{
		 Connection conn = null;
		 PreparedStatement ps = null;		
		 try{
			 conn = getConnect();
			 String query ="UPDATE customer SET cust_name=?, address=? WHERE ssn=?";
			 ps = conn.prepareStatement(query);
			 ps.setString(1, cust.getName());
			 ps.setString(2, cust.getAddress());
			 ps.setString(3, cust.getSsn());
			 int row = ps.executeUpdate();
			 
			 if(row==1) System.out.println(row+" �� update success...");
			 else throw new RecordNotFoundException("������ ����� ���..");
		 }finally{
			 closeAll(ps, conn);
		 }
	 }
	 /*
	  * ���� ������ �ֽ� ����(shares)....
	  * �Ѹ��� ���� �������� �ֽ������� �����Ҽ� �ֱ� ������...Vector�� ��Ҵ�.
	  */
	 public Vector<SharesRec> getPortfolio(String ssn)throws SQLException{
		 Connection conn = null;
		 PreparedStatement ps = null;	
		 ResultSet rs = null;
		 Vector<SharesRec> v = new Vector<SharesRec>();
		 try{
			 conn = getConnect();
			 String query = "SELECT * FROM shares WHERE ssn=?";
			 ps = conn.prepareStatement(query);
			 ps.setString(1,ssn);
			 rs = ps.executeQuery();
			 while(rs.next()){
				 v.add(new SharesRec(ssn, 
						 			rs.getString("symbol"), 
						 			rs.getInt("quantity")));
			 }			 
		 }finally{
			 closeAll(rs, ps, conn);
		 }
		 return v; //while�� �ٱ�����...
	 }
	 /*
	  * ���� ���� ���� ����(customer) + ���� ������ �ֽ� ����(shares)....
	  */
	 public CustomerRec getCustomer(String ssn)throws SQLException{
		 Connection conn = null;
		 PreparedStatement ps = null;	
		 ResultSet rs = null;
		 CustomerRec cust = null;
		 try{
			 conn = getConnect();
			 String query = "SELECT * FROM customer WHERE ssn=?";
			 ps = conn.prepareStatement(query);
			 ps.setString(1,ssn);
			 rs = ps.executeQuery();
			 if(rs.next()){
				 cust = new CustomerRec(ssn, 
						 				rs.getString("cust_name"), 
						 				rs.getString("address"));
			 }//
			 cust.setPortfolio(getPortfolio(ssn));
		 }finally{
			 closeAll(rs, ps, conn);
		 }
		 return cust;
	 }
	 public ArrayList<CustomerRec> getAllCustomers()throws SQLException{
		 Connection conn = null;
		 PreparedStatement ps = null;	
		 ResultSet rs = null;
		 ArrayList<CustomerRec> list = new ArrayList<CustomerRec>();
		 try{
			 
			 conn=  getConnect();
			 String query = "SELECT * FROM customer";
			 ps = conn.prepareStatement(query);
			 rs = ps.executeQuery();
			 while(rs.next()){
				 list.add(new CustomerRec(rs.getString(1), 
						 				  rs.getString(2), 
						 				  rs.getString(3), 
						 				  getPortfolio(rs.getString(1))));
			 }
			/* for(int i=0; i<list.size(); i++){
				 list.get(i).setPortfolio(getPortfolio(list.get(i).getSsn()));
			 }*/
			 
		 }finally{
			 closeAll(rs, ps, conn);
		 }
		 return list;
	 }
	 
	 public ArrayList<StockRec> getAllStocks()throws SQLException{
		 Connection conn = null;
		 PreparedStatement ps = null;	
		 ResultSet rs = null;
		 ArrayList<StockRec> list = new ArrayList<StockRec>();
		 try{
			 conn = getConnect();
			 String query = "SELECT * FROM stock";
			 ps = conn.prepareStatement(query);
			 rs = ps.executeQuery();
			 while(rs.next()){
				 list.add(new StockRec(rs.getString(1), 
						 			   rs.getFloat(2)));
			 }
		 }finally{
			 closeAll(rs, ps, conn);
		 }
		 return list;
	 }
	 
	 public float getStcokPrice(String symbol)throws SQLException,RecordNotFoundException{
		 Connection conn = null;
		 PreparedStatement ps = null;	
		 ResultSet rs = null;
		 float price = 0.0f;
		 
		 try{
			 conn = getConnect();
			 String query = "SELECT price FROM stock WHERE symbol=?";
			 ps = conn.prepareStatement(query);
			 ps.setString(1, symbol);
			 rs = ps.executeQuery();
			 if(rs.next()) price = rs.getFloat(1);
			 else throw new RecordNotFoundException("���� �ֽ��Դϴ�..");
		 }finally{
			 closeAll(rs, ps, conn);
		 }
		 return price;
	 }
	 
	
	 //���� � �ֽ��� � ��ų�...
	 //������ �ֳľ��ĸ� ���� �˾ƺ���.
	 //������...update / ������...insert
	 public void buyShares(String ssn, String symbol, int quantity)
	 				throws SQLException{
		 Connection conn = null;
		 PreparedStatement ps = null;	
		 ResultSet rs = null;
		 try{
			 conn = getConnect();
			 String query = "SELECT quantity FROM shares WHERE ssn=? AND symbol=?";
			 ps = conn.prepareStatement(query);
			 ps.setString(1, ssn);
			 ps.setString(2, symbol);
			 rs = ps.executeQuery();
			 if(rs.next()){ //������ �ֽ��� ������ ���� �ֵ�.
				 int q = rs.getInt(1); //������ �ִ� �ֽ��� ����
				 int newQuantity = q+quantity; //update��
				 String query1 = "UPDATE shares SET quantity=? WHERE ssn=? AND symbol=?";
				 ps = conn.prepareStatement(query1);
				 ps.setInt(1, newQuantity);
				 ps.setString(2, ssn);
				 ps.setString(3, symbol);
				 System.out.println(ps.executeUpdate()+" row buyShares()....ok");
			 }else{ //�ֽ��� ����
				 String query2 = "INSERT INTO shares VALUES(?,?,?)";
				 ps = conn.prepareStatement(query2);
				 ps.setString(1, ssn);
				 ps.setString(2, symbol);
				 ps.setInt(3, quantity);
				 System.out.println(ps.executeUpdate()+" row buyShares()...insert ok");
			 }
		 }finally{
			 closeAll(rs, ps, conn);
		 }
				 
	 }
	//���� � �ֽ��� � �Ȱų�...��� ���� ������ �ִ���...quantity
	 /*
	  * 100�� ������ �ִ�(���� �����ϰ� �ִ� �ֽ��� ����)
	  * 1) 100�� �ȾҴٸ�-----delete
	  * 2) 200�� �ȾҴٸ�----InvalidTransactionE~~
	  * 3) 20�� �ȾҴٸ� ---- update
	  */
	 public void sellShares(String ssn, String symbol, int quantity)
			 throws SQLException,RecordNotFoundException,InvalidTransactionException{
		 Connection conn = null;
		 PreparedStatement ps = null;	
		 ResultSet rs = null;
		 try{
			 conn=  getConnect();
			 if(!isExist(conn, ssn)){
				 throw new RecordNotFoundException("�ֽ��� �ȷ��� ����� ���..");
			 }else{
				 String query = "SELECT quantity FROM shares WHERE ssn=? AND symbol=?";
				 ps = conn.prepareStatement(query);
				 ps.setString(1, ssn);
				 ps.setString(2, symbol);
				 rs = ps.executeQuery();
				 rs.next(); //quantity �κ��� �ϴ� ����Ű�� �Ѵ�.
				 int q = rs.getInt(1); //100�� ������ �ִ�.
				 int newQuantity=q - quantity;//�Ȱ� ���� �ֽ��� ����..update
				 if(q==quantity){
					 //delete
					 String query1 = "DELETE FROM shares WHERE ssn=? AND symbol=?";
					 ps = conn.prepareStatement(query1);
					 ps.setString(1, ssn);
					 ps.setString(2, symbol);
					 System.out.println(ps.executeUpdate()+" row shares DELETE...1.");
				 }else if(q>quantity){
					 //update
					 String query2 = "UPDATE shares SET quantity=? WHERE ssn=? AND symbol=?";
					 ps = conn.prepareStatement(query2);
					 ps.setInt(1, newQuantity);
					 ps.setString(2, ssn);
					 ps.setString(3, symbol);
					 System.out.println(ps.executeUpdate()+" row shares UPDATE..2");
				 }else{ //q<quantity�� ���..
					 //����
					 throw new InvalidTransactionException("�ȷ��� �ֽ��� ������ �� ���ƿ�..");
				 }
			 }
		 }finally{
			 closeAll(ps, conn);			 
		 }
	 }
	 
	 public void updateStockPrice(String symbol, float price) throws SQLException{
		 Connection conn=  null;
		 PreparedStatement ps = null;
		 try{
			 conn=  getConnect();
			 String query = "UPDATE stock SET price=? WHERE symbol=?";
			 ps = conn.prepareStatement(query);
			 ps.setFloat(1, price);
			 ps.setString(2, symbol);
			 System.out.println(ps.executeUpdate()+" row updateStockPrice()..ok");
		 }finally{
			 closeAll(ps, conn);
		 }
		 
	 }
}//class


















