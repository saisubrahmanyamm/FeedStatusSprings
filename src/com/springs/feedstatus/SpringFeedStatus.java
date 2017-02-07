package com.springs.feedstatus;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class SpringFeedStatus {

	// Validating session of user-mail
	@RequestMapping("/")
	protected String homePage(HttpServletRequest req) {
		HttpSession session = req.getSession();
		String email = (String) session.getAttribute("SessionID_Email");
		if (email != null) {
			return "home";
		} else {
			return "index";
		}
	}

	// Login activity
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	protected String userValidation(@RequestBody String loginData, HttpServletRequest req) throws Exception {
		String useremail, password;
		Map<String, String> responseMapObj = new HashMap<String, String>();

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> storingMapObj = objectMapper.readValue(loginData, new TypeReference<Map<String, Object>>() {
		});
		useremail = (String) storingMapObj.get("Email");
		password = (String) storingMapObj.get("Password");

		// Sending details for Login validation
		boolean result = SignupAndLogin.loginValidation(useremail, password);
		String userName = SignupAndLogin.gettingUserName(useremail);
		if (result == true) {
			responseMapObj.put("SuccessMsg", "success");
			responseMapObj.put("Email", useremail);
			HttpSession session = req.getSession();
			session.setAttribute("SessionID_Email", useremail);
			session.setAttribute("SessionID_UserName", userName);
			return objectMapper.writeValueAsString(responseMapObj);
		} else {
			responseMapObj.put("SuccessMsg", "failed");
			return objectMapper.writeValueAsString(responseMapObj);
		}

	}

	// Logout and session invalidating
	@RequestMapping(value = "/logout")
	protected String logout(HttpServletRequest req) {
		HttpSession session = req.getSession();
		session.invalidate();
		return "index";
	}

	// Redirecting Profile page
	@RequestMapping(value = "/profile")
	protected String profile(HttpServletRequest req) {
		HttpSession session = req.getSession();
		String email = (String) session.getAttribute("SessionID_Email");
		if (email != null)
			return "profile";
		else
			return "index";
	}

	// Redirecting Home page
	@RequestMapping(value = "/home")
	protected String home(HttpServletRequest req) {
		HttpSession session = req.getSession();
		String email = (String) session.getAttribute("SessionID_Email");
		if (email != null)
			return "home";
		else
			return "index";
	}

	// Signup activity
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	@ResponseBody
	protected String signup(@RequestBody String userData, HttpServletRequest req)
			throws JsonParseException, JsonMappingException, IOException {
		String firstName, lastName, email, password, dob, gender, mobile;
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, String> responseMapObj = new HashMap<String, String>();

		Map<String, Object> storingMapObj = objectMapper.readValue(userData, new TypeReference<Map<String, Object>>() {
		});
		firstName = (String) storingMapObj.get("FirstName");
		lastName = (String) storingMapObj.get("LastName");
		email = (String) storingMapObj.get("Email");
		password = (String) storingMapObj.get("Password");
		dob = (String) storingMapObj.get("DOB");
		gender = (String) storingMapObj.get("Gender");
		mobile = (String) storingMapObj.get("Mobile");

		// Sending details to method for storing
		boolean result = SignupAndLogin.signup(firstName, lastName, email, password, gender, dob, mobile);
		if (result == true) {
			responseMapObj.put("SuccessMsg", "success");
			responseMapObj.put("Email", email);
			HttpSession session = req.getSession();
			session.setAttribute("SessionID_Email", email);
			session.setAttribute("SessionID_UserName", firstName);
			return objectMapper.writeValueAsString(responseMapObj);
		} else {
			responseMapObj.put("SuccessMsg", "failed");
			return objectMapper.writeValueAsString(responseMapObj);
		}
	}

	// Updating the feeds
	@RequestMapping(value = "/updatefeed", method = RequestMethod.POST)
	@ResponseBody
	protected String update(@RequestBody String update) throws IOException {
		String feedText, userMail, userName;
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> responseMapObj = new HashMap<String, Object>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Map<String, Object> storingMapObj = objectMapper.readValue(update, new TypeReference<Map<String, Object>>() {
		});
		userName = (String) storingMapObj.get("userName");
		feedText = (String) storingMapObj.get("feed");
		userMail = (String) storingMapObj.get("userMail");
		long millis = System.currentTimeMillis();

		// Setting the fees for user
		FeedsDatabase updateFeed = new FeedsDatabase();
		if (!feedText.equals("")) {
			updateFeed.setUserName(userName);
			updateFeed.setFeed(feedText);
			updateFeed.setUserMail(userMail);
			updateFeed.setDate(millis);
		}
		pm.makePersistent(updateFeed);
		responseMapObj.put("Email", userMail);
		responseMapObj.put("UserName", userName);
		responseMapObj.put("Feed", feedText);
		responseMapObj.put("Time", millis);
		pm.close();
		return objectMapper.writeValueAsString(responseMapObj);

	}

	// Fetching update details from table
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/fetchUpdates")
	@ResponseBody
	public String fetchUpdates(@RequestBody String fetch) throws IOException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ObjectMapper objectMapper = new ObjectMapper();
		Query q = pm.newQuery("select from " + FeedsDatabase.class.getName() + " order by date desc");
		List<FeedsDatabase> feeds = null;
		feeds = (List<FeedsDatabase>) q.execute();
		if (!(feeds == null) && !feeds.isEmpty()) {
			return objectMapper.writeValueAsString(feeds);
		}
		pm.close();
		q.closeAll();
		return "";
	}

}
