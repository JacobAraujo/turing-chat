import React from "react";
import { useNavigate } from "react-router-dom";
import UsernameInput from "../components/UsernameInput";

const HomePage = () => {
    const navigate = useNavigate();

    const handleUsernameSubmission = (username) => {
        navigate(`/chat`, { state: { username } });
    };

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gradient-to-b from-blue-500 to-blue-700 text-white">
            <h1 className="text-4xl font-bold mb-6">Bem-vindo ao Chat</h1>
            <div className="w-full max-w-md bg-white rounded-lg shadow-lg p-6 text-gray-800">
                <UsernameInput onUsernameSubmitted={handleUsernameSubmission} />
            </div>
            <div className="mt-10 flex space-x-4">
                <button
                    onClick={() => navigate("/create-group")}
                    className="px-6 py-3 bg-green-600 text-white rounded-lg hover:bg-green-700 transition duration-300 shadow-lg transform hover:scale-105"
                >
                    Criar Grupo
                </button>
                <button
                    onClick={() => navigate("/public-groups")}
                    className="px-6 py-3 bg-gray-700 text-white rounded-lg hover:bg-gray-800 transition duration-300 shadow-lg transform hover:scale-105"
                >
                    Ver Grupos Públicos
                </button>
            </div>
        </div>
    );
};

export default HomePage;
