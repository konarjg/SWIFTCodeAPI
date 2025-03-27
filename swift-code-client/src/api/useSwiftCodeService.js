import React from 'react';
import axios from 'axios';
import {useAuth} from './useAuth';
import { GetSwiftCodesByCountry } from '../pages/GetSwiftCodesByCountry';

export function useSwiftCodeService() {
    const authenticate = useAuth();

    const getSwiftCodeDetails = async (swiftCode) => {
        const authToken = await authenticate();

        try {
            const response = await axios.get(`http://localhost:8080/v1/swift-codes/${swiftCode}`, {
                headers: {
                    Authorization: `Bearer ${authToken}`
                }
            });

            return response.data;
        } catch (error) {
            console.error(error);
            return null;
        }
    };

    const getSwiftCodesByCountry = async (countryISO2) => {
        const authToken = await authenticate();

        try {
            const response = await axios.get(`http://localhost:8080/v1/swift-codes/country/${countryISO2}`, {
                headers: {
                    Authorization: `Bearer ${authToken}`
                }
            });

            return response.data;
        } catch (error) {
            console.error(error);
            return null;
        }
    };

    const addSwiftCode = async(secret, swiftCodeData) => {
        const authToken = await authenticate(true, secret);

        try {
            const response = await axios.post(`http://localhost:8080/v1/swift-codes`, swiftCodeData, {
                headers: {
                    Authorization: `Bearer ${authToken}`
                }
            });

            return response.data;
        } catch (error) {
            console.error(error);
            return null;
        }
    };

    const removeSwiftCode = async(secret, swiftCode) => {
        const authToken = await authenticate(true, secret);

        try {
            const response = await axios.delete(`http://localhost:8080/v1/swift-codes/${swiftCode}`, {
                headers: {
                    Authorization: `Bearer ${authToken}`
                }
            });

            return response.data;
        } catch (error) {
            console.error(error);
            return null;
        }
    };

    return [getSwiftCodeDetails, getSwiftCodesByCountry, addSwiftCode, removeSwiftCode];
}