import React, {useState} from "react";
import {useQuery, useMutation, useQueryClient} from "react-query";
import {addRestaurant, deleteRestaurant, getRestaurants} from "../service/restaureantService";

import {Button, Card, Col, Container, Form, Modal, Row} from "react-bootstrap";
import {redirect, useNavigate} from "react-router-dom";

const Restaurants = () => {
    const [showModal, setShowModal] = useState(false);

    const [newRestaurant, setNewRestaurant] = useState({nom: "", adresse: ""});
    const queryClient = useQueryClient();
    const {isLoading, error, data: restaurants} = useQuery("restaurantData", getRestaurants);

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

    const navigate = useNavigate();
    const redirectToEvalutation = (restauId) => {

        navigate('/evaluations',{state: {restauId}});
    }

    //Condition pour afficher le chargement des restaurants
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
                    <hr/>
                    <p className="mb-0">Veuillez réessayer plus tard.</p>
                </div>
            </div>
        );
    }

    return (
        <>
            <Container className="mt-5">
                <h1 className="text-center mb-5 fw-bold text-primary">Nos Restaurants</h1>
                <Row className="g-4">
                    {/* Bouton pour afficher la modal */}
                    <div className="d-flex justify-content-center mt-4">
                        <Button variant="primary" onClick={() => setShowModal(true)}>
                            Ajouter un restaurant
                        </Button>
                    </div>

                    {/* Affichage des restaurants */}
                    {restaurants.map((restaurant) => (
                        <Col lg={4} md={6} key={restaurant.id}>
                            <Card className="h-100 shadow-sm rounded-3 border-0">
                                <Card.Body className="d-flex flex-column">
                                    <Card.Title className="text-primary">{restaurant.nom}</Card.Title>
                                    <Card.Text className="text-muted mb-3">
                                        <strong>Adresse :</strong> {restaurant.adresse}
                                    </Card.Text>
                                    <Card.Text className="mb-4">
                                        <strong>Note Moyenne :</strong>{" "}
                                        <span className="text-warning">
                                            {restaurant.moyenne === -1
                                                ? "Aucune note"
                                                : `${restaurant.moyenne}/5`}
                                        </span>
                                    </Card.Text>
                                    <Button variant="outline-primary" className="mt-auto"
                                            onClick={() => redirectToEvalutation(restaurant.id)}>
                                        Evaluation
                                    </Button>

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
                                </Card.Body>
                            </Card>
                        </Col>
                    ))}
                </Row>
            </Container>

            {/* Modal */}
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
                                onChange={(e) =>
                                    setNewRestaurant({...newRestaurant, nom: e.target.value})
                                }
                            />
                        </Form.Group>
                        <Form.Group controlId="formRestaurantAddress" className="mt-3">
                            <Form.Label>Adresse</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Entrez l'adresse du restaurant"
                                value={newRestaurant.adresse}
                                onChange={(e) =>
                                    setNewRestaurant({...newRestaurant, adresse: e.target.value})
                                }
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
        </>
    );
};


export default Restaurants;
