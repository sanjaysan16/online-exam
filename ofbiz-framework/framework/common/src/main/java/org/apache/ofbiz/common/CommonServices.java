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
package org.apache.ofbiz.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.mail.internet.MimeMessage;

import org.apache.ofbiz.base.metrics.Metrics;
import org.apache.ofbiz.base.metrics.MetricsFactory;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilCodec;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.model.ModelEntity;
import org.apache.ofbiz.entity.transaction.TransactionUtil;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ModelService;
import org.apache.ofbiz.service.ServiceSynchronization;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.service.mail.MimeMessageWrapper;

/**
 * Common Services
 */
public class CommonServices {

    private static final String MODULE = CommonServices.class.getName();
    private static final String RESOURCE = "CommonUiLabels";

    /**
     * Generic Test Service
     *@param dctx The DispatchContext that this service is operating in
     *@param context Map containing the input parameters
     *@return Map with the result of the service, the output parameters
     */
    public static Map<String, Object> testService(DispatchContext dctx, Map<String, ?> context) {
        Map<String, Object> response = ServiceUtil.returnSuccess();

        if (!context.isEmpty()) {
            for (Map.Entry<String, ?> entry: context.entrySet()) {
                Object cKey = entry.getKey();
                Object value = entry.getValue();

                Debug.logInfo("---- SVC-CONTEXT: " + cKey + " => " + value, MODULE);
            }
        }
        if (!context.containsKey("message")) {
            response.put("resp", "no message found");
        } else {
            Debug.logInfo("-----SERVICE TEST----- : " + (String) context.get("message"), MODULE);
            response.put("resp", "service done");
        }

        Debug.logInfo("----- SVC: " + dctx.getName() + " -----", MODULE);
        return response;
    }

