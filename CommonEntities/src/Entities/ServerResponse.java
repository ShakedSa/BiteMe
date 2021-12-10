package Entities;

import java.io.Serializable;

/**
 * Class to hold a response from the server.
 * 
 * This class holds the object with the proper class for the request.
 * 
 * @author Shaked
 * */
public class ServerResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1062261494793351579L;
	private Object serverResponse;
	private String msg;
	private String dataType;
	
	
	public ServerResponse() {
	}
	
	/**
	 * @param dataType
	 */
	public ServerResponse(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * @param serverReponse
	 * @param msg
	 * @param dataType
	 */
	public ServerResponse(Object serverReponse, String msg, String dataType)
	{
		this.serverResponse = serverResponse;
		this.msg = msg;
		this.dataType = dataType;
	}

	/**
	 * @return the serverResponse
	 */
	public Object getServerResponse() {
		return serverResponse;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @return the dataType
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * @param serverResponse the serverResponse to set
	 */
	public void setServerResponse(Object serverResponse) {
		this.serverResponse = serverResponse;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	@Override
	public String toString() {
		return serverResponse.toString();
	}
	
}
