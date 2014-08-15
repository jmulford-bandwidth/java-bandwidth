package com.bandwidth.sdk.model;

import com.bandwidth.sdk.BandwidthRestClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vpotapenko
 */
public class AvailableNumbers {

    private final BandwidthRestClient client;

    public AvailableNumbers(BandwidthRestClient client) {
        this.client = client;
    }

    public QueryLocalNumbersBuilder queryLocalNumbersBuilder() {
        return new QueryLocalNumbersBuilder();
    }

    public QueryTollFreeNumbersBuilder queryTollFreeNumbersBuilder() {
        return new QueryTollFreeNumbersBuilder();
    }

    private List<Number> getLocalNumbers(Map<String, Object> params) throws IOException {
        JSONArray array = client.requestLocalAvailableNumbers(params);

        List<Number> numbers = new ArrayList<Number>();
        for (Object obj : array) {
            numbers.add(Number.from((JSONObject) obj));
        }
        return numbers;
    }

    private List<Number> getTollFreeNumbers(Map<String, Object> params) throws IOException {
        JSONArray array = client.requestTollFreeAvailableNumbers(params);

        List<Number> numbers = new ArrayList<Number>();
        for (Object obj : array) {
            numbers.add(Number.from((JSONObject) obj));
        }
        return numbers;
    }

    public class QueryTollFreeNumbersBuilder {

        private final Map<String, Object> params = new HashMap<String, Object>();

        public List<Number> list() throws IOException {
            return getTollFreeNumbers(params);
        }

        public QueryTollFreeNumbersBuilder quantity(int quantity) {
            params.put("quantity", String.valueOf(quantity));
            return this;
        }

        public QueryTollFreeNumbersBuilder pattern(String pattern) {
            params.put("pattern", pattern);
            return this;
        }
    }

    public class QueryLocalNumbersBuilder {

        private final Map<String, Object> params = new HashMap<String, Object>();

        public List<Number> list() throws IOException {
            return getLocalNumbers(params);
        }

        public QueryLocalNumbersBuilder city(String city) {
            params.put("city", city);
            return this;
        }

        public QueryLocalNumbersBuilder state(String state) {
            params.put("state", state);
            return this;
        }

        public QueryLocalNumbersBuilder zip(String zip) {
            params.put("zip", zip);
            return this;
        }

        public QueryLocalNumbersBuilder areaCode(String areaCode) {
            params.put("areaCode", areaCode);
            return this;
        }

        public QueryLocalNumbersBuilder localNumber(String localNumber) {
            params.put("localNumber", localNumber);
            return this;
        }

        public QueryLocalNumbersBuilder inLocalCallingArea(boolean inLocalCallingArea) {
            params.put("inLocalCallingArea", String.valueOf(inLocalCallingArea));
            return this;
        }

        public QueryLocalNumbersBuilder quantity(int quantity) {
            params.put("quantity", String.valueOf(quantity));
            return this;
        }
    }
}