package org.incode.eurocommercial.ecpcrm.webapp.card_check_unbound;

import java.net.URL;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import org.approvaltests.Approvals;
import org.junit.Ignore;
import org.junit.Test;

import org.incode.eurocommercial.ecpcrm.webapp.ecp_crm_test.EcpCrmTest;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;

public class CardCheckUnboundTest extends EcpCrmTest {

    private String endpoint = "card-check-unbound";

    private String sendRequest(String cardNumber) throws Exception {
        String request = "{\"card\": \"" + cardNumber +"\"}";
        return super.sendRequest(request, endpoint);
    }

    @Test
    public void when_required_parameter_is_missing_we_expect_302_error() throws Exception {
        String cardNumber = "";
        assertThatJson(sendRequest(cardNumber))
                .node("status").isEqualTo(302);
    }

    @Test
    public void when_card_does_not_exist_and_has_invalid_number_we_expect_312_error() throws Exception {
        String cardNumber = "1";
        assertThatJson(sendRequest(cardNumber))
            .node("status").isEqualTo(312);
    }

    @Test
    public void when_card_exists_but_is_not_enabled_we_expect_303_error() throws Exception {
        String cardNumber = "2037000090418";
        assertThatJson(sendRequest(cardNumber))
            .node("status").isEqualTo(303);
    }

    @Test
    @Ignore
    // TODO: Not sure how to test this yet
    public void when_card_exists_but_is_not_the_same_center_as_crm_user_we_expect_317_error() throws Exception {
        String cardNumber = "1";
        assertThatJson(sendRequest(cardNumber))
            .node("status").isEqualTo(317);
    }

    @Test
    public void when_card_exists_but_is_already_bound_to_user_we_expect_308_error() throws Exception {
        String cardNumber = "2037000029999";
        assertThatJson(sendRequest(cardNumber))
            .node("status").isEqualTo(308);
    }

    @Test
    @Ignore
    public void when_card_exists_and_is_unbound_we_expect_happy_response_existing_card() throws Exception {
        /* Haven't a test request yet */
        final URL resource = Resources.getResource(CardCheckUnboundTest.class, "CardCheckUnboundTest.when_card_does_not_exist_but_has_valid_number_we_expect_happy_response_existing_card.json");
        final String json = Resources.toString(resource, Charsets.UTF_8);
        Approvals.verifyJson(sendRequest(json));
    }

    @Test
    public void when_card_does_not_exist_but_has_valid_number_we_expect_happy_response_non_existing_card() throws Exception {
        final URL resource = Resources.getResource(CardCheckUnboundTest.class, "CardCheckUnboundTest.when_card_does_not_exist_but_has_valid_number_we_expect_happy_response_non_existing_card.json");
        final String json = Resources.toString(resource, Charsets.UTF_8);
        Approvals.verifyJson(sendRequest(json));
    }

}