/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.hanze.wad.friendshipbench.Controllers.ApiController;
import com.hanze.wad.friendshipbench.Controllers.VolleyCallback;
import com.hanze.wad.friendshipbench.Models.Client;
import com.hanze.wad.friendshipbench.Models.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Fragment controller for the chat page.
 */
public class ConversationFragment extends CustomFragment {

    private Socket socket;
    private ArrayList<Message> messages = new ArrayList<>();

    /**
     * The OnCreateView method which will be called first.
     * @param inflater The inflater.
     * @param container The container.
     * @param savedInstanceState The saved instance state.
     * @return The created view.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        initializeSuper(R.layout.conversation_layout, true, null, inflater, container);
        return view;
    }

    /**
     * The initialization of the specific fragment.
     */
    protected void initializeFragment(){

        // Check if the user already has somebody to chat with.
        if(!activity.user.hasHealthworker()){
            Toast.makeText(context, getResources().getString(R.string.need_healthworker), Toast.LENGTH_LONG).show();
            switchFragment(new ChooseHealthworkerFragment(), true);
            return;
        }

        // Get the message history.
        fetchMessageHistory();

        // Create a connection with the Node.JS server.
        initializeSocket();

        // Handle the button for sending a new message.
        view.findViewById(R.id.buttonSendMessage).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Client user = activity.user;
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentTime = df.format(new Date());
                socket.emit("new message", user.getId(), user.getFullname(), ((EditText)view.findViewById(R.id.messageTextField)).getText().toString(), currentTime, user.getChatKey());
                showMyMessage(((EditText)view.findViewById(R.id.messageTextField)).getText().toString(), currentTime);
                ((EditText)view.findViewById(R.id.messageTextField)).setText("");
            }
        });
    }

    /**
     * Initialize the chat connection.
     */
    private void initializeSocket(){

        // Try to connect.
        try {
            socket = IO.socket(getResources().getString(R.string.chat_url));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Toast.makeText(context, getResources().getString(R.string.error_message), Toast.LENGTH_LONG).show();
            return;
        }
        socket.connect();

        // Enter a room and set the handlers.
        Client user = ((MainActivity)getActivity()).user;
        socket.emit("join room", user.getChatKey());
        socket.on("new message", handleNewMessage);
    }

    /**
     * Listen to new messages from the healthworker.
     */
    private Emitter.Listener handleNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showOtherMessage(args[0].toString(), args[1].toString(), args[2].toString(), args[3].toString());
                }
            });
        }
    };

    /**
     * Show the messages of your healthworker.
     * @param user The person's ID.
     * @param name The person's name.
     * @param message The message.
     * @param time The time.
     */
    private void showOtherMessage(String user, String name, String message, String time){

        // Create a new linearLabelValueLayout.
        ContextThemeWrapper layoutContext = new ContextThemeWrapper(context, R.style.ChatMessage);
        LinearLayout chatMessage = new LinearLayout(layoutContext);

        // Set the text fields.
        TextView sender = new TextView(new ContextThemeWrapper(context, R.style.OtherSender));
        sender.setText(name);
        TextView messageText = new TextView(new ContextThemeWrapper(context, R.style.OtherMessage));
        messageText.setText(message);

        // Add everything to the chat.
        chatMessage.addView(sender);
        chatMessage.addView(messageText);
        ((LinearLayout)view.findViewById(R.id.chatLayout)).addView(chatMessage);
        scrollDown();
    }

    private void fetchMessageHistory() {

        // Make an API GET request.
        ApiController.getInstance(context).apiRequest(getResources().getString(R.string.message_history_url) + "/" + activity.user.getChatKey(), Request.Method.GET, null, activity.token.getAccessToken(), new VolleyCallback(){
            @Override
            public void onSuccess(String result){
                try {
                    jsonToMessages(new JSONArray(result));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(VolleyError result){
                Toast.makeText(context, getResources().getString(R.string.error_message), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void jsonToMessages(JSONArray json){

        // Loop through the list.
        for (int i = 0; i < json.length(); i++) {
            JSONObject messageJson = null;
            try {
                messageJson = json.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            messages.add(new Message(messageJson));
        }

        // Show the messages on the screen.
        showHistory();
        ((ScrollView)view.findViewById(R.id.scrollView)).fullScroll(View.FOCUS_DOWN);
    }

    private void showHistory(){
        for(Message message : messages){
            if(message.getUserId().equals(activity.user.getId()))
                showMyMessage(message.getMessage(), message.getFancyTime());
            else
                showOtherMessage(message.getUserId(), message.getUserName(), message.getMessage(), message.getFancyTime());
        }
    }

    /**
     * Show the messages of yourself.
     * @param message The message.
     * @param time The time.
     */
    private void showMyMessage(String message, String time){

        // Create a new linearLabelValueLayout.
        ContextThemeWrapper layoutContext = new ContextThemeWrapper(context, R.style.ChatMessage);
        LinearLayout chatMessage = new LinearLayout(layoutContext);

        // Set the text fields.
        TextView sender = new TextView(new ContextThemeWrapper(context, R.style.OwnSender));
        sender.setText("You");
        TextView messageText = new TextView(new ContextThemeWrapper(context, R.style.OwnMessage));
        messageText.setText(message);

        // Add everything to the chat.
        chatMessage.addView(sender);
        chatMessage.addView(messageText);
        ((LinearLayout)view.findViewById(R.id.chatLayout)).addView(chatMessage);
        scrollDown();
    }

    /**
     * Scroll to the bottom of the ScrollView.
     */
    private void scrollDown(){
        final ScrollView scrollLayout = view.findViewById(R.id.scrollView);
        scrollLayout.post(new Runnable() {
            @Override
            public void run() {
                scrollLayout.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    public void closeConnection(){
        socket.close();
    }
}
