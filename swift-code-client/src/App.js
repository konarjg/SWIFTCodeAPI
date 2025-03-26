import {React} from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import {Home} from './pages/Home';
import { GetSwiftCodeDetails } from './pages/GetSwiftCodeDetails';

export default function App() {
  return (
      <Router>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/swift-code-details" element={<GetSwiftCodeDetails/>} />
        </Routes>
      </Router>
  );
}
