package com.bandwidth.sdk.model;

import com.bandwidth.sdk.driver.IRestDriver;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * Information about call.
 *
 * @author vpotapenko
 */
public class Call extends BaseModelObject {

    public Call(IRestDriver driver, String parentUri, JSONObject jsonObject) {
        super(driver, parentUri, jsonObject);
    }

    public String getDirection() {
        return getPropertyAsString("direction");
    }

    public String getState() {
        return getPropertyAsString("state");
    }

    public String getFrom() {
        return getPropertyAsString("from");
    }

    public String getTo() {
        return getPropertyAsString("to");
    }

    public String getCallbackUrl() {
        return getPropertyAsString("callbackUrl");
    }

    public String getEvents() {
        return getPropertyAsString("events");
    }

    public Date getStartTime() {
        return getPropertyAsDate("startTime");
    }

    public Date getActiveTime() {
        return getPropertyAsDate("activeTime");
    }

    public Date getEndTime() {
        return getPropertyAsDate("endTime");
    }

    public Long getChargeableDuration() {
        return getPropertyAsLong("chargeableDuration");
    }

    public boolean isRecordingEnabled() {
        return getPropertyAsBoolean("recordingEnabled");
    }

    /**
     * Retrieve all recordings related to the call.
     *
     * @return recordings
     * @throws IOException
     */
    public List<Recording> getRecordings() throws IOException {
        String recordingsPath = StringUtils.join(new String[]{
                getUri(),
                "recordings"
        }, '/');
        JSONArray array = driver.getArray(recordingsPath, null);

        List<Recording> list = new ArrayList<Recording>();
        for (Object object : array) {
            list.add(new Recording(driver, recordingsPath, (JSONObject) object));
        }
        return list;
    }

    /**
     * Gets the events that occurred during the call.
     *
     * @return events
     * @throws IOException
     */
    public List<Event> getEventsList() throws IOException {
        String eventsPath = StringUtils.join(new String[]{
                getUri(),
                "events"
        }, '/');
        JSONArray array = driver.getArray(eventsPath, null);

        List<Event> list = new ArrayList<Event>();
        for (Object object : array) {
            list.add(new Event(driver, eventsPath, (JSONObject) object));
        }
        return list;
    }

    /**
     * Gets information about one call event.
     *
     * @param eventId event id
     * @return information about event
     * @throws IOException
     */
    public Event getEventById(String eventId) throws IOException {
        String eventPath = StringUtils.join(new String[]{
                getUri(),
                "events",
                eventId
        }, '/');
        JSONObject jsonObject = driver.getObject(eventPath);
        String eventsPath = StringUtils.join(new String[]{
                getUri(),
                "events"
        }, '/');
        return new Event(driver, eventsPath, jsonObject);
    }

    /**
     * Hang up a phone call.
     *
     * @throws IOException
     */
    public void hangUp() throws IOException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("state", "completed");

        String uri = getUri();
        driver.post(uri, params);

