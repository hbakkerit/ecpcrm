/**
 * Copyright 2015-2016 Eurocommercial Properties NV
 * <p>
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.incode.eurocommercial.ecpcrm.webapp;

import java.io.IOException;
import java.io.InputStreamReader;

import com.github.mjeanroy.junit.servers.jetty.EmbeddedJetty;
import com.github.mjeanroy.junit.servers.rules.JettyServerRule;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.common.io.CharStreams;

import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;

public abstract class EcpCrmTestAbstract {

    @ClassRule
    public static JettyServerRule server = new JettyServerRule(new EmbeddedJetty());

    private final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    private HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
        @Override public void initialize(final HttpRequest httpRequest) throws IOException {
            httpRequest.setHeaders(new HttpHeaders() {
                {
                    setBasicAuthentication("ecpcrm-admin","pass");
                }
            });
        }
    });

    protected String sendRequest(String json, String endpoint) throws Exception {
        GenericUrl url = new GenericUrl(server.getUrl() + "restful/crm/api/6.0/" + endpoint);
        HttpContent content = ByteArrayContent.fromString("application/json", json);
        HttpRequest request = requestFactory.buildPostRequest(url, content);
        HttpResponse response = request.execute();

        return CharStreams.toString(new InputStreamReader(response.getContent()));
    }

    @Test
    public void can_bootstrap_test() throws Exception {
    }

    @Test
    @Ignore
    // TODO: Not sure how to test this
    public void when_request_is_invalid_we_expect_300_error() throws Exception {
    }

    @Test
    @Ignore
    // TODO: Not sure how to test this
    public void when_request_is_unauthenticated_we_expect_310_error() throws Exception {
    }

    @Test
    @Ignore
    // TODO: Not sure how to test this
    public void when_device_is_invalid_we_expect_301_error() throws Exception {
    }

    @Test
    @Ignore
    // TODO: Can't seem to find a case in which a parameter would be invalid
    public void when_parameter_is_invalid_we_expect_302_error() throws Exception {
    }
}
