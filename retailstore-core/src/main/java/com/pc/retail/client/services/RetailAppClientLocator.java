package com.pc.retail.client.services;

public class RetailAppClientLocator {

	public RetailAppClient getRetailAppClient(){
		return new RetailAppClientImpl();
	}
}
