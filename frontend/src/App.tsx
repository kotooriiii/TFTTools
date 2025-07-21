import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';
import { MultiToolApp } from './components/MultiToolApp';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/*" element={<MultiToolApp />} />
      </Routes>
    </Router>
  );
}

export default App;