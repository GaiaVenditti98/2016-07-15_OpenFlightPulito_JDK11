package it.polito.tdp.flight.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.flight.db.FlightDAO;


public class Model {
	private FlightDAO dao;
	private Graph<Airport, DefaultWeightedEdge> grafo;
	private Map<Integer, Airport> idMap;
	private Simulator sim;

	public Model() {
		this.dao = new FlightDAO();
		this.sim=new Simulator();
	}

	public void creaGrafo(Integer km) {
		this.grafo = new SimpleDirectedWeightedGraph<Airport, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.idMap = new HashMap<>();
		this.dao.getAllAirports(idMap);
		Graphs.addAllVertices(this.grafo, idMap.values());

		for (Adiacenza a : this.dao.getAdiacenze()) {
			Double distanza = calcolaDistanza(idMap.get(a.getId1()), idMap.get(a.getId2()));

			if (distanza < km && a.getId1() != a.getId2()) {
				Double tempo = distanza / 800;// tempo in ore

				Graphs.addEdgeWithVertices(this.grafo, idMap.get(a.getId1()), idMap.get(a.getId2()), tempo);
			}
		}
		System.out.println(this.grafo);
	}

	private Double calcolaDistanza(Airport airport1, Airport airport2) {
		return LatLngTool.distance(new LatLng(airport1.getLatitude(), airport1.getLongitude()),
				new LatLng(airport2.getLatitude(), airport2.getLongitude()), LengthUnit.KILOMETER);

	}
	
	public boolean isConnect() {
		ConnectivityInspector<Airport, DefaultWeightedEdge> c = new ConnectivityInspector<>(this.grafo);
		return c.isConnected();
	}
	
	public Airport piuLontano() {
		Airport fiumicino=null;
		for(Airport a:idMap.values()) 
			if(a.getName().equals("Fiumicino"))
				fiumicino=a;
		Airport risultato=null;
		List<Airport> vicini=Graphs.neighborListOf(this.grafo,fiumicino);
		Double max=0.0;
		for(Airport a:vicini) {
			DefaultWeightedEdge e=this.grafo.getEdge(a, fiumicino);
			if(this.grafo.getEdgeWeight(e) > max) {
				max=this.grafo.getEdgeWeight(e);
				risultato=a;
			}
		}
		return risultato;
			
		
	}
	
	public int numVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int numArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<PasseggeriNegliAeroporti> simula(Integer k) {
		this.sim.init(grafo, k);
		this.sim.run();
		List<PasseggeriNegliAeroporti>passeggeri=new ArrayList<PasseggeriNegliAeroporti>();
		for(Airport a:this.sim.getMappaPasseggeri().keySet()) {
			if(this.sim.getMappaPasseggeri().get(a) > 0)
			passeggeri.add(new PasseggeriNegliAeroporti(a,this.sim.getMappaPasseggeri().get(a) ));
			
		}
		
		Collections.sort(passeggeri);
		return passeggeri;
	}

}
