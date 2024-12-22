import React from "react";
import { Routes, Route, useParams } from "react-router-dom";
import HomePage from "../pages/HomePage";
import CreateGroup from "../components/CreateGroup";
import GroupList from "../components/GroupList";
import Chat from "../components/Chat";

function AppRoutes() {
    return (
        <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/create-group" element={<CreateGroup />} />
            <Route path="/public-groups" element={<GroupList />} />
            <Route path="/chat/:groupId" element={<ChatWrapper />} />
        </Routes>
    );
}

const ChatWrapper = () => {
    const { groupId } = useParams();
    return <Chat groupId={groupId} />;
};

export default AppRoutes;
