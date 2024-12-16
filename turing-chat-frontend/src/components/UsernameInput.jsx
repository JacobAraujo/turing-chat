import React, { useState } from "react";
import Chat from "./Chat";

const UsernameInput = ({ groupId }) => {
    const [username, setUsername] = useState("");
    const [isChatVisible, setIsChatVisible] = useState(false);

    const handleEnterChat = () => {
        if (username.trim()) {
            setIsChatVisible(true); // Exibe o chat após o username ser definido
        } else {
            alert("Por favor, insira um nome de usuário.");
        }
    };

    return (
        <div>
            {!isChatVisible ? (
                <div style={{ textAlign: "center", marginTop: "20px" }}>
                    <h2>Bem-vindo ao Chat</h2>
                    <input
                        type="text"
                        placeholder="Digite seu nome de usuário"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        style={{
                            padding: "10px",
                            fontSize: "16px",
                            borderRadius: "5px",
                            border: "1px solid #ccc",
                            marginRight: "10px",
                        }}
                    />
                    <button
                        onClick={handleEnterChat}
                        style={{
                            padding: "10px 15px",
                            fontSize: "16px",
                            borderRadius: "5px",
                            backgroundColor: "#007BFF",
                            color: "white",
                            border: "none",
                            cursor: "pointer",
                        }}
                    >
                        Entrar no Chat
                    </button>
                </div>
            ) : (
                <Chat groupId={groupId} username={username} />
            )}
        </div>
    );
};

export default UsernameInput;
