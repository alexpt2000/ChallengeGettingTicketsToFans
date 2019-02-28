package Viagogo;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		World world = new World();
		Scanner scan = new Scanner(System.in);
		System.out.println("Please enter your x coordinate (between -10 and 10)");
		int x = scan.nextInt();
		while (true) {
			try {
				if (x < -10 || x > 10) {
					Exception e = new Exception();
					throw e;
				} else {
					break;
				}
			} catch (Exception e) {
				System.out.println("Please enter a valid x coordinate");
				x = scan.nextInt();
			}
		}

		System.out.println("Please enter your y coordinate");
		int y = scan.nextInt();
		while (true) {
			try {
				if (y < -10 || y > 10) {
					Exception e = new Exception();
					throw e;
				} else {
					break;
				}
			} catch (Exception e) {
				System.out.println("Please enter a valid y coordinate");
				y = scan.nextInt();
			}
		}

		scan.close();

		System.out.println("Based on your location, here are the 5 closest events, with the cheapest ticket price: ");

		ArrayList<Event> nearestEvents = world.findNearestEvents(x, y);

		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);

		for (Event event : nearestEvents) {
			Ticket cheapestTicket = event.getCheapestTicket();
			String dfCheapestTicket = df.format(cheapestTicket.getPrice());
			System.out.println(
					"- Event " + event.getID() + ", " + event.getDistance() + " km(s) away, €" + dfCheapestTicket);
		}

	}

}

class Event {
	private int eventID;
	private int distanceToUser;
	private int[] location;
	private Ticket[] tickets;

	public Event(int eventID, Ticket[] tickets) {
		this.eventID = eventID;
		this.tickets = tickets;
	}

	public Ticket getCheapestTicket() {

		double currentTicketPrice = tickets[0].getPrice();
		int currentIndex = 0;
		for (int i = 1; i < tickets.length; i++) {
			double newTicketPrice = tickets[i].getPrice();
			if (newTicketPrice < currentTicketPrice) {
				currentTicketPrice = newTicketPrice;
				currentIndex = i;
			}
		}
		return tickets[currentIndex];
	}

	public int[] getLocation() {
		return location;
	}

	public void setLocation(int[] location) {
		this.location = location;
	}

	public int getDistance() {
		return distanceToUser;
	}

	public void setDistance(int distanceToUser) {
		this.distanceToUser = distanceToUser;
	}

	public int getID() {
		return eventID;
	}

	public Ticket[] getTickets() {
		return tickets;
	}
}

/*
 * Ticket
 */
class Ticket {
	private double ticketPrice;

	public Ticket(double ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public double getPrice() {
		return ticketPrice;
	}
}

/*
 * World
 */
class World {

	private ArrayList<int[]> coordinates;
	private ArrayList<Event> events;

	public World() {
		generateWorld();
		generateEvents();
	}

	private ArrayList<int[]> generateWorld() {
		coordinates = new ArrayList<int[]>();

		for (int x = -10; x <= 10; x++) {
			for (int y = -10; y <= 10; y++) {
				int[] coordinate = { x, y };
				coordinates.add(coordinate);
			}
		}

		return coordinates;
	}

	private void generateEvents() {
		int numberOfEvents = (int) (Math.random() * 395) + 5;
		System.out.println(numberOfEvents);

		events = new ArrayList<>();

		for (int i = 1; i <= numberOfEvents; i++) {
			int numberOfTickets = (int) (Math.random() * 5);
			if (i < 6) {
				numberOfTickets = (int) (Math.random() * 5) + 1;
			}

			Ticket[] tickets = generateTickets(numberOfTickets);

			Event event = new Event(i, tickets);
			events.add(event);
		}

		assignLocations(events);
	}

	private Ticket[] generateTickets(int numberOfTickets) {
		Ticket[] tickets = new Ticket[numberOfTickets];
		for (int i = 0; i < tickets.length; i++) {
			double ticketPrice = (Math.random() * 149) + 1;
			Ticket ticket = new Ticket(ticketPrice);
			tickets[i] = ticket;
		}
		return tickets;
	}

	private void assignLocations(ArrayList<Event> events) {
		for (int i = 0; i < events.size(); i++) {
			ArrayList<int[]> coordinates = this.coordinates;
			int locationIndex = (int) (Math.random() * coordinates.size());
			events.get(i).setLocation(coordinates.remove(locationIndex));
		}
	}

	public ArrayList<Event> findNearestEvents(int x, int y) {
		calculateDistances(x, y);
		ArrayList<Event> nearestEvents = new ArrayList<>();
		while (nearestEvents.size() < 5) {
			int currentDistance = events.get(0).getDistance();
			int currentIndex = 0;

			for (int j = 1; j < events.size(); j++) {
				int newDistance = events.get(j).getDistance();
				if (newDistance < currentDistance) {
					currentDistance = newDistance;
					currentIndex = j;
				}
			}
			if (events.get(currentIndex).getTickets().length > 0) {
				nearestEvents.add(events.remove(currentIndex));
			} else {
				events.remove(currentIndex);
			}
		}

		return nearestEvents;
	}

	private void calculateDistances(int x1, int y1) {
		for (Event event : events) {
			int x2 = event.getLocation()[0];
			int y2 = event.getLocation()[1];

			int distance = Math.abs(x1 - x2) + Math.abs(y1 - y2);
			event.setDistance(distance);
		}
	}

	public ArrayList<int[]> getCoordinates() {
		return coordinates;
	}
}