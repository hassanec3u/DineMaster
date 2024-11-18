import React, { useState } from "react";
import { useQuery, useMutation, useQueryClient } from "react-query";
import {addRestaurant, deleteRestaurant,getRestaurants} from "../service/restaureantService";
import { Modal, Button, Form } from "react-bootstrap"; // Importation de React Bootstrap

const Restaurants = () => {
    const [showModal, setShowModal] = useState(false);
    const [newRestaurant, setNewRestaurant] = useState({ nom: "", adresse: "" });
    const queryClient = useQueryClient();
    const { isLoading, error, data } = useQuery("restaurantData", getRestaurants);

    const addMutation = useMutation(addRestaurant, {
        onSuccess: () => {
            queryClient.invalidateQueries("restaurantData");
        }
    });

    const deleteMutation = useMutation(deleteRestaurant, {
        onSuccess: () => {
            queryClient.invalidateQueries("restaurantData");
        }
    });

    const handleSubmit = (e) => {
        e.preventDefault();
        addMutation.mutate(newRestaurant);
        setShowModal(false);
    };


    const handleDelete = (id) => {
    deleteMutation.mutate(id);
    }

    if (isLoading) {
        return (
            <div className="container d-flex flex-column align-items-center justify-content-center min-vh-100">
                <div className="spinner-border text-primary mb-3" role="status">
                    <span className="visually-hidden">Chargement...</span>
                </div>
                <p className="text-muted">Chargement des restaurants, veuillez patienter...</p>
            </div>
        );
    }



    if (error) {
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
    }

    return (

        <div className="container mt-5">
            <h1 className="text-center mb-5 fw-bold text-primary">Nos Restaurants</h1>
            <div className="row g-4">
                {/* Bouton pour afficher la modal */}
                <div className="d-flex justify-content-center mt-4">
                    <Button variant="primary" onClick={() => setShowModal(true)}>
                        Ajouter un restaurant
                    </Button>
                </div>
                {data.map((restaurant) => (
                    <div className="col-lg-4 col-md-6" key={restaurant.id}>
                        <div className="card h-100 border-0 shadow-sm rounded-3">
                            <div className="card-body d-flex flex-column">
                                <h5 className="card-title text-primary">{restaurant.nom}</h5>
                                <p className="card-text text-muted mb-3">
                                    <strong>Adresse :</strong> {restaurant.adresse}
                                </p>
                                <p className="card-text mb-4">
                                    <strong>Note Moyenne :</strong>{" "}
                                    <span className="text-warning">{restaurant.moyenne}/5</span>
                                </p>
                                <button className="btn btn-outline-primary mt-auto">
                                    Voir le restaurant
                                </button>

                                {/* Bouton Supprimer */}
                                <Button
                                    variant="danger"
                                    className="mt-2"
                                    onClick={() => handleDelete(restaurant.id)}
                                    disabled={deleteRestaurant.isLoading}
                                >
                                    {deleteRestaurant.isLoading
                                        ? "Suppression en cours..."
                                        : "Supprimer"}
                                </Button>
                            </div>
                        </div>
                    </div>
                ))}
            </div>



            <Modal show={showModal} onHide={() => setShowModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Ajouter un restaurant</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form onSubmit={handleSubmit}>
                        <Form.Group controlId="formRestaurantName">
                            <Form.Label>Nom</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Entrez le nom du restaurant"
                                value={newRestaurant.nom}
                                onChange={(e) => setNewRestaurant({ ...newRestaurant, nom: e.target.value })}
                            />
                        </Form.Group>
                        <Form.Group controlId="formRestaurantAddress" className="mt-3">
                            <Form.Label>Adresse</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Entrez l'adresse du restaurant"
                                value={newRestaurant.adresse}
                                onChange={(e) => setNewRestaurant({ ...newRestaurant, adresse: e.target.value })}
                            />
                        </Form.Group>
                        <Modal.Footer>
                            <Button variant="secondary" onClick={() => setShowModal(false)}>
                                Fermer
                            </Button>
                            <Button variant="primary" type="submit" disabled={addMutation.isLoading}>
                                {addMutation.isLoading ? "Ajout en cours..." : "Ajouter"}
                            </Button>
                        </Modal.Footer>
                    </Form>
                </Modal.Body>
            </Modal>
        </div>
    );
};


export default Restaurants;
