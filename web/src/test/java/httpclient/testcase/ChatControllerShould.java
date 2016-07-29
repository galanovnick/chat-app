package httpclient.testcase;

import entity.tiny.chat.ChatId;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static httpclient.HttpClientTestUtils.getResponseContent;
import static httpclient.HttpClientTestUtils.sendPost;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ChatControllerShould {
    private final String baseUrl = "http://localhost:8080/";
    private HttpClient client = HttpClientBuilder.create().build();

    private final String username = UUID.randomUUID().toString();
    private String token;

    @Before
    public void before() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", "12345"));
        params.add(new BasicNameValuePair("passwordConfirm", "12345"));

        HttpResponse registrationResponse = sendPost(baseUrl + "/register", params, client);
        if (registrationResponse == null) {
            fail("Failed due null response.");
        }
        String expectedContent = "{\"isRegistered\": \"true\"," +
                "\"message\": \"User has been successfully registered.\"}";

        String actualContent = getResponseContent(registrationResponse);

        assertEquals("Failed on user registration post request.",
                expectedContent, actualContent);

        params.clear();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", "12345"));

        HttpResponse authenticationResponse = sendPost(baseUrl + "/login", params, client);
        if (authenticationResponse == null) {
            fail("Failed due null response.");
        }
        assertEquals("Failed on user authentication.", 200,
                authenticationResponse.getStatusLine().getStatusCode());

        String responseContent = getResponseContent(authenticationResponse);

        Pattern pattern = Pattern.compile("\"token\": \"(.+)\"");
        Matcher matcher = pattern.matcher(responseContent);
        System.out.println(responseContent);
        if (matcher.find()) {
            token = matcher.group(1);
        } else {
            fail("Failed due invalid authentication response.");
        }
    }

    @Test
    public void handleChatCreation() {
        createNewChat(UUID.randomUUID().toString());
    }

    @Test
    public void handleUserJoin() {
        ChatId chatId = createNewChat(UUID.randomUUID().toString());
        joinChat(chatId);
    }

    @Test
    public void handleUserLeave() {
        ChatId chatId = createNewChat(UUID.randomUUID().toString());
        joinChat(chatId);
        leaveChat(chatId);
    }

    @Test
    public void handleMessageAddition() {
        ChatId chatId = createNewChat(UUID.randomUUID().toString());
        joinChat(chatId);
        addMessage(chatId, "Hi!");
    }

    @Test
    public void provideMessageList() {
        ChatId chatId = createNewChat(UUID.randomUUID().toString());
        joinChat(chatId);
        addMessage(chatId, "Hi!");
        addMessage(chatId, "Sup?");

        String expected = "{\"messages\": [\"Hi!\", \"Sup?\"]}";
        String actual = getChatMessages(chatId);

        assertEquals("Failed due incorrect message list.", expected, actual);
    }

    private String getChatMessages(ChatId chatId) {

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("chatId", chatId.value().toString()));

        HttpResponse messagesResponse = sendPost(baseUrl + "/chat/messages", params, client);
        if (messagesResponse == null) {
            fail("Failed due null response.");
        }

        assertEquals("Failed due incorrect response code.",
                200, messagesResponse.getStatusLine().getStatusCode());

        return getResponseContent(messagesResponse);
    }

    private void addMessage(ChatId chatId, String message) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("chatId", chatId.value().toString()));
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("message", message));

        HttpResponse addMessageResponse = sendPost(baseUrl + "/chat/add-message", params, client);
        if (addMessageResponse == null) {
            fail("Failed due null response.");
        }

        assertEquals("Failed due incorrect response code.",
                200, addMessageResponse.getStatusLine().getStatusCode());

        String expected = "{\"isMessageAdded\": \"true\"}";

        String actual = getResponseContent(addMessageResponse);

        assertEquals("Failed due incorrect response content.", expected, actual);
    }

    private void leaveChat(ChatId chatId) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("chatId", chatId.value().toString()));

        HttpResponse leaveChatResponse = sendPost(baseUrl + "/chat/leave", params, client);
        if (leaveChatResponse == null) {
            fail("Failed due null response.");
        }

        assertEquals("Failed due incorrect response code.",
                200, leaveChatResponse.getStatusLine().getStatusCode());

        String expected = "{\"isLeft\": \"true\"}";

        String actual = getResponseContent(leaveChatResponse);

        assertEquals("Failed due incorrect response content.", expected, actual);
    }

    private void joinChat(ChatId chatId) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("chatId", chatId.value().toString()));

        HttpResponse joinChatResponse = sendPost(baseUrl + "/chat/join", params, client);
        if (joinChatResponse == null) {
            fail("Failed due null response.");
        }

        assertEquals("Failed due incorrect response code.",
                200, joinChatResponse.getStatusLine().getStatusCode());

        String expected = "{\"isJoined\": \"true\"}";

        String actual = getResponseContent(joinChatResponse);

        assertEquals("Failed due incorrect response content.", expected, actual);
    }

    private ChatId createNewChat(String chatName) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("chatName", chatName));

        HttpResponse newChatResponse = sendPost(baseUrl + "/chat/new", params, client);
        if (newChatResponse == null) {
            fail("Failed due null response.");
        }

        assertEquals("Failed due incorrect response code.",
                200, newChatResponse.getStatusLine().getStatusCode());

        String content = getResponseContent(newChatResponse);
        Pattern pattern = Pattern.compile("\"chatId\": \"(.*)\"");
        Matcher matcher = pattern.matcher(content);

        if (!matcher.find()) {
            fail("Failed due incorrect response content.");
        }

        return new ChatId(Long.parseLong(matcher.group(0)));
    }
}
