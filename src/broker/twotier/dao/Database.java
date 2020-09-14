package broker.twotier.dao;
/*
 * Exception ::
 * DuplicateSSNException,
 * RecordNotFoundException,
 * InvalidTransactionException
 * : 팔려는 주식의 숫자가 가지고 있는것 보다 더 많을떄
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
 * DB에 Access하는 비지니스 로직을 담고 있는 클래스
 * UseCase Diagram을 보고서 기능의 선언부를 뽑아내겠다.
 */
public class Database {
	 public Database(String server)throws ClassNotFoundException{
		 Class.forName(OracleInfo.DRIVER);
		 System.out.println("드라이버 로딩 성공...");
	 }
	 
	 //////// 공통적인 로직 /////////////////////
	 public Connection getConnect() throws SQLException{
		Connection conn = 
				DriverManager.getConnection(OracleInfo.URL, OracleInfo.USER, OracleInfo.PASS);
		System.out.println("디비 연결 성공...getConnect()...");
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
	 
	 //////////////// 비지니스 로직 ////////////////////////////////////
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
				System.out.println(ps.executeUpdate()+" 명 insert success....addCustomer()");
			}else{
				throw new DuplicateSSNException("그런 사람 이미 있어여...");
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
				 System.out.println(ps.executeUpdate()+"명 delete success...deleteCustomer()");
			 }else{
				 throw new RecordNotFoundException("삭제할 사람 없어여..");
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
			 
			 if(row==1) System.out.println(row+" 명 update success...");
			 else throw new RecordNotFoundException("수정할 대상이 없어여..");
		 }finally{
			 closeAll(ps, conn);
		 }
	 }
	 /*
	  * 고객이 보유한 주식 정보(shares)....
	  * 한명의 고객이 여러개의 주식종복을 보유할수 있기 때문에...Vector에 담았다.
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
		 return v; //while문 바깥에서...
	 }
	 /*
	  * 순수 고객에 대한 정보(customer) + 고객이 보유한 주식 정보(shares)....
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
			 else throw new RecordNotFoundException("없는 주식입니다..");
		 }finally{
			 closeAll(rs, ps, conn);
		 }
		 return price;
	 }
	 
	
	 //누가 어떤 주식을 몇개 살거냐...
	 //가지고 있냐없냐를 먼저 알아본다.
	 //있으면...update / 없으면...insert
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
			 if(rs.next()){ //기존의 주식을 얼마정도 갖고 있따.
				 int q = rs.getInt(1); //가지고 있는 주식의 수량
				 int newQuantity = q+quantity; //update문
				 String query1 = "UPDATE shares SET quantity=? WHERE ssn=? AND symbol=?";
				 ps = conn.prepareStatement(query1);
				 ps.setInt(1, newQuantity);
				 ps.setString(2, ssn);
				 ps.setString(3, symbol);
				 System.out.println(ps.executeUpdate()+" row buyShares()....ok");
			 }else{ //주식이 없다
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
	//누가 어떤 주식을 몇개 팔거냐...몇개를 현재 가지고 있는지...quantity
	 /*
	  * 100개 가지고 있다(현재 보유하고 있는 주식의 수량)
	  * 1) 100개 팔았다면-----delete
	  * 2) 200개 팔았다면----InvalidTransactionE~~
	  * 3) 20개 팔았다면 ---- update
	  */
	 public void sellShares(String ssn, String symbol, int quantity)
			 throws SQLException,RecordNotFoundException,InvalidTransactionException{
		 Connection conn = null;
		 PreparedStatement ps = null;	
		 ResultSet rs = null;
		 try{
			 conn=  getConnect();
			 if(!isExist(conn, ssn)){
				 throw new RecordNotFoundException("주식을 팔려는 사람이 없어여..");
			 }else{
				 String query = "SELECT quantity FROM shares WHERE ssn=? AND symbol=?";
				 ps = conn.prepareStatement(query);
				 ps.setString(1, ssn);
				 ps.setString(2, symbol);
				 rs = ps.executeQuery();
				 rs.next(); //quantity 부분을 일단 가리키게 한다.
				 int q = rs.getInt(1); //100개 가지고 있다.
				 int newQuantity=q - quantity;//팔고 남은 주식의 수량..update
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
				 }else{ //q<quantity인 경우..
					 //예외
					 throw new InvalidTransactionException("팔려는 주식의 수량이 넘 많아여..");
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


















