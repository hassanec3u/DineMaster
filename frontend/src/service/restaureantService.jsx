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