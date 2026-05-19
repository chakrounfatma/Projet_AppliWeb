// import Card from "./components/card/Card";
// import CardRow from "./components/cardRow/CardRow";
// import CardColumn from "./components/cardColumn/CardColumn";
// import CardColumn2 from "./components/cardColumn2/CardColumn2";
import "./App.css";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./pages/Home.tsx";
import Register from "./pages/Register.tsx";
import Login from "./pages/login.tsx";
import Profile from "./pages/Profile.tsx";
import Game from "./pages/Game.tsx";
// import DrawingDeck from "./components/drawingDeck/DrawingDeck";

function App() {
  return (
    // <div className="app">
    //   <CardRow cards={myRow}></CardRow>
    //   <CardColumn cards={myRow}></CardColumn>
    //   <CardColumn2 cards={myRow}></CardColumn2>
    //   <DrawingDeck></DrawingDeck>
    // </div>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/game" element={<Game />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
