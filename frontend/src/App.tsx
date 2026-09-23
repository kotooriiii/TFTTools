import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import './App.css';
import {MultiToolApp} from './components/MultiToolApp';
import {ThemeProvider} from './contexts/ThemeContext';
import {AuthProvider} from './contexts/AuthContext';


function App()
{
    return (
        <>
            {/* Shared goo filter for Button.tsx's outline blob hover effect - defined once here
                rather than per-button so multiple buttons never render duplicate element ids. */}
            <svg aria-hidden="true" focusable="false" className="absolute h-0 w-0">
                <filter id="btn-goo-filter">
                    <feGaussianBlur in="SourceGraphic" stdDeviation="7" result="blur"/>
                    <feColorMatrix in="blur" mode="matrix"
                                   values="1 0 0 0 0  0 1 0 0 0  0 0 1 0 0  0 0 0 22 -11" result="goo"/>
                    <feComposite in="SourceGraphic" in2="goo" operator="atop"/>
                </filter>
            </svg>
            <ThemeProvider>
                <AuthProvider>
                    <Router>
                        <Routes>
                            <Route path="/*" element={<MultiToolApp/>}/>
                        </Routes>
                    </Router>
                </AuthProvider>
            </ThemeProvider>
        </>
    );
}

export default App;