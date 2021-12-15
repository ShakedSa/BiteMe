package Util;

import Entities.Customer;

public class QRReader {
	/**
	 * Module simulating QR Code reader.
	 * 
	 * @param user
	 * 
	 * @return String
	 * */
	public static String ReadQRCode(Customer customer) {
		String qrCode = customer.getW4c().getQRCode();
		if(qrCode == null || qrCode.equals("")) {
			return null;
		}
		return qrCode;
	}
}
