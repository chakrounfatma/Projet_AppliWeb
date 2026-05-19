import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import "./Profile.css";

function Profile() {
  const [user, setUser] = useState(null);
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [message, setMessage] = useState("");

  const [messages, setMessages] = useState([]);

  useEffect(() => {
    const storedUser = localStorage.getItem("user");
    if (storedUser) {
      const parsed = JSON.parse(storedUser);
      setUser(parsed);
      
    } else {
      navigate("/login");
    }
  }, []);

  useEffect(() => {
  if (user?.email) {
    fetch(`http://localhost:8083/chat/messages?email=${user.email}`)
      .then(res => res.json())
      .then(data => {
  console.log("MESSAGES RAW:", data);
  setMessages(data);
})
      .catch(err => console.log(err));
  }
}, [user]);

  if (!user) return null;

  return (
  <div className="profile">

    <div className="profile-container">

      {/* CARTE PROFIL (GAUCHE) */}
      <div className="profile-card">
        <h1>Profil joueur</h1>

        <p><b>Prénom :</b> {user.prenom}</p>
        <p><b>Email :</b> {user.email}</p>
        <p><b>Score :</b> {user.scoreTotal}</p>

        <button onClick={() => navigate("/")}>
          Retour Home
        </button>
      </div>

      {/* CARTE MESSAGERIE (DROITE) */}
      <div className="profile-card">

        <h1>Messagerie</h1>

        <div className="chat-box">

          <input
            type="email"
            placeholder="Email destinataire"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />

          <textarea
            placeholder="Votre message..."
            value={message}
            onChange={(e) => setMessage(e.target.value)}
          />

          <button
            onClick={async () => {
              try {
                await fetch(
                  `http://localhost:8083/chat/send?senderEmail=${user.email}&email=${email}&message=${message}`,
                  { method: "POST" }
                );

                alert("Message envoyé !");
                setEmail("");
                setMessage("");
              } catch (err) {
                console.log(err);
              }
            }}
          >
            Envoyer
          </button>
        </div>

        <div className="messages-list">
          <h1>Mes messages</h1>

          {Array.isArray(messages) && messages.length === 0 ? (
            <p>Aucun message</p>
          ) : (
            messages.map((msg) => (
              <div key={msg.id} className="message-card">
                <p><b>De :</b> {msg.expediteur?.email}</p>
                <p>{msg.message}</p>
                <p className="date">{msg.date}</p>
              </div>
            ))
          )}
        </div>

      </div>

    </div>
  </div>
);
}

export default Profile;