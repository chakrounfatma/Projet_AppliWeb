import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import Register from "./pages/Register";
import Login from "./pages/login";
import Profile from "./pages/Profile";
import Game from "./pages/Game";
import Historique from "./pages/Historique";
import Rules from "./pages/Rule";
function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login/>} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/game" element={<Game />} />
        <Route path="/historique" element={<Historique />} />
        <Route path="/rules" element={<Rules />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;