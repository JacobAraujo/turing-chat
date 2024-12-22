import React, { useState } from "react";

const CreateGroup = ({ onGroupCreated }) => {
    const [groupName, setGroupName] = useState("");
    const [isPublic, setIsPublic] = useState(false);

    const handleCreateGroup = async () => {
        if (!groupName.trim()) {
            alert("Por favor, insira um nome para o grupo.");
            return;
        }

        try {
            const response = await fetch("http://localhost:8080/api/chat/groups", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    chatName: groupName,
                    chatType: isPublic,
                }), 
            });

            const data = await response.json();
            onGroupCreated(data);
        } catch (error) {
            console.error("Erro ao criar grupo:", error);
        }
    };

    return (
        <div className="flex flex-col items-center min-h-screen bg-gray-100 p-6">
            <h2 className="text-3xl font-bold text-gray-700 mb-6">Criar Novo Grupo</h2>
            <div className="w-full max-w-md bg-white rounded-lg shadow-lg p-6">
                <input
                    type="text"
                    placeholder="Nome do grupo"
                    value={groupName}
                    onChange={(e) => setGroupName(e.target.value)}
                    className="w-full px-4 py-2 mb-4 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-green-500"
                />
                <div className="flex items-center mb-4">
                    <input
                        type="checkbox"
                        checked={isPublic}
                        onChange={(e) => setIsPublic(e.target.checked)}
                        className="mr-2"
                    />
                    <label className="text-gray-600">Grupo público</label>
                </div>
                <button
                    onClick={handleCreateGroup}
                    className="w-full px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition duration-300 shadow-lg transform hover:scale-105"
                >
                    Criar Grupo
                </button>
            </div>
        </div>
    );
};

export default CreateGroup;
