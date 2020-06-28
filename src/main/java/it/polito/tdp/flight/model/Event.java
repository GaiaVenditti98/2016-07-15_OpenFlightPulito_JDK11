package it.polito.tdp.flight.model;

import java.time.LocalTime;

public class Event implements Comparable<Event> {
	public enum EventType{
		PasseggeroParte,
		PasseggeroArriva,
		PartenzaVoli
	}
	
	private Airport airport;
	private LocalTime time;
	private EventType type;
	@Override
	public String toString() {
		return "Event [airport=" + airport + ", time=" + time + ", type=" + type + "]";
	}
	public Airport getAirport() {
		return airport;
	}
	public void setAirport(Airport airport) {
		this.airport = airport;
	}
	public LocalTime getTime() {
		return time;
	}
	public void setTime(LocalTime time) {
		this.time = time;
	}
	public EventType getType() {
		return type;
	}
	public void setType(EventType type) {
		this.type = type;
	}
	public Event(Airport airport, LocalTime time, EventType type) {
		super();
		this.airport = airport;
		this.time = time;
		this.type = type;
	}
	@Override
	public int compareTo(Event o) {
		
		return this.time.compareTo(o.time);
	}

	

}
