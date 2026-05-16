import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import "./Profile.css";

function Profile() {
  const [user, setUser] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const storedUser = localStorage.getItem("user");
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    } else {
      navigate("/login");
    }
  }, []);

  if (!user) return null;

  return (
    <div className="profile">
  <div className="profile-card">
    <h1>Profil joueur</h1>

    <p><b>Prénom :</b> {user.prenom}</p>
    <p><b>Email :</b> {user.email}</p>
    <p><b>Score :</b> {user.scoreTotal}</p>

    <button onClick={() => navigate("/")}>
      Retour Home
    </button>
  </div>
</div>
  );
}

export default Profile;