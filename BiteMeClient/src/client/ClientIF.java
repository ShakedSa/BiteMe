package client;

import Entities.ServerResponse;

/** Design Pattern of a Client logic interface. */
public interface ClientIF {
	public void display(String paramString);
	public void getResultFromServer(Object paramString);
	public void setUser(ServerResponse user);
	public ServerResponse getUser();
	public void setRestaurants(ServerResponse restaurants);
	public void setFavRestaurants(ServerResponse favRestaurants);
	public void setMenu(ServerResponse menu);
	public void setOptionalComponentsInProduct(ServerResponse optionalComponents);
	public void setLastResponse(ServerResponse serverResponse);
}