        JSONObject jsonObject = driver.getObject(uri);
        updateProperties(jsonObject);
    }

    /**
     * Answer an incoming phone call.
     *
     * @throws IOException
     */
    public void answerOnIncoming() throws IOException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("state", "active");

        String uri = getUri();
        driver.post(uri, params);

        JSONObject jsonObject = driver.getObject(uri);
        updateProperties(jsonObject);
    }

    /**
     * Reject an incoming phone call
     *
     * @throws IOException
     */
    public void rejectIncoming() throws IOException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("state", "rejected");

        String uri = getUri();
        driver.post(uri, params);

        JSONObject jsonObject = driver.getObject(uri);
        updateProperties(jsonObject);
    }

    /**
     * Turn call recording ON.
     *
     * @throws IOException
     */
    public void recordingOn() throws IOException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("recordingEnabled", "true");

        String uri = getUri();
        driver.post(uri, params);

        JSONObject jsonObject = driver.getObject(uri);
        updateProperties(jsonObject);
    }

    /**
     * Turn call recording OFF.
     *
     * @throws IOException
     */
    public void recordingOff() throws IOException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("recordingEnabled", "false");

        String uri = getUri();
        driver.post(uri, params);

        JSONObject jsonObject = driver.getObject(uri);
        updateProperties(jsonObject);
    }

    /**
     * Creates builder for transferring call.
     * <br>Example:<br>
     * <code>call.callTransferBuilder("{number}").sentence("hello").create();</code>
     *
     * @param transferTo number for transferring
     * @return new builder
     */
    public CallTransferBuilder callTransferBuilder(String transferTo) {
        return new CallTransferBuilder(transferTo);
    }

    /**
     * Creates new builder for playing an audio file or speaking a sentence in a call.
     * <br>Example:<br>
     * <code>call.newAudioBuilder().sentence("Hello").create();</code>
     *
     * @return new builder
     */
    public CallAudioBuilder newAudioBuilder() {
        return new CallAudioBuilder();
    }

    /**
     * Stop an audio file playing.
     *
     * @throws IOException
     */
    public void stopAudioFilePlaying() throws IOException {
        new CallAudioBuilder().fileUrl(StringUtils.EMPTY).create();
    }

    /**
     * Stop an audio sentence.
     *
     * @throws IOException
     */
    public void stopSentence() throws IOException {
        new CallAudioBuilder().sentence(StringUtils.EMPTY).create();
    }

    /**
     * Sends DTMF.
     *
     * @param dtmf DTMF value
     * @throws IOException
     */
    public void sendDtmf(String dtmf) throws IOException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dtmfOut", dtmf);

        String uri = StringUtils.join(new String[]{
                getUri(),
                "dtmf"
        }, '/');
        driver.post(uri, params);
    }

    /**
     * Creates a new builder for collecting a series of DTMF digits from a phone call with an optional prompt. This request returns immediately. When gather finishes, an event with the results will be posted to the callback URL.
     * <br>Example:<br>
     * <code>call.callGatherBuilder().maxDigits(5).promptFileUrl("url_to_file").create();</code>
     *
     * @return new builder
     */
    public CallGatherBuilder callGatherBuilder() {
        return new CallGatherBuilder();
    }

    /**
     * Gets the gather DTMF parameters and results.
     *
     * @param gatherId gather id
     * @return gather DTMF parameters and results
     * @throws IOException
     */
    public Gather getGatherById(String gatherId) throws IOException {
        String gatherPath = StringUtils.join(new String[]{
                getUri(),
                "gather",
                gatherId
        }, '/');
        JSONObject jsonObject = driver.getObject(gatherPath);
        String gathersPath = StringUtils.join(new String[]{
                getUri(),
                "events"
        }, '/');
        return new Gather(driver, gathersPath, jsonObject);
    }

    private void createGather(Map<String, Object> params) throws IOException {
        String uri = StringUtils.join(new String[]{
                getUri(),
                "gather"
        }, '/');
        driver.post(uri, params);
    }

    private void createCallAudio(Map<String, Object> params) throws IOException {
        String audioPath = StringUtils.join(new String[]{
                getUri(),
                "audio"
        }, '/');
        driver.post(audioPath, params);
    }

    private void transfer(Map<String, Object> params) throws IOException {
        params.put("state", "transferring");

        String uri = getUri();
        driver.post(uri, params);

        JSONObject jsonObject = driver.getObject(uri);
        updateProperties(jsonObject);
    }

    @Override
    public String toString() {
        return "Call{" +
                "id='" + getId() + '\'' +
                ", direction=" + getDirection() +
                ", state=" + getState() +
                ", from='" + getFrom() + '\'' +
                ", to='" + getTo() + '\'' +
                ", callbackUrl='" + getCallbackUrl() + '\'' +
                ", events='" + getEvents() + '\'' +
                ", startTime=" + getStartTime() +
                ", activeTime=" + getActiveTime() +
                ", endTime=" + getEndTime() +
                ", chargeableDuration=" + getChargeableDuration() +
                ", recordingEnabled=" + isRecordingEnabled() +
                '}';
    }

    public class CallTransferBuilder {

        private Map<String, Object> params = new HashMap<String, Object>();
        private Map<String, Object> whisperAudio = new HashMap<String, Object>();

        public CallTransferBuilder(String number) {
            params.put("transferTo", number);
        }

        public CallTransferBuilder callbackUrl(String callbackUrl) {
            params.put("callbackUrl", callbackUrl);
            return this;
        }

        public CallTransferBuilder transferCallerId(String transferCallerId) {
            params.put("transferCallerId", transferCallerId);
            return this;
        }

        public CallTransferBuilder sentence(String sentence) {
            whisperAudio.put("sentence", sentence);
            return this;
        }

        public CallTransferBuilder gender(Gender gender) {
            whisperAudio.put("gender", gender.name());
            return this;
        }

        public CallTransferBuilder voice(String voice) {
            whisperAudio.put("voice", voice);
            return this;
        }

        public CallTransferBuilder locale(SentenceLocale locale) {
            whisperAudio.put("locale", locale);
            return this;
        }

        public void create() throws IOException {
            if (!whisperAudio.isEmpty()) {
                params.put("whisperAudio", whisperAudio);
            }
            transfer(params);
        }
    }

    public class CallAudioBuilder {

        private final Map<String, Object> params = new HashMap<String, Object>();

        public CallAudioBuilder fileUrl(String fileUrl) {
            params.put("fileUrl", fileUrl);
            return this;
        }

        public CallAudioBuilder sentence(String sentence) {
            params.put("sentence", sentence);
            return this;
        }

        public CallAudioBuilder gender(Gender gender) {
            params.put("gender", gender.name());
            return this;
        }

        public CallAudioBuilder locale(SentenceLocale locale) {
            params.put("locale", locale.restValue);
            return this;
        }

        public CallAudioBuilder voice(String voice) {
            params.put("voice", voice);
            return this;
        }

        public CallAudioBuilder loopEnabled(boolean loopEnabled) {
            params.put("loopEnabled", String.valueOf(loopEnabled));
            return this;
        }

        public void create() throws IOException {
            createCallAudio(params);
        }
    }

    public class CallGatherBuilder {

        private Map<String, Object> params = new HashMap<String, Object>();
        private Map<String, Object> prompt = new HashMap<String, Object>();

        public void create() throws IOException {
            if (!prompt.isEmpty()) params.put("prompt", prompt);

            createGather(params);
        }

        public CallGatherBuilder maxDigits(int maxDigits) {
            params.put("maxDigits", String.valueOf(maxDigits));
            return this;
        }

        public CallGatherBuilder interDigitTimeout(int maxDigits) {
            params.put("interDigitTimeout", String.valueOf(maxDigits));
            return this;
        }

        public CallGatherBuilder terminatingDigits(String terminatingDigits) {
            params.put("terminatingDigits", terminatingDigits);
            return this;
        }

        public CallGatherBuilder suppressDtmf(boolean suppressDtmf) {
            params.put("suppressDtmf", String.valueOf(suppressDtmf));
            return this;
        }

        public CallGatherBuilder tag(String tag) {
            params.put("tag", tag);
            return this;
        }

        public CallGatherBuilder promptSentence(String promptSentence) {
            prompt.put("sentence", promptSentence);
            return this;
        }

        public CallGatherBuilder promptGender(Gender gender) {
            prompt.put("gender", gender.name());
            return this;
        }

        public CallGatherBuilder promptLocale(SentenceLocale locale) {
            prompt.put("locale", locale.restValue);
            return this;
        }

        public CallGatherBuilder promptFileUrl(String promptFileUrl) {
            prompt.put("fileUrl", promptFileUrl);
            return this;
        }

        public CallGatherBuilder promptLoopEnabled(boolean promptLoopEnabled) {
            prompt.put("loopEnabled", String.valueOf(promptLoopEnabled));
            return this;
        }

        public CallGatherBuilder promptBargeable(boolean promptBargeable) {
            prompt.put("bargeable", String.valueOf(promptBargeable));
            return this;
        }


    }
}
