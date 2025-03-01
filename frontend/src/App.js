import './App.css';
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { QueryClient, QueryClientProvider } from 'react-query'; // Importer QueryClient et QueryClientProvider
import Restaurants from "./components/restaurant";
import Evaluations from "./components/evaluation";
import 'bootstrap/dist/css/bootstrap.min.css';


const queryClient = new QueryClient();

function App() {
    return (
        <QueryClientProvider client={queryClient}> {}
            <Router>
                <Routes>
                    <Route path="/" element={<Restaurants />} />
                    <Route path="/evaluations" element={<Evaluations/>} />
                </Routes>
            </Router>
        </QueryClientProvider>
    );
}

export default App;
