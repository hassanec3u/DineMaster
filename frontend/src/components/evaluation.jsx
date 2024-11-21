import {useLocation} from "react-router-dom";
import React, {useEffect} from "react";
import {Button, Container, ListGroup} from "react-bootstrap";
import {useQuery, useMutation, useQueryClient} from "react-query";

import {getEvaluations} from "../service/EvaluationService";


const Evaluation = () => {
    const location = useLocation();
    const restauId = location.state?.restauId || 0;


    const {isLoading, error, data: evaluations = [],} = useQuery(["evaluations", restauId], () => getEvaluations(restauId), {
        enabled: !!restauId,
    })

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


    console.log( "voici les evaluations",evaluations);


    const evaluations2 = [
        { note: 4, auteur: "Jean Dupont", commentaire: "Super service et plats délicieux !" },
        { note: 5, auteur: "Marie Curie", commentaire: "Expérience culinaire incroyable, bravo !" },
        { note: 3, auteur: "Albert Einstein", commentaire: "Sympathique mais un peu cher pour ce que c'est." },
    ];

    return (
        // Afficher les évaluations du restaurant
        <Container className="mt-4">
            <h1 className="text-center mb-4">Évaluations du restaurant</h1>
            <Button variant="primary" className="mt-4 mb-3" href="/ajouter-evaluation">Ajouter une évaluation</Button>
            <ListGroup>
                {evaluations.map((evaluation, index) => (
                    <ListGroup.Item key={index} className="mb-3">
                        <h5>Note : {evaluation.note}/5</h5>
                        <p className="text-muted">Auteur : {evaluation.auteur}</p>
                        <p>{evaluation.commentaire}</p>
                    </ListGroup.Item>
                ))}
            </ListGroup>
        </Container>
    );
};

export default Evaluation;