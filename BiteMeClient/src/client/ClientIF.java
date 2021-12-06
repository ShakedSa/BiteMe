package client;

import Entities.User;

/** Design Pattern of a Client logic interface. */
public interface ClientIF {
	public void display(String paramString);
	public void getResultFromServer(Object paramString);
	public void setUser(User user);
	public User getUser();
}
