/**
 * Copyright (c) 2011, SOCIETIES Consortium (WATERFORD INSTITUTE OF TECHNOLOGY (TSSG), HERIOT-WATT UNIVERSITY (HWU), SOLUTA.NET 
 * (SN), GERMAN AEROSPACE CENTRE (Deutsches Zentrum fuer Luft- und Raumfahrt e.V.) (DLR), Zavod za varnostne tehnologije
 * informacijske družbe in elektronsko poslovanje (SETCCE), INSTITUTE OF COMMUNICATION AND COMPUTER SYSTEMS (ICCS), LAKE
 * COMMUNICATIONS (LAKE), INTEL PERFORMANCE LEARNING SOLUTIONS LTD (INTEL), PORTUGAL TELECOM INOVAÇÃO, SA (PTIN), IBM Corp., 
 * INSTITUT TELECOM (ITSUD), AMITEC DIACHYTI EFYIA PLIROFORIKI KAI EPIKINONIES ETERIA PERIORISMENIS EFTHINIS (AMITEC), TELECOM 
 * ITALIA S.p.a.(TI),  TRIALOG (TRIALOG), Stiftelsen SINTEF (SINTEF), NEC EUROPE LTD (NEC))
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following
 * conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 *    disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.societies.thirdparty.enterprise.sharedCalendar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.societies.api.comm.xmpp.interfaces.ICommManager;
import org.societies.api.ext3p.schema.sharedcalendar.Calendar;
import org.societies.api.ext3p.schema.sharedcalendar.Event;
import org.societies.api.ext3p.schema.sharedcalendar.SharedCalendarResult;
import org.societies.api.identity.IIdentity;
import org.societies.api.identity.InvalidFormatException;
import org.societies.api.useragent.monitoring.IUserActionMonitor;
import org.societies.thirdparty.sharedCalendar.api.ICalendarResultCallback;
import org.societies.thirdparty.sharedCalendar.api.ISharedCalendarClient;
import org.societies.thirdparty.sharedCalendar.api.UserWarning;
import org.societies.thirdparty.sharedCalendar.api.XMLGregorianCalendarConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * This is the client-class for the Shared Calendar
 *
 * @author <a href="mailto:sanchocsa@gmail.com">Sancho Rêgo</a> (PTIN)
 *
 */
public class SharedCalendarClient implements ISharedCalendarClient {

	static final Logger log = LoggerFactory.getLogger(SharedCalendarClient.class);
	private SharedCalendar sharedCalendar;
	private ICommManager commManager;
	private IUserActionMonitor userAction;
	private IIdentity myId;
	
	public IUserActionMonitor getUserAction() {
		return userAction;
	}

	public void setUserAction(IUserActionMonitor userAction) {
		this.userAction = userAction;
	}

	public SharedCalendar getSharedCalendar(){
		return this.sharedCalendar;
	}
	
	public void setSharedCalendar(SharedCalendar sharedCalendar){
		this.sharedCalendar = sharedCalendar;
	}
	
	public ICommManager getCommManager() {
		return commManager;
	}

	public void setCommManager(ICommManager commManager) {
		this.commManager = commManager;
	}
	
	public SharedCalendarClient() {
		log.info("Shared Calendar Client!");	
	}
	
	public void init() throws Exception{
		
		String myJid = getCommManager().getIdManager().getThisNetworkNode().getJid();
		
		if(log.isDebugEnabled())
			log.debug("Shared Calendar Client is running on JID: " + myJid);
		
		try {
			myId = getCommManager().getIdManager().fromJid(myJid);
		} catch (InvalidFormatException e) {
			log.error("Exception while starting the SharedCalendarClient!");
			e.printStackTrace();
		}	
	}

	@Async
	@Override
	public void retrieveCalendar(ICalendarResultCallback calendarResultCallback, String nodeId) {

		if(log.isDebugEnabled())
			log.debug("Retrieving Calendars for node: " + nodeId);
		
		try{
			IIdentity node = getCommManager().getIdManager().fromJid(nodeId);

			Calendar myCalendar = getSharedCalendar().retrieveCalendar(node, myId);
		
			SharedCalendarResult result = new SharedCalendarResult();
			result.setCalendar(myCalendar);
			
			if(log.isDebugEnabled())
				log.debug("Sending the result back: " + result.getCalendar());
			
			calendarResultCallback.receiveResult(result);

		} catch(Exception ex){
			ex.printStackTrace();
		}
		
	}

