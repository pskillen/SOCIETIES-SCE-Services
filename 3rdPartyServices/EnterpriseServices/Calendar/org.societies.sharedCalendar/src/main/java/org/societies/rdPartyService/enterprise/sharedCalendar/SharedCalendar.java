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
package org.societies.rdPartyService.enterprise.sharedCalendar;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.societies.api.schema.cssmanagement.CssRecord;
import org.societies.rdPartyService.enterprise.sharedCalendar.persistence.DAO.CSSCalendarDAO;
import org.societies.rdPartyService.enterprise.sharedCalendar.privateCalendarUtil.IPrivateCalendarUtil;
import org.societies.rdpartyservice.enterprise.sharedcalendar.Calendar;
import org.societies.rdpartyservice.enterprise.sharedcalendar.Event;

import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.EventAttendee;

/**
 * Describe your class here...
 * 
 * @author solutanet
 * 
 */
public class SharedCalendar implements ISharedCalendar, IPrivateCalendarUtil {
	private SharedCalendarUtil util;
	private SessionFactory sessionFactory;
	private static Logger log = LoggerFactory.getLogger(SharedCalendar.class);

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SharedCalendarUtil getUtil() {
		return util;
	}

	public void setUtil(SharedCalendarUtil util) {
		this.util = util;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.societies.rdPartyService.enterprise.sharedCalendar.ISharedCalendar
	 * #retrieveCalendarList()
	 */
	@Override
	public List<Calendar> retrieveCalendarList() {
		List<Calendar> returnedCalendarList = new ArrayList<Calendar>();
		try {
			returnedCalendarList = calendarListFromCalendarEntry(util
					.retrieveAllCalendar());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		return returnedCalendarList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.societies.rdPartyService.enterprise.sharedCalendar.ISharedCalendar
	 * #retrieveCalendarEvents(java.lang.String)
	 */
	@Override
	public List<Event> retrieveCalendarEvents(String calendarId) {
		List<Event> returnedEventList = new ArrayList<Event>();
		try {
			returnedEventList = eventListFromGoogleEventList(util
					.retrieveAllEvents(calendarId));
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return returnedEventList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.societies.rdPartyService.enterprise.sharedCalendar.ISharedCalendar
	 * #subscribeToEvent(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean subscribeToEvent(String calendarId, String eventId,
			String subscriberId) {
		boolean returnedValue = false;
		try {
			com.google.api.services.calendar.model.Event event = util
					.findEventUsingId(calendarId, eventId);
			/*
			 * Check if event has attendees
			 */
			if (event.getAttendees() != null) {
				event.getAttendees().add(createEventAttendee(subscriberId));
			} else {
				List<EventAttendee> attendeesList = new ArrayList<EventAttendee>();
				attendeesList.add(createEventAttendee(subscriberId));
				event.setAttendees(attendeesList);
			}

			util.updateEvent(calendarId, event);
			returnedValue = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		return returnedValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.societies.rdPartyService.enterprise.sharedCalendar.ISharedCalendar
	 * #findEvents(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Event> findEvents(String calendarId, String keyWord) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.societies.rdPartyService.enterprise.sharedCalendar.ISharedCalendar
	 * #unsubscribeFromEvent(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public boolean unsubscribeFromEvent(String calendarId, String eventId,
			String subscriberId) {
		boolean returnedValue = false;
		try {
			com.google.api.services.calendar.model.Event event = util
					.findEventUsingId(calendarId, eventId);

			List<EventAttendee> tmpAttendeeList = event.getAttendees();

			tmpAttendeeList.remove(createEventAttendee(subscriberId));

			event.setAttendees(tmpAttendeeList);
			util.updateEvent(calendarId, event);
			returnedValue = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		return returnedValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.societies.rdPartyService.enterprise.sharedCalendar.ISharedCalendar
	 * #createPrivateCalendar(java.lang.String) THIS METHOD HAS NO
	 * IMPLEMENTATION. USE THE createPrivateCalendarUsingCSSId TO STORE THE
	 * MAPPING BETWEEN CSSID AND CALENDARID
	 */
	@Override
	public boolean createPrivateCalendar(String calendarSummary) {

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.societies.rdPartyService.enterprise.sharedCalendar.privateCalendarUtil
	 * .IPrivateCalendarUtil#createPrivateCalendarUsingCSSId(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public boolean createPrivateCalendarUsingCSSId(String CSSId,
			String calendarSummary) {
		String storedCalendarId = null;
		Transaction t = null;
		Session session = null;
		boolean result=false;
		
		try {
			session = sessionFactory.openSession();
			CSSCalendarDAO template = new CSSCalendarDAO();
			template.setCSSId(CSSId);
			List<CSSCalendarDAO> results = session
					.createCriteria(CSSCalendarDAO.class)
					.add(Example.create(template)).list();
			if (results.size() < 0) {
			
			storedCalendarId = util.createCalendar(calendarSummary);
			CSSCalendarDAO cssCalendarDAO = new CSSCalendarDAO(CSSId,
					storedCalendarId);
			
			t = session.beginTransaction();
			session.save(cssCalendarDAO);
			t.commit();
			result=true;
			}else{
				log.info("The CSS already has a calendar.");
			}
		} catch (HibernateException he) {
			log.error(he.getMessage());
			if (t != null) {
				t.rollback();
				try {
					util.deleteCalendar(storedCalendarId);
				} catch (IOException e) {

					log.error(e.getMessage());
				}
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {

			if (session != null) {
				session.close();
			}

		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.societies.rdPartyService.enterprise.sharedCalendar.privateCalendarUtil
	 * .IPrivateCalendarUtil#deletePrivateCalendarUsingCSSId(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public boolean deletePrivateCalendarUsingCSSId(String CSSId) {
		boolean result = false;
		String storedCalendarId = null;
		Session session = null;
		Transaction t = null;
		try {
			session = sessionFactory.openSession();
			CSSCalendarDAO template = new CSSCalendarDAO();
			template.setCSSId(CSSId);
			List<CSSCalendarDAO> results = session
					.createCriteria(CSSCalendarDAO.class)
					.add(Example.create(template)).list();
			if (results.size() != 0) {
				CSSCalendarDAO tmpCSSCalendarDAO = (CSSCalendarDAO) session
						.get(CSSCalendarDAO.class, results.get(0).getId());
				util.deleteCalendar(results.get(0).getCalendarId());
				t = session.beginTransaction();

				session.delete(tmpCSSCalendarDAO);
				t.commit();
			}
		} catch (HibernateException he) {
			if (t != null) {
				t.rollback();
			}
			log.error(he.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return result;
	}

	/**
	 * Utility methods
	 */

	private List<Calendar> calendarListFromCalendarEntry(
			List<CalendarListEntry> inList) {
		List<Calendar> tmpCalendarList = new ArrayList<Calendar>();
		for (CalendarListEntry calendarListEntry : inList) {
			Calendar tmpCalendar = new Calendar();
			tmpCalendar.setSummary(calendarListEntry.getSummary());
			tmpCalendar.setCalendarId(calendarListEntry.getId());
			tmpCalendar.setDescription(calendarListEntry.getDescription());
			tmpCalendar.setLocation(calendarListEntry.getLocation());
			tmpCalendarList.add(tmpCalendar);
		}
		return tmpCalendarList;
	}

	private List<Event> eventListFromGoogleEventList(
			List<com.google.api.services.calendar.model.Event> inList) {
		List<Event> tmpEventList = new ArrayList<Event>();
		for (com.google.api.services.calendar.model.Event event : inList) {
			Event tmpEvent = new Event();
			tmpEvent.setEndDate(event.getEnd().toString());
			tmpEvent.setStartDate(event.getStart().toString());
			tmpEvent.setEventId(event.getId());
			tmpEvent.setEventSummary(event.getSummary());
			tmpEvent.setLocation(event.getLocation());
			tmpEvent.setEventDescription(event.getDescription());
			tmpEventList.add(tmpEvent);
		}
		return tmpEventList;
	}

	/**
	 * Used to map Societies subscriber to google EventAttendee
	 */

	private EventAttendee createEventAttendee(String subscriberId) {
		EventAttendee attendee = new EventAttendee();
		// Create the MD5 to use in the email field
		MessageDigest messageDigest;
		String mailField = "";
		try {
			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();
			messageDigest
					.update(subscriberId.getBytes(Charset.forName("UTF8")));
			byte[] resultByte = messageDigest.digest();
			mailField = new String(Hex.encodeHex(resultByte));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		//
		attendee.setEmail(mailField + "@societies.eu");
		attendee.setDisplayName(subscriberId);
		return attendee;
	}

}
