import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import './App.css';
import {MultiToolApp} from './components/MultiToolApp';
import {ThemeProvider} from './contexts/ThemeContext';


function App()
{
    return (
        <ThemeProvider>
            <Router>
                <Routes>
                    <Route path="/*" element={<MultiToolApp/>}/>
                </Routes>
            </Router>
        </ThemeProvider>
    );
}

export default App;