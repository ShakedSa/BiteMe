package ClientServerComm;

/** Design Pattern of a Client logic interface. */
public interface ChatIF {
	public abstract void display(String paramString);

	public abstract void getResultFromServer(String paramString);
}
