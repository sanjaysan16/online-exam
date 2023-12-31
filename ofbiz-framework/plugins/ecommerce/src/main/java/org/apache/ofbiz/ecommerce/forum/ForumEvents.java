/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package org.apache.ofbiz.ecommerce.forum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityListIterator;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

public class ForumEvents {

    public static final String MODULE = ForumEvents.class.getName();

    public static Map<String, Object> getForumMessages(HttpServletRequest request, Delegator delegator) {

        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        // ========== Create View Indexes
        int viewIndex = 0;
        int viewSize = 20;

        if (UtilValidate.isNotEmpty(request.getParameter("VIEW_INDEX"))) {
            Integer viewIndexInteger = Integer.parseInt(request.getParameter("VIEW_INDEX"));
            if (viewIndexInteger != null) {
                viewIndex = viewIndexInteger;
            }
        }

        if (UtilValidate.isNotEmpty(request.getParameter("VIEW_SIZE"))) {
            Integer viewSizeInteger = Integer.parseInt(request.getParameter("VIEW_SIZE"));
            if (viewSizeInteger != null) {
                viewSize = viewSizeInteger;
            }
        }

        int lowIndex = viewIndex * viewSize;
        int highIndex = (viewIndex + 1) * viewSize;

        // =========== Set up the PerformFindList
        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
        String forumId = request.getParameter("forumId");
        Map<String, Object> inputFields = new HashMap<>();
        inputFields.put("contentIdStart", forumId);
        inputFields.put("caContentAssocTypeId_fld0", "PUBLISH_LINK");
        inputFields.put("caContentAssocTypeId_fld1", "RESPONSE");

        Map<String, Object> articlesFound = null;

        try {
            articlesFound = dispatcher.runSync("performFind", UtilMisc.<String, Object>toMap("entityName",
                    "ContentAssocViewTo", "inputFields", inputFields, "userLogin", userLogin, "orderBy", "-createdDate"));
        } catch (GenericServiceException e) {
            Debug.logError(e, "Cannot get ForumMessages for Forum %s", MODULE, forumId);
            return ServiceUtil.returnError("Error while searching for Messages, please retry and/or contact Admin.");
        }
        int start = viewIndex * viewSize;
        List<GenericValue> list = null;
        Integer listSize = 0;
        try (EntityListIterator it = (EntityListIterator) articlesFound.get("listIt")) {
            list = it.getPartialList(start + 1, viewSize); // list starts at '1'
            listSize = it.getResultsSizeAfterPartialList();
        } catch (ClassCastException | NullPointerException | GenericEntityException e) {
            Debug.logInfo("Problem getting partial list" + e, MODULE);
        }

        // create the result map
        Map<String, Object> result = ServiceUtil.returnSuccess();

        result.put("highIndex", highIndex);
        result.put("listSize", listSize);
        result.put("lowIndex", lowIndex);
        result.put("viewIndex", viewIndex);
        result.put("viewSize", viewSize);
        result.put("forumMessages", list);

        return result;
    }
}