    /**
     * Generic Test SOAP Service
     *@param dctx The DispatchContext that this service is operating in
     *@param context Map containing the input parameters
     *@return Map with the result of the service, the output parameters
     */
    public static Map<String, Object> testSOAPService(DispatchContext dctx, Map<String, ?> context) {
        Delegator delegator = dctx.getDelegator();
        Map<String, Object> response = ServiceUtil.returnSuccess();

        List<GenericValue> testingNodes = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            GenericValue testingNode = delegator.makeValue("TestingNode");
            testingNode.put("testingNodeId", "TESTING_NODE" + i);
            testingNode.put("description", "Testing Node " + i);
            testingNode.put("createdStamp", UtilDateTime.nowTimestamp());
            testingNodes.add(testingNode);
        }
        response.put("testingNodes", testingNodes);
        return response;
    }

    public static Map<String, Object> blockingTestService(DispatchContext dctx, Map<String, ?> context) {
        Long duration = (Long) context.get("duration");
        if (duration == null) {
            duration = 30000L;
        }
        Debug.logInfo("-----SERVICE BLOCKING----- : " + duration / 1000d + " seconds", MODULE);
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
        }
        return CommonServices.testService(dctx, context);
    }

    public static Map<String, Object> testRollbackListener(DispatchContext dctx, Map<String, ?> context) {
        try {
            ServiceSynchronization.registerRollbackService(dctx, "testScv", null, context, false, false);
        } catch (GenericServiceException e) {
            Debug.logError(e, MODULE);
        }
        Locale locale = (Locale) context.get("locale");
        return ServiceUtil.returnError(UtilProperties.getMessage(RESOURCE, "CommonTestRollingBack", locale));
    }

    public static Map<String, Object> testCommitListener(DispatchContext dctx, Map<String, ?> context) {
        try {
            ServiceSynchronization.registerCommitService(dctx, "testScv", null, context, false, false);
        } catch (GenericServiceException e) {
            Debug.logError(e, MODULE);
        }
        return ServiceUtil.returnSuccess();
    }

    /**
     * Create Note Record
     *@param ctx The DispatchContext that this service is operating in
     *@param context Map containing the input parameters
     *@return Map with the result of the service, the output parameters
     */
    public static Map<String, Object> createNote(DispatchContext ctx, Map<String, ?> context) {
        Delegator delegator = ctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Timestamp noteDate = (Timestamp) context.get("noteDate");
        String partyId = (String) context.get("partyId");
        String noteName = (String) context.get("noteName");
        String note = (String) context.get("note");
        String noteId = delegator.getNextSeqId("NoteData");
        Locale locale = (Locale) context.get("locale");
        if (noteDate == null) {
            noteDate = UtilDateTime.nowTimestamp();
        }


        // check for a party id
        if (partyId == null) {
            if (userLogin != null && userLogin.get("partyId") != null) {
                partyId = userLogin.getString("partyId");
            }
        }

        Map<String, Object> fields = UtilMisc.toMap("noteId", noteId, "noteName", noteName, "noteInfo", note,
                "noteParty", partyId, "noteDateTime", noteDate);

        try {
            GenericValue newValue = delegator.makeValue("NoteData", fields);

            delegator.create(newValue);
        } catch (GenericEntityException e) {
            return ServiceUtil.returnError(UtilProperties.getMessage(RESOURCE, "CommonNoteCannotBeUpdated",
                    UtilMisc.toMap("errorString", e.getMessage()), locale));
        }
        Map<String, Object> result = ServiceUtil.returnSuccess();

        result.put("noteId", noteId);
        result.put("partyId", partyId);
        return result;
    }

    /**
     * Service for setting debugging levels.
     *@param dctc The DispatchContext that this service is operating in
     *@param context Map containing the input parameters
     *@return Map with the result of the service, the output parameters
     */
    public static Map<String, Object> adjustDebugLevels(DispatchContext dctc, Map<String, ?> context) {
        Debug.set(Debug.FATAL, "Y".equalsIgnoreCase((String) context.get("fatal")));
        Debug.set(Debug.ERROR, "Y".equalsIgnoreCase((String) context.get("error")));
        Debug.set(Debug.WARNING, "Y".equalsIgnoreCase((String) context.get("warning")));
        Debug.set(Debug.IMPORTANT, "Y".equalsIgnoreCase((String) context.get("important")));
        Debug.set(Debug.INFO, "Y".equalsIgnoreCase((String) context.get("info")));
        Debug.set(Debug.TIMING, "Y".equalsIgnoreCase((String) context.get("timing")));
        Debug.set(Debug.VERBOSE, "Y".equalsIgnoreCase((String) context.get("verbose")));

        return ServiceUtil.returnSuccess();
    }

    public static Map<String, Object> forceGc(DispatchContext dctx, Map<String, ?> context) {
        System.gc();
        return ServiceUtil.returnSuccess();
    }

    /**
     * Echo service; returns exactly what was sent.
     * This service does not have required parameters and does not validate
     */
    public static Map<String, Object> echoService(DispatchContext dctx, Map<String, ?> context) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.putAll(context);
        result.put(ModelService.RESPONSE_MESSAGE, ModelService.RESPOND_SUCCESS);
        return result;
    }

    /**
     * Return Error Service; Used for testing error handling
     */
    public static Map<String, Object> returnErrorService(DispatchContext dctx, Map<String, ?> context) {
        Locale locale = (Locale) context.get("locale");
        return ServiceUtil.returnError(UtilProperties.getMessage(RESOURCE, "CommonServiceReturnError", locale));
    }

    /**
     * Return TRUE Service; ECA Condition Service
     */
    public static Map<String, Object> conditionTrueService(DispatchContext dctx, Map<String, ?> context) {
        Map<String, Object> result = ServiceUtil.returnSuccess();
        result.put("conditionReply", Boolean.TRUE);
        return result;
    }

    /**
     * Return FALSE Service; ECA Condition Service
     */
    public static Map<String, Object> conditionFalseService(DispatchContext dctx, Map<String, ?> context) {
        Map<String, Object> result = ServiceUtil.returnSuccess();
        result.put("conditionReply", Boolean.FALSE);
        return result;
    }

    /** Cause a Referential Integrity Error */
    public static Map<String, Object> entityFailTest(DispatchContext dctx, Map<String, ?> context) {
        Delegator delegator = dctx.getDelegator();
        Locale locale = (Locale) context.get("locale");

        // attempt to create a DataSource entity w/ an invalid dataSourceTypeId
        GenericValue newEntity = delegator.makeValue("DataSource");
        newEntity.set("dataSourceId", "ENTITY_FAIL_TEST");
        newEntity.set("dataSourceTypeId", "ENTITY_FAIL_TEST");
        newEntity.set("description", "Entity Fail Test - Delete me if I am here");
        try {
            delegator.create(newEntity);
        } catch (GenericEntityException e) {
            Debug.logError(e, MODULE);
            return ServiceUtil.returnError(UtilProperties.getMessage(RESOURCE, "CommonEntityTestFailure", locale));
        }

        return ServiceUtil.returnSuccess();
    }

    /** Test entity sorting */
    public static Map<String, Object> entitySortTest(DispatchContext dctx, Map<String, ?> context) {
        Delegator delegator = dctx.getDelegator();
        Set<ModelEntity> set = new TreeSet<>();

        set.add(delegator.getModelEntity("Person"));
        set.add(delegator.getModelEntity("PartyRole"));
        set.add(delegator.getModelEntity("Party"));
        set.add(delegator.getModelEntity("ContactMech"));
        set.add(delegator.getModelEntity("PartyContactMech"));
        set.add(delegator.getModelEntity("OrderHeader"));
        set.add(delegator.getModelEntity("OrderItem"));
        set.add(delegator.getModelEntity("OrderContactMech"));
        set.add(delegator.getModelEntity("OrderRole"));
        set.add(delegator.getModelEntity("Product"));
        set.add(delegator.getModelEntity("RoleType"));

        for (ModelEntity modelEntity: set) {
            Debug.logInfo(modelEntity.getEntityName(), MODULE);
        }
        return ServiceUtil.returnSuccess();
    }

    public static Map<String, Object> makeALotOfVisits(DispatchContext dctx, Map<String, ?> context) {
        Delegator delegator = dctx.getDelegator();
        int count = (Integer) context.get("count");

        for (int i = 0; i < count; i++) {
            GenericValue v = delegator.makeValue("Visit");
            String seqId = delegator.getNextSeqId("Visit");

            v.set("visitId", seqId);
            v.set("userCreated", "N");
            v.set("sessionId", "NA-" + seqId);
            v.set("serverIpAddress", "127.0.0.1");
            v.set("serverHostName", "localhost");
            v.set("webappName", "webtools");
            v.set("initialLocale", "en_US");
            v.set("initialRequest", "https://localhost:8443/webtools/control/main");
            v.set("initialReferrer", "https://localhost:8443/webtools/control/main");
            v.set("initialUserAgent", "Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en-us) AppleWebKit/124 (KHTML, like Gecko) Safari/125.1");
            v.set("clientIpAddress", "127.0.0.1");
            v.set("clientHostName", "localhost");
            v.set("fromDate", UtilDateTime.nowTimestamp());

            try {
                delegator.create(v);
            } catch (GenericEntityException e) {
                Debug.logError(e, MODULE);
            }
        }

        return ServiceUtil.returnSuccess();
    }

    public static Map<String, Object> displayXaDebugInfo(DispatchContext dctx, Map<String, ?> context) {
        if (TransactionUtil.debugResources()) {
            if (UtilValidate.isNotEmpty(TransactionUtil.DEBUG_RES_MAP)) {
                TransactionUtil.logRunningTx();
            } else {
                Debug.logInfo("No running transaction to display.", MODULE);
            }
        } else {
            Debug.logInfo("Debug resources is disabled.", MODULE);
        }

        return ServiceUtil.returnSuccess();
    }

    public static Map<String, Object> byteBufferTest(DispatchContext dctx, Map<String, ?> context) {
        ByteBuffer buffer1 = (ByteBuffer) context.get("byteBuffer1");
        ByteBuffer buffer2 = (ByteBuffer) context.get("byteBuffer2");
        String fileName1 = (String) context.get("saveAsFileName1");
        String fileName2 = (String) context.get("saveAsFileName2");
        String ofbizHome = System.getProperty("ofbiz.home");
        String outputPath1 = ofbizHome + (fileName1.startsWith("/") ? fileName1 : "/" + fileName1);
        String outputPath2 = ofbizHome + (fileName2.startsWith("/") ? fileName2 : "/" + fileName2);
        RandomAccessFile file1 = null;
        RandomAccessFile file2 = null;

        try {
            file1 = new RandomAccessFile(outputPath1, "rw");
            file2 = new RandomAccessFile(outputPath2, "rw");
            file1.write(buffer1.array());
            file2.write(buffer2.array());
        } catch (IOException e) {
            Debug.logError(e, MODULE);
        } finally {
            try {
                file1.close();
                file2.close();
            } catch (Exception e) {
                Debug.logError(e, MODULE);
            }
        }

        return ServiceUtil.returnSuccess();
    }

    public static Map<String, Object> uploadTest(DispatchContext dctx, Map<String, ?> context) {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        byte[] array = (byte[]) context.get("uploadFile");
        String fileName = (String) context.get("_uploadFile_fileName");
        String contentType = (String) context.get("_uploadFile_contentType");

        Map<String, Object> createCtx = new LinkedHashMap<>();
        createCtx.put("binData", array);
        createCtx.put("dataResourceTypeId", "OFBIZ_FILE");
        createCtx.put("dataResourceName", fileName);
        createCtx.put("dataCategoryId", "PERSONAL");
        createCtx.put("statusId", "CTNT_PUBLISHED");
        createCtx.put("mimeTypeId", contentType);
        createCtx.put("userLogin", userLogin);

        Map<String, Object> createResp = null;
        try {
            createResp = dispatcher.runSync("createFile", createCtx);
        } catch (GenericServiceException e) {
            Debug.logError(e, MODULE);
            return ServiceUtil.returnError(e.getMessage());
        }
        if (ServiceUtil.isError(createResp)) {
            return ServiceUtil.returnError(ServiceUtil.getErrorMessage(createResp));
        }

        GenericValue dataResource = (GenericValue) createResp.get("dataResource");
        if (dataResource != null) {
            Map<String, Object> contentCtx = new LinkedHashMap<>();
            contentCtx.put("dataResourceId", dataResource.getString("dataResourceId"));
            contentCtx.put("localeString", ((Locale) context.get("locale")).toString());
            contentCtx.put("contentTypeId", "DOCUMENT");
            contentCtx.put("mimeTypeId", contentType);
            contentCtx.put("contentName", fileName);
            contentCtx.put("statusId", "CTNT_PUBLISHED");
            contentCtx.put("userLogin", userLogin);

            Map<String, Object> contentResp = null;
            try {
                contentResp = dispatcher.runSync("createContent", contentCtx);
            } catch (GenericServiceException e) {
                Debug.logError(e, MODULE);
                return ServiceUtil.returnError(e.getMessage());
            }
            if (ServiceUtil.isError(contentResp)) {
                return ServiceUtil.returnError(ServiceUtil.getErrorMessage(contentResp));
            }
        }

        return ServiceUtil.returnSuccess();
    }

    public static Map<String, Object> mcaTest(DispatchContext dctx, Map<String, ?> context) {
        MimeMessageWrapper wrapper = (MimeMessageWrapper) context.get("messageWrapper");
        MimeMessage message = wrapper.getMessage();
        try {
            if (message.getAllRecipients() != null) {
                Debug.logInfo("To: " + UtilMisc.toListArray(message.getAllRecipients()), MODULE);
            }
            if (message.getFrom() != null) {
                Debug.logInfo("From: " + UtilMisc.toListArray(message.getFrom()), MODULE);
            }
            Debug.logInfo("Subject: " + message.getSubject(), MODULE);
            if (message.getSentDate() != null) {
                Debug.logInfo("Sent: " + message.getSentDate().toString(), MODULE);
            }
            if (message.getReceivedDate() != null) {
                Debug.logInfo("Received: " + message.getReceivedDate().toString(), MODULE);
            }
        } catch (Exception e) {
            Debug.logError(e, MODULE);
        }
        return ServiceUtil.returnSuccess();
    }

    public static Map<String, Object> streamTest(DispatchContext dctx, Map<String, ?> context) {
        InputStream in = (InputStream) context.get("inputStream");
        OutputStream out = (OutputStream) context.get("outputStream");

        String line;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                Writer writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {
            while ((line = reader.readLine()) != null) {
                Debug.logInfo("Read line: " + line, MODULE);
                writer.write(line);
            }
        } catch (IOException e) {
            Debug.logError(e, MODULE);
            return ServiceUtil.returnError(e.getMessage());
        }

        Map<String, Object> result = ServiceUtil.returnSuccess();
        result.put("contentType", "text/plain");
        return result;
    }

    public static Map<String, Object> ping(DispatchContext dctx, Map<String, ?> context) {
        Delegator delegator = dctx.getDelegator();
        String message = (String) context.get("message");
        Locale locale = (Locale) context.get("locale");
        if (message == null) {
            message = "PONG";
        }

        long count;
        try {
            count = EntityQuery.use(delegator).from("SequenceValueItem").queryCount();
        } catch (GenericEntityException e) {
            Debug.logError(e.getMessage(), MODULE);
            return ServiceUtil.returnError(UtilProperties.getMessage(RESOURCE, "CommonPingDatasourceCannotConnect", locale));
        }

        if (count != 0L) {
            Map<String, Object> result = ServiceUtil.returnSuccess();
            result.put("message", message);
            return result;
        }
        return ServiceUtil.returnError(UtilProperties.getMessage(RESOURCE, "CommonPingDatasourceInvalidCount", locale));
    }

    public static Map<String, Object> getAllMetrics(DispatchContext dctx, Map<String, ?> context) {
        List<Map<String, Object>> metricsMapList = new LinkedList<>();
        Collection<Metrics> metricsList = MetricsFactory.getMetrics();
        for (Metrics metrics : metricsList) {
            Map<String, Object> metricsMap = new LinkedHashMap<>();
            metricsMap.put("name", metrics.getName());
            metricsMap.put("serviceRate", metrics.getServiceRate());
            metricsMap.put("threshold", metrics.getThreshold());
            metricsMap.put("totalEvents", metrics.getTotalEvents());
            metricsMapList.add(metricsMap);
        }
        Map<String, Object> result = ServiceUtil.returnSuccess();
        result.put("metricsList", metricsMapList);
        return result;
    }

    public static Map<String, Object> resetMetric(DispatchContext dctx, Map<String, ?> context) {
        String originalName = (String) context.get("name");
        Locale locale = (Locale) context.get("locale");
        String name = UtilCodec.getDecoder("url").decode(originalName);
        if (name == null) {
            return ServiceUtil.returnError(UtilProperties.getMessage(RESOURCE, "CommonExceptionThrownWhileDecodingMetric",
                    UtilMisc.toMap("originalName", originalName), locale));
        }
        Metrics metric = MetricsFactory.getMetric(name);
        if (metric != null) {
            metric.reset();
            return ServiceUtil.returnSuccess();
        }
        return ServiceUtil.returnError(UtilProperties.getMessage(RESOURCE, "CommonMetricNotFound", UtilMisc.toMap("name", name), locale));
    }
}
