import React from 'react';
import axios from 'axios';
import {useAuth} from './useAuth';

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

    return [getSwiftCodeDetails]
}