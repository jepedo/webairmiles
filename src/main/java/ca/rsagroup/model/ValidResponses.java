package ca.rsagroup.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ValidResponses implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ValidResponse> responses;

	public List<ValidResponse> getResponses() {
		if(responses == null){
			responses = new ArrayList<ValidResponse>();
		}		
		return responses;
	}

	public void setResponses(List<ValidResponse> responses) {
		this.responses = responses;
	}
	
}
