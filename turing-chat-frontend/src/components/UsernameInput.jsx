import React, { useState, useEffect } from "react";

const UsernameInput = ({ onUsernameSubmitted }) => {
    const [username, setUsername] = useState("");

    useEffect(() => {
        // Gera um nome de usuário padrão quando o componente é montado
        const defaultUsername = `user${Math.floor(1000 + Math.random() * 9000)}`;
        setUsername(defaultUsername);
    }, []);

    const handleEnterChat = () => {
        if (username.trim()) {
            onUsernameSubmitted(username);
        } else {
            alert("Por favor, insira um nome de usuário.");
        }
    };

    return (
        <div className="flex flex-col items-center">
            <input
                type="text"
                placeholder="Digite seu nome de usuário"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                className="w-full px-4 py-2 mb-4 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            />
            <button
                onClick={handleEnterChat}
                className="w-full px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition duration-300 shadow-lg transform hover:scale-105"
            >
                Entrar no Chat
            </button>
        </div>
    );
};

export default UsernameInput;
