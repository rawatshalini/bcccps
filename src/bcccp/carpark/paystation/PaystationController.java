package bcccp.carpark.paystation;

import bcccp.carpark.ICarpark;
import bcccp.tickets.adhoc.IAdhocTicket;
import java.util.Date;

public class PaystationController 
		implements IPaystationController {
	
	private IPaystationUI ui;	
	private ICarpark carpark;

	private IAdhocTicket  adhocTicket = null;
	private float charge;
	
	
	public PaystationController(ICarpark carpark, IPaystationUI ui) {
		this.carpark = carpark;
		this.ui = ui;
	}



	@Override
	public void ticketInserted(String barcode) {
		//retrieve ticket by reading barcode
		this.adhocTicket = carpark.getAdhocTicket(barcode);
		
		// get date and time of car entry
		long entryDateTime = adhocTicket.getEntryDateTime();
		
		// calculate charge for the duration of car parking
		this.charge = carpark.calculateAddHocTicketCharge(entryDateTime);
		
		// display the amount for the user
		this.ui.display("Amount to be paid for car parking : " + charge);
	}



	@Override
	public void ticketPaid() {
		Date dateTime = new Date();
		this.adhocTicket.pay(dateTime.getTime(), this.charge);
		
		String carparkId = this.adhocTicket.getCarparkId();
		int ticketNo = this.adhocTicket.getTicketNo();
		long entryDateTime = this.adhocTicket.getEntryDateTime();
		long paidDateTime = this.adhocTicket.getPaidDateTime();
		this.ui.printTicket(carparkId, ticketNo, entryDateTime, paidDateTime, this.charge, this.adhocTicket.getBarcode());
		
		this.ui.display("Please collect ticket");
	}



	@Override
	public void ticketTaken() {
		// TODO Auto-generated method stub
		
	}

	
	
}
