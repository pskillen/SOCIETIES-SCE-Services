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
package org.societies.rdPartyService.enterprise.sharedCalendar.dataObject;

import org.societies.api.activity.IActivity;

/**
 * Describe your class here...
 *
 * @author solutanet
 *
 */
public class CISActivity implements IActivity {

	private Long id;
	private String verb;
	private String actor;
	private String object;
	private String target;
	private long time;
	private String published;
	/* (non-Javadoc)
	 * @see org.societies.api.activity.IActivity#getId()
	 */
	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return id;
	}

	/* (non-Javadoc)
	 * @see org.societies.api.activity.IActivity#setId(java.lang.Long)
	 */
	@Override
	public void setId(Long id) {
		this.id=id;

	}

	/* (non-Javadoc)
	 * @see org.societies.api.activity.IActivity#getVerb()
	 */
	@Override
	public String getVerb() {
		// TODO Auto-generated method stub
		return this.verb;
	}

	/* (non-Javadoc)
	 * @see org.societies.api.activity.IActivity#setVerb(java.lang.String)
	 */
	@Override
	public void setVerb(String verb) {
		this.verb=verb;

	}

	/* (non-Javadoc)
	 * @see org.societies.api.activity.IActivity#getActor()
	 */
	@Override
	public String getActor() {
		// TODO Auto-generated method stub
		return this.actor;
	}

	/* (non-Javadoc)
	 * @see org.societies.api.activity.IActivity#setActor(java.lang.String)
	 */
	@Override
	public void setActor(String actor) {
		// TODO Auto-generated method stub
this.actor=actor;
	}

	/* (non-Javadoc)
	 * @see org.societies.api.activity.IActivity#getObject()
	 */
	@Override
	public String getObject() {
		// TODO Auto-generated method stub
		return this.object;
	}

	/* (non-Javadoc)
	 * @see org.societies.api.activity.IActivity#setObject(java.lang.String)
	 */
	@Override
	public void setObject(String object) {
		this.object=object;

	}

	/* (non-Javadoc)
	 * @see org.societies.api.activity.IActivity#getTarget()
	 */
	@Override
	public String getTarget() {
		// TODO Auto-generated method stub
		return this.target;
	}

	/* (non-Javadoc)
	 * @see org.societies.api.activity.IActivity#setTarget(java.lang.String)
	 */
	@Override
	public void setTarget(String target) {
		this.target=target;

	}

	/* (non-Javadoc)
	 * @see org.societies.api.activity.IActivity#getTime()
	 */
	@Override
	public long getTime() {
		// TODO Auto-generated method stub
		return this.time;
	}

	/* (non-Javadoc)
	 * @see org.societies.api.activity.IActivity#setTime(long)
	 */
	@Override
	public void setTime(long time) {
		this.time=time;

	}

	/* (non-Javadoc)
	 * @see org.societies.api.activity.IActivity#getPublished()
	 */
	@Override
	public String getPublished() {
		// TODO Auto-generated method stub
		return this.published;
	}

	/* (non-Javadoc)
	 * @see org.societies.api.activity.IActivity#setPublished(java.lang.String)
	 */
	@Override
	public void setPublished(String published) {
		this.published=published;

	}

}