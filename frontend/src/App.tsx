import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import './App.css';
import {MultiToolApp} from './components/MultiToolApp';
import {ThemeProvider} from './contexts/ThemeContext';
import {AuthProvider} from './contexts/AuthContext';


function App()
{
    return (
        <ThemeProvider>
            <AuthProvider>
                <Router>
                    <Routes>
                        <Route path="/*" element={<MultiToolApp/>}/>
                    </Routes>
                </Router>
            </AuthProvider>
        </ThemeProvider>
    );
}

export default App;