package database.chat;

public class OpenChatDTO {
	int roomID;
	int chatID;
	String fromID;
	String chatContent;
	String chatTime;
	int read1;
	int read2;
	int read3;
	int read4;
	int read5;
	String chatTable;
	public int getRoomID() {
		return roomID;
	}
	public void setRoomID(int roomID) {
		this.roomID = roomID;
	}
	public int getChatID() {
		return chatID;
	}
	public void setChatID(int chatID) {
		this.chatID = chatID;
	}
	public String getFromID() {
		return fromID;
	}
	public void setFromID(String fromID) {
		this.fromID = fromID;
	}
	public String getChatContent() {
		return chatContent;
	}
	public void setChatContent(String chatContent) {
		this.chatContent = chatContent;
	}
	public String getChatTime() {
		return chatTime;
	}
	public void setChatTime(String chatTime) {
		this.chatTime = chatTime;
	}
	public int getRead1() {
		return read1;
	}
	public void setRead1(int read1) {
		this.read1 = read1;
	}
	public int getRead2() {
		return read2;
	}
	public void setRead2(int read2) {
		this.read2 = read2;
	}
	public int getRead3() {
		return read3;
	}
	public void setRead3(int read3) {
		this.read3 = read3;
	}
	public int getRead4() {
		return read4;
	}
	public void setRead4(int read4) {
		this.read4 = read4;
	}
	public int getRead5() {
		return read5;
	}
	public void setRead5(int read5) {
		this.read5 = read5;
	}
	public String getChatTable() {
		return chatTable;
	}
	public void setChatTable(String chatTable) {
		this.chatTable = chatTable;
	}
	
}
