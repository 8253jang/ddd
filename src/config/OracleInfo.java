package config;
/*
 * Driver �� FQCN
 * DB Server�� URL
 * DB Server User Name
 * DB Server Password ::
 * ��� ������ ��Ʈ�� �����̴�...���� ��� ������
 */
public interface OracleInfo {
	//������ �տ� public static final�� �����Ǿ��� �ִ�.
	String DRIVER = "oracle.jdbc.driver.OracleDriver";
	String URL = "jdbc:oracle:thin:@127.0.0.1:1521:xe";
	String USER = "scott";
	String PASS = "tiger";	
}














