/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.micro.integrator.api;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.esb.integration.common.utils.ESBIntegrationTest;

import java.io.IOException;

import static org.wso2.micro.integrator.api.Constants.COUNT;
import static org.wso2.micro.integrator.api.Constants.LIST;

public class DataSourceResourceTestCase extends ESBIntegrationTest {

    private String accessToken;
    private String endpoint;
    @BeforeClass(alwaysRun = true)
    public void setEnvironment() throws Exception {
        super.init();
        accessToken = TokenUtil.getAccessToken(hostName, portOffset);
        endpoint = "https://" + hostName + ":" + (DEFAULT_INTERNAL_API_HTTPS_PORT + portOffset) + "/management/data-sources";
    }

    /**
     * This test case verifies if datasource information is retrieved successfully.
     *
     * @throws IOException
     */
    @Test(groups = {"wso2.esb"}, description = "Test get data source info")
    public void retrieveDataSourceInfo() throws IOException {
        String endpoint1 = endpoint.concat("?name=").concat("MySQLConnection2");
        String responsePayload = sendHttpRequestAndGetPayload(endpoint1, accessToken);
        JSONObject jsonResponse = new JSONObject(responsePayload);
        String datasourceType = jsonResponse.get("type").toString();
        Assert.assertEquals(datasourceType, "RDBMS");
    }

    @Test(groups = { "wso2.esb"}, description = "Test get data-source resource for search key")
    public void retrieveSearchedDataSources() throws IOException {
        String endpoint2 = endpoint.concat("?searchKey=MYSQL");
        String responsePayload = sendHttpRequestAndGetPayload(endpoint2, accessToken);
        JSONObject jsonResponse = new JSONObject(responsePayload);
        Assert.assertEquals(jsonResponse.get(COUNT), 1, "Assert Failed due to the mismatch of " +
                "actual vs expected resource count");
        Assert.assertTrue(jsonResponse.get(LIST).toString().contains("MySQLConnection2"), "Assert failed " +
                "since expected resource name not found in the list");
    }
    
    @AfterClass(alwaysRun = true)
    public void cleanState() throws Exception {
        super.cleanup();
    }
}
