package com.mindtickle.openapi.mt_openapi_example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindtickle.openapi.Company;
import com.mindtickle.openapi.Group;
import com.mindtickle.openapi.Platform;
import com.mindtickle.openapi.Series;
import com.mindtickle.openapi.objects.request.LearnerAddRequest;
import com.mindtickle.openapi.objects.request.SingleLearnerDetails;
import com.mindtickle.openapi.objects.response.CompanyProfileResponse;
import com.mindtickle.openapi.objects.response.LearnerAddResponse;
import com.mindtickle.openapi.utils.NonRetryableAPIException;
import com.mindtickle.openapi.utils.RetryableAPIException;

public class App 
{
	public static int EMAILS_PER_REQUEST = 10;
	public static ObjectMapper mapper = new ObjectMapper();

    public static void main( String[] args )
    {
    	try {
			App.getProfiles();
			sleepForSec();
			App.addToPlatform();
			sleepForSec();
			App.addToGroup();
			sleepForSec();
			App.addToSeries();
		} catch (NonRetryableAPIException e) {
			e.printStackTrace();
		} catch (RetryableAPIException e) {
			e.printStackTrace();
		} 
    }
    
    public static LearnerAddRequest getLearnerAddRequest(){
    	LearnerAddRequest learnerAddRequest = new LearnerAddRequest();
    	List<SingleLearnerDetails> learners = new ArrayList<SingleLearnerDetails>();
    	for (int i = 0; i < EMAILS_PER_REQUEST; i++) {
    		SingleLearnerDetails singleLearnerDetails = new SingleLearnerDetails();
    		singleLearnerDetails.setEmail("mylearner" + i + "@mailinator.com");
    		Map<String, String> profiles = new HashMap<String, String>();
    		profiles.put("rg", "India");
    		singleLearnerDetails.setProfile(profiles);
    		learners.add(singleLearnerDetails);
		}
    	learnerAddRequest.setLearners(learners);
    	return learnerAddRequest;
    }
    
    public static void sleepForSec(){
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    public static void addToPlatform() throws NonRetryableAPIException, RetryableAPIException{
    	LearnerAddResponse learnerAddResponse = Platform.addLearners(getLearnerAddRequest());
    	try {
			System.out.println("addToPlatform response : " + mapper.writeValueAsString(learnerAddResponse));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    }
    
    public static void addToSeries() throws NonRetryableAPIException, RetryableAPIException{
    	Series.createSeries("New Hire");
    	sleepForSec();
    	LearnerAddResponse learnerAddResponse = Series.addLearnerToSeriesByName("New Hire", getLearnerAddRequest());
    	try {
			System.out.println("addToSeries response : " + mapper.writeValueAsString(learnerAddResponse));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    }
    
    public static void addToGroup() throws NonRetryableAPIException, RetryableAPIException{
    	Group.createGroup("Asia Pacific");
    	sleepForSec();
    	LearnerAddResponse learnerAddResponse = Group.addLearnerToGroupByName("Asia Pacific", getLearnerAddRequest());
    	try {
			System.out.println("addToGroup response : " + mapper.writeValueAsString(learnerAddResponse));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    }

    public static void getProfiles() throws NonRetryableAPIException, RetryableAPIException{
    	CompanyProfileResponse companyProfileResponse = Company.getCompanyProfiles();
    	try {
			System.out.println("getProfiles response : " + mapper.writeValueAsString(companyProfileResponse));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    }
}