	@Async
	@Override
	public void retrieveEvents(ICalendarResultCallback calendarResultCallback, String nodeId) {
		
		if(log.isDebugEnabled())
			log.debug("Retrieving Events for a node: " + nodeId);
		try{
			
			IIdentity node = getCommManager().getIdManager().fromJid(nodeId);

			List<Event> eventList = getSharedCalendar().retrieveEvents(node, myId);
		
			if(log.isDebugEnabled())
				log.debug("Got event list: " + eventList.size());
		
			SharedCalendarResult result = new SharedCalendarResult();
			result.setEventList(eventList);
			
			calendarResultCallback.receiveResult(result);
						
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}


	@Async
	@Override
	public void deleteEvent(
			ICalendarResultCallback calendarResultCallback, String eventId, String nodeId) {

		if(log.isDebugEnabled())
			log.debug("Deleting event on calendar!");
		
		try{
			
			IIdentity node = getCommManager().getIdManager().fromJid(nodeId);
	
			Boolean deleteOk = getSharedCalendar().deleteEvent(eventId, node, myId);
			
			if(log.isDebugEnabled())
				log.debug("Deleted an event with Id: " + deleteOk);
			
			SharedCalendarResult result = new SharedCalendarResult();
			result.setLastOperationSuccessful(deleteOk);
			
			calendarResultCallback.receiveResult(result);
			
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}


	@Async
	@Override
	public void subscribeToEvent(ICalendarResultCallback calendarResultCallback, String eventId, String nodeId, String subscriberId) {

		
		if(log.isDebugEnabled())
			log.debug("subscribing to an event...");
		
		try{
			
			IIdentity node = getCommManager().getIdManager().fromJid(nodeId);
			IIdentity subscriber = getCommManager().getIdManager().fromJid(subscriberId);
			
			Boolean subscribeOk = getSharedCalendar().subscribeToEvent(eventId, node, subscriber);
			
			SharedCalendarResult finalResult = new SharedCalendarResult();
			finalResult.setSubscribingResult(subscribeOk);
			finalResult.setLastOperationSuccessful(subscribeOk);
			calendarResultCallback.receiveResult(finalResult);
			
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}



	@Async
	@Override
	public void unsubscribeFromEvent(
			ICalendarResultCallback calendarResultCallback, String eventId, String subscriberId) {

		if(log.isDebugEnabled())
			log.debug("unsubscribing to an event...");
		
		try{
			
			IIdentity subscriber = getCommManager().getIdManager().fromJid(subscriberId);
			
			Boolean subscribeOk = getSharedCalendar().unsubscribeFromEvent(eventId, subscriber);
			
			SharedCalendarResult finalResult = new SharedCalendarResult();
			finalResult.setSubscribingResult(subscribeOk);
			finalResult.setLastOperationSuccessful(subscribeOk);
			calendarResultCallback.receiveResult(finalResult);		
			
		} catch(Exception ex){
			ex.printStackTrace();
		}

	}


	/* (non-Javadoc)
	 * @see org.societies.thirdparty.sharedCalendar.api.ISharedCalendarClient#viewEvent(org.societies.thirdparty.sharedCalendar.api.ICalendarResultCallback, java.lang.String)
	 */
	@Override
	@Async
	public void viewEvent(ICalendarResultCallback calendarResultCallback,
			String eventId, String nodeId) {
		
		if(log.isDebugEnabled())
			log.debug("View Event: " + eventId);
		try{
		IIdentity node = getIIdentityFromJid(nodeId);
		
		Event receivedEvent = getSharedCalendar().retrieveEvent(eventId, node, myId);
		
		SharedCalendarResult returnResult = new SharedCalendarResult();
		returnResult.setEvent(receivedEvent);
		calendarResultCallback.receiveResult(returnResult);
		
		}catch(Exception ex){
			log.error("Exception occured!: " + ex);
			ex.printStackTrace();
		}
		
	}

	/* (non-Javadoc)
	 * @see org.societies.thirdparty.sharedCalendar.api.ISharedCalendarClient#createEvent(org.societies.thirdparty.sharedCalendar.api.ICalendarResultCallback, org.societies.api.ext3p.schema.sharedcalendar.Event, java.lang.String)
	 */
	@Override
	@Async
	public void createEvent(ICalendarResultCallback calendarResultCallback,
			Event newEvent, String nodeId) {
		
		if(log.isDebugEnabled())
			log.debug("Creating event on Calendar!");
		
		try{
			IIdentity node = getCommManager().getIdManager().fromJid(nodeId);
			String eventId = getSharedCalendar().createEvent(newEvent, node, myId);
			
			if(log.isDebugEnabled())
				log.debug("EventId: " + eventId);
			
			SharedCalendarResult finalResult = new SharedCalendarResult();
			finalResult.setEventId(eventId);

			if(eventId != null){
				Event theEvent = getSharedCalendar().retrieveEvent(eventId, node, myId);
				finalResult.setEvent(theEvent);
				finalResult.setLastOperationSuccessful(true);
			}
			else
				finalResult.setLastOperationSuccessful(false);
			
			calendarResultCallback.receiveResult(finalResult);	
			
		} catch(Exception ex){
			log.error("Exception occured!: " + ex);
			ex.printStackTrace();		}
		
	}

	@Override
	@Async
	public void updateEvent(ICalendarResultCallback calendarResultCallback,
			Event updatedEvent) {
		
		if(log.isDebugEnabled())
			log.debug("Updating Event!");
		
		Boolean updateResult = getSharedCalendar().updateEvent(updatedEvent, myId);
		SharedCalendarResult finalResult = new SharedCalendarResult();

		finalResult.setLastOperationSuccessful(updateResult);
		
		calendarResultCallback.receiveResult(finalResult);
	}

	@Override
	@Async
	public void findEventsInCalendar(
			ICalendarResultCallback calendarResultCallback, String nodeId,
			Event searchEvent) {
		// TODO Auto-generated method stub
		
	}


	@Override
	@Async
	public void findEventsAll(ICalendarResultCallback calendarResultCallback,
			Event searchEvent) {
		// TODO Auto-generated method stub
		
	}


	@Override
	@Async
	public void getRecommendedEvents(ICalendarResultCallback calendarResultCallback, String subscriberId) {

		if(log.isDebugEnabled())
			log.debug("Getting Recommended Events!");
		List<Event> recommendedEvents = getSharedCalendar().getMyRecommendedEvents();
		
		SharedCalendarResult result = new SharedCalendarResult();
		result.setEventList(recommendedEvents);
		
		if(log.isDebugEnabled())
			log.debug("Now returning the list of recommended events.");
		
		calendarResultCallback.receiveResult(result);
		
	}
	
	//This method converts a jid into an IIdentity
	private IIdentity getIIdentityFromJid(String jid) throws InvalidFormatException{		
		return getCommManager().getIdManager().fromJid(jid);
	}


	@Override
	public List<UserWarning> getUserWarnings() {
		if(log.isDebugEnabled())
			log.debug("Get User Warnings!");
		
		List<UserWarning> userWarnings = getSharedCalendar().getUserWarnings();
		
		if(log.isDebugEnabled()){
			for(UserWarning userWarning : userWarnings){
				log.debug("User Warnings: " + userWarning.getTitle() +" : " + userWarning.getDetail());
			}
		}
		
		return userWarnings;
		
	}
	

	@Async
	@Override
	public void getAllCalendars(ICalendarResultCallback calendarResultCallback){
		if(log.isDebugEnabled())
			log.debug("Getting all Calendars");
		
		List<Calendar> calendarList = getSharedCalendar().getAllCalendars();

		SharedCalendarResult result = new SharedCalendarResult();
		result.setCalendarList(calendarList);
				
		if(log.isDebugEnabled())
			log.debug("Now returning the list of calendars.");
		
		calendarResultCallback.receiveResult(result);
	}
}