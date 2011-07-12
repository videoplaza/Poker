package com.videoplaza.poker.bot;

public class Bet {

	private int value = 0;
	private String chatMessage;
	
	public Bet() {}

	public Bet(int value, String chatMessage) {
		this.value = value;
		this.chatMessage = chatMessage;
	}
	
	public Bet(String chatMessage) {
		this.chatMessage = chatMessage;
	}

	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getChatMessage() {
		return chatMessage;
	}
	public void setChatMessage(String chatMessage) {
		this.chatMessage = chatMessage;
	}
	
}
