import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class main {
	private static DecimalFormat df2 = new DecimalFormat(".##");
	private static Scanner sc;

	public static void main(String[] args) {
		List<Event> events = new ArrayList<>();
		List<Point> buyers = new ArrayList<>();
		sc = new Scanner(System.in);
		System.out.println("Plese enter how many events are to be included");
		int numberOfEvents = Integer.parseInt(sc.nextLine());

		while (numberOfEvents > 0) {
			System.out.println("Please enter details for event " + (numberOfEvents));
			String eventLine = sc.nextLine();
			String[] eventDetails = eventLine.split(" ");
			Event event = new Event(eventDetails);
			events.add(event);
			numberOfEvents--;
		}

		System.out.println("Please enter number of buyers");
		int numberofBuyers = Integer.parseInt(sc.nextLine());

		while (numberofBuyers > 0) {
			System.out.println("Please enter details for user " + (numberofBuyers - (numberofBuyers - 1)));
			String buyerLine = sc.nextLine();
			String[] buyerDetails = buyerLine.split(" ");
			Point point = new Point(Integer.parseInt(buyerDetails[0]), Integer.parseInt(buyerDetails[1]));
			buyers.add(point);
			numberofBuyers--;
		}

		for (int i = 0; i < buyers.size(); i++) {
			int shortestDistance = getManhattanDistance(buyers.get(i), events.get(0).getPoint());
			int shortestEvent = 0;
			for (int j = 0; j < events.size(); j++) {
				int currentDistance = getManhattanDistance(buyers.get(i), events.get(0).getPoint());
				if (currentDistance < shortestDistance) {
					shortestDistance = currentDistance;
					shortestEvent = j;
				}
			}
			System.out.println("For buyer " + i);
			System.out.println("Shortest event is " + events.get(shortestEvent).getEventID());
			if (events.get(shortestEvent).getTickets().size() > 0) {
				double currentPrice = events.get(i).getTickets().get(0);
				int cheapest = 0;
				for (int k = 1; k < events.get(i).getTickets().size(); k++) {
					double newPrice = events.get(i).getTickets().get(k);
					if (newPrice < currentPrice) {
						cheapest = k;
					}
				}
				System.out.println("Cheapest ticket " + df2.format(events.get(i).getTickets().get(cheapest)));
				events.get(i).getTickets().remove(cheapest);
			} else {
				System.out.println("No tickets available");
			}
		}
	}

	private static int getManhattanDistance(final Point eventPoint, final Point buyerPoint) {
		int distance = Math.abs(buyerPoint.getX() - eventPoint.getX()) + Math.abs(buyerPoint.getY() - eventPoint.getY());
		return distance;
	}
}


/*
 * Point
 */
class Point {
	private int x;
	private int y;

	public Point(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}

/*
 * Event
 */

class Event {
	private int eventID;
	private ArrayList<Integer> tickets;
	private Point point;

	public Event(String[] eventDetails) {
		setEventID(Integer.parseInt(eventDetails[0]));
		Point point = new Point(Integer.parseInt(eventDetails[1]), Integer.parseInt(eventDetails[2]));
		setPoint(point);

		this.tickets = new ArrayList<>();
		for (int i = 3; i < eventDetails.length; i++) {
			tickets.add(Integer.parseInt(eventDetails[i]));
		}
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public int getEventID() {
		return eventID;
	}

	public void setEventID(int eventID) {
		this.eventID = eventID;
	}

	public ArrayList<Integer> getTickets() {
		return tickets;
	}
}