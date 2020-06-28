package it.polito.tdp.flight.model;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.event.VertexSetListener;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.flight.model.Event.EventType;

public class Simulator {
	// coda degli eventi
	private PriorityQueue<Event> queue;

	// parametri iniziali
	int k;
	Graph<Airport, DefaultWeightedEdge> grafo;

	// stato del mondo
	Map<Airport, Integer> mappaPasseggeri;

	private final LocalTime oraInizio = LocalTime.of(6, 00);
	private final LocalTime oraFine = oraInizio.plus(24, ChronoUnit.HOURS);

	public void init(Graph<Airport, DefaultWeightedEdge> grafo, Integer k) {
		this.queue=new PriorityQueue<Event>();
		this.grafo = grafo;
		this.k = k;

		List<Airport> aeroporti = new ArrayList<>();

		mappaPasseggeri = new HashMap<>();
		for (Airport a : this.grafo.vertexSet()) {
			this.mappaPasseggeri.put(a, 0);
			aeroporti.add(a);
		}

		// distribuisco casualmente i k passeggeri tra gli aeroporti
		while (k != 0) {
			Random random = new Random();
			int index = random.nextInt(aeroporti.size());
			Airport casuale = aeroporti.get(index);
			//System.out.println(casuale);
			mappaPasseggeri.put(casuale, 1);

			k--;

		}

		// genero eventi partenza voli dalle 7:00 alle 23:00 ogni due ore

		LocalTime oraFinePartenze = LocalTime.of(23, 00);
		for (Airport a : aeroporti) {
			LocalTime oraInizioPartenze = LocalTime.of(7, 00);
			do {
				Event e = new Event(a, oraInizioPartenze, EventType.PartenzaVoli);
				//System.out.println(e);
				this.queue.add(e);
				oraInizioPartenze = oraInizioPartenze.plus(2, ChronoUnit.HOURS);

			} while (oraInizioPartenze.isBefore(oraFinePartenze));
			this.queue.add(new Event(a, oraFinePartenze, EventType.PartenzaVoli));
			
			oraFinePartenze=oraFinePartenze.plus(24,ChronoUnit.HOURS);
			
			oraInizioPartenze=oraInizioPartenze.plus(8,ChronoUnit.HOURS);
			
			do {
				Event e = new Event(a, oraInizioPartenze, EventType.PartenzaVoli);
				this.queue.add(e);
				//System.out.println(e);
				oraInizioPartenze = oraInizioPartenze.plus(2, ChronoUnit.HOURS);

			} while (oraInizioPartenze.isBefore(oraFinePartenze));
			this.queue.add(new Event(a, oraFinePartenze, EventType.PartenzaVoli));
		}

	}

	public void run() {
		while (!this.queue.isEmpty()) {
			Event e = this.queue.poll();// poll estrae l'elemento in testa e lo elimina
			System.out.println(e);
			processEvent(e);

		}
	}

	private void processEvent(Event e) {
		switch (e.getType()) {

		case PartenzaVoli:
			if (mappaPasseggeri.get(e.getAirport()) > 0)
				for (int i = 0; i < mappaPasseggeri.get(e.getAirport()); i++)
					this.queue.add(new Event(e.getAirport(), e.getTime(), EventType.PasseggeroParte));

			break;

		case PasseggeroParte:
			List<Airport> vicini = new ArrayList<Airport>();
			for(DefaultWeightedEdge def: this.grafo.outgoingEdgesOf(e.getAirport()))
				vicini.add(this.grafo.getEdgeTarget(def));
			
		int index = (int)(Math.random()*vicini.size());
			
			if (vicini.size() != 0) {
				Airport casuale = vicini.get(index);
			
				long tempoArrivo = (long) this.grafo.getEdgeWeight(this.grafo.getEdge(e.getAirport(), casuale));
				this.queue.add(
						new Event(casuale, e.getTime().plus(tempoArrivo, ChronoUnit.HOURS), EventType.PasseggeroArriva));
				mappaPasseggeri.put(e.getAirport(), mappaPasseggeri.get(e.getAirport()) - 1);
			}

			break;

		case PasseggeroArriva:
			mappaPasseggeri.put(e.getAirport(), mappaPasseggeri.get(e.getAirport()) + 1);

		}

	}

	public Map<Airport, Integer> getMappaPasseggeri() {
		return mappaPasseggeri;
	}

	public void setMappaPasseggeri(Map<Airport, Integer> mappaPasseggeri) {
		this.mappaPasseggeri = mappaPasseggeri;
	}

}
