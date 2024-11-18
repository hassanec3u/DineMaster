import React from "react";
import { useQuery } from "react-query";
import { getRestaurants } from "../service/restaureantService";

const Restaurants = () => {
    const { isLoading, error, data } = useQuery("restaurantData", getRestaurants);

    if (isLoading)
        return (
            <div className="container d-flex flex-column align-items-center justify-content-center min-vh-100">
                <div className="spinner-border text-primary mb-3" role="status">
                    <span className="visually-hidden">Chargement...</span>
                </div>
                <p className="text-muted">Chargement des restaurants, veuillez patienter...</p>
            </div>
        );

    if (error)
        return (
            <div className="container d-flex flex-column align-items-center justify-content-center min-vh-100">
                <div className="alert alert-danger text-center" role="alert">
                    <h4 className="alert-heading">Erreur</h4>
                    <p>Une erreur est survenue : {error.message}</p>
                    <hr />
                    <p className="mb-0">Veuillez réessayer plus tard.</p>
                </div>
            </div>
        );

    return (
        <div className="container mt-5">
            <h1 className="text-center mb-5 fw-bold text-primary">Nos Restaurants</h1>
            <div className="row g-4">
                {data.map((restaurant) => (
                    <div className="col-lg-4 col-md-6" key={restaurant.id}>
                        <div className="card h-100 border-0 shadow-sm rounded-3">
                            <div className="card-body d-flex flex-column">
                                <h5 className="card-title text-primary">{restaurant.nom}</h5>
                                <p className="card-text text-muted mb-3">
                                    <strong>Adresse :</strong> {restaurant.adresse}
                                </p>
                                <p className="card-text mb-4">
                                    <strong>Note Moyenne :</strong> <span className="text-warning">{restaurant.moyenne}/5</span>
                                </p>
                                <button className="btn btn-outline-primary mt-auto">
                                    Voir le restaurant
                                </button>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Restaurants;
