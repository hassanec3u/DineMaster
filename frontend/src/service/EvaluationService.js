import axios from 'axios';

const EvaluationService = axios.create({
    baseURL: 'http://localhost:8080/restaurants',
});


export const addEvaluation = async (newEvaluation) => {
    const response = await EvaluationService.post(`/${newEvaluation.restauId}/evaluations`, newEvaluation);

    if (response) {
        console.log('evaluation ajouté:', response.data);
        return response.data;
    }
}

export const getEvaluations = async (id) => {
    const response = await EvaluationService.get(`/${id}/evaluations`);
    if (response) {
        console.log('Voici les évaluations :', response.data);
        return response.data;
    }
};


// Exemple de fonction pour supprimer une évaluation
export const deleteEvaluation = async (restauId, evalId) => {
    const response = await EvaluationService.delete(`/${restauId}/evaluations/${evalId}`);
    if (response) {
        console.log('Évaluation supprimée:', response.data);
        return response.data; // Retourne la réponse de l'API pour que la mutation puisse l'utiliser
    }
    throw new Error("La suppression de l'évaluation a échoué");
};

