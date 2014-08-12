package com.bandwidth.sdk.bridges;

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
public class BandwidthBridges {

    private final BandwidthRestClient client;

    public BandwidthBridges(BandwidthRestClient client) {
        this.client = client;
    }

    public List<BandwidthBridge> getBridges() throws IOException {
        JSONArray array = client.getRestDriver().requestBridges();

        List<BandwidthBridge> bridges = new ArrayList<BandwidthBridge>();
        for (Object obj : array) {
            bridges.add(BandwidthBridge.from(client, (JSONObject) obj));
        }
        return bridges;
    }

    public BandwidthBridge getBridgeById(String id) throws IOException {
        JSONObject jsonObject = client.getRestDriver().requestBridgeById(id);
        return BandwidthBridge.from(client, jsonObject);
    }

    public NewBridgeBuilder newBridge() {
        return new NewBridgeBuilder();
    }

    private BandwidthBridge createBridge(Map<String, Object> params) throws IOException {
        JSONObject jsonObject = client.getRestDriver().createBridge(params);
        return BandwidthBridge.from(client, jsonObject);
    }

    public class NewBridgeBuilder {

        private Boolean bridgeAudio;
        private List<String> callIds = new ArrayList<String>();

        public NewBridgeBuilder bridgeAudio(Boolean bridgeAudio) {
            this.bridgeAudio = bridgeAudio;
            return this;
        }

        public NewBridgeBuilder addCallId(String callId) {
            callIds.add(callId);
            return this;
        }

        public BandwidthBridge create() throws IOException {
            Map<String, Object> params = new HashMap<String, Object>();
            if (bridgeAudio != null) params.put("bridgeAudio", String.valueOf(bridgeAudio));
            if (!callIds.isEmpty()) params.put("callIds", callIds);

            return createBridge(params);
        }
    }
}
