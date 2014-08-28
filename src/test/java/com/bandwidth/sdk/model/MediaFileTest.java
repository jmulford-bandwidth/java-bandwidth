package com.bandwidth.sdk.model;

import com.bandwidth.sdk.MockRestClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class MediaFileTest {

    @Test
    public void shouldBeCreatedFromJson() throws Exception {
        JSONObject jsonObject = (JSONObject) new JSONParser().parse("{\n" +
                "    \"contentLength\": 561276,\n" +
                "    \"mediaName\": \"{mediaName1}\",\n" +
                "    \"content\": \"https://api.com/v1/users/users/{userId}/media/{mediaName1}\"\n" +
                "  }");
        MediaFile mediaFile = new MediaFile(null, "parentUri", jsonObject);
        assertThat(mediaFile.getContentLength(), equalTo(561276l));
        assertThat(mediaFile.getContent(), equalTo("https://api.com/v1/users/users/{userId}/media/{mediaName1}"));
        assertThat(mediaFile.getMediaName(), equalTo("{mediaName1}"));
        assertThat(mediaFile.getUri(), equalTo("parentUri/{mediaName1}"));
    }

    @Test
    public void shouldDownloadContent() throws Exception {
        MockRestClient mockRestClient = new MockRestClient();
        JSONObject jsonObject = (JSONObject) new JSONParser().parse("{\n" +
                "    \"contentLength\": 561276,\n" +
                "    \"mediaName\": \"{mediaName1}\",\n" +
                "    \"content\": \"https://api.com/v1/users/users/{userId}/media/{mediaName1}\"\n" +
                "  }");
        MediaFile mediaFile = new MediaFile(mockRestClient, "parentUri", jsonObject);
        mediaFile.downloadTo(new File("path_to_file"));

        assertThat(mockRestClient.requests.get(0).name, equalTo("downloadFileTo"));
        assertThat(mockRestClient.requests.get(0).uri, equalTo("parentUri/{mediaName1}"));
        assertThat(mockRestClient.requests.get(0).params.get("filePath").toString(), equalTo("path_to_file"));
    }

    @Test
    public void shouldBeDeletable() throws Exception {
        MockRestClient mockRestClient = new MockRestClient();
        JSONObject jsonObject = (JSONObject) new JSONParser().parse("{\n" +
                "    \"contentLength\": 561276,\n" +
                "    \"mediaName\": \"{mediaName1}\",\n" +
                "    \"content\": \"https://api.com/v1/users/users/{userId}/media/{mediaName1}\"\n" +
                "  }");
        MediaFile mediaFile = new MediaFile(mockRestClient, "parentUri", jsonObject);
        mediaFile.delete();

        assertThat(mockRestClient.requests.get(0).name, equalTo("delete"));
        assertThat(mockRestClient.requests.get(0).uri, equalTo("parentUri/{mediaName1}"));
    }
}