package it.polito.tdp.flight.model;

public class PasseggeriNegliAeroporti implements Comparable<PasseggeriNegliAeroporti> {
	private Airport airport;
	private int k;
	public PasseggeriNegliAeroporti(Airport airport, int k) {
		super();
		this.airport = airport;
		this.k = k;
	}
	public Airport getAirport() {
		return airport;
	}
	public void setAirport(Airport airport) {
		this.airport = airport;
	}
	public int getK() {
		return k;
	}
	public void setK(int k) {
		this.k = k;
	}
	@Override
	public int compareTo(PasseggeriNegliAeroporti o) {
		
		return -(this.k-o.getK());
	}
	@Override
	public String toString() {
		return "[airport=" + airport + ", k=" + k + "]";
	}
	

}
