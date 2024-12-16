import { useEffect, useState, useRef } from "react";
import { Client } from "@stomp/stompjs";

const ChatComponent = ({ groupId, username }) => {
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState("");
    const client = useRef(null);

    useEffect(() => {
        client.current = new Client();

        client.current.configure({
            brokerURL: "ws://localhost:8080/ws", 
            onConnect: () => {
                console.log("Connected to WebSocket");

                client.current.subscribe(`/topic/group/${groupId}`, (message) => {
                    const receivedMessage = JSON.parse(message.body);
                    setMessages((prev) => [...prev, receivedMessage]);
                });
            },
            onStompError: (frame) => {
                console.error("Broker reported error: " + frame.headers["message"]);
                console.error("Additional details: " + frame.body);
            },
        });

        client.current.activate();

        return () => {
            client.current.deactivate();
        };
    }, [groupId]);

    const sendMessage = () => {
        if (!newMessage.trim()) return;

        try {
            const message = {
                content: newMessage.trim(),
                sender: username, // TODO colocar o user
            };

            client.current.publish({
                destination: `/app/sendMessage/${groupId}`, 
                body: JSON.stringify(message),
            });

            setNewMessage("");
        } catch (error) {
            console.error("Failed to send message:", error);
        }
    };

    return (
        <div>
            <ul style={{ maxHeight: "300px", overflowY: "auto" }}>
                {messages.map((msg, index) => (
                    <li key={index}>
                        <b>{msg.sender}</b>: {msg.content}
                    </li>
                ))}
            </ul>
            <input
                type="text"
                value={newMessage}
                onChange={(e) => setNewMessage(e.target.value)}
                onKeyDown={(e) => {
                    if (e.key === "Enter") sendMessage();
                }}
            />
            <button onClick={sendMessage}>Send</button>
        </div>
    );
};

export default ChatComponent;
