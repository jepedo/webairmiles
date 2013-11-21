package ca.rsagroup.model;

import java.util.ArrayList;
import java.util.List;


public class ValidResponses {
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
