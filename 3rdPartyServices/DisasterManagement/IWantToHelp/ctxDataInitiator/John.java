package org.societies.thirdPartyServices.disasterManagement.wantToHelp.ctxDataInitiator;

public class John extends BaseUser {

	@Override
	public String getName() {
		
		return "john";
	}

	@Override
	public String getSex() {

		return "male";
	}

	@Override
	public String getAge() {
		
		return "30";
	}

	@Override
	public String getLanguages() {
		
		return "english,french,portuguese";
	}

	@Override
	public String getInterests() {
		
		return "sports,cooking,reading";
	}

	@Override
	public String getMovies() {
		
		return "finding nemo,the godfather";
	}

	@Override
	public String getOccupation() {
		
		return "researcher";
	}

	@Override
	public String getStatus() {
		
		return "busy";
	}

	@Override
	public String getEmail() {

		return "john@societies.org";
	}

	@Override
	public String getBirthday() {
		
		return "10/1/1980";
	}

	@Override
	public String getPoliticalViews() {
		
		return "democratic";
	}

	@Override
	public String getLocationSymbolic() {
		
		return "HWCampus";
	}

	@Override
	public String getLocationCoordinates() {
		
		return "1234,1234";
	}

	@Override
	public String getFriends() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.societies.context.dataInit.impl.BaseUser#getSkills()
	 */
	@Override
	public String getSkills() {
		// TODO Auto-generated method stub
		return null;
	}

}