package com.dl.pojo;

public class OrderGuest {

	private String Name;
	private String RoomPos;
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getRoomPos() {
		return RoomPos;
	}
	public void setRoomPos(String roomPos) {
		RoomPos = roomPos;
	}
	@Override
	public String toString() {
		return "OrderGuest [Name=" + Name + ", RoomPos=" + RoomPos + "]";
	}
	
}
