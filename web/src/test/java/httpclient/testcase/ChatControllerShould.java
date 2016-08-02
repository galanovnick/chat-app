package httpclient.testcase;

import entity.tiny.chat.ChatId;
import httpclient.testunit.HttpClientTestUnit;
import httpclient.testunit.impl.HttpClientTestUnitImpl;
import httpclient.testunit.impl.Request;
import httpclient.testunit.impl.Response;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;

public class ChatControllerShould {
    private final String baseUrl = "http://localhost:8080/api";
    private final HttpClient client = HttpClientBuilder.create().build();

    private final HttpClientTestUnit testUnit = new HttpClientTestUnitImpl();

    private final String username = UUID.randomUUID().toString();
    private String token;

    @Before
    public void before() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", "12345"));
        params.add(new BasicNameValuePair("passwordConfirm", "12345"));

        testUnit.sendPost(new Request(params, baseUrl + "/register", client))
                .isStatusCodeEquals(SC_OK)
                .isJson()
                .hasProperty("message", "User has been successfully registered.");


        params.clear();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", "12345"));

        Response loginResponse =
                testUnit.sendPost(new Request(params, baseUrl + "/login", client));
        loginResponse
            .isStatusCodeEquals(SC_OK)
            .isJson()
                .hasProperty("token");
        token = loginResponse.getProperty("token");
    }

    @Test
    public void provideAvailableChatList() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("token", token));

        testUnit.sendPost(new Request(params, baseUrl + "/chats", client))
                .isStatusCodeEquals(SC_OK)
                .isJson()
                    .hasProperty("chats");
    }

    @Test
    public void handleChatCreation() {
        String chatName = UUID.randomUUID().toString();
        Response newChatResponse = createNewChat(chatName);
        newChatResponse
                .isStatusCodeEquals(SC_OK)
                    .hasProperty("chats");
    }

    @Test
    public void handleChatWithEmptyNameCreation() {
        Response newChatResponse = createNewChat("");
        newChatResponse
                .isStatusCodeEquals(555)
                .hasProperty("message", "Chat name cannot be empty.");
    }

    @Test
    public void handleChatWithInvalidNameCreation() {
        Response newChatResponse = createNewChat("   ");
        newChatResponse
                .isStatusCodeEquals(555)
                .hasProperty("message", "Chat name cannot start with whitespace.");
    }

    @Test
    public void handleUserJoin() {
        Response newChatResponse = createNewChat(UUID.randomUUID().toString());
        newChatResponse
                .isStatusCodeEquals(SC_OK)
                .hasProperty("chatId");

        ChatId chatId = new ChatId(Long.parseLong(newChatResponse.getProperty("chatId")));
        joinChat(chatId);
    }

    @Test
    public void handleUserLeave() {
        Response newChatResponse = createNewChat(UUID.randomUUID().toString());
        newChatResponse
                .isStatusCodeEquals(SC_OK)
                .hasProperty("chatId");

        ChatId chatId = new ChatId(Long.parseLong(newChatResponse.getProperty("chatId")));
        joinChat(chatId);
        leaveChat(chatId);
    }

    @Test
    public void handleMessageAddition() {
        Response newChatResponse = createNewChat(UUID.randomUUID().toString());
        newChatResponse
                .isStatusCodeEquals(SC_OK)
                .hasProperty("chatId");

        ChatId chatId = new ChatId(Long.parseLong(newChatResponse.getProperty("chatId")));
        joinChat(chatId);
        addMessage(chatId, "Hi!");
    }

    @Test
    public void provideMessageList() {
        Response newChatResponse = createNewChat(UUID.randomUUID().toString());
        newChatResponse
                .isStatusCodeEquals(SC_OK)
                .hasProperty("chatId");

        ChatId chatId = new ChatId(Long.parseLong(newChatResponse.getProperty("chatId")));
        joinChat(chatId);
        addMessage(chatId, "Hi!");
        addMessage(chatId, "Sup?");

        getChatMessages(chatId);
    }

    private void getChatMessages(ChatId chatId) {

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("chatId", chatId.value().toString()));

        testUnit.sendPost(new Request(params, baseUrl + "/chat/messages", client))
                .isStatusCodeEquals(SC_OK)
                .hasProperty("messages");
    }

    private void addMessage(ChatId chatId, String message) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("chatId", chatId.value().toString()));
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("message", message));

        testUnit.sendPost(new Request(params, baseUrl + "/chat/add-message", client))
                .isStatusCodeEquals(SC_OK)
                .hasProperty("roomId", chatId.value().toString())
                .hasProperty("messages");
    }

    private void leaveChat(ChatId chatId) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("chatId", chatId.value().toString()));

        testUnit.sendPost(new Request(params, baseUrl + "/chat/leave", client))
                .isStatusCodeEquals(SC_OK)
                .hasProperty("message", "User successfully left.");
    }

    private void joinChat(ChatId chatId) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("chatId", chatId.value().toString()));

        testUnit.sendPost(new Request(params, baseUrl + "/chat/join", client))
                .isStatusCodeEquals(SC_OK)
                .hasProperty("chatName");
    }

    private Response createNewChat(String chatName) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("chatName", chatName));

        return testUnit.sendPost(new Request(params, baseUrl + "/chat/new", client));
    }
}
