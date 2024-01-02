package org.vastpro.leo.services;

import java.util.Map;

import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

public class CreateEntryService {
	public static final String module=CreateEntryService.class.getName(); 
	
	public static Map<String,Object> create(DispatchContext dctx,Map<String,? extends Object> context){
		Map<String, Object> result = ServiceUtil.returnSuccess();
		System.out.println("=================result:"+result+"=================");
		Delegator delegator = dctx.getDelegator();
		GenericValue ElisaAff=delegator.makeValue("ElisaAff");
		System.out.println("=================ElisaAff:"+ElisaAff+"=================");
		ElisaAff.setPKFields(context);
		ElisaAff.setNonPKFields(context);
		try {
			ElisaAff= delegator.create(ElisaAff);
		} catch (GenericEntityException e) {
			e.printStackTrace();
			return ServiceUtil.returnError("Error");
		}
		return result;
		
	}
}
