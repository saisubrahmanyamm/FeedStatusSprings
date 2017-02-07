package com.springs.feedstatus;


import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class SignupAndLogin {

	public static boolean signup(String firstName, String lastName, String email, String password, String gender,
			String dob, String mobile) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery("select from " + UserDatabase.class.getName() + " where email == emailParam "
				+ "parameters String emailParam");

		@SuppressWarnings("unchecked")
		List<UserDatabase> results = (List<UserDatabase>) q.execute(email);

		if (results.isEmpty()) {
			UserDatabase userTable = new UserDatabase();
			userTable.setFirstName(firstName);
			userTable.setLastName(lastName);
			userTable.setEmail(email);
			userTable.setPassword(password);
			userTable.setGender(gender);
			userTable.setDob(dob);
			userTable.setMobile(mobile);
			pm.makePersistent(userTable);
			pm.close();
			return true;
		} else {
			return false;
		}
	}

	public static boolean loginValidation(String email, String password) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery("select from " + UserDatabase.class.getName() + " where email == emailParam "
				+ "parameters String emailParam");

		@SuppressWarnings("unchecked")
		List<UserDatabase> results = (List<UserDatabase>) q.execute(email);

		if (!results.isEmpty()) {
			for (UserDatabase tableObj : results) {
				String emailfound = tableObj.getEmail();
				String passwordfound = tableObj.getPassword();
				if ((email.equals(emailfound)) && (password.equals(passwordfound))) {
					return true;
				} else {
					return false;
				}
			}
		}
		q.closeAll();
		pm.close();
		return false;
	}

	public static String gettingUserName(String email) {
		String userName = "";
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery("select from " + UserDatabase.class.getName() + " where email == emailParam "
				+ "parameters String emailParam");

		@SuppressWarnings("unchecked")
		List<UserDatabase> results = (List<UserDatabase>) q.execute(email);
		if (!results.isEmpty()) {
			for (UserDatabase tableObj : results) {
				userName = tableObj.getFirstName();
			}
		}
		return userName;
	}
}