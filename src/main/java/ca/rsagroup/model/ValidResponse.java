package ca.rsagroup.model;

import java.io.Serializable;

public class ValidResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String policy;
	private String policyDate;
	private String status;
	//airmilesNumber and airmilesName are included for passing data to Tealium data layer
	private String airmilesNumber;
	private String airmilesName;
	
	public ValidResponse(){
		
	}

	public ValidResponse(String policy, String policyDate, String status){
		this.policyDate = policyDate;
		this.policy = policy;
		this.status = status;
	}
	public ValidResponse(String policy, String policyDate, String status, String airmilesNumber, String airmilesName){
		this.policyDate = policyDate;
		this.policy = policy;
		this.status = status;
		this.airmilesNumber = airmilesNumber;
		this.airmilesName = airmilesName;
	}

	
	public String getPolicy() {
		return policy;
	}
	public void setPolicy(String policy) {
		this.policy = policy;
	}
	public String getPolicyDate() {
		return policyDate;
	}
	public void setPolicyDate(String policyDate) {
		this.policyDate = policyDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getAirmilesNumber() {
		return airmilesNumber;
	}

	public void setAirmilesNumber(String airmilesNumber) {
		this.airmilesNumber = airmilesNumber;
	}

	public String getAirmilesName() {
		return airmilesName;
	}

	public void setAirmilesName(String airmilesName) {
		this.airmilesName = airmilesName;
	}
	
}
