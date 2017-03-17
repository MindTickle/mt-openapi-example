package com.mindtickle.openapi.mt_openapi_example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindtickle.mtapi.v2.request.UserSearchRequest;
import com.mindtickle.mtapi.v2.resource.User;
import com.mindtickle.mtapi.v2.response.BulkResponse;
import com.mindtickle.mtapi.v2.response.StatusCheckResponse;
import com.mindtickle.mtapi.v2.response.UserResponse;
import com.mindtickle.mtapi.v2.response.UserSearchResponse;
import com.mindtickle.mtapi.v2.utils.UserHelper;
import com.mindtickle.openapi.utils.NonRetryableAPIException;
import com.mindtickle.openapi.utils.RetryableAPIException;
import org.junit.Test;

import java.util.*;

/**
 * Created by gaurav on 13/01/17.
 */
public class UserTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getUsers() throws NonRetryableAPIException, RetryableAPIException, JsonProcessingException {
        UserSearchRequest userSearchRequest = new UserSearchRequest();

        List<User> users = UserHelper.getUsersfromsearchRequest(userSearchRequest);
        System.out.println(users.size());

    }


    @Test
    public void getUser() throws NonRetryableAPIException, RetryableAPIException, JsonProcessingException {
		String userid = "userid";
		UserResponse userResponse = UserHelper.getUser(userid);
        System.out.println(mapper.writeValueAsString(userResponse));
    }

    @Test
    public void createUser() throws NonRetryableAPIException, RetryableAPIException, JsonProcessingException, InterruptedException {
        User user = new User();
		user.groupIds = Arrays.asList("groupId_1", "groupid_2");
		user.name = " name";
		user.email = "email";
        user.profile = new HashMap<>();
		user.profile.put("dg", "designation");
		user.profile.put("dp", "Department");
        BulkResponse bulkResponse = UserHelper.createUser(user);
        System.out.println(mapper.writeValueAsString(bulkResponse));
        StatusCheckResponse statusCheckResponse;
        boolean checkSuccess;
        do {
            checkSuccess = true;
            statusCheckResponse = UserHelper.checkStatus(bulkResponse.processIds);
            System.out.println(mapper.writeValueAsString(statusCheckResponse));
            for(Map.Entry<String, StatusCheckResponse.ProcessState> entry:statusCheckResponse.status.entrySet()){
                if(!entry.getValue().equals(StatusCheckResponse.ProcessState.SUCCESS))
                    checkSuccess = false;
            }
            Thread.sleep(2000);
        }while (!checkSuccess);
    }

    @Test
    public void createUsers() throws NonRetryableAPIException, RetryableAPIException, JsonProcessingException, InterruptedException {
        List<User> users = new ArrayList<>();

		for (int i = 0; i < 100; i++) {
            User user = new User();
            user.groupIds = Arrays.asList("821635058478014498");
            user.name = " Test MTAPI-"+i;
			user.email = "test+" + i + "@mailinator.com";
            user.profile = new HashMap<>();
			user.profile.put("dg", "TEST-designation");
            users.add(user);
        }

        BulkResponse bulkResponse = UserHelper.updateUsers(users);
        System.out.println(mapper.writeValueAsString(bulkResponse));
        checkBulkStatus(bulkResponse);
    }

    @Test
    public void updateUsers() throws NonRetryableAPIException, RetryableAPIException, JsonProcessingException, InterruptedException {
        UserSearchRequest userSearchRequest = new UserSearchRequest();
		userSearchRequest.cname = "clientId";
		userSearchRequest.groupIds = Arrays.asList("groupId_1");
        List<User> users = UserHelper.getUsersfromsearchRequest(userSearchRequest);


        BulkResponse bulkResponse = UserHelper.updateUsers(users);

        System.out.println(mapper.writeValueAsString(bulkResponse));
        checkBulkStatus(bulkResponse);

    }

    @Test
    public void deleteUsers() throws NonRetryableAPIException, RetryableAPIException, JsonProcessingException, InterruptedException {
            UserSearchRequest userSearchRequest = new UserSearchRequest();
            userSearchRequest.emails = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			userSearchRequest.emails.add("test" + i + "@mailinator.com");
            }

            List<User> users = UserHelper.getUsersfromsearchRequest(userSearchRequest);

            List<String> ids = new ArrayList<>();

            for(User user: users){
                ids.add(user.id);
            }

            BulkResponse bulkResponse =UserHelper.deleteUsers(ids);
            checkBulkStatus(bulkResponse);

    }


    private static void checkBulkStatus(BulkResponse bulkResponse) throws NonRetryableAPIException, RetryableAPIException, JsonProcessingException, InterruptedException {
        StatusCheckResponse statusCheckResponse;
        boolean checkSuccess;
        do {
            checkSuccess = true;
            statusCheckResponse = UserHelper.checkStatus(bulkResponse.processIds);
            System.out.println(mapper.writeValueAsString(statusCheckResponse));
            for(Map.Entry<String, StatusCheckResponse.ProcessState> entry:statusCheckResponse.status.entrySet()){
                if(!entry.getValue().equals(StatusCheckResponse.ProcessState.SUCCESS))
                    checkSuccess = false;
            }
            Thread.sleep(500);
        }while (!checkSuccess);
    }
}
