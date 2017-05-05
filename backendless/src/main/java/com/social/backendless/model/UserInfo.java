package com.social.backendless.model;

import java.io.Serializable;
import java.util.Date;

public class UserInfo implements Serializable{

    private String mObjectId;
	private String mName;
	private String mFullName;
	private String mPhoneNumber;
    private String mProfilePicture;
	private String mLastSeen;
	private String mModifiedDate;
	private boolean mOnline;
	
	public String getPhoneNumber() {
		return mPhoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.mPhoneNumber = phoneNumber;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

    public String getFullName() {
        return mFullName;
    }

    public void setFullName(String fullName) {
        this.mFullName = fullName;
    }

    public String getObjectId() {
        return mObjectId;
    }

    public void setObjectId(String objectId) {
        this.mObjectId = objectId;
    }

    public String getProfilePicture() {
        return mProfilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.mProfilePicture = profilePicture;
    }

	public String getLastSeen() {
		return mLastSeen;
	}

	public void setLastSeen(String lastSeen) {
		this.mLastSeen = lastSeen;
	}

	public boolean isOnline() {
		return mOnline;
	}

	public void setOnline(boolean online) {
		this.mOnline = online;
	}

	public String getModifiedDate() {
		return mModifiedDate;
	}

	public void setModifiedDate(String mModifiedDate) {
		this.mModifiedDate = mModifiedDate;
	}
}
