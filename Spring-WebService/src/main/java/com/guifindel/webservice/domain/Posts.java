package com.guifindel.webservice.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class Posts extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 500, nullable = false)
	private String title;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;

	private String author;
	
	@Builder
	public Posts(String title, String content, String author) {
		this.title = title;
		this.content = content;
		this.author = author;
	}

	/*
	 * @Entity 테이블과 링크될 클래스임을 나타낸다. 언더스코어 네이밍(_)으로 이름을 매칭한다. ex) MemberManager.java
	 * -> member_manager table
	 * 
	 * @Id 해당 테이블의 PK 필드를 나타낸다.
	 * 
	 * @GeneratedValue PK의 생성 규칙을 나타낸다. 기본값은 AUTO이며, MySQL의 auto_increment와 같이 자동
	 * 증가하는 정수형 값이 된다. 스프링 부트 2.0에선 옵션을 추가해야만 auto_increment가 된다.
	 * 
	 * @Column 테이블의 컬럼을 나타내면, 굳이 선언하지 않더라도 해당 클래스의 필드는 모두 컬럼이 된다. 사용하는 이유는, 기본값 외에
	 * 추가로 변경이 필요한 옵션이 있을경우 사용한다. 문자열의 경우 VARCHAR(255)가 기본값이며, 사이즈를 500으로 늘리거
	 * 싶거나(ex: title), 타입을 TEXT로 변경하고 싶거나 (ex : content) 등의 경우에 사용된다.
	 * 
	 * Tip) 웬만하면 Entity의 PK는 Long 타입의 Auto_increment가 추천된다. (MySQL 기준으로 이렇게 작성하면
	 * bigint 타입이 된다.) 주민등록번호와 같은 비즈니스상 유니크키나, 여러 키를 조합한 복합 키로 PK를 잡을 경우 난감한 상황이 종종
	 * 발생한다. 1) FK를 맺을 때 다른 테이블에서 복합키 전부를 갖고 잇거나, 중간 테이블을 하나 더 둬야하는 상황이 발생 2) 인덱스에
	 * 좋은 영향을 끼치지 못한다. 3) 유니크한 조건이 변경될 경우 PK 전체를 수정해야하는 일이 발생 주민등록번호, 복합키 등은 유니크 키로
	 * 별도 추가하는 것이 추천된다.
	 * 
	 * Lombok 라이브러리의 어노테이션들
	 * 
	 * @NoArgsConstructor : 기본 생성자 자동 추가 -access = AccessLevel.PROTECTED : 기본 생성자의
	 * 접근 권한을 protected로 제한 생성자로 protected Posts() {}와 같은 효과 Entity 클래스를 프로젝트 코드상에서
	 * 기본 생성자로 생성하는 것은 막되, JPA에서 Entity 클래스를 생성하는 것은 허용하기 위해 추가
	 * 
	 * @Getter : 클래스 내 모든 필드의 Getter 메소드를 자동 생성
	 * 
	 * @Builder : 해당 클래스의 빌더 패턴 클래스를 생성 -생성자 상단에 선언시 생성자에 포함된 필드만 빌더에 포함
	 * 
	 * 서비스 구축단계에선 테이블 설계(여기선 Entity 설계)가 빈번하게 변경되는데, 이 때 Lombok의 어노테이션들은 코드 변경량을 최소화
	 * 시켜주기 때문에 강력 추천되는 라이브러리이다.
	 * 
	 * Entity 클래스를 생성할 때, 주의해야할 것은 무분별한 setter 메소드 생성이다. 자바빈 규약을 상정하며 getter/setter을
	 * 무작정 생성한다면 해당 클래스의 인스턴스 값들이 언제 어디서 변해야 하는지 코드상으로 명확히 구분할 수가 없어, 차후 기능 변경시
	 * 복잡해진다. 해당 필드의 값 변경이 필요하면 명확히 그 목적과 의도를 나타낼 수 있는 메소드를 추가해야 한다.
	 * 
	 * 잘못된 사용 
	 * public class Order{ 
	 * 	public void setStatus(boolean status){
	 *  	this.status = status 
	 * 	} 
	 * }
	 * 
	 * public void 주문서비스의_취소메소드 (){
	 *  order.setStatus(false); 
	 *  }
	 * 
	 * 
	 * 올바른 사용
	 * public class Order{
	 *  public void cancelOrder(){
	 *   this.status = false; 
	 *   }
	 * } 
	 * public void 주문서비스의_취소메소드 (){
	 *  order.cancelOrder(); 
	 *  }
	 *  
	 *  그렇다면 기본 생성자도 AccessLevel.PROTECTED로 막아놓고, setter 메소드도 없는 이 상황에서 어떻게 값을 채워
	 *  DB에 insert해야 할까?
	 *  
	 *  기본적인 구조는 생성자를 통해 최종 값을 채운 후 DB에 insert 하는 것이며, 값 변경이 필요한 경우 해당 이벤트에 맞는
	 *  public 메소드를 호출하여 변경하는 것을 전제로 한다.
	 *  여기서 생성자 대신에 @Builder를 통해 제공되는 빌더 클래스를 사용한다.
	 *  생성자나 빌더나 생성시점에 값을 채워주는 역할은 똑같다.
	 *  다만, 생성자의 경우 지금 채워야할 필드가 무엇인지 명확히 지정할 수가 없다.
	 *  
	 *  public Example(String a, String b){
	 *  	this.a = a;
	 *  	this.b = b;
	 *  }
	 *  
	 *  개발자가 new Example(b,a) 처럼 a와 b의 위치를 변경 해도 실제로 코드를 실행하기 전까지는 전혀 문제를 찾을 수가 없다.
	 *  하지만 빌더를 사용하게 되면 아래와 같이
	 *  
	 *  Example.builder()
	 *  	.a(a)
	 *  	.b(b)
	 *  	.build();
	 *  
	 *  어떤 필드에 어떤 값을 채워야 할지 명확하게 인지할 수 있다.
	 */
}
