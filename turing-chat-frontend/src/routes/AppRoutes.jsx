import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Chat from '../components/Chat'
import UsernameInput from '../components/UsernameInput';

function AppRoutes() {

  return (
    <Routes>
      <Route
        path="/"
        element={
          <UsernameInput groupId={123}/>
        }
      />
    </Routes>
  );
}

export default AppRoutes;