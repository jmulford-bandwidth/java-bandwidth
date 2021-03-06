package com.bandwidth.sdk.model;

import com.bandwidth.sdk.model.events.EventBase;
import org.hamcrest.CoreMatchers;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;

import static org.junit.Assert.assertThat;

public class EventTest {

    @Test
    public void shouldBeCreatedFromJson() throws Exception {
        JSONObject jsonObject = (JSONObject) new JSONParser().parse("{\"id\":\"ce-hsdbdbdhd\",\"time\":1407916959116,\"name\":\"error\",\"data\":\"Call Id wasn't found on FreeSWITCH anymore\"}");

        EventBase event = new EventBase(jsonObject);
        assertThat(event.getId(), CoreMatchers.equalTo("ce-hsdbdbdhd"));
    }
}