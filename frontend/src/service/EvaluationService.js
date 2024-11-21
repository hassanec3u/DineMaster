import axios from 'axios';

const EvaluationService = axios.create({
    baseURL: 'http://localhost:8080/restaurants',
});

export const addEvaluation = async (newEvaluation) => {
    const response = await EvaluationService.post('', newEvaluation);
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

