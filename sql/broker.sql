create table customer
       (ssn varchar2(50) primary key,
       cust_name varchar2(50) not null,
       address varchar2(100) not null ) ; 
 
create table stock
       (symbol varchar2(20) primary key,
       price number(10,2) not null) ; 
 
create table shares
       (ssn varchar2(30) not null,
       symbol varchar2(20) not null,
       quantity number(10) not null) ; 
 
insert into stock ( symbol,price) values( 'SUNW', 68.75);
insert into stock ( symbol,price) values( 'CyAs', 22.675);
insert into stock ( symbol,price) values( 'DUKE', 6.25);
insert into stock ( symbol,price) values( 'ABStk', 18.5);
insert into stock ( symbol,price) values( 'JSVco', 9.125);
insert into stock ( symbol,price) values( 'TMAs', 82.375);
insert into stock ( symbol,price) values( 'BWInc', 11.375);
insert into stock ( symbol,price) values( 'GMEnt', 44.625);
insert into stock ( symbol,price) values( 'PMLtd', 203.375);
insert into stock ( symbol,price) values( 'JDK', 33.5);
insert into customer values( '111-111', 'Yufirst1', 'Seoul');
insert into customer values( '111-112', 'Yufirst2', 'Seoul');
insert into customer values( '111-113', 'Yufirst3', 'Seoul');
insert into customer values( '111-114', 'Yufirst4', 'Seoul');
insert into customer values( '111-115', 'yufirst5', 'JeonJu');
insert into customer values( '111-116', 'Yufirst6', 'Seoul');
insert into customer values( '111-117', 'Yufirst7', 'Seoul');
insert into customer values( '111-118', 'Yufirst8', 'Seoul');
insert into customer values( '111-119', 'Yufirst9', 'Seoul');
commit;



선생님..위에서 작성한 테이블은 외래키 제약조건이 없어서
1. stock 테이블에서 삭제한 주식이 shares 테이블에서는 삭제되지 않는 문제점이 있어요
또 한가지, 
/*
 * 자식을 둔 부모레코드는 삭제될수 없다
 * 1) 자식을 먼저 죽이고 부모가 죽는 방법 ::
 *    ON DELETE CASCADE
 * 2) 부모가 삭제될때 자식을 null로 만들고 부모가 삭제되는 방법 ::
 *    ON DELETE SET NULL
 */

 2. 이런 성질 때문에 자식을 둔 부모 테이블은 삭제가 되지 않는 문제점도 발생됩니다.

===> 결론은 아래처럼 외래키 제약조건과 함께 on delete cascade 제약조건을 함꼐 줘야 합니다.
     위에서 테이블 생성 + 값 입력하고 난뒤, 
     아래의 제약조건을 변경해 주세염

/////////// Shares table에 Foreign key 추가 방법 ////////////////
//////////// 동시에 on delete cascade 사용하는 방법////////////////////////////////

ALTER TABLE shares ADD CONSTRAINT fk_shares_ssn foreign key(ssn) references customer(ssn) ON DELETE CASCADE;
ALTER TABLE shares ADD CONSTRAINT fk_shares_symbol foreign key(symbol) references stock(symbol) ON DELETE CASCADE;






