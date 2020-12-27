package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.print.DocFlavor.READER;
import javax.swing.JOptionPane;

import panel.RankPanel;
import value.WordItem;
import view.Home;
import view.Login;

public class Dao { 
	/**
	 * Data Access Object
	 */
	private static final String URL = "jdbc:mysql://127.0.0.1/wordproject";
	private static final String ID = "root";
	private static final String PW = "root";


	/**
	 * Singleton
	 */
	private Dao() {
	}

	
	/**
	 * Dao instance
	 */
	private static Dao instance;

	public static Dao getInstance() {
		if (instance == null)
			instance = new Dao();
		return instance;
	}

	
	/**
	 * DB Connection 생성
	 * @return {@link java.sql.Connection} or printStackTrace of {@link java.sql.SQLException}
	 */
	private Connection getConnection() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(URL, ID, PW);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	
	/**
	 * Connection, PreparedStatement, ResultSet Close
	 * @param conn  close할 {@link java.sql.Connection}
	 * @param ps  close할 {@link PreparedStatement}
	 * @param rs  close할 {@link ResultSet}
	 */
	private void close(Connection conn, PreparedStatement ps, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Connection, PreparedStatement Close
	 * @param conn  close할 {@link java.sql.Connection}
	 * @param ps  close할 {@link PreparedStatement}
	 */
	private void close(Connection conn, PreparedStatement ps) {
		this.close(conn, ps, null);
	}
	

	/**
	 * Implement login function
	 * 
	 * idField, pwField can't be empty.
	 * Passwords must match.
	 *  
	 * @param id  ID to log in
	 * @param pw  PW to log in
	 * @return login success->true / fail ->false
	 */
	public Boolean login(String id, String pw) {

		Connection conn = null;
		PreparedStatement p = null;
		ResultSet rs = null;

		String sql = "SELECT id, pw FROM user WHERE id=?";

		try {
			p = (conn = getConnection()).prepareStatement(sql);
			p.setString(1, id);
			rs = p.executeQuery();

			if (id.equals("")) {
				JOptionPane.showMessageDialog(null, "ID를 입력하세요!");
				return false;
			}
		
			if (pw.equals("")) {
				JOptionPane.showMessageDialog(null, "PW를 입력하세요!");
				return false;
			}
		
			if (rs.next()) {
				
				if (!(rs.getString(2)).equals(pw)) {

					JOptionPane.showMessageDialog(null, "패스워드가 일치하지 않습니다!");

					return false;
				}
				
				JOptionPane.showMessageDialog(null, "로그인 성공!");
				return true;
			}

			JOptionPane.showMessageDialog(null, "미등록 아이디입니다!");

		} catch (SQLException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "SQL 쿼리문 오류");
		} catch (NullPointerException e2) {
			e2.printStackTrace();

		}
		close(conn, p, rs);
		return null;
	}

	
	/**
	 * Check ID duplication when signing up
	 * @param id  ID to sign up
	 * @return result message
	 * idField is empty /the ID is available/ the ID is already registered
	 */
	public String idCheck(String id) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet rs = null;

		String sql = "SELECT id FROM user WHERE id=?";

		try {
			p = (conn = getConnection()).prepareStatement(sql);
			
			if (id.equals("")) {
				JOptionPane.showMessageDialog(null, "ID를 입력하세요!");
				return null;
			}
			p.setString(1, id);
			rs = p.executeQuery();

			
			if (!rs.next()) {
				return "사용가능한 아이디입니다";
			}
		
			JOptionPane.showMessageDialog(null, "이미 등록된 ID 입니다!");

		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (NullPointerException e2) {
			e2.printStackTrace();
		}
		close(conn, p, rs);
		return null;
	}

	////////////////register-user table에 저장///////////////////////
	
