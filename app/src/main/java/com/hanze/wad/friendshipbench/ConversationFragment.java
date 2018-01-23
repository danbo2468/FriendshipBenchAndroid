/*
 * Copyright (c) 2018. Developed by the Hanzehogeschool Groningen for Friendship Bench Zimbabwe.
 */

package com.hanze.wad.friendshipbench;

import android.app.Fragment;
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
import java.net.URISyntaxException;

public class ConversationFragment extends Fragment {

    private View view;
    private String key = "privatekey123";
    private Socket socket;

    /**
     * This method is called when a view is opened.
     * @param inflater The inflater.
     * @param container The container.
     * @param savedInstanceState The saved instance state.
     * @return The view.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // Get the current view.
        view = inflater.inflate(R.layout.conversation_layout, container, false);
        initializeSocket();

        // Handle the button for sending a new message.
        view.findViewById(R.id.buttonSendMessage).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                socket.emit("new message", ((EditText)view.findViewById(R.id.messageTextField)).getText().toString(), key);
                showMessage(((EditText)view.findViewById(R.id.messageTextField)).getText().toString(), new ContextThemeWrapper(getActivity().getBaseContext(), R.style.OwnMessage));
                ((EditText)view.findViewById(R.id.messageTextField)).setText("");
            }
        });

        // Return the view.
        return view;
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
            Toast.makeText(getActivity().getBaseContext(), getResources().getString(R.string.error_message), Toast.LENGTH_LONG).show();
            return;
        }
        socket.connect();

        // Enter a room and set the handlers.
        socket.emit("join room", key);
        socket.on("server message", handleServerMessage);
        socket.on("new message", handleNewMessage);
    }

    /**
     * Listen to new messages from the server.
     */
    private Emitter.Listener handleServerMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showMessage(args[0].toString(), new ContextThemeWrapper(getActivity().getBaseContext(), R.style.ServerMessage));
                }
            });
        }
    };

    /**
     * Listen to new messages from the healthworker.
     */
    private Emitter.Listener handleNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showMessage(args[0].toString(), new ContextThemeWrapper(getActivity().getBaseContext(), R.style.OtherMessage));
                }
            });
        }
    };

    /**
     * Showing a new message.
     * @param message The message information.
     * @param theme The style for the message.
     */
    private void showMessage(String message, ContextThemeWrapper theme){
        TextView label = new TextView(theme);
        label.setText(message);
        ((LinearLayout)view.findViewById(R.id.chatLayout)).addView(label);
    }
}
