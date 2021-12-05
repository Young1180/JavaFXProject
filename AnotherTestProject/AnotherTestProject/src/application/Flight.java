package application;

import javafx.beans.property.SimpleStringProperty;

public class Flight {
	final int id;
	final SimpleStringProperty startCity;
	final SimpleStringProperty endCity;
	final SimpleStringProperty departuretime;
	final SimpleStringProperty arrivalTime;
	final String totalSeats;
	final SimpleStringProperty date;
	
	public Flight(
		int id,
		String startCity,
		String endCity,
		String departuretime,
		String arrivalTime,
		String totalSeats,
		String date
	) {
		this.id = id;
		this.startCity = new SimpleStringProperty(startCity);
		this.endCity = new SimpleStringProperty(endCity);
		this.departuretime = new SimpleStringProperty(departuretime);
		this.arrivalTime = new SimpleStringProperty(arrivalTime);
		this.totalSeats = totalSeats;
		this.date = new SimpleStringProperty(date);
	}
	
	public String getStartCity() {
		return startCity.get();
	}
	
	public String getEndCity() {
		return endCity.get();
	}
	
	public String getDeparturetime() {
		return departuretime.get();
	}
	
	public String getArrivalTime() {
		return arrivalTime.get();
	}
	
	public String getDate() {
		return date.get();
	}
	
}



// after initial login 