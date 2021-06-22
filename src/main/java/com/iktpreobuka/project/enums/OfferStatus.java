package com.iktpreobuka.project.enums;

public enum OfferStatus {

	WAIT_FOR_APPROVING("wait"),
	APPROVED("approved"),
	DECLINED("declined"),
	EXPIRED("expired");
	
	private String type;
	
	private OfferStatus(String type) {
		this.type = type;
	}
	
	public static OfferStatus fromString(String t) {
		for (OfferStatus ot : OfferStatus.values())
			if (ot.type.equalsIgnoreCase(t))
				return ot;
		
		return null;
	}
}
