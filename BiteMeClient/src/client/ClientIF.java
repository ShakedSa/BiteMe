package client;

import java.io.File;
import java.util.HashMap;

import Entities.User;

/** Design Pattern of a Client logic interface. */
public interface ClientIF {
	public void display(String paramString);
	public void getResultFromServer(Object paramString);
	public void setUser(User user);
	public User getUser();
	public void setRestaurants(HashMap<String, File> restaurants);
}
