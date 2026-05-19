import axios from "axios";

// create a service to handle game related API calls

const API = "http://localhost:8083/game";

export const createGame = (userIds: number[]) => {
  return axios.post(`${API}/create`, userIds);
};

export const getHand = (joueurId: number, partieId: number) => {
  return axios.get(`${API}/hand/${joueurId}/${partieId}`);
};

export const getTopCard = (partieId: number) => {
  return axios.get(`${API}/top-card/${partieId}`);
};

export const playCard = (
  joueurId: number,
  carteId: number,
  couleur: string | null = null,
) => {
  let url = `${API}/play?joueurId=${joueurId}&carteId=${carteId}`;

  if (couleur !== null) {
    url += `&couleur=${couleur}`;
  }

  return axios.post(url);
};

export const getGameState = (partieId: number) => {
  return axios.get(`${API}/state/${partieId}`);
};
