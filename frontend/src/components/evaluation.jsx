import {useLocation} from "react-router-dom";
import React, {useState} from "react";
import {Button, Container, ListGroup, Form, Modal, Row} from "react-bootstrap";
import {useMutation, useQuery, useQueryClient} from "react-query";
import {getEvaluations, deleteEvaluation, addEvaluation} from "../service/EvaluationService";


const Evaluation = () => {

    // queryClient est un objet qui permet de gérer les requêtes
    const queryClient = useQueryClient();

    // Récupérer l'identifiant du restaurant depuis la location
    const location = useLocation();
    const restauId = location.state?.restauId || 0;

    // État local pour gérer les nouvelles évaluations
    const [newEvaluation, setNewEvaluation] = useState({RestauId: restauId,note: 0, auteur: "", commentaire: ""});

    // Mutation pour ajouter une évaluation
    const [showModal, setShowModal] = useState(false);

    // Récupérer les évaluations du restaurant
    const {isLoading, error, data: evaluations} = useQuery("evaluationsData", () => getEvaluations(restauId));

    const addMutation = useMutation(addEvaluation, {
        onSuccess: () => {
            queryClient.invalidateQueries("evaluationsData");
        }
    });




    const deleteMutation = useMutation(
        (params) => deleteEvaluation(params.restauId, params.evalId),
        {
            onSuccess: () => {
                queryClient.invalidateQueries("evaluationsData");
            },
            onError: (error) => {
                console.error("Erreur lors de la suppression de l'évaluation : ", error);
            }
        }
    );


    const handleSubmitEvaluation = (e) => {
        e.preventDefault();

        //ajouter du restaurantId dans l'objet newEvaluation
        addMutation.mutate(newEvaluation);
        setShowModal(false);
    }

    const handleDeleteEvaluation = (id) => {
        // Supprimer l'évaluation
        deleteMutation.mutate({ restauId, evalId: id });
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

            <Container className="mt-4">
                <h1 className="text-center mb-4">Évaluations du restaurant</h1>

                {/* Bouton pour ajouter une évaluation */}
                <Button
                    variant="primary"
                    className="mt-4 mb-3"
                    onClick={() => setShowModal(true)}  // Cette fonction doit ouvrir un modal ou rediriger vers la page d'ajout
                >
                    Ajouter une évaluation
                </Button>

                <ListGroup>
                    {evaluations.map((evaluation, index) => (
                        <ListGroup.Item key={index} className="mb-3">
                            <h5>Note : {evaluation.note}/5</h5>
                            <p className="text-muted">Auteur : {evaluation.auteur}</p>
                            <p>{evaluation.commentaire}</p>

                            {/* Bouton pour supprimer l'évaluation */}
                            <Button
                                variant="danger"
                                size="sm"
                                onClick={() => handleDeleteEvaluation(evaluation.id)}
                            >
                                Supprimer
                            </Button>
                        </ListGroup.Item>
                    ))}
                </ListGroup>
            </Container>
            <Modal show={showModal} onHide={() => setShowModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Ajouter une évaluation</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form onSubmit={handleSubmitEvaluation}>
                        <Form.Group controlId="formEvaluationNote">
                            <Form.Label>Note</Form.Label>
                            <Form.Control
                                as="select"
                                value={newEvaluation.note}
                                onChange={(e) => setNewEvaluation({...newEvaluation, note: Number(e.target.value)})}
                            >
                                <option value={0}>Sélectionner une note</option>
                                <option value={1}>1</option>
                                <option value={2}>2</option>
                                <option value={3}>3</option>
                                <option value={4}>4</option>
                                <option value={5}>5</option>
                            </Form.Control>
                        </Form.Group>

                        <Form.Group controlId="formEvaluationAuteur" className="mt-3">
                            <Form.Label>Auteur</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Entrez votre nom"
                                value={newEvaluation.auteur}
                                onChange={(e) =>
                                    setNewEvaluation({...newEvaluation, auteur: e.target.value})
                                }
                            />
                        </Form.Group>

                        <Form.Group controlId="formEvaluationCommentaire" className="mt-3">
                            <Form.Label>Commentaire</Form.Label>
                            <Form.Control
                                as="textarea"
                                rows={3}
                                placeholder="Entrez votre commentaire"
                                value={newEvaluation.commentaire}
                                onChange={(e) =>
                                    setNewEvaluation({...newEvaluation, commentaire: e.target.value})
                                }
                            />
                        </Form.Group>

                        <Modal.Footer>
                            <Button variant="secondary" onClick={() => setShowModal(false)}>
                                Fermer
                            </Button>
                            <Button variant="primary" type="submit" disabled={addEvaluation.isLoading}>
                                {addEvaluation.isLoading ? "Ajout en cours..." : "Ajouter l'évaluation"}
                            </Button>
                        </Modal.Footer>
                    </Form>
                </Modal.Body>
            </Modal>
        </>
    );
};

export default Evaluation;