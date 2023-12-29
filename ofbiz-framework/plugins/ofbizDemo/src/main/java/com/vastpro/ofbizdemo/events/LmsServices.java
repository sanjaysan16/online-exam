package com.vastpro.ofbizdemo.events;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.HashedMap;
import org.apache.solr.common.util.Hash;

public class LmsServices {

	public static String gettingJson(HttpServletRequest request,HttpServletResponse response){
		
		HashMap<String, String> lmsmap = new HashMap<String, String>();		
		lmsmap.put("sanjay", "sathyabama");
		lmsmap.put("hari", "jepiar");
		lmsmap.put("lakshmi", "savitha");
		
		request.setAttribute("lmsmap", lmsmap);
		return "Success";
		
	}
}

