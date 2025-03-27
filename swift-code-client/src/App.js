import {React} from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import {Home} from './pages/Home';
import { GetSwiftCodeDetails } from './pages/GetSwiftCodeDetails';
import { GetSwiftCodesByCountry } from './pages/GetSwiftCodesByCountry';
import { AddSwiftCode } from './pages/AddSwiftCode';
import { RemoveSwiftCode } from './pages/RemoveSwiftCode';

export default function App() {
  return (
      <Router>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/swift-code-details" element={<GetSwiftCodeDetails/>} />
          <Route path="/swift-codes-by-country" element={<GetSwiftCodesByCountry />} />
          <Route path="/add-swift-code" element={<AddSwiftCode />} />
          <Route path="/remove-swift-code" element={<RemoveSwiftCode />} />
        </Routes>
      </Router>
  );
}
