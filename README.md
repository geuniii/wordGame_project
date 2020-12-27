# WORD GENIUS

**영어 단어 암기를 게임을 통해 흥미롭고 효율적으로 학습할 수 있는 단어 맞추기 게임이다.**

**최고 기록을 갱신하며 타 사용자들과의 경쟁을 통해 학습 의욕을 상승시킬 수 있다.**    

![frames](https://user-images.githubusercontent.com/62981623/103170293-9f474e00-4886-11eb-8d77-46c378ff1b6f.jpg)  



## 개발환경
JDK 15.0.1  
IDE : Eclipse  
DB : mysql  Ver 8.0.20, MariaDB 10.5.8  <br/><br/><br/>



## 주요 기능
- 로그인, 회원가입
- DB에 있는 단어 랜덤 추출, 그래픽화
- 단어 좌/우 랜덤하게 이동
- 단어 생성, 이동 속도 조절
- 단어 뜻 정답 체크
- 상위 점수 아이디 랭킹 나열
- 사용자 랭킹 조회  <br/><br/><br/>

## 작품 영상  
<div>
	<a href=https://youtu.be/IIp5B48cUWA"><image src ="https://user-images.githubusercontent.com/62981623/103178660-15b87000-48c8-11eb-9093-7aa73548d1f8.jpg"></a>

</div>


## 작품의 특장점
### 1. 멀티 스레딩으로 다양한 기능을 병렬적, 경제적 수행  
   
   진행된 정도 표시 스레드 / 정답 속도 타이머 스레드 /단어 생성 스레드 / 그래픽 repaint 스레드
     ![스레드 구성](https://user-images.githubusercontent.com/62981623/103170305-ad956a00-4886-11eb-8139-efd6b53cdfe1.jpg)<br/><br/>
     

### 2. 더블 버퍼링으로 그래픽의 움직임을 부드럽게 표현   

   보이지 않는 화면을 하나 추가하여 버퍼 역할을 해주어 단어의 움직임을 끊김 없이 표현한다.  
    
```java
   Image offScreenImage = getParent().createImage(getSize().width, getSize().height);
   Graphics offScreen = offScreenImage.getGraphics();
```
<br/><br/>  

### 3. 관계형 데이터베이스 활용을 통해 순위 조회, 기록 갱신  

 
   - 레벨 별로 총 인원과, 사용자의 순위 조회로 랭킹을 보여준다.  
   
```java
   String sql = "SELECT ID, rank() over(order by max_score DESC) from maxscore where level=?";
```   
<br/>

   - 게임이 끝난 후 점수가 사용자의 최고 기록을 넘을 시 점수를 갱신한다.  
   
```java
   public String maxScoreCheck(String id, int level, int score) {

		String sql = "SELECT max_score FROM maxscore WHERE ID =? AND level=?";
		ResultSet rs = null;
		PreparedStatement p = null;
		Connection conn = null;
		String returnMsg = null;
		try {
			p = (conn = getConnection()).prepareStatement(sql);			
			p.setString(1, id);
			p.setInt(2, level);
			rs = p.executeQuery();
			
			if (rs.next()) {				
				if (score >rs.getInt(1)) {
					updateMaxScore(score,id, level);
					returnMsg = "나의 최고 기록 갱신 성공!^__^";
					return returnMsg;
				}
				returnMsg =  "나의 최고 기록 갱신 실패!T_T";
				return returnMsg;
			}			
			else {
				insertMaxScore(id, level, score);
				returnMsg =  "나의 최초 기록 갱신 성공!^__^";
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		close(conn, p, rs);
		return returnMsg;
	}
```  

<br/><br/><br/>

## 문제점 및 해결방안 


### 1. 스윙 이벤트 스레드의 중단 문제  

게임 동작 화면에서 스윙 관련 여러 스레드가 동시에 하나의 스윙 컴포넌트에 접근해서 데이터를 조작하다보니 충돌 상황이 일어나 모든 스레드가 중단되는 어려움이 있었다.  

--> invokeLater()을 사용하여 이벤트 처리 쓰레드로 이벤트들을 큐에 넣어 진행하고 있는 작업과 분리해서 실행시켜 해결하였다.  

<br/><br/>


### 2. 단어와 뜻이 1 : n 관계일 경우 다중 정답 처리 문제  

사용자가 적은 답을 정답과 비교하려면 1:1 관계가 되어야 하는데, 다양한 답이 정답으로 처리될 수 있어야 하므로 어려움을 겪었다.  
<br/>
**- 첫번째 시도**
```sql
String sql = "SELECT word FROM word WHERE meaning LIKE '%?%'"; 
```
DB에 meaning을 여러가지 넣었을 때, 사용자가 입력한 값이 있으면 정답으로 간주하려 했으나, 한 글자만 맞아도 정답이 되는 문제가 생겨 실패하였다.   

<br/>

**- 두번째 시도**    

하나의 단어를 뜻을 다르게 여러개 DB에 저장하여 해결하였다. 먼저 사용자가 입력한 뜻을 가지고 있는 단어가 DB에 있는지 탐색하고, 그래픽에 뿌려지는 단어 중 해당 단어와 뜻을 가진 wordItem객체가 있나 탐색하여 정답 처리하여 해결하였다.  

<br/><br/>  

### **3. BGM을 넣은 AudioStream이 새 Frame 생성시마다 여러번 호출되는 문제**  

모든 Frame class는 InitFrame class를 상속받아 일괄적으로 초기화된다. 하나의 음원을 프로그램 실행시 연속하여 반복재생하기 위해  
InitFrme에서 객체를 생성했지만, 상속받은 Frame들이 생성될 때마다 Audio Clip도 생성되는 문제가 생겼다.  

--> 기존 Frame을 닫고 새로운 Frame을 생성할 때마다 clip.stop();을 통해 잠시 멈춘 후 멈춘 곳에서 다시 재생하게 하여 해결하였다.  

<br/><br/><br/>


## 개선방안 

### - Socket 통신을 통해 대결 기능 추가  

TCP/IP Socket 통신으로 1:1 대결 기능을 추가하고자 하였다. 문자열만 주고 받는 통신을 해서 WordItem 객체를 통신하는 방법이 필요하다. Client에서 문자열을 주면 Server단에서 WordItem객체를 생성하도록 로직을 바꾸거나, WordItem 객체를 주고 받는 방법이 있다면 개선할 수 있다.  
<br/><br/>

### - 틀린 단어 단어장 추가  

단어의 Y좌표 위치가 그래픽을 구현한 Panel의 높이를 벗어나면 HashMap에서 삭제하게 구현하였다. 이렇게 단순히 삭제되는 단어들을 DB에 vacabulary Table을 따로 생성하여 저장한다면 사용자만의 단어장을 만들어 오답 노트로 사용할 수 있도록 개선한다면 단어 암기에 더욱 도움되는 프로그램이 될 것이다.  
<br/><br/>

### - 단어끼리의 충돌시 그래픽 개선  

단어가 랜덤하게 움직이는 동안 x좌표가 그래픽 Panel의 너비와 닿으면 반대 방향으로 움직이게 구현하였다. Panel의 너비뿐 만 아니라, 단어마다 폭을 구하여 부딪치면 반대 방향으로 움직이게 구현한다면 랜덤하게 움직이는 동안 단어들이 겹치는 현상을 개선할 수 있다.  
<br/><br/><br/>

### Javadoc 
[Javadoc](https://geuniii.github.io/wordGame_project/WordGameProject/doc/index.html)



