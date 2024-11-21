import axios from 'axios';


const RestaurantService = axios.create({
    baseURL: 'http://localhost:8080/restaurants',
});

export const getRestaurants = async () => {
    const response = await RestaurantService.get('');
    if (response) {
        console.log('voici les restaurants:', response.data);
        return response.data;
    }
}

export const addRestaurant = async (newRestaurant) => {
    const response = await RestaurantService.post('', newRestaurant);
    if (response) {
        console.log('restaurant ajouté:', response.data);
        return response.data;
    }
}

export const deleteRestaurant = async (id) => {
    const response = await RestaurantService.delete(`/${id}`);
    if (response) {
        console.log('restaurant supprimé:', response.data);
        return response.data;
    }
}
