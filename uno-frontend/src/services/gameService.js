import axios from "axios";

// create a service to handle game related API calls

const API = "http://localhost:8083/game";

export const createGame = (userIds) =>
    axios.post(`${API}/create`, userIds);

export const getHand = (joueurId, partieId) =>
    axios.get(`${API}/hand/${joueurId}/${partieId}`);

export const getTopCard = (partieId) =>
    axios.get(`${API}/top/${partieId}`);

export const playCard = (joueurId, carteId, couleur = null) => {
    let url = `${API}/play?joueurId=${joueurId}&carteId=${carteId}`;

    if (couleur !== null) {
        url += `&couleur=${couleur}`;
    }

    return axios.post(url);
};
export const drawCard = async (joueurId, partieId) => {
    const response = await axios.post(
        `${API}/draw?joueurId=${joueurId}&partieId=${partieId}`
    );

    return response.data;
};
export const getRandomPlayer = () =>
    axios.get(`${API}/random-player`);


export const getGameState = (partieId) =>
    axios.get(`${API}/state/${partieId}`);