	/**
	 * Insert new user's data in user table
	 * @param id  ID to sign up
	 * @param pw  PW to sign up
	 * @return join success->true / fail ->false
	 */
	public boolean join(String id, String pw) {

		Connection conn = null;
		PreparedStatement p = null;
		ResultSet rs = null;

		String sql = "INSERT INTO user (id,pw) VALUES(?,?)";

		try {
			p = (conn = getConnection()).prepareStatement(sql);
			p.setString(1, id);
			p.setString(2, pw);
			p.execute();
			JOptionPane.showMessageDialog(null, "회원가입 성공!");
			return true;

		} catch (SQLException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "SQL 쿼리문 오류");
		} catch (NullPointerException e2) {
			e2.printStackTrace();
		}
		close(conn, p, rs);
		return false;
	}
	
	/**
	 * Compare the score of this game with the user's highest score.
	 * @param id  ID to search the highest score
	 * @param level  Level of this game 
	 * @param score  Score for this game
	 * @return message of renew user's highest score or not  
	 */
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
				returnMsg =  "나의 최초 기록 저장 성공!^__^";
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		close(conn, p, rs);
		return returnMsg;
	}


	
	/**
	 * Update the user's highest score
	 * @param id ID to update the highest score
	 * @param level Level of this game 
	 */
	public void updateMaxScore(int score,String id, int level) {

		String sql = "UPDATE maxscore SET max_score = ? WHERE ID = ? AND level = ?";

		Connection conn = null;
		PreparedStatement p = null;
		try {
			p = (conn = getConnection()).prepareStatement(sql);
			p.setInt(1,score);
			p.setString(2, id);
			p.setInt(3, level);
			p.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		close(conn, p);
		return;
	}


	/**
	 * Insert the user's the first highest score
	 * @param id  ID to insert the highest score
	 * @param level  Level of this game
	 * @param maxScore  Score of this game
	 */
	public void insertMaxScore(String id, int level, int maxScore) {
		String sql = "INSERT INTO maxscore(ID,level,max_score) VALUES(?,?,?);";
		Connection conn = null;
		PreparedStatement p = null;
		try {
			p = (conn = getConnection()).prepareStatement(sql);
			p.setString(1, id);
			p.setInt(2, level);
			p.setInt(3, maxScore);

			p.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		close(conn, p);
		return;
	}

	/**
	 * Count id Stored in the 'maxscore' table by level
	 * @return array of count id results
	 */
	public String[] userCount() {

		Connection conn = null;
		PreparedStatement p = null;
		ResultSet rs = null;
		
		String sql = "SELECT COUNT(ID) from maxscore where level=?";

		String[] countArr = new String[RankPanel.getRanknum()];

		for (int i = 0; i < RankPanel.getRanknum(); i++) {
			int searchLevel = i + 1;

			try {
				p = (conn = getConnection()).prepareStatement(sql);
				p.setInt(1, searchLevel);
				rs = p.executeQuery();
				if (rs.next()) {
					countArr[i] = String.valueOf(rs.getInt(1));
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		close(conn, p, rs);
		return countArr;
	}

 
	/**
	 * Search user's ranking
	 * @param id  ID to search the ranking
	 * @return  StringBuffer containing user's ranking by level
	 */
	public StringBuffer myRankSearch(String id, int rankNum) {

		Connection conn = null;
		PreparedStatement p = null;
		ResultSet rs = null;

		String sql = "SELECT ID, rank() over(order by max_score DESC)" + " from maxscore where level=?";

		StringBuffer buf = new StringBuffer("<YOUR RANK>\n");
		
		String[] countArr = userCount();
		String[] levelArr = new String[rankNum];
		String[] rankArr = new String[rankNum];

		for (int i = 0; i <rankNum; i++) {
			int searchLevel = i + 1;
			levelArr[i] = String.valueOf(searchLevel);
			try {
				p = (conn = getConnection()).prepareStatement(sql);
				p.setInt(1, searchLevel);
				rs = p.executeQuery();
				while (rs.next()) {					
					if (rs.getString(1).equals(id)) {						
						rankArr[i] = String.valueOf(rs.getString(2));
						break;
					} else {
						rankArr[i] = "X ";
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			buf.append("level" + levelArr[i] + ":   " + rankArr[i] + "등 / " + countArr[i] + "명 \n");
		}
		close(conn, p, rs);
		return buf;
	}

	
	
	/**
	 * Extract the word to be used in the game and save it to hashMap
	 * @param count  Number of words to create
	 * @param level  Level of this game
	 * @return HashMaps with words
	 */
	public HashMap<Integer, WordItem> wordMaker(int count, int level) {

		Connection conn = null;
		PreparedStatement p = null;
		ResultSet rs = null;

		HashMap<Integer, WordItem> wordMap = new HashMap<>();

		int saveRand = 1;
		int index = 0;

		String sql = "SELECT word,meaning FROM word WHERE level=?";
		loop: while (true) {
			try {
				p = (conn = getConnection()).prepareStatement(sql);
				p.setInt(1, level);
				rs = p.executeQuery();

				while (rs.next()) {
					saveRand = (int) ((Math.random() * 2) + 1);
					if (saveRand == 2) {
						wordMap.put(index++, new WordItem(rs.getString(1), rs.getString(2)));
					}
					if (index == count) {
						break loop;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
				JOptionPane.showInputDialog("SQL 쿼리문 오류");
			}

		}
		close(conn, p, rs);
		return wordMap;
	}

	
	/**
	 * Check the correct answer
	 * @param answer  User-written answer
	 * @param map  HashMaps with words
	 * @return Correct or Incorrect Messages
	 */
	public String answerCheck(String answer, HashMap<Integer, WordItem> map) {

		Connection conn = null;
		PreparedStatement p = null;
		ResultSet rs = null;
		String sql = "SELECT word FROM word WHERE meaning = ?";
		String word = null;
		try {
			p = (conn = getConnection()).prepareStatement(sql);
			p.setString(1, answer);
			rs = p.executeQuery();

			if (rs.next()) {
				word = rs.getString(1);
				
				Iterator<Map.Entry<Integer, WordItem>> iter = map.entrySet().iterator();
				
				while (iter.hasNext()) {
					Map.Entry<Integer, WordItem> entry = iter.next();
					if (entry.getValue().getWord().contains(word)) {						
						iter.remove();						
						return "CORRECT";
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		close(conn, p, rs);
		return "WRONG";
	}


	/**
	 * List IDs by rank
	 * @param level  Level of this game
	 * @param rankNum  Number of Rankings
	 * @return Array of ranking IDs
	 */
	public String[] rankList(int level, int rankNum) {
		Connection conn = null;
		PreparedStatement p = null;
		ResultSet rs = null;
		String[] userId = new String[rankNum];
		String sql = "SELECT ID FROM maxscore WHERE level = ? ORDER BY max_score DESC";
		try {
			p = (conn = getConnection()).prepareStatement(sql);
			p.setInt(1, level);
			rs = p.executeQuery();

			for (int i = 0; i < rankNum; i++) {
				if (rs.next()) {
					userId[i] = rs.getString(1);
				} else {
					userId[i] = "";
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		close(conn, p, rs);
		return userId;

	}

}
