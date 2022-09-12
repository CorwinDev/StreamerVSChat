package nl.corwindev.streamervschat.youtube;


import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import nl.corwindev.streamervschat.commands;
import nl.corwindev.streamervschat.main;

import java.util.*;


public class YouTubeConnectionHelper {
    private static final String LIVE_CHAT_FIELDS =
            "items(authorDetails(channelId,displayName,isChatModerator,isChatOwner,isChatSponsor,"
                    + "profileImageUrl),snippet(displayMessage,superChatDetails,publishedAt)),"
                    + "nextPageToken,pollingIntervalMillis";

    private static YouTube youtube;

    public static void main(String args) {

        // This OAuth 2.0 access scope allows for read-only access to the
        // authenticated user's account, but not other types of account access.

        try {
            String liveChatId = main.plugin.getConfig().getString("youtube.youtubeId");
            NetHttpTransport transport = new NetHttpTransport.Builder().build();
            JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            HttpRequestInitializer httpRequestInitializer = request -> {
                request.setInterceptor(intercepted -> intercepted.getUrl().set("key", main.plugin.getConfig().getString("youtube.apiKey")));
            };

            youtube = new YouTube.Builder(transport, jsonFactory, httpRequestInitializer)
                    .setApplicationName("StreamerVsChat")
                    .build();

            liveChatId = youtube.videos().list("liveStreamingDetails").setId(liveChatId).execute().getItems().get(0).getLiveStreamingDetails().getActiveLiveChatId();
            listChatMessages(liveChatId, null, main.plugin.getConfig().getInt("commands.delay") * 1000);
        } catch (Throwable t) {
            main.plugin.getLogger().warning("[YouTube] Error: " + t.getMessage());
        }
    }
    public static TimerTask tt = null;
    /**
     * Lists live chat messages, polling at the server supplied interval. Owners and moderators of a
     * live chat will poll at a faster rate.
     *
     * @param liveChatId    The live chat id to list messages from.
     * @param nextPageToken The page token from the previous request, if any.
     * @param delayMs       The delay in milliseconds before making the request.
     */
    private static void listChatMessages(
            final String liveChatId,
            final String nextPageToken,
            long delayMs) {
        Timer pollTimer = new Timer();
        pollTimer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        tt = this;
                        try {
                            // Get chat messages from YouTube
                            LiveChatMessageListResponse response = youtube
                                    .liveChatMessages()
                                    .list(liveChatId, "snippet, authorDetails")
                                    .setPageToken(nextPageToken)
                                    .setFields(LIVE_CHAT_FIELDS)
                                    .execute();

                            // Display messages and super chat details
                            List<LiveChatMessage> messages = response.getItems();
                            for (int i = 0; i < messages.size(); i++) {
                                LiveChatMessage message = messages.get(i);
                                LiveChatMessageSnippet snippet = message.getSnippet();
                                System.out.println(buildOutput(
                                        snippet.getDisplayMessage(),
                                        message.getAuthorDetails(),
                                        snippet.getSuperChatDetails()));
                            }

                            // Request the next page of messages
                            listChatMessages(
                                    liveChatId,
                                    response.getNextPageToken(),
                                    response.getPollingIntervalMillis());
                        } catch (Throwable t) {
                            main.plugin.getLogger().warning("[YouTube] Error: " + t.getMessage());
                        }
                    }
                }, main.plugin.getConfig().getInt("commands.delay") * 1000);
    }

    /**
     * Formats a chat message for console output.
     *
     * @param message          The display message to output.
     * @param author           The author of the message.
     * @param superChatDetails SuperChat details associated with the message.
     * @return A formatted string for console output.
     */
    private static String buildOutput(
            String message,
            LiveChatMessageAuthorDetails author,
            LiveChatSuperChatDetails superChatDetails) {
        StringBuilder output = new StringBuilder();
        if (superChatDetails != null) {
            output.append(superChatDetails.getAmountDisplayString());
            output.append("SUPERCHAT RECEIVED FROM ");
            commands.runCmd(message.replace(main.plugin.getConfig().getString("commands.prefix"), ""));
        }
        output.append(author.getDisplayName());
        List<String> roles = new ArrayList<String>();
        if (author.getIsChatOwner()) {
            roles.add("OWNER");
        }
        if (author.getIsChatModerator()) {
            roles.add("MODERATOR");
        }
        if (author.getIsChatSponsor()) {
            roles.add("SPONSOR");
        }
        commands.UserList.add(author.getDisplayName());
        if (roles.size() > 0) {
            output.append(" (");
            String delim = "";
            for (String role : roles) {
                output.append(delim).append(role);
                delim = ", ";
            }
            output.append(")");
        }
        if (message != null && !message.isEmpty()) {
            output.append(": ");
            output.append(message);
        }
        if (message.contains(main.plugin.getConfig().getString("commands.prefix"))) {
            commands.commandList.add(message.replace(main.plugin.getConfig().getString("commands.prefix"), ""));
        }else{
            main.plugin.getServer().broadcastMessage(output.toString());
        }
        return output.toString();
    }

    public static void reload(){
        main.plugin.getLogger().info("[YouTube] Reloading...");
        main.plugin.reloadConfig();
        tt.cancel();
        main("run");
        main.plugin.getLogger().info("[YouTube] Reloaded!");
    }

    public static void stop(){
        main.plugin.getLogger().info("[YouTube] Stopping...");
        tt.cancel();
        main.plugin.getLogger().info("[YouTube] Stopped!");
    }
}