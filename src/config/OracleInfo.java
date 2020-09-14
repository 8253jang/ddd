package config;
/*
 * Driver 의 FQCN
 * DB Server의 URL
 * DB Server User Name
 * DB Server Password ::
 * 모든 값들은 스트링 형태이다...주의 깊게 봐두자
 */
public interface OracleInfo {
	//무조건 앞에 public static final이 생략되어져 있다.
	String DRIVER = "oracle.jdbc.driver.OracleDriver";
	String URL = "jdbc:oracle:thin:@127.0.0.1:1521:xe";
	String USER = "scott";
	String PASS = "tiger";	
}














