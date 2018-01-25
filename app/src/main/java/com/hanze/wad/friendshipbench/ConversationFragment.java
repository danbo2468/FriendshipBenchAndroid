/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.hanze.wad.friendshipbench.Models.User;
import java.net.URISyntaxException;

/**
 * Fragment controller for the chat page.
 */
public class ConversationFragment extends CustomFragment {

    private Socket socket;

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
        initializeSuper(R.layout.conversation_layout, true, inflater, container);
        return view;
    }

    /**
     * The initialization of the specific fragment.
     */
    protected void initializeFragment(){

        // Create a connection with the Node.JS server.
        initializeSocket();

        // Handle the button for sending a new message.
        view.findViewById(R.id.buttonSendMessage).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                User user = ((MainActivity)getActivity()).user;
                socket.emit("new message", user.getId(), user.getFullname(), ((EditText)view.findViewById(R.id.messageTextField)).getText().toString(), "13:02", user.getChatKey());
                showMyMessage(((EditText)view.findViewById(R.id.messageTextField)).getText().toString(), "13:02");
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
        SharedPreferences sharedPref = activity.getPreferences(context.MODE_PRIVATE);
        Gson gson = new Gson();
        User user = gson.fromJson(sharedPref.getString("user", ""), User.class);
        socket.emit("join room", user.getChatKey());
        socket.on("new message", handleNewMessage);
    }

    /**
     * Listen to new messages from the healthworker.
     */
    private Emitter.Listener handleNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
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
        ContextThemeWrapper layoutContext = new ContextThemeWrapper(getActivity().getBaseContext(), R.style.ChatMessage);
        LinearLayout chatMessage = new LinearLayout(layoutContext);

        // Set the text fields.
        TextView sender = new TextView(new ContextThemeWrapper(getActivity().getBaseContext(), R.style.OtherSender));
        sender.setText(name);
        TextView messageText = new TextView(new ContextThemeWrapper(getActivity().getBaseContext(), R.style.OtherMessage));
        messageText.setText(message);

        // Add everything to the chat.
        chatMessage.addView(sender);
        chatMessage.addView(messageText);
        ((LinearLayout)view.findViewById(R.id.chatLayout)).addView(chatMessage);
    }

    /**
     * Show the messages of yourself.
     * @param message The message.
     * @param time The time.
     */
    private void showMyMessage(String message, String time){

        // Create a new linearLabelValueLayout.
        ContextThemeWrapper layoutContext = new ContextThemeWrapper(getActivity().getBaseContext(), R.style.ChatMessage);
        LinearLayout chatMessage = new LinearLayout(layoutContext);

        // Set the text fields.
        TextView sender = new TextView(new ContextThemeWrapper(getActivity().getBaseContext(), R.style.OwnSender));
        sender.setText("You");
        TextView messageText = new TextView(new ContextThemeWrapper(getActivity().getBaseContext(), R.style.OwnMessage));
        messageText.setText(message);

        // Add everything to the chat.
        chatMessage.addView(sender);
        chatMessage.addView(messageText);
        ((LinearLayout)view.findViewById(R.id.chatLayout)).addView(chatMessage);
    }
}